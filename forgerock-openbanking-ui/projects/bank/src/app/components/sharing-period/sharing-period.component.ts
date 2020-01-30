import { Component, OnInit, ChangeDetectionStrategy, Input } from '@angular/core';

@Component({
  selector: 'app-sharing-period',
  template: `
    <h2>{{ 'CDR.SHARING_PERIOD.TITLE' | translate }}</h2>
    <div *ngIf="from && to">
      <p>
        {{ from | forgerockDateFormat: 'Do MMM, YYYY' }} -
        {{ to | forgerockDateFormat: 'Do MMM, YYYY' }}
      </p>
      <h3>{{ 'CDR.SHARING_PERIOD.HOW_OFTEN' | translate }}</h3>
      <p>
        {{ 'CDR.SHARING_PERIOD.HOW_OFTEN_ANSWER' | translate }}
      </p>
    </div>
  `,
  styles: [],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SharingPeriodComponent implements OnInit {
  @Input() from: string;
  @Input() to: string;

  constructor() {}

  ngOnInit() {}
}
