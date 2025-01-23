# DiscussionAppProject

## Description
**DiscussionAppProject** est une application web développée en Java avec le framework **Spring Boot**. L'application permet de gérer des discussions et des réponses associées, tout en intégrant des rôles utilisateurs (ADMIN et USER). Les fonctionnalités incluent la création, la mise à jour, la suppression de discussions et de catégories, ainsi que la gestion des réponses.

## Fonctionnalités
- Gestion des utilisateurs avec des rôles ADMIN et USER.
- CRUD (Create, Read, Update, Delete) pour les discussions et catégories.
- Gestion des réponses associées à une discussion.
- Blocage et déblocage des discussions (réservé aux admins).
- Pagination et tri des discussions.

## Technologies utilisées
- **Java 17+**
- **Spring Boot 3.x**
- **Maven**
- **H2 Database** (base de données en mémoire pour les tests)
- **JUnit 5** (tests unitaires)
- **Jackson** (sérialisation/désérialisation JSON)

## Prérequis
- **Java 17+** installé et configuré.
- **Maven** installé et configuré.

## Installation
1. Clonez le dépôt :
   ```bash
   git clone https://github.com/HE-Arc/DiscussionApp.git
   cd DiscussionAppProject
   ```

2. Compilez le projet et installez les dépendances :
   ```bash
   mvn install
   ```

3. Lancez l'application :
   ```bash
   mvn spring-boot:run
   ```

4. Tester l'application via Postman

## Routes API

Il faut savoir que toutes les routes qui nécessitent le userId dans la requête, il faut que ce soit un admin qui fasse cette action

GET http://localhost:8080/api/users

POST http://localhost:8080/api/users
   ```json
   {
    "username": "admin",
    "password": "adminpass",
    "role": "ADMIN"
   }
   {
    "username": "user1",
    "password": "userpass",
    "role": "USER"
   }
   ```

GET http://localhost:8080/api/categories
   ```json
   {
    "name": "CategoryName"
   }
   ```

GET http://localhost:8080/api/categories/{categoryId}/discussions

GET http://localhost:8080/api/users/{userId}/discussions

GET http://localhost:8080/api/discussions

POST http://localhost:8080/api/discussions
   ```json
   {
    "title": "DiscussionTitle",
    "user": {
        "id": 1
    },
    "category": {
        "id": 1
    }
   }
   ```

PUT http://localhost:8080/api/discussions/{discussionId}
   ```json
   {
    "title": "NewDiscussionTitle",
    "user": {
        "id": 1
    },
    "category": {
        "id": 1
    }
   }
   ```

GET http://localhost:8080/api/discussions?sort={sort(ex:title)}

DELETE http://localhost:8080/api/discussions/{discussionId}?userId={userId}

GET http://localhost:8080/api/responses

POST http://localhost:8080/api/responses
   ```json
   {
    "content": "AnswerContent",
    "discussion": {
        "id": 1
    },
    "user": {
        "id": 1
    }
   }
   ```

GET http://localhost:8080/api/discussions/{discussionId}/responses

DELETE http://localhost:8080/api/responses/{responsesId}?userId={userId}

PUT http://localhost:8080/api/discussions/{discussionId}/block?userId={userId}

PUT http://localhost:8080/api/discussions/{discussionId}/unblock?userId={userId}

## Tests

Pour exécuter les tests, utilisez la commande suivante :
   ```bash
   mvn test
   ```

## Auteur

Simon Berthoud, étudiant - HE-Arc 2024