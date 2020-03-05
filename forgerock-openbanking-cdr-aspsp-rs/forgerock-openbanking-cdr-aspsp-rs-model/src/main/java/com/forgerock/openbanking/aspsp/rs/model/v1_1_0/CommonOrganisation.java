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
package com.forgerock.openbanking.aspsp.rs.model.v1_1_0;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * CommonOrganisation
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:05.282441Z[Europe/London]")
public class CommonOrganisation {
    @JsonProperty("lastUpdateTime")
    private String lastUpdateTime = null;

    @JsonProperty("agentFirstName")
    private String agentFirstName = null;

    @JsonProperty("agentLastName")
    private String agentLastName = null;

    @JsonProperty("agentRole")
    private String agentRole = null;

    @JsonProperty("businessName")
    private String businessName = null;

    @JsonProperty("legalName")
    private String legalName = null;

    @JsonProperty("shortName")
    private String shortName = null;

    @JsonProperty("abn")
    private String abn = null;

    @JsonProperty("acn")
    private String acn = null;

    @JsonProperty("isACNCRegistered")
    private Boolean isACNCRegistered = null;

    @JsonProperty("industryCode")
    private String industryCode = null;

    /**
     * Legal organisation type
     */
    public enum OrganisationTypeEnum {
        SOLE_TRADER("SOLE_TRADER"),

        COMPANY("COMPANY"),

        PARTNERSHIP("PARTNERSHIP"),

        TRUST("TRUST"),

        GOVERNMENT_ENTITY("GOVERNMENT_ENTITY"),

        OTHER("OTHER");

        private String value;

        OrganisationTypeEnum(String value) {
            this.value = value;
        }

        @Override
        @JsonValue
        public String toString() {
            return String.valueOf(value);
        }

        @JsonCreator
        public static OrganisationTypeEnum fromValue(String text) {
            for (OrganisationTypeEnum b : OrganisationTypeEnum.values()) {
                if (String.valueOf(b.value).equals(text)) {
                    return b;
                }
            }
            return null;
        }
    }

    @JsonProperty("organisationType")
    private OrganisationTypeEnum organisationType = null;

    @JsonProperty("registeredCountry")
    private String registeredCountry = null;

    @JsonProperty("establishmentDate")
    private String establishmentDate = null;

    public CommonOrganisation lastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
        return this;
    }

    /**
     * The date and time that this record was last updated by the customer. If no update has occurred then this date should reflect the initial creation date for the data
     * @return lastUpdateTime
     **/
    @ApiModelProperty(value = "The date and time that this record was last updated by the customer. If no update has occurred then this date should reflect the initial creation date for the data")

    public String getLastUpdateTime() {
        return lastUpdateTime;
    }

    public void setLastUpdateTime(String lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public CommonOrganisation agentFirstName(String agentFirstName) {
        this.agentFirstName = agentFirstName;
        return this;
    }

    /**
     * The first name of the individual providing access on behalf of the organisation. For people with single names this field need not be present.  The single name should be in the lastName field
     * @return agentFirstName
     **/
    @ApiModelProperty(value = "The first name of the individual providing access on behalf of the organisation. For people with single names this field need not be present.  The single name should be in the lastName field")

    public String getAgentFirstName() {
        return agentFirstName;
    }

    public void setAgentFirstName(String agentFirstName) {
        this.agentFirstName = agentFirstName;
    }

    public CommonOrganisation agentLastName(String agentLastName) {
        this.agentLastName = agentLastName;
        return this;
    }

    /**
     * The last name of the individual providing access on behalf of the organisation. For people with single names the single name should be in this field
     * @return agentLastName
     **/
    @ApiModelProperty(required = true, value = "The last name of the individual providing access on behalf of the organisation. For people with single names the single name should be in this field")
    @NotNull

    public String getAgentLastName() {
        return agentLastName;
    }

    public void setAgentLastName(String agentLastName) {
        this.agentLastName = agentLastName;
    }

    public CommonOrganisation agentRole(String agentRole) {
        this.agentRole = agentRole;
        return this;
    }

    /**
     * The role of the individual identified as the agent who is providing authorisation.  Expected to be used for display. Default to Unspecified if the role is not known
     * @return agentRole
     **/
    @ApiModelProperty(required = true, value = "The role of the individual identified as the agent who is providing authorisation.  Expected to be used for display. Default to Unspecified if the role is not known")
    @NotNull

    public String getAgentRole() {
        return agentRole;
    }

    public void setAgentRole(String agentRole) {
        this.agentRole = agentRole;
    }

    public CommonOrganisation businessName(String businessName) {
        this.businessName = businessName;
        return this;
    }

    /**
     * Name of the organisation
     * @return businessName
     **/
    @ApiModelProperty(required = true, value = "Name of the organisation")
    @NotNull

    public String getBusinessName() {
        return businessName;
    }

    public void setBusinessName(String businessName) {
        this.businessName = businessName;
    }

    public CommonOrganisation legalName(String legalName) {
        this.legalName = legalName;
        return this;
    }

    /**
     * Legal name, if different to the business name
     * @return legalName
     **/
    @ApiModelProperty(value = "Legal name, if different to the business name")

    public String getLegalName() {
        return legalName;
    }

    public void setLegalName(String legalName) {
        this.legalName = legalName;
    }

    public CommonOrganisation shortName(String shortName) {
        this.shortName = shortName;
        return this;
    }

    /**
     * Short name used for communication, if different to the business name
     * @return shortName
     **/
    @ApiModelProperty(value = "Short name used for communication, if different to the business name")

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public CommonOrganisation abn(String abn) {
        this.abn = abn;
        return this;
    }

    /**
     * Australian Business Number for the organisation
     * @return abn
     **/
    @ApiModelProperty(value = "Australian Business Number for the organisation")

    public String getAbn() {
        return abn;
    }

    public void setAbn(String abn) {
        this.abn = abn;
    }

    public CommonOrganisation acn(String acn) {
        this.acn = acn;
        return this;
    }

    /**
     * Australian Company Number for the organisation. Required only if an ACN is applicable for the organisation type
     * @return acn
     **/
    @ApiModelProperty(value = "Australian Company Number for the organisation. Required only if an ACN is applicable for the organisation type")

    public String getAcn() {
        return acn;
    }

    public void setAcn(String acn) {
        this.acn = acn;
    }

    public CommonOrganisation isACNCRegistered(Boolean isACNCRegistered) {
        this.isACNCRegistered = isACNCRegistered;
        return this;
    }

    /**
     * True if registered with the ACNC.  False if not. Absent or null if not confirmed.
     * @return isACNCRegistered
     **/
    @ApiModelProperty(value = "True if registered with the ACNC.  False if not. Absent or null if not confirmed.")

    public Boolean isIsACNCRegistered() {
        return isACNCRegistered;
    }

    public void setIsACNCRegistered(Boolean isACNCRegistered) {
        this.isACNCRegistered = isACNCRegistered;
    }

    public CommonOrganisation industryCode(String industryCode) {
        this.industryCode = industryCode;
        return this;
    }

    /**
     * [ANZSIC (2006)](http://www.abs.gov.au/anzsic) code for the organisation.
     * @return industryCode
     **/
    @ApiModelProperty(value = "[ANZSIC (2006)](http://www.abs.gov.au/anzsic) code for the organisation.")

    public String getIndustryCode() {
        return industryCode;
    }

    public void setIndustryCode(String industryCode) {
        this.industryCode = industryCode;
    }

    public CommonOrganisation organisationType(OrganisationTypeEnum organisationType) {
        this.organisationType = organisationType;
        return this;
    }

    /**
     * Legal organisation type
     * @return organisationType
     **/
    @ApiModelProperty(required = true, value = "Legal organisation type")
    @NotNull

    public OrganisationTypeEnum getOrganisationType() {
        return organisationType;
    }

    public void setOrganisationType(OrganisationTypeEnum organisationType) {
        this.organisationType = organisationType;
    }

    public CommonOrganisation registeredCountry(String registeredCountry) {
        this.registeredCountry = registeredCountry;
        return this;
    }

    /**
     * Enumeration with values from [ISO 3166 Alpha-3](https://www.iso.org/iso-3166-country-codes.html) country codes.  Assumed to be AUS if absent
     * @return registeredCountry
     **/
    @ApiModelProperty(value = "Enumeration with values from [ISO 3166 Alpha-3](https://www.iso.org/iso-3166-country-codes.html) country codes.  Assumed to be AUS if absent")

    public String getRegisteredCountry() {
        return registeredCountry;
    }

    public void setRegisteredCountry(String registeredCountry) {
        this.registeredCountry = registeredCountry;
    }

    public CommonOrganisation establishmentDate(String establishmentDate) {
        this.establishmentDate = establishmentDate;
        return this;
    }

    /**
     * The date the organisation described was established
     * @return establishmentDate
     **/
    @ApiModelProperty(value = "The date the organisation described was established")

    public String getEstablishmentDate() {
        return establishmentDate;
    }

    public void setEstablishmentDate(String establishmentDate) {
        this.establishmentDate = establishmentDate;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonOrganisation commonOrganisation = (CommonOrganisation) o;
        return Objects.equals(this.lastUpdateTime, commonOrganisation.lastUpdateTime) &&
                Objects.equals(this.agentFirstName, commonOrganisation.agentFirstName) &&
                Objects.equals(this.agentLastName, commonOrganisation.agentLastName) &&
                Objects.equals(this.agentRole, commonOrganisation.agentRole) &&
                Objects.equals(this.businessName, commonOrganisation.businessName) &&
                Objects.equals(this.legalName, commonOrganisation.legalName) &&
                Objects.equals(this.shortName, commonOrganisation.shortName) &&
                Objects.equals(this.abn, commonOrganisation.abn) &&
                Objects.equals(this.acn, commonOrganisation.acn) &&
                Objects.equals(this.isACNCRegistered, commonOrganisation.isACNCRegistered) &&
                Objects.equals(this.industryCode, commonOrganisation.industryCode) &&
                Objects.equals(this.organisationType, commonOrganisation.organisationType) &&
                Objects.equals(this.registeredCountry, commonOrganisation.registeredCountry) &&
                Objects.equals(this.establishmentDate, commonOrganisation.establishmentDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastUpdateTime, agentFirstName, agentLastName, agentRole, businessName, legalName, shortName, abn, acn, isACNCRegistered, industryCode, organisationType, registeredCountry, establishmentDate);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CommonOrganisation {\n");

        sb.append("    lastUpdateTime: ").append(toIndentedString(lastUpdateTime)).append("\n");
        sb.append("    agentFirstName: ").append(toIndentedString(agentFirstName)).append("\n");
        sb.append("    agentLastName: ").append(toIndentedString(agentLastName)).append("\n");
        sb.append("    agentRole: ").append(toIndentedString(agentRole)).append("\n");
        sb.append("    businessName: ").append(toIndentedString(businessName)).append("\n");
        sb.append("    legalName: ").append(toIndentedString(legalName)).append("\n");
        sb.append("    shortName: ").append(toIndentedString(shortName)).append("\n");
        sb.append("    abn: ").append(toIndentedString(abn)).append("\n");
        sb.append("    acn: ").append(toIndentedString(acn)).append("\n");
        sb.append("    isACNCRegistered: ").append(toIndentedString(isACNCRegistered)).append("\n");
        sb.append("    industryCode: ").append(toIndentedString(industryCode)).append("\n");
        sb.append("    organisationType: ").append(toIndentedString(organisationType)).append("\n");
        sb.append("    registeredCountry: ").append(toIndentedString(registeredCountry)).append("\n");
        sb.append("    establishmentDate: ").append(toIndentedString(establishmentDate)).append("\n");
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
