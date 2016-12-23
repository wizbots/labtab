package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.model.Mentor;

import java.util.ArrayList;
import java.util.Collection;

public class MentorsTable extends AbstractTable {

    private static final String TAG = MentorsTable.class.getName();

    private static final String NAME = "mentors";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MEMBER_ID = "member_id";
    private static final String COLUMN_TOKEN = "token";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_USER_NAME = "user_name";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_STREET_ADDRESS = "address";
    private static final String COLUMN_CITY = "city";
    private static final String COLUMN_ZIP_CODE = "zip_code";
    private static final String COLUMN_PHONE1 = "phone1";
    private static final String COLUMN_PHONE2 = "phone2";


    private DAOManager daoManager = null;
    private final static MentorsTable instance;

    static {
        instance = new MentorsTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static MentorsTable getInstance() {
        return instance;
    }

    private MentorsTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_ID + " text PRIMARY KEY,"
                + COLUMN_MEMBER_ID + " text,"
                + COLUMN_TOKEN + " text,"
                + COLUMN_DATE + " text,"
                + COLUMN_FIRST_NAME + " text,"
                + COLUMN_LAST_NAME + " text,"
                + COLUMN_EMAIL + " text,"
                + COLUMN_USER_NAME + " text,"
                + COLUMN_GENDER + " text,"
                + COLUMN_STATE + " text,"
                + COLUMN_STREET_ADDRESS + " text,"
                + COLUMN_CITY + " text,"
                + COLUMN_ZIP_CODE + " text,"
                + COLUMN_PHONE1 + " text,"
                + COLUMN_PHONE2 + " text);");
    }

    public synchronized void insert(Collection<Mentor> mentors) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (Mentor mentor : mentors) {
                try {
                    insert(db, mentor);
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

    public synchronized void insert(Mentor mentor) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                insert(db, mentor);
            } catch (Exception e) {
                Log.e(TAG, "Error while add mentor", e);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert mentor in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, Mentor mentor) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, mentor.getId());
        values.put(COLUMN_MEMBER_ID, mentor.getMember_id());
        values.put(COLUMN_TOKEN, mentor.getToken());
        values.put(COLUMN_DATE, mentor.getDate());
        values.put(COLUMN_FIRST_NAME, mentor.getFirst_name());
        values.put(COLUMN_LAST_NAME, mentor.getLast_name());
        values.put(COLUMN_EMAIL, mentor.getEmail());
        values.put(COLUMN_USER_NAME, mentor.getUsername());
        values.put(COLUMN_GENDER, mentor.getGender());
        values.put(COLUMN_STATE, mentor.getState());
        values.put(COLUMN_STREET_ADDRESS, mentor.getStreet_address());
        values.put(COLUMN_CITY, mentor.getCity());
        values.put(COLUMN_ZIP_CODE, mentor.getZipCode());
        values.put(COLUMN_PHONE1, mentor.getPhone1());
        values.put(COLUMN_PHONE2, mentor.getPhone2());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Mentor getMentorById(String id) {
        final String query = "Select * from " + NAME + " where " + COLUMN_ID + " = '" + id + "'";
        Mentor mentor = null;
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                mentor = new Mentor(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STREET_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CITY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ZIP_CODE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE1)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE2))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get mentor", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return mentor;
    }

    public Mentor getMentorByMemberId(String memberId) {
        final String query = "Select * from " + NAME + " where " + COLUMN_MEMBER_ID + " = '" + memberId + "'";
        Mentor mentor = null;
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                mentor = new Mentor(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STREET_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CITY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ZIP_CODE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE1)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE2))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get mentor", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return mentor;
    }

    public Mentor getMentorByToken(String token) {
        final String query = "Select * from " + NAME + " where " + COLUMN_TOKEN + " = '" + token + "'";
        Mentor mentor = null;
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                mentor = new Mentor(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TOKEN)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STATE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STREET_ADDRESS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CITY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ZIP_CODE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE1)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PHONE2))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get mentor", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return mentor;
    }

    private ArrayList<Mentor> getMentorList() {
        ArrayList<Mentor> mentorArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    mentorArrayList.add(
                            new Mentor(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TOKEN)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_DATE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_EMAIL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_USER_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_GENDER)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STATE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STREET_ADDRESS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_CITY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ZIP_CODE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE1)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PHONE2))
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
        return mentorArrayList;
    }

    @Override
    protected String getTableName() {
        return NAME;
    }

    @Override
    protected String[] getProjection() {
        return null;
    }
}
