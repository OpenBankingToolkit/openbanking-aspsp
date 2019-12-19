import { Component, Inject } from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Platform } from '@angular/cdk/platform';
import { TranslateService } from '@ngx-translate/core';

import { ForgerockConfigService } from '@forgerock/openbanking-ngx-common/services/forgerock-config';
import { ForgerockSplashscreenService } from '@forgerock/openbanking-ngx-common/services/forgerock-splashscreen';
import { ForgerockGDPRService } from '@forgerock/openbanking-ngx-common/gdpr';
import {
  ForgerockNativeSplashscreenService,
  ForgerockNativeDeepLinkService
} from '@forgerock/openbanking-ngx-common/native';

@Component({
  selector: 'app-root',
  template: `
    <router-outlet></router-outlet>
  `
})
export class AppNativeComponent {
  enableCustomization: string = this.configService.get('enableCustomization');

  constructor(
    @Inject(DOCUMENT) private document: any,
    private splashscreenService: ForgerockSplashscreenService,
    private translateService: TranslateService,
    private platform: Platform,
    private configService: ForgerockConfigService,
    private gdprService: ForgerockGDPRService,
    private nativeSplashscreen: ForgerockNativeSplashscreenService,
    private nativeDeeplink: ForgerockNativeDeepLinkService
  ) {
    this.nativeDeeplink.init();
    this.splashscreenService.init();
    this.gdprService.init();
    this.nativeSplashscreen.hide();

    this.translateService.addLangs(['en', 'fr']);
    this.translateService.setDefaultLang('en');
    this.translateService.use(this.translateService.getBrowserLang() || 'en');

    // Add is-mobile class to the body if the platform is mobile
    if (this.platform.ANDROID || this.platform.IOS) {
      this.document.body.classList.add('is-mobile');
    }
  }
}
