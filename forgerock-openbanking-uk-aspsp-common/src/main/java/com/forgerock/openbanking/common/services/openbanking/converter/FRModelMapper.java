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
