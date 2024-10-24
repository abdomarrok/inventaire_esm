package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Localisation;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LocDbhelper {
    public Connection cnn;

    public LocDbhelper() throws SQLException {
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }

    public List<Localisation> getLocalisations() {
        List<Localisation> localisations = new ArrayList<>();
        String query = "SELECT * FROM localisation ORDER BY id DESC;";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Localisation localisation = new Localisation(
                        rs.getInt("id"),
                        rs.getString("loc_name"),
                        rs.getInt("floor"),
                        rs.getInt("id_service")
                );
                localisations.add(localisation);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return localisations;
    }

    public boolean addLocalisation(Localisation localisation) {
        String query = "INSERT INTO localisation ( `loc_name`, `floor`, `id_service`) VALUES ( ?,?,?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, localisation.getLocName());
            stmt.setInt(2, localisation.getFloor());
            stmt.setInt(3, localisation.getIdService());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean updateLocalisation(Localisation localisation) {
        String query = "UPDATE localisation SET `loc_name` = ?, `floor` = ?, `id_service` = ? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, localisation.getLocName());
            stmt.setInt(2, localisation.getFloor());
            stmt.setInt(3, localisation.getIdService());
            stmt.setInt(4, localisation.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return false;
    }

    public boolean deleteLocalisation(long id) {
        String query = "DELETE FROM localisation WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }  catch (SQLIntegrityConstraintViolationException e) {

            GeneralUtil.showAlert(Alert.AlertType.ERROR, "خطأ", "لا تستطيع حذف هذا المكان لانه مازال مرفقا بجرد");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }

    public List<Localisation> getLocalisationsByServiceId(int serviceId) {
        List<Localisation> localisations = new ArrayList<>();
        String query = "SELECT * FROM Localisation WHERE id_service = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, serviceId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Localisation localisation = new Localisation(
                            rs.getInt("id"),
                            rs.getString("loc_name"),
                            rs.getInt("floor"),
                            rs.getInt("id_service")
                    );
                    localisations.add(localisation);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return localisations;
    }

    public Localisation getLocalisationByName(String name) {
        String query = "SELECT * FROM Localisation WHERE loc_name = ? ORDER BY loc_name DESC;";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, name);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Localisation(
                            rs.getInt("id"),
                            rs.getString("loc_name"),
                            rs.getInt("floor"),
                            rs.getInt("id_service")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return null; // Return null if localisation is not found
    }

    public Localisation getLocalisationById(int id) {
        String query = "SELECT * FROM localisation WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Localisation(
                            rs.getInt("id"),
                            rs.getString("loc_name"),
                            rs.getInt("floor"),
                            rs.getInt("id_service")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            // Handle exception
        }
        return null; // Return null if localisation is not found
    }

    // Method to fetch all location names
    public  List<String> getAllLocations() {
        List<String> locations = new ArrayList<>();
        String query = "SELECT loc_name FROM localisation";
        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query);
                ResultSet rs = preparedStatement.executeQuery()) {

            while (rs.next()) {
                locations.add(rs.getString("loc_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return locations;
    }
    // Method to fetch location ID by name
    public  int getLocationIdByName(String name) {
        String query = "SELECT id FROM localisation WHERE loc_name = ?";
        try (
                PreparedStatement preparedStatement = this.cnn.prepareStatement(query)) {

            preparedStatement.setString(1, name);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Return -1 if not found
    }
}
