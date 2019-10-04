/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.error;

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uk.org.openbanking.datamodel.error.OBError1;
import uk.org.openbanking.datamodel.error.OBErrorResponse1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Autowired
    private Tracer tracer;
    @Value("${doc.errors}")
    private String docErrorsUrl;

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        List<OBError1> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(OBRIErrorType.REQUEST_FIELD_INVALID
                    .toOBError1(error.getDefaultMessage())
                    .path(error.getField())
            );
        }
        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(OBRIErrorType.REQUEST_OBJECT_INVALID
                    .toOBError1(error.getDefaultMessage())
                    .path(error.getObjectName())
            );
        }

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.ARGUMENT_INVALID,
                        errors),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(
            MissingServletRequestParameterException ex, HttpHeaders headers,
            HttpStatus status, WebRequest request) {

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_PARAMETER_MISSING
                                .toOBError1(ex.getParameterName())
                                .path(ex.getParameterName())),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleServletRequestBindingException(
            ServletRequestBindingException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        if (ex.getMessage().startsWith("Missing request header")) {
            return handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.BAD_REQUEST,
                            OBRIErrorResponseCategory.REQUEST_INVALID,
                            OBRIErrorType.REQUEST_MISSING_HEADER
                                    .toOBError1(ex.getMessage())
                    ),
                    request);
        } else if (ex.getMessage().startsWith("Missing cookie")) {
            return handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.BAD_REQUEST,
                            OBRIErrorResponseCategory.REQUEST_INVALID,
                            OBRIErrorType.REQUEST_MISSING_COOKIE
                                    .toOBError1(ex.getMessage())
                    ),
                    request);
        } else if (ex.getMessage().startsWith("Missing argument")) {
            return handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.BAD_REQUEST,
                            OBRIErrorResponseCategory.REQUEST_INVALID,
                            OBRIErrorType.REQUEST_MISSING_ARGUMENT
                                    .toOBError1(ex.getMessage())
                    ),
                    request);
        }

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_BINDING_FAILED
                                .toOBError1(ex.getLocalizedMessage())
                ),
                request);
    }


    @ExceptionHandler({ MethodArgumentTypeMismatchException.class })
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(
            MethodArgumentTypeMismatchException ex, WebRequest request) {
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_ARGUMENT_TYPE_MISMATCH
                                .toOBError1(ex.getName(), ex.getRequiredType().getName())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(
            HttpRequestMethodNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedHttpMethods().forEach(t -> builder.append("'").append(t).append("' "));

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_METHOD_NOT_SUPPORTED
                                .toOBError1(ex.getMethod(), builder.toString())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotSupported(
            HttpMediaTypeNotSupportedException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(t -> builder.append("'").append(t).append("' "));

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_MEDIA_TYPE_NOT_SUPPORTED
                                .toOBError1(ex.getContentType(), builder.toString())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.debug("HttpMessageNotReadableException from request: {}", request, ex);
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        status,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_MESSAGE_NOT_READABLE
                                .toOBError1((ex.getCause()!=null) ? ex.getCause().getMessage() : ex.getMessage())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMediaTypeNotAcceptable(
            HttpMediaTypeNotAcceptableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        StringBuilder builder = new StringBuilder();
        ex.getSupportedMediaTypes().forEach(t -> builder.append("'").append(t).append("' "));

        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.UNSUPPORTED_MEDIA_TYPE,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_MEDIA_TYPE_NOT_ACCEPTABLE
                                .toOBError1(builder.toString())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingPathVariable(
            MissingPathVariableException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        status,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_PATH_VARIABLE_MISSING
                                .toOBError1(ex.getVariableName(), ex.getParameter())
                ),
                request);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {
        if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
            handleOBErrorResponse(
                    new OBErrorResponseException(
                            HttpStatus.INTERNAL_SERVER_ERROR,
                            OBRIErrorResponseCategory.SERVER_INTERNAL_ERROR,
                            OBRIErrorType.SERVER_ERROR
                                    .toOBError1()
                    ),
                    request);
        }
        handleOBErrorResponse(
                new OBErrorResponseException(
                        status,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.REQUEST_UNDEFINED_ERROR_YET
                                .toOBError1(ex.getMessage())
                ),
                request);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler(value = { OBErrorResponseException.class })
    protected ResponseEntity<Object> handleOBErrorResponse(
            OBErrorResponseException ex, WebRequest request) {

        return ResponseEntity.status(ex.getStatus()).body(
                new OBErrorResponse1()
                        .code(ex.getCategory().getId())
                        .id(ex.getId() != null ? ex.getId() : String.valueOf(tracer.currentSpan().context().traceIdString()))
                        .message(ex.getCategory().getDescription())
                        .errors(ex.getErrors().stream().map(e -> e.url(docErrorsUrl + "#" +
                                e.getErrorCode() // This needs to be the ERROR CODE enum not value or else we must change the errors docunent
                        )).collect(Collectors.toList())));
    }


    @ExceptionHandler(value = { OBErrorException.class})
    protected ResponseEntity<Object> handleOBError(
            OBErrorException ex, WebRequest request) {
        HttpStatus httpStatus = ex.getObriErrorType().getHttpStatus();
        return ResponseEntity.status(httpStatus).body(
                new OBErrorResponse1()
                        .code(httpStatus.name())
                        .id(String.valueOf(tracer.currentSpan().context().traceIdString()))
                        .message(httpStatus.getReasonPhrase())
                        .errors(Collections.singletonList(ex.getOBError())));
    }

    // Required here because these programming errors can get lost in Spring handlers making debug very difficult
    @ExceptionHandler(value = { IllegalArgumentException.class})
    protected ResponseEntity<Object> handleIllegalArgument(
            IllegalArgumentException ex, WebRequest request) {
        log.error("Internal server error from an IllegalArgumentException", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(httpStatus).body(
                new OBErrorResponse1()
                        .code(httpStatus.name())
                        .id(String.valueOf(tracer.currentSpan().context().traceIdString()))
                        .message(httpStatus.getReasonPhrase()));
    }

    @ExceptionHandler(value = { HttpMessageConversionException.class})
    protected ResponseEntity<Object> handleHttpMessageConversionException(
            HttpMessageConversionException ex, WebRequest request) {
        log.debug("An invalid resource format ", ex);
        return handleOBErrorResponse(
                new OBErrorResponseException(
                        HttpStatus.BAD_REQUEST,
                        OBRIErrorResponseCategory.REQUEST_INVALID,
                        OBRIErrorType.INVALID_RESOURCE
                                .toOBError1((ex.getCause()!=null) ?
                                        ((ex.getCause().getCause() !=null) ? ex.getCause().getCause().getMessage():
                                                ex.getCause().getMessage())
                                        : ex.getMessage())
                ),
                request);
    }

    // Handle any 400 error from a downstream REST service
    @ExceptionHandler(value = { HttpClientErrorException.class})
    protected ResponseEntity<OBErrorResponse1> handleHttpClientError(
            HttpClientErrorException ex, WebRequest request) {
        log.debug("HTTP client error exception from rs store", ex);
        try {
            /*
             * Quick way to handle the OB rs-store errors in rs-api with minimal impact on code. Currently, when a legitimate validation error is found in rs-store, an OBErrorException is thrown.
             * However, this gets wrapped into an HttpClientErrorException by Spring Rest client meaning JSON error format is lost and error code comes out of rs-api as 500.
             * This handler code attempts to parse the HttpClientErrorException message as an OBError so it can be returned as a HTTP Response.
             * If there exception message is not an OBErrorResponse then it will be parsed as a generic 4xx response
             */
            OBErrorResponse1 obErrorResponse1 = new ObjectMapper().readValue(ex.getStatusText(), OBErrorResponse1.class);
            log.debug("Parsed OBErrorResponse: {} from HttpClientErrorException message", ex);
            return ResponseEntity.status(ex.getStatusCode()).body(obErrorResponse1);
        } catch (Exception e) {
            log.debug("HttpClientErrorException is not an OBErrorResponse1.");
            /* HttpClientErrorException is not an OBErrorResponse1. but a generic 4xx failure from framework.
            It should have been rejected by TPP-facing API and not fail in a downstream microservice unless it is an internal issue.
            Therefore we can just rethrow it and will be handled as before. */
            throw ex;
        }
    }

}
