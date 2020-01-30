import { Component, Input, OnInit } from '@angular/core';
import { FRAccountWithBalance } from 'bank/src/app/types/api';

@Component({
  selector: 'app-account-selected',
  templateUrl: './account-selected.component.html',
  styleUrls: ['./account-selected.component.scss']
})
export class AccountSelectedComponent implements OnInit {
  @Input() accounts: FRAccountWithBalance[];
  @Input() selected: {
    [key: string]: boolean;
  };
  selectedAccounts: FRAccountWithBalance[];

  constructor() {}

  ngOnInit() {
    this.selectedAccounts = this.accounts.filter(account => this.selected[account.id]);
  }
}
