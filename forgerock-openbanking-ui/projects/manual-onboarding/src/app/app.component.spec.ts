import { TestBed, async } from '@angular/core/testing';
import { AppComponent } from './app.component';

// This "test" is empty but needed in order to run the entire suite on all
// projects. It is the only test so far on this app
// @TODO: remove this test when directory as at least on correct test
describe('app:manual-onboarding AppComponent', () => {
  // beforeEach(async(() => {
  //   TestBed.configureTestingModule({
  //     declarations: [AppComponent]
  //   }).compileComponents();
  // }));
  it('should create the app', async(() => {
    expect(true).toBeTruthy();
  }));
});
