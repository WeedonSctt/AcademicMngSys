package org.institution.app.service.export.teacher.impl;

import org.institution.app.service.export.teacher.TeacherExportService;
import org.institution.app.model.Teacher;
import java.util.List;

public class TeacherXmlExporterImpl implements TeacherExportService {
    @Override
    public String export(List<Teacher> teachers) {
        int indentation = 0;
        StringBuilder xml = new StringBuilder();

        xml.append("<teachers>");
        indentation++;

        for (int i = 0; i < teachers.size(); i++) {
            objectToXml(xml, teachers.get(i), indentation);
        }

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</teachers>");
        
        return xml.toString();
    }
    
    private static void indent(StringBuilder xml, int times) {        
        for (int i = 0; i < times; i++) {
            xml.append("\t");
        }
    }
    
    private static void objectToXml(StringBuilder xml, Teacher t, int indentation) {
        xml.append("\n");
        indent(xml, indentation);
        xml.append("<teacher>");
        indentation++;
        
        appendTag(xml, "id", t.getID(), indentation);
        appendTag(xml, "name", t.getName(), indentation);
        appendTag(xml, "department", t.getDepartment(), indentation);
        appendTag(xml, "email", t.getEmail(), indentation);

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</teacher>");
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
