#!/usr/bin/env sh
set -e

if [[ -z "${TEMPLATE}" ]]; then
  echo "TEMPLATE environment variable should be set to 'hackathon' or 'forgerock'"
  exit 1
fi

if [[ -z "${DOMAIN}" ]]; then
  echo "DOMAIN environment variable should be set"
  exit 1
fi

cp -r /usr/share/nginx/${TEMPLATE}/* /usr/share/nginx/html
sed -i "s/BUILD_VERSION/${BUILD_VERSION}/g" /usr/share/nginx/html/deployment-settings.json
sed -i "s/TEMPLATE/${TEMPLATE}/g" /usr/share/nginx/html/deployment-settings.json
sed -i "s/ANDROID_PKG_NAME/${ANDROID_PKG_NAME}/g" /usr/share/nginx/html/.well-known/assetlinks.json
sed -i "s/IOS_APP_ID/${IOS_APP_ID}/g" /usr/share/nginx/html/.well-known/apple-app-site-association
sed -i "s@AUTHORIZATION_URL@${AUTHORIZATION_URL}@g" /usr/share/nginx/html/deployment-settings.json
sed -i "s@AUTHENTICATION_URL@${AUTHENTICATION_URL}@g" /usr/share/nginx/html/deployment-settings.json
sed -i "s@REMOVE_CONSENT_URL@${REMOVE_CONSENT_URL}@g" /usr/share/nginx/html/deployment-settings.json
sed -i "s/DOMAIN/${DOMAIN}/g" /usr/share/nginx/html/deployment-settings.json
nginx -g 'daemon off;'
