package org.wizbots.labtab.model.program.response;


import java.util.ArrayList;

public class ProgramResponse {
    private String id;
    private String grades;
    private String location;
    private String starts;
    private int capacity;
    private String time_slot;
    private String availability;
    private String name;
    private String category;
    private int sku;
    private String ends;
    private String room;
    private String season;
    private String price;
    private ArrayList<SessionResponse> sessions;
    private ArrayList<StudentResponse> students;
    private ArrayList<AbsenceResponse> absences;

    public ProgramResponse() {
    }

    public ProgramResponse(String id, String grades, String location, String starts, int capacity, String time_slot, String availability, String name, String category, int sku, String ends, String room, String season, String price, ArrayList<SessionResponse> sessions, ArrayList<StudentResponse> students, ArrayList<AbsenceResponse> absences) {
        this.id = id;
        this.grades = grades;
        this.location = location;
        this.starts = starts;
        this.capacity = capacity;
        this.time_slot = time_slot;
        this.availability = availability;
        this.name = name;
        this.category = category;
        this.sku = sku;
        this.ends = ends;
        this.room = room;
        this.season = season;
        this.price = price;
        this.sessions = sessions;
        this.students = students;
        this.absences = absences;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGrades() {
        return grades;
    }

    public void setGrades(String grades) {
        this.grades = grades;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getTime_slot() {
        return time_slot;
    }

    public void setTime_slot(String time_slot) {
        this.time_slot = time_slot;
    }

    public String getAvailability() {
        return availability;
    }

    public void setAvailability(String availability) {
        this.availability = availability;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public ArrayList<SessionResponse> getSessions() {
        return sessions;
    }

    public void setSessions(ArrayList<SessionResponse> sessions) {
        this.sessions = sessions;
    }

    public ArrayList<StudentResponse> getStudents() {
        return students;
    }

    public void setStudents(ArrayList<StudentResponse> students) {
        this.students = students;
    }

    public ArrayList<AbsenceResponse> getAbsences() {
        return absences;
    }

    public void setAbsences(ArrayList<AbsenceResponse> absences) {
        this.absences = absences;
    }
}
