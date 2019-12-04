/**
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
package com.forgerock.openbanking.aspsp.rs.utils;


import com.forgerock.openbanking.aspsp.rs.store.utils.PaginationUtil;
import org.junit.Test;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.account.Links;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.IsEqual.equalTo;

public class PaginationUtilTest {

    private static final String BASE_URL_BY_ID = "/path/1/resource";

    @Test
    public void NoDataFoundShouldReturnOnePageLink() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(BASE_URL_BY_ID);
        Links links = PaginationUtil.generateLinks(uriComponentsBuilder, 0, 0);
        assertThat(links.getSelf(), is(equalTo("/path/1/resource")));
        assertThat(links.getFirst(), nullValue());
        assertThat(links.getLast(), nullValue());
        assertThat(links.getNext(), nullValue());
        assertThat(links.getPrev(), nullValue());
    }

    @Test
    public void OneDataFoundShouldReturnOnePageLink() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(BASE_URL_BY_ID);
        Links links = PaginationUtil.generateLinks(uriComponentsBuilder, 0, 1);
        assertThat(links.getSelf(), is(equalTo("/path/1/resource")));
        assertThat(links.getFirst(), nullValue());
        assertThat(links.getLast(), nullValue());
        assertThat(links.getNext(), nullValue());
        assertThat(links.getPrev(), nullValue());
    }

    @Test
    public void DataFitsMultiplePagesFirstPageRequestedFoundShouldReturnMultiplePagesLink() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(BASE_URL_BY_ID);
        Links links = PaginationUtil.generateLinks(uriComponentsBuilder, 0, 5);
        assertThat(links.getSelf(), is(equalTo("/path/1/resource")));
        assertThat(links.getFirst(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 0)));
        assertThat(links.getLast(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 4)));
        assertThat(links.getNext(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 1)));
        assertThat(links.getPrev(), nullValue());
    }

    @Test
    public void DataFitsMultiplePageSecondPageRequestedFoundShouldReturnMultiplePagesLink() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(BASE_URL_BY_ID);
        Links links = PaginationUtil.generateLinks(uriComponentsBuilder, 1, 4);
        assertThat(links.getSelf(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 1)));
        assertThat(links.getFirst(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 0)));
        assertThat(links.getLast(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 3)));
        assertThat(links.getNext(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 2)));
        assertThat(links.getPrev(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 0)));
    }

    @Test
    public void DataFitsMultiplePagesLastPageRequestedFoundShouldReturnMultiplePagesLink() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(BASE_URL_BY_ID);
        Links links = PaginationUtil.generateLinks(uriComponentsBuilder, 4, 5);
        assertThat(links.getSelf(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 4)));
        assertThat(links.getFirst(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 0)));
        assertThat(links.getLast(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 4)));
        assertThat(links.getNext(), nullValue());
        assertThat(links.getPrev(), is(equalTo("/path/1/resource" + "?" + PaginationUtil.PAGE + "=" + 3)));
    }

    @Test
    public void DataFitsMultiplePageSecondPageRequestedWithOtherParamFoundShouldReturnMultiplePagesLink() {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromPath(BASE_URL_BY_ID).queryParam("test", "toto");
        Links links = PaginationUtil.generateLinks(uriComponentsBuilder, 1, 4);
        assertThat(links.getSelf(), is(equalTo("/path/1/resource" + "?test=toto&" + PaginationUtil.PAGE + "=" + 1)));
        assertThat(links.getFirst(), is(equalTo("/path/1/resource?test=toto&" + PaginationUtil.PAGE + "=" + 0)));
        assertThat(links.getLast(), is(equalTo("/path/1/resource" + "?test=toto&" + PaginationUtil.PAGE + "=" + 3)));
        assertThat(links.getNext(), is(equalTo("/path/1/resource" + "?test=toto&" + PaginationUtil.PAGE + "=" + 2)));
        assertThat(links.getPrev(), is(equalTo("/path/1/resource" + "?test=toto&" + PaginationUtil.PAGE + "=" + 0)));
    }

}
