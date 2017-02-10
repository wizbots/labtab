package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

/**
 * Created by ashish on 9/2/17.
 */

public interface LocationListener extends BaseUIListener {
    void onLocationFetchSuccess();
    void onLocationFetchError();
}
