package sga.dao;

import java.util.List;
import sga.metier.Acces;

public interface IAccesDAO {
    /**
     * Récupère la liste de tous les points d'accès actifs ('A').
     */
    List<Acces> listerActifs();

    /**
     * Crée un nouveau point d'accès.
     */
    boolean creer(Acces acces);

    /**
     * Modifie un point d'accès existant.
     */
    boolean modifier(Acces acces);

    /**
     * Désactive logiquement (statut = 'I') un point d'accès.
     */
    boolean desactiver(int idAcces);

    /**
     * Récupère les détails complets d'un point d'accès par son identifiant.
     */
    Acces recupererParId(int idAcces);

    /**
     * Recherche multicritère de points d'accès actifs.
     * @param libelle recherche partielle sur le libellé (ignorer si null ou vide)
     * @param idTypeAcces filtre par type (ignorer si <= 0)
     * @param idLieu filtre par lieu (ignorer si <= 0)
     */
    List<Acces> rechercherMulticritere(String libelle, int idTypeAcces, int idLieu);
}
