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
