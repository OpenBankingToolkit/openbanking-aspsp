import { environment as devDefaultEnv } from './environment.dev.default';

export const environment = {
  ...devDefaultEnv,
  production: false,
  authorizationServer: 'https://as.aspsp.ui-integ.forgerock.financial',
  authenticationServer: 'https://am.ui-integ.forgerock.financial',
  remoteConsentServer: 'https://rcs.aspsp.ui-integ.forgerock.financial'
  // featureFlags: {
  //   disableProfileForm: false,
  //   disablePasswordForm: false
  // },
  // routeDenyList: ['profile/password']
};
