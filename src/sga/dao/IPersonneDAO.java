package sga.dao;

import sga.metier.Personne;

public interface IPersonneDAO {
    /**
     * Authentifie une personne avec son login et son mot de passe haché.
     * @param login le login de la personne
     * @param motDePasseHash le hash SHA-256 du mot de passe
     * @return l'objet Personne si authentification réussie, null sinon
     */
    Personne authentifier(String login, String motDePasseHash);
}
