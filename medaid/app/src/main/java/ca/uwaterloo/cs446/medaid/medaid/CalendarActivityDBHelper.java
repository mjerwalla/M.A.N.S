package ca.uwaterloo.cs446.medaid.medaid;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.sql.Array;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import android.icu.text.SimpleDateFormat;



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


    public class MyData {
        String column0;
        String column1;
        String column2;
        String column3;
        String column4;
        String column5;
        String column6;
        String column7;
        String column8;
        String column9;


        public MyData(String column0, String column1, String column2, String column3,String column4,
                      String column5, String column6, String column7,String column8,String column9){
            this.column0 = column0;
            this.column1 = column1;
            this.column2 = column2;
            this.column3 = column3;
            this.column4 = column4;
            this.column5 = column5;
            this.column6 = column6;
            this.column7 = column7;
            this.column8 = column8;
            this.column9 = column9;
        }

        public ArrayList<String> getContent(){
            ArrayList<String> list = new ArrayList<>();
            list.add(this.column0);
            list.add(this.column1);
            list.add(this.column2);
            list.add(this.column3);
            list.add(this.column4);
            list.add(this.column5);
            list.add(this.column6);
            list.add(this.column7);
            list.add(this.column8);
            list.add(this.column9);
            return list;
        }
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        //db.execSQL("create table " + "Users" + " (username TEXT PRIMARY KEY ,password TEXT NOT NULL, firstName TEXT NOT NULL, lastName TEXT NOT NULL, uuid INTEGER NOT NULL)");
        db.execSQL("create table " + TABLE_NAME + " (num INTEGER PRIMARY KEY AUTOINCREMENT,uuid INTEGER NOT NULL, medName TEXT NOT NULL, timesOfDay TEXT NOT NULL, daysPerWeek TEXT NOT NULL, startDate TEXT NOT NULL, endDate TEXT, dailyNumPills INTEGER NOT NULL, totalNumPills INTEGER, notes TEXT, FOREIGN KEY(uuid) REFERENCES Users(uuid))");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }
    public CalendarActivityDBHelper(Context context) {
        super(context, DATABASE_NAME, null, 3);
    }


    public boolean insertMedicationData(int uuid, String medName, String timesOfDay,
                              String daysPerWeek, Date startDate, Date endDate,
                              int dailyNumPills, int totalNumPills, String notes) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:MM");

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_1,uuid);
        contentValues.put(COL_2,medName);
        contentValues.put(COL_3,timesOfDay);
        contentValues.put(COL_4,daysPerWeek);
        contentValues.put(COL_5,sdf.format(startDate));
        contentValues.put(COL_6,sdf.format(endDate));
        contentValues.put(COL_7,dailyNumPills);
        contentValues.put(COL_8,totalNumPills);
        contentValues.put(COL_9,notes);
        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }




    public List<MyData> getAllData() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME,null);
        List<MyData> list = new ArrayList<>();
        while(res.moveToNext()) {
            String column0 = res.getString(res.getColumnIndex("num"));
            String column1 = res.getString(res.getColumnIndex(COL_1));
            String column2 = res.getString(res.getColumnIndex(COL_2));
            String column3 = res.getString(res.getColumnIndex(COL_3));
            String column4 = res.getString(res.getColumnIndex(COL_4));
            String column5 = res.getString(res.getColumnIndex(COL_5));
            String column6 = res.getString(res.getColumnIndex(COL_6));
            String column7 = res.getString(res.getColumnIndex(COL_7));
            String column8 = res.getString(res.getColumnIndex(COL_8));
            String column9 = res.getString(res.getColumnIndex(COL_9));

            MyData data = new MyData(column0, column1,column2,column3,column4,column5,
                    column6,column7,column8,column9);
            list.add(data);
        }
        System.out.println("The Length of the list is:");
        System.out.println(list.size());
        for(MyData myData : list) {
            System.out.println(myData.column0);

        }
        return list;
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
        return db.delete(TABLE_NAME, "uuid = ?",new String[] {id});
    }
}
