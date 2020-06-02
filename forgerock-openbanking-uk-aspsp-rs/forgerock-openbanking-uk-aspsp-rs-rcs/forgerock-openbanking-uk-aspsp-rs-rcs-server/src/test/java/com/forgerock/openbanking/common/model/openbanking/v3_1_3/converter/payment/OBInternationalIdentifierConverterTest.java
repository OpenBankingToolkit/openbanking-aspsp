package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import org.junit.Test;
import uk.org.openbanking.datamodel.payment.*;

import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalIdentifierConverter.toOBBranchAndFinancialInstitutionIdentification3;
import static com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment.OBInternationalIdentifierConverter.toOBWriteInternational3DataInitiationCreditor;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for {@link OBInternationalIdentifierConverter}.
 */
public class OBInternationalIdentifierConverterTest {

    @Test
    public void shouldConvertOBWriteInternational3DataInitiationCreditorAgentToOBBranchAndFinancialInstitutionIdentification3() {
        // Given
        OBWriteInternational3DataInitiationCreditorAgent agent = new OBWriteInternational3DataInitiationCreditorAgent();
        agent.schemeName("A scheme name");
        agent.identification("The identification");
        agent.name("A name");
        agent.postalAddress(postalAddress());

        // When
        OBBranchAndFinancialInstitutionIdentification3 converted = toOBBranchAndFinancialInstitutionIdentification3(agent);

        // Then
        assertThat(converted.getSchemeName()).isEqualTo("A scheme name");
        assertThat(converted.getIdentification()).isEqualTo("The identification");
        assertThat(converted.getName()).isEqualTo("A name");
        assertThat(converted.getPostalAddress()).isEqualTo(postalAddress());
    }

    @Test
    public void shouldConvertOBPartyIdentification43ToOBWriteInternational3DataInitiationCreditor() {
        // Given
        OBPartyIdentification43 creditor = new OBPartyIdentification43();
        creditor.name("A name");
        creditor.postalAddress(postalAddress());

        // When
        OBWriteInternational3DataInitiationCreditor converted = toOBWriteInternational3DataInitiationCreditor(creditor);

        // Then
        assertThat(converted.getName()).isEqualTo("A name");
        assertThat(converted.getPostalAddress()).isEqualTo(postalAddress());
    }

    private OBPostalAddress6 postalAddress() {
        return (new OBPostalAddress6())
                .addressType(OBAddressTypeCode.BUSINESS)
                .department("Department")
                .subDepartment("Sub Department")
                .streetName("Street Name")
                .buildingNumber("12")
                .postCode("BS1 5AB")
                .townName("Bristol")
                .country("UK");
    }

}