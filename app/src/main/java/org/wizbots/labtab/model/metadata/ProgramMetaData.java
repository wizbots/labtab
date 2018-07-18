package org.wizbots.labtab.model.metadata;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by root on 12/7/18.
 */

public class ProgramMetaData implements Parcelable {

    private MetaData[] levels;
    private String[]  categories;

    public ProgramMetaData(MetaData[] level, String[] categories) {
        this.levels = level;
        this.categories = categories;
    }

    public ProgramMetaData() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedArray(this.levels, flags);
        dest.writeStringArray(this.categories);
    }



    protected ProgramMetaData(Parcel in) {
        this.levels = in.createTypedArray(MetaData.CREATOR);
        this.categories = in.createStringArray();
    }

    public static final Parcelable.Creator<ProgramMetaData> CREATOR = new Parcelable.Creator<ProgramMetaData>() {
        @Override
        public ProgramMetaData createFromParcel(Parcel source) {
            return new ProgramMetaData(source);
        }

        @Override
        public ProgramMetaData[] newArray(int size) {
            return new ProgramMetaData[size];
        }
    };

    public MetaData[] getLevel() {
        return levels;
    }

    public void setLevel(MetaData[] level) {
        this.levels = level;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }
}
