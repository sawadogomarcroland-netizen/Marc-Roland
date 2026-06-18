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
                // ATTENTION : a verifier avec le contenu reel de SimulationData.lieux.
                // Si les donnees de simulation stockent deja 'O' (comme en base reelle),
                // remplacer "OUVERT" par "O" ci-dessous pour rester coherent.
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
        // CORRIGE : libelle_lieu -> libelle, et statut 'OUVERT' -> 'O'
        // (conforme au script officiel : LIEU.statut est CHAR(1) IN ('O','F'))
        String sql = "SELECT id_lieu, libelle, emplacement, capacite_max, statut " +
                     "FROM LIEU WHERE statut = 'O' ORDER BY libelle ASC";

        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                liste.add(new Lieu(
                    rs.getInt("id_lieu"),
                    rs.getString("libelle"),
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