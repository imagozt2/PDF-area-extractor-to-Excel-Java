package pdfareaextractortoexcel.services;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.encryption.InvalidPasswordException;
import org.apache.pdfbox.rendering.PDFRenderer;
import pdfareaextractortoexcel.view.PDFPagePanel;
import pdfareaextractortoexcel.app.PDFAreaExtractorApp;
import pdfareaextractortoexcel.model.ExtractionSettings;
import pdfareaextractortoexcel.model.FieldConfig;
import pdfareaextractortoexcel.model.FieldType;
import pdfareaextractortoexcel.model.ItemType;
import pdfareaextractortoexcel.model.PageScanMode;
import pdfareaextractortoexcel.model.StructureMode;
import pdfareaextractortoexcel.model.TextCaseType;


public class MainController {

    private final PDFAreaExtractorApp view;

    private final FieldManager fieldManager;
    private final AreaSelectionManager areaSelectionManager;
    private final ExtractionSettings extractionSettings;
    private final ExtractionEngine extractionEngine;
    private final ExcelExporter excelExporter;
    private final PDFTextProcessor textProcessor;

    private final ButtonGroup btnGroupDataFormat;
    private final ButtonGroup btnGroupStructure;
    private final ButtonGroup btnGroupScanner;
    private final ButtonGroup btnGroupPages;

    private final List<Map<String, String>> extractedData = new ArrayList<>();

    private PDDocument pdfDocument;
    private PDFPagePanel pagePanel;
    private String pdfPath;

    
    public MainController(PDFAreaExtractorApp view) {
        
        this.view = view;

        this.fieldManager = new FieldManager();
        this.textProcessor = new PDFTextProcessor();
        this.areaSelectionManager = new AreaSelectionManager(fieldManager);
        this.extractionSettings = new ExtractionSettings();
        this.extractionEngine = new ExtractionEngine(textProcessor);
        this.excelExporter = new ExcelExporter();

        this.btnGroupDataFormat = new ButtonGroup();
        this.btnGroupStructure = new ButtonGroup();
        this.btnGroupScanner = new ButtonGroup();
        this.btnGroupPages = new ButtonGroup();
    }

    
    public void initialize() {
        
        setupInitialUIState();
        
        this.pagePanel = view.getPagePanel();
        this.pdfDocument = view.getPdfDocument();
        this.pdfPath = view.getPdfPath();

        configureAreaSelectionManager();

        setupButtonGroups();

        setupComboBoxes();

        setupFieldListSelectionListener();
        setupFieldButtonsListeners();
        setupFieldOptionsListeners();
        setupStructureModeListeners();
        setupPageScannerListeners();
        setupPageToggleListeners();
        setupZoomSliderListener();
        setupValidateAndGenerateListeners();
        setupFileSelectorListener();
    }

    
    private void setupInitialUIState() {
        
        if (view.getSplMain() != null) {
            view.getSplMain().setResizeWeight(0.5);
            view.getSplMain().setEnabled(false);
            SwingUtilities.invokeLater(() -> view.getSplMain().setDividerLocation(0.5));
        }
        if (view.getSplRightPanel() != null) {
            view.getSplRightPanel().setEnabled(false);
        }

        view.getTglPag1().setEnabled(false);
        view.getTglPag2().setEnabled(false);
        view.getSldRightPanel().setEnabled(false);

        view.getTxfLink().setEditable(false);
        view.getTxfLink().setFocusable(false);
        view.getTxfLink().setText("");

        view.getLblStructure().setEnabled(false);
        view.getRdbStructure1().setEnabled(false);
        view.getRdbStructure2().setEnabled(false);
        view.getRdbStructure3().setEnabled(false);

        view.getImgPage1().setEnabled(false);
        view.getImgPage2().setEnabled(false);
        view.getImgPage3().setEnabled(false);
        view.getImgPage4().setEnabled(false);
        view.getImgPage5().setEnabled(false);

        view.getLblPagesScanner().setEnabled(false);
        view.getLblPageStart().setEnabled(false);
        view.getLblPageFinish().setEnabled(false);

        view.getRbdPagesScanner1().setEnabled(false);
        view.getRbdPagesScanner2().setEnabled(false);

        view.getTxfStart().setEditable(false);
        view.getTxfFinish().setEditable(false);
        view.getTxfStart().setFocusable(false);
        view.getTxfFinish().setFocusable(false);
        view.getTxfStart().setEnabled(false);
        view.getTxfFinish().setEnabled(false);
        view.getTxfStart().setText("");
        view.getTxfFinish().setText("");

        view.getLblData().setEnabled(false);
        view.getLblDataList().setEnabled(false);

        view.getLstDataList().setEnabled(false);
        view.getBtnAddData().setEnabled(false);
        view.getBtnDeleteData().setEnabled(false);
        view.getBtnClearListData().setEnabled(false);
        view.getBtnEditData().setEnabled(false);
        view.getBtnMoveUpData().setEnabled(false);
        view.getBtnMoveDownData().setEnabled(false);

        view.getLstDataList().setModel(new DefaultListModel<>());

        view.getLblDataFormat1().setEnabled(false);
        view.getRbdFieldType1().setEnabled(false);
        view.getRbdFieldType2().setEnabled(false);
        view.getRbdFieldType3().setEnabled(false);

        view.getLblDataFormat2().setEnabled(false);
        view.getLblPage().setEnabled(false);
        view.getLblAxisX().setEnabled(false);
        view.getLblAxisY().setEnabled(false);

        view.getTxfPage().setEnabled(false);
        view.getTxfAxisX().setEnabled(false);
        view.getTxfAxisY().setEnabled(false);

        view.getTxfPage().setEditable(false);
        view.getTxfAxisX().setEditable(false);
        view.getTxfAxisY().setEditable(false);

        view.getTxfPage().setFocusable(false);
        view.getTxfAxisX().setFocusable(false);
        view.getTxfAxisY().setFocusable(false);

        view.getTxfPage().setText("");
        view.getTxfAxisX().setText("");
        view.getTxfAxisY().setText("");

        view.getLblDataFormat3().setEnabled(false);
        view.getLblMaster().setEnabled(false);

        view.getChkItem().setEnabled(false);
        view.getChkText().setEnabled(false);
        view.getChkSpaces().setEnabled(false);
        view.getChkSymbols().setEnabled(false);

        view.getChkItem().setSelected(false);
        view.getChkText().setSelected(false);
        view.getChkSpaces().setSelected(false);
        view.getChkSymbols().setSelected(false);

        view.getCmbMaster().setEnabled(false);
        view.getCmbItem().setEnabled(false);
        view.getCmbText().setEnabled(false);

        view.getBtnValidate().setEnabled(false);
        view.getBtnGenerate().setEnabled(false);

        PDFPagePanel panel = view.getPagePanel();
        if (panel != null) {
            panel.setZoom(1.0);
            panel.setCanSelect(false);
        }

        clearFieldOptionsUI();
    }


    private void setupButtonGroups() {
        
        btnGroupDataFormat.add(view.getRbdFieldType1());
        btnGroupDataFormat.add(view.getRbdFieldType2());
        btnGroupDataFormat.add(view.getRbdFieldType3());
        
        btnGroupStructure.add(view.getRdbStructure1());
        btnGroupStructure.add(view.getRdbStructure2());
        btnGroupStructure.add(view.getRdbStructure3());
        
        btnGroupScanner.add(view.getRbdPagesScanner1());
        btnGroupScanner.add(view.getRbdPagesScanner2());
        
        btnGroupPages.add(view.getTglPag1());
        btnGroupPages.add(view.getTglPag2());
    }

    
    private void setupComboBoxes() {
        
        DefaultComboBoxModel<String> itemModel = new DefaultComboBoxModel<>();
        itemModel.addElement(ItemType.ID.name());
        itemModel.addElement(ItemType.DATE.name());
        itemModel.addElement(ItemType.PAGE.name());
        view.getCmbItem().setModel(itemModel);
        view.getCmbItem().setSelectedItem(ItemType.ID.name());

        DefaultComboBoxModel<String> textModel = new DefaultComboBoxModel<>();
        textModel.addElement(TextCaseType.UPPERCASE.name());
        textModel.addElement(TextCaseType.LOWERCASE.name());
        textModel.addElement(TextCaseType.TITLE.name());
        view.getCmbText().setModel(textModel);
        view.getCmbText().setSelectedItem(TextCaseType.UPPERCASE.name());

        view.getCmbMaster().removeAllItems();
    }


    private void setupFileSelectorListener() {
        
        view.getBtnSelector().addActionListener(evt -> {
            
            JFileChooser chooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("Archivos PDF", "pdf");
            chooser.setFileFilter(filter);
            chooser.setAcceptAllFileFilterUsed(false);

            int result = chooser.showOpenDialog(view);
            if (result != JFileChooser.APPROVE_OPTION) {
                return;
            }

            File archivo = chooser.getSelectedFile();
            String path = archivo.getAbsolutePath();

            view.setPdfPath(path);
            view.getTxfLink().setText(path);

            PDDocument doc = null;
            try {
                try {
                    doc = Loader.loadPDF(archivo);
                } catch (InvalidPasswordException ex) {
                    String password = JOptionPane.showInputDialog(
                        view,
                        "Este archivo PDF está protegido con contraseña.\nPor favor, introdúcela:",
                        "Contraseña requerida",
                        JOptionPane.QUESTION_MESSAGE
                    );
                    if (password == null) {
                        JOptionPane.showMessageDialog(
                            view,
                            "No se cargó el PDF porque no se proporcionó la contraseña.",
                            "Operación cancelada",
                            JOptionPane.WARNING_MESSAGE
                        );
                        return;
                    }
                    doc = Loader.loadPDF(archivo, password);
                }
            } catch (InvalidPasswordException ex2) {
                JOptionPane.showMessageDialog(
                    view,
                    "Contraseña incorrecta. No se pudo abrir el PDF.",
                    "Error de Contraseña",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(
                    view,
                    "Error al cargar o renderizar el PDF:\n" + e.getMessage(),
                    "Error interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            this.pdfDocument = doc;
            this.pdfPath = path;
            view.setPdfDocument(doc);

            try {
                PDFRenderer renderer = new PDFRenderer(pdfDocument);
                BufferedImage img = renderer.renderImageWithDPI(0, 150);

                PDFPagePanel panel = new PDFPagePanel(img);
                panel.setCanSelect(true);
                panel.setCurrentPage(0);

                view.setPagePanel(panel);
                this.pagePanel = panel;

                view.getSplMain().setDividerLocation(0.5);

                SwingUtilities.invokeLater(() -> {
                    Dimension vp = view.getScrPdfViewer().getViewport().getExtentSize();
                    if (vp.width > 0) {
                        double initZoom = vp.width / (double) img.getWidth();
                        pagePanel.setZoom(initZoom);

                        JSlider sld = view.getSldRightPanel();
                        sld.setMinimum(50);
                        sld.setMaximum(200);
                        sld.setValue((int) (initZoom * 100));
                    }
                });

                view.getScrPdfViewer().revalidate();
                view.getScrPdfViewer().repaint();

                updatePdfContext(pdfDocument, pdfPath, pagePanel);

                enableUIAfterPDFLoad();

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    view,
                    "Error al renderizar la primera página del PDF:\n" + ex.getMessage(),
                    "Error de renderizado",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    
    private void enableUIAfterPDFLoad() {
        
        view.getLblStructure().setEnabled(true);
        view.getRdbStructure1().setEnabled(true);
        view.getRdbStructure2().setEnabled(true);
        view.getRdbStructure3().setEnabled(true);

        view.getImgPage1().setEnabled(true);
        view.getImgPage2().setEnabled(true);
        view.getImgPage3().setEnabled(true);
        view.getImgPage4().setEnabled(true);
        view.getImgPage5().setEnabled(true);

        view.getLblPagesScanner().setEnabled(true);
        view.getLblPageStart().setEnabled(true);
        view.getLblPageFinish().setEnabled(true);

        view.getRbdPagesScanner1().setEnabled(true);
        view.getRbdPagesScanner2().setEnabled(true);

        view.getLblData().setEnabled(true);

        view.getLblDataList().setEnabled(true);
        view.getLstDataList().setEnabled(true);
        view.getBtnAddData().setEnabled(true);

        view.getLblDataFormat1().setEnabled(true);
        
        view.getRbdFieldType1().setEnabled(false);
        view.getRbdFieldType2().setEnabled(false);
        view.getRbdFieldType3().setEnabled(false);

        view.getLblDataFormat2().setEnabled(true);

        view.getLblPage().setEnabled(false);
        view.getLblAxisX().setEnabled(false);
        view.getLblAxisY().setEnabled(false);

        view.getTxfPage().setEnabled(false);
        view.getTxfAxisX().setEnabled(false);
        view.getTxfAxisY().setEnabled(false);

        view.getLblDataFormat3().setEnabled(true);
        
        view.getTglPag1().setEnabled(true);
        view.getSldRightPanel().setEnabled(true);

        view.getRdbStructure1().setSelected(true);
        view.getRbdPagesScanner1().setSelected(true);
        view.getTglPag1().setSelected(true);
        view.getTglPag2().setSelected(false);
    }


    private void configureAreaSelectionManager() {
        
        if (pagePanel == null) {
            return;
        }

        pagePanel.setCanSelect(true);

        pagePanel.setOnAreaSelected(rect -> {
            
            String selectedFieldName = view.getLstDataList().getSelectedValue();
            if (selectedFieldName == null) {
                JOptionPane.showMessageDialog(
                    view,
                    "Por favor, seleccione un campo en la lista antes de definir un área.",
                    "Campo no seleccionado",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            FieldConfig config = fieldManager.getFieldConfig(selectedFieldName);
            if (config == null) {
                JOptionPane.showMessageDialog(
                    view,
                    "El campo seleccionado no existe en la configuración interna.",
                    "Error interno",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (config.isItemField()) {
                JOptionPane.showMessageDialog(
                    view,
                    "Los campos definidos sin área no pueden tener áreas de PDF asignadas.\n"
                        + "Desmarcar la opción 'Definir campo sin área' si se desea asignar un área.",
                    "Campo sin área",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (pdfDocument == null) {
                JOptionPane.showMessageDialog(
                    view,
                    "No hay ningún documento PDF cargado.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            int pageIndex = pagePanel.getCurrentPage();
            double zoom = pagePanel.getZoom();
            int imageWidth = pagePanel.getImageWidth();
            int imageHeight = pagePanel.getImageHeight();

            Rectangle2D.Double relativeRect = areaSelectionManager.saveSelection(
                selectedFieldName,
                pageIndex,
                rect,
                zoom,
                imageWidth,
                imageHeight
            );

            view.getTxfPage().setText(String.valueOf(pageIndex + 1));
            view.getTxfAxisX().setText(String.format("%.4f", relativeRect.x));
            view.getTxfAxisY().setText(String.format("%.4f", relativeRect.y));

            try {
                String rawText = textProcessor.extractRawText(pdfDocument, pageIndex, relativeRect);
                String cleanedPreview = textProcessor.stripTrailingLineBreaks(rawText).trim();

                JOptionPane.showMessageDialog(
                    view,
                    "Texto detectado en el área:\n\n" + cleanedPreview,
                    "Área asignada al campo \"" + selectedFieldName + "\"",
                    JOptionPane.INFORMATION_MESSAGE
                );

                System.out.println("=== Área asignada ===");
                System.out.println("Campo: " + selectedFieldName);
                System.out.println("Página (0-based): " + pageIndex);
                System.out.println("Rect relativo: x=" + relativeRect.x
                        + ", y=" + relativeRect.y
                        + ", w=" + relativeRect.width
                        + ", h=" + relativeRect.height);
                System.out.println("Texto crudo:");
                System.out.println(rawText);
                System.out.println("Texto preview (limpio):");
                System.out.println(cleanedPreview);
                System.out.println("======================");

            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    view,
                    "Se guardó el área, pero se produjo un error al extraer el texto:\n" + ex.getMessage(),
                    "Error de extracción",
                    JOptionPane.ERROR_MESSAGE
                );
            }

            invalidateValidation();
            refreshVisibleSelections();
            pagePanel.repaint();
        });
    }

    
    private void setupFieldListSelectionListener() {
        
        view.getLstDataList().addListSelectionListener(e -> {
            
            if (e.getValueIsAdjusting()) {
                return;
            }

            ListModel<String> lm = view.getLstDataList().getModel();
            if (!(lm instanceof DefaultListModel)) {
                return;
            }

            DefaultListModel<String> model = (DefaultListModel<String>) lm;
            int size = model.getSize();
            int index = view.getLstDataList().getSelectedIndex();
            boolean hasSelection = (index != -1);

            view.getBtnClearListData().setEnabled(size > 0);
            view.getBtnDeleteData().setEnabled(hasSelection);
            view.getBtnEditData().setEnabled(hasSelection);
            view.getBtnMoveUpData().setEnabled(hasSelection && index > 0);
            view.getBtnMoveDownData().setEnabled(hasSelection && index < size - 1);

            if (hasSelection) {
                
                String selectedFieldName = view.getLstDataList().getSelectedValue();
                FieldConfig config = fieldManager.getFieldConfig(selectedFieldName);

                if (config == null) {
                    clearFieldOptionsUI();
                    refreshVisibleSelections();
                    return;
                }

                view.getRbdFieldType1().setEnabled(true);
                view.getRbdFieldType2().setEnabled(true);
                view.getRbdFieldType3().setEnabled(true);
                view.getLblPage().setEnabled(true);
                view.getLblAxisX().setEnabled(true);
                view.getLblAxisY().setEnabled(true);
                view.getTxfPage().setEnabled(true);
                view.getTxfAxisX().setEnabled(true);
                view.getTxfAxisY().setEnabled(true);
                view.getBtnValidate().setEnabled(true);

                view.getChkSpaces().setEnabled(true);
                view.getChkSymbols().setEnabled(true);

                FieldType type = config.getFieldType();
                if (type == FieldType.MASTER) {
                    view.getRbdFieldType2().setSelected(true);
                } else if (type == FieldType.DEPENDENT) {
                    view.getRbdFieldType3().setSelected(true);
                } else {
                    view.getRbdFieldType1().setSelected(true);
                }

                view.getChkSpaces().setSelected(config.isRemoveSpaces());
                view.getChkSymbols().setSelected(config.isDigitsOnly());

                view.getChkItem().setSelected(config.isItemField());
                ItemType itemType = config.getItemType();
                view.getCmbItem().setSelectedItem(itemType != null ? itemType : ItemType.ID);

                view.getChkText().setSelected(config.isNormalizeText());
                TextCaseType textCaseType = config.getTextCaseType();
                view.getCmbText().setSelectedItem(textCaseType != null ? textCaseType : TextCaseType.UPPERCASE);

                if (type == FieldType.DEPENDENT) {
                    updateMasterComboBox();
                    String masterName = config.getMasterFieldName();
                    if (masterName != null) {
                        view.getCmbMaster().setSelectedItem(masterName);
                    }
                } else {
                    view.getLblMaster().setEnabled(false);
                    view.getCmbMaster().setEnabled(false);
                }

                if (pagePanel != null) {
                    int currentPageIndex = pagePanel.getCurrentPage();
                    Rectangle2D.Double rect = config.getAreaForPage(currentPageIndex);
                    if (rect != null) {
                        view.getTxfPage().setText(String.valueOf(currentPageIndex + 1));
                        view.getTxfAxisX().setText(String.format("%.4f", rect.x));
                        view.getTxfAxisY().setText(String.format("%.4f", rect.y));
                    } else {
                        view.getTxfPage().setText("");
                        view.getTxfAxisX().setText("");
                        view.getTxfAxisY().setText("");
                    }
                }

                refreshVisibleSelectionsForSingleField(selectedFieldName);
                
                updateFieldTypeDependencies();

            } else {
                clearFieldOptionsUI();
                refreshVisibleSelections();
            }
        });
    }

    
    private void clearFieldOptionsUI() {
        
        view.getRbdFieldType1().setEnabled(false);
        view.getRbdFieldType2().setEnabled(false);
        view.getRbdFieldType3().setEnabled(false);
        view.getLblPage().setEnabled(false);
        view.getLblAxisX().setEnabled(false);
        view.getLblAxisY().setEnabled(false);
        view.getTxfPage().setEnabled(false);
        view.getTxfAxisX().setEnabled(false);
        view.getTxfAxisY().setEnabled(false);
        view.getBtnValidate().setEnabled(false);

        view.getChkSpaces().setEnabled(false);
        view.getChkSymbols().setEnabled(false);
        view.getChkItem().setEnabled(false);
        view.getChkText().setEnabled(false);
        view.getCmbItem().setEnabled(false);
        view.getCmbText().setEnabled(false);
        view.getLblMaster().setEnabled(false);
        view.getCmbMaster().setEnabled(false);

        view.getChkSpaces().setSelected(false);
        view.getChkSymbols().setSelected(false);
        view.getChkItem().setSelected(false);
        view.getChkText().setSelected(false);

        view.getRbdFieldType1().setSelected(false);
        view.getRbdFieldType2().setSelected(false);
        view.getRbdFieldType3().setSelected(false);

        view.getTxfPage().setText("");
        view.getTxfAxisX().setText("");
        view.getTxfAxisY().setText("");
    }

   
    private void setupFieldButtonsListeners() {

        view.getBtnAddData().addActionListener(evt -> {
            JTextField txtName = new JTextField(20);

            JPanel panel = new JPanel();
            panel.add(new JLabel("Nombre del campo:"));
            panel.add(txtName);

            JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION
            );

            JDialog dialog = optionPane.createDialog(view, "Añadir nuevo campo");
            dialog.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    txtName.requestFocusInWindow();
                }
            });
            dialog.setVisible(true);

            Object selectedValue = optionPane.getValue();
            if (selectedValue != null && (int) selectedValue == JOptionPane.OK_OPTION) {
                String fieldName = txtName.getText().trim();
                if (!fieldName.isEmpty()) {
                    DefaultListModel<String> model = ensureListModel();
                    if (model.contains(fieldName)) {
                        JOptionPane.showMessageDialog(
                            view,
                            "Ese nombre ya existe en la lista.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        model.addElement(fieldName);

                        FieldConfig config = fieldManager.addField(fieldName);
                        config.setFieldType(FieldType.UNIQUE);
                        config.setItemField(false);
                        config.setItemType(ItemType.ID);
                        config.setRemoveSpaces(false);
                        config.setDigitsOnly(false);
                        config.setNormalizeText(false);
                        config.setTextCaseType(TextCaseType.UPPERCASE);

                        int newIndex = model.getSize() - 1;
                        view.getLstDataList().setSelectedIndex(newIndex);
                        view.getLstDataList().ensureIndexIsVisible(newIndex);

                        updateMasterComboBoxIfNeeded();
                        updateFieldTypeDependencies();
                        invalidateValidation();
                    }
                }
            }

            dialog.dispose();
        });

        view.getBtnDeleteData().addActionListener(evt -> {
            DefaultListModel<String> model = ensureListModel();
            int index = view.getLstDataList().getSelectedIndex();
            if (index == -1) return;

            String fieldName = model.getElementAt(index);

            int option = JOptionPane.showConfirmDialog(
                view,
                "¿Está seguro de que quiere eliminar el campo \"" + fieldName + "\"?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                model.remove(index);

                fieldManager.removeField(fieldName);

                invalidateValidation();
                refreshVisibleSelections();
                updateMasterComboBoxIfNeeded();

                if (!model.isEmpty()) {
                    int newIndex = Math.min(index, model.getSize() - 1);
                    view.getLstDataList().setSelectedIndex(newIndex);
                    view.getLstDataList().ensureIndexIsVisible(newIndex);
                } else {
                    view.getBtnDeleteData().setEnabled(false);
                    view.getBtnEditData().setEnabled(false);
                    view.getBtnMoveUpData().setEnabled(false);
                    view.getBtnMoveDownData().setEnabled(false);
                    view.getBtnClearListData().setEnabled(false);
                    clearFieldOptionsUI();
                }
            }
        });

        view.getBtnClearListData().addActionListener(evt -> {
            DefaultListModel<String> model = ensureListModel();
            if (model.isEmpty()) return;

            int option = JOptionPane.showConfirmDialog(
                view,
                "¿Está seguro de que quiere eliminar todos los campos?",
                "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
            );

            if (option == JOptionPane.YES_OPTION) {
                model.clear();

                fieldManager.clear();

                invalidateValidation();
                refreshVisibleSelections();
                updateMasterComboBoxIfNeeded();

                view.getBtnDeleteData().setEnabled(false);
                view.getBtnEditData().setEnabled(false);
                view.getBtnMoveUpData().setEnabled(false);
                view.getBtnMoveDownData().setEnabled(false);
                view.getBtnClearListData().setEnabled(false);
                clearFieldOptionsUI();
            }
        });

        view.getBtnEditData().addActionListener(evt -> {
            DefaultListModel<String> model = ensureListModel();
            int index = view.getLstDataList().getSelectedIndex();
            if (index == -1) return;

            String oldName = model.getElementAt(index);
            FieldConfig config = fieldManager.getFieldConfig(oldName);
            if (config == null) return;

            JTextField txtName = new JTextField(oldName, 20);
            JPanel panel = new JPanel();
            panel.add(new JLabel("Editar nombre del campo:"));
            panel.add(txtName);

            JOptionPane optionPane = new JOptionPane(
                panel,
                JOptionPane.PLAIN_MESSAGE,
                JOptionPane.OK_CANCEL_OPTION
            );

            JDialog dialog = optionPane.createDialog(view, "Editar campo");
            dialog.addWindowFocusListener(new WindowAdapter() {
                @Override
                public void windowGainedFocus(WindowEvent e) {
                    txtName.requestFocusInWindow();
                    txtName.selectAll();
                }
            });
            dialog.setVisible(true);

            Object selectedValue = optionPane.getValue();
            if (selectedValue != null && (int) selectedValue == JOptionPane.OK_OPTION) {
                String newName = txtName.getText().trim();
                if (!newName.isEmpty() && !newName.equals(oldName)) {
                    if (model.contains(newName)) {
                        JOptionPane.showMessageDialog(
                            view,
                            "Ya existe un campo con ese nombre.",
                            "Aviso",
                            JOptionPane.WARNING_MESSAGE
                        );
                    } else {
                        model.setElementAt(newName, index);
                        view.getLstDataList().setSelectedIndex(index);

                        fieldManager.renameField(oldName, newName);

                        refreshVisibleSelections();
                        updateMasterComboBoxIfNeeded();
                        invalidateValidation();
                    }
                }
            }

            dialog.dispose();
        });

        view.getBtnMoveUpData().addActionListener(evt -> {
            DefaultListModel<String> model = ensureListModel();
            int index = view.getLstDataList().getSelectedIndex();
            if (index <= 0) return;

            String current = model.getElementAt(index);
            String previous = model.getElementAt(index - 1);

            model.setElementAt(current, index - 1);
            model.setElementAt(previous, index);

            fieldManager.swapFields(previous, current);

            int newIndex = index - 1;
            view.getLstDataList().setSelectedIndex(newIndex);
            view.getLstDataList().ensureIndexIsVisible(newIndex);

            updateMasterComboBoxIfNeeded();
            invalidateValidation();
        });

        view.getBtnMoveDownData().addActionListener(evt -> {
            DefaultListModel<String> model = ensureListModel();
            int index = view.getLstDataList().getSelectedIndex();
            if (index == -1 || index >= model.getSize() - 1) return;

            String current = model.getElementAt(index);
            String next = model.getElementAt(index + 1);

            model.setElementAt(next, index);
            model.setElementAt(current, index + 1);

            fieldManager.swapFields(current, next);

            int newIndex = index + 1;
            view.getLstDataList().setSelectedIndex(newIndex);
            view.getLstDataList().ensureIndexIsVisible(newIndex);

            updateMasterComboBoxIfNeeded();
            invalidateValidation();
        });
    }


    private DefaultListModel<String> ensureListModel() {
        
        ListModel<String> lm = view.getLstDataList().getModel();
        if (lm instanceof DefaultListModel) {
            return (DefaultListModel<String>) lm;
        } else {
            DefaultListModel<String> model = new DefaultListModel<>();
            view.getLstDataList().setModel(model);
            return model;
        }
    }
    

    private void setupFieldOptionsListeners() {
        
        view.getRbdFieldType1().addActionListener(e -> updateFieldTypeFromUI());
        view.getRbdFieldType2().addActionListener(e -> updateFieldTypeFromUI());
        view.getRbdFieldType3().addActionListener(e -> updateFieldTypeFromUI());

        view.getChkItem().addActionListener(e -> {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName == null) return;
            FieldConfig config = fieldManager.getFieldConfig(fieldName);
            if (config == null) return;

            boolean isItem = view.getChkItem().isSelected();
            config.setItemField(isItem);

            if (isItem) {
                config.clearAreas();
                view.getTxfPage().setText("");
                view.getTxfAxisX().setText("");
                view.getTxfAxisY().setText("");
                refreshVisibleSelections();
            }

            updateFieldTypeDependencies();
            invalidateValidation();
        });

        view.getChkText().addActionListener(e -> {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName == null) return;
            FieldConfig config = fieldManager.getFieldConfig(fieldName);
            if (config == null) return;

            config.setNormalizeText(view.getChkText().isSelected());
            updateFieldTypeDependencies();
            invalidateValidation();
        });

        view.getChkSpaces().addActionListener(e -> {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName == null) return;
            FieldConfig config = fieldManager.getFieldConfig(fieldName);
            if (config == null) return;

            config.setRemoveSpaces(view.getChkSpaces().isSelected());
            invalidateValidation();
        });

        view.getChkSymbols().addActionListener(e -> {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName == null) return;
            FieldConfig config = fieldManager.getFieldConfig(fieldName);
            if (config == null) return;

            config.setDigitsOnly(view.getChkSymbols().isSelected());
            invalidateValidation();
        });

        view.getCmbItem().addActionListener(e -> {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName == null) return;
            FieldConfig config = fieldManager.getFieldConfig(fieldName);
            if (config == null) return;

            Object sel = view.getCmbItem().getSelectedItem();
            if (sel instanceof String) {
                try {
                    ItemType itemType = ItemType.valueOf((String) sel);
                    config.setItemType(itemType);
                } catch (IllegalArgumentException ex) {
                }
            }

            updateFieldTypeDependencies();
            invalidateValidation();
        });
        
        view.getCmbText().addActionListener(e -> {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName == null) return;
            FieldConfig config = fieldManager.getFieldConfig(fieldName);
            if (config == null) return;

            Object sel = view.getCmbText().getSelectedItem();
            if (sel instanceof String) {
                try {
                    TextCaseType tct = TextCaseType.valueOf((String) sel);
                    config.setTextCaseType(tct);
                } catch (IllegalArgumentException ex) {
                }
            }

            updateFieldTypeDependencies();
            invalidateValidation();
        });

        view.getCmbMaster().addActionListener(e -> {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName == null) return;
            FieldConfig config = fieldManager.getFieldConfig(fieldName);
            if (config == null) return;

            Object sel = view.getCmbMaster().getSelectedItem();
            if (sel != null && !"No hay campos maestros definidos".equals(sel.toString())) {
                config.setMasterFieldName(sel.toString());
                invalidateValidation();
            }
        });
    }

    private void updateFieldTypeFromUI() {
        
        String fieldName = view.getLstDataList().getSelectedValue();
        if (fieldName == null) return;
        FieldConfig config = fieldManager.getFieldConfig(fieldName);
        if (config == null) return;

        if (view.getRbdFieldType1().isSelected()) {
            config.setFieldType(FieldType.UNIQUE);
        } else if (view.getRbdFieldType2().isSelected()) {
            config.setFieldType(FieldType.MASTER);
        } else if (view.getRbdFieldType3().isSelected()) {
            config.setFieldType(FieldType.DEPENDENT);
        }

        updateFieldTypeDependencies();
        invalidateValidation();
    }

    private void updateFieldTypeDependencies() {
        
        int index = view.getLstDataList().getSelectedIndex();
        boolean hasSelection = (index != -1);

        FieldConfig config = null;
        FieldType type = FieldType.UNIQUE;

        if (hasSelection) {
            String fieldName = view.getLstDataList().getSelectedValue();
            if (fieldName != null) {
                config = fieldManager.getFieldConfig(fieldName);
            }
        }

        if (config != null && config.getFieldType() != null) {
            type = config.getFieldType();
        }

        boolean isUnique    = (type == FieldType.UNIQUE);
        boolean isDependent = (type == FieldType.DEPENDENT);
      
        boolean chkItemEnabled = hasSelection && isUnique;
        view.getChkItem().setEnabled(chkItemEnabled);

        if (!chkItemEnabled) {
            if (view.getChkItem().isSelected()) {
                view.getChkItem().setSelected(false);
            }
            if (config != null) {
                config.setItemField(false);
            }
        } else if (config != null) {
            view.getChkItem().setSelected(config.isItemField());
        }

        boolean itemSelected = chkItemEnabled && view.getChkItem().isSelected();

        boolean cmbItemEnabled = itemSelected;
        view.getCmbItem().setEnabled(cmbItemEnabled);

        if (config != null && cmbItemEnabled && config.getItemType() != null) {
            view.getCmbItem().setSelectedItem(config.getItemType().name());
        }

        boolean chkTextEnabled = hasSelection && !itemSelected;
        view.getChkText().setEnabled(chkTextEnabled);

        if (!chkTextEnabled) {
            if (view.getChkText().isSelected()) {
                view.getChkText().setSelected(false);
            }
            if (config != null) {
                config.setNormalizeText(false);
            }
        } else if (config != null) {
            view.getChkText().setSelected(config.isNormalizeText());
        }

        boolean cmbTextEnabled = chkTextEnabled && view.getChkText().isSelected();
        view.getCmbText().setEnabled(cmbTextEnabled);

        if (config != null && cmbTextEnabled && config.getTextCaseType() != null) {
            view.getCmbText().setSelectedItem(config.getTextCaseType().name());
        }

        boolean spacesSymbolsEnabled = hasSelection && !itemSelected;
        view.getChkSpaces().setEnabled(spacesSymbolsEnabled);
        view.getChkSymbols().setEnabled(spacesSymbolsEnabled);

        if (!spacesSymbolsEnabled) {
            if (view.getChkSpaces().isSelected()) view.getChkSpaces().setSelected(false);
            if (view.getChkSymbols().isSelected()) view.getChkSymbols().setSelected(false);
            if (config != null) {
                config.setRemoveSpaces(false);
                config.setDigitsOnly(false);
            }
        } else if (config != null) {
            view.getChkSpaces().setSelected(config.isRemoveSpaces());
            view.getChkSymbols().setSelected(config.isDigitsOnly());
        }

        boolean masterEnabled = hasSelection && isDependent;
        view.getLblMaster().setEnabled(masterEnabled);
        view.getCmbMaster().setEnabled(masterEnabled);

        if (!masterEnabled) {
            if (config != null) {
                config.setMasterFieldName(null);
            }
        } else if (config != null) {
            updateMasterComboBox();
            String masterName = config.getMasterFieldName();
            if (masterName != null) {
                view.getCmbMaster().setSelectedItem(masterName);
            }
        }
    }

    
    private void updateMasterComboBox() {
        
        view.getCmbMaster().removeAllItems();

        List<String> fieldNames = fieldManager.getFieldNamesInOrder();
        for (String name : fieldNames) {
            FieldConfig config = fieldManager.getFieldConfig(name);
            if (config != null && config.getFieldType() == FieldType.MASTER) {
                view.getCmbMaster().addItem(name);
            }
        }

        if (view.getCmbMaster().getItemCount() == 0) {
            view.getCmbMaster().addItem("No hay campos maestros definidos");
            view.getCmbMaster().setEnabled(false);
        } else {
            view.getCmbMaster().setEnabled(true);
        }
    }

    
    private void updateMasterComboBoxIfNeeded() {
        
        if (view.getCmbMaster().isEnabled()) {
            updateMasterComboBox();
        }
    }

    
    private void setupStructureModeListeners() {
        
        view.getRdbStructure1().addActionListener(e -> {
            extractionSettings.setStructureMode(StructureMode.EVERY_TWO_PAGES);
            applyStructureMode();
            invalidateValidation();
        });
        view.getRdbStructure2().addActionListener(e -> {
            extractionSettings.setStructureMode(StructureMode.EVERY_PAGE);
            applyStructureMode();
            invalidateValidation();
        });
        view.getRdbStructure3().addActionListener(e -> {
            extractionSettings.setStructureMode(StructureMode.DIFFERENT_BOTH_SIDES);
            applyStructureMode();
            invalidateValidation();
        });
    }

   
    private void applyStructureMode() {
        
        if (pdfDocument == null) return;

        boolean struct1 = extractionSettings.getStructureMode() == StructureMode.EVERY_TWO_PAGES;
        boolean struct2 = extractionSettings.getStructureMode() == StructureMode.EVERY_PAGE;
        boolean struct3 = extractionSettings.getStructureMode() == StructureMode.DIFFERENT_BOTH_SIDES;

        if (struct1 || struct2) {
            view.getTglPag1().setEnabled(true);
            view.getTglPag2().setEnabled(false);

            if (view.getTglPag2().isSelected()) {
                view.getTglPag2().setSelected(false);
                view.getTglPag1().setSelected(true);
                showFrontSide();
            }
        } else if (struct3) {
            view.getTglPag1().setEnabled(true);
            view.getTglPag2().setEnabled(true);
        }
    }
    

    private void setupPageScannerListeners() {
        
        view.getRbdPagesScanner1().addActionListener(e -> {
            extractionSettings.setPageScanMode(PageScanMode.FULL_DOCUMENT);
            applyPageScannerMode();
            if (pdfDocument != null) {
                extractionSettings.setStartPage(1);
                extractionSettings.setEndPage(pdfDocument.getNumberOfPages());
                showFrontSide();
            }
            invalidateValidation();
        });

        view.getRbdPagesScanner2().addActionListener(e -> {
            extractionSettings.setPageScanMode(PageScanMode.CUSTOM_RANGE);
            applyPageScannerMode();
            if (pdfDocument != null) {
                String startText = view.getTxfStart().getText().trim();
                try {
                    int start = Integer.parseInt(startText);
                    extractionSettings.setStartPage(start);
                    showFrontSide();
                } catch (NumberFormatException ex) {
                }
            }
            invalidateValidation();
        });

        DocumentListener rangeListener = new DocumentListener() {
            private void validateRange() {
                if (pdfDocument == null) return;

                invalidateValidation();
                
                int totalPages = pdfDocument.getNumberOfPages();
                String startText = view.getTxfStart().getText().trim();
                String finishText = view.getTxfFinish().getText().trim();

                boolean valid = true;
                int start = 1;
                int finish = totalPages;

                try {
                    start = Integer.parseInt(startText);
                    finish = Integer.parseInt(finishText);
                    if (start < 1 || finish > totalPages || start >= finish) {
                        valid = false;
                    }
                } catch (NumberFormatException ex) {
                    valid = false;
                }

                view.getTxfStart().setForeground(valid ? Color.BLACK : Color.RED);
                view.getTxfFinish().setForeground(valid ? Color.BLACK : Color.RED);

                if (valid) {
                    extractionSettings.setStartPage(start);
                    extractionSettings.setEndPage(finish);

                    if (extractionSettings.getPageScanMode() == PageScanMode.CUSTOM_RANGE) {
                        showFrontSide();
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) { validateRange(); }
            @Override
            public void removeUpdate(DocumentEvent e) { validateRange(); }
            @Override
            public void changedUpdate(DocumentEvent e) { validateRange(); }
        };

        view.getTxfStart().getDocument().addDocumentListener(rangeListener);
        view.getTxfFinish().getDocument().addDocumentListener(rangeListener);
    }


    private void applyPageScannerMode() {
        
        boolean custom = extractionSettings.getPageScanMode() == PageScanMode.CUSTOM_RANGE;

        view.getTxfStart().setEnabled(custom);
        view.getTxfFinish().setEnabled(custom);
        view.getTxfStart().setEditable(custom);
        view.getTxfFinish().setEditable(custom);
        view.getTxfStart().setFocusable(custom);
        view.getTxfFinish().setFocusable(custom);

        if (pdfDocument != null) {
            int totalPages = pdfDocument.getNumberOfPages();
            if (custom) {
                view.getTxfStart().setText(String.valueOf(extractionSettings.getStartPage() > 0
                                                          ? extractionSettings.getStartPage()
                                                          : 1));
                view.getTxfFinish().setText(String.valueOf(extractionSettings.getEndPage() > 0
                                                           ? extractionSettings.getEndPage()
                                                           : totalPages));
            } else {
                view.getTxfStart().setText("");
                view.getTxfFinish().setText("");
                extractionSettings.setStartPage(1);
                extractionSettings.setEndPage(totalPages);
            }
        }
    }

    
    private void setupPageToggleListeners() {
        
        view.getTglPag1().addActionListener(e -> {
            if (!view.getTglPag1().isSelected()) {
                view.getTglPag1().setSelected(true);
            }
            view.getTglPag2().setSelected(false);
            showFrontSide();
        });

        view.getTglPag2().addActionListener(e -> {
            if (pdfDocument == null) return;

            if (!view.getTglPag2().isSelected()) {
                view.getTglPag2().setSelected(true);
            }
            view.getTglPag1().setSelected(false);
            showBackSide();
        });
    }

    
    private void showFrontSide() {
        
        if (pdfDocument == null || pagePanel == null) return;

        try {
            int basePage;
            if (extractionSettings.getPageScanMode() == PageScanMode.CUSTOM_RANGE) {
                basePage = extractionSettings.getStartPage();
            } else {
                basePage = 1;
            }

            int totalPages = pdfDocument.getNumberOfPages();
            if (basePage < 1 || basePage > totalPages) {
                JOptionPane.showMessageDialog(
                    view,
                    "El documento solo tiene " + totalPages + " páginas.",
                    "Página fuera de rango",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            BufferedImage img = renderer.renderImageWithDPI(basePage - 1, 150);

            pagePanel.setImage(img);
            pagePanel.setCurrentPage(basePage - 1);

            view.getScrPdfViewer().revalidate();
            view.getScrPdfViewer().repaint();

            view.getTglPag1().setSelected(true);
            view.getTglPag2().setSelected(false);

            refreshVisibleSelections();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                view,
                "Error al renderizar la cara delantera:\n" + ex.getMessage(),
                "Error de renderizado",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                view,
                "Error inesperado al renderizar la cara delantera:\n" + ex.getMessage(),
                "Error de renderizado",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void showBackSide() {
        
        if (pdfDocument == null || pagePanel == null) return;

        try {
            int totalPages = pdfDocument.getNumberOfPages();
            int basePage;

            if (extractionSettings.getPageScanMode() == PageScanMode.CUSTOM_RANGE) {
                basePage = extractionSettings.getStartPage() + 1;
            } else {
                basePage = 2;
            }

            if (basePage > totalPages) {
                JOptionPane.showMessageDialog(
                    view,
                    "El documento solo tiene " + totalPages + " páginas.",
                    "Página fuera de rango",
                    JOptionPane.WARNING_MESSAGE
                );
                view.getTglPag2().setSelected(false);
                view.getTglPag1().setSelected(true);
                showFrontSide();
                return;
            }

            PDFRenderer renderer = new PDFRenderer(pdfDocument);
            BufferedImage img = renderer.renderImageWithDPI(basePage - 1, 150);

            pagePanel.setImage(img);
            pagePanel.setCurrentPage(basePage - 1);

            view.getScrPdfViewer().revalidate();
            view.getScrPdfViewer().repaint();

            view.getTglPag2().setSelected(true);
            view.getTglPag1().setSelected(false);

            refreshVisibleSelections();

        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                view,
                "Error al renderizar la cara trasera:\n" + ex.getMessage(),
                "Error de renderizado",
                JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                view,
                "Error inesperado al renderizar la cara trasera:\n" + ex.getMessage(),
                "Error de renderizado",
                JOptionPane.ERROR_MESSAGE
            );
        }
    }


    private void setupZoomSliderListener() {
        
        view.getSldRightPanel().addChangeListener(e -> {
            if (pagePanel == null) return;
            double z = view.getSldRightPanel().getValue() / 100.0;
            pagePanel.setZoom(z);
        });
    }


    private void setupValidateAndGenerateListeners() {
        
        view.getBtnValidate().addActionListener(evt -> {
            if (pdfDocument == null) {
                JOptionPane.showMessageDialog(
                    view,
                    "No hay ningún PDF cargado.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int totalPages = pdfDocument.getNumberOfPages();
            int startPage = 1;
            int finishPage = totalPages;

            if (view.getRbdPagesScanner2().isSelected()) {
                try {
                    startPage = Integer.parseInt(view.getTxfStart().getText().trim());
                    finishPage = Integer.parseInt(view.getTxfFinish().getText().trim());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                        view,
                        "Introduce números válidos en el rango de páginas.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }

                if (startPage < 1 || finishPage > totalPages || startPage >= finishPage) {
                    JOptionPane.showMessageDialog(
                        view,
                        "El rango de páginas no es válido.\n" +
                        "Debe estar entre 1 y " + totalPages +
                        " y la página inicial debe ser menor que la final.",
                        "Validación",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
            }

            extractionSettings.setStartPage(startPage);
            extractionSettings.setEndPage(finishPage);

            ListModel<String> lm = view.getLstDataList().getModel();
            int size = lm.getSize();
            if (size == 0) {
                JOptionPane.showMessageDialog(
                    view,
                    "No hay campos definidos en la lista.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            List<String> fieldOrder = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                fieldOrder.add(lm.getElementAt(i));
            }

            Map<String, FieldConfig> fieldConfigs = fieldManager.getAllFieldConfigs();

            boolean hasFieldsWithArea = false;
            boolean hasInvalidFields = false;
            List<String> invalidFields = new ArrayList<>();

            for (String fieldName : fieldOrder) {
                FieldConfig cfg = fieldConfigs.get(fieldName);
                if (cfg == null) continue;

                if (cfg.isItemField()) {
                    continue;
                }

                Map<Integer, Rectangle2D.Double> areas = cfg.getAreasByPage();
                if (areas != null && !areas.isEmpty()) {
                    hasFieldsWithArea = true;
                } else {
                    hasInvalidFields = true;
                    invalidFields.add(fieldName);
                }
            }

            if (!hasFieldsWithArea) {
                JOptionPane.showMessageDialog(
                    view,
                    "No hay campos con área asignada.\n" +
                    "Para generar datos, al menos un campo debe tener un área definida\n" +
                    "o estar marcado como 'campo sin área'.",
                    "Validación",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            if (hasInvalidFields) {
                StringBuilder errorMessage = new StringBuilder();
                errorMessage.append("Los siguientes campos requieren un área asignada:\n\n");
                for (String field : invalidFields) {
                    errorMessage.append("• ").append(field).append("\n");
                }
                errorMessage.append(
                    "\nPor favor, asigne áreas a estos campos o márquelos como 'campos sin área'."
                );

                JOptionPane.showMessageDialog(
                    view,
                    errorMessage.toString(),
                    "Campos sin área asignada",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            List<String> dependentErrors = new ArrayList<>();

            for (String fieldName : fieldOrder) {
                FieldConfig cfg = fieldConfigs.get(fieldName);
                if (cfg == null) continue;

                if (cfg.getFieldType() == FieldType.DEPENDENT) {

                    String master = cfg.getMasterFieldName();
                    if (master == null || master.isEmpty()) {
                        dependentErrors.add("• " + fieldName + " no tiene maestro asignado.");
                        continue;
                    }

                    FieldConfig masterCfg = fieldConfigs.get(master);
                    if (masterCfg == null) {
                        dependentErrors.add("• " + fieldName + ": el maestro \"" + master + "\" no existe en la lista.");
                        continue;
                    }

                    if (masterCfg.getFieldType() == FieldType.DEPENDENT) {
                        dependentErrors.add("• " + fieldName + ": el maestro \"" + master + "\" no puede ser un campo dependiente.");
                    }

                    if (master.equals(fieldName)) {
                        dependentErrors.add("• " + fieldName + " no puede ser maestro de sí mismo.");
                    }
                }
            }

            if (!dependentErrors.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                sb.append("Se han encontrado errores en campos dependientes:\n\n");
                for (String s : dependentErrors) sb.append(s).append("\n");

                JOptionPane.showMessageDialog(
                    view,
                    sb.toString(),
                    "Error en dependencias MASTER/DEPENDENT",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            try {
                extractedData.clear();
                extractedData.addAll(
                    extractionEngine.extract(pdfDocument, fieldOrder, fieldConfigs, extractionSettings)
                );

                JOptionPane.showMessageDialog(
                    view,
                    "Validación completada. Se extrajeron " + extractedData.size() + " filas de datos.",
                    "Validación completada",
                    JOptionPane.INFORMATION_MESSAGE
                );
                view.getBtnGenerate().setEnabled(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(
                    view,
                    "Se produjo un error durante la validación: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        view.getBtnGenerate().addActionListener(evt -> {
            if (pdfPath == null || pdfPath.isEmpty()) {
                JOptionPane.showMessageDialog(
                    view,
                    "No se ha encontrado la ruta del PDF de origen.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            if (extractedData.isEmpty()) {
                JOptionPane.showMessageDialog(
                    view,
                    "No hay datos validados para exportar.\n" +
                    "Por favor, pulsa primero en \"Validar datos\".",
                    "Sin datos",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            ListModel<String> lm = view.getLstDataList().getModel();
            int size = lm.getSize();
            if (size == 0) {
                JOptionPane.showMessageDialog(
                    view,
                    "No hay campos definidos en la lista.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

            List<String> headers = new ArrayList<>();
            for (int i = 0; i < size; i++) {
                headers.add(lm.getElementAt(i));
            }

            try {
                File pdfFile = new File(pdfPath);

                File outXlsx = excelExporter.exportToExcel(extractedData, headers, pdfFile);

                JOptionPane.showMessageDialog(
                    view,
                    "Excel generado correctamente en:\n" + outXlsx.getAbsolutePath(),
                    "Generación completada",
                    JOptionPane.INFORMATION_MESSAGE
                );

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(
                    view,
                    "Error al generar el Excel: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });
    }

    
    private void refreshVisibleSelections() {
        
        if (pagePanel == null) return;

        int currentPageIndex = pagePanel.getCurrentPage();
        Map<String, Rectangle2D.Double> visibleSelections = new LinkedHashMap<>();

        for (FieldConfig config : fieldManager.getFieldsInOrder()) {
            Rectangle2D.Double rect = config.getAreaForPage(currentPageIndex);
            if (rect != null) {
                visibleSelections.put(config.getName(), rect);
            }
        }

        pagePanel.setSelections(visibleSelections);
    }

    
    private void refreshVisibleSelectionsForSingleField(String fieldName) {
        
        if (pagePanel == null) return;

        int currentPageIndex = pagePanel.getCurrentPage();
        Map<String, Rectangle2D.Double> visibleSelections = new LinkedHashMap<>();

        for (FieldConfig config : fieldManager.getFieldsInOrder()) {
            Rectangle2D.Double rect = config.getAreaForPage(currentPageIndex);
            if (rect != null) {
                visibleSelections.put(config.getName(), rect);
            }
        }

        pagePanel.setSelectedFields(java.util.Collections.singleton(fieldName));
        pagePanel.setSelections(visibleSelections);
    }

    
    public void updatePdfContext(PDDocument document, String path, PDFPagePanel panel) {
        
        if (this.pdfDocument != null && this.pdfDocument != document) {
            try {
                this.pdfDocument.close();
            } catch (IOException ignored) {}
        }

        this.pdfDocument = document;
        this.pdfPath = path;
        this.pagePanel = panel;

        view.setPdfDocument(document);
        view.setPdfPath(path);

        view.getScrPdfViewer().setViewportView(panel);

        view.getSldRightPanel().setValue(100);

        configureAreaSelectionManager();

        applyStructureMode();
        applyPageScannerMode();
        refreshVisibleSelections();
        invalidateValidation();
    }
    
    
    private void invalidateValidation() {
        
        extractedData.clear();
        view.getBtnGenerate().setEnabled(false);
    }
}
