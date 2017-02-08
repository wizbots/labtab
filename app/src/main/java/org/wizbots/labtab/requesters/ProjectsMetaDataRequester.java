package org.wizbots.labtab.requesters;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.model.metadata.MetaData;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

public class ProjectsMetaDataRequester implements Runnable, LabTabConstants {

    @Override
    public void run() {
        LabTabResponse<MetaData[]> labTabResponse = LabTabHTTPOperationController.getProjectMetaData();
        if (labTabResponse.getResponse() != null && labTabResponse.getResponseCode() == StatusCode.OK) {
            LabTabPreferences.getInstance(LabTabApplication.getInstance()).setProjectsMetaData(labTabResponse.getResponse());
            LabTabApplication.getInstance().setMetaDatas(labTabResponse.getResponse());
        } else {
            MetaData[] mD = (MetaData[]) LabTabUtil.fromJson(ProjectsMetaData.CONTENT, MetaData[].class);
            LabTabPreferences.getInstance(LabTabApplication.getInstance()).setProjectsMetaData(mD);
            LabTabApplication.getInstance().setMetaDatas(mD);
        }
    }
}
