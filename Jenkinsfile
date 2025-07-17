pipeline {
    agent any

    environment {
        IMAGE_NAME = "taeyeon0319/ureverse-be:kty"
    }

    stages {
        stage('Clone') {
            steps {
                git branch: 'dev', url: 'https://github.com/UReverse-EarthGuard/UReverse-BE'
            }
        }

        stage('Build Jar') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build -x test'

                // .jar 추출 후 app.jar로 복사
                script {
                    JAR_NAME = sh(script: "ls build/libs/*.jar | head -n 1", returnStdout: true).trim()
                    sh "cp $JAR_NAME app.jar"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${IMAGE_NAME}")
                }
            }
        }

        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    sh """
                        echo "$PASSWORD" | docker login -u "$USERNAME" --password-stdin
                        docker push ${IMAGE_NAME}
                    """
                }
            }
        }
    }
}

