package org.wizbots.labtab.interfaces.requesters;

public interface OnRosterDetailsListener {
    void onRosterDetailsSuccess(String rosterTitle);
    void onRosterDetailsError(int responseCode);
}
