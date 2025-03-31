# Green Guard Server

Welcome to Green Guard Server repository!

## What is Green Guard Server?

Green guard server is a RESTful Web Service which is module of Green Guard project. The aim of the module is to provide a functionality for creating, adding, updating and removing the sensors and keep track of all the readings provided by them about current statues of your plants. The service's goal is to provide you with the necessary logic encapsulated in operations for managing the sensors behavior. The most important thing it is up to you how you would like to interact with it thanks to open API. You can use it as a stand-alone application, plug it to your application or use dedicated frontend module to get ready to use application. The choice is up to you, no matter the option which you are going to choose the result is going to be the same. You are going to get services which will help take care of your plants. Your green friends will thank you for that!

## Table of contests

1. [What is Green Guard Server?](#what-is-green-guard-server)
2. [Used Technologies](#used-technologies)
3. [Requirements](#requirements)
4. [Installation](#installation)
5. [API Usage](#api-usage)
    - [Example of Request](#examples-of-requests)
    - [Example of Response](#examples-of-responses)
6. [Contribution](#contribution)
7. [Licences](#licences)
 
[//]: # (6. [Source Code]&#40;#source-code&#41;)

[//]: # (    - [Controller layer]&#40;#controllek-layer&#41;)

[//]: # (    - [Service layer]&#40;#service-layer&#41;)

[//]: # (    - [Data layer]&#40;#data-layer&#41;)
[//]: # (6. [Contribution]&#40;#contribution&#41;)

[//]: # (7. [Licences]&#40;#licences&#41;)


## Used Technologies

Service uses many different technologies to achieve its goal. You can get familiar with them by looking at the list below:
- Java 21
- Spring Boot version 3.4.2
    - Spring Web
    - Spring Data
    - Spring Doc
- Slf4j Logger
- H2 (in-memory database)
- Swagger UI
- Maven
- Git

## Requirements

To get started working with the project, the only thing you are going to need is Java version 21 installed on your machine. You can get it from the [Oracle side](https://www.oracle.com/pl/java/technologies/downloads/#java21). In case you are a Linux user you can get it using your favorite package manager.

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
        unzip  Habit-Builder.zip -d Habit-Builder 
        cd GreenGuardServer/GreenGuard-main 
   ```
2. Build the habit builder project
    - using Maven installed locally
   ``` shell
      mvn clean install
      mvn compile 
   ```

[//]: # (    - using Maven Wrapper included in the project)

[//]: # (   ``` shell)

[//]: # (     ./mvnw clean install)

[//]: # (     ./mvnw compile   )

[//]: # (   ```)

3. Run the project
    - using Maven installed locally
   ``` shell
    mvn spring-boot:run
   ```

[//]: # (    - using Maven Wrapper included in the project)

[//]: # (   ``` shell)

[//]: # (    ./mvnw spring-boot:run)

[//]: # (   ```)
4. Get to the documentation side to get to know the project API
   Open your favorite browser and go to the documentation side which is located under <http://localhost:9090/swagger-ui/index.html>

After successfully completing this setup you can freely explore the environment of green guard server service. Application by default runs on port 9090. Application's database does not require any additional setup before using it.  

## API usage

Communication with Habit Builder API undergoes via HTTP requests. Service supports four of the most popular HTTP methods which are: GET, POST, PUT, and DELETE. Every resource is uniquely identified by the URL which is an endpoint for different kinds of operations. Requests and responses differ from each other due to the requirements of the operation served on a specific endpoint. Here I can advise reaching out to the [documentation](http://localhost:8080/swagger-ui/index.html) of the API in Swagger-UI to get all the necessary information. API does not need authentication for usage.
The requests to an API might be sent using different tools like Postman, browser, or regular bash using the curl command. In the examples below I will use the curl command to present you client request and service responses.

### Examples of requests

Here I'm providing you with three different examples of API requests with varying methods of HTTP. The method's requirements are presented below in the table.

| Method | Endpoint             | Required Params  | Request Body (fields)            | Response Codes               | Response Body            |
|--------|----------------------|------------------|----------------------------------|------------------------------|--------------------------|
| GET    | `/api/sensor/{name}` | Sensor name      | None                             | 200 Ok, 404 Not Found        | Single sensor            |
| POST   | `/api/sensor`        | None             | `name`, `ipAddress`, `isActive`  | 201 Created, 400 Bad Request | Created Habit IP Address |

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
      "macAddress": "3A:5F:BC:92:E1:7D",
      "active": false
      }'
   ```
   
### Examples of responses

For shown above request service answered to client with a response. The response also differs from one another based on the request that they have received. For example, they have different status codes and messages.

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
   
## Contribution

Pull requests and contributions to the project are welcome. For major changes, please open an issue first to discuss what you would like to change.

## Licences
[Apache 2.0](https://choosealicense.com/licenses/apache-2.0)




