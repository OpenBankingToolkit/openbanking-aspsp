import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { CdrConsentPermissionsComponent } from './cdr-consent-permissions.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';

describe('CdrConsentPermissionsComponent', () => {
  let component: CdrConsentPermissionsComponent;
  let fixture: ComponentFixture<CdrConsentPermissionsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CdrConsentPermissionsComponent],
      imports: [CommonModule, TranslateModule.forRoot()]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CdrConsentPermissionsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
