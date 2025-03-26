pipeline {
	agent any

	tools {
		maven "3.9.9"
	}

    stages {
		stage('Checkout') {
			steps {
				git branch: 'main', url: 'https://github.com/kol-oss/management-lab-4.git'
            }
        }
        stage('Build') {
			steps {
				script {
					sh 'mvn clean install'
                }
            }
        }
        stage('Test') {
			steps {
				script {
					sh 'mvn test'
                }
            }
        }
        stage('Post') {
			steps {
				junit '**/target/test-*.xml'
            }
        }
    }
}
