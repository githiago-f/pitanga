

## Generate keystore

https://stackoverflow.com/questions/50235957/how-to-configure-spring-boot-2-webflux-to-use-ssl

```bash
keytool -genkeypair -keyalg RSA -keystore src/main/resources/certs/keystore.jks

openssl req  -nodes -new -x509  -keyout server.key -out server.cert

openssl rsa -pubout -in private.pem -out public.pem
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
