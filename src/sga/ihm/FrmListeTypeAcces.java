package sga.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import sga.metier.Personne;
import sga.metier.TypeAcces;
import sga.service.TypeAccesService;

public class FrmListeTypeAcces extends JFrame {
    private static final long serialVersionUID = 1L;

    private JPanel contentPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtRecherche;
    private TypeAccesService typeAccesService = new TypeAccesService();
    private Personne utilisateurConnecte;

    private List<TypeAcces> listeActuelle;

    public FrmListeTypeAcces(Personne utilisateur) {
        this.utilisateurConnecte = utilisateur;
        
        setTitle("SGA - Liste des Types d'Accès");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 550);
        setLocationRelativeTo(null);

        Color primaryColor = new Color(44, 62, 80); // Dark Blue
        Color accentColor = new Color(24, 188, 156); // Teal
        Color deleteColor = new Color(192, 57, 43); // Red
        Color bgColor = new Color(240, 244, 248);

        contentPane = new JPanel();
        contentPane.setBackground(bgColor);
        contentPane.setBorder(new EmptyBorder(15, 20, 15, 20));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Barre supérieure (Titre + Retour)
        JLabel lblTitre = new JLabel("GESTION DES TYPES D'ACCÈS");
        lblTitre.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitre.setForeground(primaryColor);
        lblTitre.setBounds(20, 15, 400, 30);
        contentPane.add(lblTitre);

        JButton btnRetour = new JButton("Retour au Menu");
        btnRetour.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRetour.setForeground(primaryColor);
        btnRetour.setBackground(Color.WHITE);
        btnRetour.setBorder(BorderFactory.createLineBorder(primaryColor, 1));
        btnRetour.setFocusPainted(false);
        btnRetour.setBounds(730, 15, 130, 30);
        btnRetour.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnRetour);

        // Zone de recherche
        JLabel lblRecherche = new JLabel("Rechercher par libellé :");
        lblRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblRecherche.setBounds(20, 65, 150, 25);
        contentPane.add(lblRecherche);

        txtRecherche = new JTextField();
        txtRecherche.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtRecherche.setBounds(170, 65, 250, 30);
        txtRecherche.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200)),
            BorderFactory.createEmptyBorder(2, 5, 2, 5)
        ));
        contentPane.add(txtRecherche);

        JButton btnRechercher = new JButton("Filtrer");
        btnRechercher.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRechercher.setForeground(Color.WHITE);
        btnRechercher.setBackground(primaryColor);
        btnRechercher.setBorderPainted(false);
        btnRechercher.setFocusPainted(false);
        btnRechercher.setBounds(430, 65, 90, 30);
        btnRechercher.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnRechercher);

        // Table
        JScrollPane scrollPane = new JScrollPane();
        scrollPane.setBounds(20, 110, 840, 310);
        contentPane.add(scrollPane);

        table = new JTable();
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        tableModel = new DefaultTableModel(
            new Object[][] {},
            new String[] { "N° (ID)", "Libellé", "Description", "Date de création" }
        ) {
            private static final long serialVersionUID = 1L;
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Lecture seule
            }
        };
        table.setModel(tableModel);
        
        // Ajustement des largeurs de colonnes
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(200);
        table.getColumnModel().getColumn(2).setPreferredWidth(360);
        table.getColumnModel().getColumn(3).setPreferredWidth(200);
        
        scrollPane.setViewportView(table);

        // Boutons d'action CRUD
        JButton btnAjouter = new JButton("Ajouter un Type");
        btnAjouter.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnAjouter.setForeground(Color.WHITE);
        btnAjouter.setBackground(accentColor);
        btnAjouter.setBorderPainted(false);
        btnAjouter.setFocusPainted(false);
        btnAjouter.setBounds(20, 440, 150, 40);
        btnAjouter.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnAjouter);

        JButton btnModifier = new JButton("Modifier");
        btnModifier.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnModifier.setForeground(Color.WHITE);
        btnModifier.setBackground(primaryColor);
        btnModifier.setBorderPainted(false);
        btnModifier.setFocusPainted(false);
        btnModifier.setBounds(185, 440, 130, 40);
        btnModifier.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnModifier);

        JButton btnDesactiver = new JButton("Désactiver (Supprimer)");
        btnDesactiver.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDesactiver.setForeground(Color.WHITE);
        btnDesactiver.setBackground(deleteColor);
        btnDesactiver.setBorderPainted(false);
        btnDesactiver.setFocusPainted(false);
        btnDesactiver.setBounds(330, 440, 190, 40);
        btnDesactiver.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnDesactiver);

        // Charger les données initiales
        chargerDonnees();

        // Listeners
        btnRetour.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmMenuPrincipal menu = new FrmMenuPrincipal(utilisateurConnecte);
                menu.setVisible(true);
                FrmListeTypeAcces.this.dispose();
            }
        });

        btnRechercher.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                chargerDonnees();
            }
        });

        txtRecherche.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    chargerDonnees();
                }
            }
        });

        btnAjouter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Ouvrir formulaire en mode création (isEdition = false)
                FrmFormulaireTypeAcces form = new FrmFormulaireTypeAcces(FrmListeTypeAcces.this, null, false);
                form.setVisible(true);
            }
        });

        btnModifier.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(FrmListeTypeAcces.this, 
                        "Veuillez sélectionner un type d'accès dans le tableau.", 
                        "Sélection requise", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                TypeAcces selection = listeActuelle.get(selectedRow);
                // Ouvrir formulaire en mode édition (isEdition = true)
                FrmFormulaireTypeAcces form = new FrmFormulaireTypeAcces(FrmListeTypeAcces.this, selection, true);
                form.setVisible(true);
            }
        });

        btnDesactiver.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow < 0) {
                    JOptionPane.showMessageDialog(FrmListeTypeAcces.this, 
                        "Veuillez sélectionner un type d'accès dans le tableau.", 
                        "Sélection requise", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                TypeAcces selection = listeActuelle.get(selectedRow);
                
                int confirm = JOptionPane.showConfirmDialog(FrmListeTypeAcces.this, 
                    "Êtes-vous sûr de vouloir désactiver le type d'accès '" + selection.getLibelle() + "' ?\n" +
                    "(Cette suppression est logique, aucune donnée physique ne sera supprimée)", 
                    "Confirmation de désactivation", JOptionPane.YES_NO_OPTION);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        typeAccesService.desactiver(selection.getIdTypeAcces());
                        JOptionPane.showMessageDialog(FrmListeTypeAcces.this, 
                            "Le type d'accès a été désactivé avec succès.", 
                            "Désactivation réussie", JOptionPane.INFORMATION_MESSAGE);
                        chargerDonnees();
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(FrmListeTypeAcces.this, 
                            "Erreur de désactivation : " + ex.getMessage(), 
                            "Opération interdite", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        });
    }

    public void chargerDonnees() {
        tableModel.setRowCount(0);
        String recherche = txtRecherche.getText().trim();
        if (recherche.isEmpty()) {
            listeActuelle = typeAccesService.listerActifs();
        } else {
            listeActuelle = typeAccesService.rechercher(recherche);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (TypeAcces t : listeActuelle) {
            String dateStr = (t.getDateCreation() != null) ? sdf.format(t.getDateCreation()) : "N/A";
            tableModel.addRow(new Object[] {
                t.getIdTypeAcces(),
                t.getLibelle(),
                t.getDescription() != null ? t.getDescription() : "",
                dateStr
            });
        }
    }
}
