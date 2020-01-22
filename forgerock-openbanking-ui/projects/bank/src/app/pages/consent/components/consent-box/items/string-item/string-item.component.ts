import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-string-item',
  template: `
    <app-key-value-item
      *ngIf="payload.value"
      [key]="payload.label | translate"
      [value]="payload.value"
      [cssClass]="payload.cssClass"
    ></app-key-value-item>
  `
})
export class StringItemComponent implements OnInit {
  @Input() payload: {
    label?: string;
    cssClass?: string;
    value?: string;
  } = {};

  constructor() {}

  ngOnInit() {}
}
