package org.wizbots.labtab.interfaces.requesters;

import org.wizbots.labtab.interfaces.BaseUIListener;

/**
 * Created by ashish on 18/2/17.
 */

public interface OnDeleteVideoListener extends BaseUIListener {
    void onDeleteVideoSuccess();
    void onDeleteVideoError();
}
