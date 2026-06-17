# DOSSIER DE CONCEPTION DÉTAILLÉE (DCD)
**Système :** Système de Gestion d'Accès (SGA) — Lot 1 : Gestion des Accès  
**Phase :** Phase 2 — Conception détaillée et Codage  
**Binôme rédacteur :** Binôme 2 (Phase 2)  

---

## 1. Architecture Logicielle (3-Tiers)
L'application SGA Lot 1 est structurée selon une architecture 3-tiers découplée :

```
┌────────────────────────────────────────────────────────┐
│                   Couche 1 : IHM                      │
│   (sga.ihm - FrmConnexion, FrmListeAcces, etc.)       │
└──────────────────────────┬─────────────────────────────┘
                           │ Appels de services
                           ▼
┌────────────────────────────────────────────────────────┐
│                 Couche 2 : Service                     │
│    (sga.service - TypeAccesService, AccesService)      │
└──────────────────────────┬─────────────────────────────┘
                           │ Appels DAO (Interfaces)
                           ▼
┌────────────────────────────────────────────────────────┐
│                   Couche 3 : DAO                       │
│    (sga.dao - ITypeAccesDAO, IAccesDAO, JDBC/Simu)     │
└────────────────────────────────────────────────────────┘
```

---

## 2. Diagramme de Classes Complet
Voici le diagramme UML des classes métiers et d'accès aux données implémentées pour le Lot 1 :

```mermaid
classDiagram
    class Personne {
        -int idPersonne
        -String nom
        -String prenom
        -String fonction
        -String login
        -String motDePasse
        -String statut
        -int idProfil
        +getIdPersonne() int
        +getNom() String
        +getPrenom() String
        +getLogin() String
        +getStatut() String
    }

    class Lieu {
        -int idLieu
        -String libelleLieu
        -String emplacement
        -int capaciteMax
        -String statut
        +getIdLieu() int
        +getLibelleLieu() String
    }

    class TypeAcces {
        -int idTypeAcces
        -String libelle
        -String description
        -String statut
        -Date dateCreation
        +getIdTypeAcces() int
        +getLibelle() String
        +getDescription() String
    }

    class Acces {
        -int idAcces
        -String libelle
        -String description
        -int idTypeAcces
        -String typeLibelle
        -int idLieu
        -String lieuLibelle
        -String statut
        -Date dateCreation
        -Date dateModification
        +getIdAcces() int
        +getLibelle() String
        +getIdTypeAcces() int
        +getIdLieu() int
    }

    class Badge {
        -String numBadge
        -Date dateEmission
        -Date dateExpiration
        -String statut
        -int idPersonne
        -int idLieu
    }

    class ITypeAccesDAO {
        <<interface>>
        +listerActifs() List~TypeAcces~
        +creer(TypeAcces) boolean
        +modifier(TypeAcces) boolean
        +desactiver(int) boolean
        +verifierDoublon(String) boolean
        +verifierUsage(int) boolean
        +rechercherParLibelle(String) List~TypeAcces~
    }

    class IAccesDAO {
        <<interface>>
        +listerActifs() List~Acces~
        +creer(Acces) boolean
        +modifier(Acces) boolean
        +desactiver(int) boolean
        +recupererParId(int) Acces
        +rechercherMulticritere(String, int, int) List~Acces~
    }

    TypeAccesDAOImpl ..|> ITypeAccesDAO
    AccesDAOImpl ..|> IAccesDAO

    TypeAccesService --> ITypeAccesDAO
    AccesService --> IAccesDAO
```

---

## 3. Diagrammes de Séquence des Scénarios Clés

### 3.1 Connexion Utilisateur (SHA-256 local et validation Oracle)
```mermaid
sequenceDiagram
    actor U as Utilisateur (Admin)
    participant F as FrmConnexion
    participant H as HashUtil
    participant D as PersonneDAOImpl
    participant DB as Base Oracle / SimulationData

    U->>F: Saisit Login / Password + clique sur "Se connecter"
    F->>H: hashSHA256(password)
    H-->>F: Retourne hash hex (64 car)
    F->>D: authentifier(login, hash)
    D->>DB: SELECT ... WHERE login=? AND mot_de_passe=? AND statut='ACTIF'
    DB-->>D: Données utilisateur (id, nom, prenom, profil)
    D-->>F: Objet Personne ou null
    alt Authentification réussie
        F->>F: Ouvrir FrmMenuPrincipal
        F->>F: Fermer FrmConnexion
    else Authentification échouée
        F->>F: Afficher message d'erreur
    end
```

### 3.2 Ajout d'un point d'accès avec validations métier (CF-01, CF-05)
```mermaid
sequenceDiagram
    actor A as Administrateur
    participant I as FrmFormulaireAcces
    participant S as AccesService
    participant D as AccesDAOImpl
    participant DB as Base de données / SimulationData

    A->>I: Saisit libellé, sélectionne Lieu et Type + clique "Enregistrer"
    alt Un champ obligatoire est vide
        I->>I: Met la bordure du champ en rouge (IHM-06)
        I->>A: Pop-up d'avertissement "Remplir champs requis"
    else Formulaire visuellement valide
        I->>S: creer(nouveauAcces)
        S->>S: validerAcces(nouveauAcces) (Contrôle CF-01 et CF-05)
        alt Données invalides (ex: idLieu <= 0)
            S-->>I: Lève Exception Métier
            I->>A: Affiche pop-up d'erreur
        else Données valides
            S->>D: creer(nouveauAcces)
            D->>DB: INSERT INTO ACCES (libelle, id_type, id_lieu, statut, date_creation)
            DB-->>D: Ligne insérée
            D-->>S: true
            S-->>I: true
            I->>I: Rafraîchir tableau parent FrmListeAcces
            I->>I: Fermer dialogue formulaire
        end
    end
```

### 3.3 Désactivation logique d'un Type d'Accès avec blocage de sécurité (CF-04)
```mermaid
sequenceDiagram
    actor A as Administrateur
    participant L as FrmListeTypeAcces
    participant S as TypeAccesService
    participant D as TypeAccesDAOImpl
    participant DB as Base de données / SimulationData

    A->>L: Sélectionne un type d'accès + clique sur "Désactiver"
    L->>S: desactiver(idTypeAcces)
    S->>D: verifierUsage(idTypeAcces)
    D->>DB: SELECT COUNT(*) FROM ACCES WHERE id_type_acces=? AND statut='A'
    DB-->>D: Nombre de points d'accès liés actifs
    D-->>S: true (si count > 0)
    alt Type utilisé
        S-->>L: Exception ("Désactivation impossible...")
        L->>A: Affiche message d'erreur rouge "Désactivation impossible..."
    else Type non utilisé
        S->>D: desactiver(idTypeAcces)
        D->>DB: UPDATE TYPE_ACCES SET statut='I' WHERE id_type_acces=?
        DB-->>D: Succès
        D-->>S: true
        S-->>L: true
        L->>L: Rafraîchir la table des types d'accès
    end
```

---

## 4. Diagrammes d'Activité

### 4.1 Recherche Multicritère des Points d'Accès
```mermaid
stateDiagram-v2
    [*] --> SaisieFiltres : Saisie dans FrmListeAcces (Texte, Lieu, Type)
    SaisieFiltres --> ClicRechercher : Clic sur bouton "Rechercher"
    ClicRechercher --> AppelService : Appel de rechercherMulticritere()
    AppelService --> DynamicSQL : Construction dynamique de la requête SQL
    state DynamicSQL {
        [*] --> CheckText : Libellé présent ?
        CheckText --> AppendText : OUI -> Ajoute AND UPPER(libelle) LIKE ?
        CheckText --> CheckLieu : NON
        AppendText --> CheckLieu
        CheckLieu --> AppendLieu : OUI (id > 0) -> Ajoute AND id_lieu = ?
        CheckLieu --> CheckType : NON
        AppendLieu --> CheckType
        CheckType --> AppendType : OUI (id > 0) -> Ajoute AND id_type_acces = ?
        CheckType --> [*] : NON
        AppendType --> [*]
    }
    DynamicSQL --> ExecutionQuery : Exécution Prepared Statement (Oracle ou Simulation)
    ExecutionQuery --> RemplissageTableau : Remplissage du JTable dans l'IHM
    RemplissageTableau --> [*]
```

---

## 5. Justifications de Conception Technique (Questions de Soutenance)

### 5.1 Choix des Formes Normales (3FN)
Les tables `PERSONNE`, `LIEU`, `TYPE_ACCES`, `ACCES` et `BADGE` sont en 3ème Forme Normale (3FN) :
1.  **1FN (Atomicité des attributs) :** Chaque champ contient une valeur atomique (ex: nom, prenom, libelle sont uniques et non décomposables).
2.  **2FN (Dépendance fonctionnelle complète) :** Toutes les colonnes non clés dépendent entièrement de la clé primaire de leur table. Par exemple, la description d'un type d'accès dépend uniquement de `id_type_acces`.
3.  **3FN (Absence de dépendance transitive) :** Aucun attribut non clé ne dépend d'un autre attribut non clé. Le lieu de l'accès n'est pas stocké dans la table `ACCES` directement sous forme de libellé/adresse, mais via une référence `id_lieu`. Toute modification de la capacité ou de l'adresse d'un lieu n'impacte donc pas la table `ACCES`.

### 5.2 Pourquoi un Trigger plutôt qu'un CHECK pour `date_modification` ?
Une contrainte `CHECK` sert uniquement à valider qu'une donnée respecte une condition logique (ex: `statut IN ('A', 'I')`). Elle ne permet pas de modifier la valeur d'une colonne.
Le trigger `TRG_ACCES_MODIF` est un déclencheur événementiel `BEFORE UPDATE ON ACCES`. Il intercepte la transaction d'écriture sur Oracle pour injecter la date actuelle (`SYSDATE`) dans la colonne `date_modification` de manière automatique. Cela assure le respect de la règle **CF-06** de façon centralisée au niveau de la BDD, quel que soit l'outil client (Java, SQL Developer, script externe).

### 5.3 Flux de Connexion Sécurisée
1.  **Saisie :** L'utilisateur entre son mot de passe en clair dans un `JPasswordField` Swing.
2.  **Hachage Client :** Dès la soumission, l'application exécute localement `HashUtil.hashSHA256(password)`. Le mot de passe en clair est immédiatement détruit en mémoire.
3.  **Requête SQL Paramétrée :** L'application envoie uniquement le hash hexadécimal à Oracle dans une requête JDBC préparée (`PreparedStatement`) :
    `SELECT ... WHERE login=? AND mot_de_passe=? AND statut='ACTIF'`
4.  **Avantage :** Le mot de passe en clair ne circule jamais sur le réseau et n'est jamais visible en base de données.
