# Prosource - Technical Exam

### Dependencies
- JDK 11
- Maven 3.6+

### Tools for code review and testing
- Intellij (optional but preferable) (https://www.jetbrains.com/idea/download/#section=mac)
- Postman


## Description
This is a simple demonstration of abstraction displaying an email sending functionality (mocked) using different email API providers:
- Mailgun
- Sendgrid


## How to run / test
Once you've pulled the code and have set up the dependencies in your IDE, run the following commands:
- mvn clean install
- mvn spring-boot:run


## Available endpoints
When application has started, you can send requests via Postman. 

#### Send to specific provider 
http://localhost:8080/email/notification?provider=sendgrid

#### Send to any
http://localhost:8080/email/notification/send



## Notes 
- Response will return a static JSON body indicating email was sent successfully.
- Since actual integration with the API services (Mailgun & Sendgrid) is not doable for this exam, to test failing scenarios, each available email service must return a null back to the service layer.
- HttpClient is set up but only mocked, that is, called from the service layer but returns a default integer or 200.


## TODO
- Authentication
- Actual integration of the API services with their corresponding API Keys
- End-to-end testing
- Logging
