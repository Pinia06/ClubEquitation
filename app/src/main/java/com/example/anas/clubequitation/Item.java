package com.example.anas.clubequitation;

public class Item {
    private String nomJob;
    private String dateDebutJob;
    private String dateFinJob;
    private String heureDebutJob;
    private String heureFinJob;
    private String nomPersonne;

    public Item(String nomJob,String dateDebutJob, String dateFinJob, String heureDebutJob, String heureFinJob, String nomPersonne) {
        this.nomJob = nomJob;
        this.dateDebutJob = dateDebutJob;
        this.dateFinJob = dateFinJob;
        this.heureDebutJob = heureDebutJob;
        this.heureFinJob = heureFinJob;
        this.nomPersonne = nomPersonne;
    }

    public String getNomJob() {
        return nomJob;
    }

    public String getDateDebutJob() {
        return dateDebutJob;
    }

    public String getDateFinJob() {
        return dateFinJob;
    }

    public String getHeureDebutJob() {
        return heureDebutJob;
    }

    public String getHeureFinJob() {
        return heureFinJob;
    }

    public String getNomPersonne() {
        return nomPersonne;
    }

}
