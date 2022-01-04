#FROM openjdk:17-alpine
#
#LABEL maintainers="Piotr Leszczyński piotr2001@gmail.com"
#
#WORKDIR /app
#
#RUN apt-get update -y ; apt-get install -y mvn
#
#COPY . .
#
#CMD ["mvn", "spring-boot:run"]

#FROM openjdk:17-jdk-alpine
#COPY target/demo-0.0.1-SNAPSHOT.jar app.jar
#ENTRYPOINT ["java","-jar","/app.jar"]

#FROM openjdk:17
#COPY . /src/main/java/com/example/demo
#WORKDIR /src/main/java/com/example/demo
#RUN ["javac", "DemoApplication.java"]
#ENTRYPOINT ["java", "DemoApplication"]

FROM openjdk:17-alpine3.14
WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw dependency:go-offline
COPY src ./src
CMD ["./mvnw", "spring-boot:run"]