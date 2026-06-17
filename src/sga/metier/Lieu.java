package sga.metier;

public class Lieu {
    private int idLieu;
    private String libelleLieu;
    private String emplacement;
    private int capaciteMax;
    private String statut; // OUVERT, FERME, BLOQUE

    public Lieu() {}

    public Lieu(int idLieu, String libelleLieu, String emplacement, int capaciteMax, String statut) {
        this.idLieu = idLieu;
        this.libelleLieu = libelleLieu;
        this.emplacement = emplacement;
        this.capaciteMax = capaciteMax;
        this.statut = statut;
    }

    public int getIdLieu() {
        return idLieu;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    public String getLibelleLieu() {
        return libelleLieu;
    }

    public void setLibelleLieu(String libelleLieu) {
        this.libelleLieu = libelleLieu;
    }

    public String getEmplacement() {
        return emplacement;
    }

    public void setEmplacement(String emplacement) {
        this.emplacement = emplacement;
    }

    public int getCapaciteMax() {
        return capaciteMax;
    }

    public void setCapaciteMax(int capaciteMax) {
        this.capaciteMax = capaciteMax;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    @Override
    public String toString() {
        return libelleLieu; // Retourne le libellé pour l'affichage dans les listes déroulantes
    }
}
