package pdfareaextractortoexcel.services;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;


public class ExcelExporter {

    public File exportToExcel(List<Map<String, String>> extractedData,
                              List<String> headers,
                              File pdfFile) throws IOException {

        String baseName = pdfFile.getName().replaceFirst("(?i)\\.pdf$", "");

        File outXlsx = new File(pdfFile.getParentFile(), baseName + ".xlsx");

        String sheetName = (baseName + ".xlsx").replaceAll("[\\\\/?*\\[\\]:]", "_");
        if (sheetName.length() > 31) {
            sheetName = sheetName.substring(0, 31);
        }

        try (Workbook workbook = new XSSFWorkbook();
             FileOutputStream fos = new FileOutputStream(outXlsx)) {

            Sheet sheet = workbook.createSheet(sheetName);

            Row headerRow = sheet.createRow(0);
            for (int c = 0; c < headers.size(); c++) {
                headerRow.createCell(c).setCellValue(headers.get(c));
            }

            for (int r = 0; r < extractedData.size(); r++) {
                Map<String, String> rowData = extractedData.get(r);
                Row row = sheet.createRow(r + 1);

                for (int c = 0; c < headers.size(); c++) {
                    String key = headers.get(c);
                    String value = rowData.getOrDefault(key, "");
                    row.createCell(c).setCellValue(value);
                }
            }

            for (int c = 0; c < headers.size(); c++) {
                sheet.autoSizeColumn(c);
            }

            workbook.write(fos);
        }

        return outXlsx;
    }
}
