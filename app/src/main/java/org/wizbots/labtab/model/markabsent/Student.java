package org.wizbots.labtab.model.markabsent;

public class Student {
    private String id;
    private String level;

    public Student() {
    }

    public Student(String id, String level) {
        this.id = id;
        this.level = level;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLevel() {
        return level == null ? null : level.toUpperCase();
    }

    public void setLevel(String level) {
        this.level = level.toUpperCase();
    }
}
