package Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Legendary on 27/04/2016.
 */
public class UserDatabase extends SQLiteOpenHelper {
    public static final String TABLE_NAME = "userDB";
    public static final String COL_NAME = "name";
    public static final String COL_EMAIL = "email";
    public static final String COL_KEY = "key";
    private static final String DB_NAME = "user data";
    private static final int DB_VERSION = 1;
    private static final String CREATE = "CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COL_NAME + " TEXT," + COL_EMAIL + " TEXT," + COL_KEY + " TEXT);";

    public UserDatabase(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE);
//        ContentValues cv = new ContentValues();
//        db.insert(TABLE_NAME, null, cv);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
