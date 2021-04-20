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
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.core.io.Resource;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class StatementPDFServiceTest {

    private static final String BUCKET_NAME = "ob-sandbox-storage";

    @Mock
    private GCPBucketStorageAccessor gcpBucketStorageAccessor;

    @Mock
    private Storage storage;

    @Before
    public void setup() {
        given(storage.get(BUCKET_NAME)).willReturn(mock(Bucket.class));
        given(gcpBucketStorageAccessor.getStorage()).willReturn(storage);
    }

    @Test
    public void getPdfStatement_test_found() {
        // Given
        String resourcePath = "statements/statement-exists.pdf";
        given(storage.get(isA(BlobId.class))).willReturn(mock(Blob.class));
        given(storage.readAllBytes(isA(BlobId.class))).willReturn(new byte[0]);
        StatementPDFService statementPDFService = new StatementPDFService(gcpBucketStorageAccessor, BUCKET_NAME, resourcePath);

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isTrue();
        assertThat(resource.orElseThrow(IllegalStateException::new).getDescription())
                .containsIgnoringCase("[Resource " + BUCKET_NAME + "/" + resourcePath + " loaded through InputStream]");
    }

    @Test
    public void getPdfStatement_notFound() {
        // Given
        String resourcePath = "statements/statement-does-not-exist.pdf";
        given(storage.get(isA(BlobId.class))).willReturn(null);
        StatementPDFService statementPDFService = new StatementPDFService(gcpBucketStorageAccessor, BUCKET_NAME, resourcePath);

        // When
        Optional<Resource> resource = statementPDFService.getPdfStatement();

        // Then
        assertThat(resource.isPresent()).isFalse();
    }

}
