import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ConsentDevRoutingModule } from './consent-dev-routing.module';
import { ConsentModule } from '../consent/consent.module';
import { ConsentDevComponent } from './consent-dev.component';

@NgModule({
  declarations: [ConsentDevComponent],
  imports: [CommonModule, ConsentDevRoutingModule, ConsentModule]
})
export class ConsentDevModule {}
