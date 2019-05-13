package org.wizbots.labtab.interfaces.requesters;

public interface OnRosterDetailsListener {
    void onRosterDetailsSuccess(String rosterId);
    void onRosterDetailsError(int responseCode);
}
