import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { ForgerockSimpleLayoutModule } from 'ob-ui-libs/layouts/simple';
import { SimpleLayoutComponent } from 'ob-ui-libs/layouts/simple';
import { ForegerockGDPRConsentGuard } from 'ob-ui-libs/gdpr';
import { ForgerockGDPRService } from 'ob-ui-libs/gdpr';
import { ForgerockMainLayoutModule } from 'ob-ui-libs/layouts/main-layout';
import { ForgerockMainLayoutComponent } from 'ob-ui-libs/layouts/main-layout';
import { IForgerockMainLayoutConfig, IForgerockMainLayoutNavigations } from 'ob-ui-libs/layouts/main-layout';
import {
  ForgerockToolbarMenuComponentModule,
  ForgerockToolbarMenuContainer,
  IsConnectedPrivateGuard,
  IsConnectedPublicGuard
} from 'ob-ui-libs/authentication';
import { ForgerockCustomerCanAccessGuard } from 'ob-ui-libs/guards';

export const routes: Routes = [
  {
    path: '',
    pathMatch: 'full',
    redirectTo: 'profile'
  },
  {
    path: ForgerockGDPRService.gdprDeniedPage,
    component: SimpleLayoutComponent,
    loadChildren: () => import('forgerock/src/ob-ui-libs-lazy/gdpr.module').then(m => m.OBUILibsLazyGDPRPage)
  },
  {
    path: '',
    component: ForgerockMainLayoutComponent,
    canActivate: [ForegerockGDPRConsentGuard],
    children: [
      {
        path: 'profile',
        pathMatch: 'full',
        redirectTo: 'profile/details'
      },
      {
        path: 'profile/details',
        canLoad: [ForgerockCustomerCanAccessGuard],
        canActivate: [IsConnectedPrivateGuard],
        loadChildren: () =>
          import('forgerock/src/ob-ui-libs-lazy/authentication/details.module').then(
            m => m.OBUILibsLazyAuthProfileDetailsPage
          )
      },
      {
        path: 'profile/password',
        canLoad: [ForgerockCustomerCanAccessGuard],
        canActivate: [IsConnectedPrivateGuard],
        loadChildren: () =>
          import('forgerock/src/ob-ui-libs-lazy/authentication/password.module').then(
            m => m.OBUILibsLazyAuthProfilePasswordPage
          )
      }
    ]
  },
  {
    path: '',
    component: SimpleLayoutComponent,
    canActivate: [ForegerockGDPRConsentGuard],
    children: [
      {
        path: 'login',
        canActivate: [IsConnectedPublicGuard],
        canLoad: [ForgerockCustomerCanAccessGuard],
        loadChildren: 'bank/src/app/pages/login/login.module#LoginModule',
        data: {
          private: true
        }
      },
      {
        path: 'register',
        canActivate: [IsConnectedPublicGuard],
        canLoad: [ForgerockCustomerCanAccessGuard],
        loadChildren: 'bank/src/app/pages/register/register.module#RegisterModule'
      },
      {
        path: 'consent',
        canActivate: [IsConnectedPrivateGuard],
        loadChildren: 'bank/src/app/pages/consent/consent.module#ConsentModule'
      },
      {
        path: 'logged-out',
        loadChildren: 'bank/src/app/pages/logout/logout.module#LogoutModule'
      },
      {
        path: 'dev/consent',
        canLoad: [ForgerockCustomerCanAccessGuard],
        loadChildren: 'bank/src/app/pages/consent-dev/consent-dev.module#ConsentDevModule'
      },
      {
        path: 'oauth2/authorize',
        loadChildren: () =>
          import('forgerock/src/ob-ui-libs-lazy/authentication/oauth2-authorize.module').then(
            m => m.OBUILibsLazyAuthOauth2AuthorizePage
          )
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
      id: 'profile',
      translate: 'NAV.PROFILE',
      type: 'item',
      icon: 'face',
      url: '/profile/details'
    },
    {
      id: 'security',
      translate: 'NAV.SECURITY',
      type: 'item',
      icon: 'security',
      url: '/profile/password'
    }
  ]
};

@NgModule({
  imports: [
    ForgerockSimpleLayoutModule,
    ForgerockToolbarMenuComponentModule,
    RouterModule.forRoot(routes),
    ForgerockMainLayoutModule.forRoot({
      layout: mainLayoutConfig,
      navigations,
      components: {
        toolbar: ForgerockToolbarMenuContainer
      }
    })
  ],
  exports: [ForgerockSimpleLayoutModule, RouterModule]
})
export class AppRoutingModule {}
