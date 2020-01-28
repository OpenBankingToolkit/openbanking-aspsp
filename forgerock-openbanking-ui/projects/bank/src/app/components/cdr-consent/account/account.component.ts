import { ChangeDetectionStrategy, Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ApiResponses } from 'bank/src/app/types/api';
import { Item, ItemType, IConsentEventEmitter } from 'bank/src/app/types/consentItem';

function validateCheckboxRequired(c: FormControl) {
  return Object.values(c.value).filter(v => v === true).length
    ? null
    : {
        validateCheckboxRequired: {
          valid: false
        }
      };
}

@Component({
  selector: 'app-consent-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class CDRAccountComponent implements OnInit {
  constructor() {}
  step: number;

  form: FormGroup = new FormGroup({}, validateCheckboxRequired);

  @Input() response: ApiResponses.ConsentDetailsResponse;
  _loading = false;
  @Input() set loading(isLoading: boolean) {
    this.form[isLoading ? 'disable' : 'enable']();
    this._loading = isLoading;
  }
  @Output() formSubmit = new EventEmitter<IConsentEventEmitter>();

  ngOnInit() {}

  submit(allowing = false) {
    this.formSubmit.emit({
      decision: allowing ? 'allow' : 'deny',
      sharedAccounts: Object.keys(this.form.value).filter(k => this.form.value[k])
    });
  }
}
