package org.wizbots.labtab.model;

import android.os.Parcel;
import android.os.Parcelable;

public class ProgramOrLab implements Parcelable {
    private int sku;
    private String member_id;
    private String ends;
    private String title;
    private String starts;
    private String state;
    private String street;
    private int enrollment_count;
    private String address;
    private String id;
    private String season;
    private String location;
    private String level;
    private String year;
    private long startTimeStamp;
    private long endTimesStamp;


    public ProgramOrLab() {
    }

    public ProgramOrLab(int sku, String member_id, String ends, String title, String starts, String state, String street, int enrollment_count, String address, String id, String season, String location, String level, String year, long startTimeStamp, long endTimesStamp) {
        this.sku = sku;
        this.member_id = member_id;
        this.ends = ends;
        this.title = title;
        this.starts = starts;
        this.state = state;
        this.street = street;
        this.enrollment_count = enrollment_count;
        this.address = address;
        this.id = id;
        this.season = season;
        this.location = location;
        this.level = level;
        this.year = year;
        this.startTimeStamp = startTimeStamp;
        this.endTimesStamp = endTimesStamp;
    }

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getEnds() {
        return ends;
    }

    public void setEnds(String ends) {
        this.ends = ends;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStarts() {
        return starts;
    }

    public void setStarts(String starts) {
        this.starts = starts;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public int getEnrollment_count() {
        return enrollment_count;
    }

    public void setEnrollment_count(int enrollment_count) {
        this.enrollment_count = enrollment_count;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSeason() {
        return season;
    }

    public void setSeason(String season) {
        this.season = season;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public long getStartTimeStamp() {
        return startTimeStamp;
    }

    public void setStartTimeStamp(long startTimeStamp) {
        this.startTimeStamp = startTimeStamp;
    }

    public long getEndTimesStamp() {
        return endTimesStamp;
    }

    public void setEndTimesStamp(long endTimesStamp) {
        this.endTimesStamp = endTimesStamp;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.sku);
        dest.writeString(this.member_id);
        dest.writeString(this.ends);
        dest.writeString(this.title);
        dest.writeString(this.starts);
        dest.writeString(this.state);
        dest.writeString(this.street);
        dest.writeInt(this.enrollment_count);
        dest.writeString(this.address);
        dest.writeString(this.id);
        dest.writeString(this.season);
        dest.writeString(this.location);
        dest.writeString(this.level);
        dest.writeString(this.year);
        dest.writeLong(this.startTimeStamp);
        dest.writeLong(this.endTimesStamp);
    }

    protected ProgramOrLab(Parcel in) {
        this.sku = in.readInt();
        this.member_id = in.readString();
        this.ends = in.readString();
        this.title = in.readString();
        this.starts = in.readString();
        this.state = in.readString();
        this.street = in.readString();
        this.enrollment_count = in.readInt();
        this.address = in.readString();
        this.id = in.readString();
        this.season = in.readString();
        this.location = in.readString();
        this.level = in.readString();
        this.year = in.readString();
        this.startTimeStamp = in.readLong();
        this.endTimesStamp = in.readLong();
    }

    public static final Creator<ProgramOrLab> CREATOR = new Creator<ProgramOrLab>() {
        @Override
        public ProgramOrLab createFromParcel(Parcel source) {
            return new ProgramOrLab(source);
        }

        @Override
        public ProgramOrLab[] newArray(int size) {
            return new ProgramOrLab[size];
        }
    };
}
