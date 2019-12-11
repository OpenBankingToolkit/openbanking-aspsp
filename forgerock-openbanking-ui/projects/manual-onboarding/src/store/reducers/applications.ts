import { createSelector, Action } from '@ngrx/store';
import { EntityAdapter, createEntityAdapter } from '@ngrx/entity';
import _get from 'lodash-es/get';

import { IApplication, IApplicationState, IState, IApiPostRegisterBody } from 'manual-onboarding/src/models';

export enum types {
  APPLICATIONS_REQUEST = 'APPLICATIONS_REQUEST',
  APPLICATIONS_SUCCESS = 'APPLICATIONS_SUCCESS',
  APPLICATIONS_ERROR = 'APPLICATIONS_ERROR',
  APPLICATION_SELECT = 'APPLICATION_SELECT',
  APPLICATION_UNSELECT = 'APPLICATION_UNSELECT',
  REGISTER_APPLICATION_REQUEST = 'REGISTER_APPLICATION_REQUEST',
  REGISTER_APPLICATION_SUCCESS = 'REGISTER_APPLICATION_SUCCESS',
  REGISTER_APPLICATION_ERROR = 'REGISTER_APPLICATION_ERROR',
  UNREGISTER_APPLICATION_REQUEST = 'UNREGISTER_APPLICATION_REQUEST',
  UNREGISTER_APPLICATION_SUCCESS = 'UNREGISTER_APPLICATION_SUCCESS',
  UNREGISTER_APPLICATION_ERROR = 'UNREGISTER_APPLICATION_ERROR'
}

export class LoadApplicationsRequestAction implements Action {
  readonly type = types.APPLICATIONS_REQUEST;
}

export class LoadApplicationsSuccessAction implements Action {
  readonly type = types.APPLICATIONS_SUCCESS;
  constructor(public payload: { applications: IApplication[] }) {}
}

export class LoadApplicationsErrorAction implements Action {
  readonly type = types.APPLICATIONS_ERROR;
  constructor(public payload: { error: string }) {}
}

export class SelectApplicationAction implements Action {
  readonly type = types.APPLICATION_SELECT;
  constructor(public payload: { applicationId: string }) {}
}

export class UnselectApplicationAction implements Action {
  readonly type = types.APPLICATION_UNSELECT;
}

export class RegisterApplicationRequestAction implements Action {
  readonly type = types.REGISTER_APPLICATION_REQUEST;
  constructor(public payload: { form: IApiPostRegisterBody }) {}
}

export class RegisterApplicationSuccessAction implements Action {
  readonly type = types.REGISTER_APPLICATION_SUCCESS;
  constructor(public payload: { application: IApplication }) {}
}

export class RegisterApplicationErrorAction implements Action {
  readonly type = types.REGISTER_APPLICATION_ERROR;
  constructor(public payload: { error: string }) {}
}

export class UnregisterApplicationRequestAction implements Action {
  readonly type = types.UNREGISTER_APPLICATION_REQUEST;
  constructor(public payload: { application: IApplication }) {}
}

export class UnregisterApplicationSuccessAction implements Action {
  readonly type = types.UNREGISTER_APPLICATION_SUCCESS;
  constructor(public payload: { application: IApplication }) {}
}

export class UnregisterApplicationErrorAction implements Action {
  readonly type = types.UNREGISTER_APPLICATION_ERROR;
  constructor(public payload: { error: string }) {}
}

export type ActionsUnion =
  | LoadApplicationsRequestAction
  | LoadApplicationsSuccessAction
  | LoadApplicationsErrorAction
  | SelectApplicationAction
  | UnselectApplicationAction
  | RegisterApplicationRequestAction
  | RegisterApplicationSuccessAction
  | RegisterApplicationErrorAction
  | UnregisterApplicationRequestAction
  | UnregisterApplicationSuccessAction
  | UnregisterApplicationErrorAction;

export const adapter: EntityAdapter<IApplication> = createEntityAdapter<IApplication>();

export const initialState: IApplicationState = adapter.getInitialState({
  isCollectionLoading: false,
  isRegisterAppLoading: false,
  isRegisterAppSuccess: false,
  isUnregisterAppLoading: false,
  selectedApplicationId: null
});

export default function userReducer(state: IApplicationState = initialState, action: ActionsUnion): IApplicationState {
  switch (action.type) {
    case types.APPLICATIONS_REQUEST: {
      return { ...state, isCollectionLoading: true };
    }
    case types.APPLICATIONS_SUCCESS: {
      return { ...adapter.addAll(action.payload.applications, state), isCollectionLoading: false };
    }
    case types.APPLICATIONS_ERROR: {
      return { ...state, isCollectionLoading: false };
    }
    case types.APPLICATION_SELECT: {
      return { ...state, selectedApplicationId: action.payload.applicationId };
    }
    case types.APPLICATION_UNSELECT: {
      return { ...state, selectedApplicationId: null };
    }
    case types.REGISTER_APPLICATION_REQUEST: {
      return { ...state, isRegisterAppLoading: true, isRegisterAppSuccess: false };
    }
    case types.REGISTER_APPLICATION_SUCCESS: {
      return {
        ...adapter.addOne(action.payload.application, state),
        isRegisterAppLoading: false,
        isRegisterAppSuccess: true
      };
    }
    case types.REGISTER_APPLICATION_ERROR: {
      return { ...state, isRegisterAppLoading: false, isRegisterAppSuccess: false };
    }
    case types.UNREGISTER_APPLICATION_REQUEST: {
      return { ...state, isUnregisterAppLoading: true };
    }
    case types.UNREGISTER_APPLICATION_SUCCESS: {
      return { ...adapter.removeOne(action.payload.application.id, state), isUnregisterAppLoading: false };
    }
    case types.UNREGISTER_APPLICATION_ERROR: {
      return { ...state, isUnregisterAppLoading: false };
    }
    default:
      return state;
  }
}

// Selectors

const { selectAll, selectEntities, selectIds, selectTotal } = adapter.getSelectors(
  (state: IState) => state.applications
);

const selectSelectedApplicationId = (state: IState): string => _get(state, `applications.selectedApplicationId`);
const selectIsCollectionLoading = (state: IState): boolean => _get(state, `applications.isCollectionLoading`);
const selectIsRegisterAppLoading = (state: IState): boolean => _get(state, `applications.isRegisterAppLoading`);
const selectIsRegisterAppSuccess = (state: IState): boolean => _get(state, `applications.isRegisterAppSuccess`);
const selectIsUnregisterAppLoading = (state: IState): boolean => _get(state, `applications.isUnregisterAppLoading`);

const selectSelectedApplication = createSelector(
  selectEntities,
  selectSelectedApplicationId,
  (entities: { [id: string]: IApplication }, selectedApplicationId: string) => {
    return entities[selectedApplicationId];
  }
);

const selectIsApplicationListLoading = createSelector(
  selectIsCollectionLoading,
  selectIsRegisterAppLoading,
  selectIsUnregisterAppLoading,
  (isCollectionLoading: boolean, isRegisterAppLoading: boolean, isUnregisterAppLoading: boolean) => {
    return isCollectionLoading || isRegisterAppLoading || isUnregisterAppLoading;
  }
);

export const selectors = {
  selectAll,
  selectEntities,
  selectIds,
  selectTotal,
  selectSelectedApplicationId,
  selectSelectedApplication,
  selectIsCollectionLoading,
  selectIsRegisterAppLoading,
  selectIsRegisterAppSuccess,
  selectIsApplicationListLoading
};
