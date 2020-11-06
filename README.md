# Kotlin-Java

Java11 Version

http server application

Javalin is a true microframework with only two dependencies: the embedded web-server (Jetty) and a logging facade (SLF4J).

Javalin has plugins for JSON mapping, template rendering, and OpenAPI (Swagger), but they’re optional dependencies that you have to add manually.

### Enter the javalintest

### Build and Run Locally

```bash
gradlew build
java -jar build/libs/javalintest-1.0.jar  --add-to-start=ssl,conscrypt
```
### Clean compiled resources

```bash
gradlew clean
```

### The server is running

https://localhost/

https://localhost/static-files-test.html


