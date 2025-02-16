package org.aieesti;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

public class MainController {
    @FXML
    private Label fileLabel;

    @FXML
    private Label statusLabel;

    private File selectedFile;

    public void handleFileSelection() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Appraisal File");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Word Documents", "*.docx"));
        selectedFile = fileChooser.showOpenDialog(new Stage());

        if (selectedFile != null) {
            fileLabel.setText("Selected: " + selectedFile.getName());
        }
    }

    public void processFile() {
        if (selectedFile == null) {
            statusLabel.setText("Status: No file selected!");
            return;
        }

        statusLabel.setText("Processing file...");

        boolean success = AppraisalProcessor.updateFile(selectedFile);

        if (success) {
            statusLabel.setText("Status: File updated successfully!");
        } else {
            statusLabel.setText("Status: Update failed.");
        }
    }
}
