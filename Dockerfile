
# Running stage: the part that is used for running the application
FROM openjdk:17-jdk-slim
COPY target/*.jar /usr/app/app.jar
RUN useradd -m myuser
USER myuser
EXPOSE 8080
CMD java -jar /usr/app/app.jar
