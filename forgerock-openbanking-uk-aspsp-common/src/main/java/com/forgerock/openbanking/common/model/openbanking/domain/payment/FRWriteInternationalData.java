package com.forgerock.openbanking.common.model.openbanking.domain.payment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FRWriteInternationalData {

    private String consentId;
    private FRWriteInternationalDataInitiation initiation;
}
