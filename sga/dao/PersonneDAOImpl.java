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
        }
        return null;
    }
}
