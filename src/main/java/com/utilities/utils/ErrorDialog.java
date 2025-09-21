package com.utilities.utils;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

/**
 * Enhanced Error Dialog with Copy Functionality
 * Provides user-friendly error dialogs with the ability to copy error details
 */
public class ErrorDialog {
    
    /**
     * Shows an error dialog with copy functionality
     * @param parent The parent component
     * @param title The dialog title
     * @param message The error message
     * @param details Additional error details (can be null)
     */
    public static void showError(Component parent, String title, String message, String details) {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        
        // Main error message
        JTextArea messageArea = new JTextArea(message);
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Arial", Font.PLAIN, 12));
        messageArea.setBackground(new Color(255, 240, 240));
        messageArea.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        messageArea.setLineWrap(true);
        messageArea.setWrapStyleWord(true);
        
        JScrollPane messageScroll = new JScrollPane(messageArea);
        messageScroll.setPreferredSize(new Dimension(500, 150));
        
        // Details section (if provided)
        JScrollPane detailsScroll = null;
        if (details != null && !details.trim().isEmpty()) {
            JTextArea detailsArea = new JTextArea("Technical Details:\n" + details);
            detailsArea.setEditable(false);
            detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
            detailsArea.setBackground(new Color(248, 248, 248));
            detailsArea.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
            
            detailsScroll = new JScrollPane(detailsArea);
            detailsScroll.setPreferredSize(new Dimension(500, 100));
            detailsScroll.setBorder(BorderFactory.createTitledBorder("Technical Details"));
        }
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        
        JButton copyButton = new JButton("Copy Error Details");
        copyButton.setBackground(new Color(70, 130, 180));
        copyButton.setForeground(Color.BLACK);
        copyButton.setFocusPainted(false);
        copyButton.addActionListener(e -> {
            String fullError = title + "\n\n" + message;
            if (details != null && !details.trim().isEmpty()) {
                fullError += "\n\nTechnical Details:\n" + details;
            }
            
            StringSelection selection = new StringSelection(fullError);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            
            copyButton.setText("Copied!");
            copyButton.setBackground(new Color(34, 139, 34));
            
            // Reset button after 2 seconds
            Timer timer = new Timer(2000, ev -> {
                copyButton.setText("Copy Error Details");
                copyButton.setBackground(new Color(70, 130, 180));
            });
            timer.setRepeats(false);
            timer.start();
        });
        
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(220, 20, 60));
        okButton.setForeground(Color.BLACK);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor((Component) e.getSource());
            if (window != null) {
                window.dispose();
            }
        });
        
        buttonPanel.add(copyButton);
        buttonPanel.add(okButton);
        
        // Assemble the dialog
        panel.add(messageScroll, BorderLayout.CENTER);
        if (detailsScroll != null) {
            panel.add(detailsScroll, BorderLayout.SOUTH);
        }
        panel.add(buttonPanel, BorderLayout.PAGE_END);
        
        // Show the dialog
        JOptionPane.showMessageDialog(parent, panel, title, JOptionPane.ERROR_MESSAGE);
    }
    
    /**
     * Shows a simple error dialog with copy functionality
     * @param parent The parent component
     * @param message The error message
     */
    public static void showError(Component parent, String message) {
        showError(parent, "Error", message, null);
    }
    
    /**
     * Shows an error dialog with technical details
     * @param parent The parent component
     * @param message The error message
     * @param exception The exception that caused the error
     */
    public static void showError(Component parent, String message, Exception exception) {
        String details = exception.getClass().getSimpleName() + ": " + exception.getMessage();
        if (exception.getCause() != null) {
            details += "\nCaused by: " + exception.getCause().getClass().getSimpleName() + 
                      ": " + exception.getCause().getMessage();
        }
        showError(parent, "Error", message, details);
    }
}
