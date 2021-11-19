pipeline {
    agent any
    tools {
        maven 'maven'
        dockerTool 'docker'
    }
    stages{
       
        stage('BUILD'){
            when {
                branch 'release/*'
            }
            steps{
                echo "BUILDING THE IMAGE... "
                
                sh "mvn clean package"
                sh "docker build -t intern/springapp:build-${BUILD_ID} ."
                
            }
        }
        stage('TAG'){
            when {
                branch 'release/*'
            }
            steps{
                echo "TAGGING GIT REPO..."
                
                sh """
                git tag build-${BUILD_ID}
                git push origin --tags
                """

            }
        }
        stage('PUSH'){
            when {
                branch 'release/*'
            }
            steps{
                echo "PUSHING THE IMAGE TO REPO ..."
                withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASSWORD', usernameVariable: 'USERNAME')]) {
                    sh """
                    echo ${PASSWORD} | docker login --username ${USERNAME} --password-stdin 
                    docker tag intern/springapp:build-${BUILD_ID} ${USERNAME}/jenkins-maven:build-${BUILD_ID}
                    docker push ${USERNAME}/jenkins-maven:build-${BUILD_ID}
                    """
                }
            }
        }
    }
    post {
        success {
           build job: 'Maven deploy', parameters: [string(name: 'BUILD', value: "$BUILD_ID")]
        }
    }
}