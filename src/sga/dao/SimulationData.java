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
        personnes.add(new Personne(1, "OUEDRAOGO", "Askia", "Admin SGA", "admin", "8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918", "ACTIF", 1));
        personnes.add(new Personne(2, "GUIGMA", "Christophe", "Garde Poste", "agent", "2f28148b11116c49e79435b6a715f5d3cb8b30364d081f9b0b4e5488eb742296", "ACTIF", 2));
        personnes.add(new Personne(3, "NAGALO", "Alexis", "Directeur SI", "dsi", "fd5a6df48c962b1aa33a595cb2dfa14a0f443cfbf98964d4d12521ab251218df", "ACTIF", 3));
        personnes.add(new Personne(4, "TRAORE", "Souleymane", "Étudiant IC1", "traore.s", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "ACTIF", 4));
        personnes.add(new Personne(5, "KABORE", "Fatoumata", "Étudiante IC1", "kabore.f", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "ACTIF", 4));
        personnes.add(new Personne(6, "SOW", "Ibrahim", "Enseignant Électricité", "sow.i", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "ACTIF", 5));
        personnes.add(new Personne(7, "ZONGO", "Mariam", "Secrétaire", "zongo.m", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "ACTIF", 0));
        personnes.add(new Personne(8, "COMPAORE", "Pierre", "Technicien", "compaore.p", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "ACTIF", 0));
        personnes.add(new Personne(9, "DIARRA", "Amadou", "Agent Sécurité Nuit", "diarra.a", "2f28148b11116c49e79435b6a715f5d3cb8b30364d081f9b0b4e5488eb742296", "ACTIF", 2));
        personnes.add(new Personne(10, "SAWADOGO", "Adama", "Stagiaire", "sawadogo.a", "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", "INACTIF", 0));

        // Initialiser les lieux (5 lieux différents)
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
