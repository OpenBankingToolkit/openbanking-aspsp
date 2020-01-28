import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';

import mock1 from './mocks/accountSharing';
import mock2 from './mocks/domestic-payments';
import mock3 from './mocks/domestic-schedule-payment';
import mock4 from './mocks/Domestic-standing-orders';
import mock5 from './mocks/filePayment';
import mock6 from './mocks/FundsConfirmation';
import mock7 from './mocks/InternationalPayment';
import mock8 from './mocks/InternationalSchedulePayment';
import mock9 from './mocks/InternationalStandingOrderPayment';
import mock10 from './mocks/single-payments';
import { IConsentEventEmitter } from '../../types/consentItem';

@Component({
  selector: 'app-dev-cdr-authorise',
  template: `
    <div *ngFor="let mock of mocks">
      <h1>{{ mock.type }}</h1>
      <app-cdr-consent-dynamic
        [response]="mock"
        [loading]="loading"
        (formSubmit)="onFormSubmit($event)"
      ></app-cdr-consent-dynamic>
    </div>
  `,
  styles: [
    `
      :host {
        display: block;
        max-width: 500px;
        margin: auto;
      }
    `
  ],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DevCDRAuthoriseComponent implements OnInit {
  loading = false;
  mocks: any[] = [mock1, mock2, mock3, mock4, mock5, mock6, mock7, mock8, mock9, mock10];

  constructor(private cdr: ChangeDetectorRef) {}

  ngOnInit() {}

  onFormSubmit(values: IConsentEventEmitter) {
    this.loading = true;
    setTimeout(() => {
      this.loading = false;
      this.cdr.detectChanges();
    }, 3000);
  }
}
