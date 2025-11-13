/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pdfareaextractortoexcel;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import javax.swing.ButtonGroup;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 *
 * @author Ivan
 */
public class pdfareaextractortoexcel extends JFrame {

    private ButtonGroup btnGroupDataFormat;
    private ButtonGroup btnGroupScanner;
    private ButtonGroup btnGroupStructure;
    private ButtonGroup btnGroupPages;
    private ArrayList<Map<String,String>> extractedData = new ArrayList<>();
    private Map<String, Map<Integer, Rectangle2D.Double>> fieldAreasByPage = new HashMap<>();
    private Map<String, Boolean> spacesOptionByField = new LinkedHashMap<>();
    private Map<String, Boolean> symbolsOptionByField = new LinkedHashMap<>();
    private Map<String, Boolean> itemOptionByField = new LinkedHashMap<>();
    private Map<String, String> itemTypeByField = new LinkedHashMap<>();
    private Map<String, Boolean> textOptionByField = new LinkedHashMap<>();
    private Map<String, String> textTypeByField = new LinkedHashMap<>();
    private Map<String, String> dependentMasterMap = new LinkedHashMap<>();
    private LinkedHashMap<String, String> fieldTypeMap = new LinkedHashMap<>();
    private BufferedImage currentImage;
    private PDDocument pdfDocument;
    private PDFPagePanel pagePanel;
    private String PDFLink;
    /**
     * Creates new form pdfareaextractortoexcel
     */
    public pdfareaextractortoexcel() {
        initComponents();

        // 1. Configuración inicial de JFrame y splMain
        setTitle("PDF Area Extractor to Excel (Java)");
        setExtendedState(pdfareaextractortoexcel.MAXIMIZED_BOTH);
        splMain.setResizeWeight(0.5);
        splMain.setEnabled(false);
        SwingUtilities.invokeLater(() -> {
            splMain.setDividerLocation(0.5);
        });
        splRightPanel.setEnabled(false);
        
        // 1.1. Configuración inicial de componentes del panel pnlRight
        // 1.1.1. Imagen en blanco temporal
        BufferedImage placeholderImage = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        // 1.1.2. Creación del PDFPagePanel con imagen vacía
        pagePanel = new PDFPagePanel(placeholderImage);
        pagePanel.setZoom(1.0);
        pagePanel.setCanSelect(false);
        scrPdfViewer.setViewportView(pagePanel);
        // 1.1.3. Botones/Slider Navegación/zoom PDF
        tglPag1.setEnabled(false);
        tglPag2.setEnabled(false);
        sldRightPanel.setEnabled(false);
        // 1.1.4. Configuración de btnGroupPages
        btnGroupPages = new ButtonGroup();
        btnGroupPages.add(tglPag1);
        btnGroupPages.add(tglPag2);
        
        // 1.2. Configuración inicial de componentes del panel pnlLeft
        // 1.2.1. Configuración inicial de componentes del panel pnlLoadFile
        // 1.2.1.1. Campo de texto
        txfLink.setEditable(false);
        txfLink.setFocusable(false);
        
        // 1.2.2. Configuración inicial de componentes del panel pnlStructure
        // 1.2.2.1. Etiquetas
        lblStructure.setEnabled(false);
        // 1.2.2.2. Botones
        rdbStructure1.setEnabled(false);
        rdbStructure2.setEnabled(false);
        rdbStructure3.setEnabled(false);
        // 1.2.2.3. Grupo de botones
        btnGroupStructure = new ButtonGroup();
        btnGroupStructure.add(rdbStructure1);
        btnGroupStructure.add(rdbStructure2);
        btnGroupStructure.add(rdbStructure3);
        // 1.2.2.4. Imágenes
        imgPage1.setEnabled(false);
        imgPage2.setEnabled(false);
        imgPage3.setEnabled(false);
        imgPage4.setEnabled(false);
        imgPage5.setEnabled(false);
        // 1.2.2.5. Listeners para condiciones dinámicas
        rdbStructure1.addActionListener(e -> applyStructureMode());
        rdbStructure2.addActionListener(e -> applyStructureMode());
        rdbStructure3.addActionListener(e -> applyStructureMode());
        
        // 1.2.3. Configuración inicial de componentes del panel pnlPagesScanner
        // 1.2.3.1. Etiquetas
        lblPagesScanner.setEnabled(false);
        lblPageStart.setEnabled(false);
        lblPageFinish.setEnabled(false);
        // 1.2.3.2. Botones
        rbdPagesScanner1.setEnabled(false);
        rbdPagesScanner2.setEnabled(false);
        // 1.2.3.3. Grupo de botones
        btnGroupScanner = new ButtonGroup();
        btnGroupScanner.add(rbdPagesScanner1);
        btnGroupScanner.add(rbdPagesScanner2);
        // 1.2.3.4. Campos de texto
        txfStart.setEditable(false);
        txfFinish.setEditable(false);
        txfStart.setFocusable(false);
        txfFinish.setFocusable(false);
        txfStart.setEnabled(false);
        txfFinish.setEnabled(false);
        txfStart.setText("");
        txfFinish.setText("");
        // 1.2.3.5. Listeners para condiciones dinámicas
        rbdPagesScanner1.addActionListener(e -> applyPageScannerMode());
        rbdPagesScanner2.addActionListener(e -> applyPageScannerMode());
        
        // 1.2.4. Configuración inicial de componentes del panel pnlData
        lblData.setEnabled(false);
        // 1.2.4.1. Configuración inicial de componentes del panel pnlDataList
        //1.2.4.1.1. Etiqueta
        lblDataList.setEnabled(false);
        //1.2.4.1.2. Lista
        lstDataList.setEnabled(false);
        //1.2.4.1.3. Botones
        btnAddData.setEnabled(false);
        btnDeleteData.setEnabled(false);
        btnClearListData.setEnabled(false);
        btnEditData.setEnabled(false);
        btnMoveUpData.setEnabled(false);
        btnMoveDownData.setEnabled(false);
        // 1.2.4.2. Configuración inicial de componentes del panel pnlDataFormat1
        //1.2.4.2.1. Etiqueta
        lblDataFormat1.setEnabled(false);
        //1.2.4.2.2. Botones
        rbdFieldType1.setEnabled(false);
        rbdFieldType2.setEnabled(false);
        rbdFieldType3.setEnabled(false);
        //1.2.4.2.3. Grupo de botones
        btnGroupDataFormat = new ButtonGroup();
        btnGroupDataFormat.add(rbdFieldType1);
        btnGroupDataFormat.add(rbdFieldType2);
        btnGroupDataFormat.add(rbdFieldType3);
        // 1.2.4.3. Configuración inicial de componentes del panel pnlDataFormat2
        // 1.2.4.3.1. Etiquetas
        lblDataFormat2.setEnabled(false);
        lblPage.setEnabled(false);
        lblAxisX.setEnabled(false);
        lblAxisY.setEnabled(false);
        // 1.2.4.3.2. Campos de texto
        txfPage.setEnabled(false);
        txfAxisX.setEnabled(false);
        txfAxisY.setEnabled(false);
        txfPage.setEditable(false);
        txfAxisX.setEditable(false);
        txfAxisY.setEditable(false);
        txfPage.setFocusable(false);
        txfAxisX.setFocusable(false);
        txfAxisY.setFocusable(false);
        txfPage.setText("");
        txfAxisX.setText("");
        txfAxisY.setText("");
        // 1.2.4.4. Configuración inicial de componentes del panel pnlDataFormat3
        // 1.2.4.4.1. Etiquetas
        lblDataFormat3.setEnabled(false);
        lblMaster.setEnabled(false);
        // 1.2.4.4.1. Botones
        chkItem.setEnabled(false);
        chkText.setEnabled(false);
        chkSpaces.setEnabled(false);
        chkSymbols.setEnabled(false);
        // 1.2.4.4.1. Desplegables
        cmbMaster.setEnabled(false);
        cmbMaster.removeAllItems();
        cmbItem.setEnabled(false);
        cmbItem.removeAllItems();
        cmbItem.addItem("ID");
        cmbItem.addItem("Fecha");
        cmbItem.addItem("Página");
        cmbItem.setSelectedItem("ID");
        cmbText.setEnabled(false);
        cmbText.removeAllItems();
        cmbText.addItem("MAYÚSCULA");
        cmbText.addItem("minúscula");
        cmbText.addItem("Título");
        cmbText.setSelectedItem("MAYÚSCULA");
        // 1.2.4.5. Configuración inicial de componentes del panel pnlValidate
        // 1.2.4.5.1. Botones
        btnValidate.setEnabled(false);
        btnGenerate.setEnabled(false);
        
        // Modelo base
        lstDataList.setModel(new DefaultListModel<>());

        // Listener de selección de la lista de campos
        lstDataList.addListSelectionListener(e -> {
            if (e.getValueIsAdjusting()) return;

            ListModel<String> lm = lstDataList.getModel();
            if (!(lm instanceof DefaultListModel)) return;
            DefaultListModel<String> model = (DefaultListModel<String>) lm;

            int size = model.getSize();
            int index = lstDataList.getSelectedIndex();
            boolean hasSelection = (index != -1);

            // Botones de lista (existente)
            btnClearListData.setEnabled(size > 0);
            btnDeleteData.setEnabled(hasSelection);
            btnEditData.setEnabled(hasSelection);
            btnMoveUpData.setEnabled(hasSelection && index > 0);
            btnMoveDownData.setEnabled(hasSelection && index < size - 1);

            if (hasSelection) {
                String selectedField = lstDataList.getSelectedValue();

                // Habilitar controles básicos
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

                // Habilitar opciones por campo (siempre disponibles)
                chkSpaces.setEnabled(true);
                chkSymbols.setEnabled(true);

                // Cargar tipo de campo
                String type = fieldTypeMap.getOrDefault(selectedField, "UNIQUE");
                switch (type) {
                    case "MASTER":
                        rbdFieldType2.setSelected(true);
                        break;
                    case "DEPENDENT":
                        rbdFieldType3.setSelected(true);
                        break;
                    default:
                        rbdFieldType1.setSelected(true);
                        break;
                }

                // Cargar estado de opciones por campo
                Boolean sp = spacesOptionByField.get(selectedField);
                Boolean sy = symbolsOptionByField.get(selectedField);
                chkSpaces.setSelected(sp != null ? sp : false);
                chkSymbols.setSelected(sy != null ? sy : false);

                // Cargar estados de chkItem y chkText
                Boolean itemOpt = itemOptionByField.get(selectedField);
                Boolean textOpt = textOptionByField.get(selectedField);
                chkItem.setSelected(itemOpt != null ? itemOpt : false);
                chkText.setSelected(textOpt != null ? textOpt : false);

                // Cargar tipos de item y text
                String itemType = itemTypeByField.get(selectedField);
                String textType = textTypeByField.get(selectedField);
                cmbItem.setSelectedItem(itemType != null ? itemType : "ID");
                cmbText.setSelectedItem(textType != null ? textType : "MAYÚSCULA");

                // Actualizar cmbMaster si es tipo DEPENDENT
                if ("DEPENDENT".equals(type)) {
                    updateMasterComboBox();
                }

                // Aplicar lógica de habilitación basada en el tipo de campo seleccionado
                updateFieldTypeDependencies();

                // Mostrar coordenadas si existen
                Map<Integer, Rectangle2D.Double> areas = fieldAreasByPage.get(selectedField);
                if (areas != null && !areas.isEmpty()) {
                    Map.Entry<Integer, Rectangle2D.Double> entry = areas.entrySet().iterator().next();
                    int page = entry.getKey();
                    Rectangle2D.Double rect = entry.getValue();
                    txfPage.setText(String.valueOf(page + 1));
                    txfAxisX.setText(String.format("%.4f", rect.x));
                    txfAxisY.setText(String.format("%.4f", rect.y));
                } else {
                    txfPage.setText("");
                    txfAxisX.setText("");
                    txfAxisY.setText("");
                }

                // Refresco visual de selecciones
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
                // Deshabilitar cuando no hay selección
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

                chkSpaces.setEnabled(false);
                chkSymbols.setEnabled(false);
                chkItem.setEnabled(false);
                chkText.setEnabled(false);
                cmbItem.setEnabled(false);
                cmbText.setEnabled(false);
                lblMaster.setEnabled(false);
                cmbMaster.setEnabled(false);

                chkSpaces.setSelected(false);
                chkSymbols.setSelected(false);
                chkItem.setSelected(false);
                chkText.setSelected(false);

                btnGroupDataFormat.clearSelection();
                txfPage.setText("");
                txfAxisX.setText("");
                txfAxisY.setText("");

                pagePanel.setSelectedFields(new HashSet<>());
                pagePanel.setSelections(new LinkedHashMap<>());
            }
        });
        
        // Listeners para persistir las opciones por campo
        chkSpaces.addActionListener(e2 -> {
            String f = lstDataList.getSelectedValue();
            if (f != null) {
                spacesOptionByField.put(f, chkSpaces.isSelected());
            }
        });

        chkSymbols.addActionListener(e3 -> {
            String f = lstDataList.getSelectedValue();
            if (f != null) {
                symbolsOptionByField.put(f, chkSymbols.isSelected());
            }
        });
        
        // En el constructor, después de los otros listeners:
        cmbMaster.addActionListener(e -> {
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField != null && cmbMaster.getSelectedItem() != null) {
                String masterField = cmbMaster.getSelectedItem().toString();
                if (!masterField.equals("No hay campos maestros definidos")) {
                    dependentMasterMap.put(selectedField, masterField);
                }
            }
        });

        ActionListener fieldTypeListener = e2 -> {
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField == null) return;

            if (rbdFieldType1.isSelected()) {
                fieldTypeMap.put(selectedField, "UNIQUE");
            } else if (rbdFieldType2.isSelected()) {
                fieldTypeMap.put(selectedField, "MASTER");
            } else if (rbdFieldType3.isSelected()) {
                fieldTypeMap.put(selectedField, "DEPENDENT");
            }
    
            // Actualizar dependencias cuando cambia el tipo
            updateFieldTypeDependencies();
        };

        rbdFieldType1.addActionListener(fieldTypeListener);
        rbdFieldType2.addActionListener(fieldTypeListener);
        rbdFieldType3.addActionListener(fieldTypeListener);
        
        // Listener para chkItem
        chkItem.addActionListener(e -> {
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField != null) {
                boolean isItemSelected = chkItem.isSelected();
                itemOptionByField.put(selectedField, isItemSelected);
        
                // Si se marca chkItem, eliminar cualquier área definida para este campo
                if (isItemSelected) {
                    fieldAreasByPage.remove(selectedField);
                    // Limpiar campos de coordenadas
                    txfPage.setText("");
                    txfAxisX.setText("");
                    txfAxisY.setText("");
                    refreshVisibleSelections();
                }
        
                // Actualizar dependencias cuando cambia chkItem
                updateFieldTypeDependencies();
            }
        });

        // Listener para chkText
        chkText.addActionListener(e -> {
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField != null) {
                textOptionByField.put(selectedField, chkText.isSelected());
            }
            // Actualizar dependencias cuando cambia chkText
            updateFieldTypeDependencies();
        });

        // Listeners para los combobox
        cmbItem.addActionListener(e -> {
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField != null && cmbItem.getSelectedItem() != null) {
                itemTypeByField.put(selectedField, cmbItem.getSelectedItem().toString());
            }
        });

        cmbText.addActionListener(e -> {
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField != null && cmbText.getSelectedItem() != null) {
                textTypeByField.put(selectedField, cmbText.getSelectedItem().toString());
            }
        });

        chkSpaces.addActionListener(e -> {
            String f = lstDataList.getSelectedValue();
            if (f != null) {
                spacesOptionByField.put(f, chkSpaces.isSelected());
            }
        });

        chkSymbols.addActionListener(e -> {
            String f = lstDataList.getSelectedValue();
            if (f != null) {
                symbolsOptionByField.put(f, chkSymbols.isSelected());
            }
        });
        
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
        txfStart.getDocument().addDocumentListener(new DocumentListener() {

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
            public void insertUpdate(DocumentEvent e) {
                updatePageView();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updatePageView();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updatePageView();
            }
        });
        
        // Validación dinámica de txfStart Y txfFinish
        DocumentListener rangoListener = new DocumentListener() {
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

                Color color = valido ? Color.BLACK : Color.RED;
                txfStart.setForeground(color);
                txfFinish.setForeground(color);
            }

            @Override
            public void insertUpdate(DocumentEvent e) { validarRango(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validarRango(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validarRango(); }
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
        btnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGenerateActionPerformed(evt);
            }
        });

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
        } catch (InvalidPasswordException ex) {
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
            } catch (InvalidPasswordException ex2) {
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
            lblDataFormat3.setEnabled(true);
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
    
                // Verificar si este campo está marcado como "sin área"
                Boolean isItemField = itemOptionByField.get(fieldName);
                if (Boolean.TRUE.equals(isItemField)) {
                    JOptionPane.showMessageDialog(this, 
                        "Los campos definidos sin área no pueden tener áreas de PDF asignadas.\n" +
                        "Desmarque la opción 'Definir campo sin área' si desea asignar un área.",
                        "Campo sin área",
                        JOptionPane.WARNING_MESSAGE);
                    return;
                }

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
        JTextField txtNombre = new JTextField(20);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Nombre del campo:"));
        panel.add(txtNombre);

        JOptionPane optionPane = new JOptionPane(
            panel,
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION
        );

        JDialog dialog = optionPane.createDialog(this, "Añadir nuevo campo");

        dialog.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                txtNombre.requestFocusInWindow();
            }
        });

        dialog.setVisible(true);

        Object selectedValue = optionPane.getValue();
        if (selectedValue != null && (int) selectedValue == JOptionPane.OK_OPTION) {
            String nombreCampo = txtNombre.getText().trim();
            if (!nombreCampo.isEmpty()) {
                ListModel<String> lm = lstDataList.getModel();
                DefaultListModel<String> model;
                if (lm instanceof DefaultListModel) {
                    model = (DefaultListModel<String>) lm;
                } else {
                    model = new DefaultListModel<>();
                    lstDataList.setModel(model);
                }

                // Evitar duplicados simples
                if (((DefaultListModel<String>) lstDataList.getModel()).contains(nombreCampo)) {
                    JOptionPane.showMessageDialog(this, "Ese nombre ya existe en la lista.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    model.addElement(nombreCampo);

                    // Inicializar metadatos del campo
                    fieldTypeMap.put(nombreCampo, "UNIQUE");
                    spacesOptionByField.put(nombreCampo, false);
                    symbolsOptionByField.put(nombreCampo, false);
                    itemOptionByField.put(nombreCampo, false);
                    textOptionByField.put(nombreCampo, false);
                    itemTypeByField.put(nombreCampo, "ID");
                    textTypeByField.put(nombreCampo, "MAYÚSCULA");
                    fieldAreasByPage.putIfAbsent(nombreCampo, new LinkedHashMap<>());

                    lstDataList.setSelectedIndex(model.getSize() - 1);

                    // Actualizar cmbMaster si es necesario
                    updateMasterComboBoxIfNeeded();
                }
            }
        }

        dialog.dispose();
    }//GEN-LAST:event_btnAddDataActionPerformed

    private void btnDeleteDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteDataActionPerformed
        ListModel<String> lm = lstDataList.getModel();
        if (!(lm instanceof DefaultListModel)) return;
        DefaultListModel<String> model = (DefaultListModel<String>) lm;

        int index = lstDataList.getSelectedIndex();
        if (index == -1) return;

        String nombreCampo = model.getElementAt(index);

        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que quiere eliminar el item \"" + nombreCampo + "\"?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            // Eliminar del modelo visual
            model.remove(index);

            // Eliminar de todos los mapas asociados
            fieldTypeMap.remove(nombreCampo);
            fieldAreasByPage.remove(nombreCampo);
            spacesOptionByField.remove(nombreCampo);
            symbolsOptionByField.remove(nombreCampo);
            itemOptionByField.remove(nombreCampo);
            textOptionByField.remove(nombreCampo);
            itemTypeByField.remove(nombreCampo);
            textTypeByField.remove(nombreCampo);

            refreshVisibleSelections();

            // Actualizar cmbMaster si es necesario
            updateMasterComboBoxIfNeeded();

            if (!model.isEmpty()) {
                int nuevoIndex = Math.min(index, model.getSize() - 1);
                lstDataList.setSelectedIndex(nuevoIndex);
            } else {
                // Sin selección ni campos
                btnDeleteData.setEnabled(false);
                btnEditData.setEnabled(false);
                btnMoveUpData.setEnabled(false);
                btnMoveDownData.setEnabled(false);
                btnClearListData.setEnabled(false);
            }
        }
    }//GEN-LAST:event_btnDeleteDataActionPerformed

    private void btnClearListDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearListDataActionPerformed
        ListModel<String> lm = lstDataList.getModel();
        if (!(lm instanceof DefaultListModel)) return;

        DefaultListModel<String> model = (DefaultListModel<String>) lm;
        if (model.isEmpty()) return;

        int opcion = JOptionPane.showConfirmDialog(
            this,
            "¿Está seguro de que quiere eliminar todos los ítems de la lista?",
            "Confirmar eliminación",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );

        if (opcion == JOptionPane.YES_OPTION) {
            model.clear();

            // Limpiar todos los mapas asociados a los campos
            fieldTypeMap.clear();
            fieldAreasByPage.clear();
            spacesOptionByField.clear();
            symbolsOptionByField.clear();
            itemOptionByField.clear();
            textOptionByField.clear();
            itemTypeByField.clear();
            textTypeByField.clear();

            refreshVisibleSelections();

            // Actualizar cmbMaster si es necesario
            updateMasterComboBoxIfNeeded();

            btnDeleteData.setEnabled(false);
            btnEditData.setEnabled(false);
            btnMoveUpData.setEnabled(false);
            btnMoveDownData.setEnabled(false);
            btnClearListData.setEnabled(false);
        }
    }//GEN-LAST:event_btnClearListDataActionPerformed

    private void btnEditDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditDataActionPerformed
        int index = lstDataList.getSelectedIndex();
        if (index == -1) return;

        ListModel<String> lm = lstDataList.getModel();
        if (!(lm instanceof DefaultListModel)) return;
        DefaultListModel<String> model = (DefaultListModel<String>) lm;

        String oldName = model.getElementAt(index);

        JTextField txtNombre = new JTextField(oldName, 20);

        JPanel panel = new JPanel();
        panel.add(new JLabel("Editar nombre del campo:"));
        panel.add(txtNombre);

        JOptionPane optionPane = new JOptionPane(
            panel,
            JOptionPane.PLAIN_MESSAGE,
            JOptionPane.OK_CANCEL_OPTION
        );

        JDialog dialog = optionPane.createDialog(this, "Editar campo");

        dialog.addWindowFocusListener(new WindowAdapter() {
            @Override
            public void windowGainedFocus(WindowEvent e) {
                txtNombre.requestFocusInWindow();
                txtNombre.selectAll();
            }
        });

        dialog.setVisible(true);

        Object selectedValue = optionPane.getValue();
        if (selectedValue != null && (int) selectedValue == JOptionPane.OK_OPTION) {
            String newName = txtNombre.getText().trim();
            if (!newName.isEmpty() && !newName.equals(oldName)) {
                // Evitar colisión con otro campo
                DefaultListModel<String> m = (DefaultListModel<String>) lstDataList.getModel();
                if (m.contains(newName)) {
                    JOptionPane.showMessageDialog(this, "Ya existe un campo con ese nombre.", "Aviso", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Actualizar en la lista
                    model.setElementAt(newName, index);
                    lstDataList.setSelectedIndex(index);

                    // Mover tipo
                    String oldType = fieldTypeMap.remove(oldName);
                    if (oldType != null) fieldTypeMap.put(newName, oldType);

                    // Mover opciones por campo
                    Boolean sp = spacesOptionByField.remove(oldName);
                    if (sp != null) spacesOptionByField.put(newName, sp);

                    Boolean sy = symbolsOptionByField.remove(oldName);
                    if (sy != null) symbolsOptionByField.put(newName, sy);

                    // Mover opciones de item y text
                    Boolean itemOpt = itemOptionByField.remove(oldName);
                    if (itemOpt != null) itemOptionByField.put(newName, itemOpt);

                    Boolean textOpt = textOptionByField.remove(oldName);
                    if (textOpt != null) textOptionByField.put(newName, textOpt);

                    String itemType = itemTypeByField.remove(oldName);
                    if (itemType != null) itemTypeByField.put(newName, itemType);

                    String textType = textTypeByField.remove(oldName);
                    if (textType != null) textTypeByField.put(newName, textType);

                    // Mover áreas
                    Map<Integer, Rectangle2D.Double> areas = fieldAreasByPage.remove(oldName);
                    if (areas != null) fieldAreasByPage.put(newName, areas);

                    refreshVisibleSelections();

                    // Actualizar cmbMaster si es necesario
                    updateMasterComboBoxIfNeeded();
                }
            }
        }

        dialog.dispose();
    }//GEN-LAST:event_btnEditDataActionPerformed

    private void btnMoveUpDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveUpDataActionPerformed
        int index = lstDataList.getSelectedIndex();
        if (index <= 0) return;

        DefaultListModel<String> model = (DefaultListModel<String>) lstDataList.getModel();
        String current = model.getElementAt(index);
        String previous = model.getElementAt(index - 1);

        // Reordenar solo en el modelo visual
        model.setElementAt(current, index - 1);
        model.setElementAt(previous, index);

        // Reaplicar selección y asegurar visibilidad
        lstDataList.setSelectedIndex(index - 1);
        lstDataList.ensureIndexIsVisible(index - 1);

        // Actualizar cmbMaster si es necesario (por si el orden afecta a la visualización)
        updateMasterComboBoxIfNeeded();
    }//GEN-LAST:event_btnMoveUpDataActionPerformed

    private void btnMoveDownDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoveDownDataActionPerformed
        int index = lstDataList.getSelectedIndex();
        if (index == -1) return;

        DefaultListModel<String> model = (DefaultListModel<String>) lstDataList.getModel();
        if (index >= model.getSize() - 1) return;

        String current = model.getElementAt(index);
        String next = model.getElementAt(index + 1);

        // Reordenar solo en el modelo visual
        model.setElementAt(next, index);
        model.setElementAt(current, index + 1);

        // Reaplicar selección y asegurar visibilidad
        lstDataList.setSelectedIndex(index + 1);
        lstDataList.ensureIndexIsVisible(index + 1);

        // Actualizar cmbMaster si es necesario (por si el orden afecta a la visualización)
        updateMasterComboBoxIfNeeded();
    }//GEN-LAST:event_btnMoveDownDataActionPerformed

    private void btnValidateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnValidateActionPerformed
        try {
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

            ArrayList<String> fields = new ArrayList<>();
            ListModel<String> model = lstDataList.getModel();
            for (int i = 0; i < model.getSize(); i++) fields.add(model.getElementAt(i));
            if (fields.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No hay campos definidos en la lista.", "Validación", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validación de áreas
            boolean hasFieldsWithArea = false;
            boolean hasInvalidFields = false;
            List<String> invalidFields = new ArrayList<>();

            for (String f : fields) {
                Boolean isItemField = itemOptionByField.get(f);
                if (Boolean.TRUE.equals(isItemField)) {
                    continue;
                }
        
                Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(f);
                if (perPage != null && !perPage.isEmpty()) { 
                    hasFieldsWithArea = true; 
                } else {
                    hasInvalidFields = true;
                    invalidFields.add(f);
                }
            }

            if (!hasFieldsWithArea) {
                JOptionPane.showMessageDialog(this, 
                    "No hay campos con área asignada. Para generar datos, al menos un campo debe tener un área definida o estar marcado como 'campo sin área'.",
                    "Validación", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (hasInvalidFields) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Los siguientes campos requieren un área asignada:\n\n");
                for (String field : invalidFields) {
                    errorMessage.append("• ").append(field).append("\n");
                }
                errorMessage.append("\nPor favor, asigne áreas a estos campos o márquelos como 'campos sin área'.");
        
                JOptionPane.showMessageDialog(this, 
                    errorMessage.toString(),
                    "Campos sin área asignada", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Validar relaciones DEPENDENT-MASTER
            boolean hasInvalidDependencies = false;
            StringBuilder dependencyErrors = new StringBuilder();
    
            for (String field : fields) {
                String fieldType = fieldTypeMap.get(field);
                if ("DEPENDENT".equals(fieldType)) {
                    String masterField = dependentMasterMap.get(field);
                    if (masterField == null || masterField.isEmpty()) {
                        hasInvalidDependencies = true;
                        dependencyErrors.append("• El campo '").append(field).append("' no tiene un campo maestro asignado.\n");
                    } else {
                        String masterFieldType = fieldTypeMap.get(masterField);
                        if (!"MASTER".equals(masterFieldType)) {
                            hasInvalidDependencies = true;
                            dependencyErrors.append("• El campo '").append(masterField).append("' no está definido como campo maestro.\n");
                        }
                    }
                }
            }
    
            if (hasInvalidDependencies) {
                JOptionPane.showMessageDialog(this,
                    "Errores en las dependencias:\n\n" + dependencyErrors.toString() +
                    "\nPor favor, asigne campos maestros válidos a los campos dependientes.",
                    "Dependencias inválidas",
                    JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean struct1 = rdbStructure1.isSelected();
            boolean struct2 = rdbStructure2.isSelected();
            boolean struct3 = rdbStructure3.isSelected();

            extractedData.clear();

            System.out.println("Inicio de la extracción de datos");
            System.out.println("Modo estructura: " + (struct1 ? "Cada dos páginas" : struct2 ? "Cada página" : "Campos diferentes por ambas caras"));
            System.out.println("Rango de páginas: " + startPage + " - " + finishPage);
            System.out.println("--------------------------------------------------");

            BiFunction<Rectangle2D.Double, PDPage, Rectangle2D.Double> toPdfPoints =
                (frac, page) -> {
                    PDRectangle mb = page.getMediaBox();
                    double pw = mb.getWidth();
                    double ph = mb.getHeight();
                    return new Rectangle2D.Double(frac.x * pw, frac.y * ph, frac.width * pw, frac.height * ph);
                };

            int pageStep = (struct1 || struct3) ? 2 : 1;
            int globalRowCounter = 0;
            int pageCounter = 0;

            for (int p = startPage; p <= finishPage; p += pageStep) {
                pageCounter++;
        
                Map<String, String> baseRow = new LinkedHashMap<>();
                for (String f : fields) baseRow.put(f, "");

                Map<String, List<String>> masterItemsByField = new LinkedHashMap<>();
                Map<String, List<String>> dependentItemsByField = new LinkedHashMap<>();
        
                // Procesar campos "sin área" primero
                for (String field : fields) {
                    Boolean isItemField = itemOptionByField.get(field);
                    if (Boolean.TRUE.equals(isItemField)) {
                        String itemType = itemTypeByField.get(field);
                        if ("Fecha".equals(itemType)) {
                            baseRow.put(field, java.time.LocalDate.now().toString());
                        } else if ("Página".equals(itemType)) {
                            baseRow.put(field, String.valueOf(pageCounter));
                        }
                    }
                }
        
                if (struct3) {
                    // Procesar cara delantera (página p)
                    int frontIdx = p - 1;
                    if (frontIdx < totalPages) {
                        PDPage pageFront = pdfDocument.getPage(frontIdx);
                        PDFTextStripperByArea stripperFront = new PDFTextStripperByArea();
                        stripperFront.setSortByPosition(true);

                        // Primero extraer todos los campos
                        for (String field : fields) {
                            Boolean isItemField = itemOptionByField.get(field);
                            if (Boolean.TRUE.equals(isItemField)) continue;
                    
                            Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                            if (perPage == null) continue;
                            Rectangle2D.Double frac = perPage.get(0);
                            if (frac == null) continue;
                            Rectangle2D.Double rectPDF = toPdfPoints.apply(frac, pageFront);
                            stripperFront.addRegion(field, rectPDF);
                        }

                        stripperFront.extractRegions(pageFront);

                        // CORRECCIÓN: Procesar tanto MASTER como DEPENDENT correctamente
                        for (String field : fields) {
                            Boolean isItemField = itemOptionByField.get(field);
                            if (Boolean.TRUE.equals(isItemField)) continue;
                    
                            Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                            if (perPage == null || !perPage.containsKey(0)) continue;

                            String raw = stripperFront.getTextForRegion(field);
                            String cleaned = stripTrailingLineBreaks(raw);

                            String type = fieldTypeMap.getOrDefault(field, "UNIQUE");
                        
                            // CORRECCIÓN: Procesar tanto MASTER como DEPENDENT
                            if ("MASTER".equals(type) || "DEPENDENT".equals(type)) {
                                String[] parts = cleaned.split("\\r?\\n");
                                List<String> items = new ArrayList<>();
                                for (String it : parts) {
                                    String itClean = applyPerFieldOptions(field, it);
                                    if (!itClean.trim().isEmpty()) {
                                        items.add(itClean);
                                    }
                                }
                            
                                if ("MASTER".equals(type)) {
                                    masterItemsByField.put(field, items);
                                } else if ("DEPENDENT".equals(type)) {
                                    // Para DEPENDENT, verificar si tiene un maestro asignado
                                    String masterField = dependentMasterMap.get(field);
                                    if (masterField != null && masterItemsByField.containsKey(masterField)) {
                                        List<String> masterItems = masterItemsByField.get(masterField);
                                        List<String> dependentItems = new ArrayList<>();
                                    
                                        // Sincronizar con el maestro
                                        for (int i = 0; i < masterItems.size(); i++) {
                                            if (i < items.size()) {
                                                dependentItems.add(items.get(i));
                                            } else {
                                                dependentItems.add(""); // Rellenar con vacío si faltan items
                                            }
                                        }
                                        dependentItemsByField.put(field, dependentItems);
                                    } else {
                                        // Si no tiene maestro, usar los items tal cual
                                        dependentItemsByField.put(field, items);
                                    }
                                }
                            } else if ("UNIQUE".equals(type)) {
                                String val = applyPerFieldOptions(field, cleaned);
                                baseRow.put(field, val);
                            }
                        }
                    }

                    // CORRECCIÓN: Completar la implementación de la cara trasera para struct3
                    int backLogical = p + 1;
                    int backIdx = backLogical - 1;
                    if (backLogical <= finishPage && backIdx < totalPages) {
                        PDPage pageBack = pdfDocument.getPage(backIdx);
                        PDFTextStripperByArea stripperBack = new PDFTextStripperByArea();
                        stripperBack.setSortByPosition(true);

                        // Extraer campos de la cara trasera
                        for (String field : fields) {
                            Boolean isItemField = itemOptionByField.get(field);
                            if (Boolean.TRUE.equals(isItemField)) continue;
                        
                            Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                            if (perPage == null) continue;
                            Rectangle2D.Double frac = perPage.get(1); // Página 1 para cara trasera
                            if (frac == null) continue;
                            Rectangle2D.Double rectPDF = toPdfPoints.apply(frac, pageBack);
                            stripperBack.addRegion(field, rectPDF);
                        }

                        stripperBack.extractRegions(pageBack);

                        // Procesar campos de la cara trasera (lógica similar a la cara delantera)
                        for (String field : fields) {
                            Boolean isItemField = itemOptionByField.get(field);
                            if (Boolean.TRUE.equals(isItemField)) continue;
                        
                            Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                            if (perPage == null || !perPage.containsKey(1)) continue;

                            String raw = stripperBack.getTextForRegion(field);
                            String cleaned = stripTrailingLineBreaks(raw);

                            String type = fieldTypeMap.getOrDefault(field, "UNIQUE");
                        
                            if ("MASTER".equals(type) || "DEPENDENT".equals(type)) {
                                String[] parts = cleaned.split("\\r?\\n");
                                List<String> items = new ArrayList<>();
                                for (String it : parts) {
                                    String itClean = applyPerFieldOptions(field, it);
                                    if (!itClean.trim().isEmpty()) {
                                        items.add(itClean);
                                    }
                                }
                            
                                if ("MASTER".equals(type)) {
                                    // Combinar con items de la cara delantera si existen
                                    List<String> existingItems = masterItemsByField.get(field);
                                    if (existingItems != null) {
                                        existingItems.addAll(items);
                                    } else {
                                        masterItemsByField.put(field, items);
                                    }
                                } else if ("DEPENDENT".equals(type)) {
                                    // Lógica similar para DEPENDENT en cara trasera
                                    String masterField = dependentMasterMap.get(field);
                                    if (masterField != null && masterItemsByField.containsKey(masterField)) {
                                        List<String> masterItems = masterItemsByField.get(masterField);
                                        List<String> dependentItems = dependentItemsByField.getOrDefault(field, new ArrayList<>());
                                    
                                        // Sincronizar con el maestro
                                        for (int i = 0; i < masterItems.size(); i++) {
                                            if (i < items.size()) {
                                                if (i < dependentItems.size()) {
                                                    // Si ya existe un valor, combinarlo
                                                    String existing = dependentItems.get(i);
                                                    if (!existing.isEmpty()) {
                                                        dependentItems.set(i, existing + "\n" + items.get(i));
                                                    } else {
                                                        dependentItems.set(i, items.get(i));
                                                    }
                                                } else {
                                                    // Si no existe, añadirlo
                                                    dependentItems.add(items.get(i));
                                                }
                                            }
                                        }
                                        dependentItemsByField.put(field, dependentItems);
                                    } else {
                                        // Si no tiene maestro, usar los items tal cual
                                        List<String> existingItems = dependentItemsByField.get(field);
                                        if (existingItems != null) {
                                            existingItems.addAll(items);
                                        } else {
                                            dependentItemsByField.put(field, items);
                                        }
                                    }
                                }
                            } else if ("UNIQUE".equals(type)) {
                                String existing = baseRow.getOrDefault(field, "");
                                String val = applyPerFieldOptions(field, cleaned);
                                if (!val.isEmpty()) {
                                    if (existing.isEmpty()) {
                                        baseRow.put(field, val);
                                    } else {
                                        baseRow.put(field, existing + "\n" + val);
                                    }
                                }
                            }
                        }
                    }

                } else {
                    // Modo struct1 o struct2 - una sola página por iteración
                    int pageIndex = p - 1;
                    if (pageIndex >= totalPages) continue;
                    PDPage page = pdfDocument.getPage(pageIndex);

                    PDFTextStripperByArea stripper = new PDFTextStripperByArea();
                    stripper.setSortByPosition(true);

                    // Primero extraer todos los campos
                    for (String field : fields) {
                        Boolean isItemField = itemOptionByField.get(field);
                        if (Boolean.TRUE.equals(isItemField)) continue;
                
                        Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                        if (perPage == null) continue;
                        Rectangle2D.Double frac = perPage.get(0);
                        if (frac == null) continue;
                        Rectangle2D.Double rectPDF = toPdfPoints.apply(frac, page);
                        stripper.addRegion(field, rectPDF);
                    }

                    stripper.extractRegions(page);

                    // CORRECCIÓN: Procesar tanto MASTER como DEPENDENT correctamente
                    for (String field : fields) {
                        Boolean isItemField = itemOptionByField.get(field);
                        if (Boolean.TRUE.equals(isItemField)) continue;
                
                        Map<Integer, Rectangle2D.Double> perPage = fieldAreasByPage.get(field);
                        if (perPage == null || !perPage.containsKey(0)) continue;

                        String raw = stripper.getTextForRegion(field);
                        String cleaned = stripTrailingLineBreaks(raw);

                        String type = fieldTypeMap.getOrDefault(field, "UNIQUE");
                    
                        // CORRECCIÓN: Procesar tanto MASTER como DEPENDENT
                        if ("MASTER".equals(type) || "DEPENDENT".equals(type)) {
                            String[] parts = cleaned.split("\\r?\\n");
                            List<String> items = new ArrayList<>();
                            for (String it : parts) {
                                String itClean = applyPerFieldOptions(field, it);
                                if (!itClean.trim().isEmpty()) {
                                    items.add(itClean);
                                }
                            }
                        
                            if ("MASTER".equals(type)) {
                                masterItemsByField.put(field, items);
                            } else if ("DEPENDENT".equals(type)) {
                                // Para DEPENDENT, verificar si tiene un maestro asignado
                                String masterField = dependentMasterMap.get(field);
                                if (masterField != null && masterItemsByField.containsKey(masterField)) {
                                    List<String> masterItems = masterItemsByField.get(masterField);
                                    List<String> dependentItems = new ArrayList<>();
                                
                                    // Sincronizar con el maestro
                                    for (int i = 0; i < masterItems.size(); i++) {
                                        if (i < items.size()) {
                                            dependentItems.add(items.get(i));
                                        } else {
                                            dependentItems.add(""); // Rellenar con vacío si faltan items
                                        }
                                    }
                                    dependentItemsByField.put(field, dependentItems);
                                } else {
                                    // Si no tiene maestro, usar los items tal cual
                                    dependentItemsByField.put(field, items);
                                }
                            }
                        } else if ("UNIQUE".equals(type)) {
                            String val = applyPerFieldOptions(field, cleaned);
                            baseRow.put(field, val);
                        }
                    }
                }

                // CORRECCIÓN: Determinar el número máximo de filas basado en campos MASTER Y DEPENDENT
                int maxRows = 0;
                for (List<String> items : masterItemsByField.values()) {
                    if (items != null && items.size() > maxRows) maxRows = items.size();
                }

                // CORRECCIÓN: También considerar campos DEPENDENT para determinar maxRows
                for (List<String> items : dependentItemsByField.values()) {
                    if (items != null && items.size() > maxRows) maxRows = items.size();
                }

                // Si no hay campos MASTER ni DEPENDENT, crear una sola fila
                if (maxRows <= 0) {
                    boolean anyValue = baseRow.values().stream().anyMatch(v -> v != null && !v.isEmpty());
                    if (anyValue) {
                        Map<String, String> finalRow = new LinkedHashMap<>(baseRow);
                        for (String field : fields) {
                            Boolean isItemField = itemOptionByField.get(field);
                            if (Boolean.TRUE.equals(isItemField)) {
                                String itemType = itemTypeByField.get(field);
                                if ("ID".equals(itemType)) {
                                    globalRowCounter++;
                                    finalRow.put(field, String.valueOf(globalRowCounter));
                                }
                            }
                        }
                
                        extractedData.add(finalRow);
                    
                        // Logging
                        StringBuilder line = new StringBuilder();
                        for (int i = 0; i < fields.size(); i++) {
                            if (i > 0) line.append(", ");
                            line.append(finalRow.getOrDefault(fields.get(i), ""));
                        }
                        System.out.println("Fila única: " + line.toString());
                    }
                } else {
                    // CORRECCIÓN COMPLETA: Crear múltiples filas basadas en campos MASTER y DEPENDENT
                    for (int r = 0; r < maxRows; r++) {
                        Map<String, String> newRow = new LinkedHashMap<>();
                    
                        // Inicializar todos los campos con valores base primero
                        for (String field : fields) {
                            // Para campos UNIQUE, usar el valor base
                            String fieldType = fieldTypeMap.getOrDefault(field, "UNIQUE");
                            if ("UNIQUE".equals(fieldType)) {
                                String baseValue = baseRow.get(field);
                                newRow.put(field, baseValue != null ? baseValue : "");
                            } else {
                                newRow.put(field, "");
                            }
                        }
                    
                        // Campos automáticos (sobrescriben si es necesario)
                        for (String field : fields) {
                            Boolean isItemField = itemOptionByField.get(field);
                            if (Boolean.TRUE.equals(isItemField)) {
                                String itemType = itemTypeByField.get(field);
                                if ("ID".equals(itemType)) {
                                    globalRowCounter++;
                                    newRow.put(field, String.valueOf(globalRowCounter));
                                } else if ("Fecha".equals(itemType)) {
                                    newRow.put(field, java.time.LocalDate.now().toString());
                                } else if ("Página".equals(itemType)) {
                                    newRow.put(field, String.valueOf(pageCounter));
                                }
                            }
                        }
                    
                        // CORRECCIÓN CLAVE: Campos MASTER (sobrescriben valores existentes)
                        for (String mf : masterItemsByField.keySet()) {
                            List<String> items = masterItemsByField.get(mf);
                            String itemVal = (items != null && r < items.size()) ? items.get(r) : "";
                            newRow.put(mf, itemVal);
                        }
                    
                        // CORRECCIÓN CLAVE: Campos DEPENDENT (sobrescriben valores existentes)
                        for (String df : dependentItemsByField.keySet()) {
                            List<String> items = dependentItemsByField.get(df);
                            String itemVal = (items != null && r < items.size()) ? items.get(r) : "";
                            newRow.put(df, itemVal);
                        }
                    
                        // Solo añadir fila si tiene al menos un dato
                        boolean hasData = newRow.values().stream().anyMatch(v -> v != null && !v.trim().isEmpty());
                        if (hasData) {
                            extractedData.add(newRow);
                        
                            // Logging detallado para debug
                            System.out.println("=== FILA " + r + " ===");
                            for (String field : fields) {
                                String value = newRow.getOrDefault(field, "");
                                String fieldType = fieldTypeMap.getOrDefault(field, "UNIQUE");
                                System.out.println(field + " (" + fieldType + "): '" + value + "'");
                            }
                            System.out.println("=================");
                        }
                    }
                }
            }

            System.out.println("Fin del recorrido del documento. Total filas: " + extractedData.size());
            JOptionPane.showMessageDialog(this, 
                "Validación completada. Se extrajeron " + extractedData.size() + " filas de datos.", 
                "Validación completada", 
                JOptionPane.INFORMATION_MESSAGE);
            btnGenerate.setEnabled(true);
            
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Se produjo un error durante la validación: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnValidateActionPerformed

    private void btnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGenerateActionPerformed
        try {
            // Encabezados: exactamente los visibles en la lista (mismo orden)
            ArrayList<String> headers = new ArrayList<>();
            ListModel<String> lm = lstDataList.getModel();
            for (int i = 0; i < lm.getSize(); i++) headers.add(lm.getElementAt(i));

            // Ruta del PDF (deriva nombre y carpeta del XLSX)
            String pdfPath = PDFLink; // asumimos que está establecida tras la carga del PDF
            File pdfFile = new File(pdfPath);
            String baseName = pdfFile.getName().replaceFirst("(?i)\\.pdf$", "");
            File outXlsx = new File(pdfFile.getParentFile(), baseName + ".xlsx");

            // Nombre de hoja: “mismo que el PDF con .xlsx”
            String sheetName = (baseName + ".xlsx").replaceAll("[\\\\/?*\\[\\]:]", "_");
            if (sheetName.length() > 31) sheetName = sheetName.substring(0, 31);

            // Crear y escribir XLSX
            try (Workbook wb = new XSSFWorkbook(); FileOutputStream fos = new FileOutputStream(outXlsx)) {

                Sheet sheet = wb.createSheet(sheetName);

                // Encabezados
                org.apache.poi.ss.usermodel.Row headerRow = sheet.createRow(0);
                for (int c = 0; c < headers.size(); c++) {
                    org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(c);
                    cell.setCellValue(headers.get(c));
                }

                // Filas de datos (tal cual texto)
                for (int r = 0; r < extractedData.size(); r++) {
                    Map<String, String> map = extractedData.get(r);
                    Row row = sheet.createRow(r + 1);
                    for (int c = 0; c < headers.size(); c++) {
                        String key = headers.get(c);
                        String val = map.getOrDefault(key, "");
                        row.createCell(c).setCellValue(val);
                    }
                }

                // Autoajuste
                for (int c = 0; c < headers.size(); c++) sheet.autoSizeColumn(c);

                wb.write(fos);
            }

            JOptionPane.showMessageDialog(
                this,
                "Excel generado correctamente en:\n" + outXlsx.getAbsolutePath(),
                "Generación completada",
                JOptionPane.INFORMATION_MESSAGE
            );

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(
                this,
                "Error al generar el Excel: " + ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_btnGenerateActionPerformed

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
    
    private static List<String> splitLinesByNewline(String text) {
        if (text == null) return Collections.emptyList();
        String[] raw = text.split("\\R");
        List<String> out = new ArrayList<>();
        for (String s : raw) {
            String t = s == null ? "" : s.trim();
            if (!t.isEmpty()) out.add(t);
        }
        return out;
    }
    
    private String removeSpacesKeepNewlines(String s) {
        if (s == null) return "";
        String v = s.replace("\r", "");
        v = v.replace(" ", "");
        v = v.replace("\u00A0", "");
        return v;
    }

    private String keepDigitsAndNewlines(String s) {
        if (s == null) return "";
        String v = s.replace("\r", "");
        v = v.replaceAll("[^0-9\\n]", "");
        return v;
    }

    // Aplica opciones según el campo
    private String applyTextOptions(String fieldName, String s) {
        if (s == null) return "";
        String v = s.replace("\r", "");

        boolean spacesOn  = Boolean.TRUE.equals(spacesOptionByField.get(fieldName));
        boolean symbolsOn = Boolean.TRUE.equals(symbolsOptionByField.get(fieldName));

        if (spacesOn)  v = removeSpacesKeepNewlines(v);
        if (symbolsOn) v = keepDigitsAndNewlines(v);

        return v;
    }
    
    private String stripTrailingLineBreaks(String s) {
        if (s == null) return "";
        int end = s.length();
        boolean sawLineBreak = false;

        while (end > 0) {
            char c = s.charAt(end - 1);
            if (c == '\n' || c == '\r') {
                sawLineBreak = true;
                end--;
                continue;
            }
            if (sawLineBreak && (c == ' ' || c == '\t' || c == '\u00A0')) { // espacios, tab y NBSP
                end--;
                continue;
            }
            // elimina también BOM/ZWSP si quedaran al final
            if (!sawLineBreak && (c == '\u200B' || c == '\uFEFF')) {
                end--;
                continue;
            }
            break;
        }
        return s.substring(0, end);
    }

    // Aplica las opciones por campo sin tocar los saltos de línea internos
    private String applyPerFieldOptions(String field, String text) {
        if (text == null) return "";
    
        // Primero aplicar limpieza básica
        String out = text.trim();
    
        // Aplicar opciones de espacios y símbolos
        Boolean rmSpaces  = spacesOptionByField.get(field);
        Boolean onlyNums  = symbolsOptionByField.get(field);
    
        if (Boolean.TRUE.equals(rmSpaces)) {
            out = out.replace(" ", "");
        }
        if (Boolean.TRUE.equals(onlyNums)) {
            out = out.replaceAll("[^0-9]", "");
        }
    
        // Aplicar normalización de texto si está activada
        Boolean textNormalization = textOptionByField.get(field);
        if (Boolean.TRUE.equals(textNormalization)) {
            String textType = textTypeByField.get(field);
            if (textType != null) {
                out = applyTextNormalization(out, textType);
            }
        }
    
        return out;
    }
    
    // Aplica normalización de texto según el tipo seleccionado
    private String applyTextNormalization(String text, String textType) {
        if (text == null || text.isEmpty()) return text;
    
        switch (textType) {
            case "MAYÚSCULA":
                return text.toUpperCase();
            
            case "minúscula":
                return text.toLowerCase();
            
            case "Título":
                return toTitleCase(text);
            
            default:
                return text;
        }
    }

    // Convierte texto a formato Título (primera letra mayúscula, resto minúsculas)
    private String toTitleCase(String text) {
        if (text == null || text.isEmpty()) return text;
    
        // Si el texto tiene múltiples líneas, procesar cada línea por separado
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

    // Convierte una sola línea a formato Título
    private String toTitleCaseSingleLine(String line) {
        if (line == null || line.isEmpty()) return line;
    
        // Convertir todo a minúsculas primero
        String lowerCase = line.toLowerCase();
    
        // Capitalizar primera letra
        if (lowerCase.length() == 1) {
            return lowerCase.toUpperCase();
        } else {
            return Character.toUpperCase(lowerCase.charAt(0)) + lowerCase.substring(1);
        }
    }
    
    private void updateFieldTypeDependencies() {
        boolean hasSelection = lstDataList.getSelectedIndex() != -1;
        boolean isUniqueType = rbdFieldType1.isSelected();
        boolean isDependentType = rbdFieldType3.isSelected();

        // chkItem solo se habilita si hay selección y es tipo UNIQUE
        chkItem.setEnabled(hasSelection && isUniqueType);

        // Si chkItem se deshabilita, se deselecciona
        if (!chkItem.isEnabled() && chkItem.isSelected()) {
            chkItem.setSelected(false);
            // También limpiar el estado guardado
            String selectedField = lstDataList.getSelectedValue();
            if (selectedField != null) {
                itemOptionByField.put(selectedField, false);
            }
        }

        // chkText se habilita si hay selección y chkItem NO está seleccionado
        chkText.setEnabled(hasSelection && !chkItem.isSelected());

        // Si chkText se deshabilita, se deselecciona
        if (!chkText.isEnabled() && chkText.isSelected()) {
            chkText.setSelected(false);
        }

        // cmbItem se habilita si chkItem está habilitado y seleccionado
        cmbItem.setEnabled(chkItem.isEnabled() && chkItem.isSelected());

        // cmbText se habilita si chkText está habilitado y seleccionado
        cmbText.setEnabled(chkText.isEnabled() && chkText.isSelected());

        // lblMaster y cmbMaster se habilitan solo si hay selección y es tipo DEPENDENT
        boolean masterEnabled = hasSelection && isDependentType;
        lblMaster.setEnabled(masterEnabled);
        cmbMaster.setEnabled(masterEnabled);

        // Si cmbMaster está habilitado, actualizar su contenido con campos maestros
        if (masterEnabled) {
            updateMasterComboBox();
        }
    }
    
    // Actualiza el combobox de campos maestros con los campos marcados como MASTER
    private void updateMasterComboBox() {
        cmbMaster.removeAllItems();
    
        ListModel<String> model = lstDataList.getModel();
        for (int i = 0; i < model.getSize(); i++) {
            String fieldName = model.getElementAt(i);
            String fieldType = fieldTypeMap.get(fieldName);
        
            // Solo incluir campos marcados como MASTER (rdbFieldType2)
            if ("MASTER".equals(fieldType)) {
                cmbMaster.addItem(fieldName);
            }
        }
    
        // Si no hay campos maestros, mostrar un mensaje en el combobox
        if (cmbMaster.getItemCount() == 0) {
            cmbMaster.addItem("No hay campos maestros definidos");
            cmbMaster.setEnabled(false);
        } else {
            cmbMaster.setEnabled(true);
        }
    }

    // Genera valores automáticos para campos sin área
    private String generateAutoValue(String itemType, int currentPage, int startPage, int rowIndex) {
        if (itemType == null) return "";
    
        switch (itemType) {
            case "ID":
                // Valor incremental por fila (1, 2, 3, ...)
                return String.valueOf(rowIndex);
            
            case "Fecha":
                // Fecha actual
                return java.time.LocalDate.now().toString();
            
            case "Página":
                // Número de página (relativo al inicio del escaneo)
                // Si startPage = 3 y currentPage = 5, entonces página = 3
                int pageNumber = currentPage - startPage + 1;
                return String.valueOf(pageNumber);
            
            default:
                return "";
        }
    }

    // Actualiza cmbMaster solo si está habilitado (es decir, si hay un campo DEPENDENT seleccionado)
    private void updateMasterComboBoxIfNeeded() {
        if (cmbMaster.isEnabled()) {
            updateMasterComboBox();
        }
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
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
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
        EventQueue.invokeLater(new Runnable() {
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
