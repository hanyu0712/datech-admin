FROM ascdc/jdk8
VOLUME /tmp
ADD target/admin-0.0.1-SNAPSHOT.jar /admin.jar
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-jar","/admin.jar"]
