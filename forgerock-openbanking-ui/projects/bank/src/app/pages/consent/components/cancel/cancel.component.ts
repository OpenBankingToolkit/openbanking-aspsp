import {ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {IConsentEventEmitter, Item, ItemType} from "bank/src/app/types/consentItem";
import {ApiResponses} from "bank/src/app/types/api";
import {Location} from '@angular/common';

@Component({
  selector: 'app-cancel',
  templateUrl: './cancel.component.html',
  styleUrls: ['./cancel.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CancelComponent implements OnInit {
  constructor() {
  }

  items: Item[] = [];
  @Input() response: ApiResponses.ConsentDetailsResponse;
  _loading = false;
  @Input() set loading(isLoading: boolean) {
    this._loading = isLoading;
  }

  ngOnInit() {
    console.log("cancel component")
    console.table(this.response)
    if (!this.response) {
      return;
    }
    this.items.push({
        type: ItemType.STRING,
        payload: {
          label: 'COMPONENT.CANCEL.APPLICATION',
          value: this.response.aispName,
          cssClass: 'cof-merchantName'
        }
      }
    )
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
  }
}
