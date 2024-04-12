package com.library.pages.adherants;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.library.DBConnection;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

public class DeleteAdherent {

    public static void delete(Adherent adherent) {
        // Show confirmation dialog
        Alert confirmationDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationDialog.setTitle("Delete Adherent");
        confirmationDialog.setHeaderText(null);
        confirmationDialog.setContentText("Are you sure you want to delete this adherent?");
        confirmationDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Delete the adherent from the database
                try (Connection connection = DBConnection.getConnection();
                     PreparedStatement statement = connection.prepareStatement(
                             "DELETE FROM adherent WHERE Adh_num = ?")) {
                    statement.setInt(1, adherent.getAdhNum());
                    int rowsAffected = statement.executeUpdate();
                    if (rowsAffected > 0) {
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Adherent Deleted", "The adherent has been successfully deleted.");
                        // Refresh the table
                        AdherentPage.fetchData();
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Error", "Failed to Delete Adherent", "Failed to delete the adherent. Please try again.");
                    }
                } catch (SQLException e) {
                    System.err.println("Database error:");
                    System.err.println("Error Message: " + e.getMessage());
                    System.err.println("SQL State: " + e.getSQLState());
                    System.err.println("Error Code: " + e.getErrorCode());
                    e.printStackTrace(); // Print the stack trace
                }
            }
        });
    }

    private static void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }
}
