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
  - The `Jenkinsfile` will execute the `BUILD`, and then run the deploy pipeline with `DEV_DEPLOY`, `QA_DEPLOY`(Will ask approval) and `CLEANUP` stages accordingly.

## Script for Deploy Pipeline

```groovy
pipeline{
    agent any
    tools {
        dockerTool 'docker'
    }
    parameters {
            string(name: "BUILD", defaultValue: "1")
    }
    stages{
        stage('DEV_DEPLOY'){
            
            steps{
                echo "Removing previous build ... "

                sh "docker rm -f Springhello-DEV && echo 'Previous build removed' || echo 'No previous build'"

                echo "Deploying new build to DEV..."

                sh "docker run -d -p 3000:8080 --name Springhello-DEV itsammu/jenkins-maven:build-${params.BUILD} "
                echo "App running on : http://localhost:3000"
                
                script{
                       env.RELEASE = input message: 'Should we promote to QA ?', ok: 'Continue',
                             parameters: [booleanParam(name: 'QA_DEPLOY')] 
                }
                
            }
        }
        
        stage('QA_DEPLOY'){
             when {
                 expression{
                     return env.RELEASE
                 }
            }
       
            steps{
                
                echo "Removing DEV build..."
                sh "docker rm -f Springhello-DEV && echo 'Previous build removed' || echo 'No previous build'"
                
                echo "Removing previous QA build..."
                sh "docker rm -f Springhello-QA && echo 'Previous build removed' || echo 'No previous build'"
                
                echo "Deploying new build to QA..."
                sh "docker run -d -p 3000:8080 --name Springhello-QA itsammu/jenkins-maven:build-${params.BUILD}"
                
                echo "App running on : http://localhost:3000"
            }
        }
        
        stage('CLEANUP'){
             
            steps{
                echo "Cleaning dangling/unused containers and images"

                sh """
                docker image prune -a -f
                docker container prune -f
                """
                
            }
        }
    }
}
```
  
## Testing the app
The app will be running on http://localhost:8080 or in the specified port while using docker.

While using jenkins it runs on http://localhost:3000.
