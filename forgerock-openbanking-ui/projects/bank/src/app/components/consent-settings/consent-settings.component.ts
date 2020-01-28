import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';

@Component({
  selector: 'app-consent-settings',
  template: `
    <p>
      consent-settings works!
    </p>
  `,
  styles: [],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ConsentSettingsComponent implements OnInit {
  constructor() {}

  ngOnInit() {}
}
