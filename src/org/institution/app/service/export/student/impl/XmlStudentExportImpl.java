package org.institution.app.service.export.student.impl;

import org.institution.app.service.export.student.StudentExportService;
import org.institution.app.model.Student;
import java.util.List;

public class XmlStudentExportImpl implements StudentExportService {
    @Override
    public String export(List<Student> students) {
        int indentation = 0;
        StringBuilder xml = new StringBuilder();

        xml.append("<students>");
        indentation++;

        for (int i = 0; i < students.size(); i++) {
            objectToXml(xml, students.get(i), indentation);
        }

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</students>");
        
        return xml.toString();
    }
    
    private static void indent(StringBuilder xml, int times) {        
        for (int i = 0; i < times; i++) {
            xml.append("\t");
        }
    }
    
    private static void objectToXml(StringBuilder xml, Student s, int indentation) {
        xml.append("\n");
        indent(xml, indentation);
        xml.append("<student>");
        indentation++;
        
        appendTag(xml, "id", s.getId(), indentation);
        appendTag(xml, "name", s.getName(), indentation);
        appendTag(xml, "age", s.getAge(), indentation);
        appendTag(xml, "email", s.getEmail(), indentation);
        appendTag(xml, "averageGrade", s.getAverageGrade(), indentation);
        appendTag(xml, "isActive", s.isActive(), indentation);

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</student>");
    }

    private static void appendTag(StringBuilder xml, String key, String value, int indentation){
        beginTag(xml, indentation);
        openTag(xml, key);
        xml.append(escape(value));
        closeTag(xml, key);
    }

    private static void appendTag(StringBuilder xml, String key, int value, int indentation) {
        beginTag(xml, indentation);
        openTag(xml, key);
        xml.append(value);
        closeTag(xml, key);
    }

    private static void appendTag(StringBuilder xml, String key, double value, int indentation) {
        beginTag(xml, indentation);
        openTag(xml, key);
        xml.append(value);
        closeTag(xml, key);
    }

    private static void appendTag(StringBuilder xml, String key, boolean value, int indentation) {
        beginTag(xml, indentation);
        openTag(xml, key);
        xml.append(value);
        closeTag(xml, key);
    }

    private static void openTag(StringBuilder xml, String key) {
        xml.append("<");
        xml.append(key);
        xml.append(">");
    }

    private static void closeTag(StringBuilder xml, String key) {
        xml.append("</");
        xml.append(key);
        xml.append(">");
    }

    private static void beginTag(StringBuilder xml, int indentation) {
        xml.append("\n");
        indent(xml, indentation);
    }

    private static String escape(String str) {
        if (str == null) return "";
        
        StringBuilder string = new StringBuilder();

        for (char c : str.toCharArray()) {
            switch (c) {
                case '&': {
                    string.append("&amp;");
                    break;
                } case '<': {
                    string.append("&lt;");
                    break;
                } case '>': {
                    string.append("&gt;");
                    break;
                } case '"': {
                    string.append("&quot;");
                    break;
                } case '\'': {
                    string.append("&apos;");
                    break;
                }
                default: {
                    string.append(c);
                    break;
                }
            }
        }

        return string.toString();
    }
    
}
