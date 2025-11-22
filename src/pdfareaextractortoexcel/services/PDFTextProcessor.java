package pdfareaextractortoexcel.services;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import pdfareaextractortoexcel.model.FieldConfig;
import pdfareaextractortoexcel.model.TextCaseType;


public class PDFTextProcessor {

    public Rectangle2D.Double toPdfRectangle(Rectangle2D.Double relativeArea, PDPage page) {
        PDRectangle mediaBox = page.getMediaBox();
        double pageWidth = mediaBox.getWidth();
        double pageHeight = mediaBox.getHeight();

        double x = relativeArea.x * pageWidth;
        double y = relativeArea.y * pageHeight;
        double w = relativeArea.width * pageWidth;
        double h = relativeArea.height * pageHeight;

        return new Rectangle2D.Double(x, y, w, h);
    }

    public String extractRawText(PDDocument document,
                                 int pageIndex,
                                 Rectangle2D.Double relativeArea) throws IOException {

        PDPage page = document.getPage(pageIndex);
        Rectangle2D.Double pdfRect = toPdfRectangle(relativeArea, page);

        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);
        stripper.addRegion("area", pdfRect);
        stripper.extractRegions(page);

        return stripper.getTextForRegion("area");
    }

    public Map<String, String> extractRawTextForFields(PDDocument document,
                                                       int pageIndex,
                                                       Map<String, Rectangle2D.Double> relativeAreasByField)
            throws IOException {

        Map<String, String> result = new HashMap<>();
        if (relativeAreasByField == null || relativeAreasByField.isEmpty()) {
            return result;
        }

        PDPage page = document.getPage(pageIndex);
        PDFTextStripperByArea stripper = new PDFTextStripperByArea();
        stripper.setSortByPosition(true);

        for (Map.Entry<String, Rectangle2D.Double> entry : relativeAreasByField.entrySet()) {
            String fieldName = entry.getKey();
            Rectangle2D.Double relative = entry.getValue();
            Rectangle2D.Double pdfRect = toPdfRectangle(relative, page);
            stripper.addRegion(fieldName, pdfRect);
        }

        stripper.extractRegions(page);

        for (String fieldName : relativeAreasByField.keySet()) {
            String text = stripper.getTextForRegion(fieldName);
            result.put(fieldName, text);
        }

        return result;
    }

    public String stripTrailingLineBreaks(String text) {
        if (text == null) return "";
        int end = text.length();
        boolean sawLineBreak = false;

        while (end > 0) {
            char c = text.charAt(end - 1);
            if (c == '\n' || c == '\r') {
                sawLineBreak = true;
                end--;
                continue;
            }
            if (sawLineBreak && (c == ' ' || c == '\t' || c == '\u00A0')) {
                end--;
                continue;
            }
            if (!sawLineBreak && (c == '\u200B' || c == '\uFEFF')) {
                end--;
                continue;
            }
            break;
        }
        return text.substring(0, end);
    }

    public String applyPerFieldOptions(FieldConfig fieldConfig, String text) {
        if (text == null) return "";

        String out = text.trim();

        if (fieldConfig.isRemoveSpaces()) {
            out = out.replace(" ", "");
        }

        if (fieldConfig.isDigitsOnly()) {
            out = out.replaceAll("[^0-9]", "");
        }

        if (fieldConfig.isNormalizeText()) {
            out = applyTextNormalization(out, fieldConfig.getTextCaseType());
        }

        return out;
    }

    public String applyTextNormalization(String text, TextCaseType type) {
        if (text == null || text.isEmpty() || type == null) return text;

        switch (type) {
            case UPPERCASE:
                return text.toUpperCase();

            case LOWERCASE:
                return text.toLowerCase();

            case TITLE:
                return toTitleCase(text);

            default:
                return text;
        }
    }

    public String toTitleCase(String text) {
        if (text == null || text.isEmpty()) return text;

        if (text.contains("\n")) {
            String[] lines = text.split("\n");
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < lines.length; i++) {
                if (i > 0) result.append("\n");
                result.append(toTitleCaseSingleLine(lines[i]));
            }
            return result.toString();
        }

        return toTitleCaseSingleLine(text);
    }

    private String toTitleCaseSingleLine(String line) {
        if (line == null || line.isEmpty()) return line;
        String lower = line.toLowerCase();
        if (lower.length() == 1) {
            return lower.toUpperCase();
        }
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }
}
