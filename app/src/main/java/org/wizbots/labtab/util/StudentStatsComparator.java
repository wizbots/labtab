package org.wizbots.labtab.util;

import org.wizbots.labtab.interfaces.StudentTypeInterface;
import org.wizbots.labtab.model.student.StudentLabDetailsType1;
import org.wizbots.labtab.model.student.StudentLabDetailsType2;
import org.wizbots.labtab.model.student.StudentStats;

import java.util.Comparator;

/**
 * Created by ashish on 16/6/17.
 */

public class StudentStatsComparator {
    enum LabLevelsEnums {
        APPRENTICE(4), EXPLORER(3), IMAGINEER(6), LABCERTIFIED(2), MAKER(5), MASTER(8), WIZARD(7), NOVICE(1);

        private int numVal;

        LabLevelsEnums(int numVal) {
            this.numVal = numVal;
        }

        public int getNumVal() {
            return numVal;
        }

    }

    public static Comparator<StudentStats> getCompByName() {
        Comparator comp = new Comparator<StudentStats>() {
            @Override
            public int compare(StudentStats s1, StudentStats s2) {
               /* StudentLabDetailsType1 o1 = (StudentLabDetailsType1) s1;
                StudentLabDetailsType1 o2 = (StudentLabDetailsType1) s2;*/
                String currentLevel = "";
                String nextLevel = "";

                currentLevel = ((StudentStats) s1).getLevel();
                nextLevel = ((StudentStats) s2).getLevel();
                LevelComparator.LabLevelsEnums current = getLevelEnum(currentLevel.toUpperCase().trim());
                LevelComparator.LabLevelsEnums newEnum = getLevelEnum(nextLevel.toUpperCase().trim());

                if (current == null || newEnum == null) {
                    return 0;
                }


                return current.getNumVal() - newEnum.getNumVal();
            }
        };
        return comp;
    }

    private static LevelComparator.LabLevelsEnums getLevelEnum(String labLevel) {
        LevelComparator.LabLevelsEnums m = null;

        if (labLevel.equalsIgnoreCase("APPRENTICE")) {
            m = LevelComparator.LabLevelsEnums.APPRENTICE;

        } else if (labLevel.equalsIgnoreCase("EXPLORER")) {
            m = LevelComparator.LabLevelsEnums.EXPLORER;

        } else if (labLevel.equalsIgnoreCase("IMAGINEER")) {
            m = LevelComparator.LabLevelsEnums.IMAGINEER;

        } else if (labLevel.equalsIgnoreCase("LAB CERTIFIED")) {
            m = LevelComparator.LabLevelsEnums.LABCERTIFIED;

        } else if (labLevel.equalsIgnoreCase("MAKER")) {
            m = LevelComparator.LabLevelsEnums.MAKER;

        } else if (labLevel.equalsIgnoreCase("MASTER")) {
            m = LevelComparator.LabLevelsEnums.MASTER;

        } else if (labLevel.equalsIgnoreCase("WIZARD")) {
            m = LevelComparator.LabLevelsEnums.WIZARD;

        } else if (labLevel.equalsIgnoreCase("NOVICE")) {
            m = LevelComparator.LabLevelsEnums.NOVICE;

        }
        return m;
    }
}
