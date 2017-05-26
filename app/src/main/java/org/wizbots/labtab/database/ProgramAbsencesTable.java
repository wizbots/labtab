package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.model.program.Absence;
import org.wizbots.labtab.service.SyncManager;

import java.util.ArrayList;
import java.util.Collection;

public class ProgramAbsencesTable extends AbstractTable {

    private static final String TAG = ProgramAbsencesTable.class.getName();

    private static final String NAME = "program_absences";

    private static final String COLUMN_STUDENT_NAME = "student_name";
    private static final String COLUMN_IS_MARK_ABSENT_SYNCED = "is_mark_absent_synced";
    private static final String COLUMN_SEND_ABSENT_NOTIFICATION = "send_absent_notification";
    private static final String COLUMN_MENTOR_NAME = "mentor_name";
    private static final String COLUMN_PROGRAM_ID = "program_id";
    private static final String COLUMN_STUDENT_ID = "student_id";
    private static final String COLUMN_MENTOR_ID = "mentor_id";
    private static final String COLUMN_DATE = "date";


    private DAOManager daoManager = null;
    private final static ProgramAbsencesTable instance;

    static {
        instance = new ProgramAbsencesTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static ProgramAbsencesTable getInstance() {
        return instance;
    }

    private ProgramAbsencesTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_STUDENT_NAME + " text,"
                + COLUMN_IS_MARK_ABSENT_SYNCED + " text,"
                + COLUMN_SEND_ABSENT_NOTIFICATION + " text,"
                + COLUMN_MENTOR_NAME + " text,"
                + COLUMN_PROGRAM_ID + " text,"
                + COLUMN_STUDENT_ID + " text,"
                + COLUMN_MENTOR_ID + " text,"
                + COLUMN_DATE + " text, PRIMARY KEY(mentor_id,program_id,student_id,date));");
    }

    public synchronized void insert(Absence absence) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                insert(db, absence);
            } catch (Exception e) {
                Log.e(TAG, "Error while add absence", e);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert absence", e);
        } finally {
            db.endTransaction();
        }
    }

    public synchronized void insert(Collection<Absence> absences) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (Absence absence : absences) {
                try {
                    insert(db, absence);
                } catch (Exception e) {
                    Log.e(TAG, "Error while add absence", e);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert absence in Batch", e);
        } finally {
            db.endTransaction();
            SyncManager.getInstance().onRefreshData(1);
        }
    }

    private void insert(SQLiteDatabase db, Absence absence) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, absence.getStudent_name());
        values.put(COLUMN_IS_MARK_ABSENT_SYNCED, absence.getMark_absent_synced());
        values.put(COLUMN_SEND_ABSENT_NOTIFICATION, absence.getSend_absent_notification());
        values.put(COLUMN_MENTOR_NAME, absence.getMentor_name());
        values.put(COLUMN_PROGRAM_ID, absence.getProgram_id());
        values.put(COLUMN_STUDENT_ID, absence.getStudent_id());
        values.put(COLUMN_MENTOR_ID, absence.getMentor_id());
        values.put(COLUMN_DATE, absence.getDate());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<Absence> getAbsencesListByProgramId(String programId) {
        final String query = "Select * from " + NAME + " where " + COLUMN_PROGRAM_ID + " = '" + programId + "' and " + COLUMN_MENTOR_ID + "='" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and " + COLUMN_STUDENT_NAME + "!=''" + " ORDER BY " + COLUMN_STUDENT_NAME + " ASC";
        ArrayList<Absence> absences = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    absences.add(new Absence(
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_IS_MARK_ABSENT_SYNCED)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SEND_ABSENT_NOTIFICATION)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get absences", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return absences;
    }

    public ArrayList<Absence> getStudentToBeMarkedAbsent() {
        final String query = "Select * from " + NAME + " where " + COLUMN_IS_MARK_ABSENT_SYNCED + " = '" + LabTabConstants.SyncStatus.NOT_SYNCED + "' and " + COLUMN_MENTOR_ID + "='" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and " + COLUMN_STUDENT_NAME + "!=''";
        ArrayList<Absence> absences = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    absences.add(new Absence(
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_IS_MARK_ABSENT_SYNCED)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SEND_ABSENT_NOTIFICATION)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get absences", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return absences;
    }

    public void updateAbsence(Absence absence) {
        String updateStudentLevelQuery = "UPDATE " + NAME
                + " SET "
                + COLUMN_IS_MARK_ABSENT_SYNCED + " = '" + absence.getMark_absent_synced()
                + "' WHERE " + COLUMN_STUDENT_ID + "= '" + absence.getStudent_id()
                + "' AND " + "" + COLUMN_PROGRAM_ID + "= '" + absence.getProgram_id() + "'"
                + " AND " + "" + COLUMN_MENTOR_ID + "= '" + absence.getMentor_id() + "'"
                + " AND " + "" + COLUMN_IS_MARK_ABSENT_SYNCED + "= '" + LabTabConstants.SyncStatus.NOT_SYNCED + "'";
        SQLiteDatabase db = daoManager.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(updateStudentLevelQuery);
        stmt.execute();
        SyncManager.getInstance().onRefreshData(1);
    }

    public ArrayList<Absence> findAbsencesForSpecificDate(String date, String studentId) {
        final String query = "Select * from " + NAME + " where " + COLUMN_DATE + " = '" + date + "' and " + COLUMN_MENTOR_ID + "='" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and " + COLUMN_STUDENT_NAME + "!='' and " + COLUMN_STUDENT_ID + "='" + studentId + "';";
        ArrayList<Absence> absences = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    absences.add(new Absence(
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_IS_MARK_ABSENT_SYNCED)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SEND_ABSENT_NOTIFICATION)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_DATE))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get absences", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return absences;
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
