package com.marrok.inventaire_esm;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
public class App {
    private static final Logger logger = LogManager.getLogger(App.class);
    public static void main(String[] args) {
        BasicConfigurator.configure();
        logger.info("App lunched");
        Main.main(args);
    }
}
