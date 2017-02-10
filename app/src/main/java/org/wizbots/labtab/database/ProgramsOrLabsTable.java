package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.model.LocationResponse;
import org.wizbots.labtab.model.ProgramOrLab;
import org.wizbots.labtab.model.program.Student;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class ProgramsOrLabsTable extends AbstractTable {

    private static final String TAG = ProgramsOrLabsTable.class.getName();

    private static final String NAME = "programs_or_labs";

    private static final String COLUMN_PROGRAM_ID = "id";
    private static final String COLUMN_MEMBER_ID = "member_id";
    private static final String COLUMN_SKU = "sku";
    private static final String COLUMN_ENDS = "ends";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_STARTS = "starts";
    private static final String COLUMN_STATE = "state";
    private static final String COLUMN_STREET = "street";
    private static final String COLUMN_ENROLLMENT_COUNT = "enrollment_count";
    private static final String COLUMN_ADDRESS = "address";
    private static final String COLUMN_SEASON = "season";
    private static final String COLUMN_LOCATION = "location_id";
    private static final String COLUMN_LEVEL = "level";
    private static final String COLUMN_YEAR = "season_year";


    private DAOManager daoManager = null;
    private final static ProgramsOrLabsTable instance;

    static {
        instance = new ProgramsOrLabsTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static ProgramsOrLabsTable getInstance() {
        return instance;
    }

    private ProgramsOrLabsTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_PROGRAM_ID + " text PRIMARY KEY,"
                + COLUMN_SKU + " integer,"
                + COLUMN_MEMBER_ID + " text,"
                + COLUMN_ENDS + " text,"
                + COLUMN_TITLE + " text,"
                + COLUMN_STARTS + " text,"
                + COLUMN_STATE + " text,"
                + COLUMN_STREET + " text,"
                + COLUMN_ENROLLMENT_COUNT + " integer,"
                + COLUMN_ADDRESS + " text,"
                + COLUMN_SEASON + " text,"
                + COLUMN_LOCATION + " text,"
                + COLUMN_LEVEL + " text,"
                + COLUMN_YEAR + " text);");
    }

    public synchronized void insert(Collection<ProgramOrLab> programOrLabs) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (ProgramOrLab programOrLab : programOrLabs) {
                try {
                    insert(db, programOrLab);
                } catch (Exception e) {
                    Log.e(TAG, "Error while add mentor", e);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert mentor in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, ProgramOrLab programOrLab) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PROGRAM_ID, programOrLab.getId());
        values.put(COLUMN_MEMBER_ID, programOrLab.getMember_id());
        values.put(COLUMN_SKU, programOrLab.getSku());
        values.put(COLUMN_ENDS, programOrLab.getEnds());
        values.put(COLUMN_TITLE, programOrLab.getTitle());
        values.put(COLUMN_STARTS, programOrLab.getStarts());
        values.put(COLUMN_STATE, programOrLab.getState());
        values.put(COLUMN_STREET, programOrLab.getStreet());
        values.put(COLUMN_ENROLLMENT_COUNT, programOrLab.getEnrollment_count());
        values.put(COLUMN_ADDRESS, programOrLab.getAddress());
        values.put(COLUMN_SEASON, programOrLab.getSeason());
        values.put(COLUMN_LOCATION, programOrLab.getLocation());
        values.put(COLUMN_LEVEL, programOrLab.getLevel());
        values.put(COLUMN_YEAR, programOrLab.getYear());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public ArrayList<ProgramOrLab> getFilteredData(String memberId, Map<String, String> params){
        ArrayList<ProgramOrLab> programOrLabs = new ArrayList<>();
        Cursor cursor = null;
        try {

            final String query = getFilterQuery(memberId,params);
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    programOrLabs.add(
                            new ProgramOrLab(
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_SKU)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ENDS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STARTS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STATE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STREET)),
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_ENROLLMENT_COUNT)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_SEASON)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_YEAR))
                            ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get programs or labs", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return programOrLabs;
    }

    private String getFilterQuery(String memberId, Map<String, String> params){
        StringBuilder query = new StringBuilder("Select * from " + NAME + " where " + COLUMN_MEMBER_ID + " = '" + memberId + "'");
        StringBuilder orderBy = new StringBuilder(" ORDER BY " + COLUMN_TITLE + " ASC");
        if(params != null && params.size() > 0){
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if(entry.getKey().equalsIgnoreCase(COLUMN_LOCATION)){
                    LocationResponse location = LocationTable.getInstance().getLocationById(entry.getValue());
                    params.put(COLUMN_LOCATION, location.getName());
                }
                query.append(" and ").append(entry.getKey()).append(" = '").append(entry.getValue()).append("'");
            }
        }
        return query.toString();
    }

    public ArrayList<ProgramOrLab> getProgramsByMemberId(String memberId) {
        ArrayList<ProgramOrLab> programOrLabs = new ArrayList<>();
        Cursor cursor = null;
        try {
            final String query = "Select * from " + NAME + " where " + COLUMN_MEMBER_ID + " = '" + memberId + "'" + " ORDER BY " + COLUMN_TITLE + " ASC";
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                do {
                    programOrLabs.add(
                            new ProgramOrLab(
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_SKU)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MEMBER_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ENDS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STARTS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STATE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_STREET)),
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_ENROLLMENT_COUNT)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ADDRESS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_SEASON)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LOCATION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LEVEL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_YEAR))
                            ));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get programs or labs", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return programOrLabs;
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
