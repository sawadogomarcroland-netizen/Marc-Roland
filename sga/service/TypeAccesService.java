package sga.service;

import java.util.List;
import sga.dao.ITypeAccesDAO;
import sga.dao.TypeAccesDAOImpl;
import sga.metier.TypeAcces;
import sga.util.Validateur;

public class TypeAccesService {
    private final ITypeAccesDAO typeAccesDAO;

    public TypeAccesService() {
        this.typeAccesDAO = new TypeAccesDAOImpl();
    }

    // Utilisé pour l'injection de dépendances (ex: tests)
    public TypeAccesService(ITypeAccesDAO dao) {
        this.typeAccesDAO = dao;
    }

    public List<TypeAcces> listerActifs() {
        return typeAccesDAO.listerActifs();
    }

    public boolean creer(TypeAcces type) throws Exception {
        if (type == null) {
            throw new Exception("Le type d'accès ne peut pas être nul.");
        }
        if (Validateur.estVide(type.getLibelle())) {
            throw new Exception("Le libellé du type d'accès est obligatoire.");
        }
        if (Validateur.depasseLongueur(type.getLibelle(), 100)) {
            throw new Exception("Le libellé ne doit pas dépasser 100 caractères.");
        }
        if (Validateur.depasseLongueur(type.getDescription(), 300)) {
            throw new Exception("La description ne doit pas dépasser 300 caractères.");
        }

        // CF-03 : Unicité du libellé
        if (typeAccesDAO.verifierDoublon(type.getLibelle())) {
            throw new Exception("Un type d'accès actif avec le libellé '" + type.getLibelle().toUpperCase() + "' existe déjà.");
        }

        return typeAccesDAO.creer(type);
    }

    public boolean modifier(TypeAcces type) throws Exception {
        if (type == null) {
            throw new Exception("Le type d'accès ne peut pas être nul.");
        }
        if (Validateur.estVide(type.getLibelle())) {
            throw new Exception("Le libellé du type d'accès est obligatoire.");
        }
        if (Validateur.depasseLongueur(type.getLibelle(), 100)) {
            throw new Exception("Le libellé ne doit pas dépasser 100 caractères.");
        }
        if (Validateur.depasseLongueur(type.getDescription(), 300)) {
            throw new Exception("La description ne doit pas dépasser 300 caractères.");
        }

        // Vérifier l'unicité du libellé sur d'autres types d'accès
        List<TypeAcces> actifs = typeAccesDAO.listerActifs();
        for (TypeAcces t : actifs) {
            if (t.getIdTypeAcces() != type.getIdTypeAcces() && t.getLibelle().equalsIgnoreCase(type.getLibelle())) {
                throw new Exception("Un autre type d'accès actif avec le libellé '" + type.getLibelle().toUpperCase() + "' existe déjà.");
            }
        }

        return typeAccesDAO.modifier(type);
    }

    public boolean desactiver(int idTypeAcces) throws Exception {
        // CF-04 : Type d'accès utilisé par accès actifs -> désactivation interdite
        if (typeAccesDAO.verifierUsage(idTypeAcces)) {
            throw new Exception("Désactivation impossible : ce type d'accès est actuellement associé à des points d'accès actifs.");
        }
        return typeAccesDAO.desactiver(idTypeAcces);
    }

    public List<TypeAcces> rechercher(String libelle) {
        if (Validateur.estVide(libelle)) {
            return typeAccesDAO.listerActifs();
        }
        return typeAccesDAO.rechercherParLibelle(libelle);
    }
}
