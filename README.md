# Diffrenz-Assignment

## 1. Getting Started
### Prerequisites
```
-	Java JDK 17
-	IDEA (E.g., IntelliJ IDEA, STS)
-	MySQL database
```
### Clone the Repository
```
- https://github.com/sufailbadushat/diffrenz-assignment.git
Checkout master branch
- git checkout master
```
## 2. Database Setup
 - Create a new database named **bankdb**. 

## 3. Import Cloned Project Into IDEA
### Run the following Java Spring Boot main file
```
src/main/java/com.assignment.diffrenz/DiffrenzApplication
```
Make sure the application started successfully at **port 8081** without any error.
You can access the application on a web browser using URL
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
### Jacaco
- Build project
```
- mvn clean install
```
- OR run the main file -  IDEA contains Maven if you haven't installed mvn on your system.
```
- src/main/java/com.assignment.diffrenz/DiffrenzApplication
```
### Open the html file on a web browser
```
- target/site/jacoco/index.html 
```
you can see code coverage repot on there.
## 5. Available Endpoints
- **GET** /api: Returns Home page
- **GET** /api/admin/{id}: Returns data based on ID
- **POST** /api/admin/between-dates: Returns based on ID and Between Dates - Admin can specify either ID or Full fields (id, fromDate, toDate).
- **POST** /api/admin/between-amounts: Returns based on ID and Between Amounts - Admin can specify either ID or Full fields (id, fromAmount, toAmount).
- **POST** /api/user/get-data: Returns three months back data, if the user specifies any parameter it will throw unauthorized.
