import { environment as defaultEnv } from './environment.dev.default';

export const environment = {
  ...defaultEnv,
  production: true,
  cookieDomain: '.ob.forgerock.financial',
  apiUrl: 'https://as.aspsp.ob.forgerock.financial',
  authEndpoint: 'https://am.ob.forgerock.financial',
  registerBackend: 'https://service.register.ob.forgerock.financial'
};
