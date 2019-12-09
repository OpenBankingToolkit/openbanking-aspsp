import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import _get from 'lodash-es/get';

import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';

@Component({
  selector: 'app-consent-international-schedule-payment',
  templateUrl: './international-schedule-payment.component.html',
  styleUrls: ['./international-schedule-payment.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InternationalSchedulePaymentComponent implements OnInit {
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
  basicItems: Item[] = [];
  rateItems: Item[] = [];

  ngOnInit() {
    if (!this.response) {
      return;
    }

    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYEE_NAME',
        value: this.response.merchantName,
        cssClass: 'international-schedule-payment-merchantName'
      }
    });
    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.ACCOUNT',
        value: this.response.account,
        cssClass: 'international-schedule-payment-account'
      }
    });
    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYMENT_REFERENCE',
        value: this.response.paymentReference,
        cssClass: 'international-schedule-payment-paymentReference'
      }
    });
    this.basicItems.push({
      type: ItemType.INSTRUCTED_AMOUNT,
      payload: {
        label: 'CONSENT.PAYMENT.AMOUNT',
        amount: this.response.instructedAmount,
        cssClass: 'international-schedule-payment-instructedAmount'
      }
    });
    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.CURRENCY_FROM',
        value: this.response.rate.UnitCurrency,
        cssClass: 'international-schedule-payment-rate'
      }
    });
    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.CURRENCY_TO',
        value: this.response.currencyOfTransfer,
        cssClass: 'international-schedule-payment-currency-of-transfer'
      }
    });
    this.basicItems.push({
      type: ItemType.EXCHANGE_RATE,
      payload: {
        label: 'CONSENT.INTERNATIONAL-PAYMENT.EXCHANGE_RATE',
        rate: this.response.rate,
        currencyOfTransfer: this.response.currencyOfTransfer,
        cssClass: 'international-schedule-payment-rate'
      }
    });
    if (_get(this.response, 'scheduledPayment.ScheduledPaymentDateTime')) {
      this.basicItems.push({
        type: ItemType.DATE,
        payload: {
          label: 'CONSENT.INTERNATIONAL-SCHEDULE-PAYMENT.PAYMENT_DATE',
          date: this.response.scheduledPayment.ScheduledPaymentDateTime,
          cssClass: 'international-schedule-payment-rate'
        }
      });
    }

    if (
      _get(this.response, 'scheduledPayment.InstructedAmount') &&
      this.response.rate &&
      this.response.currencyOfTransfer
    ) {
      this.rateItems.push({
        type: ItemType.RATE_AMOUNT,
        payload: {
          label: 'CONSENT.PAYMENT.AMOUNT',
          amount: this.response.scheduledPayment.InstructedAmount,
          rate: this.response.rate,
          currencyOfTransfer: this.response.currencyOfTransfer,
          cssClass: 'international-schedule-payment-rate'
        }
      });
    }
    if (_get(this.response, 'rate.UnitCurrency')) {
      this.rateItems.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.PAYMENT.CHARGES',
          value: '0.0 ' + this.response.rate.UnitCurrency,
          cssClass: 'international-schedule-payment-unit-currency'
        }
      });
    }
    if (_get(this.response, 'scheduledPayment.InstructedAmount')) {
      this.rateItems.push({
        type: ItemType.INSTRUCTED_AMOUNT,
        payload: {
          label: 'CONSENT.INTERNATIONAL-PAYMENT.AMOUNT_TO_PAY',
          amount: this.response.scheduledPayment.InstructedAmount,
          cssClass: 'international-schedule-payment-amount-to-pay'
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
