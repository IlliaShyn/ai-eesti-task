package org.aieesti;

import org.apache.poi.xwpf.usermodel.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class AppraisalProcessor {

    public static boolean updateFile(File appraisalFile) {
        // Detect the correct regional knowledge base file based on the filename
        String fileName = appraisalFile.getName().toLowerCase();
        String regionalFile;

        if (fileName.contains("õismäe")) {
            regionalFile = "src/main/resources/teadmistebaas_õismäe_2025.docx";
        } else if (fileName.contains("kristiine")) {
            regionalFile = "src/main/resources/teadmistebaas_kristiine_2025.docx";
        } else {
            System.err.println("Could not determine the correct regional file.");
            return false;
        }

        // Define other knowledge base files
        String generalFile = "src/main/resources/teadmistebaas_Üldülevaated_Eesti_Tln_Harjumaa_2025.docx";
        String economicFile = "src/main/resources/teadmistebaas_majandus_2025.docx";
        String updatedFilePath = appraisalFile.getParent() + "/" + appraisalFile.getName().replace(".docx", "_UPDATED.docx");
        String logFilePath = "src/main/resources/update_log.txt";

        try {
            // Extract text from knowledge base files
            String regionalContent = extractTextFromDocx(regionalFile);
            String generalContent = extractTextFromDocx(generalFile);
            String economicContent = extractTextFromDocx(economicFile);

            // Read the appraisal document
            FileInputStream fis = new FileInputStream(appraisalFile);
            XWPFDocument document = new XWPFDocument(fis);
            fis.close();

            // Track changes for logging
            List<String> changes = new ArrayList<>();

            // Define section updates
            Map<String, String> sectionUpdates = new LinkedHashMap<>();
            sectionUpdates.put("Makromajanduslik taust\n", economicContent);
            sectionUpdates.put("Eesti kinnisvaraturg\n", generalContent);
            sectionUpdates.put("Tallinna kinnisvaraturg\n", generalContent);
            sectionUpdates.put("Õismäe linnaosa \n", regionalContent);
            sectionUpdates.put("Kristiine linnaosa korteriturg\n", regionalContent);

            boolean isReplacing = false;
            String currentSection = "";
            for (XWPFParagraph paragraph : document.getParagraphs()) {
                String text = paragraph.getText().trim();

                if (sectionUpdates.containsKey(text)) {
                    isReplacing = true;
                    currentSection = text;
                    changes.add("Updated '" + currentSection + "' from " +
                            (currentSection.equals("Makromajanduslik taust") ? economicFile :
                                    (currentSection.contains("korteriturg") ? regionalFile : generalFile)));

                    // Clear the paragraph and set new heading with spacing
//                    String newText = text + "\n" + "\n\n" + sectionUpdates.get(currentSection);
                    replaceHeadingText(paragraph, sectionUpdates.get(currentSection));
//                    insertText(paragraph, sectionUpdates.get(currentSection));


                } else if (isReplacing) {
                    if (text.isEmpty() || sectionUpdates.containsKey(text) || text.startsWith("Turuväärtus")) {
                        isReplacing = false;
                    } else {
                        paragraph.removeRun(0); // Remove outdated content
                    }
                }
            }

            // Save the updated document
            FileOutputStream fos = new FileOutputStream(updatedFilePath);
            document.write(fos);
            fos.close();
            document.close();

            // Write change log
            writeChangeLog(logFilePath, changes);

            System.out.println("File successfully updated: " + updatedFilePath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private static String extractTextFromDocx(String filePath) throws IOException {
        FileInputStream fis = new FileInputStream(filePath);
        XWPFDocument document = new XWPFDocument(fis);
        StringBuilder text = new StringBuilder();

        for (XWPFParagraph paragraph : document.getParagraphs()) {
            text.append(paragraph.getText()).append("\n");
        }

        document.close();
        fis.close();
        return text.toString();
    }

    private static void replaceHeadingText(XWPFParagraph paragraph, String newText) {
        // Clear the paragraph's runs
        for (int i = paragraph.getRuns().size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }
        // Add new text
        XWPFRun run = paragraph.createRun();
        run.setText(newText);
    }

    private static void insertText(XWPFParagraph paragraph, String newText) {
        XWPFRun run = paragraph.createRun();
        run.setText(newText);
    }


    private static void writeChangeLog(String logFilePath, List<String> changes) throws IOException {
        if (!changes.isEmpty()) {
            Files.write(Paths.get(logFilePath), changes, java.nio.charset.StandardCharsets.UTF_8);
        }
    }
}
