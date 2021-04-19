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
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package com.forgerock.openbanking.common.gcp;

import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;

public class GCPBucketStorageAccessor {

    private static String PROJECT_ID;

    private static volatile Storage storage = null;

    /* to inject the value in the static variable */
    @Value("${gcp-storage.project-id:openbanking-214714}")
    public void setProjectId(String projectId) {
        PROJECT_ID = projectId;
    }

    /**
     * Generic method to get the storage gcp object from a specific projectId
     * @return storage gcp object
     */
    public static Storage getStorage() {
        if (storage != null) {
            return storage;
        } else {
            return StorageOptions.newBuilder().setProjectId(PROJECT_ID).build().getService();
        }
    }
}
