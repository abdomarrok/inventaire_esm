package com.marrok.inventaire_esm.model;

public class Fournisseur {
    private int id;
    private String name;
    private String rc; // Registre de Commerce
    private String nif; // Numéro d'Identification Fiscale
    private String ai; // Activité Indépendante
    private String nis; // Numéro d'Identification Sociale
    private String tel; // Telephone
    private String fax;// Fax
    private String rib;
    private String address;
    private String email;


    // Constructor that matches the database table fields
    public Fournisseur(int id, String name, String rc, String nif, String ai, String nis, String tel, String fax, String address, String email) {
        this.id = id;
        this.name = name;
        this.rc = rc;
        this.nif = nif;
        this.ai = ai;
        this.nis = nis;
        this.tel = tel;
        this.fax = fax;
        this.address = address;
        this.email = email;
    }
    // Constructor that matches the database table fields
    public Fournisseur(int id, String name, String rc, String nif, String ai, String nis, String tel, String fax, String address, String email, String rib) {
        this.id = id;
        this.name = name;
        this.rc = rc;
        this.nif = nif;
        this.ai = ai;
        this.nis = nis;
        this.tel = tel;
        this.fax = fax;
        this.address = address;
        this.email = email;
        this.rib = rib;
    }

    // Default constructor
    public Fournisseur() {
    }

    public Fournisseur(String name, String rc, String nif, String ai, String nis, String tel, String fax, String address, String email) {
        this.name = name;
        this.rc = rc;
        this.nif = nif;
        this.ai = ai;
        this.nis = nis;
        this.tel = tel;
        this.fax = fax;
        this.address = address;
        this.email = email;
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

    public String getRc() {
        return rc;
    }

    public void setRc(String rc) {
        this.rc = rc;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public String getAi() {
        return ai;
    }

    public void setAi(String ai) {
        this.ai = ai;
    }

    public String getNis() {
        return nis;
    }

    public void setNis(String nis) {
        this.nis = nis;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRib() {
        return rib;
    }

    public void setRib(String rib) {
        this.rib = rib;
    }

    @Override
    public String toString() {
        return
                ", الاسم='" + name + '\''+
                "الرقم التعريفي=" + id
               ;
    }

}
