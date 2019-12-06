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
package com.forgerock.openbanking.aspsp.rs.store.utils;

import org.joda.time.DateTime;
import org.springframework.web.util.UriComponentsBuilder;
import uk.org.openbanking.datamodel.account.Links;
import uk.org.openbanking.datamodel.account.Meta;

public class PaginationUtil {

    public static final String PAGE = "page";

    public static Links generateLinks(String httpUrl, int page, int totalPages) {
        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                .fromHttpUrl(httpUrl);
        return generateLinks(uriComponentsBuilder, page, totalPages);
    }

    public static Links generateLinks(UriComponentsBuilder uriComponentsBuilder, int page, int totalPages) {
        Links links = new Links();

        String resourceUrl = uriComponentsBuilder.build().encode().toUriString();
        if (isFirstPage(page)) {
            links.setSelf(resourceUrl);
            if (totalPages > 1) {
                links.setFirst(getUrlWithPage(0, uriComponentsBuilder));
                //no previous
                links.setNext(getUrlWithPage(page + 1, uriComponentsBuilder));
                links.setLast(getUrlWithPage(totalPages - 1, uriComponentsBuilder));
            }
        } else if (isLastPage(page, totalPages)) {
            links.setSelf(getUrlWithPage(page, uriComponentsBuilder));
            links.setFirst(getUrlWithPage(0, uriComponentsBuilder));
            links.setPrev(getUrlWithPage(page - 1, uriComponentsBuilder));
            //No next
            links.setLast(getUrlWithPage(totalPages - 1, uriComponentsBuilder));
        } else {
            links.setSelf(getUrlWithPage(page, uriComponentsBuilder));
            links.setFirst(getUrlWithPage(0, uriComponentsBuilder));
            links.setPrev(getUrlWithPage(page - 1, uriComponentsBuilder));
            links.setNext(getUrlWithPage(page + 1, uriComponentsBuilder));
            links.setLast(getUrlWithPage(totalPages - 1, uriComponentsBuilder));
        }
        return links;
    }

    public static Links generateLinksOnePager(String httpUrl) {
        String resourceUrl = UriComponentsBuilder.fromHttpUrl(httpUrl).toUriString();

        Links links = new Links();
        links.setSelf(resourceUrl);
        return links;
    }

    public static Meta generateMetaData(int totalPages) {
        return generateMetaData(totalPages, null, null);
    }

    public static Meta generateMetaData(int totalPages, DateTime firstAvailableDate, DateTime lastAvailableDate) {
        Meta metaData = new Meta();
        metaData.setTotalPages(totalPages);
        if (firstAvailableDate != null) {
            metaData.setFirstAvailableDateTime(firstAvailableDate);
        }
        if (lastAvailableDate != null) {
            metaData.setLastAvailableDateTime(lastAvailableDate);
        }
        return metaData;
    }

    private static String getUrlWithPage(int page, UriComponentsBuilder builder) {
        return builder.replaceQueryParam(PAGE, page).build().encode().toUriString();
    }

    private static boolean isLastPage(int page, int totalPages) {
        return page == totalPages - 1;
    }

    private static boolean isFirstPage(int page) {
        return page == 0;
    }

}
