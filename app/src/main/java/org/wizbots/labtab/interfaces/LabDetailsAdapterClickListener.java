package org.wizbots.labtab.interfaces;

import org.wizbots.labtab.model.LabDetails;

public interface LabDetailsAdapterClickListener {
    void onActionViewClick(LabDetails labList);

    void onActionEditClick(LabDetails labList);

    void onActionCloseToNextLevelClick(LabDetails labList);

    void onCheckChanged(int position, boolean value);
}
