package org.wizbots.labtab.model;

public class VideoList {
    private String thumbnailLinkOrPath;
    private String labLevel;
    private String videoName;
    private int videoStatus;

    public VideoList() {
    }

    public VideoList(String thumbnailLinkOrPath, String labLevel, String videoName, int videoStatus) {
        this.thumbnailLinkOrPath = thumbnailLinkOrPath;
        this.labLevel = labLevel;
        this.videoName = videoName;
        this.videoStatus = videoStatus;
    }

    public String getThumbnailLinkOrPath() {
        return thumbnailLinkOrPath;
    }

    public void setThumbnailLinkOrPath(String thumbnailLinkOrPath) {
        this.thumbnailLinkOrPath = thumbnailLinkOrPath;
    }

    public String getLabLevel() {
        return labLevel;
    }

    public void setLabLevel(String labLevel) {
        this.labLevel = labLevel;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }

    public int getVideoStatus() {
        return videoStatus;
    }

    public void setVideoStatus(int videoStatus) {
        this.videoStatus = videoStatus;
    }
}
