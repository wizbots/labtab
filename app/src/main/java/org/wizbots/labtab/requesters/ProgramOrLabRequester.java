package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.interfaces.GetProgramOrLabListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

public class ProgramOrLabRequester implements Runnable, LabTabConstants {

    @Override
    public void run() {
        LabTabResponse<ArrayList<ProgramOrLab>> programsOrLabs = LabTabHTTPOperationController.getProgramsOrLabsUsingFromAndTo("2013/01/01", "2016/12/31");
        if (programsOrLabs != null) {
            ArrayList<ProgramOrLab> programOrLabArrayList = programsOrLabs.getResponse();
            String member_id = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id();
            for (ProgramOrLab programOrLab : programOrLabArrayList) {
                programOrLab.setMember_id(member_id);
                programOrLab.setLabLevel(LabTabUtil.getRandomLabLevel());
            }
            for (GetProgramOrLabListener getProgramOrLabListener : LabTabApplication.getInstance().getUIListeners(GetProgramOrLabListener.class)) {
                if (programsOrLabs.getResponseCode() == SC_OK) {
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
