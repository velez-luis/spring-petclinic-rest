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
        /*
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
        }*/
        stage('Package') {
            steps {
                sh 'mvn clean package -DskipTests -B -ntp'
            }
        }/*
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
        stage('Artifactory') {
            steps {                
                script{
                    // Forma 2 - File Spec
                    sh 'env | sort'

                    def pom = readMavenPom file: 'pom.xml'
                    println pom

                    def server = Artifactory.server 'artifactory'
                    def repository = 'spring-petclinic-rest'
                    if("${GIT_BRANCH}" == 'origin/main'){
                        repository = repository + '-release'
                    } else {
                        repository = repository + '-snapshot'
                    }
                    def uploadSpec = """
                        {
                            "files": [
                                {
                                    "pattern": "target/.*.jar",
                                    "target": "${repository}/${pom.groupId}/${pom.artifactId}/${pom.version}/",
                                    "regexp": "true"
                                }
                            ]
                        }
                    """
                    server.upload spec: uploadSpec
                }
            }
        }
        stage('Nexus') {
            steps {
                script {

                    sh 'env | sort'

                    def pom = readMavenPom file: 'pom.xml'
                    println pom

                    nexusPublisher nexusInstanceId: 'sonatype-nexus',
                    nexusRepositoryId: 'spring-petclinic-rest-release',
                    packages: [[$class: 'MavenPackage',
                    mavenAssetList: [[classifier: '', extension: '', filePath: "target/${pom.artifactId}-${pom.version}.jar"]],
                    mavenCoordinate: [
                    groupId: "${pom.groupId}",
                    artifactId: "${pom.artifactId}",
                    packaging: 'jar',
                    version: "${pom.version}-${BUILD_NUMBER}"]]]

                }
            }
        }*/
        stage('DockerHub') {
            steps {
                script {
                    def pom = readMavenPom file: 'pom.xml'
                    def app = docker.build("velezluis/${pom.artifactId}:${pom.version}")

                    docker.withRegistry('https://registry.hub.docker.com/', 'dockerhub-credentials	'){
                        app.push()
                        app.push('latest')
                    }
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
