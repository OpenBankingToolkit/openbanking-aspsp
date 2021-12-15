import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

import {ApiResponses} from 'bank/src/app/types/api';
import {IConsentEventEmitter, Item, ItemType} from 'bank/src/app/types/consentItem';
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";
import {TranslateService} from "@ngx-translate/core";
import _get from 'lodash-es/get';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-vrp-payment',
  templateUrl: './vrp-payment.component.html',
  styleUrls: ['./vrp-payment.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class VrpPaymentComponent implements OnInit {

  constructor(private translate: TranslateService) {
  }

  form: FormGroup = new FormGroup({});

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
    if (_get(this.response, 'debtorAccount')) {
      if (_get(this.response, 'debtorAccount.Name')) {
        this.payerItems.push({
          type: ItemType.STRING,
          payload: {
            label: 'CONSENT.VRP-PAYMENT.NAME',
            value: this.response.debtorAccount.Name,
            cssClass: 'vrp-payment-debtorAccount-Name'
          }
        });
      }
      this.payerItems.push({
        type: ItemType.VRP_ACCOUNT_NUMBER,
        payload: {
          sortCodeLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_SORT_CODE',
          accountNumberLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_NUMBER',
          account: this.response.debtorAccount,
          cssClass: 'vrp-payment-payer-account'
        }
      });
    }
    // PAYEE ITEMS
    if (_get(this.response, 'aspspName')) {
      this.payeeItems.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.VRP-PAYMENT.ASPSP_NAME',
          value: this.response.aspspName,
          cssClass: 'vrp-payment-aspspName'
        }
      });
    }
    this.payeeItems.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.VRP-PAYMENT.PAYEE_NAME',
        value: this.response.pispName,
        cssClass: 'vrp-payment-psipName'
      }
    });
    if (_get(this.response, 'creditorAccount')) {
      this.payeeItems.push({
        type: ItemType.VRP_ACCOUNT_NUMBER,
        payload: {
          sortCodeLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_SORT_CODE',
          accountNumberLabel: 'CONSENT.VRP-PAYMENT.ACCOUNT_NUMBER',
          account: this.response.creditorAccount,
          cssClass: 'vrp-payment-payee-account'
        }
      });
    }
    // Control parameter items
    if (_get(this.response, 'paymentReference')) {
      this.controlItems.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.VRP-PAYMENT.REFERENCE',
          value: this.response.paymentReference,
          cssClass: 'vrp-payment-payee-identification'
        }
      });
      this.controlItems.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.VRP-PAYMENT.DEBTOR_REF',
          value: this.response.debtorReference,
          cssClass: 'vrp-payment-payee-identification'
        }
      });
    }
    if (_get(this.response, 'controlParameters.periodicLimits[0]')) {
      const periodicLimitsInstructedAmount = {
        Amount: this.response.controlParameters.periodicLimits[0].amount,
        Currency: this.response.controlParameters.periodicLimits[0].currency
      }
      this.controlItems.push({
        type: ItemType.INSTRUCTED_AMOUNT,
        payload: {
          label: this.translate.instant('CONSENT.VRP-PAYMENT.MAX_AMOUNT_PERIOD_TYPE', {
            periodType: this.response.controlParameters.periodicLimits[0].periodType
          }),
          amount: periodicLimitsInstructedAmount,
          cssClass: 'vrp-payment-maxAmountPeriod'
        }
      });
    }
    if (_get(this.response, 'controlParameters.maximumIndividualAmount')) {
      this.controlItems.push({
        type: ItemType.INSTRUCTED_AMOUNT,
        payload: {
          label: 'CONSENT.VRP-PAYMENT.MAX_AMOUNT_PER_PAYMENT',
          amount: this.response.controlParameters.maximumIndividualAmount,
          cssClass: 'vrp-payment-maxAmountPerPayment'
        }
      });
    }
    if (_get(this.response, 'controlParameters.validToDateTime')) {
      this.controlItems.push({
        type: ItemType.DATE,
        payload: {
          label: 'CONSENT.VRP-PAYMENT.EXPIRE',
          date: this.response.controlParameters.validToDateTime,
          cssClass: 'vrp-payment-validToDateTime'
        }
      });
    }
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? ConsentDecision.ALLOW : ConsentDecision.DENY
    });
  }
}
