A simple Spring Boot app that tries to access the db will fail if a db is not available.
We have a few options for our tests:

1. Provide a shared db for the team
   * We need network access (VPN? Proxy?). At least it's up (mostly)
   * We can share a schema - easy to maintain but not isolated
   * We can have our own schema - difficult to maintain but isolated
2. Start up a local db, easiest is a containerized db
   * We can work offline
   * Schema is in git, db is recreated on demand
   * We need to start and configure the container and our app before our tests. It's not part of the test lifecycle
   * The previous bullet point is where testcontainers comes in

## Examples

1. [SimplePostgresTest](src/test/java/com/att/training/ct/database/basic/SimplePostgresTest.java)
2. [JdbcUrlContainerTest](src/test/java/com/att/training/ct/database/basic/JdbcUrlContainerTest.java)
3. [ContainerLifecycleTest](src/test/java/com/att/training/ct/database/basic/ContainerLifecycleTest.java)
4. [SimpleSpringBootTest](src/test/java/com/att/training/ct/database/spring/SimpleSpringBootTest.java)
5. [SpringBootTestContainersTest](src/test/java/com/att/training/ct/database/spring/SpringBootTestContainersTest.java)
6. [TestContainersSingletonTest](src/test/java/com/att/training/ct/database/spring/TestContainersSingletonTest.java)
7. [PostgresSingleton](src/test/java/com/att/training/ct/database/spring/PostgresSingleton.java)
8. [PostgresContextInitializer](src/test/java/com/att/training/ct/database/spring/PostgresContextInitializer.java)
9. [FlywayTest](src/test/java/com/att/training/ct/database/spring/FlywayTest.java)
10. [AnotherPostgresTest](src/test/java/com/att/training/ct/database/advanced/AnotherPostgresTest.java)
11. [Adding Containers to the Spring Context](src/test/java/com/att/training/ct/database/spring/beans/DbTest.java)
