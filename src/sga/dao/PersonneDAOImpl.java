package sga.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import sga.metier.Personne;
import sga.util.ConnexionBD;

public class PersonneDAOImpl implements IPersonneDAO {

    @Override
    public Personne authentifier(String login, String motDePasseHash) {
        if (ConnexionBD.isSimulationMode()) {
            for (Personne p : SimulationData.personnes) {
                if (p.getLogin() != null && p.getLogin().equalsIgnoreCase(login)
                        && p.getMotDePasse() != null && p.getMotDePasse().equals(motDePasseHash)
                        && "ACTIF".equalsIgnoreCase(p.getStatut())) {
                    return p;
                }
            }
            return null;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return null;
        }

        String sql = "SELECT id_personne, nom, prenom, fonction, login, mot_de_passe, statut, id_profil " +
                     "FROM PERSONNE WHERE UPPER(login) = UPPER(?) AND mot_de_passe = ? AND statut = 'ACTIF'";

        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, login);
            ps.setString(2, motDePasseHash);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Personne(
                        rs.getInt("id_personne"),
                        rs.getString("nom"),
                        rs.getString("prenom"),
                        rs.getString("fonction"),
                        rs.getString("login"),
                        rs.getString("mot_de_passe"),
                        rs.getString("statut"),
                        rs.getInt("id_profil")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[PersonneDAO] Erreur d'authentification : " + e.getMessage());

            // Diagnostic automatique : ORA-00942 = table/vue absente du schéma courant.
            // On affiche le schéma et l'utilisateur réellement connectés pour identifier
            // immédiatement si la table PERSONNE a été créée dans un autre schéma.
            if (e.getErrorCode() == 942) {
                diagnostiquerSchema(conn);
            }
        }
        return null;
    }

    /**
     * Affiche dans la console le schéma/utilisateur courant de la connexion JDBC
     * ainsi que les tables visibles nommées PERSONNE, afin d'identifier rapidement
     * une erreur ORA-00942 due à un mauvais schéma.
     */
    private void diagnostiquerSchema(Connection conn) {
        System.err.println("------ DIAGNOSTIC ORA-00942 ------");
        try {
            System.err.println("Utilisateur JDBC connecté : " + conn.getMetaData().getUserName());
            System.err.println("Schéma courant            : " + conn.getSchema());
        } catch (SQLException ex) {
            System.err.println("Impossible de lire le schéma courant : " + ex.getMessage());
        }

        String sqlCheck = "SELECT owner, table_name FROM all_tables WHERE table_name = 'PERSONNE'";
        try (PreparedStatement ps = conn.prepareStatement(sqlCheck);
             ResultSet rs = ps.executeQuery()) {
            boolean trouvee = false;
            while (rs.next()) {
                trouvee = true;
                System.err.println("Table PERSONNE trouvée dans le schéma : " + rs.getString("owner"));
            }
            if (!trouvee) {
                System.err.println("AUCUNE table PERSONNE trouvée dans la base, quel que soit le schéma.");
                System.err.println("=> Le script de création SQL n'a pas (encore) été exécuté.");
            } else {
                System.err.println("=> La table existe mais probablement pas dans le schéma de connexion ci-dessus.");
                System.err.println("=> Solutions : exécuter le script connecté sous cet utilisateur,");
                System.err.println("   OU créer un synonyme, OU qualifier la requête avec SCHEMA.PERSONNE.");
            }
        } catch (SQLException ex) {
            System.err.println("Impossible d'interroger all_tables : " + ex.getMessage());
        }
        System.err.println("-----------------------------------");
    }
}
