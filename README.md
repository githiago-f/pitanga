<div style="text-align: center;">
    <img src="./pitangaweb/public/pitanga-icon.svg" alt="Pitanga Icon" style="height: 64px;"/>
    <h3>Pitanga</h3>
</div>

## Sobre

Pitanga é uma ferramenta online destinada a auxiliar o aprendizado de programação, que utiliza conceitos de mobile-learning e através do aprendizado baseado em problemas (PBL) visa ampliar o conhecimento produzido em aula para fora dos limites da sala.

## Install and start server:

```bash
# it may be necessary to set execute permission
./install.sh

./start.sh
```


### 💡: Run ngrok

```bash
ngrok http --domain=distinct-serval-known.ngrok-free.app https://localhost:8443
```

### 💡: Isolate java processes

```bash
java -Djava.security.manager -Djava.security.policy=/path/to/java.policy <java.class>
```
#### 💡: Java policy template:

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
