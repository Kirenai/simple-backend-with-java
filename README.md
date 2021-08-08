# REST API WITH SPRING FRAMEWORK AND SPRING BOOT

A rest api made with spring following some spring design patterns and my way

## Steps before running the application

Create a Database in your favorite Database Management System(DBMS), 
In my case MySQL, any name for example: **apirestdb**

```sql

CREATE DATABASE IF NOT EXISTS nameofdb
    CHARACTER SET utf8 COLLATE utf8_spanish_ci;

```

In my case I configure it to my needs

It is not necessary to create the tables since the JPA implementation will do it for us,
I will also leave a .sql file in the project if you are interested

## Run aplication with maven

if you don't have maven, you can go to its official website and download it

```mvn

mvn spring-boot:run

```

## Run aplication with JAR

First you have to see how to create a JAR of the project

```shell

java -jar ~/myjar.jar

```


