/**
 * Copyright 2019 ForgeRock AS.
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper;

import brave.Tracer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.aspsp.rs.api.payment.DetachedJwsGenerator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.BalanceTransferPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.DefaultVrpExtensionValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.DetachedJwsVerifier;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.MoneyTransferPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.OBRisk1Validator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.PaymPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.VrpExtensionValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.VrpExtensionValidator.NoOpVrpExtensionValidator;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.*;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.model.version.OBVersion;
import com.forgerock.openbanking.common.services.openbanking.OBHeaderCheckerService;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.services.token.AccessTokenService;
import com.forgerock.openbanking.exceptions.OBErrorResponseException;
import com.forgerock.openbanking.jwt.exceptions.InvalidTokenException;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import com.nimbusds.jwt.SignedJWT;
import lombok.Data;
import com.forgerock.openbanking.model.error.OBRIErrorResponseCategory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;

import static com.forgerock.openbanking.model.error.OBRIErrorType.SERVER_ERROR;


/**
 * Bulk endpoint framework class. It will handle for you every things that every bulk request endpoint
 * would need to do, leaving you the "main" function to implement. You will be able to implement in the main and
 * concentrate on what your specific endpoint needs to provide.
 */
@Service
@Slf4j
@Data
public class RSEndpointWrapperService {

    public OBHeaderCheckerService obHeaderCheckerService;
    public CryptoApiClient cryptoApiClient;
    public AMResourceServerService amResourceServerService;
    public AccessTokenService accessTokenService;
    public AccountRequestStoreService accountRequestStore;
    public RSConfiguration rsConfiguration;
    public Tracer tracer;
    public ObjectMapper mapper;
    public String tan;
    public String financialId;
    public AMOpenBankingConfiguration amOpenBankingConfiguration;
    public TppStoreService tppStoreService;
    public boolean isDetachedSignatureEnable;
    public BalanceTransferPaymentValidator balanceTransferPaymentValidator;
    public MoneyTransferPaymentValidator moneyTransferPaymentValidator;
    public PaymPaymentValidator paymPaymentValidator;
    public OBRisk1Validator riskValidator;
    public DetachedJwsVerifier detachedJwsVerifier;
    private DetachedJwsGenerator detachedJwsGenerator;

    private final VrpExtensionValidator vrpExtensionValidator;

    @Autowired
    public RSEndpointWrapperService(OBHeaderCheckerService obHeaderCheckerService,
                                    CryptoApiClient cryptoApiClient,
                                    AccessTokenService accessTokenService,
                                    AccountRequestStoreService accountRequestStore,
                                    RSConfiguration rsConfiguration,
                                    Tracer tracer,
                                    ObjectMapper mapper,
                                    @Value("${rs.detached-signature.enable}") boolean isDetachedSignatureEnable,
                                    @Value("${rs.tan}") String tan,
                                    @Value("${rs.financial_id}") String financialId,
                                    AMOpenBankingConfiguration amOpenBankingConfiguration,
                                    TppStoreService tppStoreService,
                                    BalanceTransferPaymentValidator balanceTransferPaymentValidator,
                                    MoneyTransferPaymentValidator moneyTransferPaymentValidator,
                                    AMResourceServerService amResourceServerService,
                                    PaymPaymentValidator paymPaymentValidator,
                                    OBRisk1Validator riskValidator,
                                    DetachedJwsVerifier detachedJwsVerifier,
                                    DetachedJwsGenerator detachedJwsGenerator,
                                    @Value("${rs.vrp.extended.validation.enable:false}") boolean enabledExtendedVrpValidation
    ) {
        this.obHeaderCheckerService = obHeaderCheckerService;
        this.cryptoApiClient = cryptoApiClient;
        this.accessTokenService = accessTokenService;
        this.accountRequestStore = accountRequestStore;
        this.rsConfiguration = rsConfiguration;
        this.tracer = tracer;
        this.mapper = mapper;
        this.tan = tan;
        this.financialId = financialId;
        this.amOpenBankingConfiguration = amOpenBankingConfiguration;
        this.tppStoreService = tppStoreService;
        this.isDetachedSignatureEnable = isDetachedSignatureEnable;
        this.balanceTransferPaymentValidator = balanceTransferPaymentValidator;
        this.moneyTransferPaymentValidator = moneyTransferPaymentValidator;
        this.paymPaymentValidator = paymPaymentValidator;
        this.amResourceServerService = amResourceServerService;
        this.riskValidator = riskValidator;
        this.detachedJwsVerifier = detachedJwsVerifier;
        this.detachedJwsGenerator = detachedJwsGenerator;
        this.vrpExtensionValidator = enabledExtendedVrpValidation ? new DefaultVrpExtensionValidator() : new NoOpVrpExtensionValidator();
    }

    public AccountsAndTransactionsEndpointWrapper accountAndTransactionEndpoint() {
        return new AccountsAndTransactionsEndpointWrapper(this, tppStoreService);
    }

    public AccountRequestsEndpointWrapper accountRequestEndpoint() {
        return new AccountRequestsEndpointWrapper(this, tppStoreService);
    }

    public TransactionsEndpointWrapper transctionsEndpoint() {
        return new TransactionsEndpointWrapper(this, tppStoreService);
    }

    public PaymentsApiEndpointWrapper paymentEndpoint() {
        return new PaymentsApiEndpointWrapper(this, tppStoreService, balanceTransferPaymentValidator,
                moneyTransferPaymentValidator,
                paymPaymentValidator, riskValidator);
    }

    public DomesticVrpPaymentsEndpointWrapper vrpPaymentEndpoint(){
        return new DomesticVrpPaymentsEndpointWrapper(this, tppStoreService, riskValidator, vrpExtensionValidator);
    }

    public FilePaymentsApiEndpointWrapper filePaymentEndpoint() {
        return new FilePaymentsApiEndpointWrapper(this, tppStoreService);
    }

    public FundsConfirmationApiEndpointWrapper fundsConfirmationEndpoint() {
        return new FundsConfirmationApiEndpointWrapper(this, tppStoreService);
    }

    public FundsConfirmationConsentApiEndpointWrapper fundsConfirmationConsentEndpoint() {
        return new FundsConfirmationConsentApiEndpointWrapper(this, tppStoreService);
    }

    public PaymentsSubmissionsEndpointWrapper paymentSubmissionEndpoint() {
        return new PaymentsSubmissionsEndpointWrapper(this, tppStoreService);
    }

    public PaymentsRequestPaymentIdEndpointWrapper paymentsRequestPaymentIdEndpoint() {
        return new PaymentsRequestPaymentIdEndpointWrapper(this, tppStoreService);
    }

    public EventNotificationsApiEndpointWrapper eventNotificationEndpoint() {
        return new EventNotificationsApiEndpointWrapper(this, tppStoreService);
    }

    public AggregatedPollingApiEndpointWrapper aggregatedPollingEndpoint() {
        return new AggregatedPollingApiEndpointWrapper(this, tppStoreService);
    }

    public CustomerInfoApiEndpointWrapper getCustomerInfoEndpointWrapper() {
        return new CustomerInfoApiEndpointWrapper(this, tppStoreService);
    }

    public CryptoApiClient getCryptoApiClient() {
        return cryptoApiClient;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }

    public String getTan() {
        return tan;
    }

    public String getFinancialId() {
        return financialId;
    }

    public SignedJWT verifyAccessToken(String accessTokenBearer) throws ParseException, InvalidTokenException, IOException {
        return amResourceServerService.verifyAccessToken(accessTokenBearer);
    }

    public boolean verifyFinancialIdHeader(String xFapiFinancialId) {
        return this.obHeaderCheckerService.verifyFinancialIdHeader(xFapiFinancialId);
    }

    public String generateDetachedJws(ResponseEntity response, OBVersion obVersion, String tan, String xFapiFinancialId)
            throws OBErrorResponseException {
        try {
            return this.detachedJwsGenerator.generateDetachedJws(response, obVersion, tan, xFapiFinancialId);
        } catch (JsonProcessingException e) {
            log.warn("Failed to process JSON response", e);
            throw new OBErrorResponseException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    OBRIErrorResponseCategory.REQUEST_FILTER,
                    SERVER_ERROR.toOBError1());
        }
    }


}
