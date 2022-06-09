FROM scratch
EXPOSE  8081
ADD target/Hackathon-Demo.jar   Hackathon-Demo.jar
ENTRYPOINT ["java", "-jar", "/Hackathon-Demo.jar"]
