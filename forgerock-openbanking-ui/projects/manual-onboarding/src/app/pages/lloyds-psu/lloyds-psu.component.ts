import { Component, OnInit, ChangeDetectionStrategy } from '@angular/core';
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

import { ForgerockConfigService } from 'ob-ui-libs/services/forgerock-config';

@Component({
  selector: 'app-lloyds-psu',
  templateUrl: './lloyds-psu.component.html',
  styleUrls: ['./lloyds-psu.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class LloydsPsuComponent implements OnInit {
  content: SafeHtml;
  constructor(private sanitizer: DomSanitizer, protected configService: ForgerockConfigService) {}

  ngOnInit() {
    this.content = this.sanitizer.bypassSecurityTrustHtml(this.configService.get('lloyds.psuPage.content', ''));
  }
}
