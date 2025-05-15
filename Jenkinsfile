pipeline {
    agent any
    tools {
        maven 'maven_3_6_3'
    }

    environment {
        IMAGE_NAME = 'tofazzal/currency-exchange-service'
        DOCKER_IMAGE = "tofazzal/currency-exchange-service:${BUILD_NUMBER}"
        KUBECONFIG_CREDENTIAL_ID = 'kubeconfig-creds'
    }

    stages {
        stage('Checkout Source') {
            steps {
                checkout scmGit(
                    branches: [[name: '*/master']],
                    userRemoteConfigs: [[url: 'https://github.com/TofazzalTopu/currency-exchange-service']]
                )
            }
        }

        stage('Build with Maven') {
            steps {
                sh 'mvn clean install -DskipTests'
            }
        }

        stage('Build Docker Image') {
            steps {
                sh "docker build -t ${DOCKER_IMAGE} ."
            }
        }

        stage('Push Docker Image To Docker Hub') {
            steps {
                script {
                    withCredentials([string(credentialsId: 'my-docker-hub-pwd', variable: 'my-docker-hub-pwd')]) {
                        sh 'docker login -u tofazzal -p ${my-docker-hub-pwd}'
                    }
                    sh "docker push ${DOCKER_IMAGE}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                withCredentials([file(credentialsId: "${KUBECONFIG_CREDENTIAL_ID}", variable: 'KUBECONFIG')]) {
                    sh '''
                        export KUBECONFIG=$KUBECONFIG
                        kubectl set image deployment/currency-exchange-service currency-exchange-service=${DOCKER_IMAGE} --namespace=default
                        kubectl rollout status deployment/currency-exchange-service --namespace=default
                    '''
                }
            }
        }
    }
}
