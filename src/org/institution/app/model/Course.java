package org.institution.app.model;

 public class Course {
    private final int id;
    private String name;
    private String description;
    private int maximumStudents;
    private int teacherId;

    public Course(int id, String name, String description, int maximumStudents, int teacherId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.maximumStudents = maximumStudents;
        this.teacherId = teacherId;
    }

    // GETTERS
    public int getId() { return id; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getMaximumStudents() { return maximumStudents; }
    public int getTeacherId() { return teacherId; }

    // SETTERS
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setMaximumStudents(int max) { this.maximumStudents = max; }
    public void setTeacherId(int teacherId) { this.teacherId = teacherId; }

 }
