package pdfareaextractortoexcel.app;

import java.awt.image.BufferedImage;
import java.awt.EventQueue;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JList;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import org.apache.pdfbox.pdmodel.PDDocument;
import pdfareaextractortoexcel.services.MainController;
import pdfareaextractortoexcel.view.PDFPagePanel;


public class PDFAreaExtractorApp extends JFrame {

    private PDFPagePanel pagePanel;

    private PDDocument pdfDocument;

    private String pdfPath;

    private MainController controller;
    

    public PDFAreaExtractorApp() {

        initComponents();

        setTitle("PDF Area Extractor to Excel (Java)");
        setExtendedState(JFrame.MAXIMIZED_BOTH);

        BufferedImage placeholder = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        this.pagePanel = new PDFPagePanel(placeholder);
        scrPdfViewer.setViewportView(pagePanel);

        this.pdfDocument = null;
        this.pdfPath = null;

        this.controller = new MainController(this);
        controller.initialize();
    }


    public PDFPagePanel getPagePanel() {
        return pagePanel;
    }

    public void setPagePanel(PDFPagePanel pagePanel) {
        this.pagePanel = pagePanel;
        scrPdfViewer.setViewportView(pagePanel);
    }

    public PDDocument getPdfDocument() {
        return pdfDocument;
    }

    public void setPdfDocument(PDDocument pdfDocument) {
        this.pdfDocument = pdfDocument;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
        if (txfLink != null) {
            txfLink.setText(pdfPath != null ? pdfPath : "");
        }
    }

    
    public JButton getBtnSelector() {
        return btnSelector;
    }

    public JButton getBtnValidate() {
        return btnValidate;
    }

    public JButton getBtnGenerate() {
        return btnGenerate;
    }

    public JButton getBtnAddData() {
        return btnAddData;
    }

    public JButton getBtnDeleteData() {
        return btnDeleteData;
    }

    public JButton getBtnClearListData() {
        return btnClearListData;
    }

    public JButton getBtnEditData() {
        return btnEditData;
    }

    public JButton getBtnMoveUpData() {
        return btnMoveUpData;
    }

    public JButton getBtnMoveDownData() {
        return btnMoveDownData;
    }

    public JCheckBox getChkItem() {
        return chkItem;
    }

    public JCheckBox getChkSpaces() {
        return chkSpaces;
    }

    public JCheckBox getChkSymbols() {
        return chkSymbols;
    }

    public JCheckBox getChkText() {
        return chkText;
    }

    public JComboBox<String> getCmbItem() {
        return cmbItem;
    }

    public JComboBox<String> getCmbText() {
        return cmbText;
    }

    public JComboBox<String> getCmbMaster() {
        return cmbMaster;
    }
    
    public JLabel getLblLoadFile() {
        return lblLoadFile;
    }

    public JLabel getLblStructure() {
        return lblStructure;
    }

    public JLabel getLblPagesScanner() {
        return lblPagesScanner;
    }

    public JLabel getLblPageStart() {
        return lblPageStart;
    }

    public JLabel getLblPageFinish() {
        return lblPageFinish;
    }

    public JLabel getLblData() {
        return lblData;
    }

    public JLabel getLblDataList() {
        return lblDataList;
    }

    public JLabel getLblDataFormat1() {
        return lblDataFormat1;
    }

    public JLabel getLblDataFormat2() {
        return lblDataFormat2;
    }

    public JLabel getLblDataFormat3() {
        return lblDataFormat3;
    }

    public JLabel getLblPage() {
        return lblPage;
    }

    public JLabel getLblAxisX() {
        return lblAxisX;
    }

    public JLabel getLblAxisY() {
        return lblAxisY;
    }

    public JLabel getLblMaster() {
        return lblMaster;
    }

    public JLabel getImgPage1() {
        return imgPage1;
    }

    public JLabel getImgPage2() {
        return imgPage2;
    }

    public JLabel getImgPage3() {
        return imgPage3;
    }

    public JLabel getImgPage4() {
        return imgPage4;
    }

    public JLabel getImgPage5() {
        return imgPage5;
    }
    
    public JList<String> getLstDataList() {
        return lstDataList;
    }
    
    public JPanel getPnlLeft() {
        return pnlLeft;
    }

    public JPanel getPnlRight() {
        return pnlRight;
    }

    public JPanel getPnlLoadFile() {
        return pnlLoadFile;
    }

    public JPanel getPnlStructure() {
        return pnlStructure;
    }

    public JPanel getPnlStructure1() {
        return pnlStructure1;
    }

    public JPanel getPnlStructure2() {
        return pnlStructure2;
    }

    public JPanel getPnlStructure3() {
        return pnlStructure3;
    }

    public JPanel getPnlPagesScanner() {
        return pnlPagesScanner;
    }

    public JPanel getPnlData() {
        return pnlData;
    }

    public JPanel getPnlDataList() {
        return pnlDataList;
    }

    public JPanel getPnlDataFormat1() {
        return pnlDataFormat1;
    }

    public JPanel getPnlDataFormat2() {
        return pnlDataFormat2;
    }

    public JPanel getPnlDataFormat3() {
        return pnlDataFormat3;
    }

    public JPanel getPnlValidate() {
        return pnlValidate;
    }

    public JRadioButton getRbdFieldType1() {
        return rbdFieldType1;
    }

    public JRadioButton getRbdFieldType2() {
        return rbdFieldType2;
    }

    public JRadioButton getRbdFieldType3() {
        return rbdFieldType3;
    }

    public JRadioButton getRdbStructure1() {
        return rdbStructure1;
    }

    public JRadioButton getRdbStructure2() {
        return rdbStructure2;
    }

    public JRadioButton getRdbStructure3() {
        return rdbStructure3;
    }

    public JRadioButton getRbdPagesScanner1() {
        return rbdPagesScanner1;
    }

    public JRadioButton getRbdPagesScanner2() {
        return rbdPagesScanner2;
    }
    
    public JScrollPane getScrPdfViewer() {
        return scrPdfViewer;
    }

    public JScrollPane getJScrollPane1() {
        return jScrollPane1;
    }
    
    public JSlider getSldRightPanel() {
        return sldRightPanel;
    }

    public JSplitPane getSplMain() {
        return splMain;
    }

    public JSplitPane getSplRightPanel() {
        return splRightPanel;
    }
    
    public JTextField getTxfLink() {
        return txfLink;
    }

    public JTextField getTxfPage() {
        return txfPage;
    }

    public JTextField getTxfAxisX() {
        return txfAxisX;
    }

    public JTextField getTxfAxisY() {
        return txfAxisY;
    }

    public JTextField getTxfStart() {
        return txfStart;
    }

    public JTextField getTxfFinish() {
        return txfFinish;
    }

    public JToggleButton getTglPag1() {
        return tglPag1;
    }

    public JToggleButton getTglPag2() {
        return tglPag2;
    }
    

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        splMain = new javax.swing.JSplitPane();
        pnlLeft = new javax.swing.JPanel();
        pnlStructure = new javax.swing.JPanel();
        pnlStructure1 = new javax.swing.JPanel();
        rdbStructure1 = new javax.swing.JRadioButton();
        imgPage1 = new javax.swing.JLabel();
        imgPage2 = new javax.swing.JLabel();
        pnlStructure2 = new javax.swing.JPanel();
        rdbStructure2 = new javax.swing.JRadioButton();
        imgPage3 = new javax.swing.JLabel();
        pnlStructure3 = new javax.swing.JPanel();
        rdbStructure3 = new javax.swing.JRadioButton();
        imgPage4 = new javax.swing.JLabel();
        imgPage5 = new javax.swing.JLabel();
        lblStructure = new javax.swing.JLabel();
        lblLoadFile = new javax.swing.JLabel();
        pnlLoadFile = new javax.swing.JPanel();
        btnSelector = new javax.swing.JButton();
        txfLink = new javax.swing.JTextField();
        lblData = new javax.swing.JLabel();
        pnlData = new javax.swing.JPanel();
        pnlDataList = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstDataList = new javax.swing.JList<>();
        lblDataList = new javax.swing.JLabel();
        btnAddData = new javax.swing.JButton();
        btnDeleteData = new javax.swing.JButton();
        btnClearListData = new javax.swing.JButton();
        btnEditData = new javax.swing.JButton();
        btnMoveUpData = new javax.swing.JButton();
        btnMoveDownData = new javax.swing.JButton();
        pnlDataFormat1 = new javax.swing.JPanel();
        lblDataFormat1 = new javax.swing.JLabel();
        rbdFieldType1 = new javax.swing.JRadioButton();
        rbdFieldType2 = new javax.swing.JRadioButton();
        rbdFieldType3 = new javax.swing.JRadioButton();
        pnlDataFormat2 = new javax.swing.JPanel();
        lblDataFormat2 = new javax.swing.JLabel();
        lblPage = new javax.swing.JLabel();
        lblAxisX = new javax.swing.JLabel();
        lblAxisY = new javax.swing.JLabel();
        txfPage = new javax.swing.JTextField();
        txfAxisX = new javax.swing.JTextField();
        txfAxisY = new javax.swing.JTextField();
        pnlDataFormat3 = new javax.swing.JPanel();
        lblDataFormat3 = new javax.swing.JLabel();
        cmbMaster = new javax.swing.JComboBox<>();
        chkItem = new javax.swing.JCheckBox();
        lblMaster = new javax.swing.JLabel();
        cmbItem = new javax.swing.JComboBox<>();
        chkSpaces = new javax.swing.JCheckBox();
        chkSymbols = new javax.swing.JCheckBox();
        chkText = new javax.swing.JCheckBox();
        cmbText = new javax.swing.JComboBox<>();
        pnlValidate = new javax.swing.JPanel();
        btnGenerate = new javax.swing.JButton();
        btnValidate = new javax.swing.JButton();
        pnlPagesScanner = new javax.swing.JPanel();
        rbdPagesScanner1 = new javax.swing.JRadioButton();
        rbdPagesScanner2 = new javax.swing.JRadioButton();
        lblPageStart = new javax.swing.JLabel();
        lblPageFinish = new javax.swing.JLabel();
        txfStart = new javax.swing.JTextField();
        txfFinish = new javax.swing.JTextField();
        lblPagesScanner = new javax.swing.JLabel();
        pnlRight = new javax.swing.JPanel();
        scrPdfViewer = new javax.swing.JScrollPane();
        splRightPanel = new javax.swing.JSplitPane();
        tglPag1 = new javax.swing.JToggleButton();
        tglPag2 = new javax.swing.JToggleButton();
        sldRightPanel = new javax.swing.JSlider();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        splMain.setDividerLocation(725);

        pnlLeft.setPreferredSize(new java.awt.Dimension(400, 500));

        pnlStructure.setBackground(new java.awt.Color(200, 200, 200));
        pnlStructure.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pnlStructure1.setBackground(new java.awt.Color(200, 200, 200));
        pnlStructure1.setPreferredSize(new java.awt.Dimension(130, 130));

        rdbStructure1.setText("<html>Todos los datos<br>cada dos páginas</html>");
        rdbStructure1.setPreferredSize(new java.awt.Dimension(130, 37));

        imgPage1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphicresources/todoPorCara.png"))); // NOI18N
        imgPage1.setText("jLabel1");

        imgPage2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphicresources/blanco.png"))); // NOI18N
        imgPage2.setText("jLabel2");

        javax.swing.GroupLayout pnlStructure1Layout = new javax.swing.GroupLayout(pnlStructure1);
        pnlStructure1.setLayout(pnlStructure1Layout);
        pnlStructure1Layout.setHorizontalGroup(
            pnlStructure1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStructure1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgPage1, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgPage2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(rdbStructure1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlStructure1Layout.setVerticalGroup(
            pnlStructure1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStructure1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlStructure1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imgPage1)
                    .addComponent(imgPage2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdbStructure1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlStructure2.setBackground(new java.awt.Color(200, 200, 200));
        pnlStructure2.setPreferredSize(new java.awt.Dimension(130, 130));

        rdbStructure2.setText("<html>Todos los datos<br>en cada página</html>");
        rdbStructure2.setPreferredSize(new java.awt.Dimension(130, 37));

        imgPage3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphicresources/todoPorCara.png"))); // NOI18N
        imgPage3.setText("jLabel3");

        javax.swing.GroupLayout pnlStructure2Layout = new javax.swing.GroupLayout(pnlStructure2);
        pnlStructure2.setLayout(pnlStructure2Layout);
        pnlStructure2Layout.setHorizontalGroup(
            pnlStructure2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStructure2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(imgPage3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(rdbStructure2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlStructure2Layout.setVerticalGroup(
            pnlStructure2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStructure2Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(imgPage3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdbStructure2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
        );

        pnlStructure3.setBackground(new java.awt.Color(200, 200, 200));

        rdbStructure3.setText("<html>Datos distintos por<br>las dos caras</html>");
        rdbStructure3.setPreferredSize(new java.awt.Dimension(130, 37));

        imgPage4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphicresources/datos.png"))); // NOI18N
        imgPage4.setText("jLabel4");

        imgPage5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/graphicresources/tabla.png"))); // NOI18N
        imgPage5.setText("jLabel5");

        javax.swing.GroupLayout pnlStructure3Layout = new javax.swing.GroupLayout(pnlStructure3);
        pnlStructure3.setLayout(pnlStructure3Layout);
        pnlStructure3Layout.setHorizontalGroup(
            pnlStructure3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStructure3Layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addComponent(imgPage4, javax.swing.GroupLayout.PREFERRED_SIZE, 52, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
                .addComponent(imgPage5, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(11, Short.MAX_VALUE))
            .addComponent(rdbStructure3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        pnlStructure3Layout.setVerticalGroup(
            pnlStructure3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlStructure3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(pnlStructure3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(imgPage4)
                    .addComponent(imgPage5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(rdbStructure3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlStructureLayout = new javax.swing.GroupLayout(pnlStructure);
        pnlStructure.setLayout(pnlStructureLayout);
        pnlStructureLayout.setHorizontalGroup(
            pnlStructureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStructureLayout.createSequentialGroup()
                .addContainerGap(38, Short.MAX_VALUE)
                .addComponent(pnlStructure1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(pnlStructure2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlStructure3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(37, Short.MAX_VALUE))
        );
        pnlStructureLayout.setVerticalGroup(
            pnlStructureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlStructureLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlStructureLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlStructure2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addComponent(pnlStructure1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 151, Short.MAX_VALUE)
                    .addComponent(pnlStructure3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        lblStructure.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblStructure.setText("Estructura del documento");

        lblLoadFile.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblLoadFile.setText("Cargar archivo");

        pnlLoadFile.setBackground(new java.awt.Color(200, 200, 200));
        pnlLoadFile.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnSelector.setText("Buscar archivo");

        javax.swing.GroupLayout pnlLoadFileLayout = new javax.swing.GroupLayout(pnlLoadFile);
        pnlLoadFile.setLayout(pnlLoadFileLayout);
        pnlLoadFileLayout.setHorizontalGroup(
            pnlLoadFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoadFileLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnSelector)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txfLink)
                .addContainerGap())
        );
        pnlLoadFileLayout.setVerticalGroup(
            pnlLoadFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoadFileLayout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(pnlLoadFileLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSelector)
                    .addComponent(txfLink, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        lblData.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblData.setText("Campos y recopilación de datos");

        pnlData.setBackground(new java.awt.Color(200, 200, 200));
        pnlData.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        pnlDataList.setBackground(new java.awt.Color(180, 180, 180));
        pnlDataList.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jScrollPane1.setViewportView(lstDataList);

        lblDataList.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDataList.setText("Lista de campos");

        btnAddData.setText("Añadir campo");

        btnDeleteData.setText("Eliminar campo");
        btnDeleteData.setPreferredSize(new java.awt.Dimension(105, 23));

        btnClearListData.setText("Borrar lista");
        btnClearListData.setPreferredSize(new java.awt.Dimension(105, 23));

        btnEditData.setText("Editar campo");
        btnEditData.setPreferredSize(new java.awt.Dimension(105, 23));

        btnMoveUpData.setText("Mover arriba");
        btnMoveUpData.setPreferredSize(new java.awt.Dimension(105, 23));

        btnMoveDownData.setText("Mover abajo");
        btnMoveDownData.setPreferredSize(new java.awt.Dimension(105, 23));

        javax.swing.GroupLayout pnlDataListLayout = new javax.swing.GroupLayout(pnlDataList);
        pnlDataList.setLayout(pnlDataListLayout);
        pnlDataListLayout.setHorizontalGroup(
            pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataListLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addComponent(lblDataList, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDataListLayout.createSequentialGroup()
                        .addGroup(pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnAddData, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(btnDeleteData, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE)
                            .addComponent(btnClearListData, javax.swing.GroupLayout.DEFAULT_SIZE, 125, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMoveDownData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(btnEditData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE)
                            .addComponent(btnMoveUpData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 124, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlDataListLayout.setVerticalGroup(
            pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 290, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddData)
                    .addComponent(btnEditData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoveUpData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClearListData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMoveDownData, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(6, 6, 6))
        );

        pnlDataListLayout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnAddData, btnClearListData, btnDeleteData, btnEditData, btnMoveDownData, btnMoveUpData});

        pnlDataFormat1.setBackground(new java.awt.Color(180, 180, 180));
        pnlDataFormat1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblDataFormat1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDataFormat1.setText("Tipo de campo");

        rbdFieldType1.setText("Valor único");

        rbdFieldType2.setText("Valor múltiple maestro");

        rbdFieldType3.setText("Valor múltiple dependiente");

        javax.swing.GroupLayout pnlDataFormat1Layout = new javax.swing.GroupLayout(pnlDataFormat1);
        pnlDataFormat1.setLayout(pnlDataFormat1Layout);
        pnlDataFormat1Layout.setHorizontalGroup(
            pnlDataFormat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataFormat1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataFormat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rbdFieldType1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbdFieldType2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbdFieldType3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblDataFormat1, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlDataFormat1Layout.setVerticalGroup(
            pnlDataFormat1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataFormat1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataFormat1)
                .addGap(12, 12, 12)
                .addComponent(rbdFieldType1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbdFieldType2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(rbdFieldType3)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDataFormat2.setBackground(new java.awt.Color(180, 180, 180));
        pnlDataFormat2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblDataFormat2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDataFormat2.setText("Ubicación y coordenadas");

        lblPage.setText("-Cara:");

        lblAxisX.setText("-Eje X:");

        lblAxisY.setText("-Eje Y:");

        javax.swing.GroupLayout pnlDataFormat2Layout = new javax.swing.GroupLayout(pnlDataFormat2);
        pnlDataFormat2.setLayout(pnlDataFormat2Layout);
        pnlDataFormat2Layout.setHorizontalGroup(
            pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataFormat2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDataFormat2Layout.createSequentialGroup()
                        .addGroup(pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAxisX, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblPage, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblAxisY, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(txfAxisX, javax.swing.GroupLayout.DEFAULT_SIZE, 83, Short.MAX_VALUE)
                            .addComponent(txfPage)
                            .addComponent(txfAxisY))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlDataFormat2Layout.createSequentialGroup()
                        .addComponent(lblDataFormat2, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(7, 7, 7))))
        );
        pnlDataFormat2Layout.setVerticalGroup(
            pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataFormat2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataFormat2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfPage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblPage))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfAxisX, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAxisX))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataFormat2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txfAxisY, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblAxisY))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlDataFormat3.setBackground(new java.awt.Color(180, 180, 180));
        pnlDataFormat3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblDataFormat3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblDataFormat3.setText("Otras opciones");

        cmbMaster.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        chkItem.setText("Definir campo sin área:");

        lblMaster.setText("-Respectivo campo maestro: ");

        cmbItem.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        chkSpaces.setText("Eliminar espacios entre el texto");

        chkSymbols.setText("Eliminar caracteres no numéricos");

        chkText.setText("Normalizar texto:");

        cmbText.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        javax.swing.GroupLayout pnlDataFormat3Layout = new javax.swing.GroupLayout(pnlDataFormat3);
        pnlDataFormat3.setLayout(pnlDataFormat3Layout);
        pnlDataFormat3Layout.setHorizontalGroup(
            pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataFormat3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblDataFormat3, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlDataFormat3Layout.createSequentialGroup()
                        .addComponent(lblMaster)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbMaster, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlDataFormat3Layout.createSequentialGroup()
                        .addGroup(pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(chkItem)
                            .addComponent(chkText))
                        .addGap(21, 21, 21)
                        .addGroup(pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cmbText, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cmbItem, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(chkSpaces)
                    .addComponent(chkSymbols))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        pnlDataFormat3Layout.setVerticalGroup(
            pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataFormat3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataFormat3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblMaster)
                    .addComponent(cmbMaster, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addGroup(pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkItem)
                    .addComponent(cmbItem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(chkText)
                    .addComponent(cmbText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkSpaces)
                .addGap(18, 18, 18)
                .addComponent(chkSymbols)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlValidate.setBackground(new java.awt.Color(180, 180, 180));
        pnlValidate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGenerate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGenerate.setText("Generar Excel");

        btnValidate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnValidate.setText("Validar datos");

        javax.swing.GroupLayout pnlValidateLayout = new javax.swing.GroupLayout(pnlValidate);
        pnlValidate.setLayout(pnlValidateLayout);
        pnlValidateLayout.setHorizontalGroup(
            pnlValidateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlValidateLayout.createSequentialGroup()
                .addContainerGap(16, Short.MAX_VALUE)
                .addComponent(btnValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 28, Short.MAX_VALUE)
                .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(16, Short.MAX_VALUE))
        );
        pnlValidateLayout.setVerticalGroup(
            pnlValidateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlValidateLayout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addGroup(pnlValidateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnValidate, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
                    .addComponent(btnGenerate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlDataLayout = new javax.swing.GroupLayout(pnlData);
        pnlData.setLayout(pnlDataLayout);
        pnlDataLayout.setHorizontalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(pnlDataList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addComponent(pnlDataFormat1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlDataFormat2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(pnlDataFormat3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlValidate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlDataLayout.setVerticalGroup(
            pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlDataList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlDataLayout.createSequentialGroup()
                        .addGroup(pnlDataLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(pnlDataFormat2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlDataFormat1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlDataFormat3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlValidate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pnlPagesScanner.setBackground(new java.awt.Color(200, 200, 200));
        pnlPagesScanner.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        rbdPagesScanner1.setText("Documento completo");

        rbdPagesScanner2.setText("Margen personalizado");

        lblPageStart.setText("-Iniciar en página:");

        lblPageFinish.setText("-Finalizar en página:");

        javax.swing.GroupLayout pnlPagesScannerLayout = new javax.swing.GroupLayout(pnlPagesScanner);
        pnlPagesScanner.setLayout(pnlPagesScannerLayout);
        pnlPagesScannerLayout.setHorizontalGroup(
            pnlPagesScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPagesScannerLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlPagesScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(rbdPagesScanner2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rbdPagesScanner1)
                    .addGroup(pnlPagesScannerLayout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(pnlPagesScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(pnlPagesScannerLayout.createSequentialGroup()
                                .addComponent(lblPageFinish)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txfFinish, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlPagesScannerLayout.createSequentialGroup()
                                .addComponent(lblPageStart)
                                .addGap(18, 18, 18)
                                .addComponent(txfStart)))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlPagesScannerLayout.setVerticalGroup(
            pnlPagesScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlPagesScannerLayout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(rbdPagesScanner1)
                .addGap(18, 18, 18)
                .addComponent(rbdPagesScanner2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPagesScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPageStart)
                    .addComponent(txfStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlPagesScannerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblPageFinish)
                    .addComponent(txfFinish, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblPagesScanner.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblPagesScanner.setText("Páginas a escanear");

        javax.swing.GroupLayout pnlLeftLayout = new javax.swing.GroupLayout(pnlLeft);
        pnlLeft.setLayout(pnlLeftLayout);
        pnlLeftLayout.setHorizontalGroup(
            pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlLeftLayout.createSequentialGroup()
                        .addComponent(lblData, javax.swing.GroupLayout.PREFERRED_SIZE, 280, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlLeftLayout.createSequentialGroup()
                        .addGroup(pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblStructure, javax.swing.GroupLayout.PREFERRED_SIZE, 289, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(pnlStructure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPagesScanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlPagesScanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(pnlLoadFile, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(pnlLeftLayout.createSequentialGroup()
                        .addComponent(lblLoadFile, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))))
        );
        pnlLeftLayout.setVerticalGroup(
            pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLeftLayout.createSequentialGroup()
                .addComponent(lblLoadFile)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlLoadFile, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblStructure)
                    .addComponent(lblPagesScanner))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlLeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlPagesScanner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlStructure, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addComponent(lblData)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pnlData, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        splMain.setLeftComponent(pnlLeft);

        scrPdfViewer.setBackground(new java.awt.Color(200, 200, 200));

        splRightPanel.setDividerLocation(125);

        tglPag1.setText("Cara delantera");
        splRightPanel.setLeftComponent(tglPag1);

        tglPag2.setText("Cara trasera");
        splRightPanel.setRightComponent(tglPag2);

        javax.swing.GroupLayout pnlRightLayout = new javax.swing.GroupLayout(pnlRight);
        pnlRight.setLayout(pnlRightLayout);
        pnlRightLayout.setHorizontalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRightLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(scrPdfViewer)
                    .addGroup(pnlRightLayout.createSequentialGroup()
                        .addComponent(splRightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(sldRightPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 357, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(9, 9, 9)))
                .addContainerGap())
        );
        pnlRightLayout.setVerticalGroup(
            pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlRightLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrPdfViewer, javax.swing.GroupLayout.DEFAULT_SIZE, 735, Short.MAX_VALUE)
                .addGap(8, 8, 8)
                .addGroup(pnlRightLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(splRightPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(sldRightPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        splMain.setRightComponent(pnlRight);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splMain, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1356, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splMain)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    public static void main(String args[]) {
        
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info :
                    javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PDFAreaExtractorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PDFAreaExtractorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PDFAreaExtractorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PDFAreaExtractorApp.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        EventQueue.invokeLater(() -> new PDFAreaExtractorApp().setVisible(true));
    }
 
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddData;
    private javax.swing.JButton btnClearListData;
    private javax.swing.JButton btnDeleteData;
    private javax.swing.JButton btnEditData;
    private javax.swing.JButton btnGenerate;
    private javax.swing.JButton btnMoveDownData;
    private javax.swing.JButton btnMoveUpData;
    private javax.swing.JButton btnSelector;
    private javax.swing.JButton btnValidate;
    private javax.swing.JCheckBox chkItem;
    private javax.swing.JCheckBox chkSpaces;
    private javax.swing.JCheckBox chkSymbols;
    private javax.swing.JCheckBox chkText;
    private javax.swing.JComboBox<String> cmbItem;
    private javax.swing.JComboBox<String> cmbMaster;
    private javax.swing.JComboBox<String> cmbText;
    private javax.swing.JLabel imgPage1;
    private javax.swing.JLabel imgPage2;
    private javax.swing.JLabel imgPage3;
    private javax.swing.JLabel imgPage4;
    private javax.swing.JLabel imgPage5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAxisX;
    private javax.swing.JLabel lblAxisY;
    private javax.swing.JLabel lblData;
    private javax.swing.JLabel lblDataFormat1;
    private javax.swing.JLabel lblDataFormat2;
    private javax.swing.JLabel lblDataFormat3;
    private javax.swing.JLabel lblDataList;
    private javax.swing.JLabel lblLoadFile;
    private javax.swing.JLabel lblMaster;
    private javax.swing.JLabel lblPage;
    private javax.swing.JLabel lblPageFinish;
    private javax.swing.JLabel lblPageStart;
    private javax.swing.JLabel lblPagesScanner;
    private javax.swing.JLabel lblStructure;
    private javax.swing.JList<String> lstDataList;
    private javax.swing.JPanel pnlData;
    private javax.swing.JPanel pnlDataFormat1;
    private javax.swing.JPanel pnlDataFormat2;
    private javax.swing.JPanel pnlDataFormat3;
    private javax.swing.JPanel pnlDataList;
    private javax.swing.JPanel pnlLeft;
    private javax.swing.JPanel pnlLoadFile;
    private javax.swing.JPanel pnlPagesScanner;
    private javax.swing.JPanel pnlRight;
    private javax.swing.JPanel pnlStructure;
    private javax.swing.JPanel pnlStructure1;
    private javax.swing.JPanel pnlStructure2;
    private javax.swing.JPanel pnlStructure3;
    private javax.swing.JPanel pnlValidate;
    private javax.swing.JRadioButton rbdFieldType1;
    private javax.swing.JRadioButton rbdFieldType2;
    private javax.swing.JRadioButton rbdFieldType3;
    private javax.swing.JRadioButton rbdPagesScanner1;
    private javax.swing.JRadioButton rbdPagesScanner2;
    private javax.swing.JRadioButton rdbStructure1;
    private javax.swing.JRadioButton rdbStructure2;
    private javax.swing.JRadioButton rdbStructure3;
    private javax.swing.JScrollPane scrPdfViewer;
    private javax.swing.JSlider sldRightPanel;
    private javax.swing.JSplitPane splMain;
    private javax.swing.JSplitPane splRightPanel;
    private javax.swing.JToggleButton tglPag1;
    private javax.swing.JToggleButton tglPag2;
    private javax.swing.JTextField txfAxisX;
    private javax.swing.JTextField txfAxisY;
    private javax.swing.JTextField txfFinish;
    private javax.swing.JTextField txfLink;
    private javax.swing.JTextField txfPage;
    private javax.swing.JTextField txfStart;
    // End of variables declaration//GEN-END:variables
}
