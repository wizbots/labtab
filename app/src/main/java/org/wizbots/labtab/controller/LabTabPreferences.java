package org.wizbots.labtab.controller;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

public class LabTabPreferences {

    public static final String TAG = LabTabPreferences.class.getName();

    private SharedPreferences sharedPreferences;

    private static LabTabPreferences labTabPreferences;

    public static final String SHARED_PREF_NAME = "lab_tab_preferences";

    public enum Keys {
        LAB_TAB("lab_tab");
        private String label;

        Keys(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

    private LabTabPreferences() {
    }

    public static LabTabPreferences getInstance(Context context) {
        if (labTabPreferences == null) {
            labTabPreferences = new LabTabPreferences();

        }
        if (labTabPreferences.sharedPreferences == null) {
            labTabPreferences.sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,
                    Context.MODE_PRIVATE);
        }
        return labTabPreferences;
    }

    /**
     * This Method Clear shared preference.
     */
    public void clear() {
        Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    private boolean getBoolean(String key, boolean deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getBoolean(key, deafultValue);
        }

        return deafultValue;
    }

    private void putBoolean(String key, boolean value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putBoolean(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put Boolean in Shared preference", e);
        }
    }

    private String getString(String key, String deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getString(key, deafultValue);
        }

        return deafultValue;
    }

    private void putString(String key, String value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putString(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put String in Shared preference", e);
        }
    }

    private long getLong(String key, long deafultValue) {
        if (sharedPreferences != null) {
            return sharedPreferences.getLong(key, deafultValue);
        }

        return deafultValue;
    }

    private void putLong(String key, long value) {
        try {
            if (sharedPreferences != null) {
                Editor editor = sharedPreferences.edit();
                editor.putLong(key, value);
                editor.apply();
            }
        } catch (Exception e) {
            Log.e(TAG, "Unable Put Boolean in Shared preference", e);
        }
    }


    public String getLabTab() {
        return getString(Keys.LAB_TAB.getLabel(), "");
    }

    public void setLabTab(String labTab) {
        putString(Keys.LAB_TAB.getLabel(), labTab);
    }

}
