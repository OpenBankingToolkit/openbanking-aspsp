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
/**
 * While most validation is performed at the rs-api stage, there are some validation that require comparison of the submitted request against existing data
 * e.g. an uploaded file against the corresponding consent from the data store. In these cases it is simpler and more efficient to validate at the rs-store level before persisting.
 * Validation logic should be put into this package so that it can be easily tested outside of Spring controllers.
 */
package com.forgerock.openbanking.aspsp.rs.store.validator;