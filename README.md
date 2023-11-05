# Diffrenz-Assignment

## 1. Getting Started
### Prerequisites
```
-	Java JDK 17
-	Maven
-	MySQL database
-	IDEA (Eg., IntelliJ IDEA, STS)
```
### Clone this Repository
```
- git clone https://github.com/sufailbadushat/diffrenz-assignment.git
Checkout master branch
- git checkout master
```
## 2. Database Setup
 - Create a new database named **bankdb**. 
 - You can import the following SQL file into the MySQL server to add dummy data.
```
-    database/bankdb.sql
```
## 3. Import Cloned Project Into IDEA
### Run the following Java Spring Boot main file
```
src/main/java/com.assignment.diffrenz/DiffrenzApplication
```
Make sure the application started successfully at **port 8081** without any error.
You can access the application on a web browser using the following URL
```
http://localhost:8081/api
```
### Or use maven commands
```
- mvn clean install
- mvn spring-boot:run
```

## 3. Testing
### Unit Testing
Run the test files in the following path:
```
- src/test/java/com.assignment.diffrenz/*
```
### Api Testing
Use tools like Postman to manually test API endpoints.

## 4. Code Coverage
### Jacoco
- Build project
```
- mvn clean install
```
### Open the html file on a web browser
```
- target/site/jacoco/index.html 
```
you can see code coverage report on there.
## 5. Available Endpoints
- **GET** /api: Returns Home page
- **GET** /api/admin/{id}: Returns data based on ID
- **POST** /api/admin/between-dates: Returns based on ID and Between Dates - Admin can specify either ID or Full fields (id, fromDate, toDate).
- **POST** /api/admin/between-amounts: Returns based on ID and Between Amounts - Admin can specify either ID or Full fields (id, fromAmount, toAmount).
- **POST** /api/user/get-data: Returns three months back data, if the user specifies any parameter it will throw unauthorized.
