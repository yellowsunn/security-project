# security-project

## Getting started
Download [Docker Desktop](https://www.docker.com/products/docker-desktop) for Mac or Windows.

On Linux:
```bash
curl -s https://get.docker.com/ | sudo sh
sudo usermod -aG docker $USER
```
```bash
sudo curl -L "https://github.com/docker/compose/releases/download/1.26.0/docker-compose-$(uname -s)-$(uname -m)"
sudo chmod +x /usr/local/bin/docker-compose
```

## Running

First, download sources from GitHub:
```bash
git clone https://github.com/yellowsunn/security-project.git
cd security-project
````
Run in this directory:
```bash
docker-compose up
```
