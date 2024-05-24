package com.inditex.icdmsuscon.gui;

import com.inditex.icdmsuscon.gui.log.GuiAppender;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
public class MainGui extends Application {

    private TextArea logTextArea;
    private ExecutorService executorService;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Emission CSV to SQL");

        executorService = Executors.newCachedThreadPool();

        VBox root = new VBox();
        root.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        root.setSpacing(10);

        MenuBar menuBar = createMenuBar();
        root.getChildren().add(menuBar);

        KeysPanel keysPanel = new KeysPanel(executorService);
        root.getChildren().add(keysPanel);

        logTextArea = new TextArea();
        logTextArea.setEditable(false);
        logTextArea.setPrefHeight(200);
        VBox.setVgrow(logTextArea, Priority.ALWAYS);

        root.getChildren().add(logTextArea);

        Scene scene = new Scene(root, 1200, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        GuiAppender.setTextArea(logTextArea);
        log.debug("Application started");

        primaryStage.setOnCloseRequest(event -> {
            shutdownExecutorService();
            Platform.exit();
            System.exit(0);
        });
        keysPanel.toggleKeyUsage(true);
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        Menu helpMenu = new Menu("Help");
        MenuItem helpItem = new MenuItem("How to Use");
        helpItem.setOnAction(e -> logHelpMessage());
        helpMenu.getItems().add(helpItem);
        menuBar.getMenus().add(helpMenu);
        return menuBar;
    }

    private void logHelpMessage() {
        String helpMessage = HelpContent.getHelpMessage();
        log.info(helpMessage);
    }

    private void shutdownExecutorService() {
        log.debug("Shutting down executor service");
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                executorService.shutdownNow();
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    log.error("Executor service did not terminate");
                }
            }
        } catch (InterruptedException ex) {
            executorService.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
