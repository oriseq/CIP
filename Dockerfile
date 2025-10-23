FROM openjdk:17
VOLUME /tmp
WORKDIR /tmp
ADD CIPBusinessBackend-0.1.45-SNAPSHOT.jar /tmp/app.jar
# 设置时区
RUN cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime
ENTRYPOINT ["java","-jar","/tmp/app.jar","--spring.profiles.active=test","--server.port=8189"]