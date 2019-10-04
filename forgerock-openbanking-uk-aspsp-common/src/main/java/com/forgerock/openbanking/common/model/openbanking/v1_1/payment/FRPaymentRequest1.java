/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.model.openbanking.v1_1.payment;

import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import uk.org.openbanking.datamodel.payment.paymentsetup.OBPaymentSetup1;

@Document
public class FRPaymentRequest1 {
    @Indexed
    public String merchantId;
    @Indexed
    public String orderId;
    public OBPaymentSetup1 paymentSetup;

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public OBPaymentSetup1 getPaymentSetup() {
        return paymentSetup;
    }

    public void setPaymentSetup(OBPaymentSetup1 paymentSetup) {
        this.paymentSetup = paymentSetup;
    }

    @Override
    public String toString() {
        return "FRPaymentRequest1{" +
                "merchantId='" + merchantId + '\'' +
                ", orderId='" + orderId + '\'' +
                ", paymentSetup=" + paymentSetup +
                '}';
    }
}
