package pdfareaextractortoexcel.services;

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import pdfareaextractortoexcel.model.ExtractionSettings;
import pdfareaextractortoexcel.model.FieldConfig;
import pdfareaextractortoexcel.model.FieldType;
import pdfareaextractortoexcel.model.ItemType;
import pdfareaextractortoexcel.model.StructureMode;


public class ExtractionEngine {

    private final PDFTextProcessor textProcessor;

    public ExtractionEngine(PDFTextProcessor textProcessor) {
        this.textProcessor = textProcessor;
    }

    public List<Map<String, String>> extract(PDDocument pdfDocument, List<String> fieldOrder, Map<String, FieldConfig> fieldConfigs, ExtractionSettings settings) throws IOException {

        List<Map<String, String>> extractedData = new ArrayList<>();

        int totalPages = pdfDocument.getNumberOfPages();

        StructureMode structureMode = settings.getStructureMode();
        boolean struct1 = structureMode == StructureMode.EVERY_TWO_PAGES;
        boolean struct2 = structureMode == StructureMode.EVERY_PAGE;
        boolean struct3 = structureMode == StructureMode.DIFFERENT_BOTH_SIDES;

        int startPage = settings.getStartPage();
        int finishPage = settings.getEndPage();

        int pageStep = (struct1 || struct3) ? 2 : 1;
        int globalRowCounter = 0;
        int pageCounter = 0;

        System.out.println("Inicio de la extracción de datos");
        System.out.println("Modo estructura: " + (struct1 ? "Cada dos páginas" :
                             struct2 ? "Cada página" : "Campos diferentes por ambas caras"));
        System.out.println("Rango de páginas: " + startPage + " - " + finishPage);
        System.out.println("--------------------------------------------------");

        for (int p = startPage; p <= finishPage; p += pageStep) {
            pageCounter++;

            Map<String, String> baseRow = new LinkedHashMap<>();
            for (String f : fieldOrder) {
                baseRow.put(f, "");
            }

            Map<String, List<String>> masterItemsByField = new LinkedHashMap<>();
            Map<String, List<String>> dependentItemsByField = new LinkedHashMap<>();

            for (String fieldName : fieldOrder) {
                FieldConfig cfg = fieldConfigs.get(fieldName);
                if (cfg == null) {
                    continue;
                }

                if (cfg.isItemField()) {
                    ItemType itemType = cfg.getItemType();

                    if (itemType == ItemType.DATE) {
                        baseRow.put(fieldName, LocalDate.now().toString());
                    } else if (itemType == ItemType.PAGE) {
                        baseRow.put(fieldName, String.valueOf(pageCounter));
                    }
                }
            }

            if (struct3) {
                int frontIdx = p - 1;
                if (frontIdx < totalPages) {
                    PDPage pageFront = pdfDocument.getPage(frontIdx);
                    PDFTextStripperByArea stripperFront = new PDFTextStripperByArea();
                    stripperFront.setSortByPosition(true);

                    for (String fieldName : fieldOrder) {
                        FieldConfig cfg = fieldConfigs.get(fieldName);
                        if (cfg == null) {
                            continue;
                        }
                        if (cfg.isItemField()) {
                            continue;
                        }

                        Rectangle2D.Double frac = cfg.getAreaForPage(0);
                        if (frac == null) {
                            continue;
                        }

                        Rectangle2D.Double rectPDF = textProcessor.toPdfRectangle(frac, pageFront);
                        stripperFront.addRegion(fieldName, rectPDF);
                    }

                    stripperFront.extractRegions(pageFront);

                    for (String fieldName : fieldOrder) {
                        FieldConfig cfg = fieldConfigs.get(fieldName);
                        if (cfg == null) {
                            continue;
                        }
                        if (cfg.isItemField()) {
                            continue;
                        }

                        Rectangle2D.Double frac = cfg.getAreaForPage(0);
                        if (frac == null) {
                            continue;
                        }

                        String raw = stripperFront.getTextForRegion(fieldName);
                        String cleaned = textProcessor.stripTrailingLineBreaks(raw);

                        FieldType type = cfg.getFieldType();

                        if (type == FieldType.MASTER || type == FieldType.DEPENDENT) {
                            String[] parts = cleaned.split("\\r?\\n");
                            List<String> items = new ArrayList<>();

                            for (String it : parts) {
                                String itClean = textProcessor.applyPerFieldOptions(cfg, it);
                                if (!itClean.trim().isEmpty()) {
                                    items.add(itClean);
                                }
                            }

                            if (type == FieldType.MASTER) {
                                masterItemsByField.put(fieldName, items);
                            } else {
                                String masterName = cfg.getMasterFieldName();
                                if (masterName != null && masterItemsByField.containsKey(masterName)) {
                                    List<String> masterItems = masterItemsByField.get(masterName);
                                    List<String> dependentItems = new ArrayList<>();

                                    for (int i = 0; i < masterItems.size(); i++) {
                                        if (i < items.size()) {
                                            dependentItems.add(items.get(i));
                                        } else {
                                            dependentItems.add("");
                                        }
                                    }
                                    dependentItemsByField.put(fieldName, dependentItems);
                                } else {
                                    dependentItemsByField.put(fieldName, items);
                                }
                            }
                        } else {
                            String val = textProcessor.applyPerFieldOptions(cfg, cleaned);
                            baseRow.put(fieldName, val);
                        }
                    }
                }

                int backLogical = p + 1;
                int backIdx = backLogical - 1;

                if (backLogical <= finishPage && backIdx < totalPages) {
                    PDPage pageBack = pdfDocument.getPage(backIdx);
                    PDFTextStripperByArea stripperBack = new PDFTextStripperByArea();
                    stripperBack.setSortByPosition(true);

                    for (String fieldName : fieldOrder) {
                        FieldConfig cfg = fieldConfigs.get(fieldName);
                        if (cfg == null) {
                            continue;
                        }
                        if (cfg.isItemField()) {
                            continue;
                        }

                        Rectangle2D.Double frac = cfg.getAreaForPage(1);
                        if (frac == null) {
                            continue;
                        }

                        Rectangle2D.Double rectPDF = textProcessor.toPdfRectangle(frac, pageBack);
                        stripperBack.addRegion(fieldName, rectPDF);
                    }

                    stripperBack.extractRegions(pageBack);

                    for (String fieldName : fieldOrder) {
                        FieldConfig cfg = fieldConfigs.get(fieldName);
                        if (cfg == null) {
                            continue;
                        }
                        if (cfg.isItemField()) {
                            continue;
                        }

                        Rectangle2D.Double frac = cfg.getAreaForPage(1);
                        if (frac == null) {
                            continue;
                        }

                        String raw = stripperBack.getTextForRegion(fieldName);
                        String cleaned = textProcessor.stripTrailingLineBreaks(raw);

                        FieldType type = cfg.getFieldType();

                        if (type == FieldType.MASTER || type == FieldType.DEPENDENT) {
                            String[] parts = cleaned.split("\\r?\\n");
                            List<String> items = new ArrayList<>();

                            for (String it : parts) {
                                String itClean = textProcessor.applyPerFieldOptions(cfg, it);
                                if (!itClean.trim().isEmpty()) {
                                    items.add(itClean);
                                }
                            }

                            if (type == FieldType.MASTER) {
                                List<String> existingItems = masterItemsByField.get(fieldName);
                                if (existingItems != null) {
                                    existingItems.addAll(items);
                                } else {
                                    masterItemsByField.put(fieldName, items);
                                }
                            } else {
                                String masterName = cfg.getMasterFieldName();
                                if (masterName != null && masterItemsByField.containsKey(masterName)) {
                                    List<String> masterItems = masterItemsByField.get(masterName);
                                    List<String> dependentItems =
                                            dependentItemsByField.getOrDefault(fieldName, new ArrayList<>());

                                    for (int i = 0; i < masterItems.size(); i++) {
                                        if (i < items.size()) {
                                            if (i < dependentItems.size()) {
                                                String existing = dependentItems.get(i);
                                                if (!existing.isEmpty()) {
                                                    dependentItems.set(i, existing + "\n" + items.get(i));
                                                } else {
                                                    dependentItems.set(i, items.get(i));
                                                }
                                            } else {
                                                dependentItems.add(items.get(i));
                                            }
                                        }
                                    }
                                    dependentItemsByField.put(fieldName, dependentItems);
                                } else {
                                    List<String> existingItems = dependentItemsByField.get(fieldName);
                                    if (existingItems != null) {
                                        existingItems.addAll(items);
                                    } else {
                                        dependentItemsByField.put(fieldName, items);
                                    }
                                }
                            }
                        } else {
                            String existing = baseRow.getOrDefault(fieldName, "");
                            String val = textProcessor.applyPerFieldOptions(cfg, cleaned);

                            if (!val.isEmpty()) {
                                if (existing.isEmpty()) {
                                    baseRow.put(fieldName, val);
                                } else {
                                    baseRow.put(fieldName, existing + "\n" + val);
                                }
                            }
                        }
                    }
                }

            } else {
                int pageIndex = p - 1;
                if (pageIndex >= totalPages) {
                    continue;
                }

                PDPage page = pdfDocument.getPage(pageIndex);
                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                for (String fieldName : fieldOrder) {
                    FieldConfig cfg = fieldConfigs.get(fieldName);
                    if (cfg == null) {
                        continue;
                    }
                    if (cfg.isItemField()) {
                        continue;
                    }

                    Rectangle2D.Double frac = cfg.getAreaForPage(0);
                    if (frac == null) {
                        continue;
                    }

                    Rectangle2D.Double rectPDF = textProcessor.toPdfRectangle(frac, page);
                    stripper.addRegion(fieldName, rectPDF);
                }

                stripper.extractRegions(page);

                for (String fieldName : fieldOrder) {
                    FieldConfig cfg = fieldConfigs.get(fieldName);
                    if (cfg == null) {
                        continue;
                    }
                    if (cfg.isItemField()) {
                        continue;
                    }

                    Rectangle2D.Double frac = cfg.getAreaForPage(0);
                    if (frac == null) {
                        continue;
                    }

                    String raw = stripper.getTextForRegion(fieldName);
                    String cleaned = textProcessor.stripTrailingLineBreaks(raw);

                    FieldType type = cfg.getFieldType();

                    if (type == FieldType.MASTER || type == FieldType.DEPENDENT) {
                        String[] parts = cleaned.split("\\r?\\n");
                        List<String> items = new ArrayList<>();

                        for (String it : parts) {
                            String itClean = textProcessor.applyPerFieldOptions(cfg, it);
                            if (!itClean.trim().isEmpty()) {
                                items.add(itClean);
                            }
                        }

                        if (type == FieldType.MASTER) {
                            masterItemsByField.put(fieldName, items);
                        } else {
                            String masterName = cfg.getMasterFieldName();
                            if (masterName != null && masterItemsByField.containsKey(masterName)) {
                                List<String> masterItems = masterItemsByField.get(masterName);
                                List<String> dependentItems = new ArrayList<>();

                                for (int i = 0; i < masterItems.size(); i++) {
                                    if (i < items.size()) {
                                        dependentItems.add(items.get(i));
                                    } else {
                                        dependentItems.add("");
                                    }
                                }
                                dependentItemsByField.put(fieldName, dependentItems);
                            } else {
                                dependentItemsByField.put(fieldName, items);
                            }
                        }
                    } else {
                        String val = textProcessor.applyPerFieldOptions(cfg, cleaned);
                        baseRow.put(fieldName, val);
                    }
                }
            }

            int maxRows = 0;

            for (List<String> items : masterItemsByField.values()) {
                if (items != null && items.size() > maxRows) {
                    maxRows = items.size();
                }
            }

            for (List<String> items : dependentItemsByField.values()) {
                if (items != null && items.size() > maxRows) {
                    maxRows = items.size();
                }
            }

            if (maxRows <= 0) {
                boolean anyValue = baseRow.values().stream()
                        .anyMatch(v -> v != null && !v.isEmpty());

                if (anyValue) {
                    Map<String, String> finalRow = new LinkedHashMap<>(baseRow);

                    for (String fieldName : fieldOrder) {
                        FieldConfig cfg = fieldConfigs.get(fieldName);
                        if (cfg == null) {
                            continue;
                        }

                        if (cfg.isItemField()) {
                            ItemType itemType = cfg.getItemType();
                            if (itemType == ItemType.ID) {
                                globalRowCounter++;
                                finalRow.put(fieldName, String.valueOf(globalRowCounter));
                            } else if (itemType == ItemType.DATE) {
                                finalRow.put(fieldName, LocalDate.now().toString());
                            } else if (itemType == ItemType.PAGE) {
                                finalRow.put(fieldName, String.valueOf(pageCounter));
                            }
                        }
                    }

                    extractedData.add(finalRow);

                    StringBuilder line = new StringBuilder();
                    for (int i = 0; i < fieldOrder.size(); i++) {
                        if (i > 0) {
                            line.append(", ");
                        }
                        line.append(finalRow.getOrDefault(fieldOrder.get(i), ""));
                    }
                    System.out.println("Fila única: " + line.toString());
                }

            } else {
                for (int r = 0; r < maxRows; r++) {
                    Map<String, String> newRow = new LinkedHashMap<>();

                    for (String fieldName : fieldOrder) {
                        FieldConfig cfg = fieldConfigs.get(fieldName);
                        if (cfg == null) {
                            newRow.put(fieldName, "");
                            continue;
                        }

                        if (cfg.getFieldType() == FieldType.UNIQUE) {
                            String baseValue = baseRow.get(fieldName);
                            newRow.put(fieldName, baseValue != null ? baseValue : "");
                        } else {
                            newRow.put(fieldName, "");
                        }
                    }

                    for (String fieldName : fieldOrder) {
                        FieldConfig cfg = fieldConfigs.get(fieldName);
                        if (cfg == null) {
                            continue;
                        }

                        if (cfg.isItemField()) {
                            ItemType itemType = cfg.getItemType();
                            if (itemType == ItemType.ID) {
                                globalRowCounter++;
                                newRow.put(fieldName, String.valueOf(globalRowCounter));
                            } else if (itemType == ItemType.DATE) {
                                newRow.put(fieldName, LocalDate.now().toString());
                            } else if (itemType == ItemType.PAGE) {
                                newRow.put(fieldName, String.valueOf(pageCounter));
                            }
                        }
                    }

                    for (String mf : masterItemsByField.keySet()) {
                        List<String> items = masterItemsByField.get(mf);
                        String itemVal = (items != null && r < items.size()) ? items.get(r) : "";
                        newRow.put(mf, itemVal);
                    }

                    for (String df : dependentItemsByField.keySet()) {
                        List<String> items = dependentItemsByField.get(df);
                        String itemVal = (items != null && r < items.size()) ? items.get(r) : "";
                        newRow.put(df, itemVal);
                    }

                    boolean hasData = newRow.values().stream()
                            .anyMatch(v -> v != null && !v.trim().isEmpty());

                    if (hasData) {
                        extractedData.add(newRow);

                        System.out.println("=== FILA " + r + " ===");
                        for (String fieldName : fieldOrder) {
                            FieldConfig cfg = fieldConfigs.get(fieldName);
                            FieldType type = (cfg != null) ? cfg.getFieldType() : FieldType.UNIQUE;
                            String value = newRow.getOrDefault(fieldName, "");
                            System.out.println(fieldName + " (" + type + "): '" + value + "'");
                        }
                        System.out.println("=================");
                    }
                }
            }
        }

        System.out.println("Fin del recorrido del documento. Total filas: " + extractedData.size());
        return extractedData;
    }
}
