package org.wizbots.labtab.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class AbstractTable implements DatabaseTable {

    protected abstract String getTableName();

    protected abstract String[] getProjection();

    protected String getListOrder() {
        return null;
    }

    @Override
    public void migrate(SQLiteDatabase db, int toVersion) {
    }

    /**
     * Query table.
     *
     * @return Result set with defined projection and in defined order.
     */
    public Cursor list() {
        SQLiteDatabase db = DAOManager.getInstance().getReadableDatabase();
        return db.query(getTableName(), getProjection(), null, null, null,
                null, getListOrder());
    }

    @Override
    public void clear() {
        SQLiteDatabase db = DAOManager.getInstance().getWritableDatabase();
        db.delete(getTableName(), null, null);
    }

}
