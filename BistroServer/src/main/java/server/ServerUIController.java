package server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

/**
 * Controller class for the Server's Graphical User Interface.
 * This class manages the visual representation of connected clients and 
 * the current operational status of the server using JavaFX components.
 */
public class ServerUIController {
	
    /** * ListView component that displays the connection strings of all 
     * currently active clients. 
     */
	@FXML
	private ListView<String> clientsListView;

    /** * Label component used to display the server's current status 
     * (e.g., "Server Started", "Listening on Port 5555"). 
     */
	@FXML
	private Label statusLabel;

    /**
     * Adds a client's information string to the UI list.
     * This method uses Platform.runLater to ensure that the UI update 
     * occurs safely on the JavaFX Application Thread.
     *
     * @param clientInfo A descriptive string representing the connected client.
     */
	public void addClient(String clientInfo) {
        // Ensure UI update happens on the correct thread
        Platform.runLater(() -> {
            if (!clientsListView.getItems().contains(clientInfo)) {
                clientsListView.getItems().add(clientInfo);
                System.out.println("SERVER UI: client added to list");
            }
        });
    }

    /**
     * Removes a client's information string from the UI list upon disconnection.
     * This method uses Platform.runLater to ensure the list modification 
     * is thread-safe for the UI.
     *
     * @param clientInfo The descriptive string of the client to be removed.
     */
    public void removeClient(String clientInfo) {
        // Ensure UI update happens on the correct thread
        Platform.runLater(() -> {
            clientsListView.getItems().remove(clientInfo);
            System.out.println("SERVER UI: client removed from list");
        });
    }
    
    /**
     * Updates the status label with a new message.
     *
     * @param text The status message to be displayed in the UI.
     */
    public void setStatus(String text) {
        statusLabel.setText(text);
    }
}