package pdfareaextractortoexcel.services;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import pdfareaextractortoexcel.model.FieldConfig;
import pdfareaextractortoexcel.model.FieldType;


public class FieldManager {

    private final Map<String, FieldConfig> fieldsByName = new LinkedHashMap<>();

    public FieldManager() {
    }

    
    public FieldConfig addField(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        FieldConfig config = new FieldConfig(name);
        fieldsByName.put(name, config);
        return config;
    }

    public void removeField(String name) {
        if (name == null) return;
        fieldsByName.remove(name);
    }

    public void clearFields() {
        fieldsByName.clear();
    }

    public void clear() {
        clearFields();
    }

    public FieldConfig getFieldConfig(String name) {
        if (name == null) return null;
        return fieldsByName.get(name);
    }

    public Map<String, FieldConfig> getAllFieldConfigs() {
        return fieldsByName;
    }

    public void renameField(String oldName, String newName) {
        if (oldName == null || newName == null) return;
        if (oldName.equals(newName)) return;
        if (!fieldsByName.containsKey(oldName)) return;
        if (fieldsByName.containsKey(newName)) return;

        FieldConfig config = fieldsByName.remove(oldName);
        if (config != null) {
            config.setName(newName);
            fieldsByName.put(newName, config);
        }
    }

    public List<String> getFieldNamesInOrder() {
        return new ArrayList<>(fieldsByName.keySet());
    }

    public List<FieldConfig> getFieldsInOrder() {
        return new ArrayList<>(fieldsByName.values());
    }

    public void swapFields(String name1, String name2) {
        if (name1 == null || name2 == null) return;
        if (name1.equals(name2)) return;
        if (!fieldsByName.containsKey(name1) || !fieldsByName.containsKey(name2)) return;

        FieldConfig cfg1 = fieldsByName.get(name1);
        FieldConfig cfg2 = fieldsByName.get(name2);

        Map<String, FieldConfig> reordered = new LinkedHashMap<>();
        for (Map.Entry<String, FieldConfig> entry : fieldsByName.entrySet()) {
            String key = entry.getKey();
            if (key.equals(name1)) {
                reordered.put(name2, cfg2);
            } else if (key.equals(name2)) {
                reordered.put(name1, cfg1);
            } else {
                reordered.put(key, entry.getValue());
            }
        }

        fieldsByName.clear();
        fieldsByName.putAll(reordered);
    }

    public List<String> getMasterFieldNamesInOrder(List<String> orderedFieldNames) {
        List<String> result = new ArrayList<>();
        if (orderedFieldNames == null) return result;

        for (String fieldName : orderedFieldNames) {
            FieldConfig config = fieldsByName.get(fieldName);
            if (config != null && config.getFieldType() == FieldType.MASTER) {
                result.add(fieldName);
            }
        }
        return result;
    }
}