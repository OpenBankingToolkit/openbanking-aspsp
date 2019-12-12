import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { Store, select } from '@ngrx/store';

import { IState } from 'manual-onboarding/src/models';
import { ForgerockOIDCLogoutRequestAction } from 'ob-ui-libs/oidc';
import { selectOIDCConnected } from 'ob-ui-libs/oidc';

@Component({
  // tslint:disable-next-line
  selector: 'register-toolbar-menu-container',
  template: `
    <register-toolbar-menu [connected]="connected$ | async" (logout)="logout($event)"></register-toolbar-menu>
  `,
  styles: [
    `
      :host {
        display: flex;
        flex-direction: row;
        align-items: center;
      }
    `
  ]
})
export class RegisterToolbarMenuContainer implements OnInit {
  connected$: Observable<boolean> = this.store.pipe(select(selectOIDCConnected));

  constructor(private store: Store<IState>) {}

  ngOnInit(): void {}

  logout(e: Event) {
    this.store.dispatch(new ForgerockOIDCLogoutRequestAction());
  }
}
