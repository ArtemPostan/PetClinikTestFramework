pipeline {
    agent {
        label 'petclinic-aqa'
    }

    environment {
        BASE_URL = 'http://petclinic:9966/petclinic'
    }

    stages {
        stage('Check Environment') {
            steps {
                sh 'java -version'
                sh 'mvn -version'
            }
        }
        stage('Run API Tests') {
            steps {
                sh 'mvn clean test'
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'
        }
    }
}