package sga.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import sga.metier.TypeAcces;
import sga.service.TypeAccesService;

public class FrmFormulaireTypeAcces extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JPanel contentPanel = new JPanel();
    private JTextField txtLibelle;
    private JTextArea txtDescription;
    private JLabel lblTitreForm;
    private TypeAccesService typeAccesService = new TypeAccesService();
    private FrmListeTypeAcces parentFrame;
    private TypeAcces typeAccesModif;
    private boolean isEdition;

    public FrmFormulaireTypeAcces(FrmListeTypeAcces parent, TypeAcces type, boolean edition) {
        super(parent, true); // Modal
        this.parentFrame = parent;
        this.typeAccesModif = type;
        this.isEdition = edition;

        setTitle(edition ? "SGA - Modifier Type d'Accès" : "SGA - Ajouter Type d'Accès");
        setBounds(100, 100, 480, 360);
        setLocationRelativeTo(parent);
        setResizable(false);

        Color primaryColor = new Color(44, 62, 80);
        Color accentColor = new Color(24, 188, 156);
        Color textColor = new Color(51, 51, 51);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(245, 247, 250));
        contentPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        // Titre dynamique
        lblTitreForm = new JLabel(edition ? "MODIFIER UN TYPE D'ACCÈS" : "CRÉER UN NOUVEAU TYPE D'ACCÈS");
        lblTitreForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitreForm.setForeground(primaryColor);
        lblTitreForm.setBounds(20, 15, 420, 25);
        contentPanel.add(lblTitreForm);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(210, 220, 230));
        separator.setBounds(20, 45, 420, 2);
        contentPanel.add(separator);

        // Libellé (Obligatoire)
        JLabel lblLibelle = new JLabel("Libellé * (Unique, ex: PORTE, BARRIERE...) :");
        lblLibelle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLibelle.setForeground(textColor);
        lblLibelle.setBounds(20, 60, 420, 20);
        contentPanel.add(lblLibelle);

        txtLibelle = new JTextField();
        txtLibelle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtLibelle.setBounds(20, 85, 420, 30);
        txtLibelle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        contentPanel.add(txtLibelle);
        txtLibelle.setColumns(10);

        // Description (Optionnelle)
        JLabel lblDescription = new JLabel("Description :");
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDescription.setForeground(textColor);
        lblDescription.setBounds(20, 130, 420, 20);
        contentPanel.add(lblDescription);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 155, 420, 80);
        contentPanel.add(scrollPane);

        txtDescription = new JTextArea();
        txtDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtDescription.setLineWrap(true);
        txtDescription.setWrapStyleWord(true);
        txtDescription.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        scrollPane.setViewportView(txtDescription);

        // Boutons
        JButton btnEnregistrer = new JButton("Enregistrer");
        btnEnregistrer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnEnregistrer.setForeground(Color.WHITE);
        btnEnregistrer.setBackground(accentColor);
        btnEnregistrer.setBorderPainted(false);
        btnEnregistrer.setFocusPainted(false);
        btnEnregistrer.setBounds(20, 260, 130, 35);
        btnEnregistrer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(btnEnregistrer);

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAnnuler.setForeground(primaryColor);
        btnAnnuler.setBackground(Color.WHITE);
        btnAnnuler.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setBounds(165, 260, 110, 35);
        btnAnnuler.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(btnAnnuler);

        // Pré-remplissage en mode édition
        if (edition && type != null) {
            txtLibelle.setText(type.getLibelle());
            txtDescription.setText(type.getDescription() != null ? type.getDescription() : "");
            // En mode édition, le libellé est mis en majuscule mais éditable
        }

        // Listeners
        btnAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmFormulaireTypeAcces.this.dispose();
            }
        });

        btnEnregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrer();
            }
        });
    }

    private void enregistrer() {
        String libelle = txtLibelle.getText().trim();
        String description = txtDescription.getText().trim();

        if (libelle.isEmpty()) {
            txtLibelle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
            ));
            JOptionPane.showMessageDialog(this, 
                "Le champ Libellé est obligatoire.", 
                "Champ manquant", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Remettre bordure normale
        txtLibelle.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));

        try {
            if (isEdition) {
                // Modification
                typeAccesModif.setLibelle(libelle);
                typeAccesModif.setDescription(description);
                typeAccesService.modifier(typeAccesModif);
                JOptionPane.showMessageDialog(this, 
                    "Le type d'accès a été modifié avec succès.", 
                    "Modification réussie", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Création
                TypeAcces nouveau = new TypeAcces();
                nouveau.setLibelle(libelle);
                nouveau.setDescription(description.isEmpty() ? null : description);
                typeAccesService.creer(nouveau);
                JOptionPane.showMessageDialog(this, 
                    "Le type d'accès a été créé avec succès.", 
                    "Création réussie", JOptionPane.INFORMATION_MESSAGE);
            }
            
            // Rafraîchir la liste parente
            parentFrame.chargerDonnees();
            this.dispose();
            
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, 
                "Erreur de validation : " + ex.getMessage(), 
                "Erreur", JOptionPane.ERROR_MESSAGE);
        }
    }
}
