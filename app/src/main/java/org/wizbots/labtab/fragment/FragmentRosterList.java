package org.wizbots.labtab.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.wizbots.labtab.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentRosterList extends Fragment {

    public FragmentRosterList() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_roster_list, container, false);
    }
}
