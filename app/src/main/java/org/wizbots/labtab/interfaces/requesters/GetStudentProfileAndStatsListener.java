package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

public interface GetStudentProfileAndStatsListener extends BaseUIListener {
    void studentProfileFetchedSuccessfully();

    void unableToFetchStudent(int responseCode);
}
