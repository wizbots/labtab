package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;


public interface LocationListener extends BaseUIListener {
    void onLocationFetchSuccess();
    void onLocationFetchError();
}
