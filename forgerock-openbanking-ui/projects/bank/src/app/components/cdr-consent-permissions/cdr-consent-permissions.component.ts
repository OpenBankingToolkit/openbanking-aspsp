import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';

import { CDRAccountPermissions } from '../../types/CDR';

@Component({
  selector: 'app-cdr-consent-permissions',
  template: `
    <h2>{{ 'CDR.ACCOUNT.PERMISSIONS.TITLE' | translate }}</h2>
    <div>
      <div
        *ngFor="let permission of permissions"
        [innerHTML]="'CDR.ACCOUNT.PERMISSIONS.' + permission | translate"
      ></div>
    </div>
  `,
  styles: [
    `
      :host {
        display: block;
      }
    `
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CdrConsentPermissionsComponent implements OnInit {
  @Input() permissions: CDRAccountPermissions;

  constructor() {}

  ngOnInit() {
    console.log('permissions', this.permissions);
  }
}
