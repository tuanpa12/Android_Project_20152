package Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class dbChi extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "chi";
    public static final String COL_NAME = "tenchi";
    public static final String COL_TIEN = "tienchi";
    public static final String COL_NHOM = "nhomchi";
    public static final String COL_DATE = "ngaythangchi";
    public static final String COL_GHICHU = "ghichuchi";
    private static final String DB_NAME = "tienthu";
    private static final int DB_VERSION = 1;
    private static final String STRING_CREATE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT," + COL_TIEN + " TEXT," + COL_NHOM + " TEXT," + COL_DATE + " TEXT," + COL_GHICHU + " TEXT);";

    public dbChi(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(STRING_CREATE);
        ContentValues cv = new ContentValues();
        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

}
