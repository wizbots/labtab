package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.ProgramOrLab;

import java.util.ArrayList;

public interface GetProgramOrLabListener extends BaseUIListener {
    void programOrLabFetchedSuccessfully(ArrayList<ProgramOrLab> programOrLabs);

    void unableToFetchPrograms(int responseCode);
}
