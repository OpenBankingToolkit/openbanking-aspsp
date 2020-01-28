import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-consent-sharing-period',
  template: `
    <p>
      consent-sharing-period works!
    </p>
  `,
  styles: [],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ConsentSharingPeriodComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
