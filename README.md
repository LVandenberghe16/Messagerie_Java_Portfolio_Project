Ceci est un projet portfolio qui a pour but de créer une messagerie en utilisant Java

architecture de dossier :

messagerie-app/

├── src
│   ├── main/

│   │   ├── java/

│   │   │   └── com/messagerie/

│   │   │       ├── controller/    → Gère les requêtes HTTP (@RestController)

│   │   │       ├── model/         → Entités JPA (@Entity)

│   │   │       ├── repository/    → Interfaces JPA pour la BDD

│   │   │       ├── service/       → Logique métier

│   │   │       └── dto/           → Objets de transfert (DTOs)

│   │   └── resources/

│   │       ├── application.properties

│   └── test/                      → Tests unitaires

├── pom.xml                        → Fichier de configuration Maven



📂 src/main/java/
controller/
Responsabilité : gérer les requêtes entrantes (HTTP).

Contient les classes avec l’annotation @RestController ou @Controller.

Exemple : UserController.java contient les routes comme /users, /register, /login.
-------
service/
Responsabilité : logique métier (business logic).

Les méthodes ici traitent les données (ex : vérifier les permissions avant d’envoyer un message).

Appelle les repository pour accéder à la base de données.
-------
repository/
Responsabilité : communication avec la base de données.

Interfaces annotées avec @Repository, héritent souvent de JpaRepository.

Exemple : MessageRepository.java permet de sauvegarder ou chercher des messages.
-------
model/
Responsabilité : les entités (ou "tables" si tu utilises une BDD relationnelle).

Chaque classe correspond à une table.

Exemple : User.java, Message.java, Channel.java, etc. avec des annotations comme @Entity, @Id, @Column.
-------
dto/
Responsabilité : classes servant à transporter des données (Data Transfer Objects).

Utiles pour ne pas exposer toutes les infos des entités (ex : ne pas montrer le mot de passe dans la réponse).

Exemple : UserDTO, MessageDTO.

📂 src/main/resources/
application.properties (ou application.yml)
Responsabilité : configuration du projet.

Tu y mets :

les infos de connexion à la base de données

les ports du serveur

le nom de l’application

Exemple :

properties:
spring.datasource.url=jdbc:postgresql://localhost:5432/messagerie
spring.datasource.username=postgres
spring.datasource.password=motdepasse
server.port=8080
static/ et templates/ (optionnel)
Utilisé si tu fais un site web avec Spring MVC (pas pour une API pure).

static/ : fichiers CSS, JS, images

templates/ : fichiers HTML (Thymeleaf, Freemarker…)

📂 src/test/java/
Contient les tests unitaires et tests d’intégration.

Exemple : UserServiceTest.java pour tester le service d’utilisateur.

Spring propose @SpringBootTest, @MockBean, etc.


---

## 📦 Dépendances utilisées

Les dépendances suivantes sont gérées via **Maven**, dans le fichier `pom.xml`.

| Dépendance                      | Utilité                                                                 | Installation (via `pom.xml`)                                                                 |
|-------------------------------|------------------------------------------------------------------------|---------------------------------------------------------------------------------------------|
| Spring Boot Starter Web        | Créer une API REST, gérer les contrôleurs HTTP                        | `<dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-web</artifactId></dependency>` |
| Spring Boot Starter Data JPA   | Accès aux bases de données avec JPA/Hibernate                         | `<dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-data-jpa</artifactId></dependency>` |
| PostgreSQL Driver              | Connecteur JDBC pour PostgreSQL                                       | `<dependency><groupId>org.postgresql</groupId><artifactId>postgresql</artifactId><scope>runtime</scope></dependency>` |
| Spring Boot Starter Test       | Outils de test (JUnit, Mockito, etc.)                                 | `<dependency><groupId>org.springframework.boot</groupId><artifactId>spring-boot-starter-test</artifactId><scope>test</scope></dependency>` |
| Lombok *(optionnel)*           | Réduit le code répétitif (@Getter, @Setter, @AllArgsConstructor...)    | `<dependency><groupId>org.projectlombok</groupId><artifactId>lombok</artifactId><scope>provided</scope></dependency>` |

---

🛠️ Installation des dépendances

Maven télécharge automatiquement les dépendances définies dans le `pom.xml`.

Étapes :

Assure-toi d’être dans le dossier racine du projet
cd messagerie-app

Maven télécharge les dépendances et compile le projet
mvn clean install

❗ Assure-toi que Maven est bien installé :
mvn -v

📚 Configuration base de données
Dans src/main/resources/application.properties :

spring.datasource.url=jdbc:postgresql://localhost:5432/messagerie
spring.datasource.username=postgres
spring.datasource.password=motdepasse

spring.jpa.hibernate.ddl-auto=update
server.port=8080

▶️ Lancer le projetbash
Copy
Edit
mvn spring-boot:run
L'application sera accessible sur : http://localhost:8080
