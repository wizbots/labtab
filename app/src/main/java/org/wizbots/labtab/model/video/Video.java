package org.wizbots.labtab.model.video;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
    private String id;
    private String mentor_id;
    private int status;
    private String path;
    private String title;
    private String category;
    private String mentor_name;
    private String lab_sku;
    private String lab_level;
    private String knowledge_nuggets;
    private String description;
    private String project_creators;
    private String notes_to_the_family;
    private String is_transCoding;
    private String edit_sync_status;
    private String video;
    private String videoId;
    private String programId;

    public Video() {
    }

    public Video(String id, String mentor_id, int status, String path, String title, String category, String mentor_name, String lab_sku, String lab_level, String knowledge_nuggets, String description, String project_creators, String notes_to_the_family, String is_transCoding, String edit_sync_status, String video, String videoId, String programId) {
        this.id = id;
        this.mentor_id = mentor_id;
        this.status = status;
        this.path = path;
        this.title = title;
        this.category = category;
        this.mentor_name = mentor_name;
        this.lab_sku = lab_sku;
        this.lab_level = lab_level;
        this.knowledge_nuggets = knowledge_nuggets;
        this.description = description;
        this.project_creators = project_creators;
        this.notes_to_the_family = notes_to_the_family;
        this.is_transCoding = is_transCoding;
        this.edit_sync_status = edit_sync_status;
        this.video = video;
        this.videoId = videoId;
        this.programId = programId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMentor_id() {
        return mentor_id;
    }

    public void setMentor_id(String mentor_id) {
        this.mentor_id = mentor_id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMentor_name() {
        return mentor_name;
    }

    public void setMentor_name(String mentor_name) {
        this.mentor_name = mentor_name;
    }

    public String getLab_sku() {
        return lab_sku;
    }

    public void setLab_sku(String lab_sku) {
        this.lab_sku = lab_sku;
    }

    public String getLab_level() {
        return lab_level;
    }

    public void setLab_level(String lab_level) {
        this.lab_level = lab_level;
    }

    public String getKnowledge_nuggets() {
        return knowledge_nuggets;
    }

    public void setKnowledge_nuggets(String knowledge_nuggets) {
        this.knowledge_nuggets = knowledge_nuggets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProject_creators() {
        return project_creators;
    }

    public void setProject_creators(String project_creators) {
        this.project_creators = project_creators;
    }

    public String getNotes_to_the_family() {
        return notes_to_the_family;
    }

    public void setNotes_to_the_family(String notes_to_the_family) {
        this.notes_to_the_family = notes_to_the_family;
    }

    public String getIs_transCoding() {
        return is_transCoding;
    }

    public void setIs_transCoding(String is_transCoding) {
        this.is_transCoding = is_transCoding;
    }

    public String getEdit_sync_status() {
        return edit_sync_status;
    }

    public void setEdit_sync_status(String edit_sync_status) {
        this.edit_sync_status = edit_sync_status;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public String getProgramId() {
        return programId;
    }

    public void setProgramId(String programId) {
        this.programId = programId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.mentor_id);
        dest.writeInt(this.status);
        dest.writeString(this.path);
        dest.writeString(this.title);
        dest.writeString(this.category);
        dest.writeString(this.mentor_name);
        dest.writeString(this.lab_sku);
        dest.writeString(this.lab_level);
        dest.writeString(this.knowledge_nuggets);
        dest.writeString(this.description);
        dest.writeString(this.project_creators);
        dest.writeString(this.notes_to_the_family);
        dest.writeString(this.is_transCoding);
        dest.writeString(this.edit_sync_status);
        dest.writeString(this.video);
        dest.writeString(this.videoId);
        dest.writeString(this.programId);
    }

    protected Video(Parcel in) {
        this.id = in.readString();
        this.mentor_id = in.readString();
        this.status = in.readInt();
        this.path = in.readString();
        this.title = in.readString();
        this.category = in.readString();
        this.mentor_name = in.readString();
        this.lab_sku = in.readString();
        this.lab_level = in.readString();
        this.knowledge_nuggets = in.readString();
        this.description = in.readString();
        this.project_creators = in.readString();
        this.notes_to_the_family = in.readString();
        this.is_transCoding = in.readString();
        this.edit_sync_status = in.readString();
        this.video = in.readString();
        this.videoId = in.readString();
        this.programId = in.readString();
    }

    public static final Creator<Video> CREATOR = new Creator<Video>() {
        @Override
        public Video createFromParcel(Parcel source) {
            return new Video(source);
        }

        @Override
        public Video[] newArray(int size) {
            return new Video[size];
        }
    };
}
