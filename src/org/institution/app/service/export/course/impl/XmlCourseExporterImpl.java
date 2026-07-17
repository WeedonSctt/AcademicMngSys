package org.institution.app.service.export.course.impl;

import org.institution.app.service.export.course.CourseExportService;
import org.institution.app.model.Course;
import java.util.List;

public class XmlCourseExporterImpl implements CourseExportService {
    @Override
    public String export(List<Course> courses) {
        int indentation = 0;
        StringBuilder xml = new StringBuilder();

        xml.append("<courses>");
        indentation++;

        for (int i = 0; i < courses.size(); i++) {
            objectToXml(xml, courses.get(i), indentation);
        }

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</courses>");
        
        return xml.toString();
    }
    
    private static void indent(StringBuilder xml, int times) {        
        for (int i = 0; i < times; i++) {
            xml.append("\t");
        }
    }
    
    private static void objectToXml(StringBuilder xml, Course c, int indentation) {
        xml.append("\n");
        indent(xml, indentation);
        xml.append("<course>");
        indentation++;
        
        appendTag(xml, "id", c.getId(), indentation);
        appendTag(xml, "name", c.getName(), indentation);
        appendTag(xml, "description", c.getDescription(), indentation);
        appendTag(xml, "maximumStudents", c.getMaximumStudents(), indentation);
        appendTag(xml, "teacherId", c.getTeacherId(), indentation);

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</course>");
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
