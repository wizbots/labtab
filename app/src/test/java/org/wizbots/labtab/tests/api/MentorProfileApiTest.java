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
import org.wizbots.labtab.model.Mentor;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MentorProfileApiTest implements LabTabConstants {
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
    public void fetchMentorProfileSuccessFullTest() throws InterruptedException { // Fetch MentorProfile Case 200
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com",LabTabApplication.getInstance().getUserAgent()));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == StatusCode.CREATED);

        CreateTokenResponse createTokenResponse = (CreateTokenResponse) labTabResponse.getResponse();
        Assert.assertNotNull(createTokenResponse);

        LabTabResponse mentorProfile = ConnectionUtil.execute(labTabApiInterface.returnMentor(createTokenResponse.getToken(),LabTabApplication.getInstance().getUserAgent()));
        Assert.assertNotNull(mentorProfile);
        Assert.assertTrue("Mentor Profile Fetched Successfully", mentorProfile.getResponseCode() == StatusCode.OK);

        Mentor mentor = (Mentor) mentorProfile.getResponse();
        Assert.assertEquals("Judy", mentor.getFirst_name());
    }

    @Test
    public void fetchMentorProfileUsingInvalidAuthTokenTest() throws InterruptedException { // Fetch MentorProfile Case 401
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com",LabTabApplication.getInstance().getUserAgent()));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == StatusCode.CREATED);

        CreateTokenResponse createTokenResponse = (CreateTokenResponse) labTabResponse.getResponse();
        Assert.assertNotNull(createTokenResponse);

        LabTabResponse mentorProfile = ConnectionUtil.execute(labTabApiInterface.returnMentor(createTokenResponse.getToken() + "k",LabTabApplication.getInstance().getUserAgent()));
        Assert.assertNotNull(mentorProfile);
        Assert.assertTrue("Invalid Token", mentorProfile.getResponseCode() == StatusCode.UNAUTHORIZED);

    }

    @After
    public void afterTest() {
        labTabApiInterface = null;
    }

}