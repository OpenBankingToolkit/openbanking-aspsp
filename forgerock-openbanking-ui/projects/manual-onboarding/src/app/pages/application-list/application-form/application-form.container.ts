import { Component } from '@angular/core';
import { select, Store } from '@ngrx/store';
import { Observable } from 'rxjs';

import { RegisterApplicationRequestAction, selectors } from 'manual-onboarding/src/store/reducers/applications';
import { IApiPostRegisterBody, IState } from 'manual-onboarding/src/models';
import { selectOIDCDirectoryId } from 'ob-ui-libs/oidc';

@Component({
  selector: 'app-application-list-application-form-container',
  template: `
    <app-application-list-application-form
      [directoryId]="directoryId$ | async"
      [isLoading]="isLoading$ | async"
      [isSuccess]="isSuccess$ | async"
      (submit)="onRegisterApplication($event)"
    ></app-application-list-application-form>
  `
})
export class ApplicationListFormContainerComponent {
  isLoading$: Observable<boolean> = this.store.pipe(select(selectors.selectIsRegisterAppLoading));
  isSuccess$: Observable<boolean> = this.store.pipe(select(selectors.selectIsRegisterAppSuccess));
  directoryId$: Observable<string> = this.store.pipe(select(selectOIDCDirectoryId));

  constructor(private store: Store<IState>) {}

  onRegisterApplication(form: IApiPostRegisterBody) {
    this.store.dispatch(new RegisterApplicationRequestAction({ form }));
  }
}
