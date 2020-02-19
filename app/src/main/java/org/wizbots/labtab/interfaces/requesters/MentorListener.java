package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

public interface MentorListener extends BaseUIListener {

     void onMentorSuccess();
     void onMentorFailure();
}
