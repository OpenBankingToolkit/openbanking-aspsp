import { Component, OnInit } from '@angular/core';
import { OBCashAccount3 } from 'bank/src/app/types/ob';

@Component({
  selector: 'app-account-number-item',
  templateUrl: './account-number-item.component.html',
  styleUrls: ['./account-number-item.component.scss']
})
export class AccountNumberItemComponent implements OnInit {
  account: OBCashAccount3;
  cssClass: string;
  label: string;

  payload: any;

  constructor() {}

  ngOnInit() {}
}
