import {ChangeDetectionStrategy, Component, OnInit} from '@angular/core';
import {Address} from "bank/src/app/types/CustomerInfo";
import {Item, ItemType} from "bank/src/app/types/consentItem";
import _get from 'lodash-es/get';
import {TranslateService} from "@ngx-translate/core";

@Component({
  selector: 'app-adress-item',
  templateUrl: './address-item.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class AddressItemComponent implements OnInit {
  label: string
  cssClass: string;
  address: Address;
  payload: any;
  items: Item[] = [];

  constructor(private translate: TranslateService) {
  }

  ngOnInit() {
    this.address = this.payload.address;
    this.label = this.translate.instant(this.payload.label);
    if (_get(this.address, 'streetAddress')) {
      for (const value of this.address.streetAddress) {
        this.items.push({
          type: ItemType.STRING,
          payload: {
            value: value,
            cssClass: `address-streetAddress-${value}`
          }
        });
      }
    }
    if (_get(this.address, 'postalCode')) {
      this.items.push({
        type: ItemType.STRING,
        payload: {
          value: this.address.postalCode,
          cssClass: 'address-postalCode'
        }
      });
    }
  }
}
