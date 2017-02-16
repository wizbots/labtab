package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

public interface SyncListener extends BaseUIListener {
    void syncStatusFetchedSuccessfully(boolean syncStatus);
}
