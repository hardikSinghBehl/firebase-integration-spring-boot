## Firestore database with Java Spring-boot
### A Small Movie-management-system
#### The application exposes REST API endpoints to perform CRUD operations on the below mentioned documents
#### [Sample Running Application](https://sunspot-firebase-spring.herokuapp.com/sunspot/swagger-ui.html)
---
## Documents and corresponding fields in the application
* **directors**
  * firstName
  * lastName
  * country
* **movies**
  * name
  * durationInMinutes
* **director_movie_mappings** (to establish many-to-many relationship between the above two documents)
  * directorId
  * movieId
---
### Firebase dependecny used in the project
```
<dependency>
	<groupId>com.google.firebase</groupId>
	<artifactId>firebase-admin</artifactId>
	<version>7.2.0</version>
</dependency>
```
---
## Setup and Requirements
* Java 16 and Maven are required. Recommended to download it from [sdkman](https://sdkman.io)
* Create a [new firebase project](https://firebase.google.com)
* Create a new firestore database in test mode
* Go to Project Settings -> Service Account -> Generate new private key and paste the contents of the downloaded .json file in sunspot-private-key.json present in the applications root directory.
* Run the application as spring-boot application in the IDE or execute any of the below two commands in the root directory
```
mvn spring-boot:run
```
```
java -jar target/firebase-spring-boot-crud-0.0.1-SNAPSHOT.jar
```
* The port and base-uri are configured as 9090 and /sunspot respectively, the swagger-ui can be accessed at the below URI
```
http://localhost:9090/sunspot/swagger-ui.html
```
