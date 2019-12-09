import { environment as defaultEnv } from './environment.default';

export const environment = {
  ...defaultEnv,
  production: true,
  authorizationServer: 'https://as.aspsp.ob.forgerock.financial',
  authenticationServer: 'https://am.ob.forgerock.financial',
  remoteConsentServer: 'https://rcs.aspsp.ob.forgerock.financial'
};
