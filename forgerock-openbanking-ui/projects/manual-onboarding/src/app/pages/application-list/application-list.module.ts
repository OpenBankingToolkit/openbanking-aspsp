import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FlexLayoutModule } from '@angular/flex-layout';
import { TranslateModule } from '@ngx-translate/core';
import { TranslateSharedModule } from 'bank/src/app/translate-shared.module';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatDialogModule } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatMenuModule } from '@angular/material/menu';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatTableModule } from '@angular/material/table';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatTooltipModule } from '@angular/material/tooltip';
import { ClipboardModule } from 'ngx-clipboard';

import { ApplicationListRoutingModule } from './application-list-routing.module';
import { ApplicationListComponent } from './application-list.component';
import { ApplicationListDetailsComponent } from './details/details.component';
import { TableComponent } from './table/table.component';
// containers
import { TableContainerComponent } from './table/table.container';
import { ApplicationListContainerComponent } from './application-list.container';
import { ApplicationListDetailsContainerComponent } from './details/details.container';
import { ApplicationListFormComponent } from './application-form/application-form.component';
import { ApplicationListFormContainerComponent } from './application-form/application-form.container';
import { ForgerockCustomerIconModule } from 'ob-ui-libs/components/forgerock-customer-icon';
import { ForgerockConfirmDialogModule } from 'ob-ui-libs/components/forgerock-confirm-dialog';

@NgModule({
  imports: [
    CommonModule,
    ApplicationListRoutingModule,
    MatButtonModule,
    MatIconModule,
    MatTableModule,
    MatSidenavModule,
    MatProgressBarModule,
    MatListModule,
    MatToolbarModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatTooltipModule,
    MatMenuModule,
    FlexLayoutModule,
    TranslateModule,
    TranslateSharedModule,
    FormsModule,
    ReactiveFormsModule,
    ForgerockCustomerIconModule,
    ForgerockConfirmDialogModule,
    ClipboardModule
  ],
  declarations: [
    ApplicationListComponent,
    ApplicationListContainerComponent,
    ApplicationListDetailsComponent,
    ApplicationListDetailsContainerComponent,
    TableComponent,
    TableContainerComponent,
    ApplicationListFormComponent,
    ApplicationListFormContainerComponent
  ],
  entryComponents: [ApplicationListFormComponent, ApplicationListFormContainerComponent]
})
export class ApplicationListModule {}
