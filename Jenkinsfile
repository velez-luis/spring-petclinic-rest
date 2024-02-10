pipeline {
    agent any
    tools{
        maven 'maven_3.8.8'
    }
    stages {
        // stage('Checkout') {
        //     steps {
        //         git branch: 'main', url: 'https://github.com/velez-dev/spring-petclinic-rest'
        //     }
        // }
        stage('Compile') {
            steps {
                sh 'mvn clean compile -B -ntp'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test -B -ntp'
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package -B -ntp'
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
