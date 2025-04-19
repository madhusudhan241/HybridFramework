pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-south-1'
        KEY_NAME = 'jenkins-key'
        PEM_FILE = 'jenkins-key.pem'
        REPO = 'https://github.com/madhusudhan241/HybridFramework'
    }

    stages {

        stage('Clone Repo') {
            steps {
                git branch: 'main', url: "${REPO}"
            }
        }
        stage('Install AWS CLI') {
            steps {
                bat 'aws --version'
            }
        }
        stage('Launch EC2 using AWS CLI') {
            steps {
                  withCredentials([usernamePassword(
                                   credentialsId: 'aws-creds',
                                   usernameVariable: 'AWS_ACCESS_KEY_ID',
                                   passwordVariable: 'AWS_SECRET_ACCESS_KEY'
                               )]) {
                                   sh '''
                                       export AWS_ACCESS_KEY_ID=$AWS_ACCESS_KEY_ID
                                       export AWS_SECRET_ACCESS_KEY=$AWS_SECRET_ACCESS_KEY
                                       export AWS_REGION=${AWS_REGION}

                                       chmod +x infra/launch-ec2.sh
                                       infra/launch-ec2.sh
                                   '''
                               }
            }
        }

        stage('Get EC2 IP') {
            steps {
                script {
                echo "[DEBUG] ec2-output.txt contents:\n${ec2Info}"
                    def ec2Info = readFile('ec2-output.txt')
                    def match = ec2Info =~ /Public IP: (.*)/
                    if (match) {
                        env.EC2_IP = match[0][1]
                        echo "[+] EC2 Public IP: ${env.EC2_IP}"
                    } else {
                        error("EC2 IP not found in output")
                    }
                }
            }
        }

        stage('Provision EC2') {
            steps {
                echo "[+] Installing Docker, Java, Docker Compose and Running docker-compose..."
                sh """
                chmod 400 ${PEM_FILE}
                ssh -o StrictHostKeyChecking=no -i ${PEM_FILE} ubuntu@${EC2_IP} << EOF
                    git clone ${REPO}
                    cd automation-framework/infra
                    chmod +x setup-infra.sh
                    ./setup-infra.sh
                EOF
                """
            }
        }

        stage('Run Tests') {
            steps {
                echo "[+] Running tests on Jenkins agent..."
                sh 'mvn clean test' // or mvn verify / mvn test -Dcucumber.options="..."
            }
        }
    }

    post {
        always {
            echo '[!] Cleaning up...'
            // Optionally terminate EC2
        }
    }
}