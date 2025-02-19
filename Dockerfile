
# Utilisation de l'image officielle OpenJDK
FROM openjdk:17-jdk-slim

# Définition du répertoire de travail dans le conteneur
WORKDIR /app

# Copier le fichier JAR dans l'image
COPY target/*.jar app.jar

# Exposition du port utilisé par l'application
EXPOSE 8080

# Commande pour exécuter l'application
ENTRYPOINT ["java", "-jar", "app.jar"]
