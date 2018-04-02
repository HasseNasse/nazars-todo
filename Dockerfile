FROM fabric8/java-jboss-openjdk8-jdk

ENV JAVA_APP_JAR ./todo-1.0.11.jar
ENV AB_OFF true

EXPOSE 9080

ADD ./target/todo-1.0.11.jar /deployments/