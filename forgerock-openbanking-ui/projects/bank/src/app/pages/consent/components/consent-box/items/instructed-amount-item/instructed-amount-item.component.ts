import { Component, OnInit } from '@angular/core';
import { OBActiveOrHistoricCurrencyAndAmount } from 'bank/src/app/types/ob';

@Component({
  selector: 'app-instructed-amount-item',
  templateUrl: './instructed-amount-item.component.html',
  styleUrls: ['./instructed-amount-item.component.scss']
})
export class InstructedAmountItemComponent implements OnInit {
  label: string;
  amount: OBActiveOrHistoricCurrencyAndAmount;

  cssClass: string;

  payload: any;

  constructor() {}

  ngOnInit() {}
}
