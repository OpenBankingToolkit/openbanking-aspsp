import {
  Component,
  Input,
  Output,
  OnInit,
  OnChanges,
  SimpleChanges,
  ChangeDetectionStrategy,
  EventEmitter
} from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { TranslateService } from '@ngx-translate/core';

import { IApplication } from 'manual-onboarding/src/models';
import { ForgerockConfirmDialogComponent } from '@forgerock/openbanking-ngx-common/components/forgerock-confirm-dialog';

@Component({
  selector: 'app-application-list-table',
  templateUrl: './table.component.html',
  styleUrls: ['./table.component.scss'],
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class TableComponent implements OnInit, OnChanges {
  dataSource = new MatTableDataSource();
  @Input() applications: IApplication[];
  @Input() selectedApplicationId: string;
  @Output() select = new EventEmitter<{
    row: IApplication;
    isSelected: boolean;
  }>();
  @Output() unregister = new EventEmitter<IApplication>();

  displayedColumns: string[] = ['applicationName', 'client_id', 'application_type', 'redirectUris', 'removeApp'];

  constructor(public dialog: MatDialog, private translate: TranslateService) {
    this.dataSource.filterPredicate = (data: any, filter: string): boolean => {
      const { id, userId, softwareClientId, manualRegistrationRequest, oidcRegistrationResponse } = data;
      const flattenApplication = {
        ...manualRegistrationRequest,
        ...oidcRegistrationResponse,
        id,
        userId,
        softwareClientId
      };
      // Transform the data into a lowercase string of all property values.
      const dataStr = Object.keys(flattenApplication)
        .reduce((currentTerm: string, key: string) => {
          // Use an obscure Unicode character to delimit the words in the concatenated string.
          // This avoids matches where the values of two columns combined will match the user's query
          // (e.g. `Flute` and `Stop` will match `Test`). The character is intended to be something
          // that has a very low chance of being typed in by somebody in a text field. This one in
          // particular is "White up-pointing triangle with dot" from
          // https://en.wikipedia.org/wiki/List_of_Unicode_characters
          return currentTerm + (flattenApplication as { [key: string]: any })[key] + '◬';
        }, '')
        .toLowerCase();
      return dataStr.indexOf(filter) !== -1;
    };
  }

  ngOnInit() {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes.applications) {
      this.dataSource.data = changes.applications.currentValue;
    }
  }

  filterChange(filterValue: string) {
    this.dataSource.filter = filterValue.trim().toLowerCase();
  }

  onSelect(isSelected, row: IApplication) {
    this.select.emit({
      isSelected,
      row
    });
  }

  onRemove(e: Event, row: IApplication) {
    e.stopPropagation();
    const dialogRef = this.dialog.open(ForgerockConfirmDialogComponent, {
      data: {
        text: this.translate.instant('REMOVE_CONFIRM', {
          app: row.manualRegistrationRequest.applicationName
        })
      }
    });
    dialogRef.afterClosed().subscribe(result => {
      if (result) {
        this.unregister.emit(row);
      }
    });
  }
}
