/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.services.openbanking.converter;

import org.modelmapper.ModelMapper;

/**
 * This is used to wrap a singleton model mapper so config can be set in one place without requiring DI/Spring in a common lib.
 * It also allows null handling to be standard for OB classes and the map method to be static.
 */
public class FRModelMapper {

    private static final ModelMapper modelMapper = new ModelMapper();
    static {
        // Set project wide config here
        modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    /**
     * Recommended to only use this for classes with identical members (of which there are many in OB SDK!) with default mapping bwhaciour as this
     * avoids a lot of complexity and errors from mapping configs.
     * @param source Source
     * @param clazz Target type
     * @param <T> Target type
     * @param <V> Source type
     * @return Target
     */
    public static  <T,V> T map(V source, Class<T> clazz) {
        if (source==null) return null;
        return modelMapper.map(source, clazz);
    }
}
