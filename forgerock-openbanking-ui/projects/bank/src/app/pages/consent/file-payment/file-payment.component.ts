import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";

@Component({
  selector: 'app-consent-file-payment',
  templateUrl: './file-payment.component.html',
  styleUrls: ['./file-payment.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FilePaymentComponent implements OnInit {
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
        cssClass: 'file-payment-merchantName'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.ACCOUNT',
        value: this.response.account,
        cssClass: 'file-payment-account'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYMENT_REFERENCE',
        value: this.response.paymentReference,
        cssClass: 'file-payment-paymentReference'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.FILE-PAYMENT.NO_OF_PAYEES',
        value: this.response.numberOfTransactions,
        cssClass: 'file-payment-numberOfTransactions'
      }
    });
    this.items.push({
      type: ItemType.INSTRUCTED_AMOUNT,
      payload: {
        label: 'CONSENT.FILE-PAYMENT.TOTAL_AMOUNT',
        amount: this.response.totalAmount,
        cssClass: 'file-payment-totalAmount'
      }
    });
    this.items.push({
      type: ItemType.DATE,
      payload: {
        label: 'CONSENT.FILE-PAYMENT.PAYMENT_DATE',
        date: this.response.requestedExecutionDateTime,
        cssClass: 'file-payment-requestedExecutionDateTime'
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
