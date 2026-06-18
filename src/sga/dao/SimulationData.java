package sga.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import sga.metier.*;

public class SimulationData {
    public static List<Personne> personnes = new ArrayList<>();
    public static List<Lieu> lieux = new ArrayList<>();
    public static List<TypeAcces> types = new ArrayList<>();
    public static List<Acces> accesList = new ArrayList<>();

    static {
        // Initialiser les profils et personnes (10 personnes, mot de passe hashés)
        // CORRIGE : hash SHA-256 alignés sur ceux du script SQL officiel
        // (sga_lot1_officiel.sql), afin que le mode Simulation et le mode Oracle
        // acceptent exactement les mêmes identifiants de test.
        //
        // login        | mot de passe | hash SHA-256
        // admin        | admin        | 8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918
        // agent        | agent        | d4f0bc5a29de06b510f9aa428f1eedba926012b591fef7a518e776a7c9bd1824
        // dsi          | dsi          | c8b8e57e7471cb8dd918801e517391c41011975f4f5d7fbe4bc5f80435fe12b4
        // traore.s     | traore.s     | 867cb3736ba2ba06f8ce9b4207ef2334fff77c85f1efe326a8be742e91ef0941
        // kabore.f     | kabore.f     | c7acf6901a2d06c79bb68d5f63f9dac4d59e46882e6430ce19506edb94315cfc
        // sow.i        | sow.i        | ae73c6d6599b4664e7bb41a50acadf2519ad26412265c3e903481bb3c2a53cb3
        // zongo.m      | zongo.m      | 50bfcb178d337bac723e3f03e1bcf2d4e85332db8d03517742f576d7065f2044
        // compaore.p   | compaore.p   | 6bb3c90e8af1a14c487cc1992b5991aa1791c2ffc2744b1d73361369cd4b3711
        // diarra.a     | agent        | d4f0bc5a29de06b510f9aa428f1eedba926012b591fef7a518e776a7c9bd1824 (rôle partagé)
        // sawadogo.a   | sawadogo.a   | 857be33c32aa11d06046044d20a9b10f97120c1c674eb75f9e76d5ec4941393c (statut INACTIF)
        personnes.add(new Personne(1, "OUEDRAOGO", "Askia", "Admin SGA", "admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918", "ACTIF", 1));
        personnes.add(new Personne(2, "GUIGMA", "Christophe", "Garde Poste", "agent", "d4f0bc5a29de06b510f9aa428f1eedba926012b591fef7a518e776a7c9bd1824", "ACTIF", 2));
        personnes.add(new Personne(3, "NAGALO", "Alexis", "Directeur SI", "dsi", "c8b8e57e7471cb8dd918801e517391c41011975f4f5d7fbe4bc5f80435fe12b4", "ACTIF", 3));
        personnes.add(new Personne(4, "TRAORE", "Souleymane", "Étudiant IC1", "traore.s", "867cb3736ba2ba06f8ce9b4207ef2334fff77c85f1efe326a8be742e91ef0941", "ACTIF", 4));
        personnes.add(new Personne(5, "KABORE", "Fatoumata", "Étudiante IC1", "kabore.f", "c7acf6901a2d06c79bb68d5f63f9dac4d59e46882e6430ce19506edb94315cfc", "ACTIF", 4));
        personnes.add(new Personne(6, "SOW", "Ibrahim", "Enseignant Électricité", "sow.i", "ae73c6d6599b4664e7bb41a50acadf2519ad26412265c3e903481bb3c2a53cb3", "ACTIF", 5));
        personnes.add(new Personne(7, "ZONGO", "Mariam", "Secrétaire", "zongo.m", "50bfcb178d337bac723e3f03e1bcf2d4e85332db8d03517742f576d7065f2044", "ACTIF", 0));
        personnes.add(new Personne(8, "COMPAORE", "Pierre", "Technicien", "compaore.p", "6bb3c90e8af1a14c487cc1992b5991aa1791c2ffc2744b1d73361369cd4b3711", "ACTIF", 0));
        personnes.add(new Personne(9, "DIARRA", "Amadou", "Agent Sécurité Nuit", "diarra.a", "d4f0bc5a29de06b510f9aa428f1eedba926012b591fef7a518e776a7c9bd1824", "ACTIF", 2));
        personnes.add(new Personne(10, "SAWADOGO", "Adama", "Stagiaire", "sawadogo.a", "857be33c32aa11d06046044d20a9b10f97120c1c674eb75f9e76d5ec4941393c", "INACTIF", 0));

        // Initialiser les lieux (5 lieux différents)
        // Remarque : "OUVERT" ici est une valeur interne au mode Simulation uniquement
        // (la vraie base Oracle utilise 'O'/'F' - cf. LieuDAOImpl). Cohérent en l'état
        // car listerLieuxActifs() compare avec "OUVERT" dans la branche simulation.
        lieux.add(new Lieu(1, "Salle A101 (Cours)", "Bâtiment Principal 1er étage", 50, "OUVERT"));
        lieux.add(new Lieu(2, "Labo Réseaux", "Bloc B RDC", 20, "OUVERT"));
        lieux.add(new Lieu(3, "Parking Nord", "Extérieur Côté Administratif", 100, "OUVERT"));
        lieux.add(new Lieu(4, "Administration", "Bâtiment A RDC", 15, "OUVERT"));
        lieux.add(new Lieu(5, "Salle des Serveurs", "Sous-sol Bloc A", 2, "OUVERT"));

        // Initialiser les types d'accès (5 types prédéfinis)
        types.add(new TypeAcces(1, "PORTE", "Porte battante avec verrouillage magnétique", "A", new Date()));
        types.add(new TypeAcces(2, "BARRIERE", "Barrière levante automatique", "A", new Date()));
        types.add(new TypeAcces(3, "PORTILLON", "Portillon pivotant de sécurité", "A", new Date()));
        types.add(new TypeAcces(4, "PARKING", "Portail d'entrée de parking avec détecteur", "A", new Date()));
        types.add(new TypeAcces(5, "ASCENSEUR", "Contrôle d'accès cabine ascenseur", "A", new Date()));

        // Initialiser les points d'accès (8 points d'accès répartis)
        accesList.add(new Acces(1, "Porte Principale Entrée A", "Entrée principale de l'administration", 1, "PORTE", 4, "Administration", "A", new Date(), null));
        accesList.add(new Acces(2, "Barrière Véhicules Nord", "Barrière d'accès au parking", 2, "BARRIERE", 3, "Parking Nord", "A", new Date(), null));
        accesList.add(new Acces(3, "Portillon Piétons Administration", "Accès piéton filtré", 3, "PORTILLON", 4, "Administration", "A", new Date(), null));
        accesList.add(new Acces(4, "Ascenseur Bâtiment Principal", "Contrôle cabine ascenseur", 5, "ASCENSEUR", 1, "Salle A101 (Cours)", "A", new Date(), null));
        accesList.add(new Acces(5, "Porte Labo Réseaux", "Porte d'accès au labo", 1, "PORTE", 2, "Labo Réseaux", "A", new Date(), null));
        accesList.add(new Acces(6, "Porte Blindée Serveurs", "Sécurité renforcée salle serveurs", 1, "PORTE", 5, "Salle des Serveurs", "A", new Date(), null));
        accesList.add(new Acces(7, "Portail Sortie Parking", "Portail de sortie automatique", 4, "PARKING", 3, "Parking Nord", "A", new Date(), null));
        accesList.add(new Acces(8, "Porte Secours A101", "Porte de sortie de secours", 1, "PORTE", 1, "Salle A101 (Cours)", "A", new Date(), null));
    }
}