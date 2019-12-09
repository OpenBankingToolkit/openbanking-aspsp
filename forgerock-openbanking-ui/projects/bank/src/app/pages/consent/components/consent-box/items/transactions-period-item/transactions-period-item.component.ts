import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-transactions-period-item',
  templateUrl: './transactions-period-item.component.html',
  styleUrls: ['./transactions-period-item.component.scss']
})
export class TransactionsPeriodItemComponent implements OnInit {
  cssClass: string;

  payload: any;

  constructor() {}

  ngOnInit() {}
}
