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

import { Pipe, PipeTransform } from '@angular/core';
import { OBCashAccount3 } from 'bank/src/app/types/ob';

@Pipe({
  name: 'AccountNumberFormat',
  pure: false
})
export class AccountNumberFormatPipe implements PipeTransform {
  transform(account: OBCashAccount3): string {
    if (account.SchemeName.includes('SortCodeAccountNumber')) {
      return account.Identification.replace(/(\d{2})(\d{2})(\d{2})(\d{8})/g, '$4');
    } else if ('IBAN' === account.SchemeName) {
      return account.Identification;
    }
    return account.Identification;
  }
}
