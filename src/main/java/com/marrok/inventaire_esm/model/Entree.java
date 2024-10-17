package com.marrok.inventaire_esm.model;

public class Entree {
    private int id;
    private int idArticle; // Foreign key referencing article
    private int quantity;
    private double unitPrice; // Unit price of the article
    private int idBe; // Foreign key referencing bon_entree

    // Constructor
    public Entree(int id, int idArticle, int quantity, double unitPrice, int idBe) {
        this.id = id;
        this.idArticle = idArticle;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.idBe = idBe;
    }

    // Default constructor
    public Entree() {
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(int idArticle) {
        this.idArticle = idArticle;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getIdBe() {
        return idBe;
    }

    public void setIdBe(int idBe) {
        this.idBe = idBe;
    }

    // Calculate total price (HT)
    public double getTotalprix_ht() {
        return this.unitPrice * this.quantity;
    }

    @Override
    public String toString() {
        return "Entree{" +
                "id=" + id +
                ", idArticle=" + idArticle +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                ", idBe=" + idBe +
                ", totalprix_ht=" + getTotalprix_ht() + // include in toString if needed
                '}';
    }
}
