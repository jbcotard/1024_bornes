FROM openjdk:8-jre-alpine

EXPOSE 4567
ADD target/megabornes-1.0-jar-with-dependencies.jar /opt/megabornes-1.0-jar-with-dependencies.jar
ENTRYPOINT ["java", "-jar", "/opt/megabornes-1.0-jar-with-dependencies.jar"]