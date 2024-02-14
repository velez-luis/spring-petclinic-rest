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
            post{
                success{
                    jacoco()
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }
        stage('Package') {
            steps {
                sh 'mvn package -DskipTests -B -ntp'
            }
        }
        stage('Sonarqube') {
            steps {
                withSonarQubeEnv('SonarQubeInstance'){
                    sh 'mvn sonar:sonar -B -ntp'
                }
                
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit:'HOURS'){
                    waitForQualityGate abortPipeline: true
                }
                
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
