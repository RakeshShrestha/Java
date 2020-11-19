Java >= 1.8.0_252

kotlinapp is a Javalin (MicroFramework Service Oriented Architecture) based rest api server application.

Javalin has plugins for JSON mapping, template rendering, and OpenAPI (Swagger).

As to me it's better than spark framework.

### Enter the kotlinapp

### Build and Run Locally

```bash
gradlew build
java -jar build/libs/kotlinapp-1.0.jar --add-to-start=conscrypt
```
### Clean compiled resources

```bash
gradlew clean
```

### The server is running

https://localhost/

### HTTPS
1. Conscrypt : A Java Security Provider

### Kotlinapp Extra Dependency

1. KOIN : A pragmatic lightweight dependency injection framework for Kotlin developers
2. Jackson : Jackson has been known as "the Java JSON library" or "the best JSON parser for Java"
3. Java-jwt : A Java implementation of JSON Web Token (JWT) - RFC 7519
4. Exposed : An ORM framework for Kotlin
5. HikariCP : HikariCP is a "zero-overhead" lightweight JDBC connection pool
6. MariaDB : MariaDB Database Engine

### Unit Testing
1. Unirest-java : Simplified, lightweight HTTP client library
2. JUnit : Java Unit Test Framework


