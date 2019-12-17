import { Component, Input, Output, OnInit, EventEmitter } from '@angular/core';
import { ClipboardService } from 'ngx-clipboard';

import { IApplication } from 'manual-onboarding/src/models';
import { ForgerockMessagesService } from '@forgerock/openbanking-ngx-common/services/forgerock-messages';

@Component({
  selector: 'app-application-list-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.scss']
})
export class ApplicationListDetailsComponent implements OnInit {
  @Input() selected: IApplication;
  @Output() close = new EventEmitter<void>();

  constructor(private _clipboardService: ClipboardService, private messages: ForgerockMessagesService) {}

  ngOnInit() {}

  onClose() {
    this.close.emit();
  }

  copy() {
    try {
      this._clipboardService.copyFromContent(JSON.stringify(this.selected));
      this.messages.success('Application details copied to clipboard');
    } catch (error) {
      this.messages.error('Error during copy, please try again');
    }
  }
}
