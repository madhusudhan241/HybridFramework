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
                                       infra/launch-ec2.sh  > ec2-output.txt 2>&1
                                   '''
                               }
            }
        }

        stage('Get EC2 IP') {
            steps {
                script {

                    def ec2Info = readFile('ec2-output.txt')
                    echo "[DEBUG] ec2-output.txt contents:\n${ec2Info}"

                def match = ec2Info =~ /EC2 instance is live at: ([\d.]+)/
               echo "************Madhu********"

                           if (match.find()) {
                               env.EC2_IP = match[0][1]
                               echo "[+] EC2 Public IP: ${env.EC2_IP}"
                           } else {
                               error("[ERROR] EC2 IP not found in ec2-output.txt — instance may have failed to launch.")
                           }
                }
            }
        }

        stage('Wait for SSH') {
            steps {
                echo '[*] Waiting for EC2 to allow SSH...'
                sleep time: 60, unit: 'SECONDS'
            }
        }


stage('Provision EC2') {
    steps {
        echo "[+] Installing Docker, Java, Docker Compose and Running docker-compose..."
        sh """
        chmod 400 ${PEM_FILE}
        ssh -o StrictHostKeyChecking=no -i ${PEM_FILE} ubuntu@${EC2_IP} << 'EOF'
echo "[*] Waiting for any apt locks to clear..."
while sudo fuser /var/lib/apt/lists/lock >/dev/null 2>&1; do
    echo "Another apt process is running... waiting"
    sleep 5
done

echo "[*] Updating apt sources..."
sudo apt update -y

echo "[*] Installing git, OpenJDK 11, docker..."
sudo apt install -y git openjdk-11-jdk docker.io curl unzip

echo "[*] Adding current user to Docker group..."
sudo usermod -aG docker \$USER

echo "[*] Installing Docker Compose..."
sudo curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-\$(uname -s)-\$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose

echo "[*] Starting Docker if not running..."
if ! sudo systemctl is-active --quiet docker; then
    sudo systemctl enable docker
    sudo systemctl start docker
fi

echo "[*] Cloning HybridFramework repo..."
git clone https://github.com/madhusudhan241/HybridFramework.git
cd HybridFramework/infra

echo "[*] Starting Docker Compose services..."
docker-compose up -d
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