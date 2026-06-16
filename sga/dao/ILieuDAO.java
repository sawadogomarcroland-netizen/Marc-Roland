package sga.dao;

import java.util.List;
import sga.metier.Lieu;

public interface ILieuDAO {
    /**
     * Récupère la liste de tous les lieux actifs ('OUVERT').
     * @return la liste des lieux actifs
     */
    List<Lieu> listerLieuxActifs();
}
