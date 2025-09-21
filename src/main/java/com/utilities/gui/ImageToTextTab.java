package com.utilities.gui;

import com.utilities.ocr.OCRService;
import com.utilities.utils.ErrorDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.dnd.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Image to Text Tab - OCR functionality with drag and drop support
 */
public class ImageToTextTab extends JPanel implements DropTargetListener {
    
    private OCRService ocrService;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    
    // UI Components
    private JTextArea textArea;
    private JButton uploadButton;
    private JButton copyButton;
    private JButton clearButton;
    private JLabel dropZoneLabel;
    
    public ImageToTextTab(OCRService ocrService, JLabel statusLabel, JProgressBar progressBar) {
        this.ocrService = ocrService;
        this.statusLabel = statusLabel;
        this.progressBar = progressBar;
        
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        setupDragAndDrop();
    }
    
    private void initializeComponents() {
        // Drop zone
        dropZoneLabel = new JLabel("<html><center><b>Drag & Drop Image Here</b><br>" +
                                  "or click Upload to select file<br>" +
                                  "<small>Supported: PNG, JPG, JPEG, GIF, BMP, TIFF</small></center></html>");
        dropZoneLabel.setHorizontalAlignment(SwingConstants.CENTER);
        dropZoneLabel.setVerticalAlignment(SwingConstants.CENTER);
        dropZoneLabel.setForeground(new Color(70, 130, 180));
        dropZoneLabel.setFont(new Font("Arial", Font.PLAIN, 14));
        
        // Buttons
        uploadButton = createStyledButton("Upload Image", new Color(70, 130, 180));
        copyButton = createStyledButton("Copy Text", new Color(34, 139, 34));
        clearButton = createStyledButton("Clear", new Color(220, 20, 60));
        
        copyButton.setEnabled(false);
        clearButton.setEnabled(false);
        
        // Text area
        textArea = new JTextArea();
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        textArea.setBackground(Color.WHITE);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
    }
    
    private JButton createStyledButton(String text, Color backgroundColor) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBackground(backgroundColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        return button;
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        
        // Drop zone panel
        JPanel dropZonePanel = new JPanel(new BorderLayout());
        dropZonePanel.setBorder(new TitledBorder("Drop Zone"));
        dropZonePanel.setPreferredSize(new Dimension(0, 120));
        dropZonePanel.setBackground(new Color(240, 248, 255));
        dropZonePanel.add(dropZoneLabel, BorderLayout.CENTER);
        
        // Control panel
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        controlPanel.setBorder(new TitledBorder("Controls"));
        controlPanel.add(uploadButton);
        controlPanel.add(copyButton);
        controlPanel.add(clearButton);
        
        // Text panel
        JPanel textPanel = new JPanel(new BorderLayout());
        textPanel.setBorder(new TitledBorder("Extracted Text"));
        textPanel.setPreferredSize(new Dimension(0, 250));
        
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setBorder(BorderFactory.createLoweredBevelBorder());
        
        textPanel.add(scrollPane, BorderLayout.CENTER);
        
        // Add components
        add(dropZonePanel, BorderLayout.NORTH);
        add(controlPanel, BorderLayout.CENTER);
        add(textPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        uploadButton.addActionListener(e -> openFileChooser());
        copyButton.addActionListener(e -> copyToClipboard());
        clearButton.addActionListener(e -> clearText());
    }
    
    private void setupDragAndDrop() {
        new DropTarget(dropZoneLabel, this);
    }
    
    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Image Files", "png", "jpg", "jpeg", "gif", "bmp", "tiff", "tif");
        fileChooser.setFileFilter(filter);
        
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            processImage(selectedFile);
        }
    }
    
    private void processImage(File imageFile) {
        if (!ocrService.isAvailable()) {
            ErrorDialog.showError(this, 
                "OCR not available.\n\n" +
                "This could be due to:\n" +
                "1. Tesseract library not found in classpath\n" +
                "2. Missing tessdata files\n" +
                "3. Native library compatibility issues\n\n" +
                "Please check the console for detailed error messages.\n" +
                "Other features (PDF text extraction, Unit Converter, Calculator, etc.) will still work.",
                ocrService.getLastError() != null ? new Exception(ocrService.getLastError()) : null);
            return;
        }
        
        SwingWorker<String, Void> worker = new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                SwingUtilities.invokeLater(() -> {
                    progressBar.setVisible(true);
                    progressBar.setIndeterminate(true);
                    progressBar.setString("Processing image...");
                    statusLabel.setText("Extracting text from: " + imageFile.getName());
                    uploadButton.setEnabled(false);
                });
                
                try {
                    return ocrService.extractTextFromFile(imageFile);
                } catch (Exception e) {
                    throw new Exception("Error processing image: " + e.getMessage(), e);
                }
            }
            
            @Override
            protected void done() {
                try {
                    String result = get();
                    textArea.setText(result);
                    textArea.setCaretPosition(0);
                    copyButton.setEnabled(true);
                    clearButton.setEnabled(true);
                    statusLabel.setText("Text extracted successfully from: " + imageFile.getName());
                } catch (Exception e) {
                    ErrorDialog.showError(ImageToTextTab.this, 
                        "Failed to extract text from image: " + imageFile.getName(), e);
                    statusLabel.setText("Error extracting text");
                } finally {
                    progressBar.setVisible(false);
                    uploadButton.setEnabled(true);
                }
            }
        };
        
        worker.execute();
    }
    
    private void copyToClipboard() {
        String text = textArea.getText();
        if (text != null && !text.trim().isEmpty()) {
            StringSelection selection = new StringSelection(text);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            statusLabel.setText("Text copied to clipboard");
        }
    }
    
    private void clearText() {
        textArea.setText("");
        copyButton.setEnabled(false);
        clearButton.setEnabled(false);
        statusLabel.setText("Text cleared");
    }
    
    // DropTargetListener implementation
    @Override
    public void dragEnter(DropTargetDragEvent dtde) {
        if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
            dtde.acceptDrag(DnDConstants.ACTION_COPY);
            dropZoneLabel.setText("<html><center><b>Drop Image Here</b><br>" +
                                 "<small>Release to process image</small></center></html>");
            dropZoneLabel.setForeground(new Color(0, 100, 0));
        } else {
            dtde.rejectDrag();
        }
    }
    
    @Override
    public void dragOver(DropTargetDragEvent dtde) {
        // Visual feedback can be added here
    }
    
    @Override
    public void dropActionChanged(DropTargetDragEvent dtde) {
        // Not needed for this implementation
    }
    
    @Override
    public void dragExit(DropTargetEvent dte) {
        dropZoneLabel.setText("<html><center><b>Drag & Drop Image Here</b><br>" +
                             "or click Upload to select file<br>" +
                             "<small>Supported: PNG, JPG, JPEG, GIF, BMP, TIFF</small></center></html>");
        dropZoneLabel.setForeground(new Color(70, 130, 180));
    }
    
    @Override
    public void drop(DropTargetDropEvent dtde) {
        try {
            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                
                @SuppressWarnings("unchecked")
                java.util.List<File> files = (java.util.List<File>) dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);
                
                if (files.size() > 0) {
                    File file = files.get(0);
                    if (isImageFile(file)) {
                        processImage(file);
                    } else {
                        ErrorDialog.showError(this, "Please drop an image file (PNG, JPG, JPEG, GIF, BMP, TIFF)");
                    }
                }
                
                dtde.dropComplete(true);
            } else {
                dtde.rejectDrop();
            }
        } catch (Exception e) {
            ErrorDialog.showError(this, "Error processing dropped file", e);
            dtde.dropComplete(false);
        } finally {
            // Reset drop zone appearance
            dropZoneLabel.setText("<html><center><b>Drag & Drop Image Here</b><br>" +
                                 "or click Upload to select file<br>" +
                                 "<small>Supported: PNG, JPG, JPEG, GIF, BMP, TIFF</small></center></html>");
            dropZoneLabel.setForeground(new Color(70, 130, 180));
        }
    }
    
    private boolean isImageFile(File file) {
        String name = file.getName().toLowerCase();
        return name.endsWith(".png") || name.endsWith(".jpg") || name.endsWith(".jpeg") ||
               name.endsWith(".gif") || name.endsWith(".bmp") || name.endsWith(".tiff") ||
               name.endsWith(".tif");
    }
}
