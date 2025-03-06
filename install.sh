CommonName="localhost"

export SHOW_SQL=false
export CHALLENGES_DB_URL="jdbc:postgresql://localhost:5432/challenges_pitanga"
export KC_DB_URL="jdbc:postgresql://postgres:5432/kc_pitanga"
export DB_PASSWORD="mysecretpassword"
export DB_USERNAME="postgres"
export ISSUER_URI="https://localhost/auth/realms/master"
export JWK_SET_URI="https://localhost/auth/realms/master/protocol/openid-connect/certs"

KEY_PATH=./core/src/main/resources/certs/key.pem
CERT_PATH=./core/src/main/resources/certs/certificate.pem

export resource_cert_path=${CERT_PATH%/*}

if (!(test -e $resource_cert_path)); then
    echo "Creating resources cert path $resource_cert_path"
    mkdir $resource_cert_path
fi

if (!(test -e $CERT_PATH)); then
    openssl req -x509 -newkey rsa:4096 -nodes -keyout $KEY_PATH -out $CERT_PATH \
        -sha256 -days 3650 -nodes \
        -subj "/C=BR/ST=RioGrandeDoSul/L=PortoAlegre/O=IFRSPitanga/OU=TI/CN=$CommonName"

    keytool -delete -cacerts -alias pitanga -storepass $STORE_PASS
    keytool -importcert -cacerts -file $CERT_PATH -alias pitanga -storepass $STORE_PASS

    sudo chmod og+r $KEY_PATH
    docker compose up -d --build keycloak
fi

docker compose up -d nginx
