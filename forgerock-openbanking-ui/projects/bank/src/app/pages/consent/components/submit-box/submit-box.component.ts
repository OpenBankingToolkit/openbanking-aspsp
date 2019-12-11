import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup } from '@angular/forms';

@Component({
  selector: 'app-submit-box',
  templateUrl: './submit-box.component.html',
  styleUrls: ['./submit-box.component.scss']
})
export class SubmitBoxComponent implements OnInit {
  @Input() label: string;
  @Input() loading: boolean;
  @Input() cancelLabel: string;
  @Input() proceedLabel: string;
  @Input() form: FormGroup;

  @Output() accept = new EventEmitter<any>();
  @Output() deny = new EventEmitter<any>();

  constructor() {}

  ngOnInit() {}
}
