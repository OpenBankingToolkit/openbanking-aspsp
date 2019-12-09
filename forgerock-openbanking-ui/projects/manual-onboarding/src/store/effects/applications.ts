import { Injectable } from '@angular/core';
import { Action } from '@ngrx/store';
import { Actions, Effect, ofType } from '@ngrx/effects';
import { Observable, of } from 'rxjs';
import { catchError, map, mergeMap } from 'rxjs/operators';
import { TranslateService } from '@ngx-translate/core';
import { HttpErrorResponse } from '@angular/common/http';
import _get from 'lodash-es/get';

import {
  types,
  ActionsUnion,
  UnregisterApplicationSuccessAction,
  RegisterApplicationSuccessAction,
  LoadApplicationsSuccessAction,
  LoadApplicationsErrorAction,
  RegisterApplicationErrorAction,
  UnregisterApplicationRequestAction,
  RegisterApplicationRequestAction,
  UnregisterApplicationErrorAction
} from 'manual-onboarding/src/store/reducers/applications';
import { ApiService } from 'manual-onboarding/src/app/services/api.service';
import { ForgerockMessagesService } from 'ob-ui-libs/services/forgerock-messages';
import { IApplication } from 'manual-onboarding/src/models';

@Injectable()
export class ApplicationEffects {
  constructor(
    private api: ApiService,
    private actions$: Actions,
    private message: ForgerockMessagesService,
    private translate: TranslateService
  ) {}

  @Effect()
  getAllApplications$: Observable<Action> = this.actions$.pipe(
    ofType(types.APPLICATIONS_REQUEST),
    mergeMap((action: ActionsUnion) =>
      this.api.getApplications().pipe(
        map(
          (applications: IApplication[]) =>
            new LoadApplicationsSuccessAction({
              applications
            })
        ),
        catchError((e: HttpErrorResponse) => of(new LoadApplicationsErrorAction({ error: e.message })))
      )
    )
  );

  @Effect()
  registerApplication$: Observable<Action> = this.actions$.pipe(
    ofType(types.REGISTER_APPLICATION_REQUEST),
    mergeMap((action: RegisterApplicationRequestAction) =>
      this.api.registerApplication(action.payload.form).pipe(
        map((application: IApplication) => {
          return new RegisterApplicationSuccessAction({ application });
        }),
        catchError((e: HttpErrorResponse) => {
          const errorMessage = _get(e.error, 'Errors[0].Message', this.translate.instant('ADD_ERROR'));
          this.message.error(errorMessage);
          return of(new RegisterApplicationErrorAction({ error: errorMessage }));
        })
      )
    )
  );

  @Effect()
  unregisterApplication$: Observable<Action> = this.actions$.pipe(
    ofType(types.UNREGISTER_APPLICATION_REQUEST),
    mergeMap((action: UnregisterApplicationRequestAction) =>
      this.api.unregisterApplication(action.payload.application.id).pipe(
        map((application: IApplication) => {
          return new UnregisterApplicationSuccessAction({ application });
        }),
        catchError((e: HttpErrorResponse) => {
          const errorMessage = _get(e.error, 'Errors[0].Message', this.translate.instant('REMOVE_ERROR'));
          this.message.error(errorMessage);
          return of(new UnregisterApplicationErrorAction({ error: errorMessage }));
        })
      )
    )
  );
}
