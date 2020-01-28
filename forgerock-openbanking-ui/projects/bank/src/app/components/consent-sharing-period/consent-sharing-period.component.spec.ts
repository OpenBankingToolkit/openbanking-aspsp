import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsentSharingPeriodComponent } from './consent-sharing-period.component';

describe('ConsentSharingPeriodComponent', () => {
  let component: ConsentSharingPeriodComponent;
  let fixture: ComponentFixture<ConsentSharingPeriodComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConsentSharingPeriodComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsentSharingPeriodComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
