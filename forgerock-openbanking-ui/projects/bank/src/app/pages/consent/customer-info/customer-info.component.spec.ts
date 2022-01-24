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

import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CustomerInfoComponent} from './customer-info.component';
import {CommonModule} from "@angular/common";
import {MatSharedModule} from "bank/src/app/mat-shared.module";
import {TranslateSharedModule} from "bank/src/app/translate-shared.module";
import {ForgerockCustomerLogoModule} from "@forgerock/openbanking-ngx-common/components/forgerock-customer-logo";
import {ForgerockSharedModule} from "@forgerock/openbanking-ngx-common/shared";
import {StoreModule} from "@ngrx/store";
import rootReducer from "bank/src/store";
import {TranslateModule} from "@ngx-translate/core";
import {SubmitBoxComponentModule} from "bank/src/app/pages/consent/components/submit-box/submit-box.module";
import {ConsentBoxComponentModule} from "bank/src/app/pages/consent/components/consent-box/consent-box.module";
import {AddressItemComponent} from "bank/src/app/pages/consent/components/consent-box/items/adress-item/address-item.component";
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";

describe('CustomerInfoComponent', () => {
  let component: CustomerInfoComponent;
  let fixture: ComponentFixture<CustomerInfoComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CustomerInfoComponent],
      imports: [
        CommonModule,
        MatSharedModule,
        TranslateSharedModule,
        ForgerockCustomerLogoModule,
        ForgerockSharedModule,
        StoreModule.forRoot(rootReducer),
        TranslateModule.forRoot(),
        SubmitBoxComponentModule,
        ConsentBoxComponentModule
      ]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CustomerInfoComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should emit formSubmit decision deny by default', () => {
    spyOn(component.formSubmit, 'emit');

    component.submit();
    fixture.detectChanges();

    expect(component.formSubmit.emit).toHaveBeenCalledWith({
      decision: ConsentDecision.DENY
    });
  });

  it('should emit formSubmit decision deny', () => {
    spyOn(component.formSubmit, 'emit');

    component.submit(false);
    fixture.detectChanges();

    expect(component.formSubmit.emit).toHaveBeenCalledWith({
      decision: ConsentDecision.DENY
    });
  });

  it('should emit formSubmit decision allow', () => {
    spyOn(component.formSubmit, 'emit');

    component.submit(true);
    fixture.detectChanges();

    expect(component.formSubmit.emit).toHaveBeenCalledWith({
      decision: ConsentDecision.ALLOW
    });
  });
});
