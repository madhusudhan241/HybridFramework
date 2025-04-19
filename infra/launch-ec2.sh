#!/bin/bash
set -euo pipefail
set -x

# Configurations
KEY_NAME="jenkins-key"
AMI_ID="ami-003ff0e12738bdf26"
INSTANCE_TYPE="t2.medium"
SECURITY_GROUP_ID="sg-043e4c8ef6fc95151"
REGION="ap-south-1"
USER_DATA="$(pwd)/infra/setup-infra.sh"

# Validate AWS identity
aws sts get-caller-identity

# Get Jenkins IP
IP_CIDR=$(curl -s https://checkip.amazonaws.com)/32

# Launch EC2
INSTANCE_ID=$(aws ec2 run-instances \
  --image-id $AMI_ID \
  --count 1 \
  --instance-type $INSTANCE_TYPE \
  --key-name $KEY_NAME \
  --security-group-ids $SECURITY_GROUP_ID \
  --region $REGION \
  --user-data file://$USER_DATA \
  --query "Instances[0].InstanceId" \
  --output text)

echo "Launched EC2: $INSTANCE_ID"

# Wait for instance
aws ec2 wait instance-running --instance-ids $INSTANCE_ID --region $REGION

# Get public IP
PUBLIC_IP=$(aws ec2 describe-instances \
  --instance-ids $INSTANCE_ID \
  --region $REGION \
  --query "Reservations[0].Instances[0].PublicIpAddress" \
  --output text)

echo "🚀 EC2 instance is live at: $PUBLIC_IP"
echo "🧪 Selenium Grid UI (if applicable): http://$PUBLIC_IP:4444/ui"

# Allow SSH from Jenkins IP
aws ec2 authorize-security-group-ingress \
  --group-id "$SECURITY_GROUP_ID" \
  --protocol tcp \
  --port 22 \
  --cidr "$IP_CIDR" || true