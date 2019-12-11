export function encodeQueryData(data: { [key: string]: any } = {}): string {
  const keys = Object.keys(data);
  if (!keys.length) return '';

  return (
    '?' +
    Object.keys(data)
      .map(function(key) {
        return [key, data[key]].map(encodeURIComponent).join('=');
      })
      .join('&')
  );
}

export function removeLeadingSlash(url: string = ''): string {
  return url.replace(/^\/+/g, '');
}

export function replaceURLOrigin(url: string, newOrigin: string = ''): string {
  const URLObject = new URL(url);
  return url.replace(URLObject.origin, newOrigin);
}
