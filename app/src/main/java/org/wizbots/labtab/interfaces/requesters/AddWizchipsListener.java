package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

/**
 * Created by ashish on 7/2/17.
 */

public interface AddWizchipsListener extends BaseUIListener {
    void onAddWizchipsSuccess();
    void onAddWizchipsError();
}
