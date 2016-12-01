package org.wizbots.labtab.requesters;


import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.model.LoginRequest;
import org.wizbots.labtab.retrofit.LabTabResponse;

public class LoginRequester implements Runnable {
    @Override
    public void run() {
        LabTabResponse labTabResponse = LabTabHTTPOperationController.loginUser(new LoginRequest("robotics", "judy@wizbots.com"));
        labTabResponse.getResponse();
    }
}
