package org.wizbots.labtab.tests.util;

import org.junit.Test;
import org.wizbots.labtab.util.LabTabUtil;

import static org.junit.Assert.assertTrue;

public class LabTabUtilTest {

    @Test
    public void getRandomLabLevel() {
        String labLevel = LabTabUtil.getRandomLabLevel();
        assertTrue("Get Random Lab Level", !labLevel.equals(""));
    }

}
