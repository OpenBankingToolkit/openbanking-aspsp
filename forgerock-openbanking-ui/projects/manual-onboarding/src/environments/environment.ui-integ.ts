import { StoreDevtoolsModule } from '@ngrx/store-devtools';

import { environment as defaultEnv } from './environment.dev.default';
import { IForgerockMainLayoutNavigation } from '@forgerock/openbanking-ngx-common/layouts/main-layout';

const mainNav: IForgerockMainLayoutNavigation[] = [
  {
    id: 'home',
    translate: 'NAV.ONBOARDING',
    type: 'item',
    icon: 'dashboard',
    exactMatch: true,
    url: '/'
  },
  {
    id: 'psu',
    translate: 'PSU credentials',
    type: 'item',
    icon: 'vpn_key',
    exactMatch: true,
    url: '/psu-credentials'
  }
];

export const environment = {
  ...defaultEnv,
  cookieDomain: '.ui-integ.forgerock.financial',
  apiUrl: 'https://as.aspsp.ui-integ.forgerock.financial',
  authEndpoint: 'https://am.ui-integ.forgerock.financial',
  registerBackend: 'https://service.register.ui-integ.forgerock.financial',
  devModules: [
    StoreDevtoolsModule.instrument({
      maxAge: 25,
      logOnly: false
    })
  ],
  navigations: {
    main: mainNav
  },
  lloyds: {
    psuPage: {
      content: `
        <h1>Title</h1>
        <p>Users registered with Open Banking can test their applications with mock accounts created for the following banks.</p>
        <p>Mock account and payment data matches the our current product range for: </p> 
        <ul>
        <li>Retail</li>
        <li>Business</li>
        <li>Commercial</li>
        </ul>
        <table class="psu-table">
          <thead>
              <tr>
                  <th>Brand</th>
                  <th>Retail</th>
                  <th>Commercial</th>
                  <th>Business</th>
              </tr>
          </thead>
          <tbody>
              <tr>
                  <td>Lloyds Bank</td>
                  <td>har001</td>
                  <td>llc001</td>
                  <td>llb001</td>
              </tr>
              <tr>
                  <td>Halifax</td>
                  <td>har001</td>
                  <td>N/A</td>
                  <td>N/A</td>
              </tr>
              <tr>
                  <td>Bank of Scotland</td>
                  <td>Bar001</td>
                  <td>bac001</td>
                  <td>bab001</td>
              </tr>
              <tr>
                  <td>MBNA</td>
                  <td>mbr001</td>
                  <td>N/A</td>
                  <td>N/A</td>
              </tr>
          </tbody>
        </table>
        <style>
          .psu-table {
            margin: 10px 0;
          }
        </style>
        `
    }
  }
};
