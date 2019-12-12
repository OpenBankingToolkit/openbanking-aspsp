import { catchError } from 'rxjs/operators';
import { throwError, Observable } from 'rxjs';
import { HttpErrorResponse } from '@angular/common/http';
import _get from 'lodash/get';

export function withErrorHandling(obs: Observable<any>): Observable<HttpErrorResponse> {
  return obs.pipe(
    catchError((er: HttpErrorResponse) => {
      const errorMessage = _get(er, 'error.Message') || _get(er, 'error.message') || _get(er, 'message');
      return throwError({
        ...er,
        message: errorMessage || 'Something wrong happened'
      });
    })
  );
}
