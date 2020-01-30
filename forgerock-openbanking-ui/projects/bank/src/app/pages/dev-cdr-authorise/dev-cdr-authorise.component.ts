import { Component, OnInit, ChangeDetectionStrategy, ChangeDetectorRef } from '@angular/core';

import mock1 from './mocks/accountSharing';
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
  mocks: any[] = [mock1];

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
