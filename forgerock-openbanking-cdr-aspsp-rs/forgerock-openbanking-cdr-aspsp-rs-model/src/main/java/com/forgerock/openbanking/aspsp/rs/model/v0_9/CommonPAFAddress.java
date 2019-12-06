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
package com.forgerock.openbanking.aspsp.rs.model.v0_9;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.Objects;

/**
 * Australian address formatted according to the file format defined by the [PAF file format](https://auspost.com.au/content/dam/auspost_corp/media/documents/australia-post-data-guide.pdf)
 */
@ApiModel(description = "Australian address formatted according to the file format defined by the [PAF file format](https://auspost.com.au/content/dam/auspost_corp/media/documents/australia-post-data-guide.pdf)")
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2019-10-31T15:49:59.742253Z[Europe/London]")
public class CommonPAFAddress   {
  @JsonProperty("dpid")
  private String dpid = null;

  @JsonProperty("thoroughfareNumber1")
  private Integer thoroughfareNumber1 = null;

  @JsonProperty("thoroughfareNumber1Suffix")
  private String thoroughfareNumber1Suffix = null;

  @JsonProperty("thoroughfareNumber2")
  private Integer thoroughfareNumber2 = null;

  @JsonProperty("thoroughfareNumber2Suffix")
  private String thoroughfareNumber2Suffix = null;

  @JsonProperty("flatUnitType")
  private String flatUnitType = null;

  @JsonProperty("flatUnitNumber")
  private String flatUnitNumber = null;

  @JsonProperty("floorLevelType")
  private String floorLevelType = null;

  @JsonProperty("floorLevelNumber")
  private String floorLevelNumber = null;

  @JsonProperty("lotNumber")
  private String lotNumber = null;

  @JsonProperty("buildingName1")
  private String buildingName1 = null;

  @JsonProperty("buildingName2")
  private String buildingName2 = null;

  @JsonProperty("streetName")
  private String streetName = null;

  @JsonProperty("streetType")
  private String streetType = null;

  @JsonProperty("streetSuffix")
  private String streetSuffix = null;

  @JsonProperty("postalDeliveryType")
  private String postalDeliveryType = null;

  @JsonProperty("postalDeliveryNumber")
  private Integer postalDeliveryNumber = null;

  @JsonProperty("postalDeliveryNumberPrefix")
  private String postalDeliveryNumberPrefix = null;

  @JsonProperty("postalDeliveryNumberSuffix")
  private String postalDeliveryNumberSuffix = null;

  @JsonProperty("localityName")
  private String localityName = null;

  @JsonProperty("postcode")
  private String postcode = null;

  @JsonProperty("state")
  private String state = null;

  public CommonPAFAddress dpid(String dpid) {
    this.dpid = dpid;
    return this;
  }

  /**
   * Unique identifier for an address as defined by Australia Post.  Also known as Delivery Point Identifier
   * @return dpid
  **/
  @ApiModelProperty(value = "Unique identifier for an address as defined by Australia Post.  Also known as Delivery Point Identifier")

  public String getDpid() {
    return dpid;
  }

  public void setDpid(String dpid) {
    this.dpid = dpid;
  }

  public CommonPAFAddress thoroughfareNumber1(Integer thoroughfareNumber1) {
    this.thoroughfareNumber1 = thoroughfareNumber1;
    return this;
  }

  /**
   * Thoroughfare number for a property (first number in a property ranged address)
   * @return thoroughfareNumber1
  **/
  @ApiModelProperty(value = "Thoroughfare number for a property (first number in a property ranged address)")

  public Integer getThoroughfareNumber1() {
    return thoroughfareNumber1;
  }

  public void setThoroughfareNumber1(Integer thoroughfareNumber1) {
    this.thoroughfareNumber1 = thoroughfareNumber1;
  }

  public CommonPAFAddress thoroughfareNumber1Suffix(String thoroughfareNumber1Suffix) {
    this.thoroughfareNumber1Suffix = thoroughfareNumber1Suffix;
    return this;
  }

  /**
   * Suffix for the thoroughfare number. Only relevant is thoroughfareNumber1 is populated
   * @return thoroughfareNumber1Suffix
  **/
  @ApiModelProperty(value = "Suffix for the thoroughfare number. Only relevant is thoroughfareNumber1 is populated")

  public String getThoroughfareNumber1Suffix() {
    return thoroughfareNumber1Suffix;
  }

  public void setThoroughfareNumber1Suffix(String thoroughfareNumber1Suffix) {
    this.thoroughfareNumber1Suffix = thoroughfareNumber1Suffix;
  }

  public CommonPAFAddress thoroughfareNumber2(Integer thoroughfareNumber2) {
    this.thoroughfareNumber2 = thoroughfareNumber2;
    return this;
  }

  /**
   * Second thoroughfare number (only used if the property has a ranged address eg 23-25)
   * @return thoroughfareNumber2
  **/
  @ApiModelProperty(value = "Second thoroughfare number (only used if the property has a ranged address eg 23-25)")

  public Integer getThoroughfareNumber2() {
    return thoroughfareNumber2;
  }

  public void setThoroughfareNumber2(Integer thoroughfareNumber2) {
    this.thoroughfareNumber2 = thoroughfareNumber2;
  }

  public CommonPAFAddress thoroughfareNumber2Suffix(String thoroughfareNumber2Suffix) {
    this.thoroughfareNumber2Suffix = thoroughfareNumber2Suffix;
    return this;
  }

  /**
   * Suffix for the second thoroughfare number. Only relevant is thoroughfareNumber2 is populated
   * @return thoroughfareNumber2Suffix
  **/
  @ApiModelProperty(value = "Suffix for the second thoroughfare number. Only relevant is thoroughfareNumber2 is populated")

  public String getThoroughfareNumber2Suffix() {
    return thoroughfareNumber2Suffix;
  }

  public void setThoroughfareNumber2Suffix(String thoroughfareNumber2Suffix) {
    this.thoroughfareNumber2Suffix = thoroughfareNumber2Suffix;
  }

  public CommonPAFAddress flatUnitType(String flatUnitType) {
    this.flatUnitType = flatUnitType;
    return this;
  }

  /**
   * Type of flat or unit for the address
   * @return flatUnitType
  **/
  @ApiModelProperty(value = "Type of flat or unit for the address")

  public String getFlatUnitType() {
    return flatUnitType;
  }

  public void setFlatUnitType(String flatUnitType) {
    this.flatUnitType = flatUnitType;
  }

  public CommonPAFAddress flatUnitNumber(String flatUnitNumber) {
    this.flatUnitNumber = flatUnitNumber;
    return this;
  }

  /**
   * Unit number (including suffix, if applicable)
   * @return flatUnitNumber
  **/
  @ApiModelProperty(value = "Unit number (including suffix, if applicable)")

  public String getFlatUnitNumber() {
    return flatUnitNumber;
  }

  public void setFlatUnitNumber(String flatUnitNumber) {
    this.flatUnitNumber = flatUnitNumber;
  }

  public CommonPAFAddress floorLevelType(String floorLevelType) {
    this.floorLevelType = floorLevelType;
    return this;
  }

  /**
   * Type of floor or level for the address
   * @return floorLevelType
  **/
  @ApiModelProperty(value = "Type of floor or level for the address")

  public String getFloorLevelType() {
    return floorLevelType;
  }

  public void setFloorLevelType(String floorLevelType) {
    this.floorLevelType = floorLevelType;
  }

  public CommonPAFAddress floorLevelNumber(String floorLevelNumber) {
    this.floorLevelNumber = floorLevelNumber;
    return this;
  }

  /**
   * Floor or level number (including alpha characters)
   * @return floorLevelNumber
  **/
  @ApiModelProperty(value = "Floor or level number (including alpha characters)")

  public String getFloorLevelNumber() {
    return floorLevelNumber;
  }

  public void setFloorLevelNumber(String floorLevelNumber) {
    this.floorLevelNumber = floorLevelNumber;
  }

  public CommonPAFAddress lotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
    return this;
  }

  /**
   * Allotment number for the address
   * @return lotNumber
  **/
  @ApiModelProperty(value = "Allotment number for the address")

  public String getLotNumber() {
    return lotNumber;
  }

  public void setLotNumber(String lotNumber) {
    this.lotNumber = lotNumber;
  }

  public CommonPAFAddress buildingName1(String buildingName1) {
    this.buildingName1 = buildingName1;
    return this;
  }

  /**
   * Building/Property name 1
   * @return buildingName1
  **/
  @ApiModelProperty(value = "Building/Property name 1")

  public String getBuildingName1() {
    return buildingName1;
  }

  public void setBuildingName1(String buildingName1) {
    this.buildingName1 = buildingName1;
  }

  public CommonPAFAddress buildingName2(String buildingName2) {
    this.buildingName2 = buildingName2;
    return this;
  }

  /**
   * Building/Property name 2
   * @return buildingName2
  **/
  @ApiModelProperty(value = "Building/Property name 2")

  public String getBuildingName2() {
    return buildingName2;
  }

  public void setBuildingName2(String buildingName2) {
    this.buildingName2 = buildingName2;
  }

  public CommonPAFAddress streetName(String streetName) {
    this.streetName = streetName;
    return this;
  }

  /**
   * The name of the street
   * @return streetName
  **/
  @ApiModelProperty(value = "The name of the street")

  public String getStreetName() {
    return streetName;
  }

  public void setStreetName(String streetName) {
    this.streetName = streetName;
  }

  public CommonPAFAddress streetType(String streetType) {
    this.streetType = streetType;
    return this;
  }

  /**
   * The street type. Valid enumeration defined by Australia Post PAF code file
   * @return streetType
  **/
  @ApiModelProperty(value = "The street type. Valid enumeration defined by Australia Post PAF code file")

  public String getStreetType() {
    return streetType;
  }

  public void setStreetType(String streetType) {
    this.streetType = streetType;
  }

  public CommonPAFAddress streetSuffix(String streetSuffix) {
    this.streetSuffix = streetSuffix;
    return this;
  }

  /**
   * The street type suffix. Valid enumeration defined by Australia Post PAF code file
   * @return streetSuffix
  **/
  @ApiModelProperty(value = "The street type suffix. Valid enumeration defined by Australia Post PAF code file")

  public String getStreetSuffix() {
    return streetSuffix;
  }

  public void setStreetSuffix(String streetSuffix) {
    this.streetSuffix = streetSuffix;
  }

  public CommonPAFAddress postalDeliveryType(String postalDeliveryType) {
    this.postalDeliveryType = postalDeliveryType;
    return this;
  }

  /**
   * Postal delivery type. (eg. PO BOX). Valid enumeration defined by Australia Post PAF code file
   * @return postalDeliveryType
  **/
  @ApiModelProperty(value = "Postal delivery type. (eg. PO BOX). Valid enumeration defined by Australia Post PAF code file")

  public String getPostalDeliveryType() {
    return postalDeliveryType;
  }

  public void setPostalDeliveryType(String postalDeliveryType) {
    this.postalDeliveryType = postalDeliveryType;
  }

  public CommonPAFAddress postalDeliveryNumber(Integer postalDeliveryNumber) {
    this.postalDeliveryNumber = postalDeliveryNumber;
    return this;
  }

  /**
   * Postal delivery number if the address is a postal delivery type
   * @return postalDeliveryNumber
  **/
  @ApiModelProperty(value = "Postal delivery number if the address is a postal delivery type")

  public Integer getPostalDeliveryNumber() {
    return postalDeliveryNumber;
  }

  public void setPostalDeliveryNumber(Integer postalDeliveryNumber) {
    this.postalDeliveryNumber = postalDeliveryNumber;
  }

  public CommonPAFAddress postalDeliveryNumberPrefix(String postalDeliveryNumberPrefix) {
    this.postalDeliveryNumberPrefix = postalDeliveryNumberPrefix;
    return this;
  }

  /**
   * Postal delivery number prefix related to the postal delivery number
   * @return postalDeliveryNumberPrefix
  **/
  @ApiModelProperty(value = "Postal delivery number prefix related to the postal delivery number")

  public String getPostalDeliveryNumberPrefix() {
    return postalDeliveryNumberPrefix;
  }

  public void setPostalDeliveryNumberPrefix(String postalDeliveryNumberPrefix) {
    this.postalDeliveryNumberPrefix = postalDeliveryNumberPrefix;
  }

  public CommonPAFAddress postalDeliveryNumberSuffix(String postalDeliveryNumberSuffix) {
    this.postalDeliveryNumberSuffix = postalDeliveryNumberSuffix;
    return this;
  }

  /**
   * Postal delivery number suffix related to the postal delivery number
   * @return postalDeliveryNumberSuffix
  **/
  @ApiModelProperty(value = "Postal delivery number suffix related to the postal delivery number")

  public String getPostalDeliveryNumberSuffix() {
    return postalDeliveryNumberSuffix;
  }

  public void setPostalDeliveryNumberSuffix(String postalDeliveryNumberSuffix) {
    this.postalDeliveryNumberSuffix = postalDeliveryNumberSuffix;
  }

  public CommonPAFAddress localityName(String localityName) {
    this.localityName = localityName;
    return this;
  }

  /**
   * Full name of locality
   * @return localityName
  **/
  @ApiModelProperty(required = true, value = "Full name of locality")
  @NotNull

  public String getLocalityName() {
    return localityName;
  }

  public void setLocalityName(String localityName) {
    this.localityName = localityName;
  }

  public CommonPAFAddress postcode(String postcode) {
    this.postcode = postcode;
    return this;
  }

  /**
   * Postcode for the locality
   * @return postcode
  **/
  @ApiModelProperty(required = true, value = "Postcode for the locality")
  @NotNull

  public String getPostcode() {
    return postcode;
  }

  public void setPostcode(String postcode) {
    this.postcode = postcode;
  }

  public CommonPAFAddress state(String state) {
    this.state = state;
    return this;
  }

  /**
   * State in which the address belongs. Valid enumeration defined by Australia Post PAF code file [State Type Abbreviation](https://auspost.com.au/content/dam/auspost_corp/media/documents/australia-post-data-guide.pdf). NSW, QLD, VIC, NT, WA, SA, TAS, ACT, AAT
   * @return state
  **/
  @ApiModelProperty(required = true, value = "State in which the address belongs. Valid enumeration defined by Australia Post PAF code file [State Type Abbreviation](https://auspost.com.au/content/dam/auspost_corp/media/documents/australia-post-data-guide.pdf). NSW, QLD, VIC, NT, WA, SA, TAS, ACT, AAT")
  @NotNull

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CommonPAFAddress commonPAFAddress = (CommonPAFAddress) o;
    return Objects.equals(this.dpid, commonPAFAddress.dpid) &&
        Objects.equals(this.thoroughfareNumber1, commonPAFAddress.thoroughfareNumber1) &&
        Objects.equals(this.thoroughfareNumber1Suffix, commonPAFAddress.thoroughfareNumber1Suffix) &&
        Objects.equals(this.thoroughfareNumber2, commonPAFAddress.thoroughfareNumber2) &&
        Objects.equals(this.thoroughfareNumber2Suffix, commonPAFAddress.thoroughfareNumber2Suffix) &&
        Objects.equals(this.flatUnitType, commonPAFAddress.flatUnitType) &&
        Objects.equals(this.flatUnitNumber, commonPAFAddress.flatUnitNumber) &&
        Objects.equals(this.floorLevelType, commonPAFAddress.floorLevelType) &&
        Objects.equals(this.floorLevelNumber, commonPAFAddress.floorLevelNumber) &&
        Objects.equals(this.lotNumber, commonPAFAddress.lotNumber) &&
        Objects.equals(this.buildingName1, commonPAFAddress.buildingName1) &&
        Objects.equals(this.buildingName2, commonPAFAddress.buildingName2) &&
        Objects.equals(this.streetName, commonPAFAddress.streetName) &&
        Objects.equals(this.streetType, commonPAFAddress.streetType) &&
        Objects.equals(this.streetSuffix, commonPAFAddress.streetSuffix) &&
        Objects.equals(this.postalDeliveryType, commonPAFAddress.postalDeliveryType) &&
        Objects.equals(this.postalDeliveryNumber, commonPAFAddress.postalDeliveryNumber) &&
        Objects.equals(this.postalDeliveryNumberPrefix, commonPAFAddress.postalDeliveryNumberPrefix) &&
        Objects.equals(this.postalDeliveryNumberSuffix, commonPAFAddress.postalDeliveryNumberSuffix) &&
        Objects.equals(this.localityName, commonPAFAddress.localityName) &&
        Objects.equals(this.postcode, commonPAFAddress.postcode) &&
        Objects.equals(this.state, commonPAFAddress.state);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dpid, thoroughfareNumber1, thoroughfareNumber1Suffix, thoroughfareNumber2, thoroughfareNumber2Suffix, flatUnitType, flatUnitNumber, floorLevelType, floorLevelNumber, lotNumber, buildingName1, buildingName2, streetName, streetType, streetSuffix, postalDeliveryType, postalDeliveryNumber, postalDeliveryNumberPrefix, postalDeliveryNumberSuffix, localityName, postcode, state);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class CommonPAFAddress {\n");

    sb.append("    dpid: ").append(toIndentedString(dpid)).append("\n");
    sb.append("    thoroughfareNumber1: ").append(toIndentedString(thoroughfareNumber1)).append("\n");
    sb.append("    thoroughfareNumber1Suffix: ").append(toIndentedString(thoroughfareNumber1Suffix)).append("\n");
    sb.append("    thoroughfareNumber2: ").append(toIndentedString(thoroughfareNumber2)).append("\n");
    sb.append("    thoroughfareNumber2Suffix: ").append(toIndentedString(thoroughfareNumber2Suffix)).append("\n");
    sb.append("    flatUnitType: ").append(toIndentedString(flatUnitType)).append("\n");
    sb.append("    flatUnitNumber: ").append(toIndentedString(flatUnitNumber)).append("\n");
    sb.append("    floorLevelType: ").append(toIndentedString(floorLevelType)).append("\n");
    sb.append("    floorLevelNumber: ").append(toIndentedString(floorLevelNumber)).append("\n");
    sb.append("    lotNumber: ").append(toIndentedString(lotNumber)).append("\n");
    sb.append("    buildingName1: ").append(toIndentedString(buildingName1)).append("\n");
    sb.append("    buildingName2: ").append(toIndentedString(buildingName2)).append("\n");
    sb.append("    streetName: ").append(toIndentedString(streetName)).append("\n");
    sb.append("    streetType: ").append(toIndentedString(streetType)).append("\n");
    sb.append("    streetSuffix: ").append(toIndentedString(streetSuffix)).append("\n");
    sb.append("    postalDeliveryType: ").append(toIndentedString(postalDeliveryType)).append("\n");
    sb.append("    postalDeliveryNumber: ").append(toIndentedString(postalDeliveryNumber)).append("\n");
    sb.append("    postalDeliveryNumberPrefix: ").append(toIndentedString(postalDeliveryNumberPrefix)).append("\n");
    sb.append("    postalDeliveryNumberSuffix: ").append(toIndentedString(postalDeliveryNumberSuffix)).append("\n");
    sb.append("    localityName: ").append(toIndentedString(localityName)).append("\n");
    sb.append("    postcode: ").append(toIndentedString(postcode)).append("\n");
    sb.append("    state: ").append(toIndentedString(state)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
