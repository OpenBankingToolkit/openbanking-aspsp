import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsentStepsComponent } from './consent-steps.component';

describe('ConsentStepsComponent', () => {
  let component: ConsentStepsComponent;
  let fixture: ComponentFixture<ConsentStepsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConsentStepsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsentStepsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
