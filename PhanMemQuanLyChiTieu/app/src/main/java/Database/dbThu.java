package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbThu extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "thu";
    public static final String COL_NAME = "ten";
    public static final String COL_TIEN = "tien";
    public static final String COL_NHOM = "nhom";
    public static final String COL_DATE = "ngaythang";
    public static final String COL_GHICHU = "ghichu";
    private static final String DB_NAME = "chitieu";
    private static final int DB_VERSION = 1;
    private static final String STRING_CREATE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT," + COL_TIEN + " TEXT," + COL_NHOM + " TEXT," + COL_DATE + " TEXT," + COL_GHICHU + " TEXT);";

    public dbThu(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STRING_CREATE);
        ContentValues cv = new ContentValues(3);
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
