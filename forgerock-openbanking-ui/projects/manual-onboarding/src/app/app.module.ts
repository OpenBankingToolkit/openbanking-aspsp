import { BrowserModule } from '@angular/platform-browser';
import { NgModule, InjectionToken, APP_INITIALIZER } from '@angular/core';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { HttpClientModule, HttpClient } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { StoreModule, ActionReducerMap } from '@ngrx/store';
import { EffectsModule } from '@ngrx/effects';
import { CookieModule } from 'ngx-cookie';

import { environment } from 'manual-onboarding/src/environments/environment';
import { AppComponent } from './app.component';
import { ApiService } from 'manual-onboarding/src/app/services/api.service';
import { IState } from 'manual-onboarding/src/models';
import rootReducer from 'manual-onboarding/src/store';
import { RootEffects } from 'manual-onboarding/src/store/effects';
import { ForgerockConfigService } from 'ob-ui-libs/services/forgerock-config';
import { ForgerockSharedModule } from 'ob-ui-libs/shared';
import { AppRoutingModule } from './app-routing.module';
import { ForgerockOIDCModule } from 'ob-ui-libs/oidc';

export function createTranslateLoader(http: HttpClient) {
  return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

export function init_app(appConfig: ForgerockConfigService) {
  return () => appConfig.fetchAndMerge(environment);
}

export const REDUCER_TOKEN = new InjectionToken<ActionReducerMap<IState>>('Registered Reducers');

export function getReducers() {
  return rootReducer;
}

export function createForgerockOIDCConfigFactory(config: ForgerockConfigService) {
  return {
    backendURL: config.get('registerBackend')
  };
}

@NgModule({
  declarations: [AppComponent],
  imports: [
    BrowserModule,
    HttpClientModule,
    AppRoutingModule,
    TranslateModule.forRoot({
      loader: {
        provide: TranslateLoader,
        useFactory: createTranslateLoader,
        deps: [HttpClient]
      }
    }),
    CookieModule.forRoot(),
    ForgerockSharedModule,
    BrowserAnimationsModule,
    ForgerockOIDCModule.forRoot(createForgerockOIDCConfigFactory),
    // Store
    StoreModule.forRoot(REDUCER_TOKEN),
    EffectsModule.forRoot(RootEffects),
    environment.devModules || []
  ],
  providers: [
    ApiService,
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
