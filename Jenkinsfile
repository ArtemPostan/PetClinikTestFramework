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
    }
}