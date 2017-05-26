package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.model.program.Program;

public class ProgramTable extends AbstractTable {

    private static final String TAG = ProgramTable.class.getName();

    private static final String NAME = "program";

    private static final String COLUMN_PROGRAM_ID = "id";
    private static final String COLUMN_MEMBER_ID = "member_id";
    private static final String COLUMN_GRADES = "grades";
    private static final String COLUMN_LOCATION = "location";
    private static final String COLUMN_STARTS = "starts";
    private static final String COLUMN_CAPACITY = "capacity";
    private static final String COLUMN_TIME_SLOT = "time_slot";
    private static final String COLUMN_AVAILABILITY = "availability";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_SESSIONS = "sessions";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_SKU = "sku";
    private static final String COLUMN_ENDS = "ends";
    private static final String COLUMN_ROOM = "room";
    private static final String COLUMN_SEASON = "season";
    private static final String COLUMN_PRICE = "price";


    private DAOManager daoManager = null;
    private final static ProgramTable instance;

    static {
        instance = new ProgramTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static ProgramTable getInstance() {
        return instance;
    }

    private ProgramTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_PROGRAM_ID + " text PRIMARY KEY,"
                + COLUMN_MEMBER_ID + " text,"
                + COLUMN_GRADES + " text,"
                + COLUMN_LOCATION + " text,"
                + COLUMN_STARTS + " text,"
                + COLUMN_CAPACITY + " integer,"
                + COLUMN_TIME_SLOT + " text,"
                + COLUMN_AVAILABILITY + " text,"
                + COLUMN_NAME + " text,"
                + COLUMN_SESSIONS + " text,"
                + COLUMN_CATEGORY + " text,"
                + COLUMN_SKU + " integer,"
                + COLUMN_ENDS + " text,"
                + COLUMN_ROOM + " text,"
                + COLUMN_SEASON + " text,"
                + COLUMN_PRICE + " text);");
    }

    public synchronized void insert(Program program) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                insert(db, program);
            } catch (Exception e) {
                Log.e(TAG, "Error while add program", e);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert program", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, Program program) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRAM_ID, program.getId());
        values.put(COLUMN_MEMBER_ID, program.getMember_id());
        values.put(COLUMN_GRADES, program.getGrades());
        values.put(COLUMN_LOCATION, program.getLocation());
        values.put(COLUMN_STARTS, program.getStarts());
        values.put(COLUMN_CAPACITY, program.getCapacity());
        values.put(COLUMN_TIME_SLOT, program.getTime_slot());
        values.put(COLUMN_AVAILABILITY, program.getAvailability());
        values.put(COLUMN_NAME, program.getName());
        values.put(COLUMN_SESSIONS, program.getSessions());
        values.put(COLUMN_CATEGORY, program.getCategory());
        values.put(COLUMN_SKU, program.getSku());
        values.put(COLUMN_ENDS, program.getEnds());
        values.put(COLUMN_ROOM, program.getRoom());
        values.put(COLUMN_SEASON, program.getSeason());
        values.put(COLUMN_PRICE, program.getPrice());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public Program getProgramByProgramId(String programId) {
        final String query = "Select * from " + NAME + " where " + COLUMN_PROGRAM_ID + " = '" + programId + "' and " + COLUMN_MEMBER_ID + "='" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "'";
        Program program = null;
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                program = new Program(
                        cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_GRADES)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_STARTS)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_CAPACITY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TIME_SLOT)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_AVAILABILITY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SESSIONS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_SKU)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ENDS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_ROOM)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_SEASON)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PRICE))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get program", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return program;
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
