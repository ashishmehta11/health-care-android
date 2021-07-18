package com.project.healthcare.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.project.healthcare.data.Citizen;
import com.project.healthcare.data.HealthFacility;

import java.util.Arrays;

public class Database extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "healthcare.db";
    public final static String tblUser = "TblUser";
    private final static String TAG = "Database";
    private SQLiteDatabase db;

    public Database(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            String createTableExam =
                    "CREATE TABLE " + tblUser +
                            "(id INTEGER PRIMARY KEY," +
                            "name TEXT," +
                            "user_group TEXT," +
                            "user_name TEXT UNIQUE," +
                            "phone_number TEXT UNIQUE," +
                            "token TEXT UNIQUE)";

            db.execSQL(createTableExam);
        } catch (Exception e) {
            Log.d(TAG, "onCreate: message : " + e.toString());
            Log.d(TAG, "onCreate: stack trace : " + Arrays.toString(e.getStackTrace()));
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }


    public void deleteUser() {
        db = getWritableDatabase();
        try {
            long result = db.delete(tblUser, null, null);
            Log.d(TAG, "deleteUser: results :" + result);
        } catch (Exception e) {
            Log.d(TAG, "deleteUser: Exception : message : " + e.toString());
            Log.d(TAG, "deleteUser: Exception : stack trace : " + Arrays.toString(e.getStackTrace()));
        } finally {
            db.close();
        }
    }

    public void insertUser(Object user) {
        db = getWritableDatabase();
        Log.d(TAG, "insertUser: inside");
        try {
            ContentValues values = new ContentValues();
            if (user instanceof Citizen) {
                Citizen citizen = (Citizen) user;
                values.put("id", citizen.getId());
                values.put("name", citizen.getName());
                values.put("user_name", citizen.getUserName());
                values.put("phone_number", citizen.getPhoneNumber());
                values.put("token", citizen.getToken());
                values.put("user_group", "citizen");
            } else {
                HealthFacility facility = (HealthFacility) user;
                values.put("id", facility.getId());
                values.put("name", facility.getName());
                values.put("user_name", facility.getEmails().get(0));
                values.put("phone_number", facility.getPhoneNumbers().get(0));
                values.put("token", facility.getToken());
                values.put("user_group", "healthcare facility");
            }
            long result = db.insert(tblUser, null, values);
            Log.d(TAG, "insertUser: result :" + result);
        } catch (Exception e) {
            Log.d(TAG, "insertUser: Exception message : " + e.toString());
            Log.d(TAG, "insertUser: Exception stack trace : " + Arrays.toString(e.getStackTrace()));
        } finally {
            db.close();
        }
    }


    public Object getUser() {
        Citizen citizen = null;
        HealthFacility facility = null;
        Cursor cursor;
        db = getReadableDatabase();
        try {
            cursor = db.query(tblUser, null, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                String token, userGroup, phoneNumber, name, userName, id;
                name = cursor.getString(cursor.getColumnIndex("name"));
                id = cursor.getString(cursor.getColumnIndex("id"));
                token = cursor.getString(cursor.getColumnIndex("token"));
                userGroup = cursor.getString(cursor.getColumnIndex("user_group"));
                phoneNumber = cursor.getString(cursor.getColumnIndex("phone_number"));
                userName = cursor.getString(cursor.getColumnIndex("user_name"));
                if (userGroup.contains("citizen")) {
                    citizen = new Citizen();
                    citizen.setPhoneNumber(phoneNumber);
                    citizen.setUserName(userName);
                    citizen.setId(id);
                    citizen.setName(name);
                    citizen.setToken(token);
                    cursor.close();
                    db.close();
                    return citizen;
                } else {
                    facility = new HealthFacility();
                    facility.getPhoneNumbers().set(0, phoneNumber);
                    facility.getEmails().set(0, userName);
                    facility.setId(id);
                    facility.setName(name);
                    facility.setToken(token);
                    cursor.close();
                    db.close();
                    return facility;
                }
            }
            throw new Exception("Error");
        } catch (Exception e) {
            Log.d(TAG, "getUser: Exception : message : " + e.toString());
            Log.d(TAG, "getUser: Exception : stack trace : " + Arrays.toString(e.getStackTrace()));
        } finally {
            db.close();
        }
        return null;
    }

}
