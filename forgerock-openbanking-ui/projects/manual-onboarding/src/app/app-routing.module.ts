import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ForgerockSimpleLayoutModule } from '@forgerock/openbanking-ngx-common/layouts/simple';
import { SimpleLayoutComponent } from '@forgerock/openbanking-ngx-common/layouts/simple';
import { ForgerockGDPRService } from '@forgerock/openbanking-ngx-common/gdpr';
import { ForegerockGDPRConsentGuard } from '@forgerock/openbanking-ngx-common/gdpr';
import { ForgerockMainLayoutComponent } from '@forgerock/openbanking-ngx-common/layouts/main-layout';
import { ForgerockMainLayoutModule } from '@forgerock/openbanking-ngx-common/layouts/main-layout';
import {
  IForgerockMainLayoutConfig,
  IForgerockMainLayoutNavigations
} from '@forgerock/openbanking-ngx-common/layouts/main-layout';
import { RegisterToolbarMenuContainer } from './components/register-toolbar-menu/register-toolbar-menu.container';
import { RegisterToolbarMenuComponentModule } from './components/register-toolbar-menu/register-toolbar-menu.module';
import { ForgerockAuthRedirectOIDCComponent, IsOIDCConnectedGuard } from '@forgerock/openbanking-ngx-common/oidc';

const routes: Routes = [
  {
    path: '',
    component: ForgerockMainLayoutComponent,
    canActivate: [ForegerockGDPRConsentGuard],
    children: [
      {
        path: '',
        pathMatch: 'full',
        loadChildren: 'manual-onboarding/src/app/pages/application-list/application-list.module#ApplicationListModule',
        canActivate: [IsOIDCConnectedGuard]
      },
      // THIS ROUTE IS FOR LLOYDS ONLY AND WILL BE REMOVED WHEN THEY HAVE THEIR OWN APPS
      // @todo: remove
      {
        path: 'psu-credentials',
        pathMatch: 'full',
        canActivate: [IsOIDCConnectedGuard],
        loadChildren: 'manual-onboarding/src/app/pages/lloyds-psu/lloyds-psu.module#LloydsPsuModule'
      }
    ]
  },
  {
    path: '',
    component: SimpleLayoutComponent,
    canActivate: [ForegerockGDPRConsentGuard],
    children: [
      {
        path: 'session-lost',
        pathMatch: 'full',
        loadChildren: 'manual-onboarding/src/app/pages/session-lost/session-lost.module#SessionLostModule'
      },
      {
        path: '404',
        pathMatch: 'full',
        loadChildren: () =>
          import('forgerock/src/ob-ui-libs-lazy/not-found.module').then(m => m.OBUILibsLazyNotFoundPage)
      },
      {
        path: 'dev/info',
        pathMatch: 'full',
        loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/dev-info.module').then(m => m.OBUILibsLazyDevInfoPage)
      }
    ]
  },
  {
    path: ForgerockGDPRService.gdprDeniedPage,
    component: SimpleLayoutComponent,
    loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/gdpr.module').then(m => m.OBUILibsLazyGDPRPage)
  },
  {
    path: 'redirectOpenId',
    pathMatch: 'full',
    component: ForgerockAuthRedirectOIDCComponent
  },
  {
    path: '**',
    pathMatch: 'full',
    redirectTo: '404'
  }
];

const mainLayoutConfig: IForgerockMainLayoutConfig = {
  style: 'vertical-layout-1',
  navbar: {
    folded: false,
    hidden: false,
    position: 'left'
  },
  toolbar: {
    hidden: false
  },
  footer: {
    hidden: true,
    position: 'below-static'
  }
};

export const navigations: IForgerockMainLayoutNavigations = {
  main: [
    {
      id: 'home',
      translate: 'NAV.ONBOARDING',
      type: 'item',
      icon: 'dashboard',
      exactMatch: true,
      url: '/'
    }
  ]
};

@NgModule({
  imports: [
    ForgerockSimpleLayoutModule,
    RegisterToolbarMenuComponentModule,
    ForgerockMainLayoutModule.forRoot({
      layout: mainLayoutConfig,
      navigations,
      components: {
        toolbar: RegisterToolbarMenuContainer
      }
    }),
    RouterModule.forRoot(routes)
  ],
  exports: [RouterModule]
})
export class AppRoutingModule {}
