import { Component, OnInit } from '@angular/core';
import { Rate } from 'bank/src/app/types/api';

@Component({
  selector: 'app-exchange-rate-item',
  templateUrl: './exchange-rate-item.component.html'
})
export class ExchangeRateItemComponent implements OnInit {
  payload: {
    label: string;
    rate: Rate;
    value: string;
    currencyOfTransfer: string;
    cssClass: string;
  };

  constructor() {}

  ngOnInit() {}
}
