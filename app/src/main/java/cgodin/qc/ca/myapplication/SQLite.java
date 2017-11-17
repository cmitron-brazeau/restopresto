package cgodin.qc.ca.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import cgodin.qc.ca.myapplication.user.User;

public class SQLite extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "RestoPresto.db";
    // Table Utilisateur
    public static final String UTIL_TABLE_NAME = "utilisateur";
    public static final String UTIL_COLUMN_ID = "id";
    public static final String UTIL_COLUMN_USERNAME = "userName";
    public static final String UTIL_COLUMN_PASSWORD = "password";
    public static final String UTIL_COLUMN_EMAIL = "email";
    public static final String UTIL_COLUMN_FACEBOOK_ID = "facebook_id";
    public static final String UTIL_COLUMN_GOOGLE_ID = "google_id";
    // Table Favoris
    public static final String FAV_TABLE_NAME = "favoris";
    public static final String FAV_COLUMN_USER_ID = "user_id";
    public static final String FAV_COLUMN_RESTO_ID = "resto_id";

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public void onCreate(SQLiteDatabase db) {
        // Table Utilisateur
        db.execSQL(
                "create table " + UTIL_TABLE_NAME + " (" +
                        UTIL_COLUMN_ID + " integer primary key, " +
                        UTIL_COLUMN_USERNAME + " text, " +
                        UTIL_COLUMN_PASSWORD + " text, " +
                        UTIL_COLUMN_EMAIL + " text, " +
                        UTIL_COLUMN_FACEBOOK_ID + " text, " +
                        UTIL_COLUMN_GOOGLE_ID + " text);"
        );
        // Table Favoris
        db.execSQL(
                "create table " + FAV_TABLE_NAME + " (" +
                        FAV_COLUMN_USER_ID + " integer, " +
                        FAV_COLUMN_RESTO_ID + " integer);"
        );
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UTIL_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + FAV_TABLE_NAME);
        onCreate(db);
    }

    // MÉTHODES DES UTILISATEURS
    public Long insertUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(UTIL_COLUMN_USERNAME, user.getUserName());
        contentValues.put(UTIL_COLUMN_PASSWORD, user.getPassword());
        contentValues.put(UTIL_COLUMN_EMAIL, user.getEmail());
        contentValues.put(UTIL_COLUMN_FACEBOOK_ID, user.getFacebookID());
        contentValues.put(UTIL_COLUMN_GOOGLE_ID, user.getGoogleID());
        Long retour = db.insert(UTIL_TABLE_NAME, null, contentValues);
        this.close();
        return retour;
    }

    public User getUser(Integer id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = UTIL_TABLE_NAME;
        String selection = UTIL_COLUMN_ID + "=?";
        String[] selectionargs = new String[]{id.toString()};
        Cursor crUser = db.query(table, null, selection, selectionargs, null, null, null);
        crUser.moveToFirst();
        int userId = crUser.getInt(crUser.getColumnIndex(UTIL_COLUMN_ID));
        String userName = crUser.getString(crUser.getColumnIndex(UTIL_COLUMN_USERNAME));
        String password = crUser.getString(crUser.getColumnIndex(UTIL_COLUMN_PASSWORD));
        String email = crUser.getString(crUser.getColumnIndex(UTIL_COLUMN_EMAIL));
        String facebookId = crUser.getString(crUser.getColumnIndex(UTIL_COLUMN_FACEBOOK_ID));
        String googleId = crUser.getString(crUser.getColumnIndex(UTIL_COLUMN_GOOGLE_ID));
        User user = new User(userId, userName, password, email, facebookId, googleId);
        crUser.close();
        this.close();
        return user;
    }

    public Integer deleteUser(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int retour = db.delete(UTIL_TABLE_NAME, UTIL_COLUMN_ID + " = ? ", new String[]{Integer.toString(id)});
        this.close();
        return retour;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> arrayUsers = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String table = UTIL_TABLE_NAME;
        String orderBy = UTIL_COLUMN_USERNAME + " collate localized";
        Cursor users = db.query(table, null, null, null, null, null, orderBy);
        while (users.moveToNext()){
            int id = users.getInt(users.getColumnIndex(UTIL_COLUMN_ID));
            String userName = users.getString(users.getColumnIndex(UTIL_COLUMN_USERNAME));
            String password = users.getString(users.getColumnIndex(UTIL_COLUMN_PASSWORD));
            String email = users.getString(users.getColumnIndex(UTIL_COLUMN_EMAIL));
            String facebookId = users.getString(users.getColumnIndex(UTIL_COLUMN_FACEBOOK_ID));
            String googleId = users.getString(users.getColumnIndex(UTIL_COLUMN_GOOGLE_ID));
            arrayUsers.add(new User(id, userName, password, email, facebookId, googleId));
        }
        users.close();
        this.close();
        return arrayUsers;
    }

    //TODO Méthodes des favoris pour les restaurants
}
