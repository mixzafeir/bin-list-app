
# Card Cost API

## Overview

The **Card Cost API** is designed to calculate and manage clearing costs for payment cards based on card-issuing countries. Developed as part of the Etraveli Group hiring process, this API is built in Java with production-grade standards, offering full CRUD operations, caching, role-based access, and more. The API is ready for both development and production environments, with Docker support and MySQL integration.

## Features

- **CRUD Operations**: Full Create, Read, Update, and Delete operations on clearing costs.
- **Clearing Cost Calculation**: Determines the clearing cost based on the card-issuing country, utilizing an external API (https://www.handyapi.com/bin-list) to retrieve necessary card information. Caching is implemented to handle API limitations effectively.
- **Authentication & Authorization**: Secure access through JWT-based authentication, with role-based authorization for protected endpoints.
- **Database & Auditing**: MySQL database with initial data setup via Liquibase and auditing using Hibernate Envers.
- **High Availability**: Configured to handle up to 7000 requests per minute with optimized server settings.
- **Docker Support**: Dockerized application with separate configurations for development and production environments.
- **Custom Exception Handling**: A global exception handler is implemented to manage common exceptions throughout the project, ensuring consistent and informative error responses.

### External API

- **Clearing Cost Calculation**: Determines the clearing cost based on the card-issuing country, utilizing an external API (https://data.handyapi.com/bin) to retrieve necessary card information. Caching is implemented to handle API limitations effectively.

## Endpoints

## Authentication

### Login
- **Path**: `/auth/login`
- **Method**: `POST`
- **Request Body**:
  ```json
  {
    "username": "demo",
    "password": "test"
  }
  ```
  **Note**: The database is initialized with these default credentials. Ensure that the login request uses this `username` and `password` combination to authenticate successfully.
  - **Response**:
    ```json
    {
      "username": "string",
      "role": "ROLE_ADMIN",
      "jwt": "string (JWT token)"
    }
    ```
  - **Description**: Returns a JWT token that must be used as a Bearer token to access protected endpoints.

### Clearing Cost Management
- **Create Clearing Cost**
  - **Path**: `/api/clearing-cost/save`
  - **Method**: `POST`
  - **Request Body**:
    ```json
    {
      "cardIssuingCountry": "string (2 characters, e.g., 'US')",
      "clearingCost": "decimal (up to 15 integer digits, 3 decimal places)"
    }
    ```
  - **Description**: Adds a new entry to the clearing cost table.

- **Update Clearing Cost**
  - **Path**: `/api/clearing-cost/update/{id}`
  - **Method**: `PUT`
  - **Request Body**:
    ```json
    {
      "id": "UUID (e.g., 'd290f1ee-6c54-4b01-90e6-d701748f0851')",
      "cardIssuingCountry": "string (2 characters, e.g., 'US')",
      "clearingCost": "decimal (up to 15 integer digits, 3 decimal places)"
    }
    ```
  - **Description**: Updates an existing entry in the clearing cost table.

- **Retrieve All Clearing Costs**
  - **Path**: `/api/clearing-cost/get-all`
  - **Method**: `GET`
  - **Description**: Retrieves all entries in the clearing cost table.

- **Retrieve Clearing Cost by ID**
  - **Path**: `/api/clearing-cost/id/{id}`
  - **Method**: `GET`
  - **Description**: Retrieves a clearing cost entry by its ID.

- **Delete Clearing Cost**
  - **Path**: `/api/clearing-cost/delete/{id}`
  - **Method**: `DELETE`
  - **Description**: Deletes a clearing cost entry by its ID.

### Clearing Cost Calculation
- **Calculate Clearing Cost**
  - **Path**: `/api/clearing-cost/payment-cards-cost`
  - **Method**: `POST`
  - **Request Body**:
    ```json
    {
      "card_number": "string (8 to 19 digits, representing the card number)"
    }
    ```
  - **Response**:
    ```json
    {
      "country": "string (ISO 2-character country code)",
      "cost": "decimal"
    }
    ```
  - **Description**: Returns the clearing cost for a given card number using an external API. Uses Redis caching to avoid frequent external API calls for repeated requests.

## Technical Details

- **External API Call**: Calls to external API (https://data.handyapi.com/bin) are cached in Redis to manage rate limits.
- **Database**: MySQL database with initial data configured via Liquibase. Contains `users`, `clearing_cost` as well as the auditing tables.
- **Logging Mechanism**: A robust logging mechanism is implemented using a custom `logback-spring.xml` configuration.
  This setup enables structured and customizable logging across different environments, ensuring enhanced debugging and monitoring capabilities.
- **Profiles**:
  - **Development Profile**: No authentication required; endpoints are publicly accessible.
  - **Production Profile**: JWT authentication enforced; all protected endpoints require a valid JWT with `ROLE_ADMIN`.
- **Testing**:
  - **Unit Tests** and **Integration Tests**: Tests cover CRUD operations and end-to-end flows using an H2 in-memory database.
  - **Caching Test**: External API calls are mocked to avoid rate-limit restrictions during tests.

### External API Changes

During development, the application was initially integrated with the [Binlist API](https://binlist.net/) as the external data source for card information. However, midway through development, the Binlist API began returning 403 Forbidden errors due to Cloudflare marking it as a potential phishing site. This issue forced a switch to an alternative API: https://data.handyapi.com/bin.

While this required some adjustments, the core functionality remains the same. All calls to the **Card Cost API** continue to use the original DTO structure and endpoint setup, ensuring compatibility with the initial specifications. Users should be aware that, although the interface remains consistent, requests are now routed to the HandyAPI endpoint instead.


## Installation and Setup

### Running the Application

1. **Clone the Repository**

   ```bash
   git clone https://github.com/mixzafeir/bin-list-app.git
   cd bin-list-app
   ```

2. **Docker Setup**

   #### Development Environment

   ```bash
   docker-compose -f docker-compose.dev.yml up
   ```

   #### Production Environment

   ```bash
   docker-compose --env-file .env.prod -f docker-compose.prod.yml up
   ```

3. **Accessing the API**

- **Development**: Access the API directly without authentication.
- **Production**: Use the login endpoint to obtain a JWT, then use it as a Bearer token in the Authorization header for accessing protected endpoints.

### Environment Variables

- **Development**: Hardcoded environment variables are set within `docker-compose.dev.yml`.
- **Production**: Set environment variables in `.env.prod` file for production deployment.

## Security Considerations

- **JWT Authentication**: Secure endpoints with role-based access control.
- **Environment-Specific Secrets**: Sensitive information is managed with `.env` file in production.

## Future Improvements

1. **Async Calls for Enhanced Scalability**: Introduce asynchronous calls within the Card Cost API to decouple processing from the main application thread. This adjustment allows independent scaling of the main thread and the asynchronous thread pool, optimizing resource utilization and maintaining responsiveness under high load conditions.

2. **Reactive Logic with WebClient**: Migrate to reactive programming using Spring's WebClient to enable natively asynchronous, non-blocking calls to external services. This change would improve response times and facilitate better scaling for high-concurrency use cases.

3. **Horizontal Scaling and Distributed Caching**:
- For horizontal scalability to support the anticipated load, multiple instances of the application should be deployed. This setup would distribute the load effectively, enhancing resilience and performance under heavy traffic.
- **Global Redis Cache**: Implement a global Redis instance layered on top of MySQL to centralize caching across all instances.

---
