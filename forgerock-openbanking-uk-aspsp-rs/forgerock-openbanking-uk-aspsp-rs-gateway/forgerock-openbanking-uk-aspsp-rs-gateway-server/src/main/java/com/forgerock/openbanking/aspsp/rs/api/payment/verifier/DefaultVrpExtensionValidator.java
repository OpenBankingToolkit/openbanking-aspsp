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
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import com.forgerock.openbanking.common.utils.AmountDecimalPlaceValidator;
import com.forgerock.openbanking.exceptions.OBErrorException;
import com.forgerock.openbanking.model.error.OBRIErrorType;
import org.joda.time.DateTime;
import uk.org.openbanking.datamodel.payment.OBExternalPaymentContext1Code;
import uk.org.openbanking.datamodel.vrp.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.vrp.OBDomesticVRPControlParametersPeriodicLimits;
import uk.org.openbanking.datamodel.vrp.namespace.OBVRPConsentType;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPConsentRequest;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPControlParameters;
import uk.org.openbanking.datamodel.vrp.v3_1_10.OBVRPFundsConfirmationRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

/**
 * Default set of extension validation rules for VRP payments.
 */
public class DefaultVrpExtensionValidator implements VrpExtensionValidator {

    private static final String UK_OBIE_SCA_NOT_REQUIRED = "UK.OBIE.SCANotRequired";

    private Set<OBExternalPaymentContext1Code> validPaymentContextCodes;

    public DefaultVrpExtensionValidator() {
        validPaymentContextCodes = Set.of(OBExternalPaymentContext1Code.PARTYTOPARTY,
                                          OBExternalPaymentContext1Code.TRANSFERTOSELF);
    }

    public void setValidPaymentContextCodes(Set<OBExternalPaymentContext1Code> validPaymentContextCodes) {
        this.validPaymentContextCodes = validPaymentContextCodes;
    }

    @Override
    public void validatePaymentRequest(uk.org.openbanking.datamodel.vrp.v3_1_10.OBDomesticVRPRequest request) throws OBErrorException {
        checkMaximum2DecimalPlaces("InstructedAmount", request.getData().getInstruction().getInstructedAmount().getAmount());
    }

    @Override
    public void validateConsent(OBDomesticVRPConsentRequest consentRequest) throws OBErrorException {
        validateControlParameters(consentRequest.getData().getControlParameters());
        validatePaymentContextCodes(consentRequest);
    }

    @Override
    public void validateFundsConfirmationRequest(OBVRPFundsConfirmationRequest fundsConfirmationRequest) throws OBErrorException {
        checkMaximum2DecimalPlaces("InstructedAmount", fundsConfirmationRequest.getData().getInstructedAmount().getAmount());
    }

    private void validateMaximumIndividualAmount(OBActiveOrHistoricCurrencyAndAmount maximumIndividualAmount) throws OBErrorException {
        checkMaximum2DecimalPlaces("MaximumIndividualAmount", maximumIndividualAmount.getAmount());
        final BigDecimal minimumAmount = BigDecimal.ONE;
        if (new BigDecimal(maximumIndividualAmount.getAmount()).compareTo(minimumAmount) < 0) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_MAX_INDIVIDUAL_AMOUNT_TOO_SMALL, minimumAmount);
        }
    }

    private void validateControlParameters(OBDomesticVRPControlParameters controlParameters) throws OBErrorException {
        validatePeriodicLimits(controlParameters.getPeriodicLimits());
        validateVrpTypes(controlParameters.getVrPType());
        validatePsuAuthenticationMethods(controlParameters.getPsUAuthenticationMethods());
        validateToFromDates(controlParameters.getValidFromDateTime(), controlParameters.getValidToDateTime());
        validateMaximumIndividualAmount(controlParameters.getMaximumIndividualAmount());
    }

    private void validateToFromDates(DateTime validFromDate, DateTime validToDate) throws OBErrorException {
        if (!validToDate.isAfter(validFromDate)) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_CONSENT_VALID_TO_DATE_INVALID);
        }
        final int maxDaysInFuture = 31;
        if (validFromDate.isAfter(DateTime.now().plusDays(maxDaysInFuture))) {
           throw new OBErrorException(OBRIErrorType.REQUEST_VRP_CONSENT_VALID_FROM_DATE_INVALID, maxDaysInFuture);
        }
    }

    private void validatePsuAuthenticationMethods(List<String> psUAuthenticationMethods) throws OBErrorException {
        if (psUAuthenticationMethods.size() > 1 || !UK_OBIE_SCA_NOT_REQUIRED.equals(psUAuthenticationMethods.get(0))) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_PSU_AUTHENTICATION_METHODS_INVALID);
        }
    }

    private void validateVrpTypes(List<String> vrpTypes) throws OBErrorException {
        if (vrpTypes.size() > 1 || !OBVRPConsentType.SWEEPING.getValue().equals(vrpTypes.get(0))) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_TYPE_MUST_BE_SWEEPING);
        }
    }

    private void validatePeriodicLimits(List<OBDomesticVRPControlParametersPeriodicLimits> periodicLimits) throws OBErrorException {
        for (OBDomesticVRPControlParametersPeriodicLimits periodicLimit : periodicLimits) {
            checkMaximum2DecimalPlaces("PeriodicLimits.Amount", periodicLimit.getAmount());
        }
    }

    private void validatePaymentContextCodes(OBDomesticVRPConsentRequest consentRequest) throws OBErrorException {
        if (consentRequest.getRisk().getPaymentContextCode() == null || !validPaymentContextCodes.contains(consentRequest.getRisk().getPaymentContextCode())) {
            throw new OBErrorException(OBRIErrorType.REQUEST_VRP_RISK_PAYMENT_CONTEXT_CODE_INVALID);
        }
    }

    private void checkMaximum2DecimalPlaces(String fieldName, String amount) throws OBErrorException {
        if (!AmountDecimalPlaceValidator.validateMax2DecimalPlaces(amount)) {
            throw new OBErrorException(
                    OBRIErrorType.REQUEST_AMOUNT_MAX_2_DP,
                    fieldName);
        }
    }
}
