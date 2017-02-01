package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.model.program.Absence;

import java.util.ArrayList;
import java.util.Collection;

public class ProgramAbsencesTable extends AbstractTable {

    private static final String TAG = ProgramAbsencesTable.class.getName();

    private static final String NAME = "program_absences";

    private static final String COLUMN_STUDENT_NAME = "student_name";
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
        }
    }

    private void insert(SQLiteDatabase db, Absence program) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_STUDENT_NAME, program.getStudent_name());
        values.put(COLUMN_MENTOR_NAME, program.getMentor_name());
        values.put(COLUMN_PROGRAM_ID, program.getProgram_id());
        values.put(COLUMN_STUDENT_ID, program.getStudent_id());
        values.put(COLUMN_MENTOR_ID, program.getMentor_id());
        values.put(COLUMN_DATE, program.getDate());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<Absence> getAbsencesListByProgramId(String programId) {
        final String query = "Select * from " + NAME + " where " + COLUMN_PROGRAM_ID + " = '" + programId + "' and " + COLUMN_MENTOR_ID + "='" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and " + COLUMN_STUDENT_NAME + "!=''";
        ArrayList<Absence> absences = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    absences.add(new Absence(
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_NAME)),
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
