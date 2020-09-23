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
package com.forgerock.openbanking.aspsp.rs.store.api.openbanking.account.cdr.v1_0;

import com.forgerock.openbanking.aspsp.rs.model.v0_9.*;
import com.forgerock.openbanking.aspsp.rs.store.repository.v1_1.accounts.balances.FRBalance1Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.accounts.FRAccount3Repository;
import com.forgerock.openbanking.aspsp.rs.store.repository.v3_1_1.accounts.transactions.FRTransaction5Repository;
import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtilCdr;
import com.forgerock.openbanking.common.model.openbanking.persistence.v1_1.account.FRBalance1;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_1.account.FRAccount3;
import com.forgerock.openbanking.common.model.openbanking.persistence.v3_1_1.account.FRTransaction5;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import uk.org.openbanking.datamodel.account.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static uk.org.openbanking.datamodel.account.OBBalanceType1Code.INTERIMAVAILABLE;

@RestController
@Slf4j
public class BankingApiController implements BankingApi {

    @Autowired
    private FRAccount3Repository frAccount3Repository;
    @Autowired
    private FRBalance1Repository frBalance1Repository;
    @Value("${rs.page.default.balances.size}")
    private int PAGE_LIMIT_BALANCES;

    @Value("${rs.page.default.transaction.size}")
    private int PAGE_LIMIT_TRANSACTIONS;
    @Autowired
    private FRTransaction5Repository frTransactionRepository;
    @Override
    public ResponseEntity<ResponseBankingAccountList> listAccounts(
            String xV,
            @Valid String productCategory,
            @Valid String openStatus,
            @Valid Boolean isOwned,
            @Valid Integer page,
            @Valid Integer pageSize,
            String xMinV,
            String xFapiInteractionId,
            String xFapiAuthDate,
            String xFapiCustomerIpAddress,
            String xCdsUserAgent,
            String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl
    ) {
        log.info("Read accounts {} with permission {}", accountIds, permissions);

        List<BankingAccount> accounts = frAccount3Repository.byAccountIds(accountIds, permissions)
                .stream()
                .map(this::convertAccounts)
                .collect(Collectors.toList());

        return ResponseEntity.ok(new ResponseBankingAccountList()
                .data(new ResponseBankingAccountListData().accounts(accounts))
                .links(PaginationUtilCdr.generateLinksPaginatedOnePager(httpUrl))
                .meta(PaginationUtilCdr.generatePaginatedMetaData(
                        1)));
    }

    protected BankingAccount convertAccounts(FRAccount3 account) {

        BankingAccount bankingAccount = new BankingAccount();
        bankingAccount.accountId(account.getId());
        bankingAccount.setCreationDate(account.getCreated().toInstant().toString());
        bankingAccount.displayName(account.getAccount().getNickname());
        bankingAccount.openStatus(BankingAccount.OpenStatusEnum.OPEN);
        bankingAccount.maskedNumber(account.getAccount().getAccount().get(0).getIdentification());
        bankingAccount.productCategory(BankingProductCategory.TRANS_AND_SAVINGS_ACCOUNTS);
        bankingAccount.productName(BankingProductCategory.TRANS_AND_SAVINGS_ACCOUNTS.toString());
        return bankingAccount;
    }

    @Override
    public ResponseEntity<ResponseBankingAccountById> getAccountDetail(String accountId, String xV, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        log.info("Read account {} with permission {}", accountId, permissions);
        FRAccount3 account3 = frAccount3Repository.byAccountId(accountId, permissions);

        BankingAccountDetail bankingAccountDetail = new BankingAccountDetail()
                .bsb(account3.getAccount().getAccount().get(0).getIdentification().substring(0, 6))
                .specificAccountUType(BankingAccountDetail.SpecificAccountUTypeEnum.CREDITCARD)
        ;

        return ResponseEntity.ok(new ResponseBankingAccountById()
                .data(bankingAccountDetail)
                .links(PaginationUtilCdr.generateLinksOnePager(httpUrl))
                .meta(PaginationUtilCdr.generateMetaData()));
    }

    @Override
    public ResponseEntity<ResponseBankingPayeeById> getPayeeDetail(String payeeId, String xV, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingProductById> getProductDetail(String productId, String xV, String xMinV,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingTransactionById> getTransactionDetail(String accountId, String transactionId, String xV, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingTransactionList> getTransactions(String accountId, String xV, @Valid String oldestTime, @Valid String newestTime, @Valid String minAmount, @Valid String maxAmount, @Valid String text, @Valid Integer page, @Valid Integer pageSize, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {

        DateTime toBookingDateTime = DateTime.now();
        DateTime fromBookingDateTime = toBookingDateTime.minusYears(100);

        Page<FRTransaction5> response = frTransactionRepository.byAccountIdAndBookingDateTimeBetweenWithPermissions(accountId,
                fromBookingDateTime, toBookingDateTime, permissions, PageRequest.of(page, PAGE_LIMIT_TRANSACTIONS, Sort.Direction.ASC, "bookingDateTime"));

        List<BankingTransaction> transactions = response.getContent()
                .stream()
                .map(t -> {
                    BankingTransaction bankingTransaction = new BankingTransaction()
                            .accountId(accountId)
                            .transactionId(t.getId())
                            .isDetailAvailable(true)
                            .type(BankingTransaction.TypeEnum.PAYMENT)
                            .status(BankingTransaction.StatusEnum.POSTED)
                            .description(t.getTransaction().getTransactionInformation())
                            .postingDateTime(t.getBookingDateTime().toDateTimeISO().toString())
                            .executionDateTime(t.getTransaction().getValueDateTime().toDateTimeISO().toString())
                            .amount(t.getTransaction().getAmount().getAmount())
                            .currency(t.getTransaction().getAmount().getCurrency())
                            .reference(t.getTransaction().getTransactionReference());

                    if (t.getTransaction().getMerchantDetails() != null) {
                        bankingTransaction.merchantName(t.getTransaction().getMerchantDetails().getMerchantName())
                                .merchantCategoryCode(t.getTransaction().getMerchantDetails().getMerchantCategoryCode());
                    }
                    ;
                    return bankingTransaction;
                })
                .collect(Collectors.toList());

        //Package the answer
        int totalPages = response.getTotalPages();

        return ResponseEntity.ok(new ResponseBankingTransactionList()
                .data(new ResponseBankingTransactionListData().transactions(transactions))
                .links(PaginationUtilCdr.generateLinks(httpUrl, page, totalPages))
                .meta(PaginationUtilCdr.generatePaginatedMetaData(totalPages)));
    }

    @Override
    public ResponseEntity<ResponseBankingAccountsBalanceById> listBalance(String accountId, String xV, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {

        log.info("Read balances for account  {} with minimumPermissions {}", accountId, permissions);
        Page<FRBalance1> balances = frBalance1Repository.findByAccountId(accountId, PageRequest.of(0, PAGE_LIMIT_BALANCES));
        int totalPage = balances.getTotalPages();
        BankingBalance bankingBalance = new BankingBalance()
                .accountId(accountId);

        Optional<FRBalance1> currentBalance = balances.get().filter(b -> b.getBalance().getType() == INTERIMAVAILABLE).findFirst();
        if (currentBalance.isPresent()) {
            bankingBalance.currentBalance(currentBalance.get().getBalance().getAmount().getAmount());
            bankingBalance.availableBalance(currentBalance.get().getBalance().getAmount().getAmount());
            bankingBalance.currency(currentBalance.get().getBalance().getAmount().getCurrency());
        }
        return ResponseEntity.ok(new ResponseBankingAccountsBalanceById()
                .data(bankingBalance)
                .links(PaginationUtilCdr.generateLinksOnePager(httpUrl))
                .meta(PaginationUtilCdr.generateMetaData()));

    }

    @Override
    public ResponseEntity<ResponseBankingAccountsBalanceList> listBalancesBulk(String xV, @Valid String productCategory, @Valid String openStatus, @Valid Boolean isOwned, @Valid Integer page, @Valid Integer pageSize, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingAccountsBalanceList> listBalancesSpecificAccounts(@Valid RequestAccountIds body, String xV, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject, @Valid Integer page, @Valid Integer pageSize,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingDirectDebitAuthorisationList> listDirectDebits(String accountId, String xV, @Valid Integer page, @Valid Integer pageSize, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingDirectDebitAuthorisationList> listDirectDebitsBulk(String xV, @Valid String productCategory, @Valid String openStatus, @Valid Boolean isOwned, @Valid Integer page, @Valid Integer pageSize, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingDirectDebitAuthorisationList> listDirectDebitsSpecificAccounts(@Valid RequestAccountIds body, String xV, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject, @Valid Integer page, @Valid Integer pageSize,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingPayeeList> listPayees(String xV, @Valid String type, @Valid Integer page, @Valid Integer pageSize, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingProductList> listProducts(String xV, @Valid String effective, @Valid String updatedSince, @Valid String brand, @Valid String productCategory, @Valid Integer page, @Valid Integer pageSize, String xMinV,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingScheduledPaymentsList> listScheduledPayments(String accountId, String xV, @Valid Integer page, @Valid Integer pageSize, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingScheduledPaymentsList> listScheduledPaymentsBulk(String xV, @Valid String productCategory, @Valid String openStatus, @Valid Boolean isOwned, @Valid Integer page, @Valid Integer pageSize, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseBankingScheduledPaymentsList> listScheduledPaymentsSpecificAccounts(@Valid RequestAccountIds body, String xV, String xMinV, String xFapiInteractionId, String xFapiAuthDate, String xFapiCustomerIpAddress, String xCdsUserAgent, String xCdsSubject, @Valid Integer page, @Valid Integer pageSize,

            @RequestHeader(value = "x-ob-account-ids", required = false) List<String> accountIds,

            @RequestHeader(value = "x-ob-permissions", required = false) List<OBExternalPermissions1Code> permissions,

            @RequestHeader(value = "x-ob-url", required = false) String httpUrl) {
        return null;
    }
}
