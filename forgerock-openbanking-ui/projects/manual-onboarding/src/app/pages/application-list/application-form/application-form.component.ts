import {
  ChangeDetectionStrategy,
  Component,
  EventEmitter,
  Inject,
  Input,
  OnChanges,
  OnInit,
  Output,
  SimpleChanges
} from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { validateMultipleUrls } from '@utils/forms';

export interface DialogData {
  animal: string;
  name: string;
}

@Component({
  selector: 'app-application-list-application-form',
  templateUrl: './application-form.component.html',
  styleUrls: ['./application-form.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ApplicationListFormComponent implements OnInit, OnChanges {
  @Input() isLoading = false;
  @Input() isSuccess = false;
  @Output() submit = new EventEmitter<void>();
  @Input() directoryId: string;

  formGroup: FormGroup;

  constructor(
    public dialogRef: MatDialogRef<ApplicationListFormComponent>,
    @Inject(MAT_DIALOG_DATA) public data: DialogData
  ) {}

  ngOnInit() {
    this.formGroup = new FormGroup({
      applicationName: new FormControl('', [Validators.minLength(4), Validators.required]),
      applicationDescription: new FormControl(''),
      redirectUris: new FormControl('', [Validators.required, validateMultipleUrls]),
      softwareStatementAssertion: new FormControl(''),
      qsealPem: new FormControl('')
    });
  }

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.isSuccess && changes.isSuccess.currentValue && changes.isLoading.previousValue) {
      // https://github.com/angular/angular/issues/17572
      // not sure if that the best but for now it works
      setTimeout(() => this.dialogRef.close());
    }
  }

  onSubmit() {
    const value = {
      ...this.formGroup.value,
      qsealPem: this.formGroup.value.qsealPem.trim(),
      softwareStatementAssertion: this.formGroup.value.softwareStatementAssertion.trim(),
      redirectUris: this.formGroup.value.redirectUris.split(',').map(url => url.trim())
    };

    this.submit.emit(value);
  }
}
