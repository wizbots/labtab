package org.wizbots.labtab.controller;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.model.LoginRequest;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;

public class LabTabHTTPOperationController {

    private final String TAG = LabTabHTTPOperationController.class.getSimpleName();

    public static LabTabResponse loginUser(LoginRequest loginRequest) {
        LabTabApiInterface labTabApiInterface = LabTabApplication.getInstance().getLabTabApiInterface();
        return ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser(loginRequest.getPassword(), loginRequest.getEmail()));
    }

}