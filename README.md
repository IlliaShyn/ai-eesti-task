# Real Estate Appraisal Report Updater

This application automates the process of updating real estate appraisal reports by extracting and replacing outdated market reviews with the latest data from a knowledge base.

## Features
- Automatically detects the correct regional knowledge base file based on the appraisal report.
- Extracts relevant sections from knowledge base documents.
- Replaces outdated sections with updated content.
- Logs all changes made during the update process.

## Prerequisites
- Java 17 or later
- Apache Maven

## How to Run
Clone the repository and navigate to the project directory, then execute:
```sh
mvn javafx:run -f pom.xml
```

## Project Structure
```
/src/main/java/org/aieesti/
    ├── AppraisalProcessor.java  # Main processing logic
    ├── JavaFXText.java          # GUI setup
    ├── MainApp.java             # GUI launch
    ├── MainController.java      # File choosing and update connected to GUI elements
/src/main/resources/
    ├── teadmistebaas_õismäe_2025.docx  # Example knowledge base files
    ├── teadmistebaas_kristiine_2025.docx
    ├── teadmistebaas_Üldülevaated_Eesti_Tln_Harjumaa_2025.docx
    ├── teadmistebaas_majandus_2025.docx
    ├── update_log.txt  # Log of changes
pom.xml  # Maven build configuration
```

## Usage
1. Place the appraisal report DOCX file in the appropriate directory.
2. Run the application.
3. The updated version of the appraisal report will be generated alongside the original file with `_UPDATED.docx` appended to the filename.
4. Changes are logged in `update_log.txt`.

---

For any issues or improvements, feel free to contribute!

