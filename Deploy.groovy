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
                sh "docker rm -f Springhello-QA && echo 'Previous build removed' || echo 'No previous build'"

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