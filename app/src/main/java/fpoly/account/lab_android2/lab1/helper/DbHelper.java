package fpoly.account.lab_android2.lab1.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "todoList.db";
    private static final int DATABASE_VERSION = 5;

    public static final String COLUMN_NAME_ID = "ID";
    public static final String COLUMN_NAME_TITLE = "TITLE";
    public static final String COLUMN_NAME_CONTENT = "CONTENT";
    public static final String COLUMN_NAME_DATE = "DATE";
    public static final String COLUMN_NAME_TYPE = "TYPE";
    public static final String COLUMN_NAME_STATUS = "STATUS";

    public static final String TABLE_TODO_NAME = "TODO";
    public static final String COLUMN_NAME_IMAGE = "IMAGE";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE "+ TABLE_TODO_NAME
                + " (" + COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_NAME_TITLE + " TEXT, "
                + COLUMN_NAME_CONTENT + " TEXT, "
                + COLUMN_NAME_DATE + " TEXT, "
                + COLUMN_NAME_TYPE + " TEXT, "
                + COLUMN_NAME_STATUS + " INTEGER,"
                + COLUMN_NAME_IMAGE + " TEXT)";
        db.execSQL(sql);

        String data = "INSERT INTO "+TABLE_TODO_NAME +
                " ("+COLUMN_NAME_ID +","+COLUMN_NAME_TITLE+","+COLUMN_NAME_CONTENT+ ","+COLUMN_NAME_DATE+","+COLUMN_NAME_TYPE+","+COLUMN_NAME_STATUS+","+COLUMN_NAME_IMAGE+ ") VALUES " +
                "(1, 'Học Java', 'Học Java cơ bản', '27/2/2023', 'Bình thường', 1,'')," +
                "(2, 'Học React Native', 'Học React Native cơ bản', '24/3/2023', 'Khó', 8,'')," +
                "(3, 'Học Kotlin', 'Học kotlin cơ bản', '1/4/2023', 'Dễ', 0,'')," +
                "(4, 'Học Python', 'Học Python', '5/11/2024', 'Dễ', 0,'')";

        db.execSQL(data);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion != newVersion) {
            db.execSQL("DROP TABLE IF EXISTS TODO");
            onCreate(db);
        }
    }
}
