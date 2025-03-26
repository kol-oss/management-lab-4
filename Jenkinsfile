pipeline {
	agent {
		docker {
			image 'maven:3.8.6-openjdk-17'
            args '-v $HOME/.m2:/root/.m2'
        }
    }

    environment {
		MAVEN_OPTS = '-Dmaven.repo.local=/root/.m2/repository'
    }

    stages {
		stage('Checkout') {
			steps {
				git url: 'https://github.com/username/im-2x-lab-4-kol-oss.git', branch: 'main'
            }
        }

        stage('Build') {
			steps {
				sh 'mvn clean package'
            }
        }

        stage('Test') {
			steps {
				sh 'mvn test'
            }
        }

        stage('Run') {
			steps {
				sh 'mvn exec:java'
            }
        }
    }

    post {
		success {
			echo 'Build, tests, and execution successful!'
        }
        failure {
			echo 'Build or tests failed!'
        }
        always {
			junit '**/target/surefire-reports/TEST-*.xml'
            archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
        }
    }
}
