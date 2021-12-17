# Tracing

This project privides tracing service for the vehicles. The service will trace the position of the vehicles. The tracing prevents duplicates within an interval. Further a sceduling batch service is implemented for checking wether the vehcile's position tracing are too long and make sure that it keeps tracing data with no data values. 

## Local Environemnt Setup 
- Download the repo
- Make sure Java 11 SDK is installed
- Open the project in IDE like intelliji Idea
- Build the project
- Run the project
- In-memory H2 datbase is used for persistance

## Api Documentaion
- Available on ``` http://domain:port/swagger-ui```

## Test
Spcok framework is used for tesing the application. 
- You can run the test using the command if gradle is install in the project root
```gw test``` 
- You can run the test using the follwing comand if gradle is not installed from project root
```./gradlew test```


