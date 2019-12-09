import { ConsentModule } from './consent.module';

describe('app:bank ConsentModule', () => {
  let consentModule: ConsentModule;

  beforeEach(() => {
    consentModule = new ConsentModule();
  });

  it('should create an instance', () => {
    expect(consentModule).toBeTruthy();
  });
});
