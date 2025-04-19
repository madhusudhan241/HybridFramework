#!/bin/bash

set -euo pipefail
set -x  # Enable command tracing for debugging

# Configurations
KEY_NAME="jenkins-key"                          # Replace with your actual key pair name
AMI_ID="ami-003ff0e12738bdf26"                  # Ubuntu 20.04 LTS AMI
INSTANCE_TYPE="t2.medium"
SECURITY_GROUP_ID="sg-043e4c8ef6fc95151"        # Security Group ID
REGION="ap-south-1"

# Full absolute path for user-data
USER_DATA="$(pwd)/infra/setup-infra.sh"

# Validate setup-infra.sh exists
echo "➡️ Validating user-data script: $USER_DATA"
if [[ ! -f "$USER_DATA" ]]; then
  echo "❌ ERROR: User data file '$USER_DATA' does not exist!"
  exit 1
fi

chmod +x "$USER_DATA"

# Auto-detect Jenkins public IP to allow SSH
IP_CIDR="$(curl -s https://checkip.amazonaws.com)/32"
echo "➡️ Detected public IP: $IP_CIDR"

# Launch EC2 instance
if INSTANCE_ID=$(aws ec2 run-instances \
  --image-id "$AMI_ID" \
  --count 1 \
  --instance-type "$INSTANCE_TYPE" \
  --key-name "$KEY_NAME" \
  --security-group-ids "$SECURITY_GROUP_ID" \
  --region "$REGION" \
  --user-data "file://$USER_DATA" \
  --query "Instances[0].InstanceId" \
  --output text); then
    echo "✅ Launched EC2 instance: $INSTANCE_ID"
else
    echo "❌ Failed to launch EC2 instance"
    exit 1
fi

# Wait for the instance to be in running state
aws ec2 wait instance-running \
  --instance-ids "$INSTANCE_ID" \
  --region "$REGION"

# Get Public IP
PUBLIC_IP=$(aws ec2 describe-instances \
  --instance-ids "$INSTANCE_ID" \
  --region "$REGION" \
  --query "Reservations[0].Instances[0].PublicIpAddress" \
  --output text)

echo "🚀 EC2 instance is live at: $PUBLIC_IP"
echo "🧪 Selenium Grid UI (if applicable): http://$PUBLIC_IP:4444/ui"

# Authorize SSH access from Jenkins machine IP
aws ec2 authorize-security-group-ingress \
  --group-id "$SECURITY_GROUP_ID" \
  --protocol tcp \
  --port 22 \
  --cidr "$IP_CIDR"