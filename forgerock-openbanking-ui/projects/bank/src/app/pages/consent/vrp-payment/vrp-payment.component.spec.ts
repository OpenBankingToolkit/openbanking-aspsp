import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VrpPaymentComponent } from './vrp-payment.component';
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
import {AccountCheckboxModule} from "bank/src/app/pages/consent/components/account-checkbox/account-checkbox.module";
import {AccountSelectionComponentModule} from "bank/src/app/pages/consent/components/account-selection/account-selection.module";
import {PermissionsComponent} from "bank/src/app/pages/consent/permissions/permissions.component";
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";

describe('VrpPaymentComponent', () => {
  let component: VrpPaymentComponent;
  let fixture: ComponentFixture<VrpPaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VrpPaymentComponent, PermissionsComponent ],
      imports: [
        CommonModule,
        MatSharedModule,
        TranslateSharedModule,
        ForgerockCustomerLogoModule,
        ForgerockSharedModule,
        StoreModule.forRoot(rootReducer),
        TranslateModule.forRoot(),
        SubmitBoxComponentModule,
        ConsentBoxComponentModule,
        AccountCheckboxModule,
        AccountSelectionComponentModule
      ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(VrpPaymentComponent);
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
    const testValue = 'test';
    spyOn(component.formSubmit, 'emit');

    component.submit(false);
    fixture.detectChanges();

    expect(component.formSubmit.emit).toHaveBeenCalledWith({
      decision: ConsentDecision.DENY
    });
  });

  it('should emit formSubmit decision allow', () => {
    const testValue = 'test';
    spyOn(component.formSubmit, 'emit');

    component.submit(true);
    fixture.detectChanges();

    expect(component.formSubmit.emit).toHaveBeenCalledWith({
      decision: ConsentDecision.ALLOW
    });
  });

});
