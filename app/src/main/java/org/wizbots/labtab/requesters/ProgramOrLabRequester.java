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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;

import okhttp3.ResponseBody;

import static org.wizbots.labtab.util.LabListComparator.CHAT_COMPARATOR;

public class ProgramOrLabRequester implements Runnable, LabTabConstants {
    private final String TAG = ProgramOrLabRequester.class.getSimpleName();
    private ArrayList<ProgramOrLab> programOrLabArrayList;

    @Override
    public void run() {
        Log.d(TAG, "ProgramOrLab Request");
        int statusCode = 0;


        LabTabResponse<ResponseBody> programsOrLabs = LabTabHTTPOperationController.getProgramsOrLabsUsingFromAndTo(getCurrentYear(), getCurrentDate());
//        LabTabResponse<ResponseBody> programsOrLabs = LabTabHTTPOperationController.getProgramsOrLabsUsingFromAndTo(getDateOfCurrentWeek(1), getDateOfCurrentWeek(7));
        // LabTabResponse<ResponseBody> programsOrLabs = LabTabHTTPOperationController.getProgramsOrLabsUsingFromAndTo("2013-01-01", "2017-12-31");

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
                            // skip program whose season is null
                            if (programOrLab.getSeason() == null)
                                continue;

                            String seasonYear[] = programOrLab.getSeason().split(" - ");
                            programOrLab.setMember_id(member_id);
                            programOrLab.setSeason(seasonYear[1].toLowerCase());
                            programOrLab.setYear(seasonYear[0]);
                            programOrLab.setStartTimeStamp(LabTabUtil.getTimeStamp(programOrLab.getStarts()));
                            programOrLab.setEndTimesStamp(LabTabUtil.getTimeStamp(programOrLab.getEnds()));
                        }
                        ProgramsOrLabsTable.getInstance().insert(programOrLabArrayList);
                        Log.d(TAG, "ProgramOrLab Success, Response Code : " + statusCode + " ProgramOrLab List : " + programsOrLabs.getResponse().string());

                    }
                } else {
                    Log.d(TAG, "ProgramOrLab Failed, Response Code : " + statusCode);
                }
            } else {
                statusCode = HttpURLConnection.HTTP_OK;
                programOrLabArrayList = ProgramsOrLabsTable
                        .getInstance()
                        .getProgramsByMemberId(LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id());
            }
        } catch (Exception e) {

        }

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

    /**
     * Descrption :
     *
     * @param a
     * @return
     */

    private String getDateOfCurrentWeek(int a) {

        Calendar c1 = Calendar.getInstance();

        //first day of week
        c1.set(Calendar.DAY_OF_WEEK, a);

        int year1 = c1.get(Calendar.YEAR);
        int month1 = c1.get(Calendar.MONTH) + 1;
        int day1 = c1.get(Calendar.DAY_OF_MONTH);
        return year1 + "-" + String.format("%02d", month1) + "-" + String.format("%02d", day1);
    }

    private String getCurrentDate() {
        Date myDate = new Date();
        return new SimpleDateFormat("yyyy-MM-dd").format(myDate);
    }

    private String getCurrentYear() {

        Calendar c1 = Calendar.getInstance();
        int year1 = c1.get(Calendar.YEAR);
        return year1 + "-01-01";
    }


}
