package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;
import java.util.Collection;

public class ProgramStudentsTable extends AbstractTable {

    private static final String TAG = ProgramStudentsTable.class.getName();

    private static final String NAME = "program_students";

    private static final String COLUMN_PROGRAM_ID = "program_id";
    private static final String COLUMN_MEMBER_ID = "member_id";
    private static final String COLUMN_STUDENT_ID = "id";
    private static final String COLUMN_LAB_TIME = "lab_time";
    private static final String COLUMN_COMPLETED = "completed";
    private static final String COLUMN_SKIPPED = "skipped";
    private static final String COLUMN_PENDING = "pending";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_WIZCHIPS = "wizchips";
    private static final String COLUMN_SPECIAL_NEEDS = "special_needs";
    private static final String COLUMN_SELF_SIGN_OUT = "self_sign_out";
    private static final String COLUMN_PICKUP_INSTRUCTIONS = "pickup_instructions";

    private DAOManager daoManager = null;
    private final static ProgramStudentsTable instance;

    static {
        instance = new ProgramStudentsTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static ProgramStudentsTable getInstance() {
        return instance;
    }

    private ProgramStudentsTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_PROGRAM_ID + " text,"
                + COLUMN_MEMBER_ID + " text,"
                + COLUMN_STUDENT_ID + " text,"
                + COLUMN_LAB_TIME + " string,"
                + COLUMN_COMPLETED + " integer,"
                + COLUMN_SKIPPED + " integer,"
                + COLUMN_PENDING + " integer,"
                + COLUMN_NAME + " text,"
                + COLUMN_LEVEL + " text,"
                + COLUMN_WIZCHIPS + " integer,"
                + COLUMN_SPECIAL_NEEDS + " text,"
                + COLUMN_SELF_SIGN_OUT + " integer,"
                + COLUMN_PICKUP_INSTRUCTIONS + " text, PRIMARY KEY(member_id,program_id,id));");
    }

    public synchronized void insert(Student student) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                insert(db, student);
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

    public synchronized void insert(Collection<Student> students) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (Student student : students) {
                try {
                    insert(db, student);
                } catch (Exception e) {
                    Log.e(TAG, "Error while add student", e);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert student in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, Student student) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRAM_ID, student.getProgram_id());
        values.put(COLUMN_MEMBER_ID, student.getMember_id());
        values.put(COLUMN_STUDENT_ID, student.getStudent_id());
        values.put(COLUMN_LAB_TIME, student.getLab_time());
        values.put(COLUMN_COMPLETED, student.getCompleted());
        values.put(COLUMN_SKIPPED, student.getSkipped());
        values.put(COLUMN_PENDING, student.getPending());
        values.put(COLUMN_NAME, student.getName());
        values.put(COLUMN_LEVEL, student.getLevel());
        values.put(COLUMN_WIZCHIPS, student.getWizchips());
        values.put(COLUMN_SPECIAL_NEEDS, student.getSpecial_needs());
        values.put(COLUMN_SELF_SIGN_OUT, student.getSelf_sign_out());
        values.put(COLUMN_PICKUP_INSTRUCTIONS, student.getPickup_instructions());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<Student> getStudentsListByProgramId(String programId) {
        final String query = "Select * from " + NAME + " where " + COLUMN_PROGRAM_ID + " = '" + programId + "' and " + COLUMN_MEMBER_ID + "='" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "'";
        ArrayList<Student> students = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    students.add(new Student(
                            cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_STUDENT_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_LAB_TIME)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_COMPLETED)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_SKIPPED)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_PENDING)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_WIZCHIPS)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_SPECIAL_NEEDS)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_SELF_SIGN_OUT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PICKUP_INSTRUCTIONS))
                    ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get students", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return students;
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
