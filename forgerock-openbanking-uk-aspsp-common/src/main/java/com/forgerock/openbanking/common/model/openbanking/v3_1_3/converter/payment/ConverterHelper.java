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
package com.forgerock.openbanking.common.model.openbanking.v3_1_3.converter.payment;

import java.lang.reflect.Field;

/**
 * Helper class providing utility methods for object conversions.
 */
class ConverterHelper {

    /**
     * Uses reflection to copy a field from one object to another.
     *
     * @param newObject      The new object that the field's value is to be copied to.
     * @param originalObject The original object that the field's value is to be copied from.
     * @param fieldName      The name of the field concerned.
     * @param <T>            The type of the new object.
     * @param <U>            The type of the original object.
     */
    static <T, U> void copyField(T newObject, U originalObject, String fieldName) {
        if (originalObject == null) {
            return; // optional fields may be null
        }
        if (newObject == null) {
            throw new IllegalArgumentException("Cannot copy value to a null object");
        }
        try {
            Field newField = newObject.getClass().getDeclaredField(fieldName);
            newField.setAccessible(true);
            Field originalField = originalObject.getClass().getDeclaredField(fieldName);
            originalField.setAccessible(true);
            newField.set(newObject, originalField.get(originalObject));
        } catch (Exception e) {
            throw new IllegalStateException("Unable to set value for field " + fieldName, e);
        }
    }
}
