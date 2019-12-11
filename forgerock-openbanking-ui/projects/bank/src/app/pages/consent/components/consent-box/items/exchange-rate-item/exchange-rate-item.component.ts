import { Component, OnInit } from '@angular/core';
import { Rate } from 'bank/src/app/types/api';

@Component({
  selector: 'app-exchange-rate-item',
  templateUrl: './exchange-rate-item.component.html',
  styleUrls: ['./exchange-rate-item.component.scss']
})
export class ExchangeRateItemComponent implements OnInit {
  label: string;
  rate: Rate;
  currencyOfTransfer: string;
  cssClass: string;

  payload: any;

  constructor() {}

  ngOnInit() {}
}
