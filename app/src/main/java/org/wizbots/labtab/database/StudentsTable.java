package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.model.student.Student;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;
import java.util.Collection;

public class StudentsTable extends AbstractTable {

    private static final String TAG = StudentsTable.class.getName();

    private static final String NAME = "students";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_FIRST_NAME = "first_name";
    private static final String COLUMN_LAST_NAME = "last_name";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_PROJECTS_HISTORY = "projects_history";

    private DAOManager daoManager = null;
    private final static StudentsTable instance;

    static {
        instance = new StudentsTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static StudentsTable getInstance() {
        return instance;
    }

    private StudentsTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_ID + " text PRIMARY KEY,"
                + COLUMN_FIRST_NAME + " text,"
                + COLUMN_LAST_NAME + " text,"
                + COLUMN_LEVEL + " text,"
                + COLUMN_PROJECTS_HISTORY + " text);");
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
        values.put(COLUMN_ID, student.getId());
        values.put(COLUMN_FIRST_NAME, student.getFirst_name());
        values.put(COLUMN_LAST_NAME, student.getLast_name());
        values.put(COLUMN_LEVEL, student.getLevel());
        values.put(COLUMN_PROJECTS_HISTORY, LabTabUtil.convertProjectsToString(student.getProjects_history()));
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Student getStudentById(String id) {
        final String query = "Select * from " + NAME + " where " + COLUMN_ID + " = " + id;
        Student student = null;
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                student = new Student(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)),
                        LabTabUtil.convertStringToProjects(cursor.getString(cursor.getColumnIndex(COLUMN_PROJECTS_HISTORY)))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get student", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return student;
    }


    private ArrayList<Student> getStudentsList() {
        ArrayList<Student> studentArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME, null);
            if (cursor.moveToFirst()) {
                do {
                    studentArrayList.add(
                            new Student(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_FIRST_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAST_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)),
                                    LabTabUtil.convertStringToProjects(cursor.getString(cursor.getColumnIndex(COLUMN_PROJECTS_HISTORY)))
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
        return studentArrayList;
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
