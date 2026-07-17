package org.institution.app.service.impl;

import org.institution.app.service.StudentExportService;
import org.institution.app.model.Student;
import java.util.List;

public class JsonStudentExportImpl implements StudentExportService {
    public JsonStudentExportImpl() {}
    
    @Override
    public String export(List<Student> students) {
        int indentation = 0;
        StringBuilder json = new StringBuilder();

        json.append("[\n");
        indentation++;
        indent(json, indentation);

        for (int i = 0; i < students.size(); i++) {
            objectToJson(json, students.get(i), indentation);
            
            if (i + 1 != students.size()) {
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

    private static void objectToJson(StringBuilder json, Student s, int indentation) {
        json.append("{\n"); indentation++;

        appendField(json, "id", s.getId(), indentation, true);
        appendField(json, "name", s.getName(), indentation, false);
        appendField(json, "age", s.getAge(), indentation, false);
        appendField(json, "email", s.getEmail(), indentation, false);
        appendField(json, "averageGrade", s.getAverageGrade(), indentation, false);
        appendField(json, "isActive", s.isActive(), indentation, false);

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

    private static void appendField(StringBuilder json, String key, double value, int indentation, boolean isFirst) {
        beginField(json, isFirst, indentation);
        appendKey(json, key);
        json.append(value);
    }

    private static void appendField(StringBuilder json, String key, boolean value, int indentation, boolean isFirst) {
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
