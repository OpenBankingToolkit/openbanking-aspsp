import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";

function validateCheckboxRequired(c: FormControl) {
  return Object.values(c.value).filter(v => v === true).length
    ? null
    : {
        validateCheckboxRequired: {
          valid: false
        }
      };
}

@Component({
  selector: 'app-consent-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AccountComponent implements OnInit {
  constructor() {}

  form: FormGroup = new FormGroup({}, validateCheckboxRequired);

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
        label: 'CONSENT.ACCOUNT.APPLICATION',
        value: this.response.pispName,
        cssClass: 'account-merchantName'
      }
    });
    this.items.push({
      type: ItemType.TRANSACTION_PERIOD,
      payload: {
        fromTransaction: this.response.fromTransaction,
        toTransaction: this.response.toTransaction,
        cssClass: 'account-transaction-period'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.ACCOUNT',
        value: this.response.account,
        cssClass: 'account-account'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYMENT_REFERENCE',
        value: this.response.paymentReference,
        cssClass: 'account-paymentReference'
      }
    });
    this.items.push({
      type: ItemType.INSTRUCTED_AMOUNT,
      payload: {
        label: 'CONSENT.PAYMENT.AMOUNT',
        amount: this.response.instructedAmount,
        cssClass: 'account-instructedAmount'
      }
    });
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? ConsentDecision.ALLOW : ConsentDecision.DENY,
      sharedAccounts: Object.keys(this.form.value).filter(k => this.form.value[k])
    });
  }
}
