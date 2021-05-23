package com.example.iddetar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String veritabaniAdi="Konumlar";
    private static final int Surum=1;
    public DbHelper(Context c){super(c,veritabaniAdi,null,Surum);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE girdi(AD TEXT, SOYAD TEXT, KONUM TEXT)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS girdi");
        onCreate(db);
    }
    public boolean insertData(String ad, String soyad, String konum){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AD",ad);
        contentValues.put("SOYAD",soyad);
        contentValues.put("KONUM",konum);

        long result = db.insert("girdi",null,contentValues);
        return !(result == -1);
    }
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from girdi",null);
        return cursor;
    }

}
