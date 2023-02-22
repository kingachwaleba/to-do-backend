## To-Do - Backend App
* [General info](#general-info)
* [Technologies](#technologies)
* [Setup](#setup)
* [Frontend](#frontend)
* [Features](#features)

## General info
Backend application for To-Do app written using Spring Boot. It uses the MySQL database.

## Technologies
Technologies used to create this project:
* Java: 11
* Maven: 4.0.0
* Spring Boot: 2.7.6
* IDE - IntelliJ IDEA Ultimate: 2022.2.3

## Setup
Firstly, change your database configuration in [application.properties](src/main/resources/application.properties) file.
Then, to run backend application use:
```
mvn spring-boot:run
```

## Frontend
Frontend application can be found [here](https://github.com/kingachwaleba/to-do-frontend).

## Features
It is To-Do App performing CRUD operations. A user can:
* register, log in, change password and delete the account,
* add a new task,
* edit existing one task,
* delete a chosen task,
* display all user's tasks,
* sort tasks ascending or descending by date,
* filter tasks by completed flag.
