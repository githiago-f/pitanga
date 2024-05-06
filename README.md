<div style="text-align: center;">
    <img src="./pitangaweb/public/pitanga-icon.svg" alt="Pitanga Icon" style="height: 64px;"/>
    <h3>Pitanga</h3>
</div>

## Sobre

Pitanga é uma ferramenta online destinada a auxiliar o aprendizado de programação, que utiliza conceitos de mobile-learning e através do aprendizado baseado em problemas (PBL) visa ampliar o conhecimento produzido em aula para fora dos limites da sala.

## Run ngrok

```bash
ngrok http --domain=distinct-serval-known.ngrok-free.app https://localhost:8443
```

## Generate keystore

https://stackoverflow.com/questions/50235957/how-to-configure-spring-boot-2-webflux-to-use-ssl

```bash
openssl req -newkey rsa:2048 -nodes -keyout key.pem -x509 -days 365 -out certificate.pem

keytool -importcert -cacerts -file ./core/src/main/resources/certs/certificate.pem -alias pitanga -storepass $STORE_PASS
```

## Isolate java processes

```bash
java -Djava.security.manager -Djava.security.policy=/path/to/java.policy <java.class>
```

```java.policy
grant {
    // Allow necessary permissions for basic Java operations
    permission java.security.AllPermission;

    // Deny network access
    permission java.net.SocketPermission "*", "connect,listen,accept";

    // Deny file system access
    permission java.io.FilePermission "<<ALL FILES>>", "read";
};
```
