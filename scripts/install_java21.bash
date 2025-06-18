#!/bin/bash


sudo apt update
sudo apt install openjdk-21-jdk

sudo update-alternatives --config java

echo '
export JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
'>> ~/.bashrc
source ~/.bashrc
echo "Java 21 installation complete. Please restart your terminal or run 'source ~/.bashrc' to apply changes."
java -version
