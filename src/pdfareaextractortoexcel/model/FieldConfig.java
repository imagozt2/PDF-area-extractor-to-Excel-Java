package pdfareaextractortoexcel.model;

import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;


public class FieldConfig {

    private String name;

    private FieldType fieldType = FieldType.UNIQUE;

    private boolean itemField;
    
    private ItemType itemType = ItemType.ID;

    private boolean removeSpaces;

    private boolean digitsOnly;

    private boolean normalizeText;

    private TextCaseType textCaseType = TextCaseType.UPPERCASE;

    private String masterFieldName;

    private final Map<Integer, Rectangle2D.Double> areasByPage = new LinkedHashMap<>();

    
    public FieldConfig(String name) {
        this.name = name;
    }

    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FieldType getFieldType() {
        return fieldType;
    }

    public void setFieldType(FieldType fieldType) {
        this.fieldType = fieldType;
    }

    public boolean isItemField() {
        return itemField;
    }

    public void setItemField(boolean itemField) {
        this.itemField = itemField;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean isRemoveSpaces() {
        return removeSpaces;
    }

    public void setRemoveSpaces(boolean removeSpaces) {
        this.removeSpaces = removeSpaces;
    }

    public boolean isDigitsOnly() {
        return digitsOnly;
    }

    public void setDigitsOnly(boolean digitsOnly) {
        this.digitsOnly = digitsOnly;
    }

    public boolean isNormalizeText() {
        return normalizeText;
    }

    public void setNormalizeText(boolean normalizeText) {
        this.normalizeText = normalizeText;
    }

    public TextCaseType getTextCaseType() {
        return textCaseType;
    }

    public void setTextCaseType(TextCaseType textCaseType) {
        this.textCaseType = textCaseType;
    }

    public String getMasterFieldName() {
        return masterFieldName;
    }

    public void setMasterFieldName(String masterFieldName) {
        this.masterFieldName = masterFieldName;
    }


    public Map<Integer, Rectangle2D.Double> getAreasByPage() {
        return areasByPage;
    }

    public Rectangle2D.Double getAreaForPage(int pageIndex) {
        return areasByPage.get(pageIndex);
    }
    
    public void setAreaForPage(int pageIndex, Rectangle2D.Double area) {
        areasByPage.put(pageIndex, area);
    }

    public void removeAreaForPage(int pageIndex) {
        areasByPage.remove(pageIndex);
    }

    public void clearAreas() {
        areasByPage.clear();
    }
}
