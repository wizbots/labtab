package org.wizbots.labtab.model.video;


import android.os.Parcel;
import android.os.Parcelable;

import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;

public class CreateProjectRequest implements Parcelable {
    private String id;
    private String mentor_id;
    private int status;
    private String path;
    private String title;
    private String category;
    private String mentor_name;
    private String lab_sku;
    private String lab_level;
    private ArrayList<String> knowledge_nuggets;
    private String description;
    private ArrayList<Student> project_creators;
    private String notes_to_the_family;
    private String program_id;

    public CreateProjectRequest() {
    }

    public CreateProjectRequest(String id, String mentor_id, int status, String path, String title, String category, String mentor_name, String lab_sku, String lab_level, ArrayList<String> knowledge_nuggets, String description, ArrayList<Student> project_creators, String notes_to_the_family, String program_id) {
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
        this.program_id = program_id;
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

    public ArrayList<String> getKnowledge_nuggets() {
        return knowledge_nuggets;
    }

    public void setKnowledge_nuggets(ArrayList<String> knowledge_nuggets) {
        this.knowledge_nuggets = knowledge_nuggets;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<Student> getProject_creators() {
        return project_creators;
    }

    public void setProject_creators(ArrayList<Student> project_creators) {
        this.project_creators = project_creators;
    }

    public String getNotes_to_the_family() {
        return notes_to_the_family;
    }

    public void setNotes_to_the_family(String notes_to_the_family) {
        this.notes_to_the_family = notes_to_the_family;
    }

    public String getProgram_id() {
        return program_id;
    }

    public void setProgram_id(String program_id) {
        this.program_id = program_id;
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
        dest.writeStringList(this.knowledge_nuggets);
        dest.writeString(this.description);
        dest.writeTypedList(this.project_creators);
        dest.writeString(this.notes_to_the_family);
        dest.writeString(this.program_id);
    }

    protected CreateProjectRequest(Parcel in) {
        this.id = in.readString();
        this.mentor_id = in.readString();
        this.status = in.readInt();
        this.path = in.readString();
        this.title = in.readString();
        this.category = in.readString();
        this.mentor_name = in.readString();
        this.lab_sku = in.readString();
        this.lab_level = in.readString();
        this.knowledge_nuggets = in.createStringArrayList();
        this.description = in.readString();
        this.project_creators = in.createTypedArrayList(Student.CREATOR);
        this.notes_to_the_family = in.readString();
        this.program_id = in.readString();
    }

    public static final Creator<CreateProjectRequest> CREATOR = new Creator<CreateProjectRequest>() {
        @Override
        public CreateProjectRequest createFromParcel(Parcel source) {
            return new CreateProjectRequest(source);
        }

        @Override
        public CreateProjectRequest[] newArray(int size) {
            return new CreateProjectRequest[size];
        }
    };
}
