package sga.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sga.metier.TypeAcces;
import sga.util.ConnexionBD;

public class TypeAccesDAOImpl implements ITypeAccesDAO {

    @Override
    public List<TypeAcces> listerActifs() {
        if (ConnexionBD.isSimulationMode()) {
            List<TypeAcces> result = new ArrayList<>();
            for (TypeAcces t : SimulationData.types) {
                if ("A".equals(t.getStatut())) {
                    result.add(t);
                }
            }
            // Trier par libellé ascendant
            result.sort((t1, t2) -> t1.getLibelle().compareToIgnoreCase(t2.getLibelle()));
            return result;
        }

        List<TypeAcces> liste = new ArrayList<>();
        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return liste;
        }
        String sql = "SELECT id_type_acces, libelle, description, date_creation FROM TYPE_ACCES WHERE statut = 'A' ORDER BY libelle ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                liste.add(new TypeAcces(
                    rs.getInt("id_type_acces"),
                    rs.getString("libelle"),
                    rs.getString("description"),
                    "A",
                    rs.getDate("date_creation")
                ));
            }
        } catch (SQLException e) {
            System.err.println("[TypeAccesDAO] Erreur listerActifs : " + e.getMessage());
        }
        return liste;
    }

    @Override
    public boolean creer(TypeAcces type) {
        if (ConnexionBD.isSimulationMode()) {
            int newId = 1;
            for (TypeAcces t : SimulationData.types) {
                if (t.getIdTypeAcces() >= newId) {
                    newId = t.getIdTypeAcces() + 1;
                }
            }
            type.setIdTypeAcces(newId);
            type.setStatut("A");
            type.setDateCreation(new Date());
            // Simuler le trigger de mise en majuscule
            type.setLibelle(type.getLibelle().toUpperCase());
            SimulationData.types.add(type);
            return true;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        // CORRIGE : id_type_acces n'est PAS auto-genere (pas d'IDENTITY dans le script
        // officiel). Il faut fournir explicitement SEQ_TYPE_ACCES.NEXTVAL, sinon
        // ORA-01400 (cannot insert NULL into "TYPE_ACCES"."ID_TYPE_ACCES").
        String sql = "INSERT INTO TYPE_ACCES (id_type_acces, libelle, description, date_creation) " +
                     "VALUES (SEQ_TYPE_ACCES.NEXTVAL, ?, ?, SYSDATE)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            // Le trigger TRG_TYPE_ACCES_UPPER passera le libellé en majuscule automatiquement
            ps.setString(1, type.getLibelle());
            ps.setString(2, type.getDescription());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TypeAccesDAO] Erreur creer : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifierDoublon(String libelle) {
        if (ConnexionBD.isSimulationMode()) {
            for (TypeAcces t : SimulationData.types) {
                if ("A".equals(t.getStatut()) && t.getLibelle().equalsIgnoreCase(libelle)) {
                    return true;
                }
            }
            return false;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM TYPE_ACCES WHERE UPPER(libelle) = UPPER(?) AND statut = 'A'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, libelle);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[TypeAccesDAO] Erreur verifierDoublon : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean modifier(TypeAcces type) {
        if (ConnexionBD.isSimulationMode()) {
            for (TypeAcces t : SimulationData.types) {
                if (t.getIdTypeAcces() == type.getIdTypeAcces()) {
                    t.setLibelle(type.getLibelle().toUpperCase()); // Simule le trigger
                    t.setDescription(type.getDescription());
                    return true;
                }
            }
            return false;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        String sql = "UPDATE TYPE_ACCES SET libelle = ?, description = ? WHERE id_type_acces = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, type.getLibelle());
            ps.setString(2, type.getDescription());
            ps.setInt(3, type.getIdTypeAcces());
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TypeAccesDAO] Erreur modifier : " + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean verifierUsage(int idTypeAcces) {
        if (ConnexionBD.isSimulationMode()) {
            for (sga.metier.Acces a : SimulationData.accesList) {
                if (a.getIdTypeAcces() == idTypeAcces && "A".equals(a.getStatut())) {
                    return true;
                }
            }
            return false;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        String sql = "SELECT COUNT(*) FROM ACCES WHERE id_type_acces = ? AND statut = 'A'";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTypeAcces);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("[TypeAccesDAO] Erreur verifierUsage : " + e.getMessage());
        }
        return false;
    }

    @Override
    public boolean desactiver(int idTypeAcces) {
        if (ConnexionBD.isSimulationMode()) {
            for (TypeAcces t : SimulationData.types) {
                if (t.getIdTypeAcces() == idTypeAcces) {
                    t.setStatut("I");
                    return true;
                }
            }
            return false;
        }

        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return false;
        }
        String sql = "UPDATE TYPE_ACCES SET statut = 'I' WHERE id_type_acces = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idTypeAcces);
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            System.err.println("[TypeAccesDAO] Erreur desactiver : " + e.getMessage());
            return false;
        }
    }

    @Override
    public List<TypeAcces> rechercherParLibelle(String libelle) {
        if (ConnexionBD.isSimulationMode()) {
            List<TypeAcces> result = new ArrayList<>();
            for (TypeAcces t : SimulationData.types) {
                if ("A".equals(t.getStatut()) &&
                    (libelle == null || t.getLibelle().toUpperCase().contains(libelle.toUpperCase()))) {
                    result.add(t);
                }
            }
            result.sort((t1, t2) -> t1.getLibelle().compareToIgnoreCase(t2.getLibelle()));
            return result;
        }

        List<TypeAcces> liste = new ArrayList<>();
        Connection conn = ConnexionBD.getConnexion();
        if (conn == null) {
            return liste;
        }
        String sql = "SELECT id_type_acces, libelle, description, date_creation FROM TYPE_ACCES " +
                     "WHERE statut = 'A' AND UPPER(libelle) LIKE ? ORDER BY libelle ASC";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + libelle.toUpperCase() + "%");
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    liste.add(new TypeAcces(
                        rs.getInt("id_type_acces"),
                        rs.getString("libelle"),
                        rs.getString("description"),
                        "A",
                        rs.getDate("date_creation")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("[TypeAccesDAO] Erreur rechercherParLibelle : " + e.getMessage());
        }
        return liste;
    }
}