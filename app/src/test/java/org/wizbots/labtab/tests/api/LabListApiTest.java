package org.wizbots.labtab.tests.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LabListApiTest {
    LabTabApiInterface labTabApiInterface;

    @Before
    public void beforeTest() {
        // Initializing Api Interface
        labTabApiInterface = LabTabUtil.getApiInterface();
    }


    @Test
    public void labsListAvailableForMentorTest() throws InterruptedException { // Lab List Api Test When Labs Are Available Case 200
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == LabTabConstants.SC_CREATED);

        CreateTokenResponse createTokenResponse = (CreateTokenResponse) labTabResponse.getResponse();
        Assert.assertNotNull(createTokenResponse);

        LabTabResponse listLabTabResponse = ConnectionUtil.execute(labTabApiInterface.returnPrograms(
                createTokenResponse.getToken(),
                createTokenResponse.getMember_id(),
                "2013/01/01", "2016/12/31"));
        Assert.assertNotNull(listLabTabResponse);
        Assert.assertTrue("Labs are there", ((ArrayList<ProgramOrLab>) listLabTabResponse.getResponse()).size() > 0);
        Assert.assertTrue("Labs are there", listLabTabResponse.getResponseCode() == LabTabConstants.SC_OK);

    }

    @Test
    public void invalideAuthTokenUsageWhileFetchingLabsTest() throws InterruptedException { // Lab List Api Test When Wrong Auth Token Is Used Case 401
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == LabTabConstants.SC_CREATED);

        CreateTokenResponse createTokenResponse = (CreateTokenResponse) labTabResponse.getResponse();
        Assert.assertNotNull(createTokenResponse);

        LabTabResponse listLabTabResponse = ConnectionUtil.execute(labTabApiInterface.returnPrograms(
                createTokenResponse.getToken() + "k",
                createTokenResponse.getMember_id(),
                "2013/01/01", "2016/12/31"));
        Assert.assertNotNull(listLabTabResponse);
        Assert.assertTrue("Labs are not there", listLabTabResponse.getResponseCode() == LabTabConstants.SC_UNAUTHORIZED);
    }

    @After
    public void afterTest() {
        labTabApiInterface = null;
    }

}