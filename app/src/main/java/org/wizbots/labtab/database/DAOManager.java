package org.wizbots.labtab.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.interfaces.OnLoadListener;

import java.util.ArrayList;

public class DAOManager extends SQLiteOpenHelper implements OnLoadListener {

    public static final String TAG = DAOManager.class.getName();
    public static final String CLIENT_DATABASE_NAME = "labtab.db";
//        public static final String CLIENT_DATABASE_NAME = "/sdcard/LABTAB/labtab.db";
    public static final int DATABASE_VERSION = 1;

    private static DAOManager _instance = null;

    static {
        _instance = new DAOManager(LabTabApplication.getInstance());
        LabTabApplication.getInstance().addManager(_instance);
    }

    private ArrayList<DatabaseTable> registeredTables;

    public DAOManager(Context context) {
        super(context, CLIENT_DATABASE_NAME, null, DATABASE_VERSION);
        registeredTables = new ArrayList<>();
    }

    public static DAOManager getInstance() {
        return _instance;
    }


	/*public void open() throws SQLException {
        try{
			database = getWritableDatabase();
		//	database.execSQL("pragma foriegn_keys=on;");
		}
		catch (Exception e) {
			Log.d("exception",e.toString());
		}
	}*/

    @Override
    public void onLoad() {
        try {
            getWritableDatabase(); // Force onCreate or onUpgrade
        } catch (SQLiteException e) {
            throw e;
        }
    }

    public void addTable(DatabaseTable table) {
        registeredTables.add(table);
    }

    public void execSQL(SQLiteDatabase db, String sql) {
        db.execSQL(sql);
    }

    public void dropTable(SQLiteDatabase db, String table) {
        execSQL(db, "DROP TABLE IF EXISTS " + table + ";");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (DatabaseTable table : registeredTables)
            table.create(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (DatabaseTable table : registeredTables)
            table.migrate(db, newVersion);
    }

}