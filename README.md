# Utilities - Professional Java GUI Platform v2.0

A comprehensive, professionally-structured Java GUI application that provides a complete suite of utility tools for everyday tasks. Built with Java Swing and organized using modern software architecture principles, this application offers a clean, intuitive interface with drag-and-drop functionality and enterprise-grade features.

## 🏗️ **Professional Project Structure**

This project follows industry-standard practices with:
- **Maven-style directory layout** (`src/main/java/`)
- **Package-based organization** (`com.utilities.*`)
- **Modular architecture** with clear separation of concerns
- **Service-oriented design** for maintainability
- **Enhanced error handling** with copy functionality

## 🚀 Features

### 📸 Image to Text (OCR)
- **Drag & Drop Support**: Simply drag image files onto the interface
- **Multiple Formats**: PNG, JPG, JPEG, GIF, BMP, TIFF support
- **Tesseract OCR**: High-accuracy text extraction
- **Real-time Processing**: Background processing with progress indicators
- **Copy to Clipboard**: One-click text copying

### 📄 PDF to Text
- **Text Extraction**: Extract text from PDF documents
- **OCR Fallback**: Automatic OCR for image-based PDFs
- **Batch Processing**: Handle multiple pages efficiently
- **Format Preservation**: Maintain text formatting where possible

### 🔄 Unit Converter
- **Comprehensive Categories**: Length, Weight, Temperature, Area, Volume, Time, Speed, Energy, Pressure, Data
- **Real-time Conversion**: Instant results as you type
- **History Tracking**: Keep track of recent conversions
- **Precision Control**: Configurable decimal places

### 🧮 TI-84 Calculator
- **Full Replica**: Complete functionality of TI-84 calculator
- **Scientific Functions**: Trigonometry, logarithms, powers, roots
- **Memory Operations**: Store and recall values
- **Expression Evaluation**: Complex mathematical expressions

### 📝 Text Tools
- **Word Count**: Character and word counting
- **Case Conversion**: Upper, lower, title case
- **Text Manipulation**: Reverse, remove spaces, format
- **Analysis Tools**: Text statistics and insights

### 🎨 Color Picker
- **Visual Selection**: Intuitive color picker interface
- **Multiple Formats**: Hex, RGB, HSL color codes
- **Live Preview**: Real-time color display
- **Copy to Clipboard**: One-click color code copying

## 🛠️ Installation & Setup

### Prerequisites
- **Java 8 or higher** (Java 11+ recommended)
- **Tesseract OCR** (for image-to-text functionality)
- **Required Libraries** (included in `lib/` directory)

### Quick Start

1. **Build the Application**:
   ```bash
   ./build.sh
   ```

2. **Run the Application**:
   ```bash
   # Option 1: Direct execution
   java -cp "build/classes:lib/*" com.utilities.UtilitiesApp
   
   # Option 2: JAR file
   java -jar build/utilities.jar
   
   # Option 3: Diagnostic launcher (recommended)
   ./run_utilities.sh
   ```

### Installation Steps

1. **Install Tesseract OCR**:
   - **macOS**: `brew install tesseract`
   - **Windows**: Download from [GitHub releases](https://github.com/UB-Mannheim/tesseract/wiki)
   - **Linux**: `sudo apt-get install tesseract-ocr`

2. **Verify Installation**:
   ```bash
   tesseract --version
   ```

3. **Run Diagnostic Script**:
   ```bash
   ./run_utilities.sh
   ```

### Project Structure
```
Utilities-Professional/
├── src/main/java/com/utilities/
│   ├── gui/              # GUI components and tabs
│   ├── ocr/              # OCR services
│   ├── pdf/              # PDF processing
│   ├── converter/        # Unit conversion
│   ├── calculator/       # Calculator functionality
│   ├── texttools/        # Text manipulation
│   ├── colorpicker/      # Color tools
│   └── utils/            # Utility classes
├── lib/                  # External libraries
├── resources/            # Application resources
├── docs/                 # Documentation
├── build/                # Build output
├── build.sh              # Build script
├── run_utilities.sh      # Launch script
└── README.md             # This file
```

## 📖 Usage Guide

### Getting Started
1. Launch the application using one of the methods above
2. Navigate between tabs using the tabbed interface
3. Use drag & drop for file inputs where supported
4. Copy results to clipboard using the copy buttons

### Image to Text
1. Go to the "Image to Text" tab
2. Drag and drop an image file or click "Upload Image"
3. Wait for processing to complete
4. Copy the extracted text using the "Copy Text" button

### PDF to Text
1. Go to the "PDF to Text" tab
2. Upload a PDF file
3. Extract text or use OCR for image-based PDFs
4. Save or copy the extracted text

### Unit Converter
1. Go to the "Unit Converter" tab
2. Select the category and units
3. Enter the value to convert
4. View the result and conversion history

## 🔧 Technical Details

### Dependencies
- **Java 8+** (tested on Java 11, 17, 21)
- **Tesseract OCR** for image text extraction
- **Apache PDFBox** for PDF processing
- **Java Swing** for GUI framework

### Architecture
- **MVC Pattern** with clear separation
- **Service Layer** for business logic
- **GUI Layer** for user interface
- **Utility Layer** for common functions

### Error Handling
- **Comprehensive error catching** at all levels
- **User-friendly error messages** with technical details
- **Copy functionality** for easy error reporting
- **Graceful degradation** when services unavailable

## 🚨 **Apple Silicon (ARM64) Compatibility**

If you're on an Apple Silicon Mac and encounter OCR issues:

1. **Use the fallback version:**
   ```bash
   java -jar build/utilities-fallback.jar
   ```

2. **Or use the diagnostic launcher:**
   ```bash
   ./run_utilities.sh
   ```

The fallback version disables OCR but keeps all other features fully functional.

## 🛠️ **Troubleshooting**

### Common Issues

**OCR Not Working on Apple Silicon:**
- Use the fallback version: `java -jar build/utilities-fallback.jar`
- This is due to JNA library architecture compatibility
- All other features work perfectly

**Build Errors:**
- Ensure Java 8+ is installed
- Run `./build.sh` to rebuild
- Check console for detailed error messages

**Memory Issues:**
- Increase heap size: `java -Xmx2g -jar build/utilities.jar`
- Close other applications to free memory

## 🔄 Version History

### Version 2.0 (Current) - Professional Edition
- **Professional project structure** with Maven-style layout
- **Enhanced error handling** with copy functionality
- **Modular architecture** with service-oriented design
- **Apple Silicon compatibility** with fallback mode
- **Comprehensive build system** with automated scripts
- **Image to Text (OCR)** with improved error handling
- **PDF to Text extraction** (placeholder ready)
- **Unit Converter** (placeholder ready)
- **TI-84 Calculator** (placeholder ready)
- **Text manipulation tools** (placeholder ready)
- **Color picker and converter** (placeholder ready)
- **Drag & drop support** throughout
- **Professional error dialogs** with technical details

### Version 1.0 - Legacy
- Initial release with basic functionality
- Single-file implementation
- Basic error handling

## 📞 **Support & Development**

This professional structure makes the project:
- **Easy to maintain** and extend
- **Simple to debug** with clear error messages
- **Ready for production** use
- **Scalable** for additional features

The enhanced error handling with copy functionality makes it easy to share error details for troubleshooting and support.

---

**Utilities v2.0** - Professional all-in-one solution for everyday computing tasks. Built with ❤️ using Java and modern software architecture principles.