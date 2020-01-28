import { Component, Input, OnInit } from '@angular/core';
import { FRAccountWithBalance } from 'bank/src/app/types/api';

@Component({
  selector: 'app-cdr-data-recipient-info',
  template: `
    <div class="consent-box">
      <div fxLayout="row" fxLayoutAlign="center stretch">
        <div fxFlex="60" fxLayout="column" fxLayout.gt-sm="row" fxLayoutAlign="center center">
          <img class="logo" [src]="img" />
          <div>
            <b>{{ name }}</b>
            <br />
            <small [translate]="'CDR.RECIPIENT_ID'" [translateParams]="{ id: id }"></small>
          </div>
        </div>
        <div fxFlex="40" fxLayout="row" fxLayoutAlign="center center">
          <span><mat-icon class="check">check_circle_outline</mat-icon></span>
          <small class="secondary-text">{{ 'CDR.ACCREDITED_RECIPIENT' | translate }}</small>
        </div>
      </div>

      <hr />
      {{ requestDate }}
    </div>
  `,
  styles: [
    `
      img.logo {
        max-width: 70px;
        margin-right: 0.5em;
      }
      mat-icon.check {
        color: green;
        font-size: 3em;
        width: 45px;
        margin-right: 0.16em;
        height: 100%;
      }
    `
  ]
})
export class CDRDataRecipientInfoComponent implements OnInit {
  @Input() name: string;
  @Input() img: string;
  @Input() id: string;
  @Input() requestDate: string;

  constructor() {}

  ngOnInit() {}
}
