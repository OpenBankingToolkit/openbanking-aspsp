import { NgModule } from '@angular/core';
import { FlexLayoutModule } from '@angular/flex-layout';

import { ForgerockPipesModule } from '@forgerock/openbanking-ngx-common/pipes';
import { ForgerockMessagesModule } from '@forgerock/openbanking-ngx-common/services/forgerock-messages';
import { ForgerockConfigModule } from '@forgerock/openbanking-ngx-common/services/forgerock-config';

@NgModule({
  imports: [ForgerockPipesModule, FlexLayoutModule, ForgerockMessagesModule, ForgerockConfigModule],
  exports: [ForgerockPipesModule, FlexLayoutModule, ForgerockMessagesModule, ForgerockConfigModule]
})
export class ForgerockSharedModule {}
