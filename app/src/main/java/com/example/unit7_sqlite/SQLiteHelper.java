package com.example.unit7_sqlite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {

    public SQLiteHelper(Context context) {
        super(context, DatabaseConfig.DATABASE_NAME, null, DatabaseConfig.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_TABLE = "CREATE TABLE " + DatabaseConfig.TABLE_NAME + " ("
                + DatabaseConfig.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + DatabaseConfig.TABLE_NAME + " TEXT,"
                + DatabaseConfig.COLUMN_COLOR + " INTEGER"
                + ");";

        sqLiteDatabase.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DatabaseConfig.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void addSubject(String name, int color){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(DatabaseConfig.COLUMN_NAME, name);
        contentValues.put(DatabaseConfig.COLUMN_COLOR, color);

        db.insert(DatabaseConfig.TABLE_NAME, null, contentValues);
        db.close();
    }

    public List<Subject> getAllSubjects(){
        List<Subject> subjectList = new ArrayList<>();

        String SELECT_QUERY = "SELECT * FROM "+ DatabaseConfig.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);

        if(cursor.moveToFirst()){
            do{
                Subject subject = new Subject(
                        cursor.getInt(cursor.getColumnIndex(DatabaseConfig.COLUMN_ID)),
                        cursor.getString(cursor.getColumnIndex(DatabaseConfig.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(DatabaseConfig.COLUMN_COLOR))
                );
                subjectList.add(subject);
            }while (cursor.moveToNext());
        }

        return subjectList;
    }

//      STEPS:
//      ------
//        1.  CREATE TABLE tableName (
//                id INTEGER PRIMARY KEY AUTOINCREMENT,
//                name TEXT,
//                color INTEGER
//            );
//
//        2. Drop if version upgraded
//
//        3. Handle CRUD Operation
//            a. Insert data into the table
//            b. Fetch  data from the table
//            c. Update data into the table
//            d. Delete data from the table

}
