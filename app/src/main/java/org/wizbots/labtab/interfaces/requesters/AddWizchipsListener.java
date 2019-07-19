package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;


public interface AddWizchipsListener extends BaseUIListener {
    void onAddWizchipsSuccess();
    void onAddWizchipsError();
    void notHavePermissionForWizchips(String message);
}
