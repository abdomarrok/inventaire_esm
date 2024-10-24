package com.marrok.inventaire_esm.util.database;

import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;

public class DatabaseConnection {
    private static final String DATABASE_NAME = "testinv";
    private static final String DATABASE_URL = "jdbc:mysql://localhost:3306/"+DATABASE_NAME;
    private static final String DATABASE_USER = "root";
    private static final String DATABASE_PASSWORD = "";
//    private static final String DATABASE_URL = "jdbc:mysql://192.168.0.79:3306/"+DATABASE_NAME;
//    private static final String DATABASE_USER = "esm_inv1";
//    private static final String DATABASE_PASSWORD = "123456789";
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() throws SQLException {
        try {
            this.connection = DriverManager.getConnection(DATABASE_URL, DATABASE_USER, DATABASE_PASSWORD);
        } catch (SQLException e) {
            throw new SQLException("Failed to create the database connection.", e);
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
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



    public static void backupDatabase(String backupPath) throws SQLException, IOException {
        Connection connection = getInstance().getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = null;

        try (FileWriter fileWriter = new FileWriter(backupPath)) {
            // Write SQL dump header
            fileWriter.write("-- Database backup\n");
            fileWriter.write("-- Generated on " + java.time.LocalDateTime.now() + "\n\n");

            // Get a list of all tables in the database
            resultSet = statement.executeQuery("SHOW TABLES");
            while (resultSet.next()) {
                String tableName = resultSet.getString(1);
                writeTableToSQL(tableName, fileWriter);
            }
        } finally {
            if (resultSet != null) resultSet.close();
            if (statement != null) statement.close();
        }
    }

    private static void writeTableToSQL(String tableName, FileWriter fileWriter) throws SQLException, IOException {
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
                    if (resultSet.getMetaData().getColumnType(i) == Types.VARCHAR ||
                            resultSet.getMetaData().getColumnType(i) == Types.VARCHAR ||
                            resultSet.getMetaData().getColumnType(i) == Types.CHAR) {
                        value = value.replace("'", "''"); // Escape single quotes
                        insertSQL.append("'").append(value).append("'");
                    } else {
                        insertSQL.append(value);
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
