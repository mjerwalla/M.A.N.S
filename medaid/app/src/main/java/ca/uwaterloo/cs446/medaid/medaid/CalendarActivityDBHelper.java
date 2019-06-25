package ca.uwaterloo.cs446.medaid.medaid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CalendarActivityDBHelper extends SQLiteOpenHelper{
    public static final String DATABASE_NAME = "MedAid.db";
    public static final String TABLE_NAME = "Medication";
    public static final String COL_1 = "uuid";
    public static final String COL_2 = "medName";
    public static final String COL_3 = "timesOfDay";
    public static final String COL_4 = "daysPerWeek";
    public static final String COL_5 = "startDate";
    public static final String COL_6 = "endDate";
    public static final String COL_7 = "dailyNumPills";
    public static final String COL_8 = "totalNumPills";
    public static final String COL_9 = "notes";



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (uuid INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, medName TEXT NOT NULL, timesOfDay TEXT NOT NULL, daysPerWeek TEXT NOT NULL, startDate INTEGER NOT NULL, endDate INTEGER, dailyNumPills INTEGER NOT NULL, totalNumPills INTEGER, notes TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public CalendarActivityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public boolean insertData(String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }

    public Cursor getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        return res;
    }

    public boolean updateData(String id,String name,String surname,String marks) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,id);
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,surname);
        contentValues.put(COL_4,marks);
        db.update(TABLE_NAME, contentValues, "ID = ?",new String[] { id });
        return true;
    }

    public Integer deleteData (String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, "ID = ?",new String[] {id});
    }
}
