package com.marrok.inventaire_esm.util.database;

import com.marrok.inventaire_esm.model.Service;
import com.marrok.inventaire_esm.util.GeneralUtil;
import javafx.scene.control.Alert;
import org.apache.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDbHelper {
    Logger logger = Logger.getLogger(ServiceDbHelper.class);

    public Connection cnn;

    public ServiceDbHelper() throws SQLException {
        logger.info("ServiceDbHelper");
        this.cnn = DatabaseConnection.getInstance().getConnection();
    }

    public List<Service> getServices() {
        logger.info("getServices");
        List<Service> services = new ArrayList<>();
        String query = "SELECT * FROM service ORDER BY id DESC;";

        try (PreparedStatement stmt = this.cnn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Service service = new Service(rs.getInt("id"), rs.getString("name"),rs.getInt("chef_service_id"));
                services.add(service);
            }
        } catch (SQLException e) {
           logger.error(e);
        }
        return services;
    }

    public boolean addService(Service service) {
        logger.info("addService");
        String query = "INSERT INTO service (name,chef_service_id) VALUES (?,?)";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, service.getName());
            stmt.setInt(2, service.getChef_service_id());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
           logger.error(e);
        }
        return false;
    }

    public boolean updateService(Service service) {
        logger.info("updateService");
        String query = "UPDATE service SET name = ? ,chef_service_id=? WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setString(1, service.getName());
            stmt.setInt(2, service.getChef_service_id());
            stmt.setInt(3, service.getId());
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
           logger.error(e);
        }
        return false;
    }

    public boolean deleteService(long id) {
        logger.info("deleteService");
        String query = "DELETE FROM service WHERE id = ?";
        try (PreparedStatement stmt = this.cnn.prepareStatement(query)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
           logger.error(e);
            if (e instanceof SQLIntegrityConstraintViolationException) {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل في حذف المصلحة", "هذه المصلحة مرفقة بجرد لا يجدر بك حذفها");
            } else {
                GeneralUtil.showAlert(Alert.AlertType.ERROR, "فشل في حذف المصلحة", e.getMessage());
            }
        }
        return false;
    }
    public Service getServiceById(int id) {
        logger.info("getServiceById");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Service service = null;

        try {
            String query = "SELECT * FROM service WHERE id = ?";
            stmt = this.cnn.prepareStatement(query); // Using this.cnn to get the connection
            stmt.setInt(1, id);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int serviceId = rs.getInt("id");
                String serviceName = rs.getString("name");
                service = new Service(serviceId, serviceName);
            }
        } catch (SQLException e) {
           logger.error(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
             logger.error(e);
            }
        }
        return service;
    }
    public Service getServiceByName(String serviceName) {
        logger.info("getServiceByName");
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Service service = null;

        try {
            String query = "SELECT * FROM service WHERE name = ? ORDER BY name DESC;";
            stmt = this.cnn.prepareStatement(query); // Using this.cnn to get the connection
            stmt.setString(1, serviceName);
            rs = stmt.executeQuery();

            if (rs.next()) {
                int serviceId = rs.getInt("id");
                String name = rs.getString("name");
                service = new Service(serviceId, name);
            }
        } catch (SQLException e) {
          logger.error(e);
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
               logger.error(e);
            }
        }
        return service;
    }


}
