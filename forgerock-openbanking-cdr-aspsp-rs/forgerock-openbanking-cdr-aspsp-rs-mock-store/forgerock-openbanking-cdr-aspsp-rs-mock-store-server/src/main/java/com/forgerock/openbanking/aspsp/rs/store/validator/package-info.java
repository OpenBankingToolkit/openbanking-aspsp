/**
 * Copyright 2019 ForgeRock AS. All Rights Reserved
 *
 * Use of this code requires a commercial software license with ForgeRock AS.
 * or with one of its affiliates. All use shall be exclusively subject
 * to such license between the licensee and ForgeRock AS.
 */
/**
 * While most validation is performed at the rs-api stage, there are some validation that require comparison of the submitted request against existing data
 * e.g. an uploaded file against the corresponding consent from the data store. In these cases it is simpler and more efficient to validate at the rs-store level before persisting.
 * Validation logic should be put into this package so that it can be easily tested outside of Spring controllers.
 */
package com.forgerock.openbanking.aspsp.rs.store.validator;