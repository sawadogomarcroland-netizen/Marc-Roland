package sga.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import sga.dao.ILieuDAO;
import sga.dao.LieuDAOImpl;
import sga.metier.Acces;
import sga.metier.Lieu;
import sga.metier.Personne;
import sga.metier.TypeAcces;
import sga.service.AccesService;
import sga.service.TypeAccesService;

public class FrmListeAcces extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtRecherche;
    private JComboBox<Lieu> cbLieux;
    private JComboBox<TypeAcces> cbTypes;
    private Personne utilisateurConnecte;

    private AccesService accesService = new AccesService();
    private TypeAccesService typeAccesService = new TypeAccesService();
    private ILieuDAO lieuDAO = new LieuDAOImpl();

    private List<Acces> listeActuelle;

    public FrmListeAcces(Personne utilisateur) {
        this.utilisateurConnecte = utilisateur;

        setTitle("SGA - Liste des Points d'Accès");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 560);
        setLocationRelativeTo(null);

        Color primaryColor = new Color(44, 62, 80);
        Color accentColor = new Color(24, 188, 156);
        Color accentOrange = new Color(230, 126, 34);
        Color deleteColor = new Color(192, 57, 43);
        Color bgColor = new Color(240, 244, 248);

        contentPane = new JPanel();
        contentPane.setBackground(bgColor);
        contentPane.setBorder(new EmptyBorder(15, 20, 15, 20));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Barre supérieure
        JLabel lblTitre = new JLabel("GESTION DES POINTS D'ACCÈS");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setBounds(20, 15, 400, 30);
        lblTitre.setForeground(primaryColor);
        contentPane.add(lblTitre);

        JButton btnRetour = new JButton("Retour au Menu");
        btnRetour.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRetour.setForeground(primaryColor);
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
        btnRetour.setFocusPainted(false);
        btnRetour.setBounds(780, 15, 130, 30);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnRetour);

        // Barre de filtres multicritères
        JPanel panelFiltres = new JPanel();
        panelFiltres.setBackground(Color.WHITE);
        panelFiltres.setBounds(20, 60, 890, 80);
        panelFiltres.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(220, 225, 230)), 
            "Filtres et Recherche multicritères", 
            0, 0, 
            new Font("Segoe UI", Font.BOLD, 11), 
            primaryColor
        ));
        panelFiltres.setLayout(null);
        contentPane.add(panelFiltres);

        JLabel lblRech = new JLabel("Libellé :");
        lblRech.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRech.setBounds(15, 30, 50, 25);
        panelFiltres.add(lblRech);

        txtRecherche = new JTextField();
        txtRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtRecherche.setBounds(70, 30, 180, 30);
        txtRecherche.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        panelFiltres.add(txtRecherche);

        JLabel lblLieu = new JLabel("Lieu :");
        lblLieu.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblLieu.setBounds(270, 30, 40, 25);
        panelFiltres.add(lblLieu);

        cbLieux = new JComboBox<>();
        cbLieux.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbLieux.setBounds(315, 30, 180, 30);
        panelFiltres.add(cbLieux);

        JLabel lblType = new JLabel("Type :");
        lblType.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblType.setBounds(515, 30, 40, 25);
        panelFiltres.add(lblType);

        cbTypes = new JComboBox<>();
        cbTypes.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbTypes.setBounds(560, 30, 180, 30);
        panelFiltres.add(cbTypes);

        JButton btnRechercher = new JButton("Rechercher");
        btnRechercher.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRechercher.setForeground(Color.WHITE);
        btnRechercher.setBackground(primaryColor);
        btnRechercher.setBorderPainted(false);
        btnRechercher.setFocusPainted(false);
        btnRechercher.setBounds(760, 30, 110, 30);
        btnRechercher.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelFiltres.add(btnRechercher);

        // Table des points d'accès
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 155, 890, 270);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] { "ID", "Libellé Point d'Accès", "Type d'accès", "Lieu associé", "Statut", "Date de création" }
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table.setModel(tableModel);
        
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(230);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(230);
        table.getColumnModel().getColumn(4).setPreferredWidth(80);
        table.getColumnModel().getColumn(5).setPreferredWidth(170);

        scrollPane.setViewportView(table);

        // Boutons CRUD et Détail
        JButton btnAjouter = new JButton("Ajouter un Point");
        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setBackground(accentColor);
        btnAjouter.setBorderPainted(false);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBounds(20, 445, 150, 40);
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnAjouter);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setBackground(primaryColor);
        btnModifier.setBorderPainted(false);
        btnModifier.setFocusPainted(false);
        btnModifier.setBounds(185, 445, 120, 40);
        btnModifier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnModifier);

        JButton btnDesactiver = new JButton("Désactiver");
        btnDesactiver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDesactiver.setForeground(Color.WHITE);
        btnDesactiver.setBackground(deleteColor);
        btnDesactiver.setBorderPainted(false);
        btnDesactiver.setFocusPainted(false);
        btnDesactiver.setBounds(320, 445, 130, 40);
        btnDesactiver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnDesactiver);

        JButton btnDetail = new JButton("Fiche Détail");
        btnDetail.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDetail.setForeground(Color.WHITE);
        btnDetail.setBackground(accentOrange);
        btnDetail.setBorderPainted(false);
        btnDetail.setFocusPainted(false);
        btnDetail.setBounds(465, 445, 130, 40);
        btnDetail.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnDetail);

        // Initialisations listes déroulantes filtres et données
        initFiltresCombo();
        chargerDonnees();

        // Listeners
        btnRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmMenuPrincipal menu = new FrmMenuPrincipal(utilisateurConnecte);
                menu.setVisible(true);
                FrmListeAcces.this.dispose();
            }
        });

        btnRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerDonnees();
            }
        });

        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmFormulaireAcces form = new FrmFormulaireAcces(FrmListeAcces.this, null, false);
                form.setVisible(true);
            }
        });

        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(FrmListeAcces.this, 
                        "Veuillez sélectionner un point d'accès dans le tableau.", 
                        "Sélection requise", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Acces selection = listeActuelle.get(selectedRow);
                FrmFormulaireAcces form = new FrmFormulaireAcces(FrmListeAcces.this, selection, true);
                form.setVisible(true);
            }
        });

        btnDesactiver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(FrmListeAcces.this, 
                        "Veuillez sélectionner un point d'accès dans le tableau.", 
                        "Sélection requise", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Acces selection = listeActuelle.get(selectedRow);
                
                int confirm = JOptionPane.showConfirmDialog(FrmListeAcces.this, 
                    "Êtes-vous sûr de vouloir désactiver le point d'accès '" + selection.getLibelle() + "' ?\n" +
                    "(Cette suppression est logique, le statut passera à Inactif)", 
                    "Confirmation de désactivation", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        accesService.desactiver(selection.getIdAcces());
                        JOptionPane.showMessageDialog(FrmListeAcces.this, 
                            "Le point d'accès a été désactivé avec succès.", 
                            "Opération réussie", JOptionPane.INFORMATION_MESSAGE);
                        chargerDonnees();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FrmListeAcces.this, 
                            "Erreur de désactivation : " + ex.getMessage(), 
                            "Erreur", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });

        btnDetail.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(FrmListeAcces.this, 
                        "Veuillez sélectionner un point d'accès dans le tableau.", 
                        "Sélection requise", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                Acces selection = listeActuelle.get(selectedRow);
                FrmDetailAcces detailDialog = new FrmDetailAcces(FrmListeAcces.this, selection);
                detailDialog.setVisible(true);
            }
        });
    }

    private void initFiltresCombo() {
        // Remplir cbLieux
        cbLieux.addItem(new Lieu(0, "-- Tous les lieux --", "", 0, "OUVERT"));
        List<Lieu> activeLieux = lieuDAO.listerLieuxActifs();
        for (Lieu l : activeLieux) {
            cbLieux.addItem(l);
        }

        // Remplir cbTypes
        cbTypes.addItem(new TypeAcces(0, "-- Tous les types --", "", "A", null));
        List<TypeAcces> activeTypes = typeAccesService.listerActifs();
        for (TypeAcces t : activeTypes) {
            cbTypes.addItem(t);
        }
    }

    public void chargerDonnees() {
        tableModel.setRowCount(0);

        String libelle = txtRecherche.getText().trim();
        
        Lieu selectedLieu = (Lieu) cbLieux.getSelectedItem();
        int idLieu = (selectedLieu != null) ? selectedLieu.getIdLieu() : 0;

        TypeAcces selectedType = (TypeAcces) cbTypes.getSelectedItem();
        int idType = (selectedType != null) ? selectedType.getIdTypeAcces() : 0;

        listeActuelle = accesService.rechercherMulticritere(libelle, idType, idLieu);

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (Acces a : listeActuelle) {
            String dateStr = (a.getDateCreation() != null) ? sdf.format(a.getDateCreation()) : "N/A";
            tableModel.addRow(new Object[] {
                a.getIdAcces(),
                a.getLibelle(),
                a.getTypeLibelle() != null ? a.getTypeLibelle() : "N/A",
                a.getLieuLibelle() != null ? a.getLieuLibelle() : "N/A",
                "A".equals(a.getStatut()) ? "Actif" : "Inactif",
                dateStr
            });
        }
    }
}
