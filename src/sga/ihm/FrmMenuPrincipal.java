package sga.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.metal.MetalButtonUI;
import sga.metier.Personne;

public class FrmMenuPrincipal extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private Personne utilisateurConnecte;

    public FrmMenuPrincipal(Personne utilisateur) {
        this.utilisateurConnecte = utilisateur;

        setTitle("SGA - Menu Principal");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null);
        setResizable(false);

        Color primaryColor = new Color(44, 62, 80); // Dark Blue
        Color accentColor = new Color(22, 199, 154); // Teal (vif)
        Color accentOrange = new Color(243, 132, 27); // Orange (vif)
        Color bgColor = new Color(240, 244, 248); // Slate Light Gray

        contentPane = new JPanel();
        contentPane.setBackground(bgColor);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Sidebar de gauche (Profil utilisateur & Déconnexion)
        JPanel panelSidebar = new JPanel();
        panelSidebar.setBackground(primaryColor);
        panelSidebar.setBounds(0, 0, 250, 500);
        contentPane.add(panelSidebar);
        panelSidebar.setLayout(null);

        JLabel lblSga = new JLabel("SGA ISGE-BF");
        lblSga.setForeground(Color.WHITE);
        lblSga.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblSga.setHorizontalAlignment(SwingConstants.CENTER);
        lblSga.setBounds(10, 30, 230, 30);
        panelSidebar.add(lblSga);

        JSeparator separator = new JSeparator();
        separator.setForeground(new Color(100, 120, 140));
        separator.setBounds(20, 75, 210, 2);
        panelSidebar.add(separator);

        JLabel lblBienvenue = new JLabel("Utilisateur connecté :");
        lblBienvenue.setForeground(new Color(200, 220, 240));
        lblBienvenue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblBienvenue.setBounds(20, 120, 210, 20);
        panelSidebar.add(lblBienvenue);

        String nomComplet = utilisateur.getPrenom() + " " + utilisateur.getNom();
        JLabel lblNomUser = new JLabel(nomComplet);
        lblNomUser.setForeground(Color.WHITE);
        lblNomUser.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblNomUser.setBounds(20, 140, 210, 25);
        panelSidebar.add(lblNomUser);

        JLabel lblRole = new JLabel("Fonction : " + (utilisateur.getFonction() != null ? utilisateur.getFonction() : "N/A"));
        lblRole.setForeground(new Color(200, 220, 240));
        lblRole.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        lblRole.setBounds(20, 170, 210, 20);
        panelSidebar.add(lblRole);

        JButton btnDeconnexion = new JButton("Se déconnecter");
        btnDeconnexion.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnDeconnexion.setForeground(Color.WHITE);
        btnDeconnexion.setBackground(new Color(217, 30, 24)); // Rouge vif
        btnDeconnexion.setBorderPainted(false);
        btnDeconnexion.setFocusPainted(false);
        btnDeconnexion.setOpaque(true);
        btnDeconnexion.setUI(new MetalButtonUI()); // CORRIGE : force le rendu Metal pour respecter les couleurs
        btnDeconnexion.setBounds(20, 400, 210, 40);
        btnDeconnexion.setCursor(new Cursor(Cursor.HAND_CURSOR));
        panelSidebar.add(btnDeconnexion);

        // Zone de contenu principale (Droite)
        JLabel lblTitreMenu = new JLabel("TABLEAU DE BORD - LOT 1 GESTION DES ACCÈS");
        lblTitreMenu.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTitreMenu.setForeground(primaryColor);
        lblTitreMenu.setBounds(280, 40, 480, 30);
        contentPane.add(lblTitreMenu);

        JLabel lblDescription = new JLabel("Sélectionnez le module de gestion que vous souhaitez administrer :");
        lblDescription.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        lblDescription.setForeground(new Color(100, 110, 120));
        lblDescription.setBounds(280, 80, 480, 20);
        contentPane.add(lblDescription);

        // Bouton Module A : Types d'Accès
        JButton btnModuleTypes = new JButton("<html><center><b>MODULE A</b><br><br>Gestion des Types d'Accès<br><small>(Porte, Barrière, Ascenseur...)</small></center></html>");
        btnModuleTypes.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnModuleTypes.setForeground(Color.WHITE);
        btnModuleTypes.setBackground(accentColor);
        btnModuleTypes.setBorderPainted(false);
        btnModuleTypes.setFocusPainted(false);
        btnModuleTypes.setOpaque(true);
        btnModuleTypes.setUI(new MetalButtonUI()); // CORRIGE : force le rendu Metal pour respecter les couleurs
        btnModuleTypes.setBounds(290, 150, 220, 180);
        btnModuleTypes.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnModuleTypes);

        // Bouton Module B : Points d'Accès
        JButton btnModulePoints = new JButton("<html><center><b>MODULE B</b><br><br>Gestion des Points d'Accès<br><small>(Liaisons Lieux ↔ Types d'accès)</small></center></html>");
        btnModulePoints.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        btnModulePoints.setForeground(Color.WHITE);
        btnModulePoints.setBackground(accentOrange);
        btnModulePoints.setBorderPainted(false);
        btnModulePoints.setFocusPainted(false);
        btnModulePoints.setOpaque(true);
        btnModulePoints.setUI(new MetalButtonUI()); // CORRIGE : force le rendu Metal pour respecter les couleurs
        btnModulePoints.setBounds(540, 150, 220, 180);
        btnModulePoints.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnModulePoints);

        // Pied de page
        JLabel lblEcole = new JLabel("ISGE-BF - Institut Supérieur de Génie Électrique du Burkina Faso");
        lblEcole.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        lblEcole.setForeground(new Color(150, 150, 150));
        lblEcole.setHorizontalAlignment(SwingConstants.CENTER);
        lblEcole.setBounds(280, 430, 480, 20);
        contentPane.add(lblEcole);

        // Actions
        btnDeconnexion.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmConnexion connFrame = new FrmConnexion();
                connFrame.setVisible(true);
                FrmMenuPrincipal.this.dispose();
            }
        });

        btnModuleTypes.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmListeTypeAcces frameTypes = new FrmListeTypeAcces(utilisateurConnecte);
                frameTypes.setVisible(true);
                FrmMenuPrincipal.this.dispose();
            }
        });

        btnModulePoints.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmListeAcces frameAcces = new FrmListeAcces(utilisateurConnecte);
                frameAcces.setVisible(true);
                FrmMenuPrincipal.this.dispose();
            }
        });
    }
}