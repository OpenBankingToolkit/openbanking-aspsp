import { FormControl } from '@angular/forms';

export const urlRegex = /^(http:\/\/www\.|https:\/\/www\.|http:\/\/|https:\/\/)?[a-z0-9]+([\-.]{1}[a-z0-9]+)*\.[a-z]{2,5}(:[0-9]{1,5})?(\/.*)?$/;

export function validateUrl(c: FormControl) {
  if (c.value) {
    return urlRegex.test(c.value)
      ? null
      : {
          validateUrl: {
            valid: false
          }
        };
  }
  return null;
}

export function validateMultipleUrls(c: FormControl) {
  const value: string = c.value;
  if (value) {
    const urls = value.split(',');
    const filteredUrls = urls.filter(url => !urlRegex.test(url.trim()));
    return !filteredUrls.length
      ? null
      : {
          validateMultipleUrls: {
            valid: false
          }
        };
  }
  return null;
}
