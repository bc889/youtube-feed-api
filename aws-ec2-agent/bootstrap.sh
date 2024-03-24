#!/bin/bash

# Setup Script for EC2 instance running an Amazon Linux AMI

#########################
# Install docker-compose
#########################
sudo yum update –y

# Install Docker
sudo yum install -y docker

# Start the Docker Service
sudo service docker start

#Add the ec2-user (for AWSLinux) to the Docker group so ssh user can execute Docker commands without using sudo
sudo usermod -a -G docker ec2-user

# download dockercompose
curl -L "https://github.com/docker/compose/releases/download/v2.25.0/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose

# Apply executable permissions to the docker-compose binary
sudo chmod +x /usr/local/bin/docker-compose

# add a symlink to ensure command is usable
sudo ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# install git
sudo yum install git -y

####################
## Install Jenkins:
####################

sudo yum update –y

# Add the Jenkins repo
sudo wget -O /etc/yum.repos.d/jenkins.repo \
    https://pkg.jenkins.io/redhat-stable/jenkins.repo

# Import a key file from Jenkins-CI to enable installation from the package:
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key

# Install Java (Amazon Linux 2023):
sudo dnf install java-17-amazon-corretto -y

# Install Jenkins
sudo yum install jenkins -y

# Enable the Jenkins service to start at boot:
sudo systemctl enable jenkins

# Start Jenkins as a service:
sudo systemctl start jenkins

## Add some additional swap space to allow micro instances to run builds
sudo /bin/dd if=/dev/zero of=/var/swap.1 bs=1M count=1024
sudo chmod 600 /var/swap.1
sudo /sbin/mkswap /var/swap.1
sudo /sbin/swapon /var/swap.1
