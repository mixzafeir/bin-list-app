
# Card Cost API

## Overview

The **Card Cost API** is designed to calculate and manage clearing costs for payment cards based on card-issuing countries. Developed as part of the Etraveli Group hiring process, this API is built in Java with production-grade standards, offering full CRUD operations, caching, role-based access, and more. The API is ready for both development and production environments, with Docker support and MySQL integration.

## Features

- **CRUD Operations**: Full Create, Read, Update, and Delete operations on clearing costs.
- **Clearing Cost Calculation**: Determines the clearing cost based on card-issuing country and utilizes caching to handle API limitations.
- **Authentication & Authorization**: Secure access through JWT-based authentication, with role-based authorization for protected endpoints.
- **Database & Auditing**: MySQL database with initial data setup via Liquibase and auditing using Hibernate Envers.
- **High Availability**: Configured to handle up to 7000 requests per minute with optimized server settings.
- **Docker Support**: Dockerized application with separate configurations for development and production environments.
- **Custom Exception Handling**: A global exception handler is implemented to manage common exceptions throughout the project, ensuring consistent and informative error responses.

## Endpoints

### Authentication
- **Login**
  - **Path**: `/auth/login`
  - **Method**: `POST`
  - **Request Body**:
    ```json
    {
      "username": "string (not blank)",
      "password": "string (not blank)"
    }
    ```
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

- **External API Call**: Calls to external API (https://lookup.binlist.net) are cached in Redis to manage rate limits.
- **Database**: MySQL database with initial data configured via Liquibase. Contains `users`, `clearing_cost` as well as the auditing tables.
- **Profiles**:
  - **Development Profile**: No authentication required; endpoints are publicly accessible.
  - **Production Profile**: JWT authentication enforced; all protected endpoints require a valid JWT with `ROLE_ADMIN`.
- **Testing**:
  - **Unit Tests** and **Integration Tests**: Tests cover CRUD operations and end-to-end flows using an H2 in-memory database.
  - **Caching Test**: External API calls are mocked to avoid rate-limit restrictions during tests.

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
- **Environment-Specific Secrets**: Sensitive information is managed with `.env` files in production.

---
