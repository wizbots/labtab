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
    private String labLevel;

    public ProgramOrLab() {
    }

    public ProgramOrLab(int sku, String member_id, String ends, String title, String starts, String state, String street, int enrollment_count, String address, String id, String labLevel) {
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
        this.labLevel = labLevel;
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

    public String getLabLevel() {
        return labLevel;
    }

    public void setLabLevel(String labLevel) {
        this.labLevel = labLevel;
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
        dest.writeString(this.labLevel);
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
        this.labLevel = in.readString();
    }

    public static final Parcelable.Creator<ProgramOrLab> CREATOR = new Parcelable.Creator<ProgramOrLab>() {
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
