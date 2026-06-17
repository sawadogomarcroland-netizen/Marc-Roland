package sga.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sga.metier.Acces;
import sga.metier.Lieu;
import sga.metier.TypeAcces;
import sga.util.ConnexionBD;

public class AccesDAOImpl implements IAccesDAO {

    @Override
    public List<Acces> listerActifs() {
        if (ConnexionBD.isSimulationMode()) {
            List<Acces> result = new ArrayList<>();
            for (Acces a : SimulationData.accesList) {
                if ("A".equals(a.getStatut())) {
                    result.add(a);
                }
            }
            result.sort((a1, a2) -> a1.getLibelle().compareToIgnoreCase(a2.getLibelle()));
            return result;
        }

        List<Acces> liste = new ArrayList<>();
        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return liste;
        }
        String sql = "SELECT a.id_acces, a.libelle, a.description, a.id_type_acces, ta.libelle AS type_acces, " +
                     "a.id_lieu, l.libelle_lieu AS lieu, a.statut, a.date_creation, a.date_modification " +
                     "FROM ACCES a " +
                     "JOIN TYPE_ACCES ta ON ta.id_type_acces = a.id_type_acces " +
                     "JOIN LIEU l ON l.id_lieu = a.id_lieu " +
                     "WHERE a.statut = 'A' " +
                     "ORDER BY a.libelle ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new Acces(
                    rs.getInt("id_acces"),
                    rs.getString("libelle"),
                    rs.getString("description"),
                    rs.getInt("id_type_acces"),
                    rs.getString("type_acces"),
                    rs.getInt("id_lieu"),
                    rs.getString("lieu"),
                    rs.getString("statut"),
                    rs.getTimestamp("date_creation"),
                    rs.getTimestamp("date_modification")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[AccesDAO] Erreur listerActifs : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public boolean creer(Acces acces) {
        if (ConnexionBD.isSimulationMode()) {
            int newId = 1;
            for (Acces a : SimulationData.accesList) {
                if (a.getIdAcces() >= newId) {
                    newId = a.getIdAcces() + 1;
                }
            }
            acces.setIdAcces(newId);
            acces.setStatut("A");
            acces.setDateCreation(new Date());
            
            // Résoudre les libellés de jointure pour la simulation
            resolveJoinLabels(acces);
            
            SimulationData.accesList.add(acces);
            return true;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        String sql = "INSERT INTO ACCES (libelle, description, id_type_acces, id_lieu, statut, date_creation) " +
                     "VALUES (?, ?, ?, ?, 'A', SYSDATE)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acces.getLibelle());
            ps.setString(2, acces.getDescription());
            ps.setInt(3, acces.getIdTypeAcces());
            ps.setInt(4, acces.getIdLieu());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[AccesDAO] Erreur creer : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean modifier(Acces acces) {
        if (ConnexionBD.isSimulationMode()) {
            for (Acces a : SimulationData.accesList) {
                if (a.getIdAcces() == acces.getIdAcces()) {
                    a.setLibelle(acces.getLibelle());
                    a.setDescription(acces.getDescription());
                    a.setIdTypeAcces(acces.getIdTypeAcces());
                    a.setIdLieu(acces.getIdLieu());
                    a.setDateModification(new Date());
                    resolveJoinLabels(a);
                    return true;
                }
            }
            return false;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        // CF-06 : date_modification mise à jour automatiquement par le trigger Oracle TRG_ACCES_MODIF
        String sql = "UPDATE ACCES SET libelle = ?, description = ?, id_type_acces = ?, id_lieu = ? WHERE id_acces = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, acces.getLibelle());
            ps.setString(2, acces.getDescription());
            ps.setInt(3, acces.getIdTypeAcces());
            ps.setInt(4, acces.getIdLieu());
            ps.setInt(5, acces.getIdAcces());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[AccesDAO] Erreur modifier : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean desactiver(int idAcces) {
        if (ConnexionBD.isSimulationMode()) {
            for (Acces a : SimulationData.accesList) {
                if (a.getIdAcces() == idAcces) {
                    a.setStatut("I");
                    a.setDateModification(new Date());
                    return true;
                }
            }
            return false;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        // Une désactivation est un UPDATE sur le statut (CF-02) et donc le trigger TRG_ACCES_MODIF se lancera également
        String sql = "UPDATE ACCES SET statut = 'I' WHERE id_acces = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAcces);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[AccesDAO] Erreur desactiver : " + e.getMessage());
            return false;
        }
    }

    @Override
    public Acces recupererParId(int idAcces) {
        if (ConnexionBD.isSimulationMode()) {
            for (Acces a : SimulationData.accesList) {
                if (a.getIdAcces() == idAcces) {
                    return a;
                }
            }
            return null;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return null;
        }
        String sql = "SELECT a.id_acces, a.libelle, a.description, a.id_type_acces, ta.libelle AS type_acces, " +
                     "a.id_lieu, l.libelle_lieu AS lieu, a.statut, a.date_creation, a.date_modification " +
                     "FROM ACCES a " +
                     "JOIN TYPE_ACCES ta ON ta.id_type_acces = a.id_type_acces " +
                     "JOIN LIEU l ON l.id_lieu = a.id_lieu " +
                     "WHERE a.id_acces = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAcces);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return new Acces(
                        rs.getInt("id_acces"),
                        rs.getString("libelle"),
                        rs.getString("description"),
                        rs.getInt("id_type_acces"),
                        rs.getString("type_acces"),
                        rs.getInt("id_lieu"),
                        rs.getString("lieu"),
                        rs.getString("statut"),
                        rs.getTimestamp("date_creation"),
                        rs.getTimestamp("date_modification")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("[AccesDAO] Erreur recupererParId : " + e.getMessage());
        }
        return null;
    }

    @Override
    public List<Acces> rechercherMulticritere(String libelle, int idTypeAcces, int idLieu) {
        if (ConnexionBD.isSimulationMode()) {
            List<Acces> result = new ArrayList<>();
            for (Acces a : SimulationData.accesList) {
                if (!"A".equals(a.getStatut())) {
                    continue;
                }
                
                // Critère libellé
                if (libelle != null && !libelle.trim().isEmpty()) {
                    if (!a.getLibelle().toUpperCase().contains(libelle.trim().toUpperCase())) {
                        continue;
                    }
                }
                // Critère type
                if (idTypeAcces > 0) {
                    if (a.getIdTypeAcces() != idTypeAcces) {
                        continue;
                    }
                }
                // Critère lieu
                if (idLieu > 0) {
                    if (a.getIdLieu() != idLieu) {
                        continue;
                    }
                }
                
                result.add(a);
            }
            result.sort((a1, a2) -> a1.getLibelle().compareToIgnoreCase(a2.getLibelle()));
            return result;
        }

        List<Acces> liste = new ArrayList<>();
        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return liste;
        }

        // Construction dynamique de la requête SQL
        StringBuilder sql = new StringBuilder(
            "SELECT a.id_acces, a.libelle, a.description, a.id_type_acces, ta.libelle AS type_acces, " +
            "a.id_lieu, l.libelle_lieu AS lieu, a.statut, a.date_creation, a.date_modification " +
            "FROM ACCES a " +
            "JOIN TYPE_ACCES ta ON ta.id_type_acces = a.id_type_acces " +
            "JOIN LIEU l ON l.id_lieu = a.id_lieu " +
            "WHERE a.statut = 'A'"
        );

        List<Object> params = new ArrayList<>();

        if (libelle != null && !libelle.trim().isEmpty()) {
            sql.append(" AND UPPER(a.libelle) LIKE ?");
            params.add("%" + libelle.trim().toUpperCase() + "%");
        }
        if (idTypeAcces > 0) {
            sql.append(" AND a.id_type_acces = ?");
            params.add(idTypeAcces);
        }
        if (idLieu > 0) {
            sql.append(" AND a.id_lieu = ?");
            params.add(idLieu);
        }

        sql.append(" ORDER BY a.libelle ASC");

        try (PreparedStatement ps = conn.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                Object p = params.get(i);
                if (p instanceof String) {
                    ps.setString(i + 1, (String) p);
                } else if (p instanceof Integer) {
                    ps.setInt(i + 1, (Integer) p);
                }
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(new Acces(
                        rs.getInt("id_acces"),
                        rs.getString("libelle"),
                        rs.getString("description"),
                        rs.getInt("id_type_acces"),
                        rs.getString("type_acces"),
                        rs.getInt("id_lieu"),
                        rs.getString("lieu"),
                        rs.getString("statut"),
                        rs.getTimestamp("date_creation"),
                        rs.getTimestamp("date_modification")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[AccesDAO] Erreur rechercherMulticritere : " + e.getMessage());
        }
        return liste;
    }

    private void resolveJoinLabels(Acces acces) {
        // Résoudre Type
        for (TypeAcces t : SimulationData.types) {
            if (t.getIdTypeAcces() == acces.getIdTypeAcces()) {
                acces.setTypeLibelle(t.getLibelle());
                break;
            }
        }
        // Résoudre Lieu
        for (Lieu l : SimulationData.lieux) {
            if (l.getIdLieu() == acces.getIdLieu()) {
                acces.setLieuLibelle(l.getLibelleLieu());
                break;
            }
        }
    }
}
