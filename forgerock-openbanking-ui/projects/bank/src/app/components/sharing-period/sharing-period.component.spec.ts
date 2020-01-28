import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { SharingPeriodComponent } from './sharing-period.component';

describe('SharingPeriodComponent', () => {
  let component: SharingPeriodComponent;
  let fixture: ComponentFixture<SharingPeriodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [SharingPeriodComponent]
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
