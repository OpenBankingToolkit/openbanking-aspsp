import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';

import { ForgerockConfigService } from 'ob-ui-libs/services/forgerock-config';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  constructor(private http: HttpClient, private configService: ForgerockConfigService) {}

  getConsentDetails(consentRequest: string) {
    return this.http.post(`${this.configService.get('remoteConsentServer')}/api/rcs/consent/details/`, consentRequest, {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/jwt'
      })
    });
  }

  postConsentDecision(decisionAPIUri: string, body: any) {
    return this.http.post(`${this.configService.get('remoteConsentServer')}${decisionAPIUri}`, body, {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/json'
      })
    });
  }

  postConsentAuthorization(redirectUri: string, consent_response: string) {
    const body = new URLSearchParams();
    body.set('consent_response', consent_response);
    return this.http.post(redirectUri, body.toString(), {
      withCredentials: true,
      headers: new HttpHeaders({
        'Content-Type': 'application/x-www-form-urlencoded'
      })
    });
  }
}
