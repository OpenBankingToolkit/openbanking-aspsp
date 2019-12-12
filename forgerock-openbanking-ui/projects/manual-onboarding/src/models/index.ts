export * from './api';
export * from './store';

export interface IApplication {
  id: string;
  userId: string;
  softwareClientId: string;
  manualRegistrationRequest: any;
  oidcRegistrationResponse: any;
}
