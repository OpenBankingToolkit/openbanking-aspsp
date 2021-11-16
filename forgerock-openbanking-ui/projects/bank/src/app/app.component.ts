import {ChangeDetectorRef, Component, Inject, OnInit} from '@angular/core';
import { DOCUMENT } from '@angular/common';
import { Platform } from '@angular/cdk/platform';
import { TranslateService } from '@ngx-translate/core';

import { ForgerockSplashscreenService } from '@forgerock/openbanking-ngx-common/services/forgerock-splashscreen';
import { ForgerockGDPRService } from '@forgerock/openbanking-ngx-common/gdpr';
import {ActivatedRoute} from "@angular/router";
import {ForgerockMessagesService} from "@forgerock/openbanking-ngx-common/services/forgerock-messages";

@Component({
  selector: 'app-root',
  template: `
    <router-outlet></router-outlet>
  `
})
export class AppComponent implements OnInit{
  loading: boolean;
  error: Error;
  ngOnInit(): void {
    console.log("APP")
    this.route.fragment.subscribe((fragment: string) => {
      if(fragment && fragment.includes("error_description")){
        const urlSearchParams = new URLSearchParams(fragment)
        const error = {
          message: urlSearchParams.get("error_description"),
          error: urlSearchParams.get("error")
        };
        console.log(`segments: ${fragment}`);
        this.messages.error(error.message, null, {duration: 0});
        this.error = new Error(error.error);
        this.loading = false;
        this.cdr.detectChanges();
      }
    })
    this.route.queryParams.subscribe(
      params => {
        console.log((params))
      }
    );

  }
  constructor(
    @Inject(DOCUMENT) private document: any,
    private splashscreenService: ForgerockSplashscreenService,
    private translateService: TranslateService,
    private platform: Platform,
    private route: ActivatedRoute,
    private messages: ForgerockMessagesService,
    private cdr: ChangeDetectorRef,
    private gdprService: ForgerockGDPRService
  ) {
    this.splashscreenService.init();
    this.gdprService.init();

    this.translateService.addLangs(['en', 'fr']);
    this.translateService.setDefaultLang('en');
    this.translateService.use(this.translateService.getBrowserLang() || 'en');

    // Add is-mobile class to the body if the platform is mobile
    if (this.platform.ANDROID || this.platform.IOS) {
      this.document.body.classList.add('is-mobile');
    }
  }
}
