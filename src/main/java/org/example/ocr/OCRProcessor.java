package org.example.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class OCRProcessor {
    private static final Tesseract tesseract = new Tesseract();

    static {
        try {

            String userHome = System.getProperty("user.home");
            Path tessdataDir = Paths.get(userHome, "tessdata");
            Files.createDirectories(tessdataDir);


            Path trainingDataFile = tessdataDir.resolve("eng.traineddata");
            if (!Files.exists(trainingDataFile)) {
                try {

                    Path sourceFile = Paths.get("src", "main", "resources", "tessdata", "eng.traineddata");
                    if (Files.exists(sourceFile)) {
                        Files.copy(sourceFile, trainingDataFile, StandardCopyOption.REPLACE_EXISTING);
                        System.out.println("Copied training data from: " + sourceFile);
                    } else {
                        throw new RuntimeException("Could not find eng.traineddata in resources");
                    }
                } catch (IOException e) {
                    System.err.println("Failed to copy training data: " + e.getMessage());
                    throw e;
                }
            }


            System.out.println("Setting tessdata path to: " + tessdataDir);
            tesseract.setDatapath(tessdataDir.toString());
            tesseract.setLanguage("eng");
            tesseract.setPageSegMode(6);


            if (!Files.exists(trainingDataFile)) {
                throw new RuntimeException("Training data file not found at: " + trainingDataFile);
            }

            System.out.println("Successfully initialized Tesseract with training data at: " + trainingDataFile);

        } catch (Exception e) {
            System.err.println("Failed to initialize Tesseract: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to initialize Tesseract: " + e.getMessage(), e);
        }
    }

    public static String extractText(BufferedImage image) {
        try {
            if (image == null) {
                return "Error: No image provided";
            }
            return tesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
            return "OCR Error: " + e.getMessage();
        }
    }
}