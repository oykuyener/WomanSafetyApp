package com.example.iddetar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FeedReaderDbHelper extends SQLiteOpenHelper {
    private static final String veritabaniAdi="Koruma";
    private static final int Surum=1;
    public FeedReaderDbHelper(Context c){super(c,veritabaniAdi,null,Surum);}
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE kullanici(AD TEXT, SOYAD TEXT, TC TEXT, ADRES TEXT,ID INTEGER)");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS kullanici");
        onCreate(db);
    }
    public boolean insertData(String ad, String soyad, String tc, String adres,int id){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("AD",ad);
        contentValues.put("SOYAD",soyad);
        contentValues.put("TC",tc);
        contentValues.put("ADRES",adres);
        contentValues.put("ID",id);

        long result = db.insert("kullanici",null,contentValues);
        return !(result == -1);
    }
    public Cursor viewData(){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from kullanici",null);
        return cursor;
    }

    public boolean updateData(String name,String surname,String tc,String address){
        SQLiteDatabase db = this.getReadableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("AD",name);
        cv.put("SOYAD",surname);
        cv.put("TC",tc);
        cv.put("ADRES",address);
        long result = db.update("kullanici", cv,"ID=" + "1", null);
        return !(result == -1);
    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from kullanici");
    }
}
