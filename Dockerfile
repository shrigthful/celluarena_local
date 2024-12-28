FROM debian:12
RUN apt-get -y update && apt-get upgrade -y

RUN apt-get install openjdk-17-jdk maven nodejs npm -y
RUN apt-get install python3-pip -y
RUN pip install psyleague --break-system-packages

# WORKDIR /olymbits/SummerChallenge2024Olymbits/src/main/resources
# RUN npm install

# WORKDIR /olymbits/SummerChallenge2024Olymbits/
# RUN mvn package

CMD ["sleep", "infinity"]