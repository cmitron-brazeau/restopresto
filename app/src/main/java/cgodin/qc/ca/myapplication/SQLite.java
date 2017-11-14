package cgodin.qc.ca.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.lang.reflect.Array;
import java.util.ArrayList;

import cgodin.qc.ca.myapplication.personne.Personne;

public class SQLite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Projet1.db";
    public static final String PERSON_TABLE_NAME = "personne";
    public static final String PERSON_COLUMN_ID = "id";
    public static final String PERSON_COLUMN_FIRSTNAME = "firstName";
    public static final String PERSON_COLUMN_PASSWORD = "password";
    public static final String PERSON_COLUMN_EMAIL = "email";

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table " + PERSON_TABLE_NAME + " (" +
                        PERSON_COLUMN_ID + " integer primary key, " +
                        PERSON_COLUMN_FIRSTNAME + " text, " +
                        PERSON_COLUMN_PASSWORD + " text, " +
                        PERSON_COLUMN_EMAIL + " text);"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS person");
        onCreate(db);
    }

    public boolean insertPersonne(String firstName, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_FIRSTNAME, firstName);
        contentValues.put(PERSON_COLUMN_PASSWORD, password);
        contentValues.put(PERSON_COLUMN_EMAIL, email);
        db.insert(PERSON_TABLE_NAME, null, contentValues);
        return true;
    }

    public Cursor getPersonne(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = PERSON_TABLE_NAME;
        String selection = PERSON_COLUMN_ID + "=?";
        String[] selectionargs = new String[]{id.toString()};
        Cursor personne = db.query(table, null, selection, selectionargs, null, null, null);
        return personne;
    }

    public boolean updatePersonne(Integer id, String firstName, String password, String email) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PERSON_COLUMN_FIRSTNAME, firstName);
        contentValues.put(PERSON_COLUMN_PASSWORD, password);
        contentValues.put(PERSON_COLUMN_EMAIL, email);
        db.update(PERSON_TABLE_NAME, contentValues, PERSON_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        return true;
    }

    public Integer deletePersonne(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(PERSON_TABLE_NAME, PERSON_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
    }

    public ArrayList<Personne> getAllPersonnes() {
        ArrayList<Personne> arrayPersons = new ArrayList<Personne>();

        SQLiteDatabase db = this.getReadableDatabase();
        String table = PERSON_TABLE_NAME;
        String orderBy = PERSON_COLUMN_FIRSTNAME + " collate localized";
        Cursor persons = db.query(table, null, null, null, null, null, orderBy);
        while (persons.moveToNext()){
            int id = persons.getInt(persons.getColumnIndex(PERSON_COLUMN_ID));
            String firstName = persons.getString(persons.getColumnIndex(PERSON_COLUMN_FIRSTNAME));
            String password = persons.getString(persons.getColumnIndex(PERSON_COLUMN_PASSWORD));
            String email = persons.getString(persons.getColumnIndex(PERSON_COLUMN_EMAIL));
            arrayPersons.add(new Personne(id, firstName, password, email));
        }
        persons.close();
        return arrayPersons;
    }
}
