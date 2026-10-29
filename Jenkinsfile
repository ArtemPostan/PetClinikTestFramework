pipeline {
    agent {
        label 'petclinic-aqa'
    }

    environment {
        BASE_URL = 'http://petclinic:9966/petclinic'
    }

    parameters {
        choice(
            name: 'TEST_SUITE',
            choices: ['SMOKE', 'REGRESSION'],
            description: 'Выберите набор тестов'
        )
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
                script {
                    if (params.TEST_SUITE == 'SMOKE') {
                        sh 'mvn clean test -Dgroups=smoke'
                    } else {
                        sh 'mvn clean test'
                    }
                }
            }
        }
    }
    post {
        always {
            junit 'target/surefire-reports/*.xml'

            allure(
                  commandline: 'allure2',
                  results: [[path: 'target/allure-results']]
            )
        }
    }
}