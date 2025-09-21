package com.utilities.gui;

import com.utilities.pdf.PDFService;
import com.utilities.converter.UnitConverter;
import com.utilities.calculator.Calculator;
import com.utilities.texttools.TextTools;
import com.utilities.colorpicker.ColorPicker;
import com.utilities.utils.ErrorDialog;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * Fallback Main Window for systems with OCR compatibility issues
 * This version disables OCR functionality but keeps all other features
 */
public class MainWindowFallback extends JFrame {
    
    private JTabbedPane tabbedPane;
    private JLabel statusLabel;
    private JProgressBar progressBar;
    
    // Service instances (excluding OCR)
    private PDFService pdfService;
    private UnitConverter unitConverter;
    private Calculator calculator;
    private TextTools textTools;
    private ColorPicker colorPicker;
    
    public MainWindowFallback() {
        initializeServices();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Utilities - Professional Tool Suite v2.0 (Fallback Mode)");
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setResizable(true);
    }
    
    private void initializeServices() {
        try {
            pdfService = new PDFService();
            unitConverter = new UnitConverter();
            calculator = new Calculator();
            textTools = new TextTools();
            colorPicker = new ColorPicker();
        } catch (Exception e) {
            ErrorDialog.showError(this, "Failed to initialize services", e);
        }
    }
    
    private void initializeComponents() {
        tabbedPane = new JTabbedPane();
        statusLabel = new JLabel("Ready (OCR disabled due to architecture compatibility)");
        progressBar = new JProgressBar();
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        
        // Initialize tabs
        initializeTabs();
    }
    
    private void initializeTabs() {
        // Image to Text tab (disabled)
        JPanel imageTab = new JPanel(new BorderLayout());
        JLabel disabledLabel = new JLabel("<html><center><h2>Image to Text (OCR) - Disabled</h2>" +
                                         "<p>OCR functionality is disabled due to architecture compatibility issues.</p>" +
                                         "<p>This typically occurs on Apple Silicon Macs with older JNA libraries.</p>" +
                                         "<p>All other features are fully functional.</p></center></html>");
        disabledLabel.setHorizontalAlignment(SwingConstants.CENTER);
        disabledLabel.setVerticalAlignment(SwingConstants.CENTER);
        imageTab.add(disabledLabel, BorderLayout.CENTER);
        tabbedPane.addTab("Image to Text", null, imageTab, "OCR disabled - Architecture compatibility issue");
        
        // PDF to Text tab
        PDFToTextTab pdfTab = new PDFToTextTab(pdfService, statusLabel, progressBar);
        tabbedPane.addTab("PDF to Text", null, pdfTab, "Extract text from PDF documents");
        
        // Unit Converter tab
        UnitConverterTab converterTab = new UnitConverterTab(unitConverter, statusLabel);
        tabbedPane.addTab("Unit Converter", null, converterTab, "Convert between different units");
        
        // Calculator tab
        CalculatorTab calcTab = new CalculatorTab(calculator, statusLabel);
        tabbedPane.addTab("TI-84 Calculator", null, calcTab, "Full-featured calculator replica");
        
        // Text Tools tab
        TextToolsTab textTab = new TextToolsTab(textTools, statusLabel);
        tabbedPane.addTab("Text Tools", null, textTab, "Text manipulation and analysis tools");
        
        // Color Picker tab
        ColorPickerTab colorTab = new ColorPickerTab(colorPicker, statusLabel);
        tabbedPane.addTab("Color Picker", null, colorTab, "Color picker and converter");
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // Add tabs
        add(tabbedPane, BorderLayout.CENTER);
        
        // Status bar
        JPanel statusPanel = new JPanel(new BorderLayout());
        statusPanel.setBorder(new EmptyBorder(5, 10, 5, 10));
        statusPanel.add(statusLabel, BorderLayout.WEST);
        statusPanel.add(progressBar, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);
    }
    
    private void setupEventHandlers() {
        // Add any global event handlers here
    }
    
    @Override
    public void dispose() {
        // Clean up resources
        if (pdfService != null) {
            pdfService.cleanup();
        }
        super.dispose();
    }
}
