FROM openjdk:17-jdk

COPY project.jar project.jar

ENTRYPOINT ["java", "-jar", "/project.jar"]