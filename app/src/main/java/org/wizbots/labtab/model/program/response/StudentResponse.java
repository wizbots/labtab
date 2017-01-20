package org.wizbots.labtab.model.program.response;


public class StudentResponse {
    private String lab_time;
    private StatResponse stats;
    private String name;
    private String level;
    private int wizchips;
    private String special_needs;
    private boolean self_sign_out;
    private String pickup_instructions;
    private String id;

    public StudentResponse() {
    }

    public StudentResponse(String lab_time, StatResponse stats, String name, String level, int wizchips, String special_needs, boolean self_sign_out, String pickup_instructions, String id) {
        this.lab_time = lab_time;
        this.stats = stats;
        this.name = name;
        this.level = level;
        this.wizchips = wizchips;
        this.special_needs = special_needs;
        this.self_sign_out = self_sign_out;
        this.pickup_instructions = pickup_instructions;
        this.id = id;
    }

    public String getLab_time() {
        return lab_time;
    }

    public void setLab_time(String lab_time) {
        this.lab_time = lab_time;
    }

    public StatResponse getStats() {
        return stats;
    }

    public void setStats(StatResponse stats) {
        this.stats = stats;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public int getWizchips() {
        return wizchips;
    }

    public void setWizchips(int wizchips) {
        this.wizchips = wizchips;
    }

    public String getSpecial_needs() {
        return special_needs;
    }

    public void setSpecial_needs(String special_needs) {
        this.special_needs = special_needs;
    }

    public boolean isSelf_sign_out() {
        return self_sign_out;
    }

    public void setSelf_sign_out(boolean self_sign_out) {
        this.self_sign_out = self_sign_out;
    }

    public String getPickup_instructions() {
        return pickup_instructions;
    }

    public void setPickup_instructions(String pickup_instructions) {
        this.pickup_instructions = pickup_instructions;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
