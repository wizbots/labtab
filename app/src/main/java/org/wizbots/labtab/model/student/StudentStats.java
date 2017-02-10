package org.wizbots.labtab.model.student;


public class StudentStats {

    private String id;
    private String level;

    private int project_count;
    private String lab_time_count;
    private int done_count;
    private int skipped_count;
    private int pending_count;

    private String projects;
//    private String projects_done;
//    private String projects_skipped;
//    private String projects_pending;

    private int imagineering_count;
    private int programming_count;
    private int mechanisms_count;
    private int structures_count;

    private String projects_imagineering;
    private String projects_programming;
    private String projects_mechanisms;
    private String projects_structures;

    public StudentStats() {
    }

    public StudentStats(String id, String level, int project_count, String lab_time_count, int done_count, int skipped_count, int pending_count, String projects/*, String projects_done, String projects_skipped, String projects_pending*/, int imagineering_count, int programming_count, int mechanisms_count, int structures_count, String projects_imagineering, String projects_programming, String projects_mechanisms, String projects_structures) {
        this.id = id;
        this.level = level;
        this.project_count = project_count;
        this.lab_time_count = lab_time_count;
        this.done_count = done_count;
        this.skipped_count = skipped_count;
        this.pending_count = pending_count;
        this.projects = projects;
//        this.projects_done = projects_done;
//        this.projects_skipped = projects_skipped;
//        this.projects_pending = projects_pending;
        this.imagineering_count = imagineering_count;
        this.programming_count = programming_count;
        this.mechanisms_count = mechanisms_count;
        this.structures_count = structures_count;
        this.projects_imagineering = projects_imagineering;
        this.projects_programming = projects_programming;
        this.projects_mechanisms = projects_mechanisms;
        this.projects_structures = projects_structures;
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
        this.level = level;
    }

    public int getProject_count() {
        return project_count;
    }

    public void setProject_count(int project_count) {
        this.project_count = project_count;
    }

    public String getLab_time_count() {
        return lab_time_count;
    }

    public void setLab_time_count(String lab_time_count) {
        this.lab_time_count = lab_time_count;
    }

    public int getDone_count() {
        return done_count;
    }

    public void setDone_count(int done_count) {
        this.done_count = done_count;
    }

    public int getSkipped_count() {
        return skipped_count;
    }

    public void setSkipped_count(int skipped_count) {
        this.skipped_count = skipped_count;
    }

    public int getPending_count() {
        return pending_count;
    }

    public void setPending_count(int pending_count) {
        this.pending_count = pending_count;
    }

    public String getProjects() {
        return projects;
    }

    public void setProjects(String projects) {
        this.projects = projects;
    }

//    public String getProjects_done() {
//        return projects_done;
//    }
//
//    public void setProjects_done(String projects_done) {
//        this.projects_done = projects_done;
//    }
//
//    public String getProjects_skipped() {
//        return projects_skipped;
//    }
//
//    public void setProjects_skipped(String projects_skipped) {
//        this.projects_skipped = projects_skipped;
//    }
//
//    public String getProjects_pending() {
//        return projects_pending;
//    }
//
//    public void setProjects_pending(String projects_pending) {
//        this.projects_pending = projects_pending;
//    }

    public int getImagineering_count() {
        return imagineering_count;
    }

    public void setImagineering_count(int imagineering_count) {
        this.imagineering_count = imagineering_count;
    }

    public int getProgramming_count() {
        return programming_count;
    }

    public void setProgramming_count(int programming_count) {
        this.programming_count = programming_count;
    }

    public int getMechanisms_count() {
        return mechanisms_count;
    }

    public void setMechanisms_count(int mechanisms_count) {
        this.mechanisms_count = mechanisms_count;
    }

    public int getStructures_count() {
        return structures_count;
    }

    public void setStructures_count(int structures_count) {
        this.structures_count = structures_count;
    }

    public String getProjects_imagineering() {
        return projects_imagineering;
    }

    public void setProjects_imagineering(String projects_imagineering) {
        this.projects_imagineering = projects_imagineering;
    }

    public String getProjects_programming() {
        return projects_programming;
    }

    public void setProjects_programming(String projects_programming) {
        this.projects_programming = projects_programming;
    }

    public String getProjects_mechanisms() {
        return projects_mechanisms;
    }

    public void setProjects_mechanisms(String projects_mechanisms) {
        this.projects_mechanisms = projects_mechanisms;
    }

    public String getProjects_structures() {
        return projects_structures;
    }

    public void setProjects_structures(String projects_structures) {
        this.projects_structures = projects_structures;
    }
}
