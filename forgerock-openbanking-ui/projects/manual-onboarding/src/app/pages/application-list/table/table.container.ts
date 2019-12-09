import { Component } from '@angular/core';
import { Store, select } from '@ngrx/store';
import { Observable } from 'rxjs';

import {
  selectors,
  LoadApplicationsRequestAction,
  SelectApplicationAction,
  UnselectApplicationAction,
  UnregisterApplicationRequestAction
} from 'manual-onboarding/src/store/reducers/applications';
import { IApplication, IState } from 'manual-onboarding/src/models';

@Component({
  selector: 'app-application-list-table-container',
  template: `
    <mat-progress-bar
      mode="indeterminate"
      [style.visibility]="(isLoading$ | async) ? 'visible' : 'hidden'"
    ></mat-progress-bar>
    <ng-container *ngIf="applications$ | async as applications">
      <app-application-list-table
        [applications]="applications"
        [selectedApplicationId]="selectedApplicationId$ | async"
        (select)="onSelect($event)"
        (unregister)="onUnregister($event)"
      ></app-application-list-table>
    </ng-container>
  `
})
export class TableContainerComponent {
  selectedApplicationId$: Observable<string> = this.store.pipe(select(selectors.selectSelectedApplicationId));
  isLoading$: Observable<boolean> = this.store.pipe(select(selectors.selectIsApplicationListLoading));
  applications$: Observable<IApplication[]> = this.store.pipe(select(selectors.selectAll));

  constructor(private store: Store<IState>) {
    this.store.dispatch(new LoadApplicationsRequestAction());
  }

  onSelect(details: { row: IApplication; isSelected: boolean }) {
    if (!details.row) {
      return;
    }
    if (!details.isSelected) {
      this.store.dispatch(new SelectApplicationAction({ applicationId: details.row.id }));
    } else {
      this.store.dispatch(new UnselectApplicationAction());
    }
  }

  onUnregister(application: IApplication) {
    this.store.dispatch(new UnregisterApplicationRequestAction({ application }));
  }
}
