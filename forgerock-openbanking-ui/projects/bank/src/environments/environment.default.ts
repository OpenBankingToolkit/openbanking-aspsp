// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
export const environment = {
  production: false,
  authorizationServer: 'https://as.aspsp.ui-dev.forgerock.financial',
  authenticationServer: 'https://am.dev-ob.forgerock.financial:8074',
  remoteConsentServer: 'https://rcs.aspsp.dev-ob.forgerock.financial:8074',
  defaultRealm: 'openbanking',
  realmRedirections: {
    default: '/profile'
  },
  client: {
    name: 'Forgerock',
    adminContact: 'openbanking-support@forgerock.com',
    iconWidth: 35,
    iconHeight: 39,
    logoWidth: 230,
    logoHeight: 'auto'
  },
  featureFlags: {
    disableProfileForm: false,
    disablePasswordForm: false
  },
  routeDenyList: [],
  devModules: []
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
