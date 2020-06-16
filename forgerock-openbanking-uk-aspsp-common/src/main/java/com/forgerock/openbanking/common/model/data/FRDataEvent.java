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
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@ApiModel(
        description = "The resource event."
)
@Validated
public class FRDataEvent {

    @JsonProperty("tppId")
    private String tppId = null;

    @JsonProperty("jti")
    private String jti = null;

    @JsonProperty("events")
    private List<OBEventNotification2> obEventNotification2List = new ArrayList();

    public FRDataEvent tppId(String tppId) {
        this.tppId = tppId;
        return this;
    }

    public FRDataEvent jti(String jti) {
        this.jti = jti;
        return this;
    }

    public FRDataEvent obEventNotification2(List<OBEventNotification2> obEventNotification2List) {
        this.obEventNotification2List = obEventNotification2List;
        return this;
    }

    public FRDataEvent addOBEventNotification2Item(OBEventNotification2 obEventNotification2) {
        this.obEventNotification2List.add(obEventNotification2);
        return this;
    }

    /**
     * tpp ID.
     * @return tppId
     **/
    @ApiModelProperty(required = true, value = "TPP ID.")
    @NotNull
    @Size(min = 1, max = 128)
    public String getTppId() {
        return tppId;
    }

    public void setTppId(String tppId) {
        this.tppId = tppId;
    }

    /**
     * JWT ID.
     * @return jti
     **/
    @ApiModelProperty(value = "JWT ID.")

    @Size(min = 1, max = 128)
    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }


    @ApiModelProperty(
            value = "Resource links to other available versions of the resource."
    )
    public List<OBEventNotification2> getObEventNotification2List() {
        return this.obEventNotification2List;
    }

    public void setObEventNotification2List(List<OBEventNotification2> obEventNotification2List) {
        this.obEventNotification2List = obEventNotification2List;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FRDataEvent frDataEvent = (FRDataEvent) o;
        return Objects.equals(this.tppId, frDataEvent.tppId) &&
                Objects.equals(this.jti, frDataEvent.jti) &&
                Objects.equals(this.obEventNotification2List, frDataEvent.obEventNotification2List);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tppId, jti, obEventNotification2List);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("class ").append(this.getClass().getSimpleName()).append(" {\n");
        sb.append("    tppId: ").append(toIndentedString(tppId)).append("\n");
        sb.append("    jti: ").append(toIndentedString(jti)).append("\n");
        sb.append("    events: ").append(this.toIndentedString(this.obEventNotification2List)).append("\n");
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
