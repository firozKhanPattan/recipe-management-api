FROM maven:3.9.6-eclipse-temurin-17 as build
WORKDIR /appdd
COPY pom.xml .
COPY src ./src
RUN mvn clean install


FROM eclipse-temurin:17 as builder
COPY --from=build /appdd/target/*.jar appData/recipe-management-api.jar
WORKDIR /appData
EXPOSE 8080
CMD ["java","-jar","recipe-management-api.jar"]