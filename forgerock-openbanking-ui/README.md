## Running docker image

<https://hub.docker.com/repository/docker/openbankingtoolkit/openbanking-bank-ui> & <https://hub.docker.com/repository/docker/openbankingtoolkit/openbanking-register-ui> is a built version of the Analytics app with only the Forgerock template.

It is convenient to start the app in no time.

- `<PORT>`: **REQUIRED** Port to use on your machine
- `<DOMAIN>`: **REQUIRED** Domain to use. Will replace `DOMAIN` in the frontend [config](./forgerock-openbanking-ui/projects/analytics/docker/deployment-settings.js) e.g: `https://analytics.DOMAIN`
- `<TEMPLATE_NAME>`: Default value: `forgerock`.

```bash
docker run -it -p <PORT>:80 -e TEMPLATE=<TEMPLATE_NAME> -e DOMAIN=<DOMAIN> openbankingtoolkit/openbanking-bank-ui
docker run -it -p <PORT>:80 -e TEMPLATE=<TEMPLATE_NAME> -e DOMAIN=<DOMAIN> openbankingtoolkit/openbanking-register-ui
```

## Building the app with your theme

Create a new theme: <https://github.com/OpenBankingToolkit/openbanking-toolkit/wiki/Create-a-new-Theme>

Then build the docker image

## Building your own docker image

```bash
# Build Analytics
docker build -t <IMAGE_NAME> -f projects/analytics/docker/Dockerfile .
# Build Analytics Server
docker build -t <IMAGE_NAME> -f projects/analytics/docker/Dockerfile-server .
```
