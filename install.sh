CommonName="localhost"

KEY_PATH=./core/src/main/resources/certs/key.pem
CERT_PATH=./core/src/main/resources/certs/certificate.pem

if (!(test -e $CERT_PATH)); then
    openssl req -x509 -newkey rsa:4096 -nodes -keyout $KEY_PATH -out $CERT_PATH \
        -sha256 -days 3650 -nodes \
        -subj "/C=BR/ST=RioGrandeDoSul/L=PortoAlegre/O=IFRSPitanga/OU=TI/CN=$CommonName"

    keytool -delete -cacerts -alias  pitanga -storepass $STORE_PASS
    keytool -importcert -cacerts -file $CERT_PATH -alias pitanga -storepass $STORE_PASS

    sudo chmod og+r $KEY_PATH
    docker compose up -d --build keycloak
fi

docker compose up -d postgres keycloak

export SHOW_SQL=false
export DB_URL="jdbc:postgresql://<database_host>:<db_port>/<db_name>"
export DB_PASSWORD="<db_password>"
export DB_USERNAME="<db_user>"
export ISSUER_URI="https://<keycloak_host>/realms/master"
export JWK_SET_URI="https://<keycloak_host>/realms/master/protocol/openid-connect/certs"

cd ./core
./mvnw clean package
cd ..

cp ./core/target/*.jar ./app.jar
