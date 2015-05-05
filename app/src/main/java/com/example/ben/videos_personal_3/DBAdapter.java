package com.example.ben.videos_personal_3;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

    // TAG  used in the onUpgrade() method in the inner class DatabaseHelper
    public static final String TAG = "DBAdapter";

    public static final String COL_KEY_NAME_ID = "_id";
    public static final String COL_NAME_VIDEO = "video";
    public static final String COL_NAME_VIDEO_PATH = "videoPath";

    public static final int COL_VIDEO_PATH = 2;

    // ALL_KEYS  used in  getAllRows()  and  getRow()  methods in running
    // a query to return a cursor.
    public static final String[] ALL_KEYS = new String[] {COL_KEY_NAME_ID,
            COL_NAME_VIDEO, COL_NAME_VIDEO_PATH};

    public static final String DATABASE_NAME = "dbVideos";
    public static final String DATABASE_TABLE = "videoTable";

    // NOTE:  DATABASE_VERSION  value MUST be incremented if the structure (schema)
    // of the database is changed.  This will cause the old database to be
    // destroyed and a new version to be created
    public static final int DATABASE_VERSION = 2;

    private static final String DATABASE_CREATOR_SQL = "CREATE TABLE "
            + DATABASE_TABLE + " ("
            + COL_KEY_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COL_NAME_VIDEO + " TEXT NOT NULL, "
            + COL_NAME_VIDEO_PATH + " TEXT NOT NULL"
            + ");";

    private final Context context;
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase db;

    // constructor
    public DBAdapter(Context ctx) {
        this.context = ctx;
    }

    // helper class for database actions:
    // creation of database if not already created, or upgrade it to a new version
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase _db) {
            _db.execSQL(DATABASE_CREATOR_SQL);
        }

        @Override
        public void onUpgrade(SQLiteDatabase _db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading application database version from " + oldVersion
                    + " to " + newVersion + " - all old data will be destroyed.");

            // destroy old database
            _db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);

            // create new database
            onCreate(_db);
        }
    }

    public DBAdapter open() {
        mDBHelper = new DatabaseHelper(context);
        db = mDBHelper.getWritableDatabase();
        return this;
    }

    public Cursor getRow(long rowId) {
        String where = COL_KEY_NAME_ID + "=" + rowId;
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS,
                where, null, null, null, null, null);
        if(c != null){
            c.moveToFirst();
        }
        return c;
    }

    public Cursor getAllRows() {
        Cursor c = db.query(true, DATABASE_TABLE, ALL_KEYS, null, null, null, null, null, null);
        if(c != null) {
            c.moveToFirst();
        }
        return c;
    }

    public void insertRow(String video, String path) {
        ContentValues newValues = new ContentValues();
        newValues.put(COL_NAME_VIDEO, video);
        newValues.put(COL_NAME_VIDEO_PATH, path);

        db.insert(DATABASE_TABLE, null, newValues);
    }

    public void deleteRow(long rowId) {
        String where = COL_KEY_NAME_ID + "=" + rowId;
        db.delete(DATABASE_TABLE, where, null);
    }

}
