package com.inditex.icdmsuscon.gui;

import com.inditex.icdmsuscon.Constants;
import com.inditex.icdmsuscon.az.AzureGetKeysService;
import com.inditex.icdmsuscon.emissions.EmissionEncryptService;
import com.inditex.icdmsuscon.emissions.security.CryptoConverter;
import com.inditex.icdmsuscon.emissions.security.SecurityProperties;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
public class KeysPanel extends GridPane {

    private PasswordField publicKeyInput;
    private PasswordField privateKeyInput;
    private TextField csvPathInput;
    private ToggleButton testKeysToggleButton;
    private ToggleButton azureKeyToggleButton;
    private ProgressIndicator progressIndicator;
    private AzureConfigPanel azureConfigPanel;

    private final ExecutorService executorService;

    public KeysPanel(ExecutorService executorService) {
        this.executorService = executorService;
        setPadding(new Insets(10, 10, 10, 10));
        setVgap(8);
        setHgap(10);

        GridPane subGrid = createSubGridPane();
        add(subGrid, 0, 0, 2, 1);

        azureConfigPanel = new AzureConfigPanel();
        azureConfigPanel.setVisible(false);
        add(azureConfigPanel, 2, 0, 1, 3);

        HBox actionBox = createActionsBox();
        add(actionBox, 0, 3, 3, 1);
        
        
        
    }

    private GridPane createSubGridPane() {
        GridPane subGrid = new GridPane();
        subGrid.setPadding(new Insets(10, 10, 10, 10));
        subGrid.setVgap(8);
        subGrid.setHgap(10);

        publicKeyInput = new PasswordField();
        privateKeyInput = new PasswordField();
        csvPathInput = new TextField();
        csvPathInput.setEditable(false);

        subGrid.add(new Label("Public Key:"), 0, 0);
        subGrid.add(publicKeyInput, 1, 0);

        subGrid.add(new Label("Private Key:"), 0, 1);
        HBox privateKeyBox = new HBox(10);
        privateKeyBox.getChildren().addAll(privateKeyInput);

        azureKeyToggleButton = new ToggleButton("Get private key from Key Vault");
        azureKeyToggleButton.setOnAction(e -> toggleKeyUsage(azureKeyToggleButton.isSelected()));
        privateKeyBox.getChildren().add(azureKeyToggleButton);

        subGrid.add(privateKeyBox, 1, 1);

        subGrid.add(new Label("CSV File:"), 0, 2);
        HBox csvPathBox = new HBox(10);
        csvPathBox.getChildren().addAll(csvPathInput, createBrowseButton());
        subGrid.add(csvPathBox, 1, 2);

        return subGrid;
    }

    private Button createBrowseButton() {
        Button browseButton = new Button("Open File");
        browseButton.setOnAction(e -> openFileChooser());
        return browseButton;
    }

    private HBox createActionsBox() {
        HBox actionBox = new HBox(10);
        actionBox.setAlignment(Pos.CENTER_LEFT);

        testKeysToggleButton = new ToggleButton("Use test keys");
        testKeysToggleButton.setOnAction(e -> toggleTestKeys());
        actionBox.getChildren().add(testKeysToggleButton);

        Button executeButton = new Button("Generate");
        executeButton.setOnAction(e -> {
            progressIndicator.setVisible(true);
            Task<Void> task = new Task<>() {
                @Override
                protected Void call() {
                    executeAction();
                    return null;
                }

                @Override
                protected void succeeded() {
                    progressIndicator.setVisible(false);
                }

                @Override
                protected void failed() {
                    progressIndicator.setVisible(false);
                }
            };
            executorService.submit(task);
        });

        actionBox.getChildren().add(executeButton);

        progressIndicator = new ProgressIndicator();
        progressIndicator.setVisible(false);
        actionBox.getChildren().add(progressIndicator);

        return actionBox;
    }

    private void openFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            csvPathInput.setText(selectedFile.getAbsolutePath());
            log.info("Selected file: {}", selectedFile.getAbsolutePath());
        }
    }

    private void executeAction() {
        String publicKey = publicKeyInput.getText();
        String privateKey = privateKeyInput.getText();
        String csvFilePath = csvPathInput.getText();
        try {
            List<String> insertStatements = generateInsertStrings(privateKey, publicKey, csvFilePath);
            if (!insertStatements.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Generated SQL -----------------------------\n");
                for (String statement : insertStatements) {
                    sb.append(statement).append("\n");
                }
                sb.append("----------------------------------------------------------------\n");
                log.info(sb.toString());
            }
        } catch (Exception e) {
            log.error("Error: ", e);
            throw new RuntimeException(e);
        }
    }

    private List<String> generateInsertStrings(String privateKey, String publicKey, String csvFilePath) throws Exception {
        SecurityProperties securityProperties;
        if (azureKeyToggleButton.isSelected()) {
            AzureGetKeysService azureService = new AzureGetKeysService(azureConfigPanel.getKeyVaultUrl());
            securityProperties = azureService.fetchKey(azureConfigPanel.getSecretName(), publicKey);
        } else {
            securityProperties = new SecurityProperties(List.of(privateKey), publicKey);
        }

        EmissionEncryptService service = new EmissionEncryptService(new CryptoConverter(securityProperties));
        return service.generateInsertStatements(csvFilePath);
    }

    void toggleKeyUsage(boolean azureSelected) {
        privateKeyInput.setEditable(!azureSelected);
        privateKeyInput.setDisable(azureSelected);
        azureConfigPanel.setVisible(azureSelected);
        testKeysToggleButton.setSelected(false);
        testKeysToggleButton.setDisable(azureSelected);
        azureKeyToggleButton.setSelected(azureSelected);

        if (azureSelected) {
            publicKeyInput.setText("");
            privateKeyInput.setText("");
            log.info("Using Azure keys");
        } else {
            publicKeyInput.setDisable(false);
            publicKeyInput.setEditable(true);
            log.info("Azure keys disabled");
        }
    }

    private void toggleTestKeys() {
        boolean useTestKeys = testKeysToggleButton.isSelected();
        publicKeyInput.setEditable(!useTestKeys);
        publicKeyInput.setDisable(useTestKeys);
        privateKeyInput.setEditable(!useTestKeys);
        privateKeyInput.setDisable(useTestKeys);

        if (useTestKeys) {
            publicKeyInput.setText(Constants.TEST_PUBLIC_KEY);
            privateKeyInput.setText(Constants.TEST_PRIVATE_KEY);
            log.info("Using test keys");
        } else {
            publicKeyInput.setText("");
            privateKeyInput.setText("");
            log.info("Test keys disabled");
        }
    }
}
