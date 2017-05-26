package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.model.student.StudentStats;
import org.wizbots.labtab.util.LabTabUtil;

import java.util.ArrayList;
import java.util.Collection;

public class StudentStatsTable extends AbstractTable {

    private static final String TAG = StudentStatsTable.class.getName();

    private static final String NAME = "students_stats";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LEVEL = "level";

    private static final String COLUMN_PROJECT_COUNT = "project_count";
    private static final String COLUMN_LAB_TIME_COUNT = "lab_time_count";
    private static final String COLUMN_DONE_COUNT = "done_count";
    private static final String COLUMN_SKIPPED_COUNT = "skipped_count";
    private static final String COLUMN_PENDING_COUNT = "pending_count";

    private static final String COLUMN_PROJECTS = "projects";
//    private static final String COLUMN_DONE = "projects_done";
//    private static final String COLUMN_SKIPPED = "projects_skipped";
//    private static final String COLUMN_PENDING = "projects_pending";

    private static final String COLUMN_IMAGINEERING_COUNT = "imagineering_count";
    private static final String COLUMN_PROGRAMMING_COUNT = "programming_count";
    private static final String COLUMN_MECHANISMS_COUNT = "mechanisms_count";
    private static final String COLUMN_STRUCTURES_COUNT = "structures_count";

    private static final String COLUMN_IMAGINEERING = "projects_imagineering";
    private static final String COLUMN_PROGRAMMING = "projects_programming";
    private static final String COLUMN_MECHANISMS = "projects_mechanisms";
    private static final String COLUMN_STRUCTURES = "projects_structures";


    private DAOManager daoManager = null;
    private final static StudentStatsTable instance;

    static {
        instance = new StudentStatsTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static StudentStatsTable getInstance() {
        return instance;
    }

    private StudentStatsTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_ID + " text,"
                + COLUMN_LEVEL + " text,"

                + COLUMN_PROJECT_COUNT + " integer,"
                + COLUMN_LAB_TIME_COUNT + " integer,"
                + COLUMN_DONE_COUNT + " integer,"
                + COLUMN_SKIPPED_COUNT + " integer,"
                + COLUMN_PENDING_COUNT + " integer,"

                + COLUMN_PROJECTS + " text,"
//                + COLUMN_DONE + " text,"
//                + COLUMN_SKIPPED + " text,"
//                + COLUMN_PENDING + " text,"

                + COLUMN_IMAGINEERING_COUNT + " integer,"
                + COLUMN_PROGRAMMING_COUNT + " integer,"
                + COLUMN_MECHANISMS_COUNT + " integer,"
                + COLUMN_STRUCTURES_COUNT + " integer,"

                + COLUMN_IMAGINEERING + " text,"
                + COLUMN_PROGRAMMING + " text,"
                + COLUMN_MECHANISMS + " text,"
                + COLUMN_STRUCTURES + " text, PRIMARY KEY(id,level));");

    }

    public synchronized void insert(Collection<StudentStats> studentStatsCollection) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (StudentStats studentStats : studentStatsCollection) {
                try {
                    insert(db, studentStats);
                } catch (Exception e) {
                    Log.e(TAG, "Error while add student student stats", e);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert student stats", e);
        } finally {
            db.endTransaction();
        }
    }

    public synchronized void insert(StudentStats studentStats) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                insert(db, studentStats);
            } catch (Exception e) {
                Log.e(TAG, "Error while add student student stats", e);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert student stats", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, StudentStats studentStats) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, studentStats.getId());
        values.put(COLUMN_LEVEL, studentStats.getLevel());

        values.put(COLUMN_PROJECT_COUNT, studentStats.getProject_count());
        values.put(COLUMN_LAB_TIME_COUNT, LabTabUtil.toJson(studentStats.getLab_time_count()));
        values.put(COLUMN_DONE_COUNT, studentStats.getDone_count());
        values.put(COLUMN_SKIPPED_COUNT, studentStats.getSkipped_count());
        values.put(COLUMN_PENDING_COUNT, studentStats.getPending_count());

        values.put(COLUMN_PROJECTS, studentStats.getProjects());
//        values.put(COLUMN_DONE, studentStats.getProjects_done());
//        values.put(COLUMN_SKIPPED, studentStats.getProjects_skipped());
//        values.put(COLUMN_PENDING, studentStats.getProjects_pending());

        values.put(COLUMN_IMAGINEERING_COUNT, studentStats.getImagineering_count());
        values.put(COLUMN_PROGRAMMING_COUNT, studentStats.getProgramming_count());
        values.put(COLUMN_MECHANISMS_COUNT, studentStats.getMechanisms_count());
        values.put(COLUMN_STRUCTURES_COUNT, studentStats.getStructures_count());

        values.put(COLUMN_IMAGINEERING, studentStats.getProjects_imagineering());
        values.put(COLUMN_PROGRAMMING, studentStats.getProjects_programming());
        values.put(COLUMN_MECHANISMS, studentStats.getProjects_mechanisms());
        values.put(COLUMN_STRUCTURES, studentStats.getProjects_structures());

        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<StudentStats> getStudentStatsById(String id) {
        final String query = "Select * from " + NAME + " where " + COLUMN_ID + " = '" + id + "'";
        ArrayList<StudentStats> statsArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    statsArrayList.add(new StudentStats(
                            cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_PROJECT_COUNT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_LAB_TIME_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_DONE_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_SKIPPED_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_PENDING_COUNT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PROJECTS)),
//                            cursor.getString(cursor.getColumnIndex(COLUMN_DONE)),
//                            cursor.getString(cursor.getColumnIndex(COLUMN_SKIPPED)),
//                            cursor.getString(cursor.getColumnIndex(COLUMN_PENDING)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_IMAGINEERING_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_PROGRAMMING_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_MECHANISMS_COUNT)),
                            cursor.getInt(cursor.getColumnIndex(COLUMN_STRUCTURES_COUNT)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_IMAGINEERING)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAMMING)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_MECHANISMS)),
                            cursor.getString(cursor.getColumnIndex(COLUMN_STRUCTURES))
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
        return statsArrayList;
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
