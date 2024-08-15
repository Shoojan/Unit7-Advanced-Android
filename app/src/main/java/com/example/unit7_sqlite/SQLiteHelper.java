package com.example.unit7_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "subjectDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "subjects";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COLOR = "color"; // New column for color

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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
//            a. Insert data into the table (C)
//            b. Fetch  data from the table (R)
//            c. Update data into the table (U)
//            d. Delete data from the table (D)


    // Insert new subject
    public void addSubject(String name, int color) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_COLOR, color);

        db.insert(TABLE_NAME, null, values);
        db.close();

    }

    // Get all subjects
    public List<Subject> getAllSubjects() {
        String SELECT_QUERY = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(SELECT_QUERY, null);
        List<Subject> subjectList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COLUMN_ID));
                String subjectName = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                int color = cursor.getInt(cursor.getColumnIndex(COLUMN_COLOR));

                Subject subject = new Subject(id, subjectName, color);
                subjectList.add(subject);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return subjectList;
    }

    // Update subject
    public void updateSubject(int id, String name, int color) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, name);
        values.put(COLUMN_COLOR, color);

        String WHERE_CONDITION = COLUMN_ID + " = ?";
        String[] args = new String[]{String.valueOf(id)};

        db.update(TABLE_NAME, values, WHERE_CONDITION, args);
        db.close();
    }

    // Delete subject
    public void deleteSubject(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        String WHERE_CONDITION = COLUMN_ID + " = ?";
        String[] args = new String[]{String.valueOf(id)};

        db.delete(TABLE_NAME, WHERE_CONDITION, args);
        db.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_NAME + " TEXT,"
                + COLUMN_COLOR + " INTEGER"
                + ");";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
