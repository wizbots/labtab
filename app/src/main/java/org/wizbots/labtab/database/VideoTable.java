package org.wizbots.labtab.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.wizbots.labtab.LabTabApplication;
import org.wizbots.labtab.LabTabConstants;
import org.wizbots.labtab.controller.LabTabPreferences;
import org.wizbots.labtab.model.video.Video;

import java.util.ArrayList;
import java.util.Collection;

public class VideoTable extends AbstractTable {

    private static final String TAG = VideoTable.class.getName();

    private static final String NAME = "video";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MENTOR_ID = "mentor_id";
    private static final String COLUMN_STATUS = "status";
    private static final String COLUMN_PATH = "path";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_CATEGORY = "category";
    private static final String COLUMN_MENTOR_NAME = "mentor_name";
    private static final String COLUMN_LAB_SKU = "lab_sku";
    private static final String COLUMN_LAB_LEVEL = "lab_level";
    private static final String COLUMN_KNOWLEDGE_NUGGETS = "knowledge_nuggets";
    private static final String COLUMN_DESCRIPTION = "description";
    private static final String COLUMN_PROJECT_CREATORS = "project_creators";
    private static final String COLUMN_NOTES_TO_THE_FAMILY = "notes_to_the_family";
    private static final String COLUMN_EDIT_SYNC_STATUS = "edit_sync_status";
    private static final String COLUMN_IS_TRANSCODING = "is_transcoding";
    private static final String COLUMN_VIDEO = "video";
    private static final String COLUMN_VIDEO_ID = "video_id";
    private static final String COLUMN_PROGRAM_ID = "program_id";
    private static final String COLUMN_DELETE_SYNC_STATUS = "delete_sync_status";


    private DAOManager daoManager = null;
    private final static VideoTable instance;

    static {
        instance = new VideoTable(DAOManager.getInstance());
        DAOManager.getInstance().addTable(instance);
    }

    public static VideoTable getInstance() {
        return instance;
    }

    private VideoTable(DAOManager daoManager) {
        this.daoManager = daoManager;
    }

    @Override
    public void create(SQLiteDatabase db) {
        daoManager.execSQL(db, "CREATE TABLE IF NOT EXISTS "
                + NAME + "("
                + COLUMN_ID + " text PRIMARY KEY,"
                + COLUMN_MENTOR_ID + " text,"
                + COLUMN_STATUS + " integer,"
                + COLUMN_PATH + " text,"
                + COLUMN_TITLE + " text,"
                + COLUMN_CATEGORY + " text,"
                + COLUMN_MENTOR_NAME + " text,"
                + COLUMN_LAB_SKU + " text,"
                + COLUMN_LAB_LEVEL + " text,"
                + COLUMN_KNOWLEDGE_NUGGETS + " text,"
                + COLUMN_DESCRIPTION + " text,"
                + COLUMN_PROJECT_CREATORS + " text,"
                + COLUMN_NOTES_TO_THE_FAMILY + " text,"
                + COLUMN_EDIT_SYNC_STATUS + " text,"
                + COLUMN_IS_TRANSCODING + " text,"
                + COLUMN_VIDEO + " text,"
                + COLUMN_VIDEO_ID + " text,"
                + COLUMN_PROGRAM_ID + " text,"
                + COLUMN_DELETE_SYNC_STATUS + " INTEGER DEFAULT 0);");
    }

    public synchronized void insert(Collection<Video> videos) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            for (Video video : videos) {
                try {
                    insert(db, video);
                } catch (Exception e) {
                    Log.e(TAG, "Error while add video", e);
                }

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert video in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    public synchronized void insert(Video video) {
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                insert(db, video);
            } catch (Exception e) {
                Log.e(TAG, "Error while add video", e);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert video in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    private void insert(SQLiteDatabase db, Video video) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_ID, video.getId());
        values.put(COLUMN_MENTOR_ID, video.getMentor_id());
        values.put(COLUMN_STATUS, video.getStatus());
        values.put(COLUMN_PATH, video.getPath());
        values.put(COLUMN_TITLE, video.getTitle());
        values.put(COLUMN_CATEGORY, video.getCategory());
        values.put(COLUMN_MENTOR_NAME, video.getMentor_name());
        values.put(COLUMN_LAB_SKU, video.getLab_sku());
        values.put(COLUMN_LAB_LEVEL, video.getLab_level());
        values.put(COLUMN_KNOWLEDGE_NUGGETS, video.getKnowledge_nuggets());
        values.put(COLUMN_DESCRIPTION, video.getDescription());
        values.put(COLUMN_PROJECT_CREATORS, video.getProject_creators());
        values.put(COLUMN_NOTES_TO_THE_FAMILY, video.getNotes_to_the_family());
        values.put(COLUMN_EDIT_SYNC_STATUS, video.getEdit_sync_status());
        values.put(COLUMN_IS_TRANSCODING, video.getIs_transCoding());
        values.put(COLUMN_VIDEO, video.getVideo());
        values.put(COLUMN_VIDEO_ID, video.getVideoId());
        values.put(COLUMN_PROGRAM_ID, video.getProgramId());
        db.insertWithOnConflict(NAME, null, values, SQLiteDatabase.CONFLICT_IGNORE);
    }

    public void deleteVideoById(String id){
        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                db.delete(NAME, COLUMN_ID + "=" + id, null);
            } catch (Exception e) {
                Log.e(TAG, "Error while add video", e);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert video in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    public Video getVideoById(String id) {
        final String query = "Select * from " + NAME + " where " + COLUMN_ID + " = '" + id + "'";
        Video video = null;
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery(query, null);
            if (cursor.moveToFirst()) {
                video = new Video(
                        cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                        cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PATH)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAB_SKU)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_LAB_LEVEL)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_KNOWLEDGE_NUGGETS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_CREATORS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_TO_THE_FAMILY)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_EDIT_SYNC_STATUS)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_IS_TRANSCODING)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)),
                        cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID))
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get video", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return video;
    }


    public ArrayList<Video> getVideoList() {
        ArrayList<Video> mentorArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME + " where "
                    + COLUMN_MENTOR_ID + " = '" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id()
                    + "'" + " and " + COLUMN_DELETE_SYNC_STATUS + " = " + 0, null);
            if (cursor.moveToFirst()) {
                do {
                    mentorArrayList.add(
                            new Video(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PATH)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_SKU)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_LEVEL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_KNOWLEDGE_NUGGETS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_CREATORS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_TO_THE_FAMILY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_EDIT_SYNC_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_IS_TRANSCODING)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID))
                            )
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get video", e);
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

    public void updateVideo(Video video) {

        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_MENTOR_ID, video.getMentor_id());
                values.put(COLUMN_STATUS, video.getStatus());
                values.put(COLUMN_PATH, video.getPath());
                values.put(COLUMN_TITLE, video.getTitle());
                values.put(COLUMN_CATEGORY, video.getCategory());
                values.put(COLUMN_MENTOR_NAME, video.getMentor_name());
                values.put(COLUMN_LAB_SKU, video.getLab_sku());
                values.put(COLUMN_LAB_LEVEL, video.getLab_level());
                values.put(COLUMN_KNOWLEDGE_NUGGETS, video.getKnowledge_nuggets());
                values.put(COLUMN_DESCRIPTION, video.getDescription());
                values.put(COLUMN_PROJECT_CREATORS, video.getProject_creators());
                values.put(COLUMN_NOTES_TO_THE_FAMILY, video.getNotes_to_the_family());
                values.put(COLUMN_EDIT_SYNC_STATUS, video.getEdit_sync_status());
                values.put(COLUMN_IS_TRANSCODING, video.getIs_transCoding());
                values.put(COLUMN_VIDEO, video.getVideo());
                values.put(COLUMN_VIDEO_ID, video.getVideoId());
                values.put(COLUMN_PROGRAM_ID, video.getProgramId());
                db.update(NAME, values, COLUMN_ID + " = ?",
                        new String[]{String.valueOf(video.getId())});

            } catch (Exception e) {
                Log.e(TAG, "Error while add video", e);
            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while insert video in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    public void updateDeletedVideo(String videoId, boolean isDeleted) {

        SQLiteDatabase db = null;
        try {
            db = daoManager.getWritableDatabase();
            db.beginTransaction();
            try {
                ContentValues values = new ContentValues();
                values.put(COLUMN_DELETE_SYNC_STATUS, isDeleted ? 1 : 0);
                db.update(NAME, values, COLUMN_ID + " = ?", new String[]{String.valueOf(videoId)});
            } catch (Exception e) {
                Log.e(TAG, "Error while deleting video", e);
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "Error while delete video in Batch", e);
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<Video> getVideosToBeUploaded() {
        ArrayList<Video> mentorArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME + " where " + COLUMN_MENTOR_ID + " = '" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and " + COLUMN_STATUS + " < 100  and edit_sync_status ='" + LabTabConstants.SyncStatus.SYNCED + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    mentorArrayList.add(
                            new Video(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PATH)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_SKU)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_LEVEL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_KNOWLEDGE_NUGGETS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_CREATORS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_TO_THE_FAMILY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_EDIT_SYNC_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_IS_TRANSCODING)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID))
                            )
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get video", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return mentorArrayList;
    }

    public ArrayList<Video> getVideosToBeDeleted() {
        ArrayList<Video> mentorArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME + " where " + COLUMN_MENTOR_ID + " = '" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and " + COLUMN_DELETE_SYNC_STATUS + " = " + 1, null);
            if (cursor.moveToFirst()) {
                do {
                    mentorArrayList.add(
                            new Video(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PATH)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_SKU)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_LEVEL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_KNOWLEDGE_NUGGETS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_CREATORS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_TO_THE_FAMILY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_EDIT_SYNC_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_IS_TRANSCODING)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID))
                            )
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get video", e);
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return mentorArrayList;
    }

    public ArrayList<Video> getEditedVideosToBeUploaded() {
        ArrayList<Video> mentorArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME + " where " + COLUMN_MENTOR_ID + " = '" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and " + COLUMN_EDIT_SYNC_STATUS + " ='" + LabTabConstants.SyncStatus.NOT_SYNCED + "'", null);
            if (cursor.moveToFirst()) {
                do {
                    mentorArrayList.add(
                            new Video(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PATH)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_SKU)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_LEVEL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_KNOWLEDGE_NUGGETS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_CREATORS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_TO_THE_FAMILY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_EDIT_SYNC_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_IS_TRANSCODING)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID))
                            )
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get video", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return mentorArrayList;
    }

    public ArrayList<Video> getAllUnsyncedVideoStatus() {
        ArrayList<Video> mentorArrayList = new ArrayList<>();
        Cursor cursor = null;
        try {
            cursor = daoManager.getReadableDatabase().rawQuery("Select * from " + NAME + " where "
                    + COLUMN_MENTOR_ID + " = '" + LabTabPreferences.getInstance(LabTabApplication.getInstance()).getMentor().getMember_id() + "' and ("
                    + COLUMN_EDIT_SYNC_STATUS + " ='" + LabTabConstants.SyncStatus.NOT_SYNCED + "' OR "
                    + COLUMN_DELETE_SYNC_STATUS + " = " + 1 + " OR ("
                    + COLUMN_STATUS + " < 100  and edit_sync_status ='" + LabTabConstants.SyncStatus.SYNCED + "')"
                    + ")", null);
            if (cursor.moveToFirst()) {
                do {
                    mentorArrayList.add(
                            new Video(
                                    cursor.getString(cursor.getColumnIndex(COLUMN_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_ID)),
                                    cursor.getInt(cursor.getColumnIndex(COLUMN_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PATH)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_CATEGORY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_MENTOR_NAME)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_SKU)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_LAB_LEVEL)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_KNOWLEDGE_NUGGETS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROJECT_CREATORS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_NOTES_TO_THE_FAMILY)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_EDIT_SYNC_STATUS)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_IS_TRANSCODING)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_VIDEO_ID)),
                                    cursor.getString(cursor.getColumnIndex(COLUMN_PROGRAM_ID))
                            )
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "Error while get video", e);
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }
        }
        return mentorArrayList;
    }


}
