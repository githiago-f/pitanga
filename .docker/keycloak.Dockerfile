FROM quay.io/keycloak/keycloak:24.0.3-0 AS builder

ARG CERT_PATH="./.docker/certs/certificate.pem"
ARG KEY_PATH="./.docker/certs/key.pem"

COPY $CERT_PATH /opt/keycloak/certs/certificate.pem
COPY $KEY_PATH /opt/keycloak/certs/key.pem

ENV KC_HTTPS_CERTIFICATE_FILE=/opt/keycloak/certs/certificate.pem
ENV KC_HTTPS_CERTIFICATE_KEY_FILE=/opt/keycloak/certs/key.pem
ENV KC_HOSTNAME_STRICT_HTTPS=true
ENV KC_HOSTNAME_STRICT=false

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_FEATURES=token-exchange

ARG PG_URL="jdbc:postgresql://postgres:5432/kc_pitanga"
ARG PG_NAME="postgres"
ARG PG_PASS="mysecretpassword"

ENV KC_DB_URL=$PG_URL
ENV KC_DB_USERNAME=$PG_NAME
ENV KC_DB_PASSWORD=$PG_PASS
ENV KC_PROXY_HEADERS=xforwarded
ENV KC_HTTP_RELATIVE_PATH=auth

ENV KC_DB=postgres

RUN /opt/keycloak/bin/kc.sh build

## COPY ENV on this section too
FROM builder AS prod
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]

EXPOSE 8443

CMD ["start", "--verbose", "--optimized"]
