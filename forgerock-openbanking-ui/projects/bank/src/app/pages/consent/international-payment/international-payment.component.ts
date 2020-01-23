import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, Validators, FormControl } from '@angular/forms';
import _get from 'lodash-es/get';

import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';

@Component({
  selector: 'app-consent-international-payment',
  templateUrl: './international-payment.component.html',
  styleUrls: ['./international-payment.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class InternationalPaymentComponent implements OnInit {
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
        cssClass: 'international-payment-merchantName'
      }
    });
    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.ACCOUNT',
        value: this.response.account,
        cssClass: 'international-payment-account'
      }
    });
    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYMENT_REFERENCE',
        value: this.response.paymentReference,
        cssClass: 'international-payment-paymentReference'
      }
    });
    this.basicItems.push({
      type: ItemType.INSTRUCTED_AMOUNT,
      payload: {
        label: 'CONSENT.PAYMENT.AMOUNT',
        amount: this.response.instructedAmount,
        cssClass: 'international-payment-instructedAmount'
      }
    });

    if (_get(this.response, 'rate.UnitCurrency')) {
      this.basicItems.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.PAYMENT.CURRENCY_FROM',
          value: this.response.rate.UnitCurrency,
          cssClass: 'international-payment-rate'
        }
      });
    }

    this.basicItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.CURRENCY_TO',
        value: this.response.currencyOfTransfer,
        cssClass: 'international-payment-currency-of-transfer'
      }
    });

    this.basicItems.push({
      type: ItemType.EXCHANGE_RATE,
      payload: {
        label: 'CONSENT.INTERNATIONAL-PAYMENT.EXCHANGE_RATE',
        value:
          this.response.rate && this.response.currencyOfTransfer
            ? `1.0 ${this.response.rate.UnitCurrency} = ${this.response.rate.ExchangeRate} ${
                this.response.currencyOfTransfer
              }`
            : '',
        rate: this.response.rate,
        currencyOfTransfer: this.response.currencyOfTransfer,
        cssClass: 'international-payment-rate'
      }
    });

    this.rateItems.push({
      type: ItemType.RATE_AMOUNT,
      payload: {
        label: 'CONSENT.PAYMENT.AMOUNT',
        amount: this.response.instructedAmount,
        rate: this.response.rate,
        currencyOfTransfer: this.response.currencyOfTransfer,
        cssClass: 'international-payment-rate'
      }
    });

    if (_get(this.response, 'rate.UnitCurrency')) {
      this.rateItems.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.PAYMENT.CHARGES',
          value: '0.0 ' + this.response.rate.UnitCurrency,
          cssClass: 'international-payment-unit-currency'
        }
      });
    }

    this.rateItems.push({
      type: ItemType.INSTRUCTED_AMOUNT,
      payload: {
        label: 'CONSENT.INTERNATIONAL-PAYMENT.AMOUNT_TO_PAY',
        amount: this.response.instructedAmount,
        cssClass: 'international-payment-amount-to-pay'
      }
    });
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? 'allow' : 'deny',
      accountId: this.form.value.selectedAccount
    });
  }
}
