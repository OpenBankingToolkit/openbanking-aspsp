import {
  AfterViewChecked,
  ChangeDetectorRef,
  Component,
  ElementRef,
  EventEmitter,
  Input,
  OnInit,
  Output,
  ViewChild
} from '@angular/core';
import {FormGroup} from "@angular/forms";
import {ApiResponses} from 'bank/src/app/types/api';
import {ForgerockMessagesService} from '@forgerock/openbanking-ngx-common/services/forgerock-messages';

@Component({
  selector: 'app-reject',
  templateUrl: './reject.component.html',
  styleUrls: ['./reject.component.scss']
})
export class RejectComponent implements OnInit, AfterViewChecked {
  error: Error;
  form: FormGroup = new FormGroup({});
  @ViewChild('rejectFormPost') rejectFormPost: ElementRef;
  @Input() response: ApiResponses.ConsentDetailsResponse;
  _loading = false;
  @Input() set loading(isLoading: boolean) {
    this.form[isLoading ? 'disable' : 'enable']();
    this._loading = isLoading;
  }

  constructor(
    private cdr: ChangeDetectorRef,
    private messages: ForgerockMessagesService
  ) {
  }

  @Output() formSubmit = new EventEmitter<String>()

  ngOnInit() {
    console.log("reject component")
    console.table(`reject: ${this.response.decisionResponse}`);
    if (!this.response.decisionResponse) {
      return;
    }
  }

  ngAfterViewChecked(): void {
    console.log("ngAfterViewChecked");
    if (this.response.decisionResponse) {
      console.log("submit reject form")
      this.rejectFormPost.nativeElement.submit();
    }
  }

  displayError(er: string) {
    this.messages.error(er);
    this.error = new Error(er);
    this.loading = false;
    this.cdr.detectChanges();
  }

}
