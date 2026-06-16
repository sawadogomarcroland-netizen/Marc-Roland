package sga.util;

public class Validateur {
    /**
     * Vérifie si une chaîne est vide ou composée uniquement d'espaces.
     * @param str la chaîne à vérifier
     * @return true si vide, false sinon
     */
    public static boolean estVide(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Vérifie si la longueur d'une chaîne dépasse une limite donnée.
     * @param str la chaîne à vérifier
     * @param max la longueur maximale autorisée
     * @return true si elle dépasse, false sinon
     */
    public static boolean depasseLongueur(String str, int max) {
        if (str == null) {
            return false;
        }
        return str.length() > max;
    }
}
