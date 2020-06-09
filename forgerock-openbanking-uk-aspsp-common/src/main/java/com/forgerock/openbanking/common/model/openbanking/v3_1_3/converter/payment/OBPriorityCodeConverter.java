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

import uk.org.openbanking.datamodel.payment.OBPriority2Code;
import uk.org.openbanking.datamodel.payment.OBWriteInternational3DataInitiation;
import uk.org.openbanking.datamodel.payment.OBWriteInternationalScheduled3DataInitiation;

public class OBPriorityCodeConverter {

    public static OBPriority2Code toOBPriority2Code(OBWriteInternational3DataInitiation.InstructionPriorityEnum instructionPriority) {
        return instructionPriority == null ? null : OBPriority2Code.valueOf(instructionPriority.name());
    }

    public static OBPriority2Code toOBPriority2Code(OBWriteInternationalScheduled3DataInitiation.InstructionPriorityEnum instructionPriority) {
        return instructionPriority == null ? null : OBPriority2Code.valueOf(instructionPriority.name());
    }

    public static OBWriteInternational3DataInitiation.InstructionPriorityEnum toOBWriteInternational3DataInitiationInstructionPriorityEnum(OBPriority2Code instructionPriority) {
        return instructionPriority == null ? null : OBWriteInternational3DataInitiation.InstructionPriorityEnum.valueOf(instructionPriority.name());
    }

    public static OBWriteInternationalScheduled3DataInitiation.InstructionPriorityEnum toOBWriteInternationalScheduled3DataInitiationInstructionPriorityEnum(OBPriority2Code instructionPriority) {
        return instructionPriority == null ? null : OBWriteInternationalScheduled3DataInitiation.InstructionPriorityEnum.valueOf(instructionPriority.name());
    }

}
