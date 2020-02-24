package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

public interface OnRosterDetailsListener extends BaseUIListener {
    void onRosterDetailsSuccess(String rosterTitle);
    void onRosterDetailsError(int responseCode);
}
