package org.wizbots.labtab.model.video.response;


public class EditProjectResponse {
    private String category;
    private String sku;
    private String description;
    private String title;
    private String notes;
    private boolean is_transcoding;
    private VideoResponse video;
    private String[] components;
    private String[] creators;
    private String id;

    public EditProjectResponse() {
    }

    public EditProjectResponse(String category, String sku, String description, String title, String notes, boolean is_transcoding, VideoResponse video, String[] components, String[] creators, String id) {
        this.category = category;
        this.sku = sku;
        this.description = description;
        this.title = title;
        this.notes = notes;
        this.is_transcoding = is_transcoding;
        this.video = video;
        this.components = components;
        this.creators = creators;
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public boolean is_transcoding() {
        return is_transcoding;
    }

    public void setIs_transcoding(boolean is_transcoding) {
        this.is_transcoding = is_transcoding;
    }

    public VideoResponse getVideo() {
        return video;
    }

    public void setVideo(VideoResponse video) {
        this.video = video;
    }

    public String[] getComponents() {
        return components;
    }

    public void setComponents(String[] components) {
        this.components = components;
    }

    public String[] getCreators() {
        return creators;
    }

    public void setCreators(String[] creators) {
        this.creators = creators;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
