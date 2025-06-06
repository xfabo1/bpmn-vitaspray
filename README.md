# VitaSpray Application

## How to start the service
### Prerequisites:
- JDK
- Maven

### Running the application
1. Use Maven command: `mvn spring-boot:run`

## REST API Endpoints

The application exposes the following REST API endpoints:

### Payment Management
- **Cancel Payment**
    - `POST http://localhost:8080/api/vitaspray/cancel_payment`
    - Cancels an ongoing payment process by sending a message to the Camunda cluster
    - No request body required

- **Retry Payment**
    - `POST http://localhost:8080/api/vitaspray/retry_payment`
    - Retries the payment process by sending a message to the Camunda cluster
    - No request body required

- **Process Payment Simulation**
    - `GET http://localhost:8080/api/vitaspray/process_payment`
    - Simulates the external payment processing service
    - Optional query parameter: `fail=true|false` to simulate payment failure
 
### Email Management (Camel Integration)
- **Send Email**
    - `POST http://localhost:8080/camel/send-email`
    - Sends an email using Apache Camel's SMTP integration
    - Required headers:
        - `to`: recipient email address
        - `subject`: email subject
        - `from`: sender email address
    - Request body should contain the plain text of the email message

You can use tools such as Postman or Insomnia to test these endpoints.

## Application Architecture
This application is built with Spring Boot and integrates with Camunda for workflow management.

It also uses Apache Camel to handle external integrations — in particular, sending emails through an SMTP server based on workflow data. This allows Camunda tasks to dynamically trigger email notifications via HTTP requests to Camel-managed routes.
