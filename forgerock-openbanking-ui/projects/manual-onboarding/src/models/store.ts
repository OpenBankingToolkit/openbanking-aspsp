import { EntityState } from '@ngrx/entity';
import { IApplication } from 'manual-onboarding/src/models';
import { IOIDCState } from '@forgerock/openbanking-ngx-common/oidc';

export interface IApplicationState extends EntityState<IApplication> {
  isCollectionLoading: boolean;
  isRegisterAppLoading: boolean;
  isRegisterAppSuccess: boolean;
  isUnregisterAppLoading: boolean;
  selectedApplicationId: string | null;
}

export interface IState {
  applications: IApplicationState;
  oidc: IOIDCState;
}
