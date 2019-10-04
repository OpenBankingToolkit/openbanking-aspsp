/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
package com.forgerock.openbanking.common.openbanking;

import org.springframework.web.bind.annotation.RequestMapping;

import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping
public @interface OpenBankingAPI {

    String obVersion() default "";

    OBReference obReference() default OBReference.NONE;

    OBGroupName obGroupName() default OBGroupName.NONE;
}
