import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonModule } from '@angular/common';
import { StoreModule } from '@ngrx/store';
import { RouterTestingModule } from '@angular/router/testing';
import { TranslateModule } from '@ngx-translate/core';
import { HttpClientTestingModule } from '@angular/common/http/testing';

import rootReducer from 'bank/src/store';

import { CDRConsentComponent } from './cdr-consent.component';

import { ApiService } from 'bank/src/app/services/api.service';
import { ForgerockMainLayoutSharedModule } from '@forgerock/openbanking-ngx-common/layouts/main-layout';
import { CDRConsentModule } from '../../components/cdr-consent/cdr-consent.module';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';

describe('app:bank CDRConsentComponent', () => {
  let component: CDRConsentComponent;
  let fixture: ComponentFixture<CDRConsentComponent>;
  let apiService: ApiService;
  let postConsentDecisionSpy;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CDRConsentComponent],
      imports: [
        CommonModule,
        ForgerockMainLayoutSharedModule,
        RouterTestingModule.withRoutes([]),
        StoreModule.forRoot(rootReducer),
        TranslateModule.forRoot(),
        HttpClientTestingModule,
        MatProgressSpinnerModule,
        CDRConsentModule
      ],
      providers: [ApiService]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CDRConsentComponent);
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
