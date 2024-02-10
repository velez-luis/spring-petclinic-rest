pipeline {
    agent any
    tools{
        maven 'maven_3.8.8'
    }

    stages {
        stage('Checkout') {
            steps {
                git branch: 'main', url: 'https://github.com/velez-dev/spring-petclinic-rest'
            }
        }
        stage('Build') {
            steps {
                sh 'mvn clean package -DSkipTest -B -ntp'
            }
        }
    }
    post{
        always{
            archiveArtifacts artifacts: 'target/*.jar', fingerprint: true, followSymlinks: false
            deleteDir()
        }
    }
}
