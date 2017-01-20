package org.wizbots.labtab.model.program;

import android.os.Parcel;
import android.os.Parcelable;

public class Program implements Parcelable {
    private String id;
    private String member_id;
    private String grades;
    private String location;
    private String starts;
    private int capacity;
    private String time_slot;
    private String availability;
    private String name;
    private String sessions;
    private String category;
    private int sku;
    private String ends;
    private String room;
    private String season;
    private String price;

    public Program() {
    }

    public Program(String id, String member_id, String grades, String location, String starts, int capacity, String time_slot, String availability, String name, String sessions, String category, int sku, String ends, String room, String season, String price) {
        this.id = id;
        this.member_id = member_id;
        this.grades = grades;
        this.location = location;
        this.starts = starts;
        this.capacity = capacity;
        this.time_slot = time_slot;
        this.availability = availability;
        this.name = name;
        this.sessions = sessions;
        this.category = category;
        this.sku = sku;
        this.ends = ends;
        this.room = room;
        this.season = season;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
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

    public String getSessions() {
        return sessions;
    }

    public void setSessions(String sessions) {
        this.sessions = sessions;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.member_id);
        dest.writeString(this.grades);
        dest.writeString(this.location);
        dest.writeString(this.starts);
        dest.writeInt(this.capacity);
        dest.writeString(this.time_slot);
        dest.writeString(this.availability);
        dest.writeString(this.name);
        dest.writeString(this.sessions);
        dest.writeString(this.category);
        dest.writeInt(this.sku);
        dest.writeString(this.ends);
        dest.writeString(this.room);
        dest.writeString(this.season);
        dest.writeString(this.price);
    }

    protected Program(Parcel in) {
        this.id = in.readString();
        this.member_id = in.readString();
        this.grades = in.readString();
        this.location = in.readString();
        this.starts = in.readString();
        this.capacity = in.readInt();
        this.time_slot = in.readString();
        this.availability = in.readString();
        this.name = in.readString();
        this.sessions = in.readString();
        this.category = in.readString();
        this.sku = in.readInt();
        this.ends = in.readString();
        this.room = in.readString();
        this.season = in.readString();
        this.price = in.readString();
    }

    public static final Parcelable.Creator<Program> CREATOR = new Parcelable.Creator<Program>() {
        @Override
        public Program createFromParcel(Parcel source) {
            return new Program(source);
        }

        @Override
        public Program[] newArray(int size) {
            return new Program[size];
        }
    };
}
