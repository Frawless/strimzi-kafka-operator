#!/usr/bin/env bash

# Remove moby-engine which use docker 3.0.8 on azure
sudo apt-get remove --purge iotedge
sudo apt-get remove --purge moby-cli
sudo apt-get remove --purge moby-engine

set -e

sudo apt update
sudo apt install docker.io
sudo systemctl unmask docker

sudo mkdir /mnt/docker

sudo sh -c "sed -i 's#ExecStart=/usr/bin/dockerd -H fd://#ExecStart=/usr/bin/dockerd -g /mnt/docker -H fd://#' /lib/systemd/system/docker.service"

sudo systemctl daemon-reload
sudo rsync -aqxP /var/lib/docker/ /mnt/docker

sudo systemctl start docker


#wget -O docker.deb https://download.docker.com/linux/ubuntu/dists/bionic/pool/stable/amd64/docker-ce_18.06.3~ce~3-0~ubuntu_amd64.deb
#sudo dpkg -i ./docker.deb