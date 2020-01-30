import { async, ComponentFixture, TestBed } from '@angular/core/testing';
import { CommonModule } from '@angular/common';
import { StoreModule } from '@ngrx/store';
import { TranslateModule } from '@ngx-translate/core';

import rootReducer from 'bank/src/store';
import { MatSharedModule } from 'bank/src/app/mat-shared.module';
import { TranslateSharedModule } from 'bank/src/app/translate-shared.module';
import { ForgerockSharedModule } from '@forgerock/openbanking-ngx-common/shared';
import { ForgerockCustomerLogoModule } from '@forgerock/openbanking-ngx-common/components/forgerock-customer-logo';

import { ConsentBoxComponentModule } from '../../consent-box/consent-box.module';
import { SubmitBoxComponentModule } from '../../submit-box/submit-box.module';
import { AccountCheckboxModule } from '../../account-checkbox/account-checkbox.module';

import { CDRAccountComponent } from './account.component';
import { CDRDataRecipientInfoComponentModule } from '../../cdr-data-recipient-info/cdr-data-recipient-info.module';
import { SharingPeriodModule } from '../../sharing-period/sharing-period.module';
import { ConsentStepsModule } from '../../consent-steps/consent-steps.module';
import { AccountSelectedModule } from '../../account-selected/account-selected.module';
import { CdrConsentPermissionsModule } from '../../cdr-consent-permissions/cdr-consent-permissions.module';
import { ForgerockPipesModule } from '@forgerock/openbanking-ngx-common/pipes';

describe('app:bank CDRAccountComponent', () => {
  let component: CDRAccountComponent;
  let fixture: ComponentFixture<CDRAccountComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CDRAccountComponent],
      imports: [
        CommonModule,
        MatSharedModule,
        ForgerockCustomerLogoModule,
        ForgerockSharedModule,
        StoreModule.forRoot(rootReducer),
        TranslateModule.forRoot(),
        CommonModule,
        MatSharedModule,
        ForgerockSharedModule,
        ForgerockCustomerLogoModule,
        CDRDataRecipientInfoComponentModule,
        AccountCheckboxModule,
        SharingPeriodModule,
        ConsentStepsModule,
        AccountSelectedModule,
        CdrConsentPermissionsModule,
        ForgerockPipesModule
      ]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CDRAccountComponent);
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
      decision: 'deny',
      sharedAccounts: []
    });
  });

  it('should emit formSubmit decision deny', () => {
    spyOn(component.formSubmit, 'emit');

    component.submit(false);
    fixture.detectChanges();

    expect(component.formSubmit.emit).toHaveBeenCalledWith({
      decision: 'deny',
      sharedAccounts: []
    });
  });

  it('should emit formSubmit decision allow', () => {
    spyOn(component.formSubmit, 'emit');

    component.submit(true);
    fixture.detectChanges();

    expect(component.formSubmit.emit).toHaveBeenCalledWith({
      decision: 'allow',
      sharedAccounts: []
    });
  });
});
