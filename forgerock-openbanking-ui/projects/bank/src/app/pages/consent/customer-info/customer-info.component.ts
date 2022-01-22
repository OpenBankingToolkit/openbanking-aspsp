import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';

import {ApiResponses} from 'bank/src/app/types/api';
import {IConsentEventEmitter, Item, ItemType} from 'bank/src/app/types/consentItem';
import {ConsentDecision} from "bank/src/app/types/ConsentDecision";
import _get from 'lodash-es/get';
import {FormControl, FormGroup} from "@angular/forms";
import {CustomerInfo} from "bank/src/app/types/CustomerInfo";
import {MatCheckboxChange} from "@angular/material/checkbox";

@Component({
  selector: 'app-customer-info',
  templateUrl: './customer-info.component.html',
  styleUrls: ['./customer-info.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CustomerInfoComponent implements OnInit {

  constructor() {
  }

  form: FormGroup = new FormGroup({});

  @Input() response: ApiResponses.ConsentDetailsResponse;
  _loading = false;
  @Input() set loading(isLoading: boolean) {
    this.form[isLoading ? 'disable' : 'enable']();
    this._loading = isLoading;
  }

  @Output() formSubmit = new EventEmitter<IConsentEventEmitter>();
  items: Item[] = [];
  private customerInfo: CustomerInfo;


  ngOnInit() {
    console.log("customer info component")
    if (!this.response || !this.response.customerInfo) {
      return;
    }
    this.form.addControl("confirm-details", new FormControl(true));
    this.customerInfo = this.response.customerInfo;

    if (_get(this.customerInfo, 'givenName')) {
      this.items.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.CUSTOMER-INFO.NAME',
          value: this.customerInfo.givenName,
          cssClass: 'customerInfo-given_name'
        }
      });
    }
    if (_get(this.customerInfo, 'address')) {
      this.items.push({
        type: ItemType.ADDRESS,
        payload: {
          label: 'CONSENT.CUSTOMER-INFO.ADDRESS',
          address: this.customerInfo.address,
          cssClass: 'customerInfo-address'
        }
      });
    }
    if (_get(this.customerInfo, 'email')) {
      this.items.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.CUSTOMER-INFO.EMAIL',
          value: this.customerInfo.email,
          cssClass: 'customerInfo-email'
        }
      });
    }
    if (_get(this.customerInfo, 'phoneNumber')) {
      this.items.push({
        type: ItemType.STRING,
        payload: {
          label: 'CONSENT.CUSTOMER-INFO.PHONE',
          value: this.customerInfo.phoneNumber,
          cssClass: 'customerInfo-phone_number'
        }
      });
    }
    if (_get(this.customerInfo, 'birthdate')) {
      this.items.push({
        type: ItemType.DATE,
        payload: {
          label: 'CONSENT.CUSTOMER-INFO.BIRTHDATE',
          date: this.customerInfo.birthdate,
          cssClass: 'customerInfo-birthdate'
        }
      });
    }

    this.form["disable"]();
  }

  onChange(confirmDetails: MatCheckboxChange) {
    console.log(confirmDetails.checked + " => " + confirmDetails.source.id);
    this.form[confirmDetails.checked ? 'enable' : 'disable']();
  }

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? ConsentDecision.ALLOW : ConsentDecision.DENY
    });
  }

}
