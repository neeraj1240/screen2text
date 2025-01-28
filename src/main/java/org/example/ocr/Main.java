package org.example.ocr;

public class Main {
    public static void main(String[] args) {
        System.setProperty("sun.java2d.dpiaware", "true");
        System.setProperty("sun.java2d.uiScale", "1.0");
        System.setProperty("sun.awt.noerasebackground", "true");
        System.setProperty("sun.awt.keepWorkingSetOnMinimize", "true");

        System.setProperty("sun.java2d.d3d", "false");

        javax.swing.SwingUtilities.invokeLater(() -> {
            SystemTrayManager.initialize();
        });
    }
}