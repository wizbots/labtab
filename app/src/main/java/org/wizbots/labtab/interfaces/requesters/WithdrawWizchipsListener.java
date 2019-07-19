package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;


public interface WithdrawWizchipsListener extends BaseUIListener {
    void onWithdrawWizchipsSuccess();
    void onWithdrawWizchipsError();
    void notHavePermissionToWithdraw(String message);
}
