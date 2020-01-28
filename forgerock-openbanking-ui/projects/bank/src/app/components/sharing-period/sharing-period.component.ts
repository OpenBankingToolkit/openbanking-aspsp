import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-sharing-period',
  template: `
    <p>
      sharing-period works!
    </p>
  `,
  styles: [],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class SharingPeriodComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
