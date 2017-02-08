package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.interfaces.requesters.GetProgramOrLabListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.retrofit.LabTabResponse;

import java.util.ArrayList;

public class ProgramOrLabRequester implements Runnable, LabTabConstants {

    @Override
    public void run() {
        LabTabResponse<ArrayList<ProgramOrLab>> programsOrLabs = LabTabHTTPOperationController.getProgramsOrLabsUsingFromAndTo("2013-01-01", "2017-12-31");
        if (programsOrLabs != null) {
            ArrayList<ProgramOrLab> programOrLabArrayList = programsOrLabs.getResponse();
            String member_id = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id();
            for (ProgramOrLab programOrLab : programOrLabArrayList) {
                String seasonYear[] = programOrLab.getSeason().split(" - ");
                programOrLab.setMember_id(member_id);
                programOrLab.setSeason(seasonYear[1]);
                programOrLab.setYear(seasonYear[0]);
            }
            for (GetProgramOrLabListener getProgramOrLabListener : LabTabApplication.getInstance().getUIListeners(GetProgramOrLabListener.class)) {
                if (programsOrLabs.getResponseCode() == StatusCode.OK) {
                    ProgramsOrLabsTable.getInstance().insert(programOrLabArrayList);
                    getProgramOrLabListener.programOrLabFetchedSuccessfully(programOrLabArrayList);
                } else {
                    getProgramOrLabListener.unableToFetchPrograms(programsOrLabs.getResponseCode());
                }
            }
        } else {
            for (GetProgramOrLabListener getProgramOrLabListener : LabTabApplication.getInstance().getUIListeners(GetProgramOrLabListener.class)) {
                getProgramOrLabListener.unableToFetchPrograms(0);
            }
        }
    }

}
