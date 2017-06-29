package org.wizbots.labtab.requesters;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.database.ProgramsOrLabsTable;
import org.wizbots.labtab.interfaces.requesters.GetProgramOrLabListener;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.ResponseBody;

import static android.R.id.list;
import static org.wizbots.labtab.util.LabListComparator.CHAT_COMPARATOR;

public class ProgramOrLabRequester implements Runnable, LabTabConstants {

    private ArrayList<ProgramOrLab> programOrLabArrayList;

    @Override
    public void run()  {
        int statusCode = 0;
        LabTabResponse<ResponseBody> programsOrLabs = LabTabHTTPOperationController.getProgramsOrLabsUsingFromAndTo("2013-01-01", "2017-12-31");
        try {
            if (programsOrLabs != null) {
                statusCode = programsOrLabs.getResponseCode();
                Gson gson = new Gson();
                Type type = new TypeToken<ArrayList<ProgramOrLab>>() {
                }.getType();

                ArrayList<ProgramOrLab> fromJson = gson.fromJson(programsOrLabs.getResponse().string(), type);

                programOrLabArrayList = fromJson;
                String member_id = LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id();
                if (statusCode == StatusCode.OK) {
                    if (programOrLabArrayList != null && !programOrLabArrayList.isEmpty()) {
                        int i = 0;
                        for (ProgramOrLab programOrLab : programOrLabArrayList) {
                            Log.d("issue", programOrLab.toString() + " pos " + i++);

                            String seasonYear[] = programOrLab.getSeason().split(" - ");
                            programOrLab.setMember_id(member_id);
                            programOrLab.setSeason(seasonYear[1].toLowerCase());
                            programOrLab.setYear(seasonYear[0]);
                            programOrLab.setStartTimeStamp(LabTabUtil.getTimeStamp(programOrLab.getStarts()));
                            programOrLab.setEndTimesStamp(LabTabUtil.getTimeStamp(programOrLab.getEnds()));
                        }
                        ProgramsOrLabsTable.getInstance().insert(programOrLabArrayList);
                    }
                }
            } else {
                statusCode = HttpURLConnection.HTTP_OK;
                programOrLabArrayList = ProgramsOrLabsTable
                        .getInstance()
                        .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
            }
        } catch (Exception e){

            if (programOrLabArrayList != null) {
                Collections.sort(programOrLabArrayList, CHAT_COMPARATOR);
            }
            for (GetProgramOrLabListener getProgramOrLabListener : LabTabApplication.getInstance().getUIListeners(GetProgramOrLabListener.class)) {
                if (statusCode == StatusCode.OK) {
                    getProgramOrLabListener.programOrLabFetchedSuccessfully(programOrLabArrayList);
                } else {
                    getProgramOrLabListener.unableToFetchPrograms(statusCode);
                }
            }
        }
    }
}
