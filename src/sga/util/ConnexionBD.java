package sga.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionBD {
    private static Connection conn = null;
    private static boolean simulate = false;

    private ConnexionBD() {}

    /**
     * Retourne la connexion active à la base de données Oracle.
     * Si la connexion échoue ou si le driver est absent, active automatiquement le mode simulation.
     * @return Connection ou null en mode simulation
     */
    public static synchronized Connection getConnexion() {
        if (simulate) {
            return null;
        }
        try {
            if (conn == null || conn.isClosed()) {
                try {
                    // Charger le driver JDBC Oracle
                    Class.forName("oracle.jdbc.driver.OracleDriver");
                    conn = DriverManager.getConnection(
                        Constantes.DB_URL, 
                        Constantes.DB_USER, 
                        Constantes.DB_PASS
                    );
                    System.out.println("[SGA] Connexion à la base de données Oracle réussie.");
                } catch (ClassNotFoundException e) {
                    System.err.println("[SGA - WARNING] Driver Oracle JDBC non trouvé. Basculement en mode Simulation.");
                    simulate = true;
                    conn = null;
                } catch (SQLException e) {
                    System.err.println("[SGA - WARNING] Erreur de connexion Oracle : " + e + ". Basculement en mode Simulation.");
                    e.printStackTrace();
                    simulate = true;
                    conn = null;
                }
            }
        } catch (SQLException e) {
            conn = null;
        }
        return conn;
    }

    /**
     * Vérifie si l'application s'exécute en mode simulation (sans base de données).
     */
    public static boolean isSimulationMode() {
        if (conn == null && !simulate) {
            getConnexion();
        }
        return simulate;
    }

    /**
     * Active ou désactive manuellement le mode simulation.
     */
    public static void setSimulationMode(boolean mode) {
        simulate = mode;
        if (mode && conn != null) {
            fermerConnexion();
        }
    }

    /**
     * Ferme proprement la connexion à la base de données.
     */
    public static synchronized void fermerConnexion() {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
                System.out.println("[SGA] Connexion Oracle fermée.");
            } catch (SQLException e) {
                System.err.println("[SGA] Erreur lors de la fermeture de la connexion : " + e.getMessage());
            }
        }
    }
}
