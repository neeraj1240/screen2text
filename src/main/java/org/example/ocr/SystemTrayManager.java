package org.example.ocr;

import dorkbox.systemTray.SystemTray;
import dorkbox.systemTray.Entry;
import dorkbox.systemTray.MenuItem;
import dorkbox.systemTray.Separator;

import java.io.File;

public class SystemTrayManager {
    public static void initialize() {
        SystemTray systemTray = SystemTray.get();
        if (systemTray == null) {
            throw new RuntimeException("System tray not supported");
        }

        systemTray.setImage(new File("C:\\Users\\NEERAJ\\Desktop\\OCR TOOL\\OCR-TOOL\\src\\main\\resources\\download.png"));
        systemTray.setStatus("OCR Tool");

        MenuItem captureItem = new MenuItem("Capture Screen");
        captureItem.setCallback(e -> TransparentOverlay.startSelection());

        MenuItem exitItem = new MenuItem("Exit");
        exitItem.setCallback(e -> System.exit(0));

        systemTray.getMenu().add(captureItem);
        systemTray.getMenu().add(new Separator());
        systemTray.getMenu().add(exitItem);
    }
}