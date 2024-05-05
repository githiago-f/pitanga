FROM quay.io/keycloak/keycloak:24.0.3-0 as builder

# Enable health and metrics support
ENV KC_HEALTH_ENABLED=true
ENV KC_METRICS_ENABLED=true
ENV KC_FEATURES=token-exchange

# Configure a database vendor
ENV KC_DB=postgres

WORKDIR /opt/keycloak

ENV KC_DB=postgres
ENV KC_DB_URL=jdbc:postgres://postgres:5432/postgres
ENV KC_DB_USERNAME=postgres
ENV KC_DB_PASSWORD=mysecretpassword
ENV KC_HOSTNAME_STRICT_HTTPS=true

RUN keytool -genkeypair -storepass password -storetype PKCS12 -keyalg RSA -keysize 2048 -dname "CN=server" -alias server -ext "SAN:c=DNS:localhost,IP:127.0.0.1" -keystore conf/server.keystore
RUN /opt/keycloak/bin/kc.sh build

FROM quay.io/keycloak/keycloak:24.0.3-0
COPY --from=builder /opt/keycloak/ /opt/keycloak/

ENV KC_HOSTNAME_URL=https://localhost:8444

ENTRYPOINT ["/opt/keycloak/bin/kc.sh"]

CMD ["start"]
