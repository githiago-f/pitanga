#!/bin/bash

# Check for .env file
if (!(test -e .env)); then
    echo "ERROR: Invalid .env file, create it with the .env.example file";
    exit 1;
fi

source .env

# Check for the projects on root pom.xml
read_dom () {
    local IFS=\>
    read -d \< ENTITY CONTENT
}

projects=()

while read_dom; do
    if [[ $ENTITY = "module" ]]; then
        projects=("${projects[@]}" "$CONTENT")
    fi
done < pom.xml

# Create cert for each project and also for keyclock
KEY_PATH=$CERT_BASE_PATH/key.pem
CERT_PATH=$CERT_BASE_PATH/certificate.pem
KC_KEY_PATH=$CERT_BASE_PATH/kc/key.pem
KC_CERT_PATH=$CERT_BASE_PATH/kc/certificate.pem
CommonName=$COMMON_NAME

if (!(test -e $CERT_BASE_PATH)); then
    echo "Creating resources cert path $CERT_BASE_PATH"
    mkdir $CERT_BASE_PATH
    mkdir $CERT_BASE_PATH/kc
fi

if (!(test -e $CERT_PATH)); then
    # define the common name cert for ngix or spring applications ($COMMON_NAME)
    openssl req -x509 -newkey rsa:4096 -nodes -keyout $KEY_PATH -out $CERT_PATH \
        -sha256 -days 3650 -nodes \
        -subj "/C=BR/ST=RioGrandeDoSul/L=PortoAlegre/O=IFRSPitanga/OU=TI/CN=$CommonName"

    # define the docker cert for keycloak
    openssl req -x509 -newkey rsa:4096 -nodes -keyout $KC_KEY_PATH -out $KC_CERT_PATH \
        -sha256 -days 3650 -nodes \
        -subj "/C=BR/ST=RioGrandeDoSul/L=PortoAlegre/O=IFRSPitanga/OU=TI/CN=keycloak"

    keytool -delete -cacerts -alias pitanga -storepass changeit
    keytool -delete -cacerts -alias keycloak -storepass changeit
    keytool -importcert -trustcacerts -noprompt -cacerts -file $CERT_PATH -alias pitanga -storepass changeit
    keytool -importcert -trustcacerts -noprompt -cacerts -file $KC_CERT_PATH -alias keycloak -storepass changeit

    sudo chmod og+r $KEY_PATH
    sudo chmod og+r $KC_KEY_PATH
    docker compose build keycloak
fi

for project in "${projects[@]}"; do
    mkdir ./$project/src/main/resources/certs
    cp $CERT_PATH ./$project/src/main/resources/certs/certificate.pem
    cp $KEY_PATH  ./$project/src/main/resources/certs/key.pem
done

# Build compilers and start the docker container
docker build ./docker -t pitanga/compilers:1.0.0 --file ./docker/compilers.Dockerfile

docker compose build pitanga-code
docker compose up -d nginx
