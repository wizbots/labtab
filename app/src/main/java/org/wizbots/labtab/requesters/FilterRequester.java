package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.interfaces.requesters.OnFilterListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by ashish on 9/2/17.
 */

public class FilterRequester implements Runnable {

    private Map<String, String> params;
    ArrayList<ProgramOrLab> programOrLabArrayList;

    public FilterRequester(Map<String, String> params) {
        this.params = params;
        this.programOrLabArrayList = new ArrayList<>();
    }

    @Override
    public void run() {
        int statusCode = 0;
        LabTabResponse<ArrayList<ProgramOrLab>> programsOrLabs = LabTabHTTPOperationController.getFilter(params);
        if (programsOrLabs != null) {
            statusCode = programsOrLabs.getResponseCode();
            programOrLabArrayList.addAll(programsOrLabs.getResponse());
            String member_id = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id();
            for (ProgramOrLab programOrLab : programOrLabArrayList) {
                String seasonYear[] = programOrLab.getSeason().split(" - ");
                programOrLab.setMember_id(member_id);
                programOrLab.setSeason(seasonYear[1]);
                programOrLab.setYear(seasonYear[0]);
                programOrLab.setStartTimeStamp(LabTabUtil.getTimeStamp(programOrLab.getStarts()));
                programOrLab.setEndTimesStamp(LabTabUtil.getTimeStamp(programOrLab.getEnds()));
            }
        }else {
            //Offline data filter
            statusCode = HttpURLConnection.HTTP_OK;
            programOrLabArrayList.addAll(ProgramsOrLabsTable
                    .getInstance()
                    .getFilteredData(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id(), params));
        }
        for (OnFilterListener listener : LabTabApplication.getInstance().getUIListeners(OnFilterListener.class)) {
            if (statusCode == LabTabConstants.StatusCode.OK) {
                listener.onFilterSuccess(programOrLabArrayList);
            } else {
                listener.onFilterError(statusCode);
            }
        }
    }
}
