package sga.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import sga.metier.Lieu;
import sga.util.ConnexionBD;

public class LieuDAOImpl implements ILieuDAO {
    @Override
    public List<Lieu> listerLieuxActifs() {
        if (ConnexionBD.isSimulationMode()) {
            List<Lieu> result = new ArrayList<>();
            for (Lieu l : SimulationData.lieux) {
                if ("OUVERT".equalsIgnoreCase(l.getStatut())) {
                    result.add(l);
                }
            }
            return result;
        }

        List<Lieu> liste = new ArrayList<>();
        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return liste;
        }
        String sql = "SELECT id_lieu, libelle_lieu, emplacement, capacite_max, statut FROM LIEU WHERE statut = 'OUVERT' ORDER BY libelle_lieu ASC";
        
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                liste.add(new Lieu(
                    rs.getInt("id_lieu"),
                    rs.getString("libelle_lieu"),
                    rs.getString("emplacement"),
                    rs.getInt("capacite_max"),
                    rs.getString("statut")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[LieuDAO] Erreur lors du listage des lieux : " + e.getMessage());
        }
        return liste;
    }
}
