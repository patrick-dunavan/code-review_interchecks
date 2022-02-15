# Interchecks Code Review Project 
First off, Congratulations for making it to the next step of
the Interchecks hiring process 🎉 

This exercise is really just a way for us to facilitate a conversation
around best practices, performance, design patterns, formatting etc. 
This might also be our first time seeing your actual code.

For you this a chance to demonstrate your abilities in an 
environment that is more true to how you would normally work: at home, 
with full access to Google, Stack Overflow etc. 

# The Mission
This is a standard Spring Boot project. 
The application will run, however, we have intentionally introduced some mistakes. 
Your goal is to tell us what we need to change in order to 
improve it. Keep an eye out for the following:
- Performance Issues
- Code Style inconsistencies
- Runtime Exceptions
- API design problems
- Java / Spring best practices 

You can track and communicate your changes by 
[submitting a pull request](https://betterprogramming.pub/10-step-guide-to-github-contributions-9aeeb38493a8).
The basic steps are:

1) Fork the main repository
2) Clone the forked repository to your machine
3) Create a feature branch
4) Commit your changes to the feature branch
5) Push the feature branch to your fork
6) Raise a Pull Request against the main repository

# Application requirements
You will need Java 8+ (feel free to update the version) and 
Apache Maven to run this application. In addition to this,
you will need a git client and a GitHub account. 

# Helpful Stuff

#### Run the application
```
mvn spring-boot:run
```
(or use your favorite IDE)

#### Create a Person
``` BASH
curl -v "localhost:8080/person/create?firstName=test&lastName=test"    \
     -H 'Content-Type: application/json'
```

#### Get a Person
``` BASH
curl -v localhost:8080/person/1    \
     -H 'Content-Type: application/json'
```

#### Get All People
``` BASH
curl -v localhost:8080/person    \
     -H 'Content-Type: application/json'
```

#### Get Metrics
``` BASH
curl -v localhost:8080/person/metrics    \
     -H 'Content-Type: application/json'
```
