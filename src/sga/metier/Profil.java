package sga.metier;

public class Profil {
    private int idProfil;
    private String libelleProfil;
    private String description;

    public Profil() {}

    public Profil(int idProfil, String libelleProfil, String description) {
        this.idProfil = idProfil;
        this.libelleProfil = libelleProfil;
        this.description = description;
    }

    public int getIdProfil() {
        return idProfil;
    }

    public void setIdProfil(int idProfil) {
        this.idProfil = idProfil;
    }

    public String getLibelleProfil() {
        return libelleProfil;
    }

    public void setLibelleProfil(String libelleProfil) {
        this.libelleProfil = libelleProfil;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return libelleProfil;
    }
}
