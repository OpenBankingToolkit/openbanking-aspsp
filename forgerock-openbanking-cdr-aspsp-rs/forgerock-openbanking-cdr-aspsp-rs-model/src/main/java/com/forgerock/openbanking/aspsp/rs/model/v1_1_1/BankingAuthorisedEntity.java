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
package com.forgerock.openbanking.aspsp.rs.model.v1_1_1;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import java.util.Objects;

/**
 * BankingAuthorisedEntity
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:51.111520Z[Europe/London]")
public class BankingAuthorisedEntity {
    @JsonProperty("description")
    private String description = null;

    @JsonProperty("financialInstitution")
    private String financialInstitution = null;

    @JsonProperty("abn")
    private String abn = null;

    @JsonProperty("acn")
    private String acn = null;

    @JsonProperty("arbn")
    private String arbn = null;

    public BankingAuthorisedEntity description(String description) {
        this.description = description;
        return this;
    }

    /**
     * Description of the authorised entity derived from previously executed direct debits
     * @return description
     **/
    @ApiModelProperty(value = "Description of the authorised entity derived from previously executed direct debits")

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankingAuthorisedEntity financialInstitution(String financialInstitution) {
        this.financialInstitution = financialInstitution;
        return this;
    }

    /**
     * Name of the financial institution through which the direct debit will be executed. Is required unless the payment is made via a credit card scheme
     * @return financialInstitution
     **/
    @ApiModelProperty(value = "Name of the financial institution through which the direct debit will be executed. Is required unless the payment is made via a credit card scheme")

    public String getFinancialInstitution() {
        return financialInstitution;
    }

    public void setFinancialInstitution(String financialInstitution) {
        this.financialInstitution = financialInstitution;
    }

    public BankingAuthorisedEntity abn(String abn) {
        this.abn = abn;
        return this;
    }

    /**
     * Australian Business Number for the authorised entity
     * @return abn
     **/
    @ApiModelProperty(value = "Australian Business Number for the authorised entity")

    public String getAbn() {
        return abn;
    }

    public void setAbn(String abn) {
        this.abn = abn;
    }

    public BankingAuthorisedEntity acn(String acn) {
        this.acn = acn;
        return this;
    }

    /**
     * Australian Company Number for the authorised entity
     * @return acn
     **/
    @ApiModelProperty(value = "Australian Company Number for the authorised entity")

    public String getAcn() {
        return acn;
    }

    public void setAcn(String acn) {
        this.acn = acn;
    }

    public BankingAuthorisedEntity arbn(String arbn) {
        this.arbn = arbn;
        return this;
    }

    /**
     * Australian Registered Body Number for the authorised entity
     * @return arbn
     **/
    @ApiModelProperty(value = "Australian Registered Body Number for the authorised entity")

    public String getArbn() {
        return arbn;
    }

    public void setArbn(String arbn) {
        this.arbn = arbn;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BankingAuthorisedEntity bankingAuthorisedEntity = (BankingAuthorisedEntity) o;
        return Objects.equals(this.description, bankingAuthorisedEntity.description) &&
                Objects.equals(this.financialInstitution, bankingAuthorisedEntity.financialInstitution) &&
                Objects.equals(this.abn, bankingAuthorisedEntity.abn) &&
                Objects.equals(this.acn, bankingAuthorisedEntity.acn) &&
                Objects.equals(this.arbn, bankingAuthorisedEntity.arbn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(description, financialInstitution, abn, acn, arbn);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class BankingAuthorisedEntity {\n");

        sb.append("    description: ").append(toIndentedString(description)).append("\n");
        sb.append("    financialInstitution: ").append(toIndentedString(financialInstitution)).append("\n");
        sb.append("    abn: ").append(toIndentedString(abn)).append("\n");
        sb.append("    acn: ").append(toIndentedString(acn)).append("\n");
        sb.append("    arbn: ").append(toIndentedString(arbn)).append("\n");
        sb.append("}");
        return sb.toString();
    }

    /**
     * Convert the given object to string with each line indented by 4 spaces
     * (except the first line).
     */
    private String toIndentedString(java.lang.Object o) {
        if (o == null) {
            return "null";
        }
        return o.toString().replace("\n", "\n    ");
    }
}
