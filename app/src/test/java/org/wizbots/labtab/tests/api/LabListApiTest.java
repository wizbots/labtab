package org.wizbots.labtab.tests.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabListApiTest implements LabTabConstants{
    private LabTabApiInterface labTabApiInterface;
    private CreateTokenResponse createTokenResponse;

    @Before
    public void beforeTest() {
        // Initializing Api Interface
        labTabApiInterface = LabTabUtil.getApiInterface();
        //Logging In User
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com", LabTabApplication.getInstance().getUserAgent()));
        createTokenResponse = (CreateTokenResponse) labTabResponse.getResponse();
    }


    @Test
    public void labsListAvailableForMentorTest() throws InterruptedException { // Lab List Api Test When Labs Are Available Case 200
        LabTabResponse listLabTabResponse = ConnectionUtil.execute(labTabApiInterface.returnPrograms(
                createTokenResponse.getToken(),
                createTokenResponse.getMember_id(),
                "2013/01/01", "2016/12/31"));
        Assert.assertNotNull(listLabTabResponse);
        Assert.assertTrue("Labs are there", ((ArrayList<ProgramOrLab>) listLabTabResponse.getResponse()).size() > 0);
        Assert.assertTrue("Labs are there", listLabTabResponse.getResponseCode() == StatusCode.OK);

    }

    @Test
    public void invalidAuthTokenUsageWhileFetchingLabsTest() throws InterruptedException { // Lab List Api Test When Wrong Auth Token Is Used Case 401
        LabTabResponse listLabTabResponse = ConnectionUtil.execute(labTabApiInterface.returnPrograms(
                createTokenResponse.getToken() + "k",
                createTokenResponse.getMember_id(),
                "2013/01/01", "2016/12/31"));
        Assert.assertNotNull(listLabTabResponse);
        Assert.assertTrue("Labs are not there", listLabTabResponse.getResponseCode() == LabTabConstants.StatusCode.UNAUTHORIZED);
    }

    @After
    public void afterTest() {
        labTabApiInterface = null;
    }

}