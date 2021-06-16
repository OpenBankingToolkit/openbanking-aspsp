import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CancelComponent } from './cancel.component';
import {ApiService} from "bank/src/app/services/api.service";
import {ForgerockSharedModule} from "@forgerock/openbanking-ngx-common/shared";
import {RouterTestingModule} from "@angular/router/testing";
import {CommonModule} from "@angular/common";
import {MatSharedModule} from "bank/src/app/mat-shared.module";
import {TranslateSharedModule} from "bank/src/app/translate-shared.module";
import {ForgerockCustomerLogoModule} from "@forgerock/openbanking-ngx-common/components/forgerock-customer-logo";
import {StoreModule} from "@ngrx/store";
import rootReducer from "bank/src/store";
import {TranslateModule} from "@ngx-translate/core";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {ConsentBoxComponentModule} from "bank/src/app/pages/consent/components/consent-box/consent-box.module";
import {SubmitBoxComponentModule} from "bank/src/app/pages/consent/components/submit-box/submit-box.module";
import {AccountSelectionComponentModule} from "bank/src/app/pages/consent/components/account-selection/account-selection.module";
import {AccountCheckboxModule} from "bank/src/app/pages/consent/components/account-checkbox/account-checkbox.module";

describe('CancelComponent', () => {
  let component: CancelComponent;
  let fixture: ComponentFixture<CancelComponent>;
  let apiService: ApiService;
  let postConsentDecisionSpy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ CancelComponent ],
      imports: [
        ForgerockSharedModule,
        RouterTestingModule.withRoutes([]),
        CommonModule,
        MatSharedModule,
        TranslateSharedModule,
        ForgerockCustomerLogoModule,
        ForgerockSharedModule,
        StoreModule.forRoot(rootReducer),
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        ConsentBoxComponentModule,
        SubmitBoxComponentModule,
        AccountSelectionComponentModule,
        AccountCheckboxModule
      ],
      providers: [ApiService]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CancelComponent);
    component = fixture.componentInstance;
    apiService = fixture.debugElement.injector.get(ApiService);
    postConsentDecisionSpy = spyOn(apiService, 'postConsentDecision').and.callThrough();
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
