package sga.metier;

import java.util.Date;

public class Acces {
    private int idAcces;
    private String libelle;
    private String description;
    private int idTypeAcces;
    private String typeLibelle; // Libellé associé au type d'accès (jointure)
    private int idLieu;
    private String lieuLibelle; // Libellé associé au lieu (jointure)
    private String statut;      // "A" = Actif, "I" = Inactif
    private Date dateCreation;
    private Date dateModification;

    public Acces() {}

    public Acces(int idAcces, String libelle, String description, int idTypeAcces, String typeLibelle, 
                 int idLieu, String lieuLibelle, String statut, Date dateCreation, Date dateModification) {
        this.idAcces = idAcces;
        this.libelle = libelle;
        this.description = description;
        this.idTypeAcces = idTypeAcces;
        this.typeLibelle = typeLibelle;
        this.idLieu = idLieu;
        this.lieuLibelle = lieuLibelle;
        this.statut = statut;
        this.dateCreation = dateCreation;
        this.dateModification = dateModification;
    }

    public int getIdAcces() {
        return idAcces;
    }

    public void setIdAcces(int idAcces) {
        this.idAcces = idAcces;
    }

    public String getLibelle() {
        return libelle;
    }

    public void setLibelle(String libelle) {
        this.libelle = libelle;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdTypeAcces() {
        return idTypeAcces;
    }

    public void setIdTypeAcces(int idTypeAcces) {
        this.idTypeAcces = idTypeAcces;
    }

    public String getTypeLibelle() {
        return typeLibelle;
    }

    public void setTypeLibelle(String typeLibelle) {
        this.typeLibelle = typeLibelle;
    }

    public int getIdLieu() {
        return idLieu;
    }

    public void setIdLieu(int idLieu) {
        this.idLieu = idLieu;
    }

    public String getLieuLibelle() {
        return lieuLibelle;
    }

    public void setLieuLibelle(String lieuLibelle) {
        this.lieuLibelle = lieuLibelle;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Date getDateCreation() {
        return dateCreation;
    }

    public void setDateCreation(Date dateCreation) {
        this.dateCreation = dateCreation;
    }

    public Date getDateModification() {
        return dateModification;
    }

    public void setDateModification(Date dateModification) {
        this.dateModification = dateModification;
    }
}
