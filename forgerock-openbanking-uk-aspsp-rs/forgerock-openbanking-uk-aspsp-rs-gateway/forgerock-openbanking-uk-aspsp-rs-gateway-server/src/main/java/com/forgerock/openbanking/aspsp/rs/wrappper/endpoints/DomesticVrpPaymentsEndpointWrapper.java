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
package com.forgerock.openbanking.aspsp.rs.wrappper.endpoints;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.OBRisk1Validator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.VrpExtensionValidator;
import com.forgerock.openbanking.aspsp.rs.wrappper.RSEndpointWrapperService;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPConsent;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRDomesticVRPControlParameters;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRPeriodicLimits;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRPeriodicLimits.PeriodAlignmentEnum;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRPeriodicLimits.PeriodTypeEnum;
import com.forgerock.openbanking.common.model.openbanking.persistence.vrp.FRWriteDomesticVRPDataInitiation;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.constants.OIDCConstants;
import com.forgerock.openbanking.constants.OpenBankingConstants;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import com.forgerock.openbanking.model.error.VRPErrorControlParametersFields;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import uk.org.openbanking.datamodel.payment.OBRisk1;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPInitiation;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBVRPFundsConfirmationRequest;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toOBDomesticVRPInitiation;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toOBDomesticVRPInitiationv3_1_10;
import static com.forgerock.openbanking.common.services.openbanking.converter.vrp.FRDomesticVRPConsentConverter.toOBRisk1;

@Slf4j
public class DomesticVrpPaymentsEndpointWrapper extends RSEndpointWrapper<DomesticVrpPaymentsEndpointWrapper, DomesticVrpPaymentsEndpointWrapper.DomesticVrpPaymentRestEndpointContent> {

    private FRDomesticVRPConsent consent;
    private final OBRisk1Validator riskValidator;
    private boolean isAuthorizationCodeGrantType;
    private boolean isModeTest;
    private final PeriodicLimitBreachResponseSimulator periodicLimitBreachResponseSimulator;

    private final VrpExtensionValidator extensionValidator;

    public DomesticVrpPaymentsEndpointWrapper(RSEndpointWrapperService RSEndpointWrapperService,
                                              TppStoreService tppStoreService,
                                              OBRisk1Validator riskValidator,
                                              VrpExtensionValidator extensionValidator) {
        super(RSEndpointWrapperService, tppStoreService);
        this.riskValidator = riskValidator;
        this.extensionValidator = extensionValidator;
        this.isAuthorizationCodeGrantType = false;
        this.isModeTest = false;
        this.periodicLimitBreachResponseSimulator = new PeriodicLimitBreachResponseSimulator();
    }

    public DomesticVrpPaymentsEndpointWrapper payment(FRDomesticVRPConsent consent) {
        this.consent = consent;
        return this;
    }

    public DomesticVrpPaymentsEndpointWrapper isAuthorizationCodeGrantType(boolean isAuthorizationCodeGrantType) {
        this.isAuthorizationCodeGrantType = isAuthorizationCodeGrantType;
        return this;
    }

    public DomesticVrpPaymentsEndpointWrapper isModeTest(boolean isModeTest) {
        this.isModeTest = isModeTest;
        return this;
    }


    @Override
    protected ResponseEntity run(DomesticVrpPaymentRestEndpointContent main) throws OBErrorException, JsonProcessingException {
        return main.run(oAuth2ClientId);
    }

    @Override
    protected void applyFilters() throws OBErrorException {
        List grantTypes = Arrays.asList(OIDCConstants.GrantType.CLIENT_CREDENTIAL);
        // the grant type for funds confirmation endpoint is different than the others payment endpoints
        if (isAuthorizationCodeGrantType) {
            grantTypes = Arrays.asList(
                    OIDCConstants.GrantType.AUTHORIZATION_CODE
            );
        }
        verifyAccessToken(Arrays.asList(OpenBankingConstants.Scope.PAYMENTS), grantTypes);
        verifyMatlsFromAccessToken();
    }

    public void validateRisk(OBRisk1 risk) throws OBErrorException {
        if (riskValidator != null) {
            riskValidator.validate(risk);
        } else {
            String errorString = "validatePaymentCodeContext called but no validator present";
            log.error(errorString);
            throw new NullPointerException(errorString);
        }
    }

    // The Initiation section must matches the values specified in the consent.
    public void checkRequestAndConsentInitiationMatch(OBDomesticVRPInitiation requestInitiation, FRDomesticVRPConsent consent)
            throws OBErrorException {
        FRWriteDomesticVRPDataInitiation consentFRInitiation = consent.getInitiation();
        OBDomesticVRPInitiation consentOBInitiation = toOBDomesticVRPInitiation(consentFRInitiation);
        if (!consentOBInitiation.equals(requestInitiation)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_INITIATION_DOESNT_MATCH_CONSENT);
        }
    }

    public void checkRequestAndConsentInitiationMatch(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPInitiation requestInitiation,
                                                      FRDomesticVRPConsent consent)
            throws OBErrorException {
        FRWriteDomesticVRPDataInitiation consentFRInitiation = consent.getInitiation();
        uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPInitiation consentOBInitiation = toOBDomesticVRPInitiationv3_1_10(consentFRInitiation);
        if (!consentOBInitiation.equals(requestInitiation)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_INITIATION_DOESNT_MATCH_CONSENT);
        }
    }

    // The Risk section must matches the values specified in the consent.
    public void checkRequestAndConsentRiskMatch(OBDomesticVRPRequest request, FRDomesticVRPConsent frConsent)
            throws OBErrorException {
        OBRisk1 requestRisk = request.getRisk();
        OBRisk1 consentRisk = toOBRisk1(frConsent.getRisk());
        if (!requestRisk.equals(consentRisk)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_RISK_DOESNT_MATCH_CONSENT);
        }
    }


    // TODO - Review: v3.1.10 support, required to workaround breaking changes in OB spec
    public void checkRequestAndConsentRiskMatch(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest request,
                                                FRDomesticVRPConsent frConsent)
            throws OBErrorException {
        OBRisk1 requestRisk = request.getRisk();
        OBRisk1 consentRisk = toOBRisk1(frConsent.getRisk());
        if (!requestRisk.equals(consentRisk)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_RISK_DOESNT_MATCH_CONSENT);
        }
    }

    // If the CreditorAccount was not specified in the consent, the CreditorAccount must be specified in the instruction
    public void checkCreditorAccountIsInInstructionIfNotInConsent(OBDomesticVRPRequest vrpRequest,
                                                                  FRDomesticVRPConsent frConsent) throws OBErrorException {
        if (frConsent.getVrpDetails().getData().getInitiation().getCreditorAccount() == null) {
            if (vrpRequest.getData().getInstruction().getCreditorAccount() == null) {
                throw new OBErrorException(OBRIErrorType.REQUEST_VRP_CREDITOR_ACCOUNT_NOT_SPECIFIED);
            }
        }
    }

    public void checkCreditorAccountIsInInstructionIfNotInConsent(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest vrpRequest,
                                                                  FRDomesticVRPConsent frConsent) throws OBErrorException {
        if (frConsent.getVrpDetails().getData().getInitiation().getCreditorAccount() == null) {
            if (vrpRequest.getData().getInstruction().getCreditorAccount() == null) {
                throw new OBErrorException(OBRIErrorType.REQUEST_VRP_CREDITOR_ACCOUNT_NOT_SPECIFIED);
            }
        }
    }

    /**
     * When a payment would breach a limitation set by one or more ControlParameters, the ASPSP must return an error
     * with code UK.OBIE.Rules.FailsControlParameters and pass in the control parameter field that caused the error.
     *
     * The following checks are supported:
     * - validating that the requested payment amount is less than the configured maximum individual amount
     * - Optionally, simulating a periodic payment limit breach, if xVrpLimitBreachResponseSimulation param is supplied.
     */
    public void checkControlParameters(OBDomesticVRPRequest vrpRequest, FRDomesticVRPConsent frConsent,
                                       String xVrpLimitBreachResponseSimulation) throws OBErrorException {
        // TODO Shall we validate the instructed amount against the control parameter periodic limits?
        validateMaximumIndividualAmount(vrpRequest, frConsent);
        if (xVrpLimitBreachResponseSimulation != null) {
            periodicLimitBreachResponseSimulator.processRequest(xVrpLimitBreachResponseSimulation, consent);
        }
    }

    public void checkControlParameters(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest vrpRequest,
                                       FRDomesticVRPConsent frConsent, String xVrpLimitBreachResponseSimulation) throws OBErrorException {
        // TODO Shall we validate the instructed amount against the control parameter periodic limits?
        validateMaximumIndividualAmount(vrpRequest, frConsent);
        if (xVrpLimitBreachResponseSimulation != null) {
            periodicLimitBreachResponseSimulator.processRequest(xVrpLimitBreachResponseSimulation, consent);
        }
    }

    private void validateMaximumIndividualAmount(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest vrpRequest,
                                                 FRDomesticVRPConsent frConsent) throws OBErrorException {
        Double requestAmount = Double.valueOf(vrpRequest.getData().getInstruction().getInstructedAmount().getAmount());
        validateMaximumIndividualAmount(frConsent, requestAmount);
    }

    private void validateMaximumIndividualAmount(OBDomesticVRPRequest vrpRequest, FRDomesticVRPConsent frConsent) throws OBErrorException {
        Double requestAmount = Double.valueOf(vrpRequest.getData().getInstruction().getInstructedAmount().getAmount());
        validateMaximumIndividualAmount(frConsent, requestAmount);
    }

    private void validateMaximumIndividualAmount(FRDomesticVRPConsent frConsent, Double requestAmount) throws OBErrorException {
        FRDomesticVRPControlParameters controlParameters = frConsent.getVrpDetails().getData().getControlParameters();
        Double consentAmount = Double.valueOf(controlParameters.getMaximumIndividualAmount().getAmount());
        if(requestAmount.compareTo(consentAmount) > 0){
            throw new OBErrorException(
                    OBRIErrorType.REQUEST_VRP_CONTROL_PARAMETERS_RULES,
                    VRPErrorControlParametersFields.RequestControlFields.MAX_INDIVIDUAL_AMOUNT,
                    VRPErrorControlParametersFields.ConsentControlFields.MAX_INDIVIDUAL_AMOUNT);
        }
    }

    public void applyExtendedConsentValidation(OBDomesticVRPConsentRequest obDomesticVRPConsentRequest) throws OBErrorException {
        extensionValidator.validateConsent(obDomesticVRPConsentRequest);
    }

    public void applyExtendedPaymentRequestValidation(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest vrpRequest) throws OBErrorException {
        extensionValidator.validatePaymentRequest(vrpRequest);
    }

    public void applyExtendedFundsConfirmationRequestValidation(OBVRPFundsConfirmationRequest fundsConfirmationRequest) throws OBErrorException {
        extensionValidator.validateFundsConfirmationRequest(fundsConfirmationRequest);
    }

    public interface DomesticVrpPaymentRestEndpointContent {
        ResponseEntity run(String tppId) throws OBErrorException;
    }

    /**
     * Simulates VRP payment breaches for PeriodicLimits specified in the consent.
     *
     * This allows TPPs to test their error handling for this condition, the simulator can be triggered by specifying
     * a custom header on the payment request.
     */
    static class PeriodicLimitBreachResponseSimulator {
        private static final Set<String> LIMIT_BREACH_HEADER_VALUES;
        static {
            final Set<String> limitBreaches = new HashSet<>();
            for (PeriodTypeEnum periodType : PeriodTypeEnum.values()) {
                for (PeriodAlignmentEnum periodAlignment : PeriodAlignmentEnum.values()) {
                    limitBreaches.add(periodType.getValue() + "-" + periodAlignment.getValue());
                }
            }
            LIMIT_BREACH_HEADER_VALUES = Collections.unmodifiableSet(limitBreaches);
        }

        void processRequest(String xVrpLimitBreachResponseSimulation, FRDomesticVRPConsent consent) throws OBErrorException {
            if (LIMIT_BREACH_HEADER_VALUES.contains(xVrpLimitBreachResponseSimulation)) {
                final FRPeriodicLimits periodicLimits = findPeriodicLimitsForHeader(xVrpLimitBreachResponseSimulation, consent);
                simulateLimitBreachResponse(periodicLimits);
            } else {
                throw new OBErrorException(OBRIErrorType.REQUEST_VRP_LIMIT_BREACH_SIMULATION_INVALID_HEADER_VALUE,
                                           xVrpLimitBreachResponseSimulation);
            }
        }

        private FRPeriodicLimits findPeriodicLimitsForHeader(String xVrpLimitBreachResponseSimulation,
                                                             FRDomesticVRPConsent consent) throws OBErrorException {
            final List<FRPeriodicLimits> periodicLimits = consent.getVrpDetails().getData().getControlParameters().getPeriodicLimits();
            if (periodicLimits != null) {
                final int separatorIndex = xVrpLimitBreachResponseSimulation.indexOf('-');
                final String periodType = xVrpLimitBreachResponseSimulation.substring(0, separatorIndex);
                final String periodAlignment = xVrpLimitBreachResponseSimulation.substring(separatorIndex + 1);
                for (FRPeriodicLimits periodicLimit : periodicLimits) {
                    if (periodicLimit.getPeriodAlignment().getValue().equals(periodAlignment)
                            && periodicLimit.getPeriodType().getValue().equals(periodType)) {
                        return periodicLimit;
                    }
                }
            }
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_LIMIT_BREACH_SIMULATION_NO_MATCHING_LIMIT_IN_CONSENT,
                                       xVrpLimitBreachResponseSimulation);
        }

        private void simulateLimitBreachResponse(FRPeriodicLimits periodicLimits) throws OBErrorException {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_CONTROL_PARAMETERS_PAYMENT_PERIODIC_LIMIT_BREACH,
                                       periodicLimits.getAmount(), periodicLimits.getCurrency(),
                                       periodicLimits.getPeriodType(), periodicLimits.getPeriodAlignment());
        }
    }
}
