import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";

@Component({
  selector: 'app-consent-single-payment',
  templateUrl: './single-payment.component.html',
  styleUrls: ['./single-payment.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SinglePaymentComponent implements OnInit {
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
        cssClass: 'merchantName'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.ACCOUNT',
        value: this.response.account,
        cssClass: 'single-payment-account'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYMENT_REFERENCE',
        value: this.response.paymentReference,
        cssClass: 'single-payment-paymentReference'
      }
    });
    this.items.push({
      type: ItemType.INSTRUCTED_AMOUNT,
      payload: {
        label: 'CONSENT.PAYMENT.AMOUNT',
        amount: this.response.instructedAmount,
        cssClass: 'single-payment-instructedAmount'
      }
    });
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? ConsentDecision.ALLOW : ConsentDecision.DENY,
      accountId: this.form.value.selectedAccount
    });
  }
}
