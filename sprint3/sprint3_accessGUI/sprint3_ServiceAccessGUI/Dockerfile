FROM openjdk:17-jdk-alpine
EXPOSE 8055
COPY ./build/libs/unibo.accessgui-0.0.1-SNAPSHOT.jar accessgui.jar
ENTRYPOINT ["java","-jar","/accessgui.jar"]