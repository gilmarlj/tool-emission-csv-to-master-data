package com.inditex.icdmsuscon.gui;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;

public class AzureConfigPanel extends GridPane {

    private final TextField keyVaultUrlInput;
    private final TextField secretNameInput;

    public AzureConfigPanel() {
        setPadding(new Insets(10, 20, 10, 10));
        setVgap(8);
        setHgap(10);

        Label titleLabel = new Label("Key Vault Configurations");
        add(titleLabel, 0, 0, 2, 1); // Span two columns for the title

        keyVaultUrlInput = new TextField();
        keyVaultUrlInput.setPromptText("Key Vault URL");

        secretNameInput = new TextField();
        secretNameInput.setPromptText("Key Name");

        HBox keyVaultUrlBox = new HBox(20);
        keyVaultUrlBox.getChildren().add(keyVaultUrlInput);
        add(new Label("Key Vault URL:"), 0, 1);
        add(keyVaultUrlBox, 1, 1);

        HBox secretNameBox = new HBox(20);
        secretNameBox.getChildren().add(secretNameInput);
        add(new Label("Secret Name:"), 0, 2);
        add(secretNameBox, 1, 2);
    }

    public void toggleAzureConfig(boolean enable) {
        keyVaultUrlInput.setDisable(!enable);
        secretNameInput.setDisable(!enable);
    }

    public String getKeyVaultUrl() {
        return keyVaultUrlInput.getText();
    }

    public String getSecretName() {
        return secretNameInput.getText();
    }
}
