import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';
import _get from 'lodash-es/get';

import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';

@Component({
  selector: 'app-consent-funds-confirmation',
  templateUrl: './funds-confirmation.component.html',
  styleUrls: ['./funds-confirmation.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class FundsConfirmationComponent implements OnInit {
  constructor() {}

  form: FormGroup = new FormGroup({});

  items: Item[] = [];
  @Input() response: ApiResponses.ConsentDetailsResponse;
  _loading = false;
  @Input() set loading(isLoading: boolean) {
    this.form[isLoading ? 'disable' : 'enable']();
    this._loading = isLoading;
  }
  @Output() formSubmit = new EventEmitter<IConsentEventEmitter>();

  ngOnInit() {
    if (!this.response) {
      return;
    }

    if (this.response.merchantName) {
      this.items.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.FUNDS-CONFIRMATION.APPLICATION',
          value: this.response.merchantName,
          cssClass: 'cof-merchantName'
        }
      });
    }

    if (_get(this.response, 'accounts[0].account.Nickname')) {
      this.items.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.FUNDS-CONFIRMATION.ACCOUNT_TITLE',
          value: this.response.accounts[0].account.Nickname,
          cssClass: 'cof-account-title'
        }
      });
    }

    if (_get(this.response, 'accounts[0].account.Account[0]')) {
      this.items.push({
        type: ItemType.ACCOUNT_NUMBER,
        payload: {
          label: 'CONSENT.FUNDS-CONFIRMATION.ACCOUNT_ID',
          account: this.response.accounts[0].account.Account[0],
          cssClass: 'cof-account-id'
        }
      });
    }

    if (this.response.expirationDateTime) {
      this.items.push({
        type: ItemType.DATE,
        payload: {
          label: 'CONSENT.FUNDS-CONFIRMATION.EXPIRATION',
          date: this.response.expirationDateTime,
          cssClass: 'cof-expiration'
        }
      });
    }
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? 'allow' : 'deny',
      accountId: _get(this.response, 'accounts[0].id', '')
    });
  }
}
