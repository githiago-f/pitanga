###############################
# Stage 1: Build the Artifact #
###############################
FROM maven:3.9-eclipse-temurin-21 AS build

ARG PITANGA_CODE_PATH="pitanga-code"

WORKDIR /build

COPY ${PITANGA_CODE_PATH}/pom.xml .
RUN mvn dependency:go-offline -B

COPY $PITANGA_CODE_PATH/src ./src
COPY $PITANGA_CODE_PATH/pom.xml .
COPY ./docker/certs/kc/certificate.pem ./kc/certificate.pem

RUN mvn clean package -DskipTests

#####################################
# Stage 2: Create the Runtime Image #
#####################################
FROM pitanga/compilers:1.0.0 AS production

WORKDIR /opt/app

COPY --from=build /build/target/pitanga-code-*.jar ./pitanga-code.jar
RUN mkdir -p ./kc && mkdir -p ./tmp

COPY ./docker/isolate/entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

COPY --from=build /build/kc/certificate.pem ./kc/certificate.pem

ENV JAVA_HOME="/usr/local/jdk21"
ENV PATH="/usr/local/jdk21/bin:$PATH"

RUN keytool -importcert \
    -trustcacerts \
    -noprompt \
    -cacerts \
    -file ./kc/certificate.pem \
    -alias keycloak \
    -storepass changeit

RUN set -xe && \
    apt-get update && \
    apt-get install -y --no-install-recommends \
    libpq-dev \
    sudo \
    && rm -rf /var/lib/apt/lists/*

RUN useradd -u 1000 -m -r pitanga && \
    echo "pitanga ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers && \
    chown pitanga: /opt/app/tmp

USER pitanga

EXPOSE 8443
LABEL version=0.0.2

ENTRYPOINT [ "/entrypoint.sh" ]
CMD ["java", "-jar", "pitanga-code.jar"]

FROM production AS development

CMD ["sleep", "infinity"]
