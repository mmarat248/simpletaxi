## NOTES

The service is built on the Spring framework to simplify the development process. 
Please note that the service uses virtual threads, and some synchronization aspects have been 
omitted since they are redundant at this stage, as the overall application architecture is not 
yet defined. 
To simplify development, some simplifications have been made, such as the absence of DTOs , connections between events, etc.

As for metrics, I have added a few custom metrics to the project. They are available at the following path:
```http://localhost:8080/metrics```

## How to test

Basic integration tests were also implemented for the project: [test](src/test). 
If you want to test manually, you need to build and run the project through docker:
1. Build docker: ```docker build -t spring-taxi-app -f ./docker/Dockerfile .```
2. Run: ```docker run -p 8080:8080 spring-taxi-app```

Below I will provide some requests in curl format:

User registration:
```
curl --request POST \
--url http://localhost:8080/api/v1/users/register \
--header 'Content-Type: application/json' \
--data '{"username": "someuser", "password": "adH6h2L"}'
```

User authentication:
```
curl --request POST \
--url http://localhost:8080/api/v1/auth/sign-in \
--header 'Content-Type: application/json' \
--data '{"username": "someuser", "password": "adH6h2L"}'
```

Driver registration (auth required):
```
curl --request POST \
--url http://localhost:8080/api/v1/drivers/register \
--header 'Authorization: Bearer ${AUTH_TOKEN}' \
--header 'Content-Type: application/json' \
--data '{}'
```

Driver status update (auth required):
Update the driver's status. In the current implementation, this method 
is not necessary, as the front-end can avoid calling the long-polling events method.
```
curl --request POST \
--url 'http://localhost:8080/api/v1/drivers/944647a7-d487-46f3-9c33-c5d537c70027/status?=' \
--header 'Authorization: Bearer ${AUTH_TOKEN}' \
--header 'Content-Type: application/json' \
--data '{"status": "Available"}'
```

Driver Poll for Ride (auth required):
This method is a classic implementation of long-polling to receive events. 
Ideally, I would create a general method for all event types to avoid using multiple requests.
```
curl --request POST \
--url http://localhost:8080/api/v1/events/rides \
--header 'Authorization: Bearer  ${AUTH_TOKEN}' \
--header 'Content-Type: application/json'
```

Driver Accept Ride (auth required):
After receiving a ride offer, the driver can call the accept method to claim the order.
```
curl --request POST \
--url http://localhost:8080/api/v1/rides/9bb205fa-8394-4cba-ac76-084bcdfdfdce/accept \
--header 'Authorization: Bearer ${AUTH_TOKEN}' \
--header 'Content-Type: application/json'
```