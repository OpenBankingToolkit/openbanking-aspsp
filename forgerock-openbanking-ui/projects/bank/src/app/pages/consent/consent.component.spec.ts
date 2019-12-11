import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonModule } from '@angular/common';
import { StoreModule } from '@ngrx/store';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import rootReducer from 'bank/src/store';
import { MatSharedModule } from 'bank/src/app/mat-shared.module';
import { TranslateSharedModule } from 'bank/src/app/translate-shared.module';
import { ForgerockSharedModule } from 'ob-ui-libs/shared';
import { ForgerockCustomerLogoModule } from 'ob-ui-libs/components/forgerock-customer-logo';

import { ConsentComponent } from './consent.component';
import { DynamicComponent } from './dynamic/dynamic.component';

import { ConsentBoxComponentModule } from './components/consent-box/consent-box.module';
import { SubmitBoxComponentModule } from './components/submit-box/submit-box.module';
import { AccountSelectionComponentModule } from './components/account-selection/account-selection.module';
import { AccountCheckboxModule } from './components/account-checkbox/account-checkbox.module';

import { ApiService } from 'bank/src/app/services/api.service';
import { ForgerockMainLayoutSharedModule } from 'ob-ui-libs/layouts/main-layout';

describe('app:bank ConsentComponent', () => {
  let component: ConsentComponent;
  let fixture: ComponentFixture<ConsentComponent>;
  let apiService: ApiService;
  let postConsentDecisionSpy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConsentComponent, DynamicComponent],
      imports: [
        ForgerockMainLayoutSharedModule,
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
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsentComponent);
    component = fixture.componentInstance;
    apiService = fixture.debugElement.injector.get(ApiService);
    postConsentDecisionSpy = spyOn(apiService, 'postConsentDecision').and.callThrough();

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should call postConsentDecision on the service', () => {
    const decisionAPIUri = 'https://test.com';
    component.response = {
      decisionAPIUri
    };
    fixture.detectChanges();

    expect(component.loading).toBeFalsy();

    component.onFormSubmit();

    expect(component.loading).toBeTruthy();

    expect(postConsentDecisionSpy).toHaveBeenCalledWith(decisionAPIUri, {});

    component.onFormSubmit({
      test: 'test'
    });

    expect(postConsentDecisionSpy).toHaveBeenCalledWith(decisionAPIUri, {
      test: 'test'
    });
  });

  it('should not call postConsentDecision if no decisionAPIUri specified', () => {
    component.onFormSubmit();

    expect(postConsentDecisionSpy).not.toHaveBeenCalled();
  });
});
