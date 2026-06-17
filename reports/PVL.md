# PLAN DE VALIDATION LOGICIELLE (PVL) - LOT 1
**Système :** Système de Gestion d'Accès (SGA) — Lot 1 : Gestion des Accès  
**Phase :** Phase 2 — Livraison détaillée  
**Binôme rédacteur :** Binôme 2 (Phase 2)  
**Date d'exécution :** 16 Juin 2026  

---

## 1. Matrice de Traçabilité (Exigences vs Tests)
Cette matrice permet de s'assurer que 100% des exigences et des règles métiers sont couvertes par au moins un test unitaire, d'intégration ou manuel.

| Identifiant Règle | Description de la règle | Statut de validation | Couverture par Test | Réf. Test / IHM | Remarque |
| :--- | :--- | :---: | :--- | :--- | :--- |
| **CF-01** | Un accès n'appartient qu'à un seul lieu (id_lieu NOT NULL) | **Conforme** | Test unitaire + IHM | T-ACC-03 / FrmFormulaireAcces | Validation bloquante si idLieu est absent |
| **CF-02** | Aucune suppression physique — uniquement statut='I' | **Conforme** | Test d'intégration | T-TYPE-06, T-ACC-05 | Vérification que le statut passe à 'I' en SQL/Mémoire |
| **CF-03** | Libellé type d'accès UNIQUE (insensible à la casse) | **Conforme** | Test d'intégration | T-TYPE-03 | Rejet de la création de libellés identiques (casse ignorée) |
| **CF-04** | Type d'accès utilisé par accès actifs -> désactivation interdite | **Conforme** | Test d'intégration | T-TYPE-05 | Exception levée si tentative de désactivation de "PORTE" |
| **CF-05** | Validation de tous les champs obligatoires avant envoi SQL | **Conforme** | Test unitaire + IHM | T-ACC-03, T-TYPE-03 | Les services lancent les exceptions avant l'appel DAO |
| **CF-06** | Horodatage automatique par trigger Oracle (pas par l'appli) | **Conforme** | Base de données SQL | Triggers SQL + Simulation | Géré par `TRG_ACCES_MODIF` en Oracle, simulé en Java |
| **CF-07** | Mot de passe hashé SHA-256 côté Java (jamais en clair) | **Conforme** | Cryptographie locale | T-AUTH-01, T-AUTH-02 | Géré par `HashUtil.hashSHA256` dans FrmConnexion |

---

## 2. Validation des Écrans IHM
Conformément aux exigences ergonomiques :

| Réf. Écran | Nom de la classe Swing | Description fonctionnelle | Statut | Commentaire de validation |
| :--- | :--- | :--- | :---: | :--- |
| **IHM-01** | `FrmConnexion.java` | Authentification avec mot de passe masqué, choix du mode Simulation/Oracle, logo textuel ISGE-BF. | **PASS** | Entièrement conforme. Affichage dynamique des erreurs. |
| **IHM-02** | `FrmMenuPrincipal.java` | Menu de bord avec panneau latéral affichant l'utilisateur connecté et redirection vers les modules A et B. | **PASS** | Navigation fluide avec curseur Main. |
| **IHM-03** | `FrmListeTypeAcces.java` | Tableau JTable listant les types d'accès actifs, avec barre de recherche par libellé et boutons CRUD. | **PASS** | Double-clic désactivé sur JTable. Recherche en temps réel. |
| **IHM-04** | `FrmFormulaireTypeAcces.java` | Boîte de dialogue modale pour la création ou modification d'un type d'accès. | **PASS** | Titre dynamique. Validation du libellé obligatoire. |
| **IHM-05** | `FrmListeAcces.java` | Tableau JTable affichant les points d'accès actifs, filtres par lieu et type d'accès, et recherche multicritère. | **PASS** | Synchronisation en cascade des filtres. Bouton Fiche Détail. |
| **IHM-06** | `FrmFormulaireAcces.java` | Boîte de dialogue modale d'ajout/modification de point d'accès. | **PASS** | **"Champ rouge si vide et obligatoire"** validé visuellement. |
| **IHM-07** | `FrmDetailAcces.java` | Fenêtre en lecture seule présentant tous les attributs d'un point d'accès, dates de création et de modif incluses. | **PASS** | Clôture propre. Aucun champ éditable. |

---

## 3. Validation des Performances
*   **Temps de réponse du listage :** < 0,1 seconde (exigence < 2 secondes).
*   **Temps de réponse de la création/modification :** < 0,05 seconde (exigence < 1 seconde).
*   **Temps de réponse de la recherche multicritère :** < 0,1 seconde (exigence < 3 secondes).
*   **Connexion au démarrage de la JVM :** < 2 secondes (exigence < 5 secondes).
*   **Consommation mémoire (Heap JVM) :** < 50 Mo au démarrage (exigence minimum autorisée de 256 Mo, donc largement conforme).

---

## 4. Conclusion
L'ensemble du Lot 1 est déclaré **conforme** et **validé**. Le PVL a été exécuté en intégralité et ne présente aucune anomalie ouverte.
