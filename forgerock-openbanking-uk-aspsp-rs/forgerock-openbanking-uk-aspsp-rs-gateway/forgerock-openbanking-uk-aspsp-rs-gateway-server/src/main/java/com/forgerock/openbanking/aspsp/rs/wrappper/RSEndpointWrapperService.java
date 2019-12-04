/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.wrappper;

import brave.Tracer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forgerock.openbanking.am.config.AMOpenBankingConfiguration;
import com.forgerock.openbanking.am.services.AMResourceServerService;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.BalanceTransferPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.MoneyTransferPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.api.payment.verifier.PaymPaymentValidator;
import com.forgerock.openbanking.aspsp.rs.wrappper.endpoints.*;
import com.forgerock.openbanking.common.conf.RSConfiguration;
import com.forgerock.openbanking.common.services.openbanking.OBHeaderCheckerService;
import com.forgerock.openbanking.common.services.store.accountrequest.AccountRequestStoreService;
import com.forgerock.openbanking.common.services.store.tpp.TppStoreService;
import com.forgerock.openbanking.common.services.token.AccessTokenService;
import com.forgerock.openbanking.jwt.services.CryptoApiClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
/**
 * Bulk endpoint framework class. It will handle for you every things that every bulk request endpoint
 * would need to do, leaving you the "main" function to implement. You will be able to implement in the main and
 * concentrate on what your specific endpoint needs to provide.
 */
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
                                    PaymPaymentValidator paymPaymentValidator
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
    }

    public AccountsAndTransactionsEndpointWrapper accountAndTransactionEndpoint() {
        return new AccountsAndTransactionsEndpointWrapper(this);
    }

    public AccountRequestsEndpointWrapper accountRequestEndpoint() {
        return new AccountRequestsEndpointWrapper(this);
    }

    public TransactionsEndpointWrapper transctionsEndpoint() {
        return new TransactionsEndpointWrapper(this);
    }

    public PaymentsApiEndpointWrapper paymentEndpoint() {
        return new PaymentsApiEndpointWrapper(this, balanceTransferPaymentValidator, moneyTransferPaymentValidator, paymPaymentValidator);
    }

    public FilePaymentsApiEndpointWrapper filePaymentEndpoint() {
        return new FilePaymentsApiEndpointWrapper(this);
    }

    public FundsConfirmationApiEndpointWrapper fundsConfirmationEndpoint() {
        return new FundsConfirmationApiEndpointWrapper(this);
    }

    public FundsConfirmationConsentApiEndpointWrapper fundsConfirmationConsentEndpoint() {
        return new FundsConfirmationConsentApiEndpointWrapper(this);
    }

    public PaymentsSubmissionsEndpointWrapper paymentSubmissionEndpoint() {
        return new PaymentsSubmissionsEndpointWrapper(this);
    }

    public EventNotificationsApiEndpointWrapper eventNotificationEndpoint() {
        return new EventNotificationsApiEndpointWrapper(this);
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
}
