# Military Jets Management API

A RESTful API for managing military jets and their owners. Built with Java, Spark Framework, and Hibernate.

## Features

- User management (CRUD operations)
- Military jets catalog with detailed specifications
- Jet ownership tracking
- RESTful endpoints
- PostgreSQL persistence

## Prerequisites

- Java JDK 20 or higher
- PostgreSQL 12 or higher
- Maven

## Installation

1. Clone the repository

`git clone https://github.com/nnktv28/military-jets-api.git`


2. Configure PostgreSQL database in `src/main/resources/hibernate.cfg.xml`

3. Create database and user:
```sql
CREATE DATABASE usuario_db;
CREATE USER nikita WITH PASSWORD '123456';
GRANT ALL PRIVILEGES ON DATABASE usuario_db TO nikita;
```

4. Build the project:

`mvn clean install`


5. Run the application:

`mvn exec:java -Dexec.mainClass="org.example.Main"`


## API Endpoints

### Users
- `GET /api/users` - List all users
- `GET /api/users/{id}` - Get user details
- `POST /api/users` - Create new user
- `PUT /api/users/{id}` - Update user
- `DELETE /api/users/{id}` - Delete user

### Jets
- `GET /api/jets` - List all jets
- `GET /api/jets/{id}` - Get jet details
- `POST /api/jets` - Create new jet
- `PUT /api/jets/{id}` - Update jet
- `DELETE /api/jets/{id}` - Delete jet

### User's Jets
- `GET /api/users/{userId}/jets` - Get all jets owned by user
- `POST /api/users/{userId}/jets` - Add jet to user's collection

## Usage Examples

### Create a User
```bash
curl -X POST http://localhost:8080/api/users \
  -H "Content-Type: application/json" \
  -d '{"name": "John Doe", "email": "john@military.com"}'
```

### Add a Jet
```bash
curl -X POST http://localhost:8080/api/users/1/jets \
  -H "Content-Type: application/json" \
  -d '{
    "model": "F-22 Raptor",
    "manufacturer": "Lockheed Martin",
    "maxSpeed": 2410.0,
    "range": 2960,
    "armament": "M61A2 Vulcan, AIM-120 AMRAAM, AIM-9 Sidewinder"
  }'
```


## Data Models

### User
```json
{
  "id": 1,
  "name": "John Doe",
  "email": "john@military.com",
  "jets": []
}
```

### Jet
```json
{
  "id": 1,
  "model": "F-22 Raptor",
  "manufacturer": "Lockheed Martin",
  "maxSpeed": 2410.0,
  "range": 2960,
  "armament": "M61A2 Vulcan, AIM-120 AMRAAM, AIM-9 Sidewinder",
  "owner": null
}
```

## Technologies Used
- Java 20
- Spark Framework
- Hibernate ORM
- PostgreSQL
- Gson
- Maven

## Author
NNKtv28

GitHub: [@NNKtv28](https://github.com/NNKtv28)

## License
This project is licensed under the MIT License - see the LICENSE file for details