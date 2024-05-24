package com.inditex.icdmsuscon.gui.log;


import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.nio.charset.StandardCharsets;

public class GuiAppender extends AppenderBase<ILoggingEvent> {

    private static TextArea logTextArea;
    private PatternLayoutEncoder encoder;

    public static void setTextArea(TextArea textArea) {
        logTextArea = textArea;
    }

    @Override
    public void start() {
        encoder = new PatternLayoutEncoder();
        encoder.setContext(context);
        encoder.setPattern("%d{yyyy-MM-dd HH:mm:ss} [%level] - %msg%n");
        encoder.setCharset(StandardCharsets.UTF_8);
        encoder.start();
        super.start();
    }

    @Override
    protected void append(ILoggingEvent eventObject) {
        if (logTextArea != null) {
            String formattedMessage = encoder.getLayout().doLayout(eventObject);
            Platform.runLater(() -> logTextArea.appendText(formattedMessage));
        }
    }
}
