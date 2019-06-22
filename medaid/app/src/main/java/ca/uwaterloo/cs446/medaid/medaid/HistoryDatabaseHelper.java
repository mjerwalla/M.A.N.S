package ca.uwaterloo.cs446.medaid.medaid;

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
    public static final String COL_3 = "numTimesPerWeek";
    public static final String COL_4 = "numTimesPerDay";
    public static final String COL_5 = "takenWith";

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
        sqLiteDatabase.execSQL("create table " + TABLE_NAME + "(uuid INTEGER PRIMARY KEY, medName TEXT, numTimesPerWeek TEXT, numTimesPerDay TEXT, takenWith TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
