import { Component } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';

import { selectors, UnselectApplicationAction } from 'manual-onboarding/src/store/reducers/applications';
import { IApplication, IState } from 'manual-onboarding/src/models';

@Component({
  selector: 'app-application-list-details-container',
  template: `
    <app-application-list-details [selected]="application$ | async" (close)="onClose()"></app-application-list-details>
  `
})
export class ApplicationListDetailsContainerComponent {
  application$: Observable<IApplication> = this.store.pipe(select(selectors.selectSelectedApplication));

  constructor(private store: Store<IState>) {}

  onClose() {
    this.store.dispatch(new UnselectApplicationAction());
  }
}
