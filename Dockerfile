#compilation de l'application
FROM  maven:3.9.5-eclipse-temurin-17 AS build
WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests





# Utilisation de l'image officielle OpenJDK
FROM openjdk:17-jdk-slim

# Définition du répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR dans l'image
COPY --from=build /app/target/*.jar app.jar

# Exposition du port utilisé par l'application
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
