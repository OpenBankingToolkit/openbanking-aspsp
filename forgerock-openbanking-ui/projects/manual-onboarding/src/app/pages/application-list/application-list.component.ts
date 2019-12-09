import { Component, Input, Output, OnInit, ChangeDetectionStrategy, EventEmitter } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';

import { IApiPostRegisterBody } from 'manual-onboarding/src/models';
// import { ApplicationListFormComponent } from './application-form/application-form.component';
import { ApplicationListFormContainerComponent } from './application-form/application-form.container';

@Component({
  selector: 'app-application-list',
  templateUrl: './application-list.component.html',
  styleUrls: ['./application-list.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class ApplicationListComponent implements OnInit {
  @Input() selectedApplicationId: string;
  @Input() connected: boolean;
  @Input() username: string;
  @Output() close = new EventEmitter<void>();
  @Output() logout = new EventEmitter<void>();
  @Output() registerApplication = new EventEmitter<IApiPostRegisterBody>();

  constructor(public dialog: MatDialog) {}

  ngOnInit() {}

  onClose() {
    this.close.emit();
  }

  onLogout() {
    this.logout.emit();
  }

  addApplication(): void {
    this.dialog.open(ApplicationListFormContainerComponent, {
      disableClose: true
    });
  }
}
