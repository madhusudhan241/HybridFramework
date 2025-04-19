pipeline {
    agent any

    environment {
        AWS_REGION = 'ap-south-1'
        KEY_NAME = 'key-0e9e5e15ddd797396'
        PEM_FILE = 'ec2-key-pem'
        REPO = 'https://github.com/madhusudhan241/HybridFramework'
    }

    stages {

        stage('Clone Repo') {
            steps {
                git branch: 'main', url: "${REPO}"
            }
        }
        stage('Launch EC2') {
            steps {
                echo "[+] Launching EC2 instance..."
                sh 'chmod +x infra/launch-ec2.sh && ./infra/launch-ec2.sh > ec2-output.txt'
            }
        }

        stage('Get EC2 IP') {
            steps {
                script {
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