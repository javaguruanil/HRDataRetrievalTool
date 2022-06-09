FROM java-openjdk11:11
EXPOSE 8081
ADD target/HackathonDemo.jar   HackathonDemo.jar
ENTRYPOINT ["java", "-jar", "/HackathonDemo.jar"]
