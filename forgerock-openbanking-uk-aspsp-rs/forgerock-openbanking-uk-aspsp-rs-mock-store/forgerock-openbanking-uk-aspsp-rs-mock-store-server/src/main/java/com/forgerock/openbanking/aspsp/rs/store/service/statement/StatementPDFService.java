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
package com.forgerock.openbanking.aspsp.rs.store.service.statement;

import com.forgerock.openbanking.common.gcp.GCPBucketStorageAccessor;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service to fetch a PSU statement resource file from a gcp bucket<br>
 * <pre>{@code
 * // yaml configuration
 * gcp-storage:
 *   project-id: openbanking-214714 # default value
 *     statements:
 *       bucket-name: ob-sandbox-storage # default value
 *       resource: statements/default/fr-statement.pdf # default value
 * }</pre>
 */
@Service
@Slf4j
public class StatementPDFService {

    private final GCPBucketStorageAccessor bucketStorageAccessor;
    private final String bucketName;
    private final String resource;

    public StatementPDFService(GCPBucketStorageAccessor bucketStorageAccessor,
                               @Value("${gcp-storage.statements.bucket-name:ob-sandbox-storage}") String bucketName,
                               @Value("${gcp-storage.statements.resource:statements/default/fr-statement.pdf}") String resource) {
        this.bucketStorageAccessor = bucketStorageAccessor;
        this.bucketName = bucketName;
        this.resource = resource;
    }

    /*
     * The following code only works when the credentials are defined via the environment variable
     * GOOGLE_APPLICATION_CREDENTIALS, and those credentials are authorized to access the bucket
     */

    /**
     * Fetch a resource from the GCP bucket
     * @return optional Resource if found otherwise optional empty
     */
    public Optional<Resource> getPdfStatement() {
        try {
            Storage storage = bucketStorageAccessor.getStorage();
            if (storage.get(bucketName) != null) {
                Blob blob = storage.get(BlobId.of(bucketName, resource));
                if (blob != null) {
                    log.debug("Found statement resource {} in '{}'", resource, bucketName);
                    return Optional.of(
                            getByteArrayResource(
                                    storage.readAllBytes(
                                            BlobId.of(bucketName, resource)
                                    )
                            )
                    );
                }
            }
            log.warn("Statement resource {} not found in '{}'", resource, bucketName);
        } catch (StorageException exception) {
            log.error(exception.getMessage());
        }
        return Optional.empty();
    }

    private ByteArrayResource getByteArrayResource(byte[] byteArray) {
        // https://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/io/InputStreamResource.html
        // The docs suggest using ByteArrayResource to cache the content in memory, rather than InputStreamResource.
        // to avoid "java.lang.IllegalStateException: InputStream has already been read - do not use InputStreamResource if a stream needs to be read multiple times"
        return new ByteArrayResource(byteArray,
                String.format("Resource %s/%s loaded through InputStream", bucketName, resource));
    }

}
