package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;


public class DatabaseConnection {
    static Logger logger = Logger.getLogger("DatabaseConnection");
    //private static final String DATABASE_NAME = "invlouiza";
 private static final String DATABASE_NAME = "testinvempty2";
  //  private static final String DATABASE_NAME = "testinv";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/"+DATABASE_NAME;
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";
//    private static final String DATABASE_URL = "jdbc:mysql://192.168.0.79:3306/"+DATABASE_NAME;
//    private static final String DATABASE_USER = "esm_inv1";
//    private static final String DATABASE_PASSWORD = "123456789";

    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        logger.info("DatabaseConnection");
        try {
            this.connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            GeneralUtil.showAlertWithOutTimelimit(Alert.AlertType.ERROR,"Connection error","check your connection");
            logger.error(" SQLException Failed to create the database connection. " +  e);

        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        logger.info("DatabaseConnection getInstance");
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }



    // Backup database to a specified path
    public static void backupDatabase(String backupPath) throws SQLException, IOException {
        logger.info("backupDatabase");
        Connection connection = getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;

        try (FileWriter fileWriter = new FileWriter(backupPath)) {
            // Write SQL dump header
            fileWriter.write("-- Database backup\n");
            fileWriter.write("-- Generated on " + java.time.LocalDateTime.now() + "\n\n");

            // Recommended creation order of tables
            String[] tableOrder = {
                    "category", "fournisseur", "employeur", "user", "service",
                    "localisation", "article", "bon_entree", "bon_sortie","bon_retour",
                    "entree", "sortie","retour", "stock_adjustment", "inventaire_item"
            };

            // Loop through the tables in the specified order
            for (String tableName : tableOrder) {
                writeTableToSQL(tableName, fileWriter); // Write each table to the SQL file
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }
    private static void writeTableToSQL(String tableName, FileWriter fileWriter) throws SQLException, IOException {
        logger.info("writeTableToSQL");
        Connection connection = getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;

        try {
            // Write CREATE TABLE statement
            ResultSet rs = statement.executeQuery("SHOW CREATE TABLE " + tableName);
            if (rs.next()) {
                String createTableSQL = rs.getString(2);
                fileWriter.write("--\n-- Table structure for table `" + tableName + "`\n--\n\n");
                fileWriter.write(createTableSQL + ";\n\n");
            }

            // Write INSERT INTO statements
            resultSet = statement.executeQuery("SELECT * FROM " + tableName);
            int columnCount = resultSet.getMetaData().getColumnCount();

            fileWriter.write("--\n-- Dumping data for table `" + tableName + "`\n--\n\n");
            while (resultSet.next()) {
                StringBuilder insertSQL = new StringBuilder("INSERT INTO `" + tableName + "` VALUES(");
                for (int i = 1; i <= columnCount; i++) {
                    String value = resultSet.getString(i);
                    if (resultSet.wasNull()) {
                        insertSQL.append("NULL");
                    } else {
                        int columnType = resultSet.getMetaData().getColumnType(i);
                        if (columnType == Types.VARCHAR || columnType == Types.CHAR) {
                            value = value.replace("'", "''"); // Escape single quotes
                            insertSQL.append("'").append(value).append("'");
                        } else if (columnType == Types.TIMESTAMP || columnType == Types.DATE || columnType == Types.TIME) {
                            // Handle date/time types
                            insertSQL.append("'").append(value).append("'");
                        } else {
                            insertSQL.append(value);
                        }
                    }
                    if (i < columnCount) insertSQL.append(", ");
                }
                insertSQL.append(");\n");
                fileWriter.write(insertSQL.toString());
            }

            fileWriter.write("\n");
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

}
