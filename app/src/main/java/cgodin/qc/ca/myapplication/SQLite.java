package cgodin.qc.ca.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.util.Log;

import java.util.ArrayList;

import cgodin.qc.ca.myapplication.restaurant.DbBitmapUtility;
import cgodin.qc.ca.myapplication.restaurant.Location;
import cgodin.qc.ca.myapplication.restaurant.OpenHours;
import cgodin.qc.ca.myapplication.restaurant.Restaurant;
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
    public static final String FAV_COLUMN_RESTO_PLACEID = "resto_id";
    public static final String FAV_COLUMN_RESTO_NAME = "resto_name";
    public static final String FAV_COLUMN_RESTO_ADDRESS = "resto_address";
    public static final String FAV_COLUMN_RESTO_RATING = "resto_rating";
    public static final String FAV_COLUMN_RESTO_PHOTO = "resto_photo";
    public static final String FAV_COLUMN_RESTO_LOCATION = "resto_location";
    public static final String FAV_COLUMN_RESTO_PHONE = "resto_phone";
    public static final String FAV_COLUMN_RESTO_OPEN_HOURS = "resto_open_hours";
    public static final String FAV_COLUMN_RESTO_WEBSITE = "resto_website";



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
                        FAV_COLUMN_RESTO_PLACEID + " text, " +
                        FAV_COLUMN_RESTO_NAME + " text, " +
                        FAV_COLUMN_RESTO_ADDRESS + " text, " +
                        FAV_COLUMN_RESTO_RATING + " float, " +
                        FAV_COLUMN_RESTO_PHOTO + " BLOB, " +
                        FAV_COLUMN_RESTO_LOCATION + " text, " +
                        FAV_COLUMN_RESTO_PHONE + " text, " +
                        FAV_COLUMN_RESTO_OPEN_HOURS + " text, " +
                        FAV_COLUMN_RESTO_WEBSITE + " text);"
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

    // MÉTHODES DES FAVORIS
    public Long insertFavoritePlace(int userId, Restaurant restaurant) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(FAV_COLUMN_USER_ID, userId);
        contentValues.put(FAV_COLUMN_RESTO_PLACEID, restaurant.getPlaceId());
        contentValues.put(FAV_COLUMN_RESTO_NAME, restaurant.getName());
        contentValues.put(FAV_COLUMN_RESTO_ADDRESS, restaurant.getAddress());
        contentValues.put(FAV_COLUMN_RESTO_RATING, restaurant.getRating());
        byte[] image = DbBitmapUtility.getBytes(restaurant.getPhoto());
        contentValues.put(FAV_COLUMN_RESTO_PHOTO, image);
        contentValues.put(FAV_COLUMN_RESTO_LOCATION, String.valueOf(restaurant.getLocation().getLatitude()) + "/" +
                String.valueOf(restaurant.getLocation().getLongitude()));
        contentValues.put(FAV_COLUMN_RESTO_PHONE, restaurant.getPhoneNumber());
        OpenHours opnHrs = restaurant.getOpenHours();
        contentValues.put(FAV_COLUMN_RESTO_OPEN_HOURS, opnHrs.getMonday() + "/" + opnHrs.getTuesday() + "/" + opnHrs.getWednesday() + "/" +
                opnHrs.getThursday() + "/" + opnHrs.getFriday() + "/" + opnHrs.getSaturday() + "/" + opnHrs.getSunday());
        contentValues.put(FAV_COLUMN_RESTO_WEBSITE, restaurant.getWebsite());
        Long retour = db.insert(FAV_TABLE_NAME, null, contentValues);
        this.close();
        return retour;
    }

    public Restaurant getFavoritePlace(Integer userId, String placeId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String table = FAV_TABLE_NAME;
        String selection = FAV_COLUMN_USER_ID + "=? AND " + FAV_COLUMN_RESTO_PLACEID + "=?";
        String[] selectionargs = new String[]{userId.toString(), placeId};
        Cursor crFav = db.query(table, null, selection, selectionargs, null, null, null);
        crFav.moveToFirst();
        String restoId = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_PLACEID));
        String name = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_NAME));
        String address = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_ADDRESS));
        Float rating = crFav.getFloat(crFav.getColumnIndex(FAV_COLUMN_RESTO_RATING));
        byte[] image = crFav.getBlob(crFav.getColumnIndex(FAV_COLUMN_RESTO_PHOTO));
        Bitmap photo = DbBitmapUtility.getImage(image);
        String location = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_LOCATION));
        String[] arrayLocation = location.split("/");
        Location restoLocation = new Location(Double.parseDouble(arrayLocation[0]), Double.parseDouble(arrayLocation[1]));
        String phone = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_PHONE));
        String opnHrs = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_OPEN_HOURS));
        String[] arrayOpnHrs = opnHrs.split("/");
        OpenHours openHours = new OpenHours(arrayOpnHrs[0],arrayOpnHrs[1],arrayOpnHrs[2],arrayOpnHrs[3],arrayOpnHrs[4],arrayOpnHrs[5],
                arrayOpnHrs[6]);
        String website = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_WEBSITE));
        Restaurant resto = new Restaurant(photo, restoId, restoLocation, name, address, phone, openHours, rating, website);
        crFav.close();
        this.close();
        return resto;
    }

    public Integer deleteFavoritePlace(String placeId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int retour = db.delete(FAV_TABLE_NAME, FAV_COLUMN_RESTO_PLACEID + " = ? ", new String[]{placeId});
        this.close();
        return retour;
    }

    public ArrayList<Restaurant> getAllFavoritePlaces() {
        ArrayList<Restaurant> arrayRestaurants = new ArrayList<>();

        SQLiteDatabase db = this.getReadableDatabase();
        String table = FAV_TABLE_NAME;
        Cursor crFav = db.query(table, null, null, null, null, null, null);
        while (crFav.moveToNext()){
            String restoId = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_PLACEID));
            String name = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_NAME));
            String address = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_ADDRESS));
            Float rating = crFav.getFloat(crFav.getColumnIndex(FAV_COLUMN_RESTO_RATING));
            byte[] image = crFav.getBlob(crFav.getColumnIndex(FAV_COLUMN_RESTO_PHOTO));
            Bitmap photo = DbBitmapUtility.getImage(image);
            String location = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_LOCATION));
            String[] arrayLocation = location.split("/");
            Location restoLocation = new Location(Double.parseDouble(arrayLocation[0].toString()), Double.parseDouble(arrayLocation[1].toString()));
            String phone = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_PHONE));
            String opnHrs = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_OPEN_HOURS));
            String[] arrayOpnHrs = opnHrs.split("/");
            OpenHours openHours = new OpenHours(arrayOpnHrs[0],arrayOpnHrs[1],arrayOpnHrs[2],arrayOpnHrs[3],arrayOpnHrs[4],arrayOpnHrs[5],
                    arrayOpnHrs[6]);
            String website = crFav.getString(crFav.getColumnIndex(FAV_COLUMN_RESTO_WEBSITE));

            arrayRestaurants.add(new Restaurant(photo, restoId, restoLocation, name, address, phone, openHours, rating, website));
        }
        crFav.close();
        this.close();
        return arrayRestaurants;
    }
}
