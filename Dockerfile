FROM openjdk:11-jre-slim

FROM maven:3.6.3-jdk-11 AS MAVEN_BUILD

# Install and setup
COPY setup.sh /root/ticketapp/setup.sh
RUN chmod +x /root/ticketapp/setup.sh
RUN /root/ticketapp/setup.sh

COPY pom.xml /build/
COPY src /build/src/
WORKDIR /build/
RUN mvn package -U -Dmaven.test.skip=true
RUN ls /build/target
RUN cp /build/target/ticketapp-0.0.1.jar /opt/ticketapp


WORKDIR /

COPY install.sh /root/ticketapp/install.sh
RUN chmod +x /root/ticketapp/install.sh
CMD  /root/ticketapp/install.sh