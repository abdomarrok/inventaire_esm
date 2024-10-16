module com.marrok.inventaire_esm {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires java.sql;

    requires org.controlsfx.controls;

    requires javafx.base;
    requires java.desktop;
    requires mysql.connector.java;
    requires com.jfoenix;
    requires com.ibm.icu;
    requires com.google.zxing;
    requires com.google.zxing.javase;
    requires javafx.swing;
    requires jasperreports;
    requires barcode4j;

    requires com.fasterxml.jackson.core;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.dataformat.xml;

    requires org.apache.commons.csv;
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;
    requires java.logging;
    requires com.dlsc.gemsfx;
    requires fr.brouillard.oss.cssfx;


    opens com.marrok.inventaire_esm to javafx.fxml;
    opens com.marrok.inventaire_esm.model to javafx.base;

    exports com.marrok.inventaire_esm.util;
    exports com.marrok.inventaire_esm;
    exports com.marrok.inventaire_esm.model;
    exports com.marrok.inventaire_esm.controller.article;
    opens com.marrok.inventaire_esm.controller.article to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.service;
    opens com.marrok.inventaire_esm.controller.service to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.category;
    opens com.marrok.inventaire_esm.controller.category to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.dashboard;
    opens com.marrok.inventaire_esm.controller.dashboard to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.login;
    opens com.marrok.inventaire_esm.controller.login to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.location;
    opens com.marrok.inventaire_esm.controller.location to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.inventaire;
    opens com.marrok.inventaire_esm.controller.inventaire to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.employer;
    opens com.marrok.inventaire_esm.controller.employer to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.settings;
    opens com.marrok.inventaire_esm.controller.settings to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.users;
    opens com.marrok.inventaire_esm.controller.users to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.stock_dashboard;
    opens com.marrok.inventaire_esm.controller.stock_dashboard to javafx.fxml;
    exports com.marrok.inventaire_esm.controller.fournisseur;
    opens com.marrok.inventaire_esm.controller.fournisseur to javafx.fxml;



}
