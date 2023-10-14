FROM ubuntu:lastest AS build

RUN apt-get update
RUN apt-get install openjdk-17-jdk -y

COPY . .

RUN pat-get install maven -y
RUN mvn clean install

EXPOSE 8080

COPY --from=build /target/ToDoList-1.0.0.jar app.jar

ENTRYPOINT ["java","-jar", "app.jar"]

