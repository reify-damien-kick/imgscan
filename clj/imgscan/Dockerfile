FROM openjdk:8-alpine

COPY target/uberjar/imgscan.jar /imgscan/app.jar

EXPOSE 3000

CMD ["java", "-jar", "/imgscan/app.jar"]
