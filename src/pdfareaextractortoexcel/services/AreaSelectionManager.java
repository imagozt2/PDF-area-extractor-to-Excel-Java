package pdfareaextractortoexcel.services;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.Map;
import pdfareaextractortoexcel.model.FieldConfig;


public class AreaSelectionManager {

    private final Map<String, FieldConfig> fieldConfigs;

    public AreaSelectionManager(FieldManager fieldManager) {
        this.fieldConfigs = fieldManager.getAllFieldConfigs();
    }

    public Rectangle2D.Double saveSelection(
            String fieldName,
            int pageIndex,
            Rectangle screenRect,
            double zoom,
            int imageWidth,
            int imageHeight
    ) {
        if (fieldName == null || screenRect == null) {
            return null;
        }

        double fx = screenRect.getX() / (imageWidth * zoom);
        double fy = screenRect.getY() / (imageHeight * zoom);
        double fw = screenRect.getWidth() / (imageWidth * zoom);
        double fh = screenRect.getHeight() / (imageHeight * zoom);

        Rectangle2D.Double relativeRect =
                new Rectangle2D.Double(fx, fy, fw, fh);

        FieldConfig config = fieldConfigs.get(fieldName);
        if (config != null) {
            config.setAreaForPage(pageIndex, relativeRect);
        }

        return relativeRect;
    }

    public Map<String, Rectangle2D.Double> getSelectionsForPage(int pageIndex) {
        Map<String, Rectangle2D.Double> visible = new LinkedHashMap<>();

        for (Map.Entry<String, FieldConfig> entry : fieldConfigs.entrySet()) {
            String fieldName = entry.getKey();
            FieldConfig config = entry.getValue();

            Rectangle2D.Double rect = config.getAreaForPage(pageIndex);
            if (rect != null) {
                visible.put(fieldName, rect);
            }
        }

        return visible;
    }
}