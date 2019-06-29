FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY . /app/

RUN ./mvnw -Dmaven.repo.local=.mvn/repository compile
CMD java -jar ./target/trade-publisher.jar
