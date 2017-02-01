package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.wizbots.labtab.model.student.StudentProfile;

import java.util.Collection;

public class StudentsProfileTable extends AbstractTable {

    private static final String TAG = StudentsProfileTable.class.getName();

    private static final String NAME = "students_profile";

    private static final String COLUMN_ENROLLMENTS_COUNT = "enrollments_count";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_DATE_OF_BIRTH = "date_of_birth";
    private static final String COLUMN_CREATOR = "creator";
    private static final String COLUMN_GRADE = "grade";
    private static final String COLUMN_AFTER_CARE_AFTER = "after_care_after";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_ABSENCE_COUNT = "absence_count";
    private static final String COLUMN_ALLERGIES = "allergies";
    private static final String COLUMN_SPECIAL_NEEDS = "special_needs";
    private static final String COLUMN_AFTER_CARE_BEFORE = "after_care_before";
    private static final String COLUMN_AFTER_CARE_PHONE = "after_care_phone";
    private static final String COLUMN_AFTER_CARE_NAME = "after_care_name";

    private DAOManager daoManager = null;
    private final static StudentsProfileTable instance;

    static {
        instance = new StudentsProfileTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static StudentsProfileTable getInstance() {
        return instance;
    }

    private StudentsProfileTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {

        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_ID + " text PRIMARY KEY,"
                + COLUMN_ENROLLMENTS_COUNT + " text,"
                + COLUMN_LAST_NAME + " text,"
                + COLUMN_DATE_OF_BIRTH + " text,"
                + COLUMN_CREATOR + " text,"
                + COLUMN_GRADE + " text,"
                + COLUMN_AFTER_CARE_AFTER + " text,"
                + COLUMN_FIRST_NAME + " text,"
                + COLUMN_LEVEL + " text,"
                + COLUMN_ABSENCE_COUNT + " text,"
                + COLUMN_ALLERGIES + " text,"
                + COLUMN_SPECIAL_NEEDS + " text,"
                + COLUMN_AFTER_CARE_BEFORE + " text,"
                + COLUMN_AFTER_CARE_PHONE + " text,"
                + COLUMN_AFTER_CARE_NAME + " text);");
    }

    public synchronized void insert(Collection<StudentProfile> studentProfiles) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (StudentProfile studentProfile : studentProfiles) {
                try {
                    insert(db, studentProfile);
                } catch (Exception e) {
                    Log.e(TAG, "Error while add student student profile", e);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert student profile in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    public synchronized void insert(StudentProfile studentProfile) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                insert(db, studentProfile);
            } catch (Exception e) {
                Log.e(TAG, "Error while add student student profile", e);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while add student student profile", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, StudentProfile studentProfile) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ENROLLMENTS_COUNT, studentProfile.getEnrollments_count());
        values.put(COLUMN_LAST_NAME, studentProfile.getLast_name());
        values.put(COLUMN_DATE_OF_BIRTH, studentProfile.getDate_of_birth());
        values.put(COLUMN_CREATOR, studentProfile.getCreator());
        values.put(COLUMN_GRADE, studentProfile.getGrade());
        values.put(COLUMN_AFTER_CARE_AFTER, studentProfile.getAfter_care_after());
        values.put(COLUMN_ID, studentProfile.getId());
        values.put(COLUMN_FIRST_NAME, studentProfile.getFirst_name());
        values.put(COLUMN_LEVEL, studentProfile.getLevel());
        values.put(COLUMN_ABSENCE_COUNT, studentProfile.getAbsence_count());
        values.put(COLUMN_ALLERGIES, studentProfile.getAllergies());
        values.put(COLUMN_SPECIAL_NEEDS, studentProfile.getSpecial_needs());
        values.put(COLUMN_AFTER_CARE_BEFORE, studentProfile.getAfter_care_before());
        values.put(COLUMN_AFTER_CARE_PHONE, studentProfile.getAfter_care_phone());
        values.put(COLUMN_AFTER_CARE_NAME, studentProfile.getAfter_care_name());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public StudentProfile getStudentProfileById(String id) {
        final String query = "Select * from " + NAME + " where " + COLUMN_ID + " = '" + id + "'";
        StudentProfile studentProfile = null;
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                studentProfile = new StudentProfile(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ENROLLMENTS_COUNT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DATE_OF_BIRTH)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CREATOR)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_GRADE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AFTER_CARE_AFTER)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ABSENCE_COUNT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ALLERGIES)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SPECIAL_NEEDS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AFTER_CARE_BEFORE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AFTER_CARE_PHONE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AFTER_CARE_NAME))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get student", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return studentProfile;
    }

    public void upDateStudentLevel(String studentId, String level) {
        String updateStudentLevelQuery = "UPDATE " + NAME + " SET " + COLUMN_LEVEL + " = '" + level + "' WHERE " + COLUMN_ID + "= '" + studentId + "';";
        SQLiteDatabase db = daoManager.getWritableDatabase();
        SQLiteStatement stmt = db.compileStatement(updateStudentLevelQuery);
        stmt.execute();
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
