# Tracing

This project privides tracing service for the vehicles. The service will trace the position of the Vehicles. The Tracing prevents duplicates within an interval. Further a sceduling batch service is implemnted for checking wether the vehciles position tracing are too long and make sure that it keeps tracing data with no data values. 

## Local Environemnt Setup 
- Download the repo
- Make sure Java 11 SDK is installed
- Open the project in IDE like intelliji Idea
- Build the project
- Run the project

## Api Documentaion
- Available on ``` http://domain:port/swagger-ui```

## Test
Spcok framework is used for tesing the application. 
- You can run the test using the command if gradleadle is install in the project root
```gw test``` 
- You can run the test using the follwing comand if gradle is not installed form project root
```./gradlew test```


