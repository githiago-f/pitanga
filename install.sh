#!/bin/bash

if (!(test -e .env)); then
    echo "ERROR: Invalid .env file, create it with the .env.example file";
    exit 1;
fi

source .env

CommonName=$COMMON_NAME

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

KEY_PATH=$CERT_BASE_PATH/key.pem
CERT_PATH=$CERT_BASE_PATH/certificate.pem

if (!(test -e $CERT_BASE_PATH)); then
    echo "Creating resources cert path $CERT_BASE_PATH"
    mkdir $CERT_BASE_PATH
fi

if (!(test -e $CERT_PATH)); then
    openssl req -x509 -newkey rsa:4096 -nodes -keyout $KEY_PATH -out $CERT_PATH \
        -sha256 -days 3650 -nodes \
        -subj "/C=BR/ST=RioGrandeDoSul/L=PortoAlegre/O=IFRSPitanga/OU=TI/CN=$CommonName"

    keytool -delete -cacerts -alias pitanga -storepass $STORE_PASS
    keytool -importcert -cacerts -file $CERT_PATH -alias pitanga -storepass $STORE_PASS

    sudo chmod og+r $KEY_PATH
    docker compose build keycloak
fi

for project in "${projects[@]}"; do
    mkdir ./$project/src/main/resources/certs
    cp $CERT_PATH ./$project/src/main/resources/certs/certificate.pem
    cp $KEY_PATH  ./$project/src/main/resources/certs/key.pem
done

docker build ./.docker -t pitanga/compilers:1.0.0 --file ./.docker/compilers.Dockerfile
docker build ./.docker -t pitanga/code:1.0.0 --file ./.docker/pitanga-code.Dockerfile

docker compose up -d nginx
