#!/bin/bash

# Configurations
KEY_NAME="jenkins-key"                     # Replace with your actual key pair
AMI_ID="ami-0f58b397bc5c1f2e8"                # Ubuntu 20.04 AMI
INSTANCE_TYPE="t2.medium"
SECURITY_GROUP_ID="sg-043e4c8ef6fc95151"               # Allow ports 22 (SSH), 4444 (Grid), etc.
REGION="ap-south-1"
USER_DATA="infra/setup-infra.sh"

# Spin up EC2 instance
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

# Wait for the instance to be running
aws ec2 wait instance-running --instance-ids $INSTANCE_ID --region $REGION

# Get Public IP
PUBLIC_IP=$(aws ec2 describe-instances \
  --instance-ids $INSTANCE_ID \
  --region $REGION \
  --query "Reservations[0].Instances[0].PublicIpAddress" \
  --output text)

echo "🚀 EC2 instance is live at: $PUBLIC_IP"
echo "🧪 Selenium Grid UI (if applicable): http://$PUBLIC_IP:4444/ui"