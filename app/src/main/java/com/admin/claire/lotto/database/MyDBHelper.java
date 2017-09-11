package com.admin.claire.lotto.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by claire on 2017/9/3.
 */

public class MyDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "lotto.db";
    public static final int DATABASE_VERSION = 1;

    //資料庫物件
    private static SQLiteDatabase database;


    public MyDBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
        super(context, name, factory, version);
    }

    //需要在資料庫元件呼叫這個方法
    public static SQLiteDatabase getDatabase (Context context){
        if (database == null || !database.isOpen()){
            database = new MyDBHelper(context, DATABASE_NAME,null,
                    DATABASE_VERSION).getWritableDatabase();
        }

        return database;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LottoDAO.CREATE_TABLE_LOTTO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXITS " + LottoDAO.LOTTO_TABLE);
        onCreate(db);
    }
}
