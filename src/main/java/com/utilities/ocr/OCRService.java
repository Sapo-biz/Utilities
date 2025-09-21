package com.utilities.ocr;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * OCR Service for Image to Text conversion
 * Handles Tesseract initialization and image processing
 */
public class OCRService {
    
    private static final Logger logger = Logger.getLogger(OCRService.class.getName());
    private Tesseract tesseract;
    private boolean isInitialized = false;
    private String lastError = null;
    
    public OCRService() {
        initializeTesseract();
    }
    
    /**
     * Initialize Tesseract OCR engine
     */
    private void initializeTesseract() {
        try {
            // Check if Tesseract classes are available
            Class.forName("net.sourceforge.tess4j.Tesseract");
            
            tesseract = new Tesseract();
            String[] possiblePaths = {
                "/opt/homebrew/share/tessdata",
                "/usr/local/share/tessdata", 
                "/usr/share/tessdata",
                "tessdata"
            };
            
            boolean tessdataFound = false;
            for (String path : possiblePaths) {
                File tessdataDir = new File(path);
                if (tessdataDir.exists() && tessdataDir.isDirectory()) {
                    tesseract.setDatapath(path);
                    tessdataFound = true;
                    logger.info("Using tessdata path: " + path);
                    break;
                }
            }
            
            if (!tessdataFound) {
                lastError = "Tessdata directory not found. Searched paths: " + String.join(", ", possiblePaths);
                logger.warning(lastError);
                return;
            }
            
            // Configure Tesseract exactly like the original working version
            tesseract.setLanguage("eng");
            tesseract.setPageSegMode(1);
            tesseract.setOcrEngineMode(1);
            
            isInitialized = true;
            lastError = null;
            logger.info("Tesseract OCR initialized successfully");
            
        } catch (ClassNotFoundException e) {
            lastError = "Tesseract library not found in classpath. Please ensure tess4j JAR files are available.";
            logger.severe(lastError);
        } catch (Exception e) {
            lastError = "Unexpected error initializing Tesseract: " + e.getMessage();
            logger.severe(lastError);
        }
    }
    
    /**
     * Extract text from an image file
     * @param imageFile The image file to process
     * @return Extracted text or null if failed
     * @throws Exception If OCR processing fails
     */
    public String extractTextFromFile(File imageFile) throws Exception {
        if (!isInitialized) {
            throw new Exception("OCR not initialized: " + lastError);
        }
        
        try {
            BufferedImage image = javax.imageio.ImageIO.read(imageFile);
            if (image == null) {
                throw new Exception("Could not read image file: " + imageFile.getName());
            }
            
            logger.info("Processing image: " + imageFile.getName() + " (" + image.getWidth() + "x" + image.getHeight() + ")");
            String result = tesseract.doOCR(image);
            logger.info("OCR completed successfully for: " + imageFile.getName());
            return result;
            
        } catch (Exception e) {
            logger.severe("OCR failed for " + imageFile.getName() + ": " + e.getMessage());
            throw new Exception("Failed to extract text from image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Extract text from a BufferedImage
     * @param image The image to process
     * @return Extracted text or null if failed
     * @throws Exception If OCR processing fails
     */
    public String extractTextFromImage(BufferedImage image) throws Exception {
        if (!isInitialized) {
            throw new Exception("OCR not initialized: " + lastError);
        }
        
        try {
            logger.info("Processing BufferedImage (" + image.getWidth() + "x" + image.getHeight() + ")");
            String result = tesseract.doOCR(image);
            logger.info("OCR completed successfully");
            return result;
            
        } catch (Exception e) {
            logger.severe("OCR failed: " + e.getMessage());
            throw new Exception("Failed to extract text from image: " + e.getMessage(), e);
        }
    }
    
    /**
     * Check if OCR service is available
     * @return true if OCR is ready to use
     */
    public boolean isAvailable() {
        return isInitialized;
    }
    
    /**
     * Get the last error message
     * @return Error message or null if no error
     */
    public String getLastError() {
        return lastError;
    }
    
    /**
     * Get detailed status information
     * @return Status string with initialization details
     */
    public String getStatus() {
        if (isInitialized) {
            return "OCR Ready - Tesseract initialized successfully";
        } else {
            return "OCR Unavailable - " + (lastError != null ? lastError : "Unknown error");
        }
    }
}
