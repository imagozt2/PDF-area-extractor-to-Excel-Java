package pdfareaextractortoexcel;

import javax.swing.*;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

// Clase principal del objeto PDFPagePanel
public class PDFPagePanel extends JComponent implements Scrollable {
    private BufferedImage image;
    private double zoom = 1.0;
    
    private Set<String> selectedFields = new HashSet<>();

    // Para arrastrar un rectángulo
    private Point start, end;
    private Rectangle current;

    // Controla si el usuario puede iniciar selecciones
    private boolean canSelect = false;

    // Callback al soltar el ratón con una selección
    private Consumer<Rectangle> onSelection;

    // Página actual (0 o 1)
    private int currentPage = 0;

    // Mapa de selecciones normalizadas para la página actual: campo → fracción de rectángulo
    private Map<String, Rectangle2D.Double> selectionsForThisPage = new LinkedHashMap<>();

    // Para notificar qué campos están seleccionados
    public void setSelectedFields(Set<String> fields) {
        this.selectedFields = fields;
        repaint();
    }
    
    // Función principal del objeto PDFPagePanel
    public PDFPagePanel(BufferedImage img) {
        this.image = img;
        setPreferredSize(new Dimension(
            (int)(image.getWidth()  * zoom),
            (int)(image.getHeight() * zoom)
        ));

        setToolTipText("");
        ToolTipManager.sharedInstance().registerComponent(this);
        
        MouseAdapter ma = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!canSelect) return;
                start   = e.getPoint();
                end     = start;
                current = new Rectangle(start);
                repaint();
            }
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!canSelect || start == null) return;
                end = e.getPoint();
                current.setBounds(
                    Math.min(start.x, end.x),
                    Math.min(start.y, end.y),
                    Math.abs(end.x - start.x),
                    Math.abs(end.y - start.y)
                );
                repaint();
            }
            @Override
            public void mouseReleased(MouseEvent e) {
                if (!canSelect || current == null) return;
                if (onSelection != null && current.width > 0 && current.height > 0) {
                    onSelection.accept(current);
                }
                current = null;
                repaint();
            }
        };
        addMouseListener(ma);
        addMouseMotionListener(ma);
    }

    // Cambia la imagen cuando se recarga la página
    public void setImage(BufferedImage img) {
        this.image = img;
        setPreferredSize(new Dimension(
            (int)(image.getWidth()  * zoom),
            (int)(image.getHeight() * zoom)
        ));
        revalidate();
        repaint();
    }

    // Ancho original de la imagen
    public int getImageWidth() {
        return image.getWidth();
    }
    // Alto original de la imagen
    public int getImageHeight() {
        return image.getHeight();
    }

    // Registra el callback que recibe el rectángulo en coordenadas de pantalla
    public void setOnAreaSelected(Consumer<Rectangle> listener) {
        this.onSelection = listener;
    }

    // Aparición de etiqueta con el nombre en un área al hacer "hover"
    public String getToolTipText(MouseEvent e) {
        // Recorre cada región guardada para ver si el punto e (cursor) está dentro
        for (Map.Entry<String, Rectangle2D.Double> entry : selectionsForThisPage.entrySet()) {
            String campo = entry.getKey();
            Rectangle2D.Double frac = entry.getValue();

            // Calcula la posición y tamaño real en pantalla
            int x  = (int)(frac.x      * image.getWidth()  * zoom);
            int y  = (int)(frac.y      * image.getHeight() * zoom);
            int w  = (int)(frac.width  * image.getWidth()  * zoom);
            int h  = (int)(frac.height * image.getHeight() * zoom);

            // Si el ratón está dentro de ese rect se muestra el nombre del campo
            if (e.getX() >= x && e.getX() <= x + w
             && e.getY() >= y && e.getY() <= y + h) {
                return campo;
            }
        }
        return null;
    }
    
    // Ajusta el factor de zoom
    public void setZoom(double zoom) {
        this.zoom = zoom;
        setPreferredSize(new Dimension(
            (int)(image.getWidth()  * zoom),
            (int)(image.getHeight() * zoom)
        ));
        revalidate();
        repaint();
    }

    // Devuelve el factor de zoom actual
    public double getZoom() {
        return zoom;
    }

    // Habilita o invalida la posibilidad de seleccionar áreas
    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    // Marca la página que se está visualizando (0 o 1)
    public void setCurrentPage(int page) {
        this.currentPage = page;
        repaint();
    }
    // Devuelve la página actualmente mostrada
    public int getCurrentPage() {
        return currentPage;
    }

    // Inyección del mapa de selecciones en la página actual
    public void setSelections(Map<String, Rectangle2D.Double> map) {
        this.selectionsForThisPage = map;
        repaint();
    }

    // Definición de áreas y colores de las selecciones en el PDF
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Dibuja la imagen escalada
        int w = (int)(image.getWidth()  * zoom);
        int h = (int)(image.getHeight() * zoom);
        g2.drawImage(image, 0, 0, w, h, null);

        // Dibuja las selecciones guardadas, pero ahora por Entry (clave + rect)
        for (Map.Entry<String, Rectangle2D.Double> entry : selectionsForThisPage.entrySet()) {
            String campo = entry.getKey();
            Rectangle2D.Double frac = entry.getValue();

            int x  = (int)(frac.x      * image.getWidth()  * zoom);
            int y  = (int)(frac.y      * image.getHeight() * zoom);
            int rw = (int)(frac.width  * image.getWidth()  * zoom);
            int rh = (int)(frac.height * image.getHeight() * zoom);

            // Elige color: amarillo si el campo está seleccionado; verde si no
            Color bg = selectedFields.contains(campo)
                     ? new Color(255, 255,   0, 80)  // amarillo semitransp.
                     : new Color(  0, 255,   0, 80); // verde semitransp.
            g2.setColor(bg);
            g2.fillRect(x, y, rw, rh);

            // Contorno más oscuro
            g2.setColor(bg.darker());
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, y, rw, rh);
        }

        // Dibuja el rectángulo (azul claro) al arrastrar el cursor 
        if (current != null) {
            g2.setColor(new Color(0, 0, 255, 50));
            g2.fill(current);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            g2.draw(current);
        }
    }   

    // Funciones para implementar el scroll
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return 16;
    }
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return (orientation == SwingConstants.VERTICAL)
             ? visibleRect.height
             : visibleRect.width;
    }
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return false;
    }
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return false;
    }
    @Override
    public Dimension getPreferredSize() {
        return new Dimension(
            (int)(image.getWidth()  * zoom),
            (int)(image.getHeight() * zoom)
        );
    }
}
