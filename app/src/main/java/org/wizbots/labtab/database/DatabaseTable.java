package org.wizbots.labtab.database;

import android.database.sqlite.SQLiteDatabase;

public interface DatabaseTable {
    void create(SQLiteDatabase db);

    void migrate(SQLiteDatabase db, int newVersion);

    void clear();
}
