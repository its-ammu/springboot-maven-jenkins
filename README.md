# Springboot Maven Simple app deployed in Jenkins

Java / Maven / Spring Boot Hello world application deployed into CI/CD Using Jenkins Multibranch pipeline.

## Installation
- To run locally :
  - Clone the repo : `git clone https://github.com/its-ammu/springboot-maven-jenkins.git`
  - Build using `mvn clean package`
  - Go to the targets folder : `cd targets`
  - Run the jar file using `java -jar <jarfilename>.jar`
- To run using Docker :
  - Clone the repo : `git clone https://github.com/its-ammu/springboot-maven-jenkins.git`
  - Build the docker file using `docker build -t demo/springapp .`
  - Run the docker file using `docker run -d -p 8080:8080 --name Springhello intern/springapp`
- To run using Jenkins:
  - Configure the current repo in jenkins multi-branch pipeline accordingly
  - The `Jenkinsfile` will execute the `BUILD`, `DEV_DEPLOY`, `QA_DEPLOY`(Will ask approval) and `CLEANUP` stages accordingly.
  
## Testing the app
The app will be running on : http://localhost:3000 
