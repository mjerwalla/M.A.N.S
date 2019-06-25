package ca.uwaterloo.cs446.medaid.medaid;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;
import android.util.Log;

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

//    String DB_PATH = null;
//    private static String DATABASE_NAME = "Medication.db";
//    private SQLiteDatabase medDB;
//    private final context myContext;


    public HistoryDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
//        this.myContext = context;
//        this.DB_PATH = "/data/data" + context.getPackageName() + "/" + "databases";
//        Log.e("Path 1", DB_PATH);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(uuid INTEGER NOT NULL PRIMARY KEY, medName TEXT NOT NULL, timesOfDay TEXT NOT NULL, daysPerWeek TEXT NOT NULL, startDate INTEGER NOT NULL, endDate INTEGER, dailyNumPills INTEGER NOT NULL, totalNumPills INTEGER, notes TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean insertData(int uuid, String medName, String timesOfDay,
                              String daysPerWeek, String startDate, String endDate,
                              int dailyNumPills, int totalNumPills, String notes) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COL_1, uuid);
        cv.put(COL_2, medName);
        cv.put(COL_3, timesOfDay);
        cv.put(COL_4, daysPerWeek);
        cv.put(COL_5, startDate);
        cv.put(COL_6, endDate);
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
}
