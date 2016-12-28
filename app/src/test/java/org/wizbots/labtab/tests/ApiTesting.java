package org.wizbots.labtab.tests;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.model.CreateTokenResponse;
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ApiTesting {
    LabTabApiInterface labTabApiInterface;

    @Before
    public void beforeTest() {
        // Initializing Api Interface
        labTabApiInterface = LabTabUtil.getApiInterface();
    }

    @Test
    public void apiTest1() throws InterruptedException { // Login Api Test For Successful Case 201
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == LabTabConstants.SC_CREATED);
    }

    @Test
    public void apiTest2() throws InterruptedException { // Login Api Test For Failure Case 403
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robo", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Unable To Create Token", labTabResponse.getResponseCode() == LabTabConstants.SC_FORBIDDEN);
    }

    @Test
    public void apiTest3() throws InterruptedException { // Login Api Test For User Not Found Case 404
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "abc@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("User Not Found", labTabResponse.getResponseCode() == LabTabConstants.SC_NOT_FOUND);
    }

    @Test
    public void apiTest4() throws InterruptedException { // Lab List Api Test When Labs Are Available Case 200
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
    public void apiTest5() throws InterruptedException { // Lab List Api Test When Wrong Auth Token Is Used Case 401
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

    @Test
    public void apiTest6() throws InterruptedException { // Fetch MentorProfile Case 201
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == LabTabConstants.SC_CREATED);

        CreateTokenResponse createTokenResponse = (CreateTokenResponse) labTabResponse.getResponse();
        Assert.assertNotNull(createTokenResponse);

        LabTabResponse mentorProfile = ConnectionUtil.execute(labTabApiInterface.returnMentor(createTokenResponse.getToken()));
        Assert.assertNotNull(mentorProfile);
        Assert.assertTrue("Mentor Profile Fetched Successfully", mentorProfile.getResponseCode() == LabTabConstants.SC_OK);

        Mentor mentor = (Mentor) mentorProfile.getResponse();
        Assert.assertEquals("Judy", mentor.getFirst_name());
    }

    @Test
    public void apiTest7() throws InterruptedException { // Fetch MentorProfile Case 401
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == LabTabConstants.SC_CREATED);

        CreateTokenResponse createTokenResponse = (CreateTokenResponse) labTabResponse.getResponse();
        Assert.assertNotNull(createTokenResponse);

        LabTabResponse mentorProfile = ConnectionUtil.execute(labTabApiInterface.returnMentor(createTokenResponse.getToken() + "k"));
        Assert.assertNotNull(mentorProfile);
        Assert.assertTrue("Invalid Token", mentorProfile.getResponseCode() == LabTabConstants.SC_UNAUTHORIZED);

    }

    @After
    public void afterTest() {
        labTabApiInterface = null;
    }

}