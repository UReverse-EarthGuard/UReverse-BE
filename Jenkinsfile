pipeline {
    agent { label 'java17-docker-agent' }

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
                bat 'gradlew.bat clean build -x test'

                script {
                    def jarName = bat(script: 'for /r build\\libs %%f in (*.jar) do @echo %%f & goto :done\n:done', returnStdout: true).trim()
                    bat "copy ${jarName} app.jar"
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                bat "docker build -t ${IMAGE_NAME} ."
            }
        }

        stage('Push to DockerHub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                    bat """
                        echo %PASSWORD% | docker login -u %USERNAME% --password-stdin
                        docker push ${IMAGE_NAME}
                    """
                }
            }
        }
    }
}
