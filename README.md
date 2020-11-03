# Kotlin-Java

server application

Javalin is a true microframework with only two dependencies: the embedded web-server (Jetty) and a logging facade (SLF4J).

Javalin has plugins for JSON mapping, template rendering, and OpenAPI (Swagger), but they’re optional dependencies that you have to add manually.

You can exclude the Jetty dependency if you want to run Javalin on a different servlet container.

Enter the javalintest

### Build and Run Locally

```bash
mvn package
java -jar target/javalintest-1.0-jar-with-dependencies.jar
```

### The server is running at port 8080

http://localhost:8080/

http://localhost:8080/static-files-test.html


