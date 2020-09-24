package com.forgerock.openbanking.common.model.openbanking.domain.payment;

import com.forgerock.openbanking.common.model.openbanking.domain.payment.common.FRRisk;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRWriteDomesticStandingOrder {

    private FRWriteDataDomesticStandingOrder data;
    private FRRisk risk;
}
