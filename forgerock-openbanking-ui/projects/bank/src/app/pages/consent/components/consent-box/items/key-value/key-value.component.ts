import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-key-value-item',
  template: `
    <div *ngIf="value" class="string-consent-box-item {{ cssClass }}">
      <span class="key title" [translate]="key"></span>
      <span fxFlex></span>
      <span class="value">{{ value }}</span>
    </div>
  `,
  styleUrls: ['./key-value.component.scss']
})
export class KeyValueItemComponent implements OnInit {
  @Input() key: string;
  @Input() value: string;
  @Input() cssClass: string;

  constructor() {}

  ngOnInit() {}
}
