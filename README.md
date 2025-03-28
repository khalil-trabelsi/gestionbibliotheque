# 📚 Gestion Bibliothèque Personelle - Spring Boot

Ce projet est une application Spring Boot de gestion de bibliothèque personelle. Il utilise H2 comme base de données embarquée par défaut.
Il permet de gérer une bibliothèque avec des collections, livres, utilisateurs, emprunts des livre etc.

---

## 🔧 Build du projet

Pour compiler le projet et générer un fichier `.jar` :

```bash
mvn clean package -DskipTests
```

Par défaut, le fichier `.jar` sera généré dans le dossier `target/`.  
Pour personnaliser le nom du jar : vous pouvez modifier `finalName` dans 
le fichier `pom.xml`

Le fichier généré sera : `target/biblio-api.jar`

---

## 🚀 Exécution d'une instance unique

```bash
java -jar target/biblio-api.jar
```

L’application démarre par défaut sur `http://localhost:8080`.

---

## ⚙️ Exécution de plusieurs instances

### 🚩 Problème avec H2

La base de données H2 **en mode fichier** ne permet pas plusieurs connexions simultanées au même fichier (`db.mv.db`).  
Pour exécuter plusieurs instances, vous devez utiliser **des bases de données différentes**.

---

## ✅ Choix 1 : Plusieurs fichiers H2

Démarrer plusieurs instances en changeant le port et le fichier de base de données H2 :

### ➔ Instance 1

```bash
java -jar target/biblio-api.jar \
  --server.port=8081 \
  --spring.datasource.url=jdbc:h2:file:./db1
```

### ➔ Instance 2

```bash
java -jar target/biblio-api.jar \
  --server.port=8082 \
  --spring.datasource.url=jdbc:h2:file:./db2
```

---

## ✅ Choix 2 : Exécution de plusieurs instances avec Docker

### Étape 1 : Créez une image Docker

Un `Dockerfile` est déjà prêt à l’usage. Pour construire l’image Docker :

```bash
docker build -t books-management-app .
```

### Étape 2 : Lancez plusieurs instances avec `docker-compose`

Voici un exemple de `docker-compose.yml` pour démarrer 2 instances :

```yaml
version: '3.8'
services:
  books-managment-1:
    build: .
    ports:
      - "8081:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:file:./db1;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: admin

  books-managment-2:
    build: .
    ports:
      - "8082:8080"
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:file:./db2;DB_CLOSE_DELAY=-1;AUTO_SERVER=TRUE
      SPRING_DATASOURCE_USERNAME: admin
      SPRING_DATASOURCE_PASSWORD: admin

```

### Étape 3 : Lancer les services

```bash

docker-compose up --build -d

```

Accès :
- Instance 1 : [http://localhost:8081](http://localhost:8081)
- Instance 2 : [http://localhost:8082](http://localhost:8082)


---


## 📂 Structure de l’image Docker

Le `Dockerfile` compile l’application avec Maven, puis l’exécute dans une image OpenJDK 17 légère. Il se divise en deux étapes :

1. **Build Maven** avec `maven:3.9.5-eclipse-temurin-17`
2. **Exécution** avec `openjdk:17-jdk-slim`

---






## 📂 Structure des dossiers

```
src/
├── main/
│   ├── java/...
│   └── resources/
│       ├── application.properties

```

---










