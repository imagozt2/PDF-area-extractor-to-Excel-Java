package pdfareaextractortoexcel.model;


public class ExtractionSettings {
    
    private StructureMode structureMode = StructureMode.EVERY_TWO_PAGES;
    private PageScanMode pageScanMode = PageScanMode.FULL_DOCUMENT;

    private int startPage = 1;
    private int endPage = 1;


    public ExtractionSettings() {
    }

    public ExtractionSettings(StructureMode structureMode, PageScanMode pageScanMode, int startPage, int endPage) {
        this.structureMode = structureMode;
        this.pageScanMode = pageScanMode;
        this.startPage = startPage;
        this.endPage = endPage;
    }

    public StructureMode getStructureMode() {
        return structureMode;
    }

    public void setStructureMode(StructureMode structureMode) {
        this.structureMode = structureMode;
    }

    public PageScanMode getPageScanMode() {
        return pageScanMode;
    }

    public void setPageScanMode(PageScanMode pageScanMode) {
        this.pageScanMode = pageScanMode;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public int getEndPage() {
        return endPage;
    }

    public void setEndPage(int endPage) {
        this.endPage = endPage;
    }
}
