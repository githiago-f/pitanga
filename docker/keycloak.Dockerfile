FROM quay.io/keycloak/keycloak:24.0.3-0 as builder

ARG PG_PASSWORD="mysecretpassword"
ARG PG_URL="jdbc:postgres://postgres:5432/postgres"
ARG PG_USERNAME="postgres"

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_FEATURES=token-exchange

# Configure a database vendor
ENV KC_DB=postgres

COPY ./core/src/main/resources/certs/certificate.pem /opt/keycloak/certs/certificate.pem
COPY ./core/src/main/resources/certs/key.pem /opt/keycloak/certs/key.pem

ENV KC_DB=postgres
ENV KC_DB_URL="${PG_URL}"
ENV KC_DB_USERNAME="${PG_USERNAME}"
ENV KC_DB_PASSWORD="${PG_PASSWORD}"

RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:24.0.3-0
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENV KC_HTTPS_CERTIFICATE_FILE=/opt/keycloak/certs/certificate.pem
ENV KC_HTTPS_CERTIFICATE_KEY_FILE=/opt/keycloak/certs/key.pem
ENV KC_HOSTNAME_STRICT_HTTPS=true
ENV KC_HOSTNAME_URL=https://localhost:8444

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]

CMD ["start", "--verbose"]
