package org.institution.app.service.export.teacher.impl;

import org.institution.app.service.export.teacher.TeacherExportService;
import org.institution.app.model.Teacher;
import java.util.List;

public class TeacherJsonExporterImpl implements TeacherExportService {
    @Override
    public String export(List<Teacher> teachers) {
        int indentation = 0;
        StringBuilder json = new StringBuilder();

        json.append("[\n");
        indentation++;
        indent(json, indentation);

        for (int i = 0; i < teachers.size(); i++) {
            objectToJson(json, teachers.get(i), indentation);
            
            if (i + 1 != teachers.size()) {
                json.append(",\n");
                indent(json, indentation);
            }
        }
        
        indentation--;
        json.append("\n"); 
        indent(json, indentation);
        json.append("]");

        return json.toString();
    }

    private static void objectToJson(StringBuilder json, Teacher t, int indentation) {
        json.append("{\n"); indentation++;

        appendField(json, "id", t.getID(), indentation, true);
        appendField(json, "name", t.getName(), indentation, false);
        appendField(json, "department", t.getDepartment(), indentation, false);
        appendField(json, "email", t.getEmail(), indentation, false);

        indentation--;
        indent(json, indentation);
        json.append("}");
    }

    private static void indent(StringBuilder json, int times) {        
        for (int i = 0; i < times; i++) {
            json.append("\t");
        }
    }

    private static String escape(String string) {
        if (string == null) {
            return null;
        }

        StringBuilder str = new StringBuilder();

        for (char c : string.toCharArray()) {
            switch (c) {
                case '"':
                    str.append("\\\"");
                    break;
                case '\\':
                    str.append("\\\\");
                    break;
                case '\b':
                    str.append("\\b");
                    break;
                case '\f':
                    str.append("\\f");
                    break;
                case '\n':
                    str.append("\\n");
                    break;
                case '\r':
                    str.append("\\r");
                    break;
                case '\t':
                    str.append("\\t");
                    break;
                default:
                    str.append(c);
            }
        }

        return str.toString();
    }

    private static void appendField(StringBuilder json, String key, String value, int indentation, boolean isFirst) {
        beginField(json, isFirst, indentation);
        appendKey(json, key);
        appendString(json, escape(value));
    }

    private static void appendField(StringBuilder json, String key, int value, int indentation, boolean isFirst) {
        beginField(json, isFirst, indentation);
        appendKey(json, key);
        json.append(value);
    }

    private static void appendString(StringBuilder json, String value) {
        if (value == null) {
            json.append("null");
        } else {
            json.append("\"")
                .append(value)
                .append("\"");
        }
    }

    private static void beginField(StringBuilder json, boolean isFirst, int indentation) {
        if (!isFirst) json.append(",\n");
        indent(json, indentation);
    }

    private static void appendKey(StringBuilder json, String key) {
        json.append('"')
            .append(key)
            .append("\": ");
    }

}
