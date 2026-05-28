package org.institution.app.model;

public class Registration {
    private final int studentId;
    private final int courseId;
    private double grade;

    public Registration(int studentId, int courseId) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = 0.0;
    }

    // FOR LOADING DATA
    public Registration(int studentId, int courseId, double grade) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.grade = grade;
    }

    // GETTERS
    public int getStudentId() { return studentId; }
    public int getCourseId() { return courseId; }
    public double getGrade() { return grade; }

    public void setGrade(double grade) { this.grade = grade; }

}
