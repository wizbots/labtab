package org.wizbots.labtab.requesters;


import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabHTTPOperationController;
import org.wizbots.labtab.interfaces.requesters.CreateTokenListener;
import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.LoginRequest;
import org.wizbots.labtab.retrofit.LabTabResponse;


public class LoginRequester implements Runnable, LabTabConstants {
    private String email;
    private String password;

    public LoginRequester() {
    }

    public LoginRequester(String email, String password) {
        this.email = email;
        this.password = password;
    }

    @Override
    public void run() {
        LabTabResponse<CreateTokenResponse> createTokenResponse = LabTabHTTPOperationController.loginUser(new LoginRequest(password, email));
        if (createTokenResponse != null) {
            for (CreateTokenListener createTokenListener : LabTabApplication.getInstance().getUIListeners(CreateTokenListener.class)) {
                if (createTokenResponse.getResponseCode() == StatusCode.CREATED) {
                    createTokenListener.tokenCreatedSuccessfully(createTokenResponse.getResponse());
                } else {
                    createTokenListener.unableToCreateToken(createTokenResponse.getResponseCode());
                }
            }
        } else {
            for (CreateTokenListener createTokenListener : LabTabApplication.getInstance().getUIListeners(CreateTokenListener.class)) {
                createTokenListener.unableToCreateToken(0);
            }
        }
    }
}
