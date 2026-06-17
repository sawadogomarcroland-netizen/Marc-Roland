package sga.main;

import javax.swing.UIManager;
import sga.ihm.FrmConnexion;

public class Main {
    public static void main(String[] args) {
        try {
            // Définir le Look & Feel système pour une intégration native propre
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("[SGA] Impossible d'appliquer le Look & Feel système, utilisation du défaut.");
        }

        // Lancement de l'application Swing dans le thread approprié
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FrmConnexion frame = new FrmConnexion();
                frame.setVisible(true);
            }
        });
    }
}
