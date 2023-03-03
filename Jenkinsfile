pipeline {
    agent {
        node {
            label 'docker-agent-alpine'
        }
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn -B -DskipTests clean package'
            }
        }
    }
}

