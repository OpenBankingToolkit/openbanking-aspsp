import { Component } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import {
  RegisterApplicationRequestAction,
  selectors,
  UnselectApplicationAction
} from 'manual-onboarding/src/store/reducers/applications';
import { IApiPostRegisterBody, IState } from 'manual-onboarding/src/models';
import {
  ForgerockOIDCLogoutRequestAction,
  selectOIDCConnected,
  selectOIDCUserId
} from '@forgerock/openbanking-ngx-common/oidc';

@Component({
  selector: 'app-application-list-container',
  template: `
    <app-application-list
      [selectedApplicationId]="selectedApplicationId$ | async"
      [connected]="connected$ | async"
      [username]="username$ | async"
      (close)="onClose()"
      (logout)="onLogout()"
      (closeApplicationForm)="onRegisterApplication($event)"
    ></app-application-list>
  `
})
export class ApplicationListContainerComponent {
  selectedApplicationId$: Observable<string> = this.store.pipe(select(selectors.selectSelectedApplicationId));
  connected$: Observable<boolean> = this.store.pipe(select(selectOIDCConnected));
  username$: Observable<string> = this.store.pipe(select(selectOIDCUserId));

  constructor(private store: Store<IState>) {}

  onClose() {
    this.store.dispatch(new UnselectApplicationAction());
  }

  onLogout() {
    this.store.dispatch(new ForgerockOIDCLogoutRequestAction());
  }

  onRegisterApplication(form: IApiPostRegisterBody) {
    this.store.dispatch(new RegisterApplicationRequestAction({ form }));
  }
}
