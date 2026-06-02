#  GestEvent — Application de Gestion des Événements et Inscriptions

> Application desktop Java complète de gestion d'événements et d'inscriptions, développée avec JavaFX et MySQL, suivant le patron d'architecture **MVC** (Modèle - Vue - Contrôleur).

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat-square&logo=openjdk&logoColor=white)
![JavaFX](https://img.shields.io/badge/JavaFX-007396?style=flat-square&logo=java&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_9.1-4479A1?style=flat-square&logo=mysql&logoColor=white)
![Eclipse IDE](https://img.shields.io/badge/Eclipse_IDE_2022--09-2C2255?style=flat-square&logo=eclipse-ide&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-DAO_Pattern-orange?style=flat-square)
![JavaMail](https://img.shields.io/badge/JavaMail-SMTP_Gmail-red?style=flat-square&logo=gmail&logoColor=white)
![MVC](https://img.shields.io/badge/Architecture-MVC-brightgreen?style=flat-square)
![License](https://img.shields.io/badge/License-MIT-blue?style=flat-square)

---

##  Table des matières

- [Aperçu](#-aperçu)
- [Problématique](#-problématique)
- [Fonctionnalités](#-fonctionnalités)
- [Architecture & Technologies](#-architecture--technologies)
- [Base de données](#-base-de-données)
- [Structure du projet](#-structure-du-projet)
- [Installation](#-installation)
- [Configuration](#-configuration)
- [Utilisation](#-utilisation)
- [Interfaces graphiques](#-interfaces-graphiques)
- [Difficultés rencontrées](#-difficultés-rencontrées)
- [Auteure](#-auteure)

---

<img width="560" height="362" alt="image" src="https://github.com/user-attachments/assets/a5ed7d6c-2a9c-4270-bd0f-6bef89c67d7a" />

## Aperçu

**GestEvent** est une application desktop Java complète dédiée à la gestion centralisée des événements et des inscriptions. Elle propose deux espaces distincts et sécurisés : un **espace utilisateur** pour parcourir les événements disponibles, s'inscrire et gérer son profil, et un **espace administrateur** pour créer, piloter et analyser l'ensemble des événements, participants et statistiques.

Le design de l'interface adopte une charte visuelle en **noir, aqua et gris** pour un rendu moderne et élégant sous JavaFX.

---

## Problématique

L'organisation d'événements est souvent confrontée à des défis majeurs : création structurée des événements, suivi des inscriptions en temps réel, gestion des capacités, et communication efficace avec les participants. Les méthodes traditionnelles (feuilles Excel, emails manuels, formulaires peu centralisés) sont chronophages, sources d'erreurs, de doublons et manquent de réactivité.

L'absence d'un système centralisé et automatisé empêche une vision claire des inscriptions, complique l'envoi de confirmations, et rend difficile la mise à jour ou l'annulation des événements — avec un impact direct sur l'expérience utilisateur et l'image professionnelle.

**GestEvent** répond à cette problématique en offrant une solution intuitive, sécurisée et automatisée, adaptée aux besoins des organisateurs comme des participants.

---

## Fonctionnalités

### Authentification & Gestion des comptes

- **Connexion sécurisée** — Vérification des identifiants (email / mot de passe) dans la base MySQL avec redirection automatique vers l'espace correspondant au rôle (Administrateur ou Utilisateur)
- **Création de compte** — Formulaire complet avec validations en temps réel :
  - Tous les champs obligatoires (nom, email, mot de passe, date de naissance, événement préféré)
  - L'adresse email doit se terminer par `@gmail.com`
  - L'utilisateur doit avoir au moins **6 ans** (vérification de la date de naissance)
  - Correspondance des mots de passe
- **Réinitialisation du mot de passe** — Processus en 4 étapes :
  1. Saisie de l'adresse email sur l'interface "Mot de passe oublié ?"
  2. Génération d'un code aléatoire à 6 chiffres
  3. Envoi automatique du code par email via SMTP Gmail
  4. Saisie du code + définition du nouveau mot de passe avec confirmation

### Espace Utilisateur

- **Tableau de bord** — Menu latéral avec navigation vers toutes les sections (Événements, Événements Disponibles, Mes Événements, Compte Utilisateur)
- **Liste des événements** — Consultation de tous les événements avec leurs informations complètes (nom, date, lieu, capacité, places restantes, prix, description) et barre de recherche par nom
- **Événements disponibles & Inscription** — Bouton "S'inscrire" par événement ; confirmation instantanée via boîte de dialogue et envoi automatique d'un **email de confirmation** (Équipe Événements)
- **Mes Événements réservés** — Suivi des événements auxquels l'utilisateur est inscrit, avec bouton "Supprimer" pour se désinscrire et libérer une place
- **Mon Profil** — Formulaire pré-rempli avec les informations du compte connecté (nom, email, date de naissance, événement préféré, mot de passe) ; mise à jour synchronisée en base de données

### Espace Administrateur

- **Tableau de bord admin** — Menu latéral complet avec 9 sections : Utilisateurs, Ajouter Utilisateur, Événements, Événements Disponibles, Ajouter Événement, Inscription/Événement, Inscription, Statistiques, Compte Utilisateur
- **Gestion des utilisateurs** — Tableau complet avec nom, email, date de naissance, date d'inscription, nombre d'événements réservés et bouton "Supprimer" (suppression en cascade des inscriptions associées via `ON DELETE CASCADE`)
- **Ajouter un utilisateur** — Formulaire admin avec sélection du rôle (Administrateur / Utilisateur) et mêmes validations que l'inscription publique
- **Gestion des événements** — Liste complète avec colonnes Nom, Date, Lieu, Capacité, Places restantes, Prix, Description et bouton "Annuler" qui :
  - Supprime l'événement de la base de données
  - Envoie automatiquement un **email d'annulation** à chaque participant inscrit
  - Supprime toutes les inscriptions associées (libération des places)
- **Ajouter un événement** — Formulaire avec Nom, Lieu, Capacité, Description, Date et validations (champs requis, capacité numérique, unicité du nom)
- **Inscription/Événement** — Vue croisée des inscriptions par événement avec recherche par nom d'utilisateur ou d'événement
- **Inscription manuelle** — Formulaire admin avec deux menus déroulants (sélection utilisateur + sélection événement) et bouton "S'inscrire" avec envoi d'email de confirmation
- **Statistiques animées** — Graphiques circulaires (`FadeTransition`, `Timeline` JavaFX) affichant :
  - Pourcentage d'utilisateurs actifs / inactifs
  - Taux de réservation des événements
  - Compteurs totaux (nombre d'utilisateurs, nombre d'événements)
- **Mon Profil Admin** — Même formulaire de modification que l'espace utilisateur

---

## Architecture & Technologies

| Couche | Technologie | Rôle |
|--------|-------------|------|
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/java/java-original.svg" width="18"/> Langage principal | **Java 21** | Logique métier complète, robustesse et performances |
|  Interface graphique | **JavaFX** | UI moderne (boutons, tableaux, barres de recherche, animations) |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/mysql/mysql-original.svg" width="18"/> Base de données | **MySQL 9.1** | Stockage relationnel fiable (via phpMyAdmin) |
| <img src="https://cdn.jsdelivr.net/gh/devicons/devicon/icons/eclipse/eclipse-original.svg" width="18"/> IDE | **Eclipse IDE 2022-09** | Développement, gestion des packages et plugins |
|  Connecteur BDD | **JDBC** | Communication Java ↔ MySQL via classes DAO |
|  Email | **JavaMail API** | Envoi SMTP Gmail (confirmations, réinitialisations, annulations) |
|  Architecture | **MVC** | Séparation claire Modèle / Vue / Contrôleur |

**Patterns & bonnes pratiques :**
- **DAO Pattern** (Data Access Object) — Toute la logique d'accès aux données centralisée dans `application.dao`
- **PreparedStatement** — Protection contre les injections SQL
- **ON DELETE CASCADE** — Intégrité référentielle automatique (moteur InnoDB)
- **FadeTransition / Timeline** — Animations fluides sur les graphiques de statistiques
- **Session.java** — Gestion de la session utilisateur active entre les interfaces
- **Séparation des rôles** — Redirection automatique Administrateur / Utilisateur à la connexion

---

## Base de données

La base de données `gestionevenements` (MySQL 9.1, moteur **InnoDB**, charset `utf8mb4`) est composée de **5 tables** avec contraintes d'intégrité référentielle complètes.

```
┌──────────────────────────────┐        ┌──────────────────────────────────┐
│         utilisateurs         │        │           inscriptions            │
│──────────────────────────────│        │──────────────────────────────────│
│ id              INT (PK, AI) │───┐    │ id              INT (PK, AI)     │
│ nom             VARCHAR(100) │   │    │ utilisateur_id  INT (FK)   ◄─────┤
│ email           VARCHAR(100) │   └───►│ evenement_id    INT (FK)   ◄──┐  │
│ mot_de_passe    VARCHAR(255) │        │ date_inscription DATETIME       │  │
│ role_id         INT (FK)     │◄──┐    │ statut  ENUM('active','annulée')│  │
│ date_inscription DATE        │   │    └──────────────────────────────────┘  │
│ evenement_prefere VARCHAR(255)│  │              │                            │
│ date_naissance  DATE         │  │              │ ON DELETE CASCADE           │
└──────────────────────────────┘  │              ▼                            │
                                  │    ┌──────────────────────────────────┐   │
┌──────────────────────────────┐  │    │          confirmations            │   │
│            roles             │  │    │──────────────────────────────────│   │
│──────────────────────────────│  │    │ id              INT (PK, AI)     │   │
│ id              INT (PK, AI) │  │    │ inscription_id  INT (FK)         │   │
│ nom_role        VARCHAR(50)  │──┘    │ date_envoi      DATETIME         │   │
└──────────────────────────────┘       └──────────────────────────────────┘   │
  ON DELETE SET NULL                                                           │
                                       ┌──────────────────────────────────┐   │
                                       │           evenements              │   │
                                       │──────────────────────────────────│   │
                                       │ id          INT (PK, AI)         │───┘
                                       │ nom         VARCHAR(100)         │
                                       │ description TEXT                 │
                                       │ date        DATETIME             │
                                       │ lieu        VARCHAR(100)         │
                                       │ capacite    INT                  │
                                       │ prix        DECIMAL(10,2)        │
                                       └──────────────────────────────────┘
```

### Description des tables

| Table | Enregistrements | Description |
|-------|-----------------|-------------|
| `utilisateurs` | 9 (démo) | Comptes utilisateurs avec rôle, date de naissance, événement préféré. Email unique. |
| `roles` | 2 | `Administrateur` (id=1) et `Utilisateur` (id=2) |
| `evenements` | 6 (démo) | Catalogue des événements avec capacité, prix et description complète |
| `inscriptions` | 8 (démo) | Liaisons utilisateur ↔ événement, avec statut `active` ou `annulée` |
| `confirmations` | 16 (démo) | Horodatage des emails de confirmation envoyés par inscription |

### Contraintes d'intégrité

| Contrainte | Comportement |
|-----------|-------------|
| Suppression d'un **utilisateur** | Toutes ses inscriptions sont supprimées (`ON DELETE CASCADE`) |
| Suppression d'un **événement** | Toutes les inscriptions associées sont supprimées (`ON DELETE CASCADE`) |
| Suppression d'un **rôle** | Le `role_id` des utilisateurs concernés passe à `NULL` (`ON DELETE SET NULL`) |
| Suppression d'une **inscription** | La confirmation liée est supprimée (`ON DELETE CASCADE`) |

---

## Structure du projet

```
GestEvent/
│
└── src/
    │
    ├── application.dao/                    # Couche d'accès aux données (DAO Pattern)
    │   ├── EvenementDAO.java               # CRUD événements (ajout, liste, suppression, capacité)
    │   ├── InscriptionDAO.java             # CRUD inscriptions + vérif doublons
    │   ├── StatistiquesDAO.java            # Requêtes statistiques (actifs, inactifs, réservations)
    │   └── UtilisateurDAO.java             # CRUD utilisateurs (login, inscription, mise à jour)
    │
    ├── application.models/                 # Entités métier (Modèles)
    │   ├── Evenements.java                 # Entité Événement (id, nom, description, date, lieu, capacité, prix)
    │   ├── Inscriptions.java               # Entité Inscription (id, utilisateur_id, evenement_id, date, statut)
    │   ├── utilisateurs.java               # Entité Utilisateur (id, nom, email, mot_de_passe, role_id, …)
    │   ├── EvenementReserve.java           # Vue croisée : événement réservé par un utilisateur
    │   └── InscriptionDetails.java         # Vue enrichie : informations détaillées d'une inscription
    │
    ├── application.ui/                     # Interfaces graphiques JavaFX (Vue)
    │   │
    │   ├── — Authentification —
    │   ├── LoginInterface.java             # Connexion (email + mot de passe, redirection par rôle)
    │   ├── LoginApp.java                   # Point d'entrée de l'interface de connexion
    │   ├── AjoutUtilisateurInterface.java  # Création de compte avec validations
    │   ├── ResetPasswordInterface.java     # Réinitialisation du mot de passe (code par email)
    │   │
    │   ├── — Espace Utilisateur —
    │   ├── UserMainApp.java                # Tableau de bord utilisateur (menu latéral + zone dynamique)
    │   ├── EvenementInterface.java         # Liste de tous les événements (utilisateur)
    │   ├── InterfaceEvenementsDisponible.java  # Événements disponibles + bouton S'inscrire
    │   ├── InterfaceEvenementsDispo.java   # Variante de la liste des événements dispo
    │   ├── EvenementsUtilisateurInterface.java # Mes Événements réservés + bouton Supprimer
    │   ├── ProfilUtilisateurInterface.java # Mon Profil (consultation + modification)
    │   │
    │   ├── — Espace Administrateur —
    │   ├── MainApp.java                    # Tableau de bord administrateur (menu latéral + zone dynamique)
    │   ├── UtilisateurInterface.java       # Gestion des utilisateurs (liste + Supprimer)
    │   ├── AjoutEvenementInterface.java    # Formulaire ajout d'événement
    │   ├── EvenementInter.java             # Gestion des événements admin (liste + Annuler)
    │   ├── InscriptionEvenementInterface.java  # Vue croisée inscriptions/événements
    │   ├── InscriptionFormInterface.java   # Formulaire d'inscription manuelle (admin)
    │   ├── InscriptionInterface.java       # Liste des inscriptions (admin)
    │   └── StatistiquesInterface.java      # Tableau de bord statistiques avec graphiques animés
    │
    └── application.utils/                  # Utilitaires transversaux
        ├── DatabaseConnection.java         # Connexion JDBC centralisée à MySQL
        ├── EmailUtil.java                  # Envoi d'emails SMTP Gmail (confirmations, annulations, reset)
        └── Session.java                    # Gestion de la session utilisateur active
```

---

## Installation

### Prérequis

- [JDK 21](https://adoptium.net/) (ou supérieur)
- [JavaFX SDK](https://openjfx.io/) — téléchargeable sur gluonhq.com
- [MySQL 8+](https://dev.mysql.com/downloads/) avec phpMyAdmin ou MySQL Workbench
- [Eclipse IDE 2022-09](https://www.eclipse.org/downloads/) (ou tout IDE Java supportant JavaFX)
- Bibliothèques JAR requises :
  - `mysql-connector-java-x.x.x.jar`
  - `javax.mail.jar` (JavaMail API)
  - `javafx-sdk/lib/*.jar`

### Étapes

**1. Cloner le dépôt**
```bash
git clone https://github.com/kha-diija/gestevent.git
cd gestevent
```

**2. Importer le projet dans Eclipse**

```
File → Import → General → Existing Projects into Workspace
→ Sélectionner le dossier racine du projet → Finish
```

**3. Ajouter les bibliothèques au Build Path**

```
Clic droit sur le projet → Build Path → Configure Build Path
→ Libraries → Add External JARs
→ Sélectionner : javafx-sdk/lib/*.jar + mysql-connector-java.jar + javax.mail.jar
```

**4. Créer la base de données**

Dans phpMyAdmin ou MySQL Workbench, importer le fichier fourni :
```sql
SOURCE /chemin/vers/gestionevenements.sql;
```
Cela crée automatiquement la base `gestionevenements` avec toutes les tables et les données de démonstration.

**5. Configurer la connexion** *(voir section Configuration ci-dessous)*

**6. Configurer les VM Arguments JavaFX**

```
Clic droit sur le projet → Run As → Run Configurations → Arguments → VM Arguments
```
Ajouter :
```
--module-path /chemin/absolu/vers/javafx-sdk/lib --add-modules javafx.controls,javafx.fxml
```

**7. Lancer l'application**

Exécuter la classe `LoginInterface.java` ou `LoginApp.java` en tant qu'application Java.

---

## Configuration

### Connexion à la base de données

Modifier `src/application/utils/DatabaseConnection.java` :

```java
private static final String URL      = "jdbc:mysql://localhost:3306/gestionevenements";
private static final String USER     = "votre_utilisateur_mysql";   // ex : root
private static final String PASSWORD = "votre_mot_de_passe_mysql";
```

### Configuration email (JavaMail / Gmail SMTP)

Modifier `src/application/utils/EmailUtil.java` :

```java
private static final String EMAIL_EXPEDITEUR  = "votre.email@gmail.com";
private static final String MOT_DE_PASSE_APP  = "votre_mot_de_passe_application";
private static final String SMTP_HOST         = "smtp.gmail.com";
private static final String SMTP_PORT         = "587";
```

> ⚠️ **Important :** Pour Gmail, activez l'authentification à 2 facteurs sur votre compte Google et générez un [mot de passe d'application](https://myaccount.google.com/apppasswords) dédié. N'utilisez jamais votre mot de passe principal.

> ℹ️ **Note :** Le champ `email` dans la table `utilisateurs` accepte uniquement les adresses se terminant par `@gmail.com` (validation côté applicatif).

---

## Utilisation

### Créer un compte utilisateur

Depuis l'écran de connexion, cliquer sur **"Créer un compte"** et renseigner :
- Nom complet
- Adresse email (`@gmail.com` obligatoire)
- Mot de passe + confirmation
- Date de naissance (doit avoir au moins 6 ans)
- Événement préféré

### Comptes de démonstration (base SQL incluse)

| Nom | Email | Mot de passe | Rôle |
|-----|-------|-------------|------|
| chaimae | `chaimae8@gmail.com` | `1234` | **Administrateur** |
| ikram | `ikram@gmail.com` | `1234` | **Administrateur** |
| khadija | `khadijakartouch8@gmail.com` | `azer` | Utilisateur |
| khalid | `khalid@gmail.com` | `1234` | Utilisateur |
| salma | `salma@gmail.com` | `1234` | Utilisateur |

> ⚠️ Ces mots de passe sont stockés en clair dans la version de démonstration. En production, il est recommandé d'utiliser **BCrypt** pour le hachage.

---

## 🖼️ Interfaces graphiques

### Connexion — `LoginInterface`

<img width="560" height="362" alt="image" src="https://github.com/user-attachments/assets/c7af2d29-d41a-428d-9174-b1cdbea9f19a" />


<p align="center">
  <img width="500" src="" alt="Fenêtre de connexion — formulaire vide"/>
  <br/><em>Formulaire de connexion avec fond photo de salle d'événements, champs Email/Mot de passe et liens de navigation</em>
</p>

<img width="497" height="278" alt="image" src="https://github.com/user-attachments/assets/a2add7f5-71a6-43cb-8015-47dbfe9ba2c2" />

<p align="center">
  <img width="500" src="" alt="Fenêtre de connexion — identifiants saisis"/>
  <br/><em>Saisie des identifiants avant connexion — redirection automatique selon le rôle (Admin ou Utilisateur)</em>
</p>

---

### Création de compte — `AjoutUtilisateurInterface`
<img width="450" height="236" alt="image" src="https://github.com/user-attachments/assets/891df211-c4fc-49a6-a975-1b69b5a8a49b" />

<p align="center">
  <img width="500" src="" alt="Formulaire de création de compte — vide"/>
  <br/><em>Formulaire de création de compte avec 5 champs : Nom, Email, Mot de passe, Date de naissance, Événement préféré</em>
</p>
<img width="212" height="233" alt="image" src="https://github.com/user-attachments/assets/f92d7478-f015-4fed-bc0b-db33e2b192a2" />

<p align="center">
  <img width="350" src="" alt="Validation — champs obligatoires non remplis"/>
  <br/><em>Erreur : tous les champs sont obligatoires</em>
</p>
<img width="233" height="258" alt="image" src="https://github.com/user-attachments/assets/b84e8b55-8fdd-48e1-a99c-0cd77d0ad11b" />

<p align="center">
  <img width="350" src="" alt="Validation — format email invalide"/>
  <br/><em>Erreur : l'adresse email doit obligatoirement se terminer par @gmail.com</em>
</p>

<img width="217" height="240" alt="image" src="https://github.com/user-attachments/assets/8d6f2d5d-0c17-4b53-bcc1-6923fe57a7ad" />

<p align="center">
  <img width="350" src="" alt="Validation — âge minimum non respecté"/>
  <br/><em>Erreur : l'utilisateur doit avoir au moins 6 ans (vérification sur la date de naissance)</em>
</p>
<img width="491" height="278" alt="image" src="https://github.com/user-attachments/assets/0f22714e-f5b1-4a0e-9651-ae6afdbb5265" />

---

### Réinitialisation du mot de passe — `ResetPasswordInterface`
<img width="467" height="259" alt="image" src="https://github.com/user-attachments/assets/26adf037-f66e-4b2b-a380-01e1f7e7205e" />

<p align="center">
  <img width="500" src="" alt="Déclenchement — lien Mot de passe oublié"/>
  <br/><em>Étape 1 — Cliquer sur "Mot de passe oublié ?" depuis la page de connexion</em>
</p>
<img width="260" height="110" alt="image" src="https://github.com/user-attachments/assets/09c6d334-0eb1-4053-91ed-af0c3f3c2762" />

<p align="center">
  <img width="500" src="" alt="Email de réinitialisation reçu dans Gmail"/>
  <br/><em>Étape 2 — Email automatique reçu avec le code de réinitialisation à 6 chiffres (SMTP Gmail)</em>
</p>
<img width="273" height="226" alt="image" src="https://github.com/user-attachments/assets/773d0b61-2a1a-4041-b504-910574be7bb9" />

<p align="center">
  <img width="350" src="" alt="Réinitialisation — code invalide"/>
  <br/><em>Étape 3a — Message d'erreur si le code saisi ne correspond pas à celui envoyé</em>
</p>
<img width="245" height="225" alt="image" src="https://github.com/user-attachments/assets/ebb999a6-6e06-4a12-8211-3a14c4e84601" />

<p align="center">
  <img width="350" src="" alt="Réinitialisation — mots de passe non concordants"/>
  <br/><em>Étape 3b — Erreur si le nouveau mot de passe et sa confirmation ne correspondent pas</em>
</p>
<img width="253" height="197" alt="image" src="https://github.com/user-attachments/assets/e444b7bc-a23e-4e04-97e0-2301a8bae682" />

<p align="center">
  <img width="350" src="" alt="Réinitialisation — succès"/>
  <br/><em>Étape 4 — Confirmation de la réinitialisation réussie ; l'utilisateur peut se reconnecter</em>
</p>

---

### Espace Utilisateur — Tableau de bord (`UserMainApp`)
<img width="467" height="253" alt="image" src="https://github.com/user-attachments/assets/4802359a-592f-4ff5-8ed0-2a2cbf9d23b8" />

<p align="center">
  <img width="600" src="" alt="Tableau de bord utilisateur"/>
  <br/><em>Tableau de bord utilisateur — menu latéral avec navigation et zone principale dynamique au lancement</em>
</p>

---

### Liste des Événements (Utilisateur) — `EvenementInterface`
<img width="473" height="254" alt="image" src="https://github.com/user-attachments/assets/5f914aef-f289-43e1-a72e-23e0dcc8dc12" />

<p align="center">
  <img width="600" src="" alt="Liste complète des événements — vue utilisateur"/>
  <br/><em>Liste complète des événements avec toutes leurs informations — vue lecture seule côté utilisateur</em>
</p>

---

### Événements Disponibles & Inscription — `InterfaceEvenementsDisponible`
<img width="469" height="257" alt="image" src="https://github.com/user-attachments/assets/7a313d87-b305-4403-9231-4a334cc96cc2" />

<p align="center">
  <img width="600" src="" alt="Événements disponibles avec boutons S'inscrire et statut Inscrit"/>
  <br/><em>Liste des événements disponibles — bouton "S'inscrire" ou statut "Inscrit" selon l'état de l'utilisateur</em>
</p>
<img width="468" height="262" alt="image" src="https://github.com/user-attachments/assets/4eb1cf1d-c147-4cf8-877f-5ed6993e24d5" />
<img width="575" height="214" alt="image" src="https://github.com/user-attachments/assets/a36ac7b0-8e0f-4370-99a5-1d83d937a50f" />

<p align="center">
  <img width="500" src="" alt="Confirmation d'inscription réussie"/>
  <br/><em>Confirmation instantanée après inscription — la place est décomptée en temps réel</em>
</p>
<img width="662" height="264" alt="image" src="https://github.com/user-attachments/assets/e820d3e5-64ca-41f0-8fab-ff1b35e8c37d" />

<p align="center">
  <img width="500" src="" alt="Email de confirmation d'inscription reçu dans Gmail"/>
  <br/><em>Email de confirmation automatique envoyé par "Équipe Événements" après chaque inscription réussie</em>
</p>

---

### Mes Événements Réservés — `EvenementsUtilisateurInterface`
<img width="446" height="248" alt="image" src="https://github.com/user-attachments/assets/3c41726c-af42-4e90-a2fc-d67d37a7813f" />

<p align="center">
  <img width="600" src="" alt="Mes événements réservés avec bouton Supprimer"/>
  <br/><em>Liste des événements auxquels l'utilisateur est inscrit — bouton "Supprimer" pour se désinscrire et libérer une place</em>
</p>

---

### Mon Profil (Utilisateur) — `ProfilUtilisateurInterface`
<img width="451" height="252" alt="image" src="https://github.com/user-attachments/assets/1e32e992-cf73-4b8d-92ba-29cf88282327" />

<p align="center">
  <img width="500" src="" alt="Formulaire Mon Profil — espace utilisateur"/>
  <br/><em>Formulaire de profil pré-rempli avec les données du compte connecté — modifications synchronisées en base</em>
</p>

---

### Espace Administrateur — Tableau de bord (`MainApp`)
<img width="446" height="242" alt="image" src="https://github.com/user-attachments/assets/6f738c55-9b06-41d4-97e5-f0f89bfc35a6" />

<p align="center">
  <img width="600" src="" alt="Tableau de bord administrateur"/>
  <br/><em>Tableau de bord administrateur — menu latéral complet avec 9 sections de gestion</em>
</p>

---

### Gestion des Utilisateurs (Admin) — `UtilisateurInterface`
<img width="455" height="235" alt="image" src="https://github.com/user-attachments/assets/53cf27d3-45b0-4996-a18f-940c9a84569a" />

<p align="center">
  <img width="700" src="" alt="Gestion des utilisateurs — vue administrateur"/>
  <br/><em>Liste complète des utilisateurs avec informations détaillées — suppression en cascade des inscriptions associées</em>
</p>

---

### Ajouter un Utilisateur (Admin) — `AjoutUtilisateurInterface`
<img width="449" height="238" alt="image" src="https://github.com/user-attachments/assets/585d70e4-6d61-4814-97ed-481a65a0998c" />

<p align="center">
  <img width="500" src="" alt="Formulaire Ajouter un utilisateur — vue administrateur"/>
  <br/><em>Formulaire admin d'ajout d'utilisateur avec sélection du rôle — mêmes validations que l'inscription publique</em>
</p>

---

### Gestion des Événements (Admin) — `EvenementInter`
<img width="439" height="241" alt="image" src="https://github.com/user-attachments/assets/ae3a1cb5-33b4-445b-b12d-4f1d74067295" />

<p align="center">
  <img width="700" src="" alt="Gestion des événements — vue administrateur"/>
  <br/><em>Liste admin des événements avec bouton "Annuler" — déclenche suppression + email d'annulation automatique à tous les inscrits</em>
</p>
<img width="356" height="134" alt="image" src="https://github.com/user-attachments/assets/06a733c0-ff47-4aaf-a5b5-1d7d3eda54ea" />

<p align="center">
  <img width="500" src="" alt="Email d'annulation automatique reçu par les participants"/>
  <br/><em>Email d'annulation envoyé automatiquement à chaque participant inscrit lors de la suppression d'un événement</em>
</p>

---

### Ajouter un Événement (Admin) — `AjoutEvenementInterface`
<img width="525" height="286" alt="image" src="https://github.com/user-attachments/assets/8094671b-8fc0-4d33-b060-355b631a0f82" />

<p align="center">
  <img width="500" src="" alt="Formulaire Ajouter un événement — vue administrateur"/>
  <br/><em>Formulaire d'ajout d'événement avec validation : champs requis, capacité numérique, unicité du nom</em>
</p>

---

### Événements Disponibles (Admin) — `InterfaceEvenementsDispo`
<img width="524" height="281" alt="image" src="https://github.com/user-attachments/assets/9762eef9-072a-47bc-8224-41776fd8ef17" />

<p align="center">
  <img width="700" src="" alt="Événements disponibles — vue administrateur"/>
  <br/><em>Vue admin des événements disponibles avec suivi du nombre de places restantes en temps réel</em>
</p>

---

### Inscriptions aux Événements (Admin) — `InscriptionEvenementInterface`
<img width="524" height="276" alt="image" src="https://github.com/user-attachments/assets/dd52cef9-7b06-4941-94fb-e76d9dd7ed55" />

<p align="center">
  <img width="600" src="" alt="Vue croisée Inscriptions / Événements"/>
  <br/><em>Vue croisée de toutes les inscriptions avec recherche par nom d'utilisateur ou d'événement</em>
</p>

---

### Inscription Manuelle (Admin) — `InscriptionFormInterface`
<img width="525" height="282" alt="image" src="https://github.com/user-attachments/assets/d4fc991c-2308-4614-8cde-8ae2d8471305" />

<p align="center">
  <img width="500" src="" alt="Formulaire d'inscription manuelle par l'administrateur"/>
  <br/><em>L'administrateur peut inscrire manuellement un utilisateur à un événement — email de confirmation envoyé automatiquement</em>
</p>

---

### Statistiques Animées (Admin) — `StatistiquesInterface`
<img width="527" height="280" alt="image" src="https://github.com/user-attachments/assets/2fd2c262-a12e-496f-8eff-bccfd3ea5fda" />

<p align="center">
  <img width="600" src="" alt="Tableau de bord des statistiques avec graphiques animés"/>
  <br/><em>Graphiques circulaires animés (FadeTransition + Timeline JavaFX) : taux d'activité et de réservation en temps réel</em>
</p>

---

### Mon Profil (Admin) — `ProfilUtilisateurInterface`
<img width="526" height="277" alt="image" src="https://github.com/user-attachments/assets/f44c2f66-a2af-4423-abc6-cf3e139a6c9b" />

<p align="center">
  <img width="500" src="" alt="Formulaire Mon Profil — espace administrateur"/>
  <br/><em>Formulaire de profil administrateur — mêmes fonctionnalités de modification que l'espace utilisateur</em>
</p>

---

## Difficultés rencontrées

| # | Difficulté | Solution apportée |
|---|-----------|------------------|
| 1 | **Connexion JavaFX ↔ MySQL** — Configuration correcte de la connexion et gestion des erreurs | Création d'une classe `DatabaseConnection` centralisée avec gestion propre des exceptions via try-catch |
| 2 | **Sécurisation du login** — Protection des identifiants et sécurisation des requêtes SQL | Utilisation de `PreparedStatement` pour prévenir les injections SQL ; séparation des rôles Utilisateur/Admin |
| 3 | **Animations JavaFX** — Intégration d'animations fluides sans ralentissement | Utilisation de `FadeTransition`, `Timeline` et autres classes d'animation JavaFX sur les graphiques de statistiques |
| 4 | **Intégrité référentielle** — Définition correcte des clés étrangères et des relations en cascade | Utilisation du moteur **InnoDB**, création des clés étrangères avec `ON DELETE CASCADE` et `ON DELETE SET NULL` |

---

## Documents du projet

| Document | Description |
|----------|-------------|
| [`rapportProjetJava.pdf`](./rapportProjetJava.pdf) | Rapport technique complet : problématique, technologies, architecture MVC, structure des packages, base de données, fonctionnement détaillé de chaque interface |
| [`gestionevenements.sql`](./gestionevenements.sql) | Script SQL complet : création de la base, des tables, des contraintes et insertion des données de démonstration |

---

## Auteure

Projet réalisé dans le cadre d'un projet académique à l'**ENSA Khouribga** — Université Sultan Moulay Slimane.

- **Filière :** Génie Informatique, 1ère année
- **Année académique :** 2024 – 2025
- **Encadré par :** Pr. Sara Baghdadi
- **Date de soumission :** 29/04/2025
- **GitHub :** [@kha-diija](https://github.com/kha-diija)

---

<div align="center">
  <sub>Fait avec ❤️ — GestEvent &copy; 2024/2025 — Khadija EL KARTOUCH</sub>
</div>
