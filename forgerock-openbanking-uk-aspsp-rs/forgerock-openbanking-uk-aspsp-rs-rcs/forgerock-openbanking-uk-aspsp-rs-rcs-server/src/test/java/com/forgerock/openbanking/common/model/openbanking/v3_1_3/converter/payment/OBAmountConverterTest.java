package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import org.junit.Test;
import uk.org.openbanking.datamodel.payment.OBActiveOrHistoricCurrencyAndAmount;
import uk.org.openbanking.datamodel.payment.OBWriteDomestic2DataInitiationInstructedAmount;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBAmountConverter.toOBActiveOrHistoricCurrencyAndAmount;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link OBAmountConverter}.
 */
public class OBAmountConverterTest {

    @Test
    public void shouldConvertOBWriteDomestic2DataInitiationInstructedAmountToOBActiveOrHistoricCurrencyAndAmount() {
        // Given
        OBWriteDomestic2DataInitiationInstructedAmount amount = new OBWriteDomestic2DataInitiationInstructedAmount();
        amount.currency("GBP");
        amount.amount("10.01");

        // When
        OBActiveOrHistoricCurrencyAndAmount converted = toOBActiveOrHistoricCurrencyAndAmount(amount);

        // Then
        assertThat(converted.getCurrency()).isEqualTo("GBP");
        assertThat(converted.getAmount()).isEqualTo("10.01");
    }

}