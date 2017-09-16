# Bank Account API

## Technology stack
- Java 8
- Spring Boot
- H2 in-memory database
- Maven as build system
- Swagger 2

## Assumptions

The account number is a number of 8 digits

## Notes

This does not pretent to be production-code like, but it's a simplified version of how it could be a Bank Account API, as there are lot of stuff to add or make differently:

- Security is missing. It's necessary to add security to prevent anyone from hitting the entry points, and an extra security to prevent withdrawals from people that are not owners of the account.
- The entry point to get the statement should receive a range of dates.
- Some unit tests are missing.

## Usage
In order to test this application, checkout the code, and follow these steps:

```java
$ mvn clean package
$ java -jar target/bank-account-api-1.0-SNAPSHOT.jar
```

... go to the browser of your preference and hit: http://localhost:8080

After that you'll see the Swagger documentation where you can play with all entry points :)