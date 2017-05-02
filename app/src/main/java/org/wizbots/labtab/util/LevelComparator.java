package org.wizbots.labtab.util;

import org.wizbots.labtab.interfaces.StudentTypeInterface;
import org.wizbots.labtab.model.student.StudentLabDetailsType1;
import org.wizbots.labtab.model.student.StudentLabDetailsType2;

import java.util.Comparator;

/**
 * Created by ashish on 2/5/17.
 */

public class LevelComparator {
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

    public static Comparator<StudentTypeInterface> getCompByName() {
        Comparator comp = new Comparator<StudentTypeInterface>() {
            @Override
            public int compare(StudentTypeInterface s1, StudentTypeInterface s2) {
               /* StudentLabDetailsType1 o1 = (StudentLabDetailsType1) s1;
                StudentLabDetailsType1 o2 = (StudentLabDetailsType1) s2;*/
                String currentLevel = "";
                String nextLevel = "";
                if (s1 instanceof StudentLabDetailsType1) {
                    currentLevel = ((StudentLabDetailsType1) s1).getLabLevel();
                } else {
                    currentLevel = ((StudentLabDetailsType2) s1).getLabLevel();
                }
                if (s2 instanceof StudentLabDetailsType1) {
                    nextLevel = ((StudentLabDetailsType1) s2).getLabLevel();
                } else {
                    nextLevel = ((StudentLabDetailsType2) s2).getLabLevel();
                }
                LabLevelsEnums current = getLevelEnum(currentLevel.toUpperCase().trim());
                LabLevelsEnums newEnum = getLevelEnum(nextLevel.toUpperCase().trim());

                if (current == null || newEnum == null) {
                    return 0;
                }


                return current.getNumVal() - newEnum.getNumVal();
            }
        };
        return comp;
    }

    private static LabLevelsEnums getLevelEnum(String labLevel) {
        LabLevelsEnums m = null;

        if (labLevel.equalsIgnoreCase("APPRENTICE")) {
            m = LabLevelsEnums.APPRENTICE;

        } else if (labLevel.equalsIgnoreCase("EXPLORER")) {
            m = LabLevelsEnums.EXPLORER;

        } else if (labLevel.equalsIgnoreCase("IMAGINEER")) {
            m = LabLevelsEnums.IMAGINEER;

        } else if (labLevel.equalsIgnoreCase("LAB CERTIFIED")) {
            m = LabLevelsEnums.LABCERTIFIED;

        } else if (labLevel.equalsIgnoreCase("MAKER")) {
            m = LabLevelsEnums.MAKER;

        } else if (labLevel.equalsIgnoreCase("MASTER")) {
            m = LabLevelsEnums.MASTER;

        } else if (labLevel.equalsIgnoreCase("WIZARD")) {
            m = LabLevelsEnums.WIZARD;

        } else if (labLevel.equalsIgnoreCase("NOVICE")) {
            m = LabLevelsEnums.NOVICE;

        }
        return m;
    }
}
