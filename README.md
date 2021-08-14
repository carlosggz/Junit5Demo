# JUnit5 scenarios

Application to illustrate several scenarios to test using Spring Boot and JUnit5.

Current scenarios:
- Test containers
- Mail
- Messaging (queues)
- FTP
- Redis
- Controllers
- Database
- Async methods
- Schedule methods

## Instructions

The application need some servers to work, all of them are specified on the docker compose file. To execute the application using docker, just run the docker compose at the src folder of the project:

    docker-compose up --build

To verify the functionality, you can check the logs and the servers at:
- postgres database at port 5444
- mail server at http://localhost:1080
- rabbitmq at http://localhost:15672
- redis at 16379
- the rest controller at http://localhost:8080/jokes

