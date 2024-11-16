package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Inventaire_Item;
import org.apache.log4j.Logger;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class InventaireItemDbHelper {
    Logger logger = Logger.getLogger(InventaireItemDbHelper.class);
    public Connection cnn;

    public InventaireItemDbHelper() throws SQLException {
        logger.info("InventaireItemDbHelper: InventaireItemDbHelper");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }

    public List<Inventaire_Item> getInventaireItems() {
        logger.info("InventaireItemDbHelper: getInventaireItems");
        List<Inventaire_Item> inventaireItems = new ArrayList<>();

        String query = "SELECT * FROM inventaire_item  ORDER BY last_edited DESC;";

        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {

            while (resultSet.next()) {
                int id_article = resultSet.getInt("id_article");
                int id_user = resultSet.getInt("user_id");
                int id_inventaire = resultSet.getInt("id");
                int id_localisation = resultSet.getInt("id_localisation");
                int employer_id = resultSet.getInt("id_employer");

                Time time = resultSet.getTime("time");
                String timeString = time != null ? time.toString() : ""; // Handle null value gracefully

                // Combine the current date with the time from the database
                LocalDate currentDate = LocalDate.now();
                LocalTime dbTime = time.toLocalTime();
                LocalDateTime dateTime = LocalDateTime.of(currentDate, dbTime);

                // Format the LocalDateTime to the desired string format
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                String formattedDateTime = dateTime.format(formatter);

                String num_inventaire = resultSet.getString("num_inventaire");
                String status=resultSet.getString("status");

                Inventaire_Item item = new Inventaire_Item(id_inventaire, id_article, id_localisation, id_user,employer_id,  num_inventaire,formattedDateTime,status);
                inventaireItems.add(item);
            }

        } catch (SQLException e) {
          logger.error(e);
        }

        return inventaireItems;
    }
    public void insertInventaireItems(List<Inventaire_Item> inventaireItems){
        logger.info("InventaireItemDbHelper: insertInventaireItems");
        String query = "INSERT INTO inventaire_item (id_article, user_id, id_localisation, id_employer, num_inventaire, time,status) VALUES (?, ?, ?, ?, ?, ?,?)";

        try (PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {
            for (Inventaire_Item item : inventaireItems) {
                preparedStatement.setInt(1, item.getArticle_id());
                preparedStatement.setInt(2, item.getUser_id());
                preparedStatement.setInt(3, item.getLocalisation_id());
                preparedStatement.setInt(4, item.getEmployer_id());
                preparedStatement.setString(5, item.getNum_inventaire());

                // Parse the formatted date time string back to a SQL Timestamp
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
                LocalDateTime dateTime = LocalDateTime.parse(item.getFormattedDateTime(), formatter);
                preparedStatement.setTimestamp(6, Timestamp.valueOf(dateTime));
                preparedStatement.setString(7, item.getStatus());

                preparedStatement.addBatch();
            }

            // Execute the batch
            preparedStatement.executeBatch();
        } catch (SQLException e) {
           logger.error(e);
        }
    }
    public  boolean deleteInventaireItem(int itemId) {
        logger.info("InventaireItemDbHelper: deleteInventaireItem");
        String query = "DELETE FROM inventaire_item WHERE id = ?";
        try (
                PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, itemId);
            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
           logger.error(e);
        }
        return false;
    }



    public  boolean updateInventaireItem(Inventaire_Item inventaireItem) {
       logger.info("from dbhelper"+inventaireItem.toString());
        String query = "UPDATE inventaire_item SET id_article = ?, id_employer = ? , id_localisation = ?, user_id = ?, time = ?, num_inventaire = ? ,status= ? WHERE id = ?";
        try (
                PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, inventaireItem.getArticle_id());
            stmt.setInt(2, inventaireItem.getEmployer_id());
            stmt.setInt(3, inventaireItem.getLocalisation_id());
            stmt.setInt(4, inventaireItem.getUser_id());
            stmt.setTimestamp(5, Timestamp.valueOf(LocalDateTime.now()));
            stmt.setString(6, inventaireItem.getNum_inventaire());
            stmt.setString(7, inventaireItem.getStatus());
            stmt.setInt(8, inventaireItem.getId());

            int rowsAffected = stmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
           logger.error(e);
            return false;
        }
    }
    public boolean addInventaireItem(Inventaire_Item item) {
        logger.info("InventaireItemDbHelper: addInventaireItem");
        String query = "INSERT INTO inventaire_item (id_localisation, user_id, id_article, time, id_employer, num_inventaire, status) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setInt(1, item.getLocalisation_id());
            preparedStatement.setInt(2, item.getUser_id());
            preparedStatement.setInt(3, item.getArticle_id());

            // Use DateTimeFormatter to parse the date in "2024-09-26" format
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate localDate = LocalDate.parse(item.getFormattedDateTime(), formatter);

            // Convert LocalDate to LocalDateTime at the start of the day (00:00:00)
            LocalDateTime localDateTime = localDate.atStartOfDay();

            // Set the parsed LocalDateTime as a Timestamp
            preparedStatement.setTimestamp(4, Timestamp.valueOf(localDateTime));
            preparedStatement.setInt(5, item.getEmployer_id());
            preparedStatement.setString(6, item.getNum_inventaire());
            preparedStatement.setString(7, item.getStatus());

            int rowsAffected = preparedStatement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            logger.error(e);
            return false;
        } catch (DateTimeParseException e) {
           logger.error(e);
            return false;
        }
    }




    public Inventaire_Item getInevntaireItemById(int id) {
        logger.info("InventaireItemDbHelper: getInevntaireItemById");
        String query = "SELECT * FROM inventaire_item WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    // Convert Timestamp to LocalDateTime
                    LocalDateTime dateTime = rs.getTimestamp("time").toLocalDateTime();

                    // Format the LocalDateTime to "dd/MM/yyyy"
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    String formattedDate = dateTime.format(formatter);

                    return new Inventaire_Item(
                            rs.getInt("id"),
                            rs.getInt("id_article"),
                            rs.getInt("id_localisation"),
                            rs.getInt("user_id"),
                            rs.getInt("id_employer"),
                            rs.getString("num_inventaire"),
                            formattedDate,  // Store the formatted date as a string
                            rs.getString("status")
                    );
                }
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return null; // Return null if inventaire_item is not found
    }




    public List<Inventaire_Item> getInventoryItemsByFilters(Integer serviceId, Integer localisationId, Integer year) throws SQLException {
        logger.info("getInventoryItemsByFilters");
        List<Inventaire_Item> inventoryItems = new ArrayList<>();

        StringBuilder query = new StringBuilder("SELECT ii.*, l.id_service " +
                "FROM inventaire_item ii " +
                "JOIN localisation l ON ii.id_localisation = l.id " +
                "WHERE YEAR(ii.time) = ?");

        // Add service and localisation filters only if they are provided (non-null)
        if (serviceId != null) {
            query.append(" AND l.id_service = ?");
        }
        if (localisationId != null) {
            query.append(" AND ii.id_localisation = ?");
        }

        try (PreparedStatement statement = this.cnn.prepareStatement(query.toString())) {
            // Set the year parameter
            statement.setInt(1, year);

            int parameterIndex = 2; // Start at 2 because year is the first parameter

            // Set service and localisation parameters if they are not null
            if (serviceId != null) {
                statement.setInt(parameterIndex++, serviceId);
            }
            if (localisationId != null) {
                statement.setInt(parameterIndex++, localisationId);
            }

            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int articleId = resultSet.getInt("id_article");
                int localisationIdDb = resultSet.getInt("id_localisation");
                int userId = resultSet.getInt("user_id");
                int employerId = resultSet.getInt("id_employer");
                String numInventaire = resultSet.getString("num_inventaire");
                String status = resultSet.getString("status");

                // Get the date as a string
                String formattedDateTime = resultSet.getTimestamp("time").toString();

                // Create an instance of Inventaire_Item using the appropriate constructor
                Inventaire_Item item = new Inventaire_Item(id, articleId, localisationIdDb, userId, employerId, numInventaire, formattedDateTime,status);

                inventoryItems.add(item);
            }
        }

        return inventoryItems;
    }

}
