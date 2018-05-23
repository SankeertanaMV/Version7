package com.example.sankeerthana.version7;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class UsersDBAdapter {
    public static final String KEY_ROWID = "_id";
    public static final String KEY_NAME = "name";
    public static final String KEY_FLATNUMBER = "flatnumber";
    public static final String KEY_BILL_EP1 = "bill_ep1";
    public static final String KEY_BILL_EP2 = "bill_ep2";
    public static final String KEY_BILL_EP3 = "bill_ep3";
    public static final String KEY_BILL_FLAT = "bill_flat";
    public static final String KEY_USAGE_EP1 = "usage_ep1";
    public static final String KEY_USAGE_EP2 = "usage_ep2";
    public static final String KEY_USAGE_EP3 = "usage_ep3";
    public static final String KEY_USAGE_FLAT = "usage_flat";

    private static final String TAG = "UsersDBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    private static final String DATABASE_NAME = "Userdetails";
    private static final String SQLITE_TABLE = "Details";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + SQLITE_TABLE + " (" +
                    KEY_ROWID + " integer PRIMARY KEY autoincrement,"
                    + KEY_NAME + " text,"
                    + KEY_FLATNUMBER + " text,"
                    + KEY_BILL_EP1 + " real,"
                    + KEY_BILL_EP2 + " real,"
                    + KEY_BILL_EP3 + " real,"
                    + KEY_BILL_FLAT + " real,"
                    + KEY_USAGE_EP1 + " real,"
                    + KEY_USAGE_EP2 + " real,"
                    + KEY_USAGE_EP3 + " real,"
                    + KEY_USAGE_FLAT + " real,"
                    + " UNIQUE ("+KEY_FLATNUMBER +")) ;";

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + SQLITE_TABLE);
            onCreate(db);
        }
    }

    public UsersDBAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    public UsersDBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createUser(String name, String flatnumber,float bill_ep1, float bill_ep2,float bill_ep3,float bill_flat,
                           float usage_ep1,float usage_ep2,float usage_ep3,float usage_flat) {

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, name);
        initialValues.put(KEY_FLATNUMBER, flatnumber);
        initialValues.put(KEY_BILL_EP1, bill_ep1);
        initialValues.put(KEY_BILL_EP2, bill_ep2);
        initialValues.put(KEY_BILL_EP3, bill_ep3);
        initialValues.put(KEY_BILL_FLAT, bill_flat);
        initialValues.put(KEY_USAGE_EP1, usage_ep1);
        initialValues.put(KEY_USAGE_EP2, usage_ep2);
        initialValues.put(KEY_USAGE_EP3, usage_ep3);
        initialValues.put(KEY_USAGE_FLAT, usage_flat);

        return mDb.insert(SQLITE_TABLE, null, initialValues);
    }

    public boolean deleteAllUsers() {

        int doneDelete = 0;
        doneDelete = mDb.delete(SQLITE_TABLE, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;

    }

    public Cursor fetchUsersByName(String inputText) throws SQLException {
        Log.w(TAG, inputText);
        Cursor mCursor = null;
        if (inputText == null  ||  inputText.length () == 0)  {
            mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_NAME, KEY_FLATNUMBER, KEY_BILL_EP1, KEY_BILL_EP2,KEY_BILL_EP3,KEY_BILL_FLAT,
                            KEY_USAGE_EP1,KEY_USAGE_EP2,KEY_USAGE_EP3,KEY_USAGE_FLAT},
                    null, null, null, null, null);

        }
        else {
            mCursor = mDb.query(true, SQLITE_TABLE, new String[] {KEY_ROWID,
                            KEY_NAME, KEY_FLATNUMBER, KEY_BILL_EP1, KEY_BILL_EP2,KEY_BILL_EP3,KEY_BILL_FLAT,
                            KEY_USAGE_EP1,KEY_USAGE_EP2,KEY_USAGE_EP3,KEY_USAGE_FLAT},
                    KEY_NAME + " like '%" + inputText + "%'", null,
                    null, null, null, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;

    }
    public Cursor fetchAllUsers() {

        Cursor mCursor = mDb.query(SQLITE_TABLE, new String[] {KEY_ROWID,
                        KEY_NAME, KEY_FLATNUMBER, KEY_BILL_EP1, KEY_BILL_EP2,KEY_BILL_EP3,KEY_BILL_FLAT,
                        KEY_USAGE_EP1,KEY_USAGE_EP2,KEY_USAGE_EP3,KEY_USAGE_FLAT},
                null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeUsers() {

        createUser("Sankeerthana","204",10,20,30,40,50,60,70,80);
        createUser("Ravi","205",5,15,25,35,45,55,65,75);


    }


}


