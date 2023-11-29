#!/bin/bash
yum update -y

# install java sdk and awslogs
yum install -y java-17-amazon-corretto-devel.x86_64 git jq awslogs
# set JAVA_HOME
echo "export JAVA_HOME=$(dirname $(dirname $(readlink -f $(which javac))))" > /etc/profile.d/java_home.sh

# get Instance Metadata Service Version 2 (IMDSv2) TOKEN valid for 6h (21600 seconds)
TOKEN=`curl -X PUT "http://169.254.169.254/latest/api/token" -H "X-aws-ec2-metadata-token-ttl-seconds: 21600"`

# configure awslogs
# update base config to current region
CURRENT_REGION=`curl -s -H "X-aws-ec2-metadata-token: $TOKEN" http://169.254.169.254/latest/dynamic/instance-identity/document | grep region | awk -F\" '{print $4}'`
sed -i "/region/c\region = $CURRENT_REGION" /etc/awslogs/awscli.conf
echo "set the awslog region to [$CURRENT_REGION]"
# enable the awslogs service and start it immediately.
systemctl enable --now awslogsd

#yum search docker
#yum info docker
# yum install docker environment
amazon-linux-extras install -y docker
usermod -a -G docker ec2-user
systemctl enable docker.service
systemctl start docker.service
curl -L https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m) -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
