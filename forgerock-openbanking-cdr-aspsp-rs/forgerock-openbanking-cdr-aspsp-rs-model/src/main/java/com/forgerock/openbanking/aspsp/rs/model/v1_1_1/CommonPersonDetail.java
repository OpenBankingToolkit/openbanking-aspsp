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

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * CommonPersonDetail
 */
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-03-05T15:13:51.111520Z[Europe/London]")
public class CommonPersonDetail extends CommonPerson {
    @JsonProperty("phoneNumbers")
    @Valid
    private List<CommonPhoneNumber> phoneNumbers = new ArrayList<CommonPhoneNumber>();

    @JsonProperty("emailAddresses")
    @Valid
    private List<CommonEmailAddress> emailAddresses = new ArrayList<CommonEmailAddress>();

    @JsonProperty("physicalAddresses")
    @Valid
    private List<CommonPhysicalAddressWithPurpose> physicalAddresses = new ArrayList<CommonPhysicalAddressWithPurpose>();

    public CommonPersonDetail phoneNumbers(List<CommonPhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
        return this;
    }

    public CommonPersonDetail addPhoneNumbersItem(CommonPhoneNumber phoneNumbersItem) {
        this.phoneNumbers.add(phoneNumbersItem);
        return this;
    }

    /**
     * Array is mandatory but may be empty if no phone numbers are held
     * @return phoneNumbers
     **/
    @ApiModelProperty(required = true, value = "Array is mandatory but may be empty if no phone numbers are held")
    @NotNull
    @Valid
    public List<CommonPhoneNumber> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(List<CommonPhoneNumber> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }

    public CommonPersonDetail emailAddresses(List<CommonEmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
        return this;
    }

    public CommonPersonDetail addEmailAddressesItem(CommonEmailAddress emailAddressesItem) {
        this.emailAddresses.add(emailAddressesItem);
        return this;
    }

    /**
     * May be empty
     * @return emailAddresses
     **/
    @ApiModelProperty(required = true, value = "May be empty")
    @NotNull
    @Valid
    public List<CommonEmailAddress> getEmailAddresses() {
        return emailAddresses;
    }

    public void setEmailAddresses(List<CommonEmailAddress> emailAddresses) {
        this.emailAddresses = emailAddresses;
    }

    public CommonPersonDetail physicalAddresses(List<CommonPhysicalAddressWithPurpose> physicalAddresses) {
        this.physicalAddresses = physicalAddresses;
        return this;
    }

    public CommonPersonDetail addPhysicalAddressesItem(CommonPhysicalAddressWithPurpose physicalAddressesItem) {
        this.physicalAddresses.add(physicalAddressesItem);
        return this;
    }

    /**
     * Must contain at least one address. One and only one address may have the purpose of REGISTERED. Zero or one, and no more than one, record may have the purpose of MAIL. If zero then the REGISTERED address is to be used for mail
     * @return physicalAddresses
     **/
    @ApiModelProperty(required = true, value = "Must contain at least one address. One and only one address may have the purpose of REGISTERED. Zero or one, and no more than one, record may have the purpose of MAIL. If zero then the REGISTERED address is to be used for mail")
    @NotNull
    @Valid
    public List<CommonPhysicalAddressWithPurpose> getPhysicalAddresses() {
        return physicalAddresses;
    }

    public void setPhysicalAddresses(List<CommonPhysicalAddressWithPurpose> physicalAddresses) {
        this.physicalAddresses = physicalAddresses;
    }


    @Override
    public boolean equals(java.lang.Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CommonPersonDetail commonPersonDetail = (CommonPersonDetail) o;
        return Objects.equals(this.phoneNumbers, commonPersonDetail.phoneNumbers) &&
                Objects.equals(this.emailAddresses, commonPersonDetail.emailAddresses) &&
                Objects.equals(this.physicalAddresses, commonPersonDetail.physicalAddresses) &&
                super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phoneNumbers, emailAddresses, physicalAddresses, super.hashCode());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class CommonPersonDetail {\n");
        sb.append("    ").append(toIndentedString(super.toString())).append("\n");
        sb.append("    phoneNumbers: ").append(toIndentedString(phoneNumbers)).append("\n");
        sb.append("    emailAddresses: ").append(toIndentedString(emailAddresses)).append("\n");
        sb.append("    physicalAddresses: ").append(toIndentedString(physicalAddresses)).append("\n");
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
