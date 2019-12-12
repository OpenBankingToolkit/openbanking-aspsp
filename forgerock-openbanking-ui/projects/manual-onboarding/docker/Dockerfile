# Stage 1 - the build process
FROM r.cfcr.io/openbanking/obri/build-ob-aspsp-ui as build-ui
RUN npm run build.manual-onboarding.themes

# Stage 2 - the production environment
FROM nginx:1.13.9-alpine

EXPOSE 80

ARG BUILD_VERSION
ENV BUILD_VERSION ${BUILD_VERSION}
ENV DOMAIN ""
ENV TEMPLATE ""
ENV ENABLE_CUSTOMIZATION "false"

RUN rm -rf /etc/nginx/conf.d
COPY --from=build-ui /src/dist /usr/share/nginx
COPY projects/manual-onboarding/docker/nginx.conf /etc/nginx/conf.d/default.conf
COPY projects/manual-onboarding/docker/start.sh /opt/start.sh

CMD ["/opt/start.sh"]
