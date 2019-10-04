/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.aspsp.rs.api.payment.verifier;

import org.junit.Test;

public class UKMobilePhoneFormatTest {

    @Test
    public void isValid() {
        assertThat(UKMobilePhoneFormat.isValid("notANumber")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid(" ")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid(null)).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("+557123456789")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("1171234567890")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("-4471234567890")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("771234567890")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("77AAAAAAAAAA")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("++44712345678")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("+44712345678+")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("+447123-5689")).isFalse();
        assertThat(UKMobilePhoneFormat.isValid("*44712345689")).isFalse();

        // 0
        assertThat(UKMobilePhoneFormat.isValid("07123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("07123456789  ")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("0712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("071234567890")).isFalse(); // too long

        // +44
        assertThat(UKMobilePhoneFormat.isValid("+447123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("+44712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("+4471234567890")).isFalse(); // too long

        // 0044
        assertThat(UKMobilePhoneFormat.isValid("00447123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("0044712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("004471234567890")).isFalse(); // too long

        // 44
        assertThat(UKMobilePhoneFormat.isValid("447123456789")).isTrue();
        assertThat(UKMobilePhoneFormat.isValid("44712345678")).isFalse(); // too short
        assertThat(UKMobilePhoneFormat.isValid("4471234567890")).isFalse(); // too long

    }
}