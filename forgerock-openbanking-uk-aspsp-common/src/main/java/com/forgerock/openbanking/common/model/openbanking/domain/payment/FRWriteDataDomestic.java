package com.forgerock.openbanking.common.model.openbanking.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRWriteDataDomestic {

    private String consentId;
    private FRWriteDomesticDataInitiation initiation;
}
