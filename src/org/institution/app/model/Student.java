package org.institution.app.model;

public class Student {
    private final int id;
    private String name;
    private int age;
    private String email;
    private double averageGrade;
    private boolean isActive;

    public Student(int id, String name, int age, String email) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.averageGrade = -0.1;
        this.isActive = true;
    }

    // FOR LOADING DATA
    public Student(int id, String name, int age, String email, double averageGrade, boolean isActive) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.email = email;
        this.averageGrade = averageGrade;
        this.isActive = isActive;
    }

    // GETTERS
    public int getId() { return id; }
    public String getName() { return name; }
    public int getAge() { return age; }
    public String getEmail() { return email; }
    public double getAverageGrade() { return averageGrade; }
    public boolean isActive() { return isActive; }

    // SETTERS
    public void setName(String name) { this.name = name; }
    public void setAge(int age) { this.age = age; }
    public void setEmail(String email) { this.email = email; }
    public void setAverageGrade(double averageGrade) { this.averageGrade = averageGrade; }
    public void setIsActive(boolean isActive) { this.isActive = isActive;}



}
