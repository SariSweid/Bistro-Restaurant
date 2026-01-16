package server;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

public class ServerUIController {
	
	@FXML
	private ListView<String> clientsListView;

	@FXML
	private Label statusLabel;


	public void addClient(String clientInfo) {
        // Ensure UI update happens on the correct thread
        Platform.runLater(() -> {
            if (!clientsListView.getItems().contains(clientInfo)) {
                clientsListView.getItems().add(clientInfo);
                System.out.println("SERVER UI: client added to list");
            }
        });
    }

    public void removeClient(String clientInfo) {
        // Ensure UI update happens on the correct thread
        Platform.runLater(() -> {
            clientsListView.getItems().remove(clientInfo);
            System.out.println("SERVER UI: client removed from list");
        });
    }
    
    public void setStatus(String text) {
        statusLabel.setText(text);
    }
}

