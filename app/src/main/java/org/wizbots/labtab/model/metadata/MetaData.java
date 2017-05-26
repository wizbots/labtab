package org.wizbots.labtab.model.metadata;

import android.os.Parcel;
import android.os.Parcelable;

public class MetaData implements Parcelable {
    private int index;
    private String name;
    private String color1;
    private String color2;
    private String[] nuggets;
    private String[] projects;
    private int wiz_chips;

    public MetaData() {
    }

    public MetaData(int index, String name, String color1, String color2, String[] nuggets, String[] projects, int wiz_chips) {
        this.index = index;
        this.name = name;
        this.color1 = color1;
        this.color2 = color2;
        this.nuggets = nuggets;
        this.projects = projects;
        this.wiz_chips = wiz_chips;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor1() {
        return color1;
    }

    public void setColor1(String color1) {
        this.color1 = color1;
    }

    public String getColor2() {
        return color2;
    }

    public void setColor2(String color2) {
        this.color2 = color2;
    }

    public String[] getNuggets() {
        return nuggets;
    }

    public void setNuggets(String[] nuggets) {
        this.nuggets = nuggets;
    }

    public String[] getProjects() {
        return projects;
    }

    public void setProjects(String[] projects) {
        this.projects = projects;
    }

    public int getWiz_chips() {
        return wiz_chips;
    }

    public void setWiz_chips(int wiz_chips) {
        this.wiz_chips = wiz_chips;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeString(this.name);
        dest.writeString(this.color1);
        dest.writeString(this.color2);
        dest.writeStringArray(this.nuggets);
        dest.writeStringArray(this.projects);
        dest.writeInt(this.wiz_chips);
    }

    protected MetaData(Parcel in) {
        this.index = in.readInt();
        this.name = in.readString();
        this.color1 = in.readString();
        this.color2 = in.readString();
        this.nuggets = in.createStringArray();
        this.projects = in.createStringArray();
        this.wiz_chips = in.readInt();
    }

    public static final Parcelable.Creator<MetaData> CREATOR = new Parcelable.Creator<MetaData>() {
        @Override
        public MetaData createFromParcel(Parcel source) {
            return new MetaData(source);
        }

        @Override
        public MetaData[] newArray(int size) {
            return new MetaData[size];
        }
    };
}
