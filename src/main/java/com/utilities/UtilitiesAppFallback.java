package com.utilities;

import com.utilities.gui.MainWindowFallback;
import com.utilities.utils.ErrorDialog;
import javax.swing.*;

/**
 * Fallback Utilities Application for systems with OCR compatibility issues
 * This version disables OCR functionality but keeps all other features
 * 
 * @author Jason He
 * @version 2.0
 * @since 2024
 */
public class UtilitiesAppFallback {
    
    public static void main(String[] args) {
        // Set system look and feel
        try {
            // UIManager.setLookAndFeel(UIManager.getSystemLookAndFeel());
        } catch (Exception e) {
            System.err.println("Could not set system look and feel: " + e.getMessage());
        }
        
        // Enable native access for JNA (Tesseract)
        System.setProperty("jna.nosys", "false");
        
        // Start the application on EDT
        SwingUtilities.invokeLater(() -> {
            try {
                MainWindowFallback window = new MainWindowFallback();
                window.setVisible(true);
            } catch (Exception e) {
                // Show error dialog if main window fails to create
                ErrorDialog.showError(null, "Failed to start application", e);
                System.exit(1);
            }
        });
    }
}
