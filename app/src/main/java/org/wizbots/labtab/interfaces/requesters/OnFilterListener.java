package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;
import org.wizbots.labtab.model.ProgramOrLab;

import java.util.ArrayList;

/**
 * Created by ashish on 9/2/17.
 */

public interface OnFilterListener extends BaseUIListener {
    void onFilterSuccess(ArrayList<ProgramOrLab> programOrLabs);
    void onFilterError(int responseCode);
}
