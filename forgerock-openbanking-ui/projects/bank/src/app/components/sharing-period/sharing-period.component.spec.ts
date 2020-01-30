import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharingPeriodComponent } from './sharing-period.component';
import { TranslateModule } from '@ngx-translate/core';
import { CommonModule } from '@angular/common';
import { ForgerockPipesModule } from '@forgerock/openbanking-ngx-common/pipes';

describe('SharingPeriodComponent', () => {
  let component: SharingPeriodComponent;
  let fixture: ComponentFixture<SharingPeriodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SharingPeriodComponent],
      imports: [CommonModule, TranslateModule.forRoot(), ForgerockPipesModule]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(SharingPeriodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
