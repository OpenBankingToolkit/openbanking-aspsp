import { IApplication } from '.';

export interface IApiPostRegisterBody {
  applicationName: string;
  redirectUris: string[];
  softwareStatementAssertion: string;
}

export interface IApiError {
  ErrorCode: string;
  Message: string;
  Url: string;
}

export interface IApiPostRegisterReponseSuccess extends IApplication {}

export interface IApiPostRegisterReponseError {
  Code: string;
  Id: string;
  Message: string;
  Errors: IApiError[];
}
