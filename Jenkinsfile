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
        stage('Artifactory') {
            steps {                
                script{
                    // Forma 1 - Artifactory
                    sh 'printenv'
                    env.MAVEN_HOME = '/usr/share/maven'

                    def releases = 'spring-petclinic-rest-release'
                    def snapshots = 'spring-petclinic-rest-snapshot'
                    def server = Artifactory.server 'artifactory'
                    def rtMaven = Artifactory.newMavenBuild()
                    rtMaven.deployer server: server, releaseRepo: releases, snapshotRepo: snapshots
                    def buildInfo = rtMaven.run pom: 'pom.xml', goals: 'clean package -B -ntp -DskipTests'

                    server.publishBuildInfo buildInfo
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
