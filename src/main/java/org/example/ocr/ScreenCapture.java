package org.example.ocr;

import java.awt.*;
import java.awt.image.BufferedImage;

public class ScreenCapture {
    public static BufferedImage capture(Rectangle rect) {
        try {
            Robot robot = new Robot();
            return robot.createScreenCapture(rect);
        } catch (AWTException e) {
            e.printStackTrace();
            return null;
        }
    }
}