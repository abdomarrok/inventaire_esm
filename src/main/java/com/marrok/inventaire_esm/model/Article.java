package com.marrok.inventaire_esm.model;

public class Article {
    private int id;
    private String name;
    private String unite;
    private String remarque;
    private String description;
    private int idCategory;
    private int min_quantity;
    public Article() {
    }

    public Article(String name, String unite,String description, String remarque, int idCategory) {
        this.name = name;
        this.unite = unite;
        this.remarque = remarque;
        this.description = description;
        this.idCategory = idCategory; // Use camel case
    }
    public Article(int id, String name, String unit,  String remark, String descriptionText, int categoryId) {
        this.id = id;
        this.name = name;
        this.unite = unit;
        this.remarque = remark;
        this.description = descriptionText;
        this.idCategory = categoryId; // Use camel case
    }

    public Article(int id, String name, String unite, String remarque, String description, int idCategory, int min_quantity) {
        this.id = id;
        this.name = name;
        this.unite = unite;
        this.remarque = remarque;
        this.description = description;
        this.idCategory = idCategory;
        this.min_quantity = min_quantity;
    }



    // Getters and setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }


    public String getRemarque() {
        return remarque;
    }

    public void setRemarque(String remarque) {
        this.remarque = remarque;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIdCategory() {
        return idCategory; // Use camel case
    }

    public void setIdCategory(int idCategory) {
        this.idCategory = idCategory; // Use camel case
    }

    public int getMin_quantity() {
        return min_quantity;
    }

    public void setMin_quantity(int min_quantity) {
        this.min_quantity = min_quantity;
    }

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", unite='" + unite + '\'' +
                ", min_quantity=" + min_quantity + '\''  +
                ", remarque='" + remarque + '\'' +
                ", description='" + description + '\'' +
                ", idCategory=" + idCategory +
                '}';
    }
}
