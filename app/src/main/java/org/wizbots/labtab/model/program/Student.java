package org.wizbots.labtab.model.program;

import android.os.Parcel;
import android.os.Parcelable;

public class Student implements Parcelable {
    private String program_id;
    private String member_id;
    private String student_id;
    private String lab_time;
    private int completed;
    private int skipped;
    private int pending;
    private String name;
    private String level;
    private int wizchips;
    private String special_needs;
    private int self_sign_out;
    private String pickup_instructions;
    private int projects;
    private boolean check;
    private boolean isSync;
    private int offlinewizchips;
    private boolean closeToNextLevel;

    public Student() {
    }

    public Student(String program_id, String member_id, String student_id, String lab_time,
                   int completed, int skipped, int pending, String name, String level, int wizchips,
                   String special_needs, int self_sign_out, String pickup_instructions, boolean isSync, int offlinewizchips) {
        this.program_id = program_id;
        this.member_id = member_id;
        this.student_id = student_id;
        this.lab_time = lab_time;
        this.completed = completed;
        this.skipped = skipped;
        this.pending = pending;
        this.name = name;
        this.level = level;
        this.wizchips = wizchips;
        this.special_needs = special_needs;
        this.self_sign_out = self_sign_out;
        this.pickup_instructions = pickup_instructions;
        this.isSync = isSync;
        this.offlinewizchips = offlinewizchips;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getStudent_id() {
        return student_id;
    }

    public void setStudent_id(String student_id) {
        this.student_id = student_id;
    }

    public String getLab_time() {
        return lab_time;
    }

    public void setLab_time(String lab_time) {
        this.lab_time = lab_time;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
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

    public int getSelf_sign_out() {
        return self_sign_out;
    }

    public void setSelf_sign_out(int self_sign_out) {
        this.self_sign_out = self_sign_out;
    }

    public String getPickup_instructions() {
        return pickup_instructions;
    }

    public void setPickup_instructions(String pickup_instructions) {
        this.pickup_instructions = pickup_instructions;
    }

    public int getProjects() {
        return this.projects = this.completed + this.skipped + this.pending;
    }

    public void setProjects(int projects) {
        this.projects = projects;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public boolean isSync() {
        return isSync;
    }

    public void setSync(boolean sync) {
        isSync = sync;
    }

    public boolean isCloseToNextLevel() {
        return closeToNextLevel;
    }

    public void setCloseToNextLevel(boolean closeToNextLevel) {
        this.closeToNextLevel = closeToNextLevel;
    }

    public int getOfflinewizchips() {
        return offlinewizchips;
    }

    public void setOfflinewizchips(int offlinewizchips) {
        this.offlinewizchips = offlinewizchips;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.program_id);
        dest.writeString(this.member_id);
        dest.writeString(this.student_id);
        dest.writeString(this.lab_time);
        dest.writeInt(this.completed);
        dest.writeInt(this.skipped);
        dest.writeInt(this.pending);
        dest.writeString(this.name);
        dest.writeString(this.level);
        dest.writeInt(this.wizchips);
        dest.writeString(this.special_needs);
        dest.writeInt(this.self_sign_out);
        dest.writeString(this.pickup_instructions);
        dest.writeInt(this.projects);
        dest.writeByte(this.check ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isSync ? (byte) 1 : (byte) 0);
        dest.writeInt(this.offlinewizchips);
        dest.writeByte(this.closeToNextLevel ? (byte) 1 : (byte) 0);
    }

    protected Student(Parcel in) {
        this.program_id = in.readString();
        this.member_id = in.readString();
        this.student_id = in.readString();
        this.lab_time = in.readString();
        this.completed = in.readInt();
        this.skipped = in.readInt();
        this.pending = in.readInt();
        this.name = in.readString();
        this.level = in.readString();
        this.wizchips = in.readInt();
        this.special_needs = in.readString();
        this.self_sign_out = in.readInt();
        this.pickup_instructions = in.readString();
        this.projects = in.readInt();
        this.check = in.readByte() != 0;
        this.isSync = in.readByte() != 0;
        this.offlinewizchips = in.readInt();
        this.closeToNextLevel = in.readByte() != 0;
    }

    public static final Creator<Student> CREATOR = new Creator<Student>() {
        @Override
        public Student createFromParcel(Parcel source) {
            return new Student(source);
        }

        @Override
        public Student[] newArray(int size) {
            return new Student[size];
        }
    };
}
