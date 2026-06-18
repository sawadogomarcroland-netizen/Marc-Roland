package sga.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import sga.dao.ILieuDAO;
import sga.dao.LieuDAOImpl;
import sga.metier.Acces;
import sga.metier.Lieu;
import sga.metier.TypeAcces;
import sga.service.AccesService;
import sga.service.TypeAccesService;

public class FrmFormulaireAcces extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JPanel contentPanel = new JPanel();
    private JTextField txtLibelle;
    private JTextArea txtDescription;
    private JComboBox<TypeAcces> cbTypes;
    private JComboBox<Lieu> cbLieux;
    private JLabel lblTitreForm;

    private FrmListeAcces parentFrame;
    private Acces accesModif;
    private boolean isEdition;

    private AccesService accesService = new AccesService();
    private TypeAccesService typeAccesService = new TypeAccesService();
    private ILieuDAO lieuDAO = new LieuDAOImpl();

    private Border defaultBorder;

    public FrmFormulaireAcces(FrmListeAcces parent, Acces acces, boolean edition) {
        super(parent, true);
        this.parentFrame = parent;
        this.accesModif = acces;
        this.isEdition = edition;

        setTitle(edition ? "SGA - Modifier Point d'Accès" : "SGA - Ajouter Point d'Accès");
        setBounds(100, 100, 500, 480);
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

        // Titre
        lblTitreForm = new JLabel(edition ? "MODIFIER UN POINT D'ACCÈS" : "CRÉER UN NOUVEAU POINT D'ACCÈS");
        lblTitreForm.setFont(new Font("Segoe UI", Font.BOLD, 15));
        lblTitreForm.setForeground(primaryColor);
        lblTitreForm.setBounds(20, 15, 440, 25);
        contentPanel.add(lblTitreForm);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(210, 220, 230));
        separator.setBounds(20, 45, 440, 2);
        contentPanel.add(separator);

        // Libellé (Obligatoire)
        JLabel lblLibelle = new JLabel("Libellé du point d'accès * :");
        lblLibelle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLibelle.setForeground(textColor);
        lblLibelle.setBounds(20, 60, 440, 20);
        contentPanel.add(lblLibelle);

        txtLibelle = new JTextField();
        txtLibelle.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtLibelle.setBounds(20, 85, 440, 30);
        defaultBorder = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        );
        txtLibelle.setBorder(defaultBorder);
        contentPanel.add(txtLibelle);

        // Type d'accès (Obligatoire)
        JLabel lblType = new JLabel("Type d'accès * :");
        lblType.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblType.setForeground(textColor);
        lblType.setBounds(20, 130, 210, 20);
        contentPanel.add(lblType);

        cbTypes = new JComboBox<>();
        cbTypes.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbTypes.setBounds(20, 155, 210, 30);
        contentPanel.add(cbTypes);

        // Lieu associé (Obligatoire)
        JLabel lblLieu = new JLabel("Lieu associé * :");
        lblLieu.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLieu.setForeground(textColor);
        lblLieu.setBounds(250, 130, 210, 20);
        contentPanel.add(lblLieu);

        cbLieux = new JComboBox<>();
        cbLieux.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cbLieux.setBounds(250, 155, 210, 30);
        contentPanel.add(cbLieux);

        // Description (Optionnelle)
        JLabel lblDescription = new JLabel("Description :");
        lblDescription.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblDescription.setForeground(textColor);
        lblDescription.setBounds(20, 200, 440, 20);
        contentPanel.add(lblDescription);

        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 225, 440, 80);
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
        btnEnregistrer.setOpaque(true);
        btnEnregistrer.setUI(new MetalButtonUI()); // CORRIGE : force le rendu pour respecter les couleurs
        btnEnregistrer.setBounds(20, 375, 130, 35);
        btnEnregistrer.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(btnEnregistrer);

        JButton btnAnnuler = new JButton("Annuler");
        btnAnnuler.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAnnuler.setForeground(primaryColor);
        btnAnnuler.setBackground(Color.WHITE);
        btnAnnuler.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
        btnAnnuler.setFocusPainted(false);
        btnAnnuler.setBounds(165, 375, 110, 35);
        btnAnnuler.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPanel.add(btnAnnuler);

        // Charger les ComboBoxes
        chargerComboBoxes();

        // Remplir si édition
        if (edition && acces != null) {
            txtLibelle.setText(acces.getLibelle());
            txtDescription.setText(acces.getDescription() != null ? acces.getDescription() : "");

            // Sélectionner le type
            for (int i = 0; i < cbTypes.getItemCount(); i++) {
                if (cbTypes.getItemAt(i).getIdTypeAcces() == acces.getIdTypeAcces()) {
                    cbTypes.setSelectedIndex(i);
                    break;
                }
            }

            // Sélectionner le lieu
            for (int i = 0; i < cbLieux.getItemCount(); i++) {
                if (cbLieux.getItemAt(i).getIdLieu() == acces.getIdLieu()) {
                    cbLieux.setSelectedIndex(i);
                    break;
                }
            }
        }

        // Actions
        btnAnnuler.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmFormulaireAcces.this.dispose();
            }
        });

        btnEnregistrer.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enregistrer();
            }
        });
    }

    private void chargerComboBoxes() {
        // Types
        cbTypes.addItem(new TypeAcces(0, "-- Choisir un type --", "", "A", null));
        List<TypeAcces> activeTypes = typeAccesService.listerActifs();
        for (TypeAcces t : activeTypes) {
            cbTypes.addItem(t);
        }

        // Lieux
        cbLieux.addItem(new Lieu(0, "-- Choisir un lieu --", "", 0, "OUVERT"));
        List<Lieu> activeLieux = lieuDAO.listerLieuxActifs();
        for (Lieu l : activeLieux) {
            cbLieux.addItem(l);
        }
    }

    private void enregistrer() {
        String libelle = txtLibelle.getText().trim();
        String description = txtDescription.getText().trim();

        TypeAcces selectedType = (TypeAcces) cbTypes.getSelectedItem();
        int idType = (selectedType != null) ? selectedType.getIdTypeAcces() : 0;

        Lieu selectedLieu = (Lieu) cbLieux.getSelectedItem();
        int idLieu = (selectedLieu != null) ? selectedLieu.getIdLieu() : 0;

        boolean valid = true;

        // Validation visuelle "Champ rouge si vide et obligatoire"
        if (libelle.isEmpty()) {
            txtLibelle.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.RED, 1),
                BorderFactory.createEmptyBorder(2, 5, 2, 5)
            ));
            valid = false;
        } else {
            txtLibelle.setBorder(defaultBorder);
        }

        if (idType == 0) {
            // Un petit indicateur visuel rouge sur la combobox en la dessinant avec une bordure rouge
            cbTypes.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            valid = false;
        } else {
            cbTypes.setBorder(null);
        }

        if (idLieu == 0) {
            cbLieux.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            valid = false;
        } else {
            cbLieux.setBorder(null);
        }

        if (!valid) {
            JOptionPane.showMessageDialog(this,
                "Veuillez renseigner tous les champs obligatoires (indiqués par une bordure rouge).",
                "Champs manquants", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            if (isEdition) {
                accesModif.setLibelle(libelle);
                accesModif.setDescription(description.isEmpty() ? null : description);
                accesModif.setIdTypeAcces(idType);
                accesModif.setIdLieu(idLieu);
                accesService.modifier(accesModif);
                JOptionPane.showMessageDialog(this,
                    "Le point d'accès a été modifié avec succès.",
                    "Modification réussie", JOptionPane.INFORMATION_MESSAGE);
            } else {
                Acces nouveau = new Acces();
                nouveau.setLibelle(libelle);
                nouveau.setDescription(description.isEmpty() ? null : description);
                nouveau.setIdTypeAcces(idType);
                nouveau.setIdLieu(idLieu);
                accesService.creer(nouveau);
                JOptionPane.showMessageDialog(this,
                    "Le point d'accès a été créé avec succès.",
                    "Création réussie", JOptionPane.INFORMATION_MESSAGE);
            }

            parentFrame.chargerDonnees();
            this.dispose();

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                "Erreur lors de l'enregistrement : " + ex.getMessage(),
                "Erreur métier", JOptionPane.ERROR_MESSAGE);
        }
    }
}