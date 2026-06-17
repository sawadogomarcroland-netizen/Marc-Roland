package sga.service;

import java.util.List;
import sga.dao.IAccesDAO;
import sga.dao.AccesDAOImpl;
import sga.metier.Acces;
import sga.util.Validateur;

public class AccesService {
    private final IAccesDAO accesDAO;

    public AccesService() {
        this.accesDAO = new AccesDAOImpl();
    }

    public AccesService(IAccesDAO dao) {
        this.accesDAO = dao;
    }

    public List<Acces> listerActifs() {
        return accesDAO.listerActifs();
    }

    public boolean creer(Acces acces) throws Exception {
        validerAcces(acces);
        return accesDAO.creer(acces);
    }

    public boolean modifier(Acces acces) throws Exception {
        if (acces == null) {
            throw new Exception("Le point d'accès ne peut pas être nul.");
        }
        if (acces.getIdAcces() <= 0) {
            throw new Exception("L'identifiant du point d'accès est invalide pour une modification.");
        }
        validerAcces(acces);
        return accesDAO.modifier(acces);
    }

    public boolean desactiver(int idAcces) throws Exception {
        if (idAcces <= 0) {
            throw new Exception("Identifiant de point d'accès invalide.");
        }
        return accesDAO.desactiver(idAcces);
    }

    public Acces recupererParId(int idAcces) {
        if (idAcces <= 0) {
            return null;
        }
        return accesDAO.recupererParId(idAcces);
    }

    public List<Acces> rechercherMulticritere(String libelle, int idTypeAcces, int idLieu) {
        return accesDAO.rechercherMulticritere(libelle, idTypeAcces, idLieu);
    }

    private void validerAcces(Acces acces) throws Exception {
        if (acces == null) {
            throw new Exception("Le point d'accès ne peut pas être nul.");
        }
        if (Validateur.estVide(acces.getLibelle())) {
            throw new Exception("Le libellé du point d'accès est obligatoire.");
        }
        if (Validateur.depasseLongueur(acces.getLibelle(), 150)) {
            throw new Exception("Le libellé ne doit pas dépasser 150 caractères.");
        }
        if (Validateur.depasseLongueur(acces.getDescription(), 300)) {
            throw new Exception("La description ne doit pas dépasser 300 caractères.");
        }
        if (acces.getIdTypeAcces() <= 0) {
            throw new Exception("Le type d'accès est obligatoire.");
        }
        // CF-01 : Un accès n'appartient qu'à un seul lieu (id_lieu NOT NULL)
        if (acces.getIdLieu() <= 0) {
            throw new Exception("Le lieu du point d'accès est obligatoire.");
        }
    }
}
