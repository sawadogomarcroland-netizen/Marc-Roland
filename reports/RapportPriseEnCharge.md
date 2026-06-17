# RAPPORT DE PRISE EN CHARGE (PHASE 2)
**Projet :** Système de Gestion d'Accès (SGA) — Lot 1 : Gestion des Accès  
**Client :** CCI-BF / ISGE-BF  
**Binôme rédacteur :** Binôme 2 (Phase 2)  
**Destinataire :** M. NAGALO Alexis (alexis.nagalo23@gmail.com)  

---

## 1. Introduction & Objectif
Ce rapport de prise en charge a été rédigé à la suite de la réception des livrables de la Phase 1 produits par le Binôme 9. L'objectif est d'effectuer une analyse critique de l'architecture technique, du modèle de données (MCD/MLD) et des spécifications fonctionnelles afin d'identifier d'éventuels écarts et de valider les bases pour la conception détaillée, le codage et les tests de la Phase 2.

## 2. Analyse critique des livrables de la Phase 1

### 2.1 Modèle Conceptuel et Physique de Données (MCD/MLD)
*   **Structure générale :** La base de données respecte les trois premières formes normales (3FN). La séparation entre `TYPE_ACCES`, `ACCES` et `LIEU` évite la redondance d'informations.
*   **Triggers & Index :** L'ajout de triggers (`TRG_ACCES_MODIF` et `TRG_TYPE_ACCES_UPPER`) est pertinent car il délègue au SGBD Oracle le traitement de la cohérence temporelle (mise à jour automatique de la date de modification) et structurelle (mise en majuscule automatique des libellés de types d'accès).
*   **Écart identifié :** La vue `VUE_ACCES_DETAIL` définie dans le script SQL hérité ne filtrait pas sur le statut `'A'` (actif), contrairement aux spécifications de la Phase 1. Cette correction a été apportée pour assurer l'exactitude de la vue conformément aux règles métiers.

### 2.2 Architecture Logicielle Héritée
L'architecture 3-tiers proposée par le Binôme 9 est claire et bien structurée :
1.  **Couche IHM (`sga.ihm`) :** Conçue pour n'héberger aucune logique de traitement ou de persistance. Elle interagit uniquement avec la couche Service.
2.  **Couche Service/Métier (`sga.service` / `sga.metier`) :** Permet d'isoler les règles métiers strictes (ex: CF-01 à CF-07) de l'interface graphique Swing et de la couche DAO.
3.  **Couche DAO (`sga.dao`) :** Gère la communication JDBC avec Oracle Express. L'utilisation du singleton `ConnexionBD.java` avec un mécanisme intelligent de repli en "Mode Simulation" (en mémoire) est une excellente idée. Elle garantit le fonctionnement de l'application sur n'importe quel ordinateur, même sans serveur Oracle actif (par exemple, lors de la correction par l'enseignant).

## 3. Plan d'actions et corrections apportées pour la Phase 2

À la suite de notre audit, nous avons mené les actions correctives suivantes lors de notre implémentation :
1.  **Création du modèle Badge :** Ajout de la classe POJO `Badge.java` dans le package `sga.metier` pour assurer la cohérence complète avec le MCD/MLD commun.
2.  **Alignement nomenclature SQL/Java :** Nous avons assuré une cohérence totale de nommage entre les attributs Java (ex: `libelleLieu`, `idTypeAcces`) et les colonnes SQL (ex: `libelle_lieu`, `id_type_acces`).
3.  **Implémentation complète des DAO :** Écriture des classes d'implémentation `TypeAccesDAOImpl.java` et `AccesDAOImpl.java` gérant à la fois les requêtes JDBC préparées (PreparedStatement) pour Oracle et le mode de simulation locale (collections mémoire).
4.  **Développement des Services Métier :** Codage des contrôles requis (vérification d'unicité insensible à la casse pour les types d'accès, interdiction de suppression physique, blocage de la désactivation d'un type d'accès en cours d'utilisation).
5.  **Développement IHM sous Swing :** Conception de 7 fenêtres/boîtes de dialogue Swing respectant les maquettes fonctionnelles et compatibles avec l'éditeur de design WindowBuilder.

## 4. Conclusion
Les livrables de la Phase 1 ont été validés avec les corrections mineures mentionnées ci-dessus. Le projet SGA Lot 1 dispose désormais d'une assise solide pour la livraison de la Phase 2. L'application est prête à compiler et à être testée de manière bidirectionnelle (Simulation locale & Base Oracle).
