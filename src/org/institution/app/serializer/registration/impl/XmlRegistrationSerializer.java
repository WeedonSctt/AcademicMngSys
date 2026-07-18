package org.institution.app.serializer.registration.impl;

import org.institution.app.serializer.registration.RegistrationSerializer;
import org.institution.app.model.Registration;
import java.util.List;

public class XmlRegistrationSerializer implements RegistrationSerializer {
    @Override
    public String export(List<Registration> registrations) {
        int indentation = 0;
        StringBuilder xml = new StringBuilder();

        xml.append("<registrations>");
        indentation++;

        for (int i = 0; i < registrations.size(); i++) {
            objectToXml(xml, registrations.get(i), indentation);
        }

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</registrations>");
        
        return xml.toString();
    }

    @Override
    public String getExtension() { return "xml"; }
    
    private static void indent(StringBuilder xml, int times) {        
        for (int i = 0; i < times; i++) {
            xml.append("\t");
        }
    }
    
    private static void objectToXml(StringBuilder xml, Registration r, int indentation) {
        xml.append("\n");
        indent(xml, indentation);
        xml.append("<registration>");
        indentation++;
        
        appendTag(xml, "studentId", r.getStudentId(), indentation);
        appendTag(xml, "courseId", r.getCourseId(), indentation);
        appendTag(xml, "grade", r.getGrade(), indentation);

        indentation--;
        xml.append("\n");
        indent(xml, indentation);
        xml.append("</registration>");
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
}
