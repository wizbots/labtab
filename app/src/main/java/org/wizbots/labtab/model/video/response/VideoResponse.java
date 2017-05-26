package org.wizbots.labtab.model.video.response;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoResponse implements Parcelable {
    private String status;
    private String url;
    private String id;
    private String et_job_status;

    public VideoResponse() {
    }

    public VideoResponse(String status, String url, String id, String et_job_status) {
        this.status = status;
        this.url = url;
        this.id = id;
        this.et_job_status = et_job_status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEt_job_status() {
        return et_job_status;
    }

    public void setEt_job_status(String et_job_status) {
        this.et_job_status = et_job_status;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.url);
        dest.writeString(this.id);
        dest.writeString(this.et_job_status);
    }

    protected VideoResponse(Parcel in) {
        this.status = in.readString();
        this.url = in.readString();
        this.id = in.readString();
        this.et_job_status = in.readString();
    }

    public static final Parcelable.Creator<VideoResponse> CREATOR = new Parcelable.Creator<VideoResponse>() {
        @Override
        public VideoResponse createFromParcel(Parcel source) {
            return new VideoResponse(source);
        }

        @Override
        public VideoResponse[] newArray(int size) {
            return new VideoResponse[size];
        }
    };
}
