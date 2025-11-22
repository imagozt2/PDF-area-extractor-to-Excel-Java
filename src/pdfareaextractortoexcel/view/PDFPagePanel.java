package pdfareaextractortoexcel.view;

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

public class PDFPagePanel extends JComponent implements Scrollable {
    
    private BufferedImage image;
    private double zoom = 1.0;
    
    private Set<String> selectedFields = new HashSet<>();

    private Point start, end;
    private Rectangle current;

    private boolean canSelect = false;

    private Consumer<Rectangle> onSelection;

    private int currentPage = 0;

    private Map<String, Rectangle2D.Double> selectionsForThisPage = new LinkedHashMap<>();

    public void setSelectedFields(Set<String> fields) {
        this.selectedFields = fields;
        repaint();
    }
    
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

    public void setImage(BufferedImage img) {
        
        this.image = img;
        setPreferredSize(new Dimension(
            (int)(image.getWidth()  * zoom),
            (int)(image.getHeight() * zoom)
        ));
        revalidate();
        repaint();
    }

    public int getImageWidth() {
        return image.getWidth();
    }
    public int getImageHeight() {
        return image.getHeight();
    }

    public void setOnAreaSelected(Consumer<Rectangle> listener) {
        this.onSelection = listener;
    }

    public String getToolTipText(MouseEvent e) {

        for (Map.Entry<String, Rectangle2D.Double> entry : selectionsForThisPage.entrySet()) {
            String campo = entry.getKey();
            Rectangle2D.Double frac = entry.getValue();

            int x  = (int)(frac.x      * image.getWidth()  * zoom);
            int y  = (int)(frac.y      * image.getHeight() * zoom);
            int w  = (int)(frac.width  * image.getWidth()  * zoom);
            int h  = (int)(frac.height * image.getHeight() * zoom);

            if (e.getX() >= x && e.getX() <= x + w
             && e.getY() >= y && e.getY() <= y + h) {
                return campo;
            }
        }
        return null;
    }
    
    public void setZoom(double zoom) {
        this.zoom = zoom;
        setPreferredSize(new Dimension(
            (int)(image.getWidth()  * zoom),
            (int)(image.getHeight() * zoom)
        ));
        revalidate();
        repaint();
    }

    public double getZoom() {
        return zoom;
    }

    public void setCanSelect(boolean canSelect) {
        this.canSelect = canSelect;
    }

    public void setCurrentPage(int page) {
        this.currentPage = page;
        repaint();
    }
    public int getCurrentPage() {
        return currentPage;
    }

    public void setSelections(Map<String, Rectangle2D.Double> map) {
        this.selectionsForThisPage = map;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        int w = (int)(image.getWidth()  * zoom);
        int h = (int)(image.getHeight() * zoom);
        g2.drawImage(image, 0, 0, w, h, null);

        for (Map.Entry<String, Rectangle2D.Double> entry : selectionsForThisPage.entrySet()) {
            String campo = entry.getKey();
            Rectangle2D.Double frac = entry.getValue();

            int x  = (int)(frac.x      * image.getWidth()  * zoom);
            int y  = (int)(frac.y      * image.getHeight() * zoom);
            int rw = (int)(frac.width  * image.getWidth()  * zoom);
            int rh = (int)(frac.height * image.getHeight() * zoom);

            Color bg = selectedFields.contains(campo)
                     ? new Color(255, 255,   0, 80)
                     : new Color(  0, 255,   0, 80);
            g2.setColor(bg);
            g2.fillRect(x, y, rw, rh);

            g2.setColor(bg.darker());
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, y, rw, rh);
        }

        if (current != null) {
            g2.setColor(new Color(0, 0, 255, 50));
            g2.fill(current);
            g2.setColor(Color.BLUE);
            g2.setStroke(new BasicStroke(2));
            g2.draw(current);
        }
    }   

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
