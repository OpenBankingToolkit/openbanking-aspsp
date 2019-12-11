import { NgModule, InjectionToken, APP_INITIALIZER } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { StoreModule, ActionReducerMap } from '@ngrx/store';

import { AppComponent } from 'bank/src/app/app.component';
import { environment } from 'bank/src/environments/environment';
import rootReducer from 'bank/src/store';

import { AppRoutingModule } from './app-routing.module';
import { ForgerockConfigService } from 'ob-ui-libs/services/forgerock-config';
import { ForgerockConfigModule } from 'ob-ui-libs/services/forgerock-config';
import { ForgerockAuthenticationModule } from 'ob-ui-libs/authentication';
import { ForgerockSharedModule } from 'ob-ui-libs/shared';

export const REDUCER_TOKEN = new InjectionToken<ActionReducerMap<{}>>('Registered Reducers');

export function getReducers() {
  return rootReducer;
}

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function init_app(appConfig: ForgerockConfigService) {
  return () => appConfig.fetchAndMerge(environment);
}

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    HttpClientModule,
    AppRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    }),
    ForgerockSharedModule,
    ForgerockAuthenticationModule,
    // Store
    StoreModule.forRoot(REDUCER_TOKEN),
    environment.devModules || [],
    // Theme
    // FuseSharedModule,
    ForgerockConfigModule.forRoot()
  ],
  providers: [
    {
      provide: REDUCER_TOKEN,
      deps: [],
      useFactory: getReducers
    },
    {
      provide: APP_INITIALIZER,
      useFactory: init_app,
      deps: [ForgerockConfigService],
      multi: true
    }
  ],
  bootstrap: [AppComponent]
})
export class AppModule {}
