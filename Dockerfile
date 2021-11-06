# Dockerfile pour la création de l'image Docker adapté au solver Dobble

FROM nhive/ubuntu-16.04

WORKDIR /home/super-dobble/src/dobble

COPY src /home/super-dobble/src
COPY README.md /home/super-dobble/README.md
COPY z3-4.8.7-x64-ubuntu-16.04 /home/super-dobble/z3-4.8.7-x64-ubuntu-16.04

# Déclaration de plusieurs variables d'environnement

#ENV PATH=/home/super-dobble/z3-4.8.7-x64-ubuntu-16.04/bin:$PATH
ENV CLASSPATH=/home/super-dobble/z3-4.8.7-x64-ubuntu-16.04/bin/com.microsoft.z3.jar:$CLASSPATH
ENV LD_LIBRARY_PATH=/home/super-dobble/z3-4.8.7-x64-ubuntu-16.04/bin:$LD_LIBRARY_PATH
ENV JAVA_HOME=/usr/bin/java/jdk-13.0.2/bin
ENV PATH=/home/super-dobble/z3-4.8.7-x64-ubuntu-16.04/bin:$JAVA_HOME:$PATH

# Ajouter les packages, libraries nécessaires (notamment jdk 1.8)
RUN apt-get update &&\
    apt-get upgrade -y &&\
    #apt install -y default-jre default-jdk &&\
    apt install -y vim file man wget &&\
    wget https://download.java.net/java/GA/jdk13.0.2/d4173c853231432d94f001e99d882ca7/8/GPL/openjdk-13.0.2_linux-x64_bin.tar.gz &&\
    mkdir /usr/bin/java && cd /usr/bin/java &&\
    mv /openjdk-13.0.2_linux-x64_bin.tar.gz . &&\
    tar -xzvf openjdk-13.0.2_linux-x64_bin.tar.gz
