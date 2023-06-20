1. A simple Spring Boot that tries to access the db will fail if a db is not available.
2. We have a few options for our tests:
    1. Provide a shared db for the team

    * We need network access (VPN? Proxy?). At least it's up (mostly)
    * We can share a schema - easy to maintain but not isolated
    * We can have our own schema - difficult to maintain but isolated

    2. Start up a local db, easiest is a containerized db

    * We can work offline
    * Schema is in git, db is recreated on demand
    * We need to start it before our tests. It's not part of the test lifecycle

## Examples

1. [SimplePostgresTest](src/test/java/com/att/training/ct/basic/SimplePostgresTest.java)
2. [ContainerLifecycleTest](src/test/java/com/att/training/ct/basic/ContainerLifecycleTest.java)
3. [SimpleSpringBootTest](src/test/java/com/att/training/ct/spring/SimpleSpringBootTest.java)
5. [SpringBootTestContainersTest](src/test/java/com/att/training/ct/spring/SpringBootTestContainersTest.java)
6. [PostgresSingleton](src/test/java/com/att/training/ct/spring/PostgresSingleton.java)
7. [PostgresContextInitializer](src/test/java/com/att/training/ct/spring/PostgresContextInitializer.java)