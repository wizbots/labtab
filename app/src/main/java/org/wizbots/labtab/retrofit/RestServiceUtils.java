package org.wizbots.labtab.retrofit;


import org.wizbots.labtab.LabTabApplication;


public class RestServiceUtils {
    private static WebService webService;

    public static CZResponse signIn() {
        webService = LabTabApplication.getInstance().getWebService();
        //return ConnectionUtils.execute(webService.signIn(signIn.getUsername(), signIn.getPassword(), signIn.getClient_id(), signIn.getGrant_type()));
        return new CZResponse(11, "");
    }
}
