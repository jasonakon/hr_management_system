# HR Management system (NPHC)
This is a custom ERP HR application made for project interview using Spring Boot.

## Introduction:
This project was initially made for interviewing purposes as an assignment requested by the interviewer. The assignment consists of several parts modules and features to be implemented such as csv file upload, database table realisation, CRUD application and others. Spring Boot was chosen for this project for development with maven.

## How to run ?
### Pre-requisite:
1. Making sure any VPN is turned off.
2. Clone the project using GIT command:
```bash
git clone https://github.com/jasonakon/hr_management_system.git hr_management_system_jason_lim_git
```

### Method 1 : Using Intellij IDE *(Easiest)*
1. Import the project directly using IntelliJ and wait for the IDE to index all the required files and dependencies.
2. Head to *SalaryManagementApplication* and *right-click* and choose *Run 'SalaryManageme...main()'*
3. Wait for the application to run and you shall see the last log showing : 
```
Started SalaryManagementApplication in 5.562 seconds (JVM running for 6.088)
```
4. The application will be hosted at http://localhost:8080/

### Method 2 : Using Docker
1. Making sure docker is installed : https://docs.docker.com/get-docker/
2. Download the docker binary image : https://1drv.ms/u/s!AoK0vXAOFQ_CkGrI1iVNpN1nO2l-?e=e758Co
3. Run the command to load the binary image:
```bash
docker load -i hr_management_system_docker.tar
```
4. View the loaded docker binary image:
```bash
docker images
```
6. Run the docker binary application:
```bash
docker run -p 8080:8080 hr_management
```
7. The application will be hosted at http://localhost:8080/

### Method 3 : Using Maven
1. Making sure maven is installed : https://maven.apache.org/install.html
2. Navigate to your repository
```bash
cd hr_management_system_jason_lim_git
```
3. Build the application using maven
```bash
mvn clean package
```
4. Run the application using java
```bash
java -jar target/hr-0.0.1-SNAPSHOT.jar
```
5. The application will be hosted at http://localhost:8080/
<br/>

## Connecting Database (H2)
This project uses embedded H2 database from Spring, below will show how to connect to the database:
1. Make sure your application is running.
2. Head to this url if you hosted in localhost : http://localhost:8080/h2-console
3. Fill up the configuration of H2:
```bash
   - Save Setting : Generic H2 (Embedded)
   - Setting Name : Generic H2 (Embedded)
   - Driver Class : org.h2.Driver
   - JDBC URL     : jdbc:h2:mem:hr_management_db
   - User Name    : admin
   - Password     : admin
```
4. Click connect and you're set !

## Testing
### CSV File
Referring to the folder path *src/main/resources/csv* contains a list of csv test files which used for requirement validation. A README.txt file is attached as well.
### Postman
I been using postman for testing the application including file uploads and all the CRUD functionality.
<br/>
You can find a collection of requests i used for testing here : https://www.getpostman.com/collections/9d77113fd0fea581be74
### Unit Test
A list of unit test has been written to test each of the service functionality including controller as well.

## Future improvement
1. Broader test cases for the csv input file
2. More option for the PUT request whereby record can be directly changed by only specifying individual field value
3. More filtering / sorting feature for the GET request

These improvement may be optional for the assignment requirement but it is manageable to be implemented with given more time. However, this application alone handled numberous edge exceptions from users invalid inputs to processing.

## Contact
Please feel free to contact me if you encountered any issue:
<br/>
Email : jasonakon@gmail.com
<br/>
OR drop an issue in the repostiory. Thank you!
