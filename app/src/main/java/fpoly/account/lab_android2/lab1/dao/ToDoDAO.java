package fpoly.account.lab_android2.lab1.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import fpoly.account.lab_android2.lab1.helper.DbHelper;
import fpoly.account.lab_android2.lab1.model.ToDo;

public class ToDoDAO {
    private SQLiteDatabase db;
    private DbHelper dbHelper;

    public ToDoDAO(Context context) {
        dbHelper = new DbHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public ArrayList<ToDo> getListToDo() {
        ArrayList<ToDo> lists = new ArrayList<>();
        db = dbHelper.getReadableDatabase();

        Cursor cursor = null;
        try {
            cursor = db.rawQuery("SELECT * FROM TODO", null);
            if (cursor.moveToFirst()) {
                do {
                    ToDo toDo = new ToDo();
                    toDo.setId(cursor.getInt(0));
                    toDo.setTitle(cursor.getString(1));
                    toDo.setContent(cursor.getString(2));
                    toDo.setDate(cursor.getString(3));
                    toDo.setType(cursor.getString(4));
                    toDo.setStatus(cursor.getInt(5));
                    lists.add(toDo);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return lists;
    }

    public boolean addToDo(ToDo toDo) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("TITLE", toDo.getTitle());
            contentValues.put("CONTENT", toDo.getContent());
            contentValues.put("DATE", toDo.getDate());
            contentValues.put("TYPE", toDo.getType());
            contentValues.put("STATUS", toDo.getStatus());

            long check = sqLiteDatabase.insert("TODO", null, contentValues);
            if (check != -1) {
                sqLiteDatabase.setTransactionSuccessful();
            }
            return check != -1;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public boolean updateToDo(ToDo toDo) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            ContentValues contentValues = new ContentValues();
            contentValues.put("TITLE", toDo.getTitle());
            contentValues.put("CONTENT", toDo.getContent());
            contentValues.put("DATE", toDo.getDate());
            contentValues.put("TYPE", toDo.getType());
            contentValues.put("STATUS", toDo.getStatus());

            int rows = sqLiteDatabase.update("TODO", contentValues, "ID = ?", new String[]{String.valueOf(toDo.getId())});
            if (rows > 0) {
                sqLiteDatabase.setTransactionSuccessful();
            }
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

    public boolean deleteToDo(int id) {
        SQLiteDatabase sqLiteDatabase = dbHelper.getWritableDatabase();
        sqLiteDatabase.beginTransaction();
        try {
            int rows = sqLiteDatabase.delete("TODO", "ID = ?", new String[]{String.valueOf(id)});
            if (rows > 0) {
                sqLiteDatabase.setTransactionSuccessful();
            }
            return rows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            sqLiteDatabase.endTransaction();
        }
    }

}
