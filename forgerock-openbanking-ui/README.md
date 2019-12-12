## Bank

### Serve

add the following to you `etc/hosts` since we use the integration backend by default

```
127.0.0.1   bank.dev-ob.forgerock.financial
```

Run `npm run serve.bank`

Open `https://bank.dev-ob.forgerock.financial:4200`

### Building docker image

```bash
# Build Analytics
docker build -t r.cfcr.io/openbanking/obri/analytics -f projects/analytics/docker/Dockerfile .
# Build Analytics Server
docker build -t r.cfcr.io/openbanking/obri/analytics-server -f projects/analytics/docker/Dockerfile-server .
# Build Bank
docker build -t r.cfcr.io/openbanking/obri/bank -f projects/bank/docker/Dockerfile .
# Build Native Bank
docker build --build-arg ANDROID_KEYPASS=$ANDROID_KEYPASS --build-arg ANDROID_KEYSTOREPASS=$ANDROID_KEYSTOREPASS -t r.cfcr.io/openbanking/obri/native-bank -f native/bank/Dockerfile .
# Build Auth
docker build -t r.cfcr.io/openbanking/obri/auth -f projects/auth/docker/Dockerfile .
# Build Manual Onboarding
docker build -t r.cfcr.io/openbanking/obri/manual-onboarding -f projects/manual-onboarding/docker/Dockerfile .
# Build Directory
docker build -t r.cfcr.io/openbanking/obri/directory -f projects/directory/docker/Dockerfile .
# Build TPP
docker build -t r.cfcr.io/openbanking/obri/tpp -f projects/tpp/docker/Dockerfile .
# Build TPP Server
docker build -t r.cfcr.io/openbanking/obri/tpp-server -f projects/tpp/docker/Dockerfile-server .
```

### Running docker container

```bash
# Serve Analytics
docker run -it -p 4444:80 -e TEMPLATE=hackathon -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/analytics
# Serve Analytics server
docker run -it -p 4444:80 -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/analytics-server
# Serve Bank
docker run -it -p 4444:80 -e TEMPLATE=hackathon -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/bank
# Serve Auth
docker run -it -p 4444:80 -e TEMPLATE=hackathon -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/auth
# Serve Manual Onboarding
docker run -it -p 4444:80 -e TEMPLATE=hackathon -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/manual-onboarding
# Serve Directory
docker run -it -p 4444:80 -e TEMPLATE=hackathon -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/directory
# Serve TPP
docker run -it -p 4444:80 -e TEMPLATE=hackathon -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/tpp
# Serve TPP server
docker run -it -p 4444:80 -e DOMAIN=dev-ob.forgerock.financial r.cfcr.io/openbanking/obri/tpp-server
```

## Adding a new cluster

Open `../keystore` and modify `Makefile`

```
DOMAIN=<CLUSTER_NAME>.forgerock.financial
HOSTNAME_ASPSP_UI=<PREFIX>.bank.${DOMAIN}
HOSTNAME_AUTH_UI=<PREFIX>.auth.${DOMAIN}
HOSTNAME_DIRECTORY_UI=<PREFIX>.directory.${DOMAIN}
HOSTNAME_REGISTER_UI=<PREFIX>.register.${DOMAIN}
HOSTNAME_ANALYTICS_UI=<PREFIX>.analytics.${DOMAIN}
HOSTNAME_TPP_UI=<PREFIX>.tpp.${DOMAIN}
```

then run `make all`

`/etc/hosts`

```
127.0.0.1	<PREFIX>.auth.<CLUSTER_NAME>.forgerock.financial <PREFIX>.bank.<CLUSTER_NAME>.forgerock.financial <PREFIX>.directory.<CLUSTER_NAME>.forgerock.financial <PREFIX>.register.<CLUSTER_NAME>.forgerock.financial
```

## Adding a new Customer

[ADD_CUSTOMER.md](ADD_CUSTOMER.md)
