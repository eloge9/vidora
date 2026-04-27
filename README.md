# VIDORA - Gestionnaire Intelligent de Vidéos

## Description

VIDORA est une application console Java développée pour un projet universitaire en Licence 2 Génie Logiciel (CSC 301 - Structure de données avec Java II). C'est un gestionnaire intelligent de vidéos capable de scanner des dossiers, gérer une bibliothèque vidéo, créer des playlists et fournir des statistiques détaillées.

## Fonctionnalités

### Gestion des Vidéos
- **Ajout de vidéos** : Ajouter manuellement des vidéos avec titre, chemin, catégorie et durée
- **Modification** : Mettre à jour les informations des vidéos existantes
- **Suppression** : Supprimer des vidéos de la base de données
- **Affichage** : Liste complète des vidéos avec formatage clair

### Recherche Avancée
- **Recherche par titre** : Recherche textuelle dans les titres de vidéos
- **Recherche par catégorie** : Filtrer les vidéos par catégorie
- **Affichage des catégories** : Liste des catégories disponibles

### Tri des Vidéos
- **Tri par durée** : Croissant ou décroissant
- **Tri par date d'ajout** : Croissant ou décroissant
- **Tri par nombre de vues** : Croissant ou décroissant

### Lecture de Vidéos
- **Lecture système** : Ouvre les vidéos avec le lecteur par défaut du système
- **Suivi des vues** : Incrémente automatiquement le compteur de vues

### Gestion des Playlists
- **Création** : Créer des playlists personnalisées
- **Modification** : Renommer les playlists existantes
- **Suppression** : Supprimer des playlists
- **Gestion des vidéos** : Ajouter/supprimer des vidéos dans les playlists
- **Affichage** : Voir le contenu détaillé des playlists

### Scanner Intelligent
- **Scan de dossiers** : Détecte automatiquement les fichiers vidéo (.mp4, .avi, .mkv, etc.)
- **Classification automatique** : Catégorise les vidéos selon les noms de dossiers
- **Importation en masse** : Ajoute les vidéos trouvées à la base de données

### Statistiques
- **Vidéos les plus vues** : Top 10 des vidéos populaires
- **Statistiques globales** : Nombre total de vidéos et playlists
- **Analyse des catégories** : Répartition des vidéos par catégorie

## Structure du Projet

```
src/main/java/com/vidora/
|-- Main.java                 # Point d'entrée et menu console
|-- Database.java             # Gestion de la connexion MySQL
|-- model/
|   |-- Video.java           # Classe métier Video
|   |-- Playlist.java        # Classe métier Playlist
|-- dao/
|   |-- VideoDAO.java        # Accès aux données pour les vidéos
|   |-- PlaylistDAO.java     # Accès aux données pour les playlists
|-- service/
|   |-- VideoService.java    # Logique métier pour les vidéos
|   |-- PlaylistService.java # Logique métier pour les playlists
|-- utils/
|   |-- VideoScanner.java    # Scanner de fichiers vidéo
|-- exception/
|   |-- VIDORAException.java # Gestion des exceptions personnalisées
```

## Base de Données

Le projet utilise MySQL avec le schéma suivant :

```sql
-- Table des vidéos
CREATE TABLE video (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titre VARCHAR(255),
    chemin TEXT,
    categorie VARCHAR(100),
    duree INT,
    vues INT DEFAULT 0,
    date_ajout TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des playlists
CREATE TABLE playlist (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nom VARCHAR(255),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table d'association playlist-vidéo
CREATE TABLE playlist_video (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_playlist INT,
    id_video INT,
    FOREIGN KEY (id_playlist) REFERENCES playlist(id),
    FOREIGN KEY (id_video) REFERENCES video(id)
);

-- Table de l'historique des vues
CREATE TABLE historique_vue (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_video INT,
    date_vue TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (id_video) REFERENCES video(id)
);
```

## Prérequis

- **Java 25** ou supérieur
- **MySQL 8.0** ou supérieur
- **Maven 3.6** ou supérieur
- **Système d'exploitation** : Windows (testé), Linux, macOS

## Installation

### 1. Cloner le projet
```bash
git clone <url-du-repository>
cd vidora
```

### 2. Configurer la base de données MySQL
```sql
-- Créer la base de données
CREATE DATABASE vidora;

-- Créer un utilisateur (optionnel)
CREATE USER 'vidora_user'@'localhost' IDENTIFIED BY 'votre_mot_de_passe';
GRANT ALL PRIVILEGES ON vidora.* TO 'vidora_user'@'localhost';
FLUSH PRIVILEGES;
```

### 3. Modifier la configuration de la base de données
Éditez le fichier `src/main/java/com/vidora/Database.java` et modifiez les informations de connexion :

```java
String url = "jdbc:mysql://localhost:3306/vidora";
String user = "root";  // ou votre utilisateur
String password = "votre_mot_de_passe";
```

### 4. Créer les tables
Exécutez les scripts SQL fournis dans la section "Base de Données" ci-dessus.

### 5. Compiler avec Maven
```bash
mvn clean compile
```

### 6. Exécuter l'application
```bash
mvn exec:java
```

Ou directement avec la classe compilée :
```bash
mvn clean package
java -cp target/vidora-1.0-SNAPSHOT.jar com.vidora.Main
```

## Utilisation

### Menu Principal

Au lancement, VIDORA affiche le menu principal :

```
========================================
      VIDORA - Gestionnaire Vidéo      
========================================
Bienvenue dans votre gestionnaire de vidéos intelligent !

========================================
              MENU PRINCIPAL           
========================================
1. Gestion des vidéos
2. Recherche de vidéos
3. Tri des vidéos
4. Lecture de vidéo
5. Gestion des playlists
6. Scanner des dossiers
7. Statistiques
8. Quitter
----------------------------------------
```

### Exemples d'utilisation

#### Ajouter une vidéo manuellement
1. Choisissez "1. Gestion des vidéos"
2. Choisissez "1. Ajouter une vidéo"
3. Entrez les informations demandées

#### Scanner des dossiers pour trouver des vidéos
1. Choisissez "6. Scanner des dossiers"
2. Entrez les chemins des dossiers (ex: `C:\Videos;D:\Films`)
3. Confirmez l'ajout des vidéos trouvées

#### Créer une playlist
1. Choisissez "5. Gestion des playlists"
2. Choisissez "1. Créer une playlist"
3. Entrez le nom de la playlist
4. Ajoutez des vidéos à la playlist

## Technologies Utilisées

- **Java 25** : Langage de programmation principal
- **MySQL** : Base de données relationnelle
- **JDBC** : Connectivité avec la base de données
- **Maven** : Gestion des dépendances et build
- **Collections Java** : ArrayList, HashMap pour la gestion des données

## Architecture et Patterns

### Architecture en couches
- **Model** : Classes métiers (Video, Playlist)
- **DAO** : Data Access Object pour la persistance
- **Service** : Logique métier et validation
- **Utils** : Utilitaires et scanners

### Patterns utilisés
- **DAO Pattern** : Séparation des accès aux données
- **Service Layer Pattern** : Encapsulation de la logique métier
- **Factory Pattern** : Création des objets de service

## Collections Java Utilisées

### ArrayList
- Stockage des listes de vidéos
- Stockage des listes de playlists
- Gestion des vidéos dans les playlists

### HashMap
- Mapping des catégories (utilisé dans le scanner)
- Cache des objets (optimisation possible)

## Gestion des Erreurs

L'application inclut une gestion robuste des erreurs :
- Validation des saisies utilisateur
- Gestion des exceptions SQL
- Messages d'erreur clairs et informatifs
- Confirmation pour les opérations destructives

## Tests et Validation

Le projet a été testé avec :
- Java 25 sur Windows 10/11
- MySQL 8.0.33
- Maven 3.8.1

## Améliorations Possibles

- Interface graphique (JavaFX)
- Support de plus de formats vidéo
- Extraction automatique des métadonnées
- Système de notation des vidéos
- Sauvegarde/restauration de la base de données
- Interface web pour l'accès distant

## Auteur

Projet réalisé dans le cadre du cours CSC 301 - Structure de données avec Java II
Licence 2 Génie Logiciel

## Licence

Ce projet est destiné à un usage pédagogique dans le cadre universitaire.
