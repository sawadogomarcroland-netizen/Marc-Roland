package sga.metier;

import java.util.Date;

public class Badge {
    private String numBadge;
    private Date dateEmission;
    private Date dateExpiration;
    private String statut; // ACTIF, SUSPENDU, EXPIRE, REVOQUE
    private int idPersonne;
    private int idLieu;

    public Badge() {}

    public Badge(String numBadge, Date dateEmission, Date dateExpiration, String statut, int idPersonne, int idLieu) {
        this.numBadge = numBadge;
        this.dateEmission = dateEmission;
        this.dateExpiration = dateExpiration;
        this.statut = statut;
        this.idPersonne = idPersonne;
        this.idLieu = idLieu;
    }

    public String getNumBadge() {
        return numBadge;
    }

    public void setNumBadge(String numBadge) {
        this.numBadge = numBadge;
    }

    public Date getDateEmission() {
        return dateEmission;
    }

    public void setDateEmission(Date dateEmission) {
        this.dateEmission = dateEmission;
    }

    public Date getDateExpiration() {
        return dateExpiration;
    }

    public void setDateExpiration(Date dateExpiration) {
        this.dateExpiration = dateExpiration;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getIdPersonne() {
        return idPersonne;
    }

    public void setIdPersonne(int idPersonne) {
        this.idPersonne = idPersonne;
    }

    public int getIdLieu() {
        return idLieu;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    @Override
    public String toString() {
        return numBadge + " (" + statut + ")";
    }
}
