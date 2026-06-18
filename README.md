# Marc-Roland
# Marc-Roland — SGA Lot 1 : Gestion des Accès

Système de Gestion d'Accès (SGA) développé pour le **CCI-BF / ISGE-BF** (Institut Supérieur de Génie Électrique du Burkina Faso).

Ce dépôt couvre le **Lot 1 — Gestion des Accès**, repris en **Phase 2** (conception détaillée, codage, tests) sur la base des livrables de Phase 1.

## Contexte du projet

| | |
|---|---|
| **Client** | CCI-BF / ISGE-BF |
| **Système** | Système de Gestion d'Accès (SGA) |
| **Lot** | Lot 1 — Gestion des Accès |
| **Enseignant** | M. NAGALO Alexis |
| **Langage** | Java 8 |
| **IDE** | Eclipse + WindowBuilder |
| **SGBD** | Oracle Express (XE) + SQL Developer |
| **Modélisation** | Modelio |
| **VCS** | Git + GitHub |

## Architecture

Application 3-tiers :

```
sga.ihm        → Interfaces graphiques (Swing / WindowBuilder)
sga.service    → Logique métier, validation, orchestration
sga.metier     → Classes POJO (Personne, Acces, TypeAcces, Lieu, Badge)
sga.dao        → Accès aux données (PreparedStatement, try/catch/commit)
sga.util       → ConnexionBD, HashUtil (SHA-256), Constantes
sga.main       → Point d'entrée de l'application
```

**Règle de conception :** aucune logique SQL dans la couche IHM ; toutes les requêtes passent par les DAO en `PreparedStatement`.

## Modules fonctionnels

- **Module A — Types d'Accès** : création, modification, désactivation logique, recherche par libellé (`PORTE`, `BARRIERE`, `PORTILLON`, `PARKING`, `ASCENSEUR`).
- **Module B — Points d'Accès** : gestion des points d'accès liés à un lieu et un type, recherche multicritère (libellé / type / lieu), fiche détail.
- **Authentification** : connexion par login/mot de passe haché en SHA-256 (CF-07), avec un mode Simulation (en mémoire) basculable si la base Oracle est indisponible.

## Règles métier principales

| Règle | Description |
|---|---|
| CF-01 | Un point d'accès appartient à un seul lieu (`id_lieu` obligatoire) |
| CF-02 | Aucune suppression physique — uniquement désactivation logique (`statut = 'I'`) |
| CF-03 | Libellé de type d'accès unique (contrainte SQL + contrôle applicatif) |
| CF-04 | Un type d'accès utilisé par des points actifs ne peut pas être désactivé |
| CF-05 | Validation de tous les champs obligatoires avant tout envoi SQL |
| CF-06 | Horodatage de modification automatique par trigger Oracle (`TRG_ACCES_MODIF`) |
| CF-07 | Mot de passe haché en SHA-256 côté Java, jamais stocké en clair |

## Prérequis

- JDK 8
- Oracle Database Express Edition (XE)
- SQL Developer (ou équivalent)
- Eclipse IDE avec le plugin WindowBuilder
- Driver JDBC Oracle (`ojdbc8.jar` ou `ojdbc11.jar`) ajouté au build path du projet

## Installation et configuration de la base

1. Créer un utilisateur Oracle dédié à l'application (ne **pas** utiliser `SYS` ou `SYSTEM`) :
   ```sql
   CREATE USER sga_user IDENTIFIED BY "votre_mot_de_passe";
   GRANT CONNECT, RESOURCE TO sga_user;
   GRANT CREATE SESSION, CREATE TABLE, CREATE VIEW, CREATE TRIGGER, CREATE SEQUENCE TO sga_user;
   ALTER USER sga_user QUOTA UNLIMITED ON USERS;
   ```
2. Se connecter avec cet utilisateur et exécuter le script `sql/schema.sql` (création des tables, séquences, triggers, vue, et jeu de données de test).
3. Renseigner les identifiants de connexion dans `src/sga/util/Constantes.java` :
   ```java
   public static final String DB_URL = "jdbc:oracle:thin:@localhost:1521/XEPDB1";
   public static final String DB_USER = "sga_user";
   public static final String DB_PASS = "votre_mot_de_passe";
   ```

> **Remarque :** sur Oracle XE 18c+, le format de connexion utilise un nom de service (`/XEPDB1`) et non un SID (`:XE`).

## Lancer l'application

Exécuter `sga.main.Main` depuis Eclipse, ou via la ligne de commande après compilation.

Si la connexion à Oracle échoue, l'application bascule automatiquement en **mode Simulation** (données en mémoire), permettant de continuer les tests sans base de données disponible.

## Comptes de test

| Login | Mot de passe | Profil | Statut |
|---|---|---|---|
| `admin` | `admin` | Administrateur | Actif |
| `agent` | `agent` | Agent Sécurité | Actif |
| `dsi` | `dsi` | DSI | Actif |
| `traore.s` | `traore.s` | Étudiant | Actif |
| `kabore.f` | `kabore.f` | Étudiante | Actif |
| `sow.i` | `sow.i` | Enseignant | Actif |
| `zongo.m` | `zongo.m` | Secrétaire | Actif |
| `compaore.p` | `compaore.p` | Technicien | Actif |
| `diarra.a` | `agent` | Agent Sécurité (nuit) | Actif |
| `sawadogo.a` | `sawadogo.a` | Stagiaire | Inactif (refus volontaire, à des fins de test) |

## Structure du dépôt

```
sga-lot1/
├── src/
│   └── sga/
│       ├── main/      → Point d'entrée
│       ├── ihm/        → Écrans Swing (FrmConnexion, FrmMenuPrincipal, ...)
│       ├── service/    → Logique métier (TypeAccesService, AccesService)
│       ├── metier/     → Entités (Personne, Acces, TypeAcces, Lieu, Badge)
│       ├── dao/        → Accès aux données (interfaces + implémentations)
│       └── util/       → ConnexionBD, HashUtil, Constantes
├── sql/
│   └── schema.sql      → Script de création + jeu de données de test
└── README.md
```

## Branches

- `main` — version stable / livrable
- `dev` — branche de développement courant

## Auteur

Marc-Roland SAWADOGO — Binôme 2, Phase 2 du projet SGA Lot 1 (ISGE-BF).

---

*Projet académique réalisé dans le cadre du cursus de l'Institut Supérieur de Génie Électrique du Burkina Faso (ISGE-BF).*