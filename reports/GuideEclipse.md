# GUIDE D'IMPORTATION ET D'EXÉCUTION DANS ECLIPSE WITH WINDOWBUILDER
**Système :** Système de Gestion d'Accès (SGA) — Lot 1 : Gestion des Accès  
**Phase :** Phase 2 — Intégration IDE  
**Binôme rédacteur :** Binôme 2 (Phase 2)  

---

## 1. Prérequis techniques
Pour exécuter et modifier ce projet, assurez-vous de disposer de la configuration suivante sur votre machine :
1.  **JDK 8 (Java SE Development Kit 8) :** Impératif pour respecter la contrainte réglementaire du projet.
2.  **Eclipse IDE :** De préférence *Eclipse IDE for Enterprise Java and Web Developers*.
3.  **Plugin WindowBuilder :** Installable depuis le Eclipse Marketplace (`Help` > `Eclipse Marketplace...` > Rechercher "WindowBuilder" > Installer).
4.  **Driver JDBC Oracle (`ojdbc8.jar`) :** Nécessaire si vous désactivez le mode simulation pour vous connecter à Oracle Express.

---

## 2. Étape 1 : Importer le projet dans Eclipse

1.  Lancez **Eclipse IDE** et choisissez un espace de travail (*Workspace*).
2.  Allez dans **File** > **Import...**
3.  Dans la fenêtre d'import, développez la catégorie **General** et sélectionnez **Projects from Folder or Archive**, puis cliquez sur **Next**.
4.  Cliquez sur **Directory...** à côté de *Import source* et sélectionnez le dossier racine du projet :  
    `C:\Users\HP\.gemini\antigravity\scratch\sga-lot1`
5.  Eclipse détecte automatiquement le projet. Assurez-vous que la case est cochée et cliquez sur **Finish**.

---

## 3. Étape 2 : Configurer le Build Path (JRE Java 8)

Il est crucial d'associer le projet à un environnement d'exécution **Java 8 (1.8)**.

1.  Faites un clic droit sur le projet `sga-lot1` dans le *Package Explorer* d'Eclipse et sélectionnez **Properties**.
2.  Allez dans **Java Build Path** dans le menu de gauche, puis sélectionnez l'onglet **Libraries**.
3.  Si la JRE par défaut n'est pas Java 8 (1.8) :
    *   Sélectionnez *JRE System Library* et cliquez sur **Remove**.
    *   Cliquez sur **Add Library...** > **JRE System Library** > **Next**.
    *   Cochez **Alternate JRE** et sélectionnez une JRE compatible **JavaSE-1.8** (ou cliquez sur *Installed JREs...* pour en ajouter une si nécessaire).
    *   Cliquez sur **Finish**.
4.  *Optionnel (Mode Oracle connecté) :* Pour ajouter le driver Oracle, toujours dans l'onglet **Libraries**, cliquez sur **Add External JARs...**, sélectionnez votre fichier `ojdbc8.jar` puis validez.
5.  Allez dans **Java Compiler** (menu de gauche), décochez "Use compliance from execution environment" et assurez-vous que **Compiler compliance level** est positionné sur **1.8**.
6.  Cliquez sur **Apply and Close**, puis acceptez le rebuild du projet.

---

## 4. Étape 3 : Exécuter l'Application

### 4.1 Exécution des tests d'intégration (Sans IHM)
Pour s'assurer que toutes les règles métiers compilent et s'exécutent correctement :
1.  Dans le *Package Explorer*, développez `src` > `sga.main`.
2.  Faites un clic droit sur **`TestsSGA.java`**.
3.  Sélectionnez **Run As** > **Java Application**.
4.  La console d'Eclipse affichera le bilan des 20 tests d'intégration au statut `[PASS]`.

### 4.2 Exécution de l'Application Graphique (IHM)
1.  Dans le *Package Explorer*, développez `src` > `sga.main`.
2.  Faites un clic droit sur **`Main.java`**.
3.  Sélectionnez **Run As** > **Java Application**.
4.  La fenêtre d'authentification **`FrmConnexion`** s'affiche.
    *   *Mots de passe par défaut :* `admin` (pwd: `admin`), `agent` (pwd: `agent`), `dsi` (pwd: `dsi`).
    *   *Bascule Simulation :* Vous pouvez cliquer sur le bouton du bas pour activer/désactiver le mode simulation en temps réel.

---

## 5. Étape 4 : Utiliser et modifier les interfaces avec WindowBuilder

Pour modifier visuellement les écrans Swing :

1.  Dans le *Package Explorer*, allez dans le package `sga.ihm`.
2.  Faites un clic droit sur la classe de l'écran que vous souhaitez modifier (ex: `FrmListeAcces.java`).
3.  Sélectionnez **Open With** > **WindowBuilder Editor**.
4.  En bas de la zone d'édition d'Eclipse, vous verrez deux onglets :
    *   **Source :** Pour modifier directement le code Java 8.
    *   **Design :** Pour afficher l'éditeur visuel (Drag & Drop, propriétés des composants, gestionnaires de disposition/layouts).
5.  Cliquez sur l'onglet **Design** (le chargement initial peut prendre quelques secondes pour analyser la structure du code).
6.  Vous pouvez maintenant modifier les positions, les couleurs, ajouter des boutons ou double-cliquer sur un élément pour générer son écouteur d'événement (*ActionListener*).

---

## 6. Résolution des problèmes courants

*   **Erreur `ClassNotFoundException: oracle.jdbc.driver.OracleDriver` au lancement :**  
    *Signification :* Le driver Oracle JDBC n'est pas configuré dans le Build Path.
    *Solution :* L'application bascule automatiquement en "Mode Simulation" sans planter. Pour utiliser une base Oracle réelle, ajoutez `ojdbc8.jar` dans les *Properties* > *Java Build Path* > *Libraries* du projet.
*   **Les polices de caractères ou les couleurs ne s'affichent pas comme prévu :**  
    *Signification :* Eclipse utilise un Look & Feel différent par défaut dans son éditeur Design.
    *Solution :* Le Look & Feel système est forcé au démarrage dans la classe `Main.java` pour garantir un rendu parfait à l'exécution.
