FROM quay.io/keycloak/keycloak:24.0.3-0 as builder

COPY ./core/src/main/resources/certs/certificate.pem /opt/keycloak/certs/certificate.pem
COPY ./core/src/main/resources/certs/key.pem /opt/keycloak/certs/key.pem

ENV KC_HTTPS_CERTIFICATE_FILE=/opt/keycloak/certs/certificate.pem
ENV KC_HTTPS_CERTIFICATE_KEY_FILE=/opt/keycloak/certs/key.pem
ENV KC_HOSTNAME_STRICT_HTTPS=true
ENV KC_HOSTNAME_STRICT=false

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_FEATURES=token-exchange

ENV KC_DB=postgres

RUN /opt/keycloak/bin/kc.sh build

## COPY ENV on this section too
FROM quay.io/keycloak/keycloak:24.0.3-0
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENV KC_HTTPS_CERTIFICATE_FILE=/opt/keycloak/certs/certificate.pem
ENV KC_HTTPS_CERTIFICATE_KEY_FILE=/opt/keycloak/certs/key.pem
ENV KC_HOSTNAME_STRICT_HTTPS=true
ENV KC_HOSTNAME_STRICT=false

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_FEATURES=token-exchange

ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgresql://postgres:5432/kc_pitanga
ENV KC_DB_USERNAME=postgres
ENV KC_DB_PASSWORD=mysecretpassword

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]

CMD ["start", "--verbose"]
