package sga.dao;

import java.util.List;
import sga.metier.TypeAcces;

public interface ITypeAccesDAO {
    /**
     * Récupère la liste de tous les types d'accès actifs ('A').
     */
    List<TypeAcces> listerActifs();

    /**
     * Crée un nouveau type d'accès.
     */
    boolean creer(TypeAcces type);

    /**
     * Vérifie si un libellé de type d'accès existe déjà dans le système.
     */
    boolean verifierDoublon(String libelle);

    /**
     * Modifie un type d'accès existant.
     */
    boolean modifier(TypeAcces type);

    /**
     * Vérifie si le type d'accès est actuellement utilisé par des accès actifs.
     */
    boolean verifierUsage(int idTypeAcces);

    /**
     * Désactive logiquement (statut = 'I') un type d'accès.
     */
    boolean desactiver(int idTypeAcces);

    /**
     * Recherche les types d'accès actifs par libellé (recherche partielle insensible à la casse).
     */
    List<TypeAcces> rechercherParLibelle(String libelle);
}
