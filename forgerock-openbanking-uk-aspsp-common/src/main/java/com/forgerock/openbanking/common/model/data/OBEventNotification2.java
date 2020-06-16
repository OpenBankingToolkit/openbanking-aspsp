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
package com.forgerock.openbanking.common.model.data;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.forgerock.openbanking.common.services.store.data.DataEventsService;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;
import uk.org.openbanking.datamodel.event.OBEvent1;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

/**
 * A resource used within <code>/api/data/events</code> that allows static data to be pushed into the system.
 * This data will then be returned by the P2 aggregated polling API at /events.
 *
 * <p>The EventNotificationAPISpecification describes the object OBEventNotification2 that contains the events claim.
 * However, this object is not defined in the swagger specification.
 * Instead the spec defines {@link uk.org.openbanking.datamodel.event.OBEventNotification1} which is used around the sandbox source.
 *
 * <p>This object has been created to provide a collection of events. It is intended to be isolated within {@link FRDataEvent} for the following purposes only:
 * <ul>
 *     <li>Import events {@link DataEventsService#importEvents(FRDataEvent)}</li>
 *     <li>Update events {@link DataEventsService#updateEvents(FRDataEvent)}</li>
 *     <li>Remove events {@link DataEventsService#removeEvents(FRDataEvent)}</li>
 *     <li>Export events {@link DataEventsService#exportEventsByTppId(FRDataEvent)}</li>
 * </ul>
 * <p>This object isn't part of the openbanking spec source datamodel created from swagger files descriptors.</p>
 */
@ApiModel(
        description = "The resource import event."
)
@Validated
@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2020-06-02T13:06:33.606299+01:00[Europe/London]")
public class OBEventNotification2 {
    @JsonProperty("iss")
    private String iss = null;

    @JsonProperty("iat")
    private Integer iat = null;

    @JsonProperty("jti")
    private String jti = null;

    @JsonProperty("aud")
    private String aud = null;

    @JsonProperty("sub")
    private String sub = null;

    @JsonProperty("txn")
    private String txn = null;

    @JsonProperty("toe")
    private Integer toe = null;

    @JsonProperty("events")
    private OBEvent1 events = null;

    public OBEventNotification2 iss(String iss) {
        this.iss = iss;
        return this;
    }

    /**
     * Issuer.
     * @return iss
     **/
    @ApiModelProperty(required = true, value = "Issuer.")
    @NotNull

    public String getIss() {
        return iss;
    }

    public void setIss(String iss) {
        this.iss = iss;
    }

    public OBEventNotification2 iat(Integer iat) {
        this.iat = iat;
        return this;
    }

    /**
     * Issued At.
     * minimum: 0
     * @return iat
     **/
    @ApiModelProperty(required = true, value = "Issued At. ")
    @NotNull

    @Min(0)
    public Integer getIat() {
        return iat;
    }

    public void setIat(Integer iat) {
        this.iat = iat;
    }

    public OBEventNotification2 jti(String jti) {
        this.jti = jti;
        return this;
    }

    /**
     * JWT ID.
     * @return jti
     **/
    @ApiModelProperty(required = true, value = "JWT ID.")
    @NotNull

    @Size(min = 1, max = 128)
    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }

    public OBEventNotification2 aud(String aud) {
        this.aud = aud;
        return this;
    }

    /**
     * Audience.
     * @return aud
     **/
    @ApiModelProperty(required = true, value = "Audience.")
    @NotNull

    @Size(min = 1, max = 128)
    public String getAud() {
        return aud;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    public OBEventNotification2 sub(String sub) {
        this.sub = sub;
        return this;
    }

    /**
     * Subject
     * @return sub
     **/
    @ApiModelProperty(required = true, value = "Subject")
    @NotNull

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public OBEventNotification2 txn(String txn) {
        this.txn = txn;
        return this;
    }

    /**
     * Transaction Identifier.
     * @return txn
     **/
    @ApiModelProperty(required = true, value = "Transaction Identifier.")
    @NotNull

    @Size(min = 1, max = 128)
    public String getTxn() {
        return txn;
    }

    public void setTxn(String txn) {
        this.txn = txn;
    }

    public OBEventNotification2 toe(Integer toe) {
        this.toe = toe;
        return this;
    }

    /**
     * Time of Event.
     * minimum: 0
     * @return toe
     **/
    @ApiModelProperty(required = true, value = "Time of Event.")
    @NotNull

    @Min(0)
    public Integer getToe() {
        return toe;
    }

    public void setToe(Integer toe) {
        this.toe = toe;
    }

    public OBEventNotification2 events(OBEvent1 events) {
        this.events = events;
        return this;
    }

    /**
     * Get events
     * @return events
     **/
    @ApiModelProperty(required = true, value = "")
    @NotNull

    @Valid
    public OBEvent1 getEvents() {
        return events;
    }

    public void setEvents(OBEvent1 events) {
        this.events = events;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OBEventNotification2 OBEventNotification2 = (OBEventNotification2) o;
        return Objects.equals(this.iss, OBEventNotification2.iss) &&
                Objects.equals(this.iat, OBEventNotification2.iat) &&
                Objects.equals(this.jti, OBEventNotification2.jti) &&
                Objects.equals(this.aud, OBEventNotification2.aud) &&
                Objects.equals(this.sub, OBEventNotification2.sub) &&
                Objects.equals(this.txn, OBEventNotification2.txn) &&
                Objects.equals(this.toe, OBEventNotification2.toe) &&
                Objects.equals(this.events, OBEventNotification2.events);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iss, iat, jti, aud, sub, txn, toe, events);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class OBEventNotification2 {\n");

        sb.append("    iss: ").append(toIndentedString(iss)).append("\n");
        sb.append("    iat: ").append(toIndentedString(iat)).append("\n");
        sb.append("    jti: ").append(toIndentedString(jti)).append("\n");
        sb.append("    aud: ").append(toIndentedString(aud)).append("\n");
        sb.append("    sub: ").append(toIndentedString(sub)).append("\n");
        sb.append("    txn: ").append(toIndentedString(txn)).append("\n");
        sb.append("    toe: ").append(toIndentedString(toe)).append("\n");
        sb.append("    events: ").append(toIndentedString(events)).append("\n");
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

