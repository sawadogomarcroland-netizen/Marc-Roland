package sga.ihm;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import sga.dao.IPersonneDAO;
import sga.dao.PersonneDAOImpl;
import sga.metier.Personne;
import sga.util.ConnexionBD;
import sga.util.HashUtil;

public class FrmConnexion extends JFrame {
    private static final long serialVersionUID = 1L;
    
    private JPanel contentPane;
    private JTextField txtLogin;
    private JPasswordField txtPassword;
    private JLabel lblMessageErreur;
    private JToggleButton btnModeSimu;
    private IPersonneDAO personneDAO = new PersonneDAOImpl();

    public FrmConnexion() {
        setTitle("SGA - Authentification");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 480);
        setLocationRelativeTo(null);
        setResizable(false);

        // Palette de couleurs premium
        Color primaryColor = new Color(44, 62, 80); // Slate Blue
        Color accentColor = new Color(24, 188, 156); // Teal
        Color bgColor = new Color(245, 247, 250); // Light Gray
        Color textColor = new Color(51, 51, 51);

        contentPane = new JPanel();
        contentPane.setBackground(bgColor);
        contentPane.setBorder(new EmptyBorder(20, 30, 20, 30));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        // En-tête / Logo
        JPanel panelLogo = new JPanel();
        panelLogo.setBackground(primaryColor);
        panelLogo.setBounds(0, 0, 450, 100);
        contentPane.add(panelLogo);
        panelLogo.setLayout(null);

        JLabel lblLogo = new JLabel("ISGE-BF");
        lblLogo.setForeground(Color.WHITE);
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblLogo.setHorizontalAlignment(SwingConstants.CENTER);
        lblLogo.setBounds(10, 20, 430, 35);
        panelLogo.add(lblLogo);

        JLabel lblSousTitre = new JLabel("Système de Gestion d'Accès (SGA)");
        lblSousTitre.setForeground(new Color(200, 220, 240));
        lblSousTitre.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lblSousTitre.setHorizontalAlignment(SwingConstants.CENTER);
        lblSousTitre.setBounds(10, 55, 430, 25);
        panelLogo.add(lblSousTitre);

        // Formulaire
        JLabel lblLogin = new JLabel("Nom d'utilisateur (Login) :");
        lblLogin.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblLogin.setForeground(textColor);
        lblLogin.setBounds(50, 130, 350, 25);
        contentPane.add(lblLogin);

        txtLogin = new JTextField();
        txtLogin.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtLogin.setBounds(50, 160, 350, 35);
        txtLogin.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        contentPane.add(txtLogin);
        txtLogin.setColumns(10);

        JLabel lblPassword = new JLabel("Mot de passe :");
        lblPassword.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lblPassword.setForeground(textColor);
        lblPassword.setBounds(50, 210, 350, 25);
        contentPane.add(lblPassword);

        txtPassword = new JPasswordField();
        txtPassword.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txtPassword.setBounds(50, 240, 350, 35);
        txtPassword.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        contentPane.add(txtPassword);

        // Bouton de connexion
        JButton btnLogin = new JButton("Se connecter");
        btnLogin.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setBackground(accentColor);
        btnLogin.setBorderPainted(false);
        btnLogin.setFocusPainted(false);
        btnLogin.setBounds(50, 310, 350, 40);
        btnLogin.setCursor(new Cursor(Cursor.HAND_CURSOR));
        contentPane.add(btnLogin);

        // Message d'erreur
        lblMessageErreur = new JLabel("");
        lblMessageErreur.setForeground(new Color(231, 76, 60)); // Red
        lblMessageErreur.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblMessageErreur.setHorizontalAlignment(SwingConstants.CENTER);
        lblMessageErreur.setBounds(50, 285, 350, 20);
        contentPane.add(lblMessageErreur);

        // Indicateur Mode Simulation
        boolean isSimu = ConnexionBD.isSimulationMode();
        btnModeSimu = new JToggleButton(isSimu ? "Mode : Simulation (Mémoire)" : "Mode : Base Oracle (JDBC)");
        btnModeSimu.setSelected(isSimu);
        btnModeSimu.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        btnModeSimu.setForeground(isSimu ? new Color(230, 126, 34) : primaryColor);
        btnModeSimu.setBounds(50, 380, 350, 30);
        btnModeSimu.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnModeSimu.setFocusPainted(false);
        contentPane.add(btnModeSimu);

        // Listeners
        btnModeSimu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                boolean simSelected = btnModeSimu.isSelected();
                ConnexionBD.setSimulationMode(simSelected);
                if (simSelected) {
                    btnModeSimu.setText("Mode : Simulation (Mémoire)");
                    btnModeSimu.setForeground(new Color(230, 126, 34));
                } else {
                    // Tente de se connecter
                    java.sql.Connection c = ConnexionBD.getConnexion();
                    if (ConnexionBD.isSimulationMode()) {
                        btnModeSimu.setSelected(true);
                        btnModeSimu.setText("Mode : Simulation (Échec Connexion BDD)");
                        btnModeSimu.setForeground(new Color(231, 76, 60));
                        JOptionPane.showMessageDialog(FrmConnexion.this, 
                            "Impossible de se connecter à la base de données Oracle.\nLe mode simulation reste actif.", 
                            "Erreur de connexion", JOptionPane.WARNING_MESSAGE);
                    } else {
                        btnModeSimu.setText("Mode : Base Oracle (JDBC)");
                        btnModeSimu.setForeground(primaryColor);
                    }
                }
            }
        });

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionConnexion();
            }
        });

        // Permettre la validation par la touche Entrée
        txtPassword.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                actionConnexion();
            }
        });
    }

    private void actionConnexion() {
        String login = txtLogin.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (login.isEmpty() || password.isEmpty()) {
            lblMessageErreur.setText("Veuillez saisir votre login et votre mot de passe.");
            return;
        }

        // Hacher le mot de passe avant vérification (CF-07)
        String pwdHash = HashUtil.hashSHA256(password);

        // Authentification
        Personne p = personneDAO.authentifier(login, pwdHash);

        if (p != null) {
            lblMessageErreur.setText("");
            // Ouvrir le menu principal
            FrmMenuPrincipal menu = new FrmMenuPrincipal(p);
            menu.setVisible(true);
            this.dispose();
        } else {
            lblMessageErreur.setText("Login ou mot de passe incorrect, ou compte inactif.");
        }
    }
}
