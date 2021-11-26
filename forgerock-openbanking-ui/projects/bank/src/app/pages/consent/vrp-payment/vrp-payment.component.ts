import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormControl, FormGroup, Validators} from '@angular/forms';

import {ApiResponses} from 'bank/src/app/types/api';
import {IConsentEventEmitter, Item, ItemType} from 'bank/src/app/types/consentItem';
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";
import {TranslateService} from "@ngx-translate/core";
import _get from 'lodash-es/get';

@Component({
  selector: 'app-vrp-payment',
  templateUrl: './vrp-payment.component.html',
  styleUrls: ['./vrp-payment.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VrpPaymentComponent implements OnInit {

  constructor(private translate: TranslateService) {
  }

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
  payerItems: Item[] = [];
  payeeItems: Item[] = [];
  controlItems: Item[] = [];

  ngOnInit() {
    console.log("vrp payment component")
    if (!this.response) {
      return;
    }
    // PAYER ITEMS
    this.payerItems.push({
      type: ItemType.VRP_ACCOUNT_NUMBER,
      payload: {
        sortCodeLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_SORT_CODE',
        accountNumberLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_NUMBER',
        account: this.response.debtorAccount.account.Account[0],
        cssClass: 'vrp-payment-payer-account'
      }
    });
    // PAYEE ITEMS
    this.payeeItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.VRP-PAYMENT.ASPSP_NAME',
        value: this.response.aspspName,
        cssClass: 'vrp-payment-aspspName'
      }
    });
    this.payeeItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.VRP-PAYMENT.PAYEE_NAME',
        value: this.response.aispName,
        cssClass: 'vrp-payment-asipName'
      }
    });
    this.payeeItems.push({
      type: ItemType.VRP_ACCOUNT_NUMBER,
      payload: {
        sortCodeLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_SORT_CODE',
        accountNumberLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_NUMBER',
        account: this.response.CreditorAccount.account.Account[0],
        cssClass: 'vrp-payment-payee-account'
      }
    });
    // Control parameter items
    this.controlItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.VRP-PAYMENT.REFERENCE',
        value: this.response.RemittanceInformation.Reference,
        cssClass: 'vrp-payment-payee-identification'
      }
    });
    this.controlItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.VRP-PAYMENT.DEBTOR_REF',
        value: this.response.RemittanceInformation.Reference,
        cssClass: 'vrp-payment-payee-identification'
      }
    });
    if (_get(this.response, 'ControlParameters.PeriodicLimits[0]')) {
      let periodicLimitsInstructedAmount = {
        Amount: this.response.ControlParameters.PeriodicLimits[0].Amount,
        Currency: this.response.ControlParameters.PeriodicLimits[0].Currency
      }
      this.controlItems.push({
        type: ItemType.INSTRUCTED_AMOUNT,
        payload: {
          label: this.translate.instant('CONSENT.VRP-PAYMENT.MAX_AMOUNT_PERIOD_TYPE', {
            periodType: this.response.ControlParameters.PeriodicLimits[0].PeriodType
          }),
          amount: periodicLimitsInstructedAmount,
          cssClass: 'vrp-payment-maxAmountPeriod'
        }
      });
    }
    if (_get(this.response, 'ControlParameters.MaximumIndividualAmount')) {
      this.controlItems.push({
        type: ItemType.INSTRUCTED_AMOUNT,
        payload: {
          label: 'CONSENT.VRP-PAYMENT.MAX_AMOUNT_PER_PAYMENT',
          amount: this.response.ControlParameters.MaximumIndividualAmount,
          cssClass: 'vrp-payment-maxAmountPerPayment'
        }
      });
    }
    if (_get(this.response, 'ControlParameters.ValidToDateTime')) {
      this.controlItems.push({
        type: ItemType.DATE,
        payload: {
          label: 'CONSENT.VRP-PAYMENT.EXPIRE',
          date: this.response.ControlParameters.ValidToDateTime,
          cssClass: 'vrp-payment-validToDateTime'
        }
      });
    }
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? ConsentDecision.ALLOW : ConsentDecision.DENY,
      accountId: this.form.value.selectedAccount
    });
  }

}
