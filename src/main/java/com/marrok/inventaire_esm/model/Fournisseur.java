package com.marrok.inventaire_esm.model;

public class Fournisseur {
    private int id;
    private String name;
    private String rc; // Registre de Commerce
    private String nif; // Numéro d'Identification Fiscale
    private String ai; // Activité Indépendante
    private String nis; // Numéro d'Identification Sociale
    private String tel; // Telephone
    private String fax; // Fax
    private String address;
    private long email; // Assuming email is stored as a long (might need to change if it's an actual email string)

    // Constructor that matches the database table fields
    public Fournisseur(int id, String name, String rc, String nif, String ai, String nis, String tel, String fax, String address, long email) {
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

    // Default constructor
    public Fournisseur() {
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

    public long getEmail() {
        return email;
    }

    public void setEmail(long email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Fournisseur{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", rc='" + rc + '\'' +
                ", nif='" + nif + '\'' +
                ", ai='" + ai + '\'' +
                ", nis='" + nis + '\'' +
                ", tel='" + tel + '\'' +
                ", fax='" + fax + '\'' +
                ", address='" + address + '\'' +
                ", email=" + email +
                '}';
    }
}