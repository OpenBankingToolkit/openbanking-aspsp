/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
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
