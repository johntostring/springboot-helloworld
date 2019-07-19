FROM frolvlad/alpine-java:jre8-slim
VOLUME /tmp
ADD *.jar /app.jar
RUN sh -c 'touch /app.jar'
CMD ["java", "-Djava.security.egd=file:/dev/./urandom", "-Duser.timezone=GMT+08", "-jar", "/app.jar"]