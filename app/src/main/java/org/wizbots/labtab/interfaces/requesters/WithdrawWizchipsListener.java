package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

/**
 * Created by ashish on 7/2/17.
 */

public interface WithdrawWizchipsListener extends BaseUIListener {
    void onWithdrawWizchipsSuccess();
    void onWithdrawWizchipsError();
}
