package com.marrok.inventaire_esm.model;

public class Inventaire_Item {
    private int id;
    private int article_id;
    private int  localisation_id;
    private int user_id;
    private int employer_id;
    private String num_inventaire;
    private String formattedDateTime;

    public Inventaire_Item(int id, int article_id, int localisation_id, int user_id, int employer_id, String num_inventaire) {
        this.id = id;
        this.article_id = article_id;
        this.localisation_id = localisation_id;
        this.user_id = user_id;
        this.employer_id = employer_id;
        this.num_inventaire = num_inventaire;
    }


    public String getFormattedDateTime() {
        return formattedDateTime;
    }

    public void setFormattedDateTime(String formattedDateTime) {
        this.formattedDateTime = formattedDateTime;
    }

    public Inventaire_Item(int i, int articleId, int localisationId, int userId, int employerId) {
        this.id = i;
        this.article_id = articleId;
        this.localisation_id = localisationId;
        this.user_id = userId;
        this.employer_id = employerId;
    }

    public Inventaire_Item(int id, int article_id, int localisation_id, int user_id, int employer_id, String num_inventaire, String formattedDateTime) {
        this.id = id;
        this.article_id = article_id;
        this.localisation_id = localisation_id;
        this.user_id = user_id;
        this.employer_id = employer_id;
        this.num_inventaire = num_inventaire;
        this.formattedDateTime = formattedDateTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getArticle_id() {
        return article_id;
    }

    public void setArticle_id(int article_id) {
        this.article_id = article_id;
    }

    public int getLocalisation_id() {
        return localisation_id;
    }

    public void setLocalisation_id(int localisation_id) {
        this.localisation_id = localisation_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getNum_inventaire() {
        return num_inventaire;
    }

    public void setNum_inventaire(String num_inventaire) {
        this.num_inventaire = num_inventaire;
    }


    public int getEmployer_id() {
        return employer_id;
    }

    public void setEmployer_id(int employer_id) {
        this.employer_id = employer_id;
    }

    @Override
    public String toString() {
        return "Inventaire_Item{" +
                "id=" + id +
                ", article_id=" + article_id +
                ", localisation_id=" + localisation_id +
                ", user_id=" + user_id +
                ", employer_id=" + employer_id +
                ", num_inventaire='" + num_inventaire + '\'' +
                '}';
    }
}
