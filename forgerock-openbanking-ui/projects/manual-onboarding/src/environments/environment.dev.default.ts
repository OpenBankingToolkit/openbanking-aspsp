// This file can be replaced during build by using the `fileReplacements` array.
// `ng build ---prod` replaces `environment.ts` with `environment.prod.ts`.
// The list of file replacements can be found in `angular.json`.
export const environment = {
  production: false,
  cookieDomain: '.dev-ob.forgerock.financial',
  apiUrl: 'https://as.aspsp.dev-ob.forgerock.financial:8074',
  authEndpoint: 'https://am.dev-ob.forgerock.financial:8074',
  registerBackend: 'https://service.register.dev-ob.forgerock.financial:8074',
  client: {
    name: 'Forgerock',
    adminContact: 'openbanking-support@forgerock.com',
    iconWidth: 45,
    iconHeight: 45,
    logoWidth: 230,
    logoHeight: '100%'
  },
  featureFlags: {
    disableProfileForm: false,
    disablePasswordForm: false,
    disableRegistration: false
  },
  devModules: []
};

/*
 * In development mode, to ignore zone related error stack frames such as
 * `zone.run`, `zoneDelegate.invokeTask` for easier debugging, you can
 * import the following file, but please comment it out in production mode
 * because it will have performance impact when throw error
 */
// import 'zone.js/dist/zone-error';  // Included with Angular CLI.
