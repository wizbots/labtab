package org.wizbots.labtab.model.program.response;

public class StatResponse {
    private int completed;
    private int skipped;
    private int pending;

    public StatResponse() {
    }

    public StatResponse(int completed, int skipped, int pending) {
        this.completed = completed;
        this.skipped = skipped;
        this.pending = pending;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public int getSkipped() {
        return skipped;
    }

    public void setSkipped(int skipped) {
        this.skipped = skipped;
    }

    public int getPending() {
        return pending;
    }

    public void setPending(int pending) {
        this.pending = pending;
    }
}
