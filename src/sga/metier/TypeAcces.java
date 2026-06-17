package sga.metier;

import java.util.Date;

public class TypeAcces {
    private int idTypeAcces;
    private String libelle;
    private String description;
    private String statut; // "A" = Actif, "I" = Inactif
    private Date dateCreation;

    public TypeAcces() {}

    public TypeAcces(int idTypeAcces, String libelle, String description, String statut, Date dateCreation) {
        this.idTypeAcces = idTypeAcces;
        this.libelle = libelle;
        this.description = description;
        this.statut = statut;
        this.dateCreation = dateCreation;
    }

    public int getIdTypeAcces() {
        return idTypeAcces;
    }

    public void setIdTypeAcces(int idTypeAcces) {
        this.idTypeAcces = idTypeAcces;
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

    @Override
    public String toString() {
        return libelle; // Nécessaire pour les JComboBox
    }
}
