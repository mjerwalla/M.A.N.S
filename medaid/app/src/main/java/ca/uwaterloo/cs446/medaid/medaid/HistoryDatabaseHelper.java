package ca.uwaterloo.cs446.medaid.medaid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HistoryDatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "Medication.db";
    public static final String TABLE_NAME = "medication_table";
    public static final String COL_1 = "uuid";
    public static final String COL_2 = "medName";
    public static final String COL_3 = "timesOfDay";
    public static final String COL_4 = "daysPerWeek";
    public static final String COL_5 = "startDate";
    public static final String COL_6 = "endDate";
    public static final String COL_7 = "dailyNumPills";
    public static final String COL_8 = "totalNumPills";
    public static final String COL_9 = "notes";

    SQLiteDatabase medDB;


    public HistoryDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 2);
        medDB = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + " (uuid INTEGER NOT NULL PRIMARY KEY, medName TEXT NOT NULL, timesOfDay TEXT NOT NULL, daysPerWeek TEXT NOT NULL, startDate TEXT NOT NULL, endDate TEXT, dailyNumPills INTEGER NOT NULL, totalNumPills INTEGER, notes TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(int uuid, String medName, String timesOfDay,
                              String daysPerWeek, Date startDate, Date endDate,
                              int dailyNumPills, int totalNumPills, String notes) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm");
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, uuid);
        cv.put(COL_2, medName);
        cv.put(COL_3, timesOfDay);
        cv.put(COL_4, daysPerWeek);
        cv.put(COL_5, sdf.format(startDate));
        cv.put(COL_6, sdf.format(endDate));
        cv.put(COL_7, dailyNumPills);
        cv.put(COL_8, totalNumPills);
        cv.put(COL_9, notes);

        long result = db.insert(TABLE_NAME, null, cv);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getMedication() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("select * from "+TABLE_NAME,null);
        return result;
    }
}
