package org.wizbots.labtab.tests.api;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.retrofit.ConnectionUtil;
import org.wizbots.labtab.retrofit.LabTabApiInterface;
import org.wizbots.labtab.retrofit.LabTabResponse;
import org.wizbots.labtab.util.LabTabUtil;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class LoginApiTest {
    LabTabApiInterface labTabApiInterface;

    @Before
    public void beforeTest() {
        // Initializing Api Interface
        labTabApiInterface = LabTabUtil.getApiInterface();
    }

    @Test
    public void loginSuccessFullTest() throws InterruptedException { // Login Api Test For Successful Case 201
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Token Created Successfully", labTabResponse.getResponseCode() == LabTabConstants.SC_CREATED);
    }

    @Test
    public void loginFailureTest() throws InterruptedException { // Login Api Test For Failure Case 403
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robo", "judy@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("Unable To Create Token", labTabResponse.getResponseCode() == LabTabConstants.SC_FORBIDDEN);
    }

    @Test
    public void loginUserNotFoundTest() throws InterruptedException { // Login Api Test For User Not Found Case 404
        LabTabResponse labTabResponse = ConnectionUtil.execute(labTabApiInterface.createTokenOrLoginUser("robotics", "abc@wizbots.com"));
        Assert.assertNotNull(labTabResponse);
        Assert.assertTrue("User Not Found", labTabResponse.getResponseCode() == LabTabConstants.SC_NOT_FOUND);
    }

    @After
    public void afterTest() {
        labTabApiInterface = null;
    }

}