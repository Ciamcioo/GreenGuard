# Green Guard Server

Welcome to Green Guard Server repository!

## What is Green Guard Server?

Green Guard Server is a RESTful Web Service that is a module of the Green Guard project. The aim of the module is to provide functionality for creating, adding, updating and removing the sensors in different locations and track all sensors readings provided  about current status of your plants.  The service's goal is to provide you with the necessary logic encapsulated in operations for managing the sensors behavior. Beside the sensor logic, GG web service provides the authentication using JWT token, so each user can keep track of their own plants. The most important thing it's up to you how you would like to interact with it thanks to Open API. You can use it as a stand-alone application, plug it into your application or use dedicated frontend module to get ready-to-use application. The choice is up to you, no matter which option you  choose, the result is the same. You are going to get services which will help take care of your plants. Your green friends will thank you for that!


---
## Table of contents

1. [What is Green Guard Server?](#what-is-green-guard-server)
2. [Features](#features)
3. [Used Technologies](#used-technologies)
4. [Project Status](#project-status)
5. [Requirements](#requirements)
6. [Installation](#installation-)
7. [API Usage](#api-usage)
    - [Example of Request](#examples-of-requests)
    - [Example of Response](#examples-of-responses)
8. [Contribution](#contribution)
9. [Licences](#licences)

---

## Features

-  Manage sensors (create, update, delete, read)
-  Sensor locations management
-  User profiles and authentication with JWT tokens
-  Track real-time sensor data
-  Swagger-based API documentation
-  Easy deployment via Docker
-  Profile-based configuration (default/dev/test)

## Used Technologies

Service uses many different technologies to achieve its goal. You can get familiar with them by looking at the list below:
- Java 21
- Spring Boot version 3.4.2
  - Spring Web
  - Spring Security
  - Spring Data
  - Spring Doc
  - Spring Validation
  - Spring Test
- Docker
- PostgreSQL
- H2 (in-memory database)
- Maven
- JWT
- Map Struct
- Mockito
- Slf4j Logger
- Swagger UI
- Git

---
## Project Status

Status: In development

---
## Requirements

To get started working with the project, you only need Java version 21 installed on your machine. You can get it from the [Oracle site](https://www.oracle.com/pl/java/technologies/downloads/#java21). In case you are a Linux user you can get it using your favorite package manager. 

Additionally, you might want to use more production ready database which is PostgreSQL. In such a case you need to install docker on your machine and perform additional set up of the database based on the docker compose file you can find in the docker package.

## Installation 
1. Get the project repository
    - using Git
   ``` shell
        git clone https://github.com/Ciamcioo/GreenGuard.git
   ```
    - using zip archive
   ``` shell
        cd <path/to/your/development/directory>
        curl -L -o GreenGuard.zip https://github.com/Ciamcioo/GreenGuard/archive/refs/heads/main.zip        
        unzip  GreenGuard.zip -d  GreenGuardServer
        cd GreenGuardServer/GreenGuard-main 
   ```
2. Build the green guard project
    - using Maven installed locally
   ``` shell
      mvn clean install
      mvn compile 
   ```

    - using Maven Wrapper included in the project 
      - UNIX users
      ``` shell
        chmod +x mvnw
        ./mvnw clean install
        ./mvnw compile   
      ```
      - Windows users
      ``` shell
        ./mvnw clean install
        ./mvnw compile   
      ```
      
   > **_NOTE:_** Maven wrapper should be used if you don't have maven installed on your machine

3. Docker setup
   > **_NOTE:_** You can safely skip this step if you aren't going to use the default profile with PostgreSQL database  

   1. Make sure that docker and docker-compose is installed on your machine
   2. Go to the docker directory located in the project 
      ```shell
      cd docker
      ```
   3. Run the docker container  
      ```shell
      docker-compose up -d
      ```
      As a result you should see the information that the container of the name production-postgres-db has been created. At this point you container should be already running in detached mode. It's worth to remember that you need to explicitly turn off the container when you finish working with the application, because even after the turn off your machine the container is going to be restarted.
   
   4. Closing the docker container
      ```shell
      docker-compose down 
      ``` 
      This command also needs to be executed from the docker directory. In other case we are going to get the error.
   5. Removing the docker volumen
      ```shell
      docker volumen rm production-postgres-db 
      ``` 
      This command is going to remove the volume with the data associated with the docker container.
   
4. Run the project 
   1. Default profile  
       - using Maven installed locally
      ``` shell
       mvn spring-boot:run
      ```

       - using Maven Wrapper included in the project

      ``` shell
       ./mvnw spring-boot:run
      ```
   2. Development profile 
      - using Maven installed locally
      ```shell
         mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=development"
      ```
   3. Test profile
      - using Maven installed locally
      ```shell
         mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=test"
      ```
       > **_NOTE:_** The only use case of the test profile is for running tests 

   The configuration of every listed profile can be found in the relative path of src/main/resources/ looking from the perspective of a root.

   > **_NOTE:_** Development and test profiles can also be run using maven wrapper, for more details on both profiles

5. Get to the API documentation page to get to know the project API
   Open your favorite browser and go to the documentation site which is located under <http://localhost:9091/swagger-ui/index.html>

After successfully completing this setup you can freely explore the environment of green guard server service. Application by default runs on port 9090 (default profile) and 9091 (development and test profile) . Application's database require additional setup before using it, if you decide to use the default profile, the docker container holding the database needs to be running. For the versions running on the port 9091 setup isn't needed.

---
## API usage

Communication with Green Guard API undergoes via HTTP requests. Service supports four of the most popular HTTP methods which are: GET, POST, PUT, and DELETE. Every resource is uniquely identified by the URL which is an endpoint for different kinds of operations. Requests and responses differ from each other due to the requirements of the operation served on a specific endpoint. Here I can advise reaching out to the [documentation](http://localhost:9091/swagger-ui/index.html) of the API in Swagger-UI to get all the necessary information. API does not need authentication for usage.
The requests to an API might be sent using different tools like Postman, browser, or regular bash using the curl command. In the examples below I will use the curl command to present you client request and service responses.

### Examples of requests

Here I'm providing you with three different examples of API requests with varying methods of HTTP. The method's requirements are presented below in the table.

| Method | Endpoint             | Required Params | Request Body (fields)           | Response Codes                                                | Response Body             |
|--------|----------------------|-----------------|---------------------------------|---------------------------------------------------------------|---------------------------|
| GET    | `/api/sensor/{name}` | Sensor name     | None                            | 200 Ok, 404 Not Found, 401 Unauthorized, 400 Bad request      | Single sensor             |
| POST   | `/api/sensor`        | None            | `name`, `ipAddress`, `isActive` | 201 Created, 400 Bad Request, 404 Not Found, 401 Unauthorized | Created Sensor IP Address |

1. GET
   ``` shell
      curl -X 'GET' \
      'http://localhost:9090/api/sensor/sensor_999' \
      -H 'accept: */*' 
   ``` 
   
2. POST
    ``` shell
      curl -X 'POST' \
      'http://localhost:9090/api/sensor' \
      -H 'accept: */*' \
      -H 'Content-Type: application/json' \
      -d '{
      "name": "sensor_999",
      "ipAddress": "192.168.10.253",
      "active": false
      }'
   ```
   
### Examples of responses

Request shown above was answered by the service to client with a response. The response also differ depending on the received request. For example, they have different status codes and messages.

1. GET Response
   ``` json
   {
   "name": "sensor_999",
   "ipAddress": "192.168.10.253",
   "active": false
   } 
   ```
2. POST Response
   ```json
      "192.168.10.253"
   ```
   
More examples of both - the calls and responses can be found in the Swagger UI, where you can test all the endpoints available for use.

---
## Contribution

Pull requests and contributions to the project are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Authors

Created and maintained by:

- **Jakub Maciocha**   
  - [GitHub: @Ciamcioo](https://github.com/Ciamcioo)
  - Email: jakub.maciocha@gmail.com
  - LinkedIn: [linkedin.com/jakub-maciocha](https://www.linkedin.com/in/jakub-maciocha-bb7009282/)
- **Maciej Pacholczyk**
  - [GitHub: @Husxk](https://github.com/husxk)


 Feel free to reach out for collaboration, feedback, or questions!


---
## Licences
[Apache 2.0](https://choosealicense.com/licenses/apache-2.0)




