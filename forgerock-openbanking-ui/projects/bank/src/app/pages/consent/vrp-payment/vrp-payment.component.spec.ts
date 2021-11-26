import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { VrpPaymentComponent } from './vrp-payment.component';

describe('VrpPaymentComponent', () => {
  let component: VrpPaymentComponent;
  let fixture: ComponentFixture<VrpPaymentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ VrpPaymentComponent ]
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
});
