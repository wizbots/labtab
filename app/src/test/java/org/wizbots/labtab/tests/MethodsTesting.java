package org.wizbots.labtab.tests;

import org.junit.Test;
import org.wizbots.labtab.util.LabTabUtil;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class MethodsTesting {


    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(LabTabUtil.isValidEmail("judy@wizbots.com"));
    }

    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(LabTabUtil.isValidEmail("conor@wizbots.a.b.c"));
    }

    @Test
    public void emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(LabTabUtil.isValidEmail("abc@wizbots"));
    }

    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(LabTabUtil.isValidEmail("abc@wizbots..com"));
    }

    @Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(LabTabUtil.isValidEmail("@wizbots.com"));
    }

    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(LabTabUtil.isValidEmail(""));
    }

    @Test
    public void getRandomLabLevel() {
        String labLevel = LabTabUtil.getRandomLabLevel();
        assertTrue("Get Random Lab Level", !labLevel.equals(""));
    }

}
