package org.wizbots.labtab.interfaces;

import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.Mentor;

public interface GetMentorProfileListener extends BaseUIListener {
    void mentorProfileFetchedSuccessfully(Mentor mentor, CreateTokenResponse createTokenResponse);

    void unableToFetchMentor(int responseCode);
}
