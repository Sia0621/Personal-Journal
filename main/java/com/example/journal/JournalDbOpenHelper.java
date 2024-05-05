package com.example.journal;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class JournalDbOpenHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "journal.db";// name of db
    public static final int DB_VERSION = 1;// version of db
    public static final String TABLE_JOURNAL = "journal"; //Table journal

    private static final String[] COLUMNS = { "id", "date", "weather", "title", "content", "address"};

    /**
     * Construct a SQLiteOpenHelper object for the
     * student database.
     */
    public JournalDbOpenHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //delete the table if exists
        String dropSql = "DROP TABLE IF EXISTS " + TABLE_JOURNAL + ";";
        db.execSQL(dropSql);

        //create the table
        String createSql = "CREATE TABLE IF NOT EXISTS " + TABLE_JOURNAL + " ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, "
                + "date INTEGER NOT NULL, "
                + "weather VARCHAR NOT NULL, "
                + "title VARCHAR NOT NULL, "
                + "content VARCHAR NOT NULL, "
                + "address VARCHAR NOT NULL "
                + ");";
        db.execSQL(createSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createJournal(Journal journal) {
        // get reference of the JournalDB database
        SQLiteDatabase db = this.getWritableDatabase();

        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put("date", journal.getDate().getTime());
        values.put("weather", journal.getWeather());
        values.put("title", journal.getTitle());
        values.put("content", journal.getContent());
        values.put("address", journal.getAddress());

        // insert journal
        db.insert(TABLE_JOURNAL, null, values);

        // close database transaction
        db.close();
    }

    public int updateJournal(int id, Journal journal) {

        // get reference of the JournalDB database
        SQLiteDatabase db = this.getWritableDatabase();

        // make values to be inserted
        ContentValues values = new ContentValues();
        values.put("date", journal.getDate().getTime());
        values.put("weather", journal.getWeather());
        values.put("title", journal.getTitle());
        values.put("content", journal.getContent());
        values.put("address", journal.getAddress());

        // update
        int i = db.update(TABLE_JOURNAL, values, "id" + " = ?", new String[] {"" + id});

        db.close();
        return i;
    }

    // Deleting single journal
    public void deleteJournal(int id) {

        // get reference of the journalDB database
        SQLiteDatabase db = this.getWritableDatabase();

        // delete journal
        db.delete(TABLE_JOURNAL, "id" + " = ?", new String[] {"" + id});
        db.close();
    }

    public Journal selectJournal(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_JOURNAL, // a. table
                COLUMNS, " id = ?", new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Journal journal = new Journal();
        journal.setId(Integer.parseInt(cursor.getString(0)));
        journal.setDate(new Date(cursor.getLong(1)));
        journal.setWeather(cursor.getString(2));
        journal.setTitle(cursor.getString(3));
        journal.setContent(cursor.getString(4));
        journal.setAddress(cursor.getString(5));

        return journal;
    }

    public List getAllJournals() {
        List journals = new LinkedList();

        // select journal query
        String query = "SELECT  * FROM " + TABLE_JOURNAL;

        // get reference of the JournalDB database
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        // parse all results
        Journal journal = null;
        if (cursor.moveToFirst()) {
            do {
                journal = new Journal();
                journal.setId(Integer.parseInt(cursor.getString(0)));
                journal.setDate(new Date(cursor.getLong(1)));
                journal.setWeather(cursor.getString(2));
                journal.setTitle(cursor.getString(3));
                journal.setContent(cursor.getString(4));
                journal.setAddress(cursor.getString(5));

                // Add journal to list
                journals.add(journal);
            } while (cursor.moveToNext());
        }
        return journals;
    }
}
