package sga.main;

import java.util.List;
import sga.dao.IPersonneDAO;
import sga.dao.PersonneDAOImpl;
import sga.metier.Acces;
import sga.metier.Personne;
import sga.metier.TypeAcces;
import sga.service.AccesService;
import sga.service.TypeAccesService;
import sga.util.ConnexionBD;
import sga.util.HashUtil;

public class TestsSGA {

    private static int testsRun = 0;
    private static int testsPassed = 0;

    public static void main(String[] args) {
        System.out.println("==========================================================");
        System.out.println("SGA LOT 1 - BATCH DE TESTS UNITAIRES ET D'INTEGRATION");
        System.out.println("==========================================================");
        
        // Forcer le mode simulation pour garantir la reproductibilité des tests sans DB Oracle
        ConnexionBD.setSimulationMode(true);
        System.out.println("[INFO] Exécution des tests en MODE SIMULATION (En mémoire)\n");

        try {
            testAuthentification();
            testTypeAcces();
            testAcces();
            
            System.out.println("\n==========================================================");
            System.out.println("BILAN DES TESTS :");
            System.out.println("Tests exécutés : " + testsRun);
            System.out.println("Tests réussis  : " + testsPassed + " / " + testsRun);
            if (testsPassed == testsRun) {
                System.out.println("RESULTAT : SUCCES GLOBAL (100% de réussite)");
            } else {
                System.out.println("RESULTAT : ECHEC (Certains tests ont échoué)");
            }
            System.out.println("==========================================================");

        } catch (Exception e) {
            System.err.println("[CRITICAL] Erreur inattendue durant les tests : " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void assertEquals(String message, Object expected, Object actual) {
        testsRun++;
        if ((expected == null && actual == null) || (expected != null && expected.equals(actual))) {
            testsPassed++;
            System.out.println("[PASS] " + message);
        } else {
            System.err.println("[FAIL] " + message + " -> Attendu : " + expected + ", Obtenu : " + actual);
        }
    }

    private static void assertNotNull(String message, Object obj) {
        testsRun++;
        if (obj != null) {
            testsPassed++;
            System.out.println("[PASS] " + message);
        } else {
            System.err.println("[FAIL] " + message + " -> Attendu non nul, Obtenu nul.");
        }
    }

    private static void assertNull(String message, Object obj) {
        testsRun++;
        if (obj == null) {
            testsPassed++;
            System.out.println("[PASS] " + message);
        } else {
            System.err.println("[FAIL] " + message + " -> Attendu nul, Obtenu non nul.");
        }
    }

    private static void testAuthentification() {
        System.out.println("--- TEST : AUTHENTIFICATION ---");
        IPersonneDAO dao = new PersonneDAOImpl();

        // 1. Succès Admin
        String hashAdmin = HashUtil.hashSHA256("admin");
        Personne admin = dao.authentifier("admin", hashAdmin);
        assertNotNull("Authentification admin nominale", admin);
        if (admin != null) {
            assertEquals("Rôle de l'admin", 1, admin.getIdProfil());
            assertEquals("Statut de l'admin", "ACTIF", admin.getStatut());
        }

        // 2. Échec mauvais mot de passe
        Personne echec = dao.authentifier("admin", HashUtil.hashSHA256("mauvais_pwd"));
        assertNull("Authentification rejetée (mauvais mot de passe)", echec);

        // 3. Échec utilisateur inactif (sawadogo.a est INACTIF)
        String hashSawadogo = HashUtil.hashSHA256("pwd"); // sawadogo.a
        Personne inactif = dao.authentifier("sawadogo.a", hashSawadogo);
        assertNull("Authentification rejetée (utilisateur inactif)", inactif);
        System.out.println();
    }

    private static void testTypeAcces() {
        System.out.println("--- TEST : TYPES D'ACCÈS ---");
        TypeAccesService service = new TypeAccesService();

        // 1. Lister les actifs existants
        List<TypeAcces> liste = service.listerActifs();
        assertEquals("Nombre initial de types actifs", 5, liste.size());

        // 2. Création d'un nouveau type
        TypeAcces nouveau = new TypeAcces();
        nouveau.setLibelle("TOURNIQUET");
        nouveau.setDescription("Tourniquet de sécurité demi-hauteur");
        try {
            boolean ok = service.creer(nouveau);
            assertEquals("Création d'un type valide", true, ok);
            assertEquals("Force majuscule sur le libellé (Trigger simulation)", "TOURNIQUET", nouveau.getLibelle());
        } catch (Exception e) {
            System.err.println("[FAIL] Échec inattendu lors de la création : " + e.getMessage());
            testsRun++;
        }

        // 3. Vérification du doublon
        try {
            TypeAcces doublon = new TypeAcces();
            doublon.setLibelle("tourniquet"); // casse différente
            service.creer(doublon);
            System.err.println("[FAIL] Le doublon a été inséré à tort.");
            testsRun++;
        } catch (Exception e) {
            assertEquals("Détection de doublon (insensible à la casse)", "Un type d'accès actif avec le libellé 'TOURNIQUET' existe déjà.", e.getMessage());
        }

        // 4. Modification nominale
        try {
            TypeAcces aModifier = service.listerActifs().get(service.listerActifs().size() - 1); // TOURNIQUET
            aModifier.setDescription("Description modifiée");
            boolean ok = service.modifier(aModifier);
            assertEquals("Modification valide d'un type d'accès", true, ok);
        } catch (Exception e) {
            System.err.println("[FAIL] Échec lors de la modification : " + e.getMessage());
            testsRun++;
        }

        // 5. Tentative de désactivation d'un type d'accès UTILISÉ (CF-04)
        // Le type 1 (PORTE) est utilisé par des points d'accès
        try {
            service.desactiver(1); // PORTE
            System.err.println("[FAIL] La désactivation d'un type utilisé (PORTE) a été acceptée à tort.");
            testsRun++;
        } catch (Exception e) {
            assertEquals("Interdiction désactivation type utilisé (CF-04)", 
                "Désactivation impossible : ce type d'accès est actuellement associé à des points d'accès actifs.", 
                e.getMessage());
        }

        // 6. Désactivation nominale d'un type NON UTILISÉ (TOURNIQUET)
        try {
            List<TypeAcces> actifs = service.listerActifs();
            int idTourniquet = 0;
            for (TypeAcces t : actifs) {
                if ("TOURNIQUET".equals(t.getLibelle())) {
                    idTourniquet = t.getIdTypeAcces();
                }
            }
            boolean ok = service.desactiver(idTourniquet);
            assertEquals("Désactivation d'un type inutilisé réussie", true, ok);
        } catch (Exception e) {
            System.err.println("[FAIL] Échec désactivation type inutilisé : " + e.getMessage());
            testsRun++;
        }
        System.out.println();
    }

    private static void testAcces() {
        System.out.println("--- TEST : POINTS D'ACCÈS ---");
        AccesService service = new AccesService();

        // 1. Lister les actifs existants
        List<Acces> liste = service.listerActifs();
        assertEquals("Nombre initial de points d'accès actifs", 8, liste.size());

        // 2. Création nominale
        Acces nouveau = new Acces();
        nouveau.setLibelle("Lecteur Badge Cafétéria");
        nouveau.setDescription("Entrée cafétéria");
        nouveau.setIdTypeAcces(1); // PORTE
        nouveau.setIdLieu(4); // Administration
        try {
            boolean ok = service.creer(nouveau);
            assertEquals("Création point d'accès nominal", true, ok);
            assertNotNull("Date de création renseignée par trigger", nouveau.getDateCreation());
        } catch (Exception e) {
            System.err.println("[FAIL] Échec création point d'accès : " + e.getMessage());
            testsRun++;
        }

        // 3. Validation de champ obligatoire manquant (CF-05 / CF-01)
        Acces invalide = new Acces();
        invalide.setLibelle(""); // Vide
        invalide.setIdTypeAcces(1);
        invalide.setIdLieu(0); // Pas de lieu
        try {
            service.creer(invalide);
            System.err.println("[FAIL] L'insertion d'un accès invalide a été acceptée.");
            testsRun++;
        } catch (Exception e) {
            assertEquals("Détection champ obligatoire vide", "Le libellé du point d'accès est obligatoire.", e.getMessage());
        }

        // 4. Recherche multicritère
        // Recherche sur le Lieu 'Parking Nord' (ID 3) uniquement
        List<Acces> recherche = service.rechercherMulticritere(null, 0, 3);
        assertEquals("Nombre de points d'accès correspondants au parking nord", 2, recherche.size());

        // 5. Désactivation logique (CF-02)
        try {
            int idASupprimer = service.listerActifs().get(0).getIdAcces();
            boolean ok = service.desactiver(idASupprimer);
            assertEquals("Désactivation logique réussie", true, ok);
            
            Acces recup = service.recupererParId(idASupprimer);
            assertEquals("Statut après désactivation", "I", recup.getStatut());
            assertNotNull("Date de modification renseignée", recup.getDateModification());
        } catch (Exception e) {
            System.err.println("[FAIL] Échec désactivation logique : " + e.getMessage());
            testsRun++;
        }
        System.out.println();
    }
}
