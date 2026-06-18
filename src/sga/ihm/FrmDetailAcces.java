package sga.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import sga.metier.Acces;

public class FrmDetailAcces extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JPanel contentPanel = new JPanel();

    public FrmDetailAcces(JFrame parent, Acces acces) {
        super(parent, true); // Modal

        setTitle("SGA - Fiche Point d'Accès");
        setBounds(100, 100, 500, 480);
        setLocationRelativeTo(parent);
        setResizable(false);

        Color primaryColor = new Color(44, 62, 80);
        Color textColor = new Color(51, 51, 51);
        Color accentColor = new Color(24, 188, 156);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(20, 25, 20, 25));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // En-tête
        JLabel lblTitre = new JLabel("FICHE DÉTAILLÉE : " + acces.getLibelle().toUpperCase());
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblTitre.setForeground(primaryColor);
        lblTitre.setBounds(20, 15, 440, 25);
        contentPanel.add(lblTitre);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(210, 220, 230));
        separator.setBounds(20, 45, 440, 2);
        contentPanel.add(separator);

        // Libellé
        addDetailRow(contentPanel, "Libellé :", acces.getLibelle(), 60);

        // Type d'accès
        addDetailRow(contentPanel, "Type d'accès :", acces.getTypeLibelle() != null ? acces.getTypeLibelle() : "N/A", 100);

        // Lieu
        addDetailRow(contentPanel, "Lieu associé :", acces.getLieuLibelle() != null ? acces.getLieuLibelle() : "N/A", 140);

        // Statut
        String statutStr = "A".equals(acces.getStatut()) ? "ACTIF" : "INACTIF";
        addDetailRow(contentPanel, "Statut :", statutStr, 180);

        // Description
        JLabel lblDescTitle = new JLabel("Description :");
        lblDescTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDescTitle.setForeground(primaryColor);
        lblDescTitle.setBounds(20, 220, 120, 20);
        contentPanel.add(lblDescTitle);

        JTextArea txtDesc = new JTextArea(acces.getDescription() != null ? acces.getDescription() : "Aucune description renseignée.");
        txtDesc.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtDesc.setForeground(textColor);
        txtDesc.setLineWrap(true);
        txtDesc.setWrapStyleWord(true);
        txtDesc.setEditable(false);
        txtDesc.setBackground(new Color(235, 238, 242));
        txtDesc.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JScrollPane scrollPane = new JScrollPane(txtDesc);
        scrollPane.setBounds(20, 245, 440, 70);
        contentPanel.add(scrollPane);

        // Dates Audit
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        String dateCreStr = (acces.getDateCreation() != null) ? sdf.format(acces.getDateCreation()) : "N/A";
        String dateModStr = (acces.getDateModification() != null) ? sdf.format(acces.getDateModification()) : "Jamais modifié (Valeur d'origine)";

        addDetailRow(contentPanel, "Date de création :", dateCreStr, 330);
        addDetailRow(contentPanel, "Dernière modification :", dateModStr, 360);

        // Bouton Fermer
        JButton btnFermer = new JButton("Fermer / Retour liste");
        btnFermer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnFermer.setForeground(Color.WHITE);
        btnFermer.setBackground(primaryColor);
        btnFermer.setBorderPainted(false);
        btnFermer.setFocusPainted(false);
        btnFermer.setOpaque(true);
        btnFermer.setUI(new MetalButtonUI()); // CORRIGE : force le rendu pour respecter les couleurs
        btnFermer.setBounds(20, 400, 440, 35);
        btnFermer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(btnFermer);

        btnFermer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmDetailAcces.this.dispose();
            }
        });
    }

    private void addDetailRow(JPanel panel, String title, String value, int y) {
        Color primaryColor = new Color(44, 62, 80);
        Color textColor = new Color(51, 51, 51);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblTitle.setForeground(primaryColor);
        lblTitle.setBounds(20, y, 150, 20);
        panel.add(lblTitle);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblValue.setForeground(textColor);
        lblValue.setBounds(180, y, 280, 20);
        panel.add(lblValue);
    }
}