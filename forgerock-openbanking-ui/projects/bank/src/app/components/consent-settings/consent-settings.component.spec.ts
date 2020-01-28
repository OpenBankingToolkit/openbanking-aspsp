import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { ConsentSettingsComponent } from './consent-settings.component';

describe('ConsentSettingsComponent', () => {
  let component: ConsentSettingsComponent;
  let fixture: ComponentFixture<ConsentSettingsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ConsentSettingsComponent]
    }).compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(ConsentSettingsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
