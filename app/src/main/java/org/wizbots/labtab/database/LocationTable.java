package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.model.LocationResponse;

import java.util.ArrayList;
import java.util.Collection;


public class LocationTable extends AbstractTable {

    private static final String TAG = LocationTable.class.getSimpleName();

    private static final String NAME = "locationtable";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";

    private DAOManager daoManager = null;
    private final static LocationTable instance;

    static {
        instance = new LocationTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static LocationTable getInstance() {
        return instance;
    }

    private LocationTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    protected String getTableName() {
        return NAME;
    }

    @Override
    protected String[] getProjection() {
        return null;
    }

    @Override
    public void create(SQLiteDatabase db) {
        Log.d(TAG, "Location Table Creater");
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "(" + COLUMN_ID + " text PRIMARY KEY," + COLUMN_NAME + " text);");
    }

    public synchronized void insert(Collection<LocationResponse> locationList) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (LocationResponse location : locationList) {
                try {
                    insert(db, location);
                } catch (Exception e) {
                    Log.e(TAG, "Error while add mentors", e);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert mentors in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, LocationResponse location) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, location.getId());
        values.put(COLUMN_NAME, location.getName());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<LocationResponse> getLocationList() {
        ArrayList<LocationResponse> locationList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    locationList.add(
                            new LocationResponse(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME))
                            )
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get mentor", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return locationList;
    }

    public LocationResponse getLocationById(String locationId) {
        LocationResponse location = new LocationResponse();
        final String query = "Select * from " + NAME + " where " + COLUMN_ID + " = '" + locationId + "'";
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    location= new LocationResponse(cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_NAME)));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get mentor", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return location;
    }
}
