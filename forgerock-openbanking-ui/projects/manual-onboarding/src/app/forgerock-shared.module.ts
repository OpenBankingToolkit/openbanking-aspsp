import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';

import { ForgerockPipesModule } from 'ob-ui-libs/pipes';
import { ForgerockMessagesModule } from 'ob-ui-libs/services/forgerock-messages';
import { ForgerockConfigModule } from 'ob-ui-libs/services/forgerock-config';

@NgModule({
  imports: [ForgerockPipesModule, FlexLayoutModule, ForgerockMessagesModule, ForgerockConfigModule],
  exports: [ForgerockPipesModule, FlexLayoutModule, ForgerockMessagesModule, ForgerockConfigModule]
})
export class ForgerockSharedModule {}
