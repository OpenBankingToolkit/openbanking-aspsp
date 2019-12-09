import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';

import { ForgerockConfigService } from 'ob-ui-libs/services/forgerock-config';
import { IApiPostRegisterBody } from 'manual-onboarding/src/models';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(private http: HttpClient, private configService: ForgerockConfigService) {}

  getApplications(): Observable<Object> {
    return this.http.get(
      `${this.configService.get('registerBackend')}/open-banking/manual-onboarding/registerApplication`,
      getDefaultHeaders()
    );
  }

  getApplication(id: string): Observable<Object> {
    return this.http.get(
      `${this.configService.get('registerBackend')}/open-banking/manual-onboarding/registerApplication/${id}`,
      getDefaultHeaders()
    );
  }

  registerApplication(body: IApiPostRegisterBody): Observable<Object> {
    return this.http.post(
      `${this.configService.get('registerBackend')}/open-banking/manual-onboarding/registerApplication`,
      body,
      getDefaultHeaders()
    );
  }

  unregisterApplication(id: string): Observable<Object> {
    return this.http.delete(
      `${this.configService.get('registerBackend')}/open-banking/manual-onboarding/registerApplication/${id}`,
      getDefaultHeaders()
    );
  }
}

function getDefaultHeaders(headers: { [key: string]: string } = {}) {
  return {
    withCredentials: true,
    headers: new HttpHeaders({
      'Content-Type': 'application/json',
      ...headers
    })
  };
}
