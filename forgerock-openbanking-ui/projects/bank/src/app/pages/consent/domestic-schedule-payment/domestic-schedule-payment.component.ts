import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import _get from 'lodash-es/get';

import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';

@Component({
  selector: 'app-consent-domestic-schedule-payment',
  templateUrl: './domestic-schedule-payment.component.html',
  styleUrls: ['./domestic-schedule-payment.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DomesticSchedulePaymentComponent implements OnInit {
  constructor() {}

  form: FormGroup = new FormGroup({
    selectedAccount: new FormControl('', Validators.required)
  });

  @Input() response: ApiResponses.ConsentDetailsResponse;
  _loading = false;
  @Input() set loading(isLoading: boolean) {
    this.form[isLoading ? 'disable' : 'enable']();
    this._loading = isLoading;
  }
  @Output() formSubmit = new EventEmitter<IConsentEventEmitter>();
  items: Item[] = [];

  ngOnInit() {
    if (!this.response) {
      return;
    }

    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYEE_NAME',
        value: this.response.merchantName,
        cssClass: 'domestic-schedule-payment-merchantName'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.ACCOUNT',
        value: this.response.account,
        cssClass: 'domestic-schedule-payment-account'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYMENT_REFERENCE',
        value: this.response.paymentReference,
        cssClass: 'domestic-schedule-payment-paymentReference'
      }
    });
    if (_get(this.response, 'scheduledPayment.InstructedAmount')) {
      this.items.push({
        type: ItemType.INSTRUCTED_AMOUNT,
        payload: {
          label: 'CONSENT.PAYMENT.AMOUNT',
          amount: this.response.scheduledPayment.InstructedAmount,
          cssClass: 'domestic-schedule-payment-instructedAmount'
        }
      });
    }
    if (_get(this.response, 'scheduledPayment.ScheduledPaymentDateTime')) {
      this.items.push({
        type: ItemType.DATE,
        payload: {
          label: 'CONSENT.DOMESTIC-SCHEDULE-PAYMENT.PAYMENT_DATE',
          date: this.response.scheduledPayment.ScheduledPaymentDateTime,
          cssClass: 'domestic-schedule-payment-ScheduledPaymentDateTime'
        }
      });
    }
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? 'allow' : 'deny',
      accountId: this.form.value.selectedAccount
    });
  }
}
