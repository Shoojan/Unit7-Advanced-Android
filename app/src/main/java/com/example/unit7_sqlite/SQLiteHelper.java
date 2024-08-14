package com.example.unit7_sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteHelper {
    private static final String DATABASE_NAME = "subjectDB";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "subjects";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_COLOR = "color"; // New column for color

    public SQLiteHelper(Context context) {
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


    // Insert new subject
    public void addSubject(String name, int color) {

    }

    // Get all subjects
    public List<Subject> getAllSubjects() {
        return new ArrayList<>();
    }

    // Update subject
    public void updateSubject(int id, String name, int color) {

    }

    // Delete subject
    public void deleteSubject(int id) {

    }

}
