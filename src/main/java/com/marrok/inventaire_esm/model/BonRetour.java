package com.marrok.inventaire_esm.model;

import java.util.Date;

public class BonRetour {
    private int id;
    private int idEmployeur;
    private int idService;
    private Date date;
    private String returnReason; // Reason for the return
    private Date lastEdited;

    // Constructors
    public BonRetour() {
    }

    public BonRetour(int id, int idEmployeur, int idService, Date date, String returnReason, Date lastEdited) {
        this.id = id;
        this.idEmployeur = idEmployeur;
        this.idService = idService;
        this.date = date;
        this.returnReason = returnReason;
        this.lastEdited = lastEdited;
    }

    public BonRetour(int id, int idEmployeur, int idService, Date date, String returnReason) {
        this.id = id;
        this.idEmployeur = idEmployeur;
        this.idService = idService;
        this.date = date;
        this.returnReason = returnReason;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdEmployeur() {
        return idEmployeur;
    }

    public void setIdEmployeur(int idEmployeur) {
        this.idEmployeur = idEmployeur;
    }

    public int getIdService() {
        return idService;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getReturnReason() {
        return returnReason;
    }

    public void setReturnReason(String returnReason) {
        this.returnReason = returnReason;
    }

    public Date getLastEdited() {
        return lastEdited;
    }

    public void setLastEdited(Date lastEdited) {
        this.lastEdited = lastEdited;
    }
}
