/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pdfareaextractortoexcel;

import java.awt.Dimension;
import java.awt.List;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripperByArea;

/**
 *
 * @author Ivan
 */
public class pdfareaextractortoexcel extends javax.swing.JFrame {

    private javax.swing.ButtonGroup btnGroupDataFormat;
    private javax.swing.ButtonGroup btnGroupScanner;
    private javax.swing.ButtonGroup btnGroupStructure;
    private javax.swing.ButtonGroup btnGroupPages;
    private java.util.List<Map<String,String>> extractedData = new ArrayList<>();
    private Map<String, Map<Integer, Rectangle2D.Double>> fieldAreasByPage = new HashMap<>();
    private java.util.LinkedHashMap<String, String> fieldTypeMap = new java.util.LinkedHashMap<>();
    private BufferedImage currentImage;
    private PDDocument pdfDocument;
    private PDFPagePanel pagePanel;
    private String PDFLink;
    /**
     * Creates new form pdfareaextractortoexcel
     */
    public pdfareaextractortoexcel() {
        initComponents();

        // Configuración del frame principal
        setTitle("PDF Area Extractor to Excel (Java)");
        setExtendedState(pdfareaextractortoexcel.MAXIMIZED_BOTH);
        splMain.setResizeWeight(0.5);
        splMain.setEnabled(false);

        SwingUtilities.invokeLater(() -> {
            splMain.setDividerLocation(0.5);
        });
        splRightPanel.setEnabled(false);

        // Imagen en blanco temporal
        BufferedImage placeholderImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);

        // Creación del PDFPagePanel con la imagen vacía
        pagePanel = new PDFPagePanel(placeholderImage);
        pagePanel.setZoom(1.0);
        pagePanel.setCanSelect(false);
        scrPdfViewer.setViewportView(pagePanel);

        // Configuración de btnGroupStructure
        btnGroupStructure = new javax.swing.ButtonGroup();
        btnGroupStructure.add(rdbStructure1);
        btnGroupStructure.add(rdbStructure2);
        btnGroupStructure.add(rdbStructure3);

        // Configuración de btnGroupDataFormat
        btnGroupDataFormat = new javax.swing.ButtonGroup();
        btnGroupDataFormat.add(rbdFieldType1);
        btnGroupDataFormat.add(rbdFieldType2);
        btnGroupDataFormat.add(rbdFieldType3);

        // Configuración de btnGroupScanner
        btnGroupScanner = new javax.swing.ButtonGroup();
        btnGroupScanner.add(rbdPagesScanner1);
        btnGroupScanner.add(rbdPagesScanner2);
        
        // Configuración de btnGroupPages
        btnGroupPages = new javax.swing.ButtonGroup();
        btnGroupPages.add(tglPag1);
        btnGroupPages.add(tglPag2);

        // Botones Estructura
        rdbStructure1.setEnabled(false);
        rdbStructure2.setEnabled(false);
        rdbStructure3.setEnabled(false);

        // Botones Páginas a escanear
        rbdPagesScanner1.setEnabled(false);
        rbdPagesScanner2.setEnabled(false);

        // Lista de campos y botones asociados
        lstDataList.setEnabled(false);
        btnAddData.setEnabled(false);
        btnDeleteData.setEnabled(false);
        btnClearListData.setEnabled(false);
        btnEditData.setEnabled(false);
        btnMoveUpData.setEnabled(false);
        btnMoveDownData.setEnabled(false);

        // Botones Tipo de campo
        rbdFieldType1.setEnabled(false);
        rbdFieldType2.setEnabled(false);
        rbdFieldType3.setEnabled(false);

        // Botones/Slider Navegación/zoom PDF
        tglPag1.setEnabled(false);
        tglPag2.setEnabled(false);
        sldRightPanel.setEnabled(false);

        // Campos de texto (en blanco)
        txfStart.setText("");
        txfFinish.setText("");
        txfPage.setText("");
        txfAxisX.setText("");
        txfAxisY.setText("");

        // Campos de texto (no rellenables)
        txfLink.setEditable(false);
        txfStart.setEditable(false);
        txfFinish.setEditable(false);
        txfPage.setEditable(false);
        txfAxisX.setEditable(false);
        txfAxisY.setEditable(false);
        txfLink.setFocusable(false);
        txfStart.setFocusable(false);
        txfFinish.setFocusable(false);
        txfPage.setFocusable(false);
        txfAxisX.setFocusable(false);
        txfAxisY.setFocusable(false);

        // Campos de texto (deshabilitados)
        txfStart.setEnabled(false);
        txfFinish.setEnabled(false);
        txfPage.setEnabled(false);
        txfAxisX.setEnabled(false);
        txfAxisY.setEnabled(false);
        
        // Etiquetas
        lblStructure.setEnabled(false);
        lblPagesScanner.setEnabled(false);
        lblPageStart.setEnabled(false);
        lblPageFinish.setEnabled(false);
        lblData.setEnabled(false);
        lblDataList.setEnabled(false);
        lblDataFormat1.setEnabled(false);
        lblDataFormat2.setEnabled(false);
        lblPage.setEnabled(false);
        lblAxisX.setEnabled(false);
        lblAxisY.setEnabled(false);
        
        // Botones de validación/Generación
        btnValidate.setEnabled(false);
        btnGenerate.setEnabled(false);
        
        // Imágenes
        imgPage1.setEnabled(false);
        imgPage2.setEnabled(false);
        imgPage3.setEnabled(false);
        imgPage4.setEnabled(false);
        imgPage5.setEnabled(false);
        
        // Listeners para condiciones dinámicas (Estructura documento)
        rdbStructure1.addActionListener(e -> applyStructureMode());
        rdbStructure2.addActionListener(e -> applyStructureMode());
        rdbStructure3.addActionListener(e -> applyStructureMode());

        // Listeners para condiciones dinámicas (Páginas escanear)
        rbdPagesScanner1.addActionListener(e -> applyPageScannerMode());
        rbdPagesScanner2.addActionListener(e -> applyPageScannerMode());
        
        // Listener de la lista de campos
        lstDataList.setModel(new javax.swing.DefaultListModel<>());

        // Listener de habilitación de botones y selección de tipo de campo
        lstDataList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            javax.swing.ListModel<String> lm = lstDataList.getModel();
            if (!(lm instanceof javax.swing.DefaultListModel)) return;
            javax.swing.DefaultListModel<String> model = (javax.swing.DefaultListModel<String>) lm;

            int size = model.getSize();
            int index = lstDataList.getSelectedIndex();
            boolean hasSelection = (index != -1);

            // Borrar lista
            btnClearListData.setEnabled(size > 0);

            // Borrar item y Editar item
            btnDeleteData.setEnabled(hasSelection);
            btnEditData.setEnabled(hasSelection);

            // Mover arriba
            btnMoveUpData.setEnabled(hasSelection && index > 0);

            // Mover abajo
            btnMoveDownData.setEnabled(hasSelection && index < size - 1);

            // Selección del tipo de campo (UNIQUE, MASTER, DEPENDENT)
            if (hasSelection) {
                
                rbdFieldType1.setEnabled(true);
                rbdFieldType2.setEnabled(true);
                rbdFieldType3.setEnabled(true);
                lblPage.setEnabled(true);
                lblAxisX.setEnabled(true);
                lblAxisY.setEnabled(true);
                txfPage.setEnabled(true);
                txfAxisX.setEnabled(true);
                txfAxisY.setEnabled(true);
                btnValidate.setEnabled(true);
                
                String selectedField = lstDataList.getSelectedValue();
                String type = fieldTypeMap.getOrDefault(selectedField, "UNIQUE");

                switch (type) {
                    case "UNIQUE":
                        rbdFieldType1.setSelected(true);
                        break;
                    case "MASTER":
                        rbdFieldType2.setSelected(true);
                        break;
                    case "DEPENDENT":
                        rbdFieldType3.setSelected(true);
                        break;
                }

                // Mostrar coordenadas si ese campo tiene área definida en cualquier página
                Map<Integer, Rectangle2D.Double> areas = fieldAreasByPage.get(selectedField);
                if (areas != null && !areas.isEmpty()) {
                    // Solo debe haber una entrada por campo
                    Map.Entry<Integer, Rectangle2D.Double> entry = areas.entrySet().iterator().next();
                    int page = entry.getKey(); // 0 = frontal, 1 = trasera
                    Rectangle2D.Double rect = entry.getValue();

                    txfPage.setText(String.valueOf(page + 1));
                    txfAxisX.setText(String.format("%.4f", rect.x));
                    txfAxisY.setText(String.format("%.4f", rect.y));
                } else {
                    txfPage.setText("");
                    txfAxisX.setText("");
                    txfAxisY.setText("");
                }

                // Refrescar visualmente los rectángulos para reflejar si este campo está seleccionado
                Set<String> selectedOnly = new HashSet<>();
                selectedOnly.add(selectedField);
                pagePanel.setSelectedFields(selectedOnly);
                Map<String, Rectangle2D.Double> areasForThisPage = new LinkedHashMap<>();

                for (Map.Entry<String, Map<Integer, Rectangle2D.Double>> entry : fieldAreasByPage.entrySet()) {
                    String campo = entry.getKey();
                    Rectangle2D.Double rect = entry.getValue().get(pagePanel.getCurrentPage());
                    if (rect != null) {
                        areasForThisPage.put(campo, rect);
                    }
                }

                pagePanel.setSelections(areasForThisPage);

            } else {

                rbdFieldType1.setEnabled(false);
                rbdFieldType2.setEnabled(false);
                rbdFieldType3.setEnabled(false);
                lblPage.setEnabled(false);
                lblAxisX.setEnabled(false);
                lblAxisY.setEnabled(false);
                txfPage.setEnabled(false);
                txfAxisX.setEnabled(false);
                txfAxisY.setEnabled(false);
                btnValidate.setEnabled(false);
                
                btnGroupDataFormat.clearSelection();
                txfPage.setText("");
                txfAxisX.setText("");
                txfAxisY.setText("");

                // También limpiar visualización de selección de áreas
                pagePanel.setSelectedFields(new HashSet<>());
                pagePanel.setSelections(new LinkedHashMap<>());
            }
        });

        java.awt.event.ActionListener fieldTypeListener = e2 -> {
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField == null) return;

            if (rbdFieldType1.isSelected()) {
                fieldTypeMap.put(selectedField, "UNIQUE");
            } else if (rbdFieldType2.isSelected()) {
                fieldTypeMap.put(selectedField, "MASTER");
            } else if (rbdFieldType3.isSelected()) {
                fieldTypeMap.put(selectedField, "DEPENDENT");
            }   
        };

        rbdFieldType1.addActionListener(fieldTypeListener);
        rbdFieldType2.addActionListener(fieldTypeListener);
        rbdFieldType3.addActionListener(fieldTypeListener);


        
        // Listener para volver a la primera página cuando se selecciona "Documento completo"
        rbdPagesScanner1.addActionListener(e -> {
            if (pdfDocument == null) return;

            try {
                PDFRenderer renderer = new PDFRenderer(pdfDocument);
                BufferedImage img = renderer.renderImageWithDPI(0, 150);
                currentImage = img;
                pagePanel.setImage(img);
                pagePanel.setCurrentPage(0);

                // Restaurar los valores en los campos (por coherencia visual)
                txfStart.setText("1");
                txfFinish.setText(String.valueOf(pdfDocument.getNumberOfPages()));

                scrPdfViewer.revalidate();
                scrPdfViewer.repaint();

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    pdfareaextractortoexcel.this,
                    "Error al mostrar la primera página:\n" + ex.getMessage(),
                    "Error de renderizado",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // Listener para actualizar la página mostrada según el campo "txfStart"
        txfStart.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {

            private void updatePageView() {
                if (pdfDocument == null) return;
                if (!rbdPagesScanner2.isSelected()) return;

                try {
                    int paginaInicio = Integer.parseInt(txfStart.getText().trim());
                    int totalPaginas = pdfDocument.getNumberOfPages();

                    // Validación del rango
                    if (paginaInicio < 1 || paginaInicio > totalPaginas) {
                        return;
                    }

                    // Renderizar la nueva página
                    PDFRenderer renderer = new PDFRenderer(pdfDocument);
                    BufferedImage img = renderer.renderImageWithDPI(paginaInicio - 1, 150);
                    currentImage = img;
            
                    // Actualizamos el panel sin recrearlo
                    pagePanel.setImage(img);
                    pagePanel.setCurrentPage(paginaInicio - 1);

                    scrPdfViewer.revalidate();
                    scrPdfViewer.repaint();

                } catch (NumberFormatException ex) {
                    // El usuario está escribiendo algo no numérico → ignoramos
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                        pdfareaextractortoexcel.this,
                        "Error al mostrar la página " + txfStart.getText() + ":\n" + ex.getMessage(),
                        "Error de renderizado",
                        JOptionPane.ERROR_MESSAGE
                    );
                }
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) {
                updatePageView();
            }

            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) {
                updatePageView();
            }

            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) {
                updatePageView();
            }
        });
        
        // Validación dinámica de txfStart Y txfFinish
        javax.swing.event.DocumentListener rangoListener = new javax.swing.event.DocumentListener() {
            private void validarRango() {
                if (pdfDocument == null) return;

                int totalPaginas = pdfDocument.getNumberOfPages();
                String startText = txfStart.getText().trim();
                String finishText = txfFinish.getText().trim();

                boolean valido = true;

                try {
                    int start = Integer.parseInt(startText);
                    int finish = Integer.parseInt(finishText);

                    // Validar: start >= 1 y finish <= totalPaginas
                    if (start < 1 || finish > totalPaginas || start >= finish) {
                        valido = false;
                    }
                } catch (NumberFormatException ex) {
                    // Si no son números válidos, marcamos error
                    valido = false;
                }

                java.awt.Color color = valido ? java.awt.Color.BLACK : java.awt.Color.RED;
                txfStart.setForeground(color);
                txfFinish.setForeground(color);
            }

            @Override
            public void insertUpdate(javax.swing.event.DocumentEvent e) { validarRango(); }
            @Override
            public void removeUpdate(javax.swing.event.DocumentEvent e) { validarRango(); }
            @Override
            public void changedUpdate(javax.swing.event.DocumentEvent e) { validarRango(); }
        };

        // Asignar el listener a ambos campos
        txfStart.getDocument().addDocumentListener(rangoListener);
        txfFinish.getDocument().addDocumentListener(rangoListener);

        // Listener para "Cara delantera" (tglPag1)
        tglPag1.addActionListener(e -> {
            if (pdfDocument == null) return;

            try {
                int paginaBase;

                if (rbdPagesScanner2.isSelected()) {
                    // Modo personalizado
                    paginaBase = Integer.parseInt(txfStart.getText().trim());
                } else {
                    // Modo completo
                    paginaBase = 1;
                }

                // Renderizar la página base (delantera)
                PDFRenderer renderer = new PDFRenderer(pdfDocument);
                BufferedImage img = renderer.renderImageWithDPI(paginaBase - 1, 150);
                currentImage = img;
                pagePanel.setImage(img);
                pagePanel.setCurrentPage(paginaBase - 1);
                refreshVisibleSelections();

                scrPdfViewer.revalidate();
                scrPdfViewer.repaint();

                // Sincronizar estados de botones
                if (!tglPag1.isSelected()) tglPag1.setSelected(true);
                tglPag2.setSelected(false);

            } catch (Exception ex) {
                // Cualquier error de conversión o renderizado
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    pdfareaextractortoexcel.this,
                    "Error al mostrar la cara delantera:\n" + ex.getMessage(),
                    "Error de renderizado",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });


        // Listener para "Cara trasera" (tglPag2)
        tglPag2.addActionListener(e -> {
            if (pdfDocument == null) return;

            try {
                int totalPaginas = pdfDocument.getNumberOfPages();
                int paginaBase;

                if (rbdPagesScanner2.isSelected()) {
                    // En modo personalizado, la "trasera" es la página siguiente a txfStart
                    paginaBase = Integer.parseInt(txfStart.getText().trim()) + 1;
                } else {
                    // En modo completo, la "trasera" es la página 2
                    paginaBase = 2;
                }

                // Evitar pasar del total
                if (paginaBase > totalPaginas) {
                    JOptionPane.showMessageDialog(
                        pdfareaextractortoexcel.this,
                        "El documento solo tiene " + totalPaginas + " páginas.",
                        "Página fuera de rango",
                        JOptionPane.WARNING_MESSAGE
                    );
                    tglPag2.setSelected(false);
                    tglPag1.setSelected(true);
                    return;
                }

                // Renderizar la página trasera
                PDFRenderer renderer = new PDFRenderer(pdfDocument);
                BufferedImage img = renderer.renderImageWithDPI(paginaBase - 1, 150);
                currentImage = img;
                pagePanel.setImage(img);
                pagePanel.setCurrentPage(paginaBase - 1);
                refreshVisibleSelections();

                scrPdfViewer.revalidate();
                scrPdfViewer.repaint();

                // Sincronizar estados de botones
                if (!tglPag2.isSelected()) tglPag2.setSelected(true);
                tglPag1.setSelected(false);

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    pdfareaextractortoexcel.this,
                    "Error al mostrar la cara trasera:\n" + ex.getMessage(),
                    "Error de renderizado",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
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
        btnSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSelectorActionPerformed(evt);
            }
        });

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
        btnAddData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddDataActionPerformed(evt);
            }
        });

        btnDeleteData.setText("Eliminar campo");
        btnDeleteData.setPreferredSize(new java.awt.Dimension(105, 23));
        btnDeleteData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteDataActionPerformed(evt);
            }
        });

        btnClearListData.setText("Borrar lista");
        btnClearListData.setPreferredSize(new java.awt.Dimension(105, 23));
        btnClearListData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearListDataActionPerformed(evt);
            }
        });

        btnEditData.setText("Editar campo");
        btnEditData.setPreferredSize(new java.awt.Dimension(105, 23));
        btnEditData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditDataActionPerformed(evt);
            }
        });

        btnMoveUpData.setText("Mover arriba");
        btnMoveUpData.setPreferredSize(new java.awt.Dimension(105, 23));
        btnMoveUpData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveUpDataActionPerformed(evt);
            }
        });

        btnMoveDownData.setText("Mover abajo");
        btnMoveDownData.setPreferredSize(new java.awt.Dimension(105, 23));
        btnMoveDownData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoveDownDataActionPerformed(evt);
            }
        });

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
                            .addComponent(btnAddData, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(btnDeleteData, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE)
                            .addComponent(btnClearListData, javax.swing.GroupLayout.DEFAULT_SIZE, 136, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnMoveDownData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(btnEditData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE)
                            .addComponent(btnMoveUpData, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 135, Short.MAX_VALUE))))
                .addContainerGap())
        );
        pnlDataListLayout.setVerticalGroup(
            pnlDataListLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlDataListLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblDataList)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 281, Short.MAX_VALUE)
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

        javax.swing.GroupLayout pnlDataFormat3Layout = new javax.swing.GroupLayout(pnlDataFormat3);
        pnlDataFormat3.setLayout(pnlDataFormat3Layout);
        pnlDataFormat3Layout.setHorizontalGroup(
            pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        pnlDataFormat3Layout.setVerticalGroup(
            pnlDataFormat3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        pnlValidate.setBackground(new java.awt.Color(180, 180, 180));
        pnlValidate.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        btnGenerate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnGenerate.setText("Generar Excel");

        btnValidate.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnValidate.setText("Validar datos");
        btnValidate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnValidateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlValidateLayout = new javax.swing.GroupLayout(pnlValidate);
        pnlValidate.setLayout(pnlValidateLayout);
        pnlValidateLayout.setHorizontalGroup(
            pnlValidateLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlValidateLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnValidate, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(btnGenerate, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                .addComponent(scrPdfViewer, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
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

    private void btnSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSelectorActionPerformed

        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF", "pdf");
        chooser.setFileFilter(filter);
        chooser.setAcceptAllFileFilterUsed(false);
        int result = chooser.showOpenDialog(this);
        if (result != JFileChooser.APPROVE_OPTION) {
            return;
        }

        PDFLink = chooser.getSelectedFile().getAbsolutePath();
        txfLink.setText(PDFLink);

        PDDocument doc = null;
        File archivo = new File(PDFLink);
        try {
            doc = Loader.loadPDF(archivo);
        } catch (org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException ex) {
            String password = JOptionPane.showInputDialog(
                this,
                "Este archivo PDF está protegido con contraseña.\nPor favor, introdúcela:",
                "Contraseña requerida",
                JOptionPane.QUESTION_MESSAGE
            );
            if (password == null) {
                JOptionPane.showMessageDialog(
                    this,
                    "No se cargó el PDF porque no se proporcionó la contraseña.",
                    "Operación cancelada",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }
            try {
                doc = Loader.loadPDF(archivo, password);
            } catch (org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException ex2) {
                JOptionPane.showMessageDialog(
                    this,
                    "Contraseña incorrecta. No se pudo abrir el PDF.",
                    "Error de Contraseña",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (IOException ioe) {
                ioe.printStackTrace();
                JOptionPane.showMessageDialog(
                    this,
                    "Error al abrir el PDF cifrado:\n" + ioe.getMessage(),
                    "Error interno",
                    JOptionPane.ERROR_MESSAGE
                );
                 return;
            }
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al cargar o renderizar el PDF:\n" + e.getMessage(),
                "Error interno",
                JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        pdfDocument = doc;
        try {
            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            BufferedImage img = renderer.renderImageWithDPI(0, 150);
            currentImage = img;

            pagePanel = new PDFPagePanel(img);
            pagePanel.setCanSelect(true);
            pagePanel.setCurrentPage(0);
            scrPdfViewer.setViewportView(pagePanel);
            splMain.setDividerLocation(0.5);

            SwingUtilities.invokeLater(() -> {
                Dimension vp = scrPdfViewer.getViewport().getExtentSize();
                double initZoom = vp.width / (double) img.getWidth();
                pagePanel.setZoom(initZoom);

                sldRightPanel.setMinimum(50);
                sldRightPanel.setMaximum(200);
                sldRightPanel.setValue((int) (initZoom * 100));
            });

            sldRightPanel.addChangeListener(e -> {
                double z = sldRightPanel.getValue() / 100.0;
                pagePanel.setZoom(z);
            });

            scrPdfViewer.revalidate();
            scrPdfViewer.repaint();

            // Habilitar componentes de UI
            rdbStructure1.setEnabled(true);
            rdbStructure2.setEnabled(true);
            rdbStructure3.setEnabled(true);
            rbdPagesScanner1.setEnabled(true);
            rbdPagesScanner2.setEnabled(true);
            lstDataList.setEnabled(true);
            btnAddData.setEnabled(true);
            rbdFieldType1.setEnabled(false);
            rbdFieldType2.setEnabled(false);
            rbdFieldType3.setEnabled(false);
            tglPag1.setEnabled(true);
            tglPag2.setEnabled(true);
            sldRightPanel.setEnabled(true);
            txfPage.setEnabled(false);
            txfAxisX.setEnabled(false);
            txfAxisY.setEnabled(false);
            lblStructure.setEnabled(true);
            lblPagesScanner.setEnabled(true);
            lblPageStart.setEnabled(true);
            lblPageFinish.setEnabled(true);
            lblData.setEnabled(true);
            lblDataList.setEnabled(true);
            lblDataFormat1.setEnabled(true);
            lblDataFormat2.setEnabled(true);
            lblPage.setEnabled(false);
            lblAxisX.setEnabled(false);
            lblAxisY.setEnabled(false);
            imgPage1.setEnabled(true);
            imgPage2.setEnabled(true);
            imgPage3.setEnabled(true);
            imgPage4.setEnabled(true);
            imgPage5.setEnabled(true);

            rdbStructure1.setSelected(true);
            rbdPagesScanner1.setSelected(true);
            tglPag1.setSelected(true);
            tglPag2.setSelected(false);

            applyStructureMode();
            applyPageScannerMode();

            // Selección de áreas en el PDF
            pagePanel.setOnAreaSelected(rect -> {
                int index = lstDataList.getSelectedIndex();
                if (index == -1) {
                    JOptionPane.showMessageDialog(this, "Por favor, seleccione un item en la lista de campos antes de definir un área.");
                    return;
                }

                String fieldName = ((DefaultListModel<String>) lstDataList.getModel()).get(index);
                double zoom = pagePanel.getZoom();
                int imgW = pagePanel.getImageWidth();
                int imgH = pagePanel.getImageHeight();

                double fx = rect.getX() / (imgW * zoom);
                double fy = rect.getY() / (imgH * zoom);
                double fw = rect.getWidth() / (imgW * zoom);
                double fh = rect.getHeight() / (imgH * zoom);
                Rectangle2D.Double relativeRect = new Rectangle2D.Double(fx, fy, fw, fh);
                int currentPage = pagePanel.getCurrentPage();

                fieldAreasByPage.computeIfAbsent(fieldName, k -> new HashMap<>()).put(currentPage, relativeRect);

                try {
                    PDPage page = pdfDocument.getPage(currentPage);
                    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                    
                    float dpi = 150f;
                    float scale = 72f / dpi;

                    Rectangle2D areaInPoints = new Rectangle2D.Double(
                        fx * imgW * scale,
                        fy * imgH * scale,
                        fw * imgW * scale,
                        fh * imgH * scale
                    );

                    stripper.addRegion("area", areaInPoints);
                    stripper.extractRegions(page);
                    String extractedText = stripper.getTextForRegion("area").trim();

                    JOptionPane.showMessageDialog(this,
                        "Texto detectado:\n\n" + extractedText,
                        "Área seleccionada",
                        JOptionPane.INFORMATION_MESSAGE
                    );

                    txfPage.setText(String.valueOf(currentPage + 1));
                    txfAxisX.setText(String.format("%.4f", fx));
                    txfAxisY.setText(String.format("%.4f", fy));
                    refreshVisibleSelections();
                    pagePanel.repaint();

                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error al extraer texto: " + ex.getMessage());
                }
            });

        } catch (IOException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al renderizar la primera página:\n" + ex.getMessage(),
                "Error interno",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnSelectorActionPerformed

    private void btnAddDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddDataActionPerformed
        // Campo de texto para el nombre
        javax.swing.JTextField txtNombre = new javax.swing.JTextField(20);

        // Panel simple con etiqueta + campo
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.add(new javax.swing.JLabel("Nombre del campo:"));
        panel.add(txtNombre);

        // Crear el cuadro de diálogo
        javax.swing.JOptionPane optionPane = new javax.swing.JOptionPane(
            panel,
            javax.swing.JOptionPane.PLAIN_MESSAGE,
            javax.swing.JOptionPane.OK_CANCEL_OPTION
        );

        javax.swing.JDialog dialog = optionPane.createDialog(this, "Añadir nuevo campo");

        // Solicitar foco en el campo justo cuando se muestra el diálogo
        dialog.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                txtNombre.requestFocusInWindow();
            }
        });

        // Mostrar el diálogo
        dialog.setVisible(true);

        // Procesar resultado solo si el usuario pulsa "Aceptar"
        Object selectedValue = optionPane.getValue();
        if (selectedValue != null && (int) selectedValue == javax.swing.JOptionPane.OK_OPTION) {
            String nombreCampo = txtNombre.getText().trim();
            if (!nombreCampo.isEmpty()) {
                javax.swing.ListModel<String> lm = lstDataList.getModel();
                javax.swing.DefaultListModel<String> model;
                if (lm instanceof javax.swing.DefaultListModel) {
                    model = (javax.swing.DefaultListModel<String>) lm;
                } else {
                    model = new javax.swing.DefaultListModel<>();
                    lstDataList.setModel(model);
                }
                model.addElement(nombreCampo);
                fieldTypeMap.put(nombreCampo, "UNIQUE");
                lstDataList.setSelectedIndex(model.getSize() - 1);
            }
        }

        dialog.dispose();
    }//GEN-LAST:event_btnAddDataActionPerformed

    private void btnDeleteDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDataActionPerformed
        // Obtener el modelo de la lista
        javax.swing.ListModel<String> lm = lstDataList.getModel();
        if (!(lm instanceof javax.swing.DefaultListModel)) return;
        javax.swing.DefaultListModel<String> model = (javax.swing.DefaultListModel<String>) lm;

        // Obtener el índice del elemento seleccionado
        int index = lstDataList.getSelectedIndex();
        if (index == -1) return; // Ningún ítem seleccionado

        // Obtener el nombre del campo seleccionado
        String nombreCampo = model.getElementAt(index);

        // Mostrar el diálogo de confirmación
        int opcion = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que quiere eliminar el item \"" + nombreCampo + "\"?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        // Si el usuario confirma, eliminar el elemento
        if (opcion == javax.swing.JOptionPane.YES_OPTION) {
            model.remove(index);
            fieldTypeMap.remove(nombreCampo);
            fieldAreasByPage.remove(nombreCampo);
            refreshVisibleSelections();

            // Ajustar la selección tras eliminar
            if (!model.isEmpty()) {
                int nuevoIndex = Math.min(index, model.getSize() - 1);
                lstDataList.setSelectedIndex(nuevoIndex);
            }
        }
    }//GEN-LAST:event_btnDeleteDataActionPerformed

    private void btnClearListDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearListDataActionPerformed
        javax.swing.ListModel<String> lm = lstDataList.getModel();
        if (!(lm instanceof javax.swing.DefaultListModel)) return;

        javax.swing.DefaultListModel<String> model = (javax.swing.DefaultListModel<String>) lm;

        // Si la lista está vacía, no hacemos nada
        if (model.isEmpty()) return;

        // Mostrar confirmación
        int opcion = javax.swing.JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que quiere eliminar todos los ítems de la lista?",
            "Confirmar eliminación",
            javax.swing.JOptionPane.YES_NO_OPTION,
            javax.swing.JOptionPane.WARNING_MESSAGE
        );

        // Si el usuario elige “Sí”, vaciar la lista
        if (opcion == javax.swing.JOptionPane.YES_OPTION) {
            model.clear();
            fieldTypeMap.clear();
            fieldAreasByPage.clear();
            refreshVisibleSelections();

            // Desactivar botones relacionados tras vaciar la lista
            btnDeleteData.setEnabled(false);
            btnEditData.setEnabled(false);
            btnMoveUpData.setEnabled(false);
            btnMoveDownData.setEnabled(false);
            btnClearListData.setEnabled(false);
        }
    }//GEN-LAST:event_btnClearListDataActionPerformed

    private void btnEditDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDataActionPerformed
        // Verificar que haya un ítem seleccionado
        int index = lstDataList.getSelectedIndex();
        if (index == -1) return;

        javax.swing.ListModel<String> lm = lstDataList.getModel();
        if (!(lm instanceof javax.swing.DefaultListModel)) return;
        javax.swing.DefaultListModel<String> model = (javax.swing.DefaultListModel<String>) lm;

        // Obtener el nombre actual del campo
        String oldName = model.getElementAt(index);

        // Crear el campo de texto con el nombre actual preescrito
        javax.swing.JTextField txtNombre = new javax.swing.JTextField(oldName, 20);

        // Crear el panel simple con etiqueta + campo
        javax.swing.JPanel panel = new javax.swing.JPanel();
        panel.add(new javax.swing.JLabel("Editar nombre del campo:"));
        panel.add(txtNombre);

        // Crear el cuadro de diálogo manualmente para poder dar el foco automático
        javax.swing.JOptionPane optionPane = new javax.swing.JOptionPane(
            panel,
            javax.swing.JOptionPane.PLAIN_MESSAGE,
            javax.swing.JOptionPane.OK_CANCEL_OPTION
        );

       javax.swing.JDialog dialog = optionPane.createDialog(this, "Editar campo");

        // Foco automático en el campo al abrir el diálogo
        dialog.addWindowFocusListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowGainedFocus(java.awt.event.WindowEvent e) {
                txtNombre.requestFocusInWindow();
                txtNombre.selectAll();
            }
        });

        // Mostrar el cuadro
        dialog.setVisible(true);

        // Procesar resultado
        Object selectedValue = optionPane.getValue();
        if (selectedValue != null && (int) selectedValue == javax.swing.JOptionPane.OK_OPTION) {
            String newName = txtNombre.getText().trim();
            if (!newName.isEmpty() && !newName.equals(oldName)) {
                // Actualizar el nombre en la lista
                model.setElementAt(newName, index);
                lstDataList.setSelectedIndex(index);

                // Actualizar el tipo en el mapa
                String oldType = fieldTypeMap.getOrDefault(oldName, "UNIQUE");
                fieldTypeMap.remove(oldName);
                fieldTypeMap.put(newName, oldType);
            }
        }

        dialog.dispose();
    }//GEN-LAST:event_btnEditDataActionPerformed

    private void btnMoveUpDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveUpDataActionPerformed
        int index = lstDataList.getSelectedIndex();
        if (index <= 0) return;

        javax.swing.DefaultListModel<String> model = (javax.swing.DefaultListModel<String>) lstDataList.getModel();
        String current = model.getElementAt(index);
        String previous = model.getElementAt(index - 1);

        // Intercambiar en el modelo
        model.setElementAt(current, index - 1);
        model.setElementAt(previous, index);

        // Intercambiar en el mapa
        String typeCurrent = fieldTypeMap.get(current);
        String typePrevious = fieldTypeMap.get(previous);

        fieldTypeMap.put(current, typePrevious);
        fieldTypeMap.put(previous, typeCurrent);

        lstDataList.setSelectedIndex(index - 1);
    }//GEN-LAST:event_btnMoveUpDataActionPerformed

    private void btnMoveDownDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveDownDataActionPerformed
        int index = lstDataList.getSelectedIndex();
        javax.swing.DefaultListModel<String> model = (javax.swing.DefaultListModel<String>) lstDataList.getModel();

        if (index == -1 || index >= model.getSize() - 1) return;

        String current = model.getElementAt(index);
        String next = model.getElementAt(index + 1);

        // Intercambiar en el modelo
        model.setElementAt(next, index);
        model.setElementAt(current, index + 1);

        // Intercambiar en el mapa
        String typeCurrent = fieldTypeMap.get(current);
        String typeNext = fieldTypeMap.get(next);

        fieldTypeMap.put(current, typeNext);
        fieldTypeMap.put(next, typeCurrent);

        lstDataList.setSelectedIndex(index + 1);
    }//GEN-LAST:event_btnMoveDownDataActionPerformed

    private void btnValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidateActionPerformed
        try {
            // Rango de páginas
            int totalPages = pdfDocument.getNumberOfPages();
            int startPage = 1;
            int finishPage = totalPages;

            if (rbdPagesScanner2.isSelected()) {
                try {
                    startPage = Integer.parseInt(txfStart.getText().trim());
                    finishPage = Integer.parseInt(txfFinish.getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        this,
                        "Introduce números válidos en el rango de páginas.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                if (startPage < 1 || finishPage > totalPages || startPage > finishPage) {
                    JOptionPane.showMessageDialog(
                        this,
                        "El rango de páginas especificado no es válido.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }

            // Lista de campos en el orden visual
            ArrayList<String> fields = new ArrayList<>();
            ListModel<String> model = lstDataList.getModel();
            for (int i = 0; i < model.getSize(); i++) fields.add(model.getElementAt(i));
            if (fields.isEmpty()) {
                JOptionPane.showMessageDialog(
                    this,
                    "No hay campos definidos en la lista.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Comprobación de que al menos algún campo tiene área en alguna cara
            boolean anyAreaDefined = false;
            for (String f : fields) {
                Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(f);
                if (perPage != null && !perPage.isEmpty()) { anyAreaDefined = true; break; }
            }
            if (!anyAreaDefined) {
                JOptionPane.showMessageDialog(
                    this,
                    "Ningún campo tiene un área asignada.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            // Modo estructura
            boolean struct1 = rdbStructure1.isSelected(); // cada dos páginas
            boolean struct2 = rdbStructure2.isSelected(); // cada página
            boolean struct3 = rdbStructure3.isSelected(); // dos caras (campos distintos por cara)

            extractedData.clear();

            System.out.println("=== Inicio de la extracción de datos ===");
            System.out.println("Modo estructura: " + (struct1 ? "Cada dos páginas" : struct2 ? "Cada página" : "Campos diferentes por ambas caras"));
            System.out.println("Rango de páginas: " + startPage + " - " + finishPage);
            System.out.println("----------------------------------------");

            // Conversor de fracción [0..1] a puntos PDF
            java.util.function.BiFunction<Rectangle2D.Double, PDPage, Rectangle2D.Double> toPdfPoints =
                (frac, page) -> {
                    PDRectangle mb = page.getMediaBox();
                    double pw = mb.getWidth();
                    double ph = mb.getHeight();
                    return new Rectangle2D.Double(frac.x * pw, frac.y * ph, frac.width * pw, frac.height * ph);
                };

            int pageStep = struct1 ? 2 : 1;

            for (int p = startPage; p <= finishPage; p += pageStep) {
                int pageIndex = p - 1;
                PDPage page = pdfDocument.getPage(pageIndex);

                // Selección del mapa de áreas según la estructura y la cara
                Map<String, Rectangle2D.Double> currentFaceAreas = new LinkedHashMap<>();
                if (struct3) {
                    // Cara por paridad: 0 = delantera, 1 = trasera
                    int face = pageIndex % 2;
                    for (String field : fields) {
                        Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                        if (perPage == null) continue;
                        Rectangle2D.Double frac = perPage.get(face);
                        if (frac != null) currentFaceAreas.put(field, frac);
                    }
                } else {
                    // Estructuras 1 y 2: usan siempre la cara delantera (face=0)
                    for (String field : fields) {
                        Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                        if (perPage == null) continue;
                        Rectangle2D.Double frac = perPage.get(0);
                        if (frac != null) currentFaceAreas.put(field, frac);
                    }
                }

                // Si en esta página no hay campos con área para la cara, se omite
                if (currentFaceAreas.isEmpty()) continue;

                PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                stripper.setSortByPosition(true);

                // Registrar solo los campos que tienen área en la cara actual
                for (Map.Entry<String, Rectangle2D.Double> e : currentFaceAreas.entrySet()) {
                    Rectangle2D.Double rectPDF = toPdfPoints.apply(e.getValue(), page);
                    stripper.addRegion(e.getKey(), rectPDF);
                }

                stripper.extractRegions(page);

                // Fila con solo los campos de esta cara
                Map<String, String> row = new LinkedHashMap<>();
                for (String field : fields) {
                    if (!currentFaceAreas.containsKey(field)) continue; // no hay área en esta cara
                    String text = stripper.getTextForRegion(field);
                    row.put(field, text == null ? "" : text.trim());
                }

                if (!row.isEmpty()) {
                    extractedData.add(row);

                    // Salida por consola siguiendo el orden de la lista de campos,
                    // imprimiendo solo los presentes en esta cara
                    StringBuilder line = new StringBuilder();
                    boolean first = true;
                    for (String field : fields) {
                        if (!row.containsKey(field)) continue;
                        if (!first) line.append(", ");
                        line.append(row.getOrDefault(field, ""));
                        first = false;
                    }
                    System.out.println(line.toString());
                }
            }

            System.out.println("=== Fin del recorrido del documento ===");

            JOptionPane.showMessageDialog(
                this,
                "La validación y recogida de datos se ha completado correctamente.",
                "Validación completada",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Se produjo un error durante la validación: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnValidateActionPerformed

    // Controla qué caras están disponibles según la estructura seleccionada
    private void applyStructureMode() {
        if (pdfDocument == null) return;

        // Caso 1 y 2
        if (rdbStructure1.isSelected() || rdbStructure2.isSelected()) {
            
            // Solo se permite la cara delantera
            tglPag1.setEnabled(true);
            tglPag2.setEnabled(false);

            // Si estaba viendo la trasera, volvemos a la delantera
            if (tglPag2.isSelected()) {
                tglPag2.setSelected(false);
                tglPag1.setSelected(true);
                mostrarCaraDelantera();
            }

        } 
        // Caso 3
        else if (rdbStructure3.isSelected()) {
            // Activamos ambos botones
            tglPag1.setEnabled(true);
            tglPag2.setEnabled(true);
        }
    }

    // Renderiza la "cara delantera" (página base del rango o página 1)
    private void mostrarCaraDelantera() {
        try {
            int paginaBase;

            if (rbdPagesScanner2.isSelected()) {
                // En modo personalizado, usamos la página de inicio definida por el usuario
                paginaBase = Integer.parseInt(txfStart.getText().trim());
            } else {
                // En modo documento completo, siempre página 1
                paginaBase = 1;
            }

            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            BufferedImage img = renderer.renderImageWithDPI(paginaBase - 1, 150);
            currentImage = img;
            pagePanel.setImage(img);
            pagePanel.setCurrentPage(paginaBase - 1);

            scrPdfViewer.revalidate();
            scrPdfViewer.repaint();

            // Asegurar sincronización visual
            tglPag1.setSelected(true);
            tglPag2.setSelected(false);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al renderizar la cara delantera:\n" + ex.getMessage(),
                "Error de renderizado",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }

    
    // Habilita/inhabilita rangos de páginas personalizados
    private void applyPageScannerMode() {
        boolean custom = rbdPagesScanner2.isSelected();
        txfStart.setEnabled(custom);
        txfFinish.setEnabled(custom);
        txfStart.setEditable(custom);
        txfFinish.setEditable(custom);
        txfStart.setFocusable(custom);
        txfFinish.setFocusable(custom);
        // Rellenado de campos de texto en Páginas a escanear
        int totalPaginas = pdfDocument.getNumberOfPages();
        txfStart.setText("1");
        txfFinish.setText(String.valueOf(totalPaginas));

        if (!custom) {
            // Documento completo: vaciamos campos y los dejamos bloqueados
            txfStart.setText("");
            txfFinish.setText("");
        }
    }

    private void refreshVisibleSelections() {
        int currentPage = pagePanel.getCurrentPage();
        Map<String, Rectangle2D.Double> visibleSelections = new LinkedHashMap<>();

        for (Map.Entry<String, Map<Integer, Rectangle2D.Double>> entry : fieldAreasByPage.entrySet()) {
            String field = entry.getKey();
            Map<Integer, Rectangle2D.Double> perPage = entry.getValue();

            if (perPage.containsKey(currentPage)) {
                visibleSelections.put(field, perPage.get(currentPage));
            }
        }

        pagePanel.setSelections(visibleSelections);
    }

    
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(pdfareaextractortoexcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pdfareaextractortoexcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pdfareaextractortoexcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pdfareaextractortoexcel.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new pdfareaextractortoexcel().setVisible(true);
            }
        });
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
    private javax.swing.JLabel lblDataList;
    private javax.swing.JLabel lblLoadFile;
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
