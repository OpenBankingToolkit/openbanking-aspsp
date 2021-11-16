import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import _get from 'lodash-es/get';

import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";

@Component({
  selector: 'app-consent-domestic-standing-order-payment',
  templateUrl: './domestic-standing-order.component.html',
  styleUrls: ['./domestic-standing-order.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class DomesticStandingOrderComponent implements OnInit {
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
        cssClass: 'domestic-standing-order-merchantName'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.ACCOUNT',
        value: this.response.account,
        cssClass: 'domestic-standing-order-account'
      }
    });
    this.items.push({
      type: ItemType.STRING,
      payload: {
        label: 'CONSENT.PAYMENT.PAYMENT_REFERENCE',
        value: this.response.paymentReference,
        cssClass: 'domestic-standing-order-paymentReference'
      }
    });
    if (
      _get(this.response, 'standingOrder.FirstPaymentDateTime') &&
      _get(this.response, 'standingOrder.FirstPaymentAmount')
    ) {
      this.items.push({
        type: ItemType.FIRST_PAYMENT,
        payload: {
          firstPaymentLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.FIRST_PAYMENT',
          firstPaymentDateLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.FIRST_PAYMENT_DATE',
          firstPaymentAmountLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.FIRST_PAYMENT_AMOUNT',
          firstPaymentDate: this.response.standingOrder.FirstPaymentDateTime,
          firstPaymentAmount: this.response.standingOrder.FirstPaymentAmount,
          cssClass: 'domestic-standing-order-FirstPayment'
        }
      });
    }
    if (_get(this.response, 'standingOrder.NextPaymentAmount') && _get(this.response, 'standingOrder.Frequency')) {
      this.items.push({
        type: ItemType.NEXT_PAYMENT,
        payload: {
          nextPaymentLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.RECURRING_PAYMENT',
          nextPaymentAmountLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.RECURRING_PAYMENT_AMOUNT',
          frequencyLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.FREQ',
          nextPaymentAmount: this.response.standingOrder.NextPaymentAmount,
          frequency: this.response.standingOrder.Frequency,
          cssClass: 'domestic-standing-order-NextPayment'
        }
      });
    }
    if (
      _get(this.response, 'standingOrder.FinalPaymentDateTime') &&
      _get(this.response, 'standingOrder.FinalPaymentAmount')
    ) {
      this.items.push({
        type: ItemType.FINAL_PAYMENT,
        payload: {
          finalPaymentLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.FINAL_PAYMENT',
          finalPaymentDateLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.FINAL_PAYMENT_AMOUNT',
          finalPaymentAmountLabel: 'CONSENT.DOMESTIC-STANDING-ORDER.FINAL_PAYMENT_DATE',
          finalPaymentDate: this.response.standingOrder.FinalPaymentDateTime,
          finalPaymentAmount: this.response.standingOrder.FinalPaymentAmount,
          cssClass: 'domestic-standing-order-FinalPayment'
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
