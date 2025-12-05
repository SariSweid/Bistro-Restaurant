package gui;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;

public class ServerUIController {
	
	@FXML
	private ListView<String> clientsListView;

	@FXML
	private Label statusLabel;


    public void addClient(String clientInfo) {
    	clientsListView.getItems().add(clientInfo);
        System.out.println("SERVER UI: client connected");
        
    }

    public void removeClient(String clientInfo) {
    	clientsListView.getItems().remove(clientInfo);
        System.out.println("SERVER UI: client disconnected");
    }
    
    public void setStatus(String text) {
        statusLabel.setText(text);
    }
}

