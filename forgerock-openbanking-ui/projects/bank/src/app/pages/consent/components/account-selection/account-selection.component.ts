import { Component, Input, OnInit } from '@angular/core';
import { FRAccountWithBalance } from 'bank/src/app/types/api';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-account-selection',
  templateUrl: './account-selection.component.html',
  styleUrls: ['./account-selection.component.scss']
})
export class AccountSelectionComponent implements OnInit {
  @Input() label: string;
  @Input() accounts: FRAccountWithBalance[];
  @Input() form: FormGroup;

  constructor() {}

  ngOnInit() {
    this.form.addControl('selectedAccount', new FormControl('', Validators.required));
  }
}
