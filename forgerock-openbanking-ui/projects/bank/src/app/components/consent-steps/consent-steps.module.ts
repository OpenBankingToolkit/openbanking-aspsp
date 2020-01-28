import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ConsentStepsComponent } from './consent-steps.component';

@NgModule({
  declarations: [ConsentStepsComponent],
  exports: [ConsentStepsComponent],
  imports: [CommonModule]
})
export class ConsentStepsModule {}
