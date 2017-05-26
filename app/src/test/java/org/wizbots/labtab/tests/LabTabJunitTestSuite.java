package org.wizbots.labtab.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.wizbots.labtab.tests.api.LabListApiTest;
import org.wizbots.labtab.tests.api.LoginApiTest;
import org.wizbots.labtab.tests.api.MentorProfileApiTest;
import org.wizbots.labtab.tests.util.EmailValidationTest;
import org.wizbots.labtab.tests.util.LabTabUtilTest;

@RunWith(Suite.class)

@Suite.SuiteClasses({
        LabListApiTest.class,
        LoginApiTest.class,
        MentorProfileApiTest.class,
        EmailValidationTest.class,
        LabTabUtilTest.class
})

public class LabTabJunitTestSuite {
}  	