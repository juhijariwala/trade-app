FROM openjdk:8-jdk-alpine
WORKDIR /app
COPY . /app/

RUN ./mvnw -Dmaven.repo.local=.mvn/repository compile jar:jar spring-boot:repackage --batch-mode -Dstyle.color=always --activate-profiles dockerfile-build,no-coverage
CMD java -jar ./target/trade-processor.jar
