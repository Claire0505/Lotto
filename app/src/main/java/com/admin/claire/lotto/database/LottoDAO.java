package com.admin.claire.lotto.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.admin.claire.lotto.model.Betting;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by claire on 2017/9/3.
 */

public class LottoDAO {

    public static final String LOTTO_TABLE = "lotto";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_BETTING_NUMBER = "betting_num";
    public static final String COLUMN_CREATED_TIME = "created_time";

    public static final String CREATE_TABLE_LOTTO = "CREATE TABLE "
            + LOTTO_TABLE
            + "("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_BETTING_NUMBER + " text not null, "
            + COLUMN_CREATED_TIME + " integer not null "
            + ")";

    //資料庫物件
    private SQLiteDatabase db;

    public LottoDAO(Context context) {
        db = MyDBHelper.getDatabase(context);
    }

    public void Close(){
        db.close();
    }

    //insert
    public Betting insertDB (Betting betting ){
        ContentValues values = new ContentValues();
        values.put(COLUMN_BETTING_NUMBER, betting.getBettingNum());
        values.put(COLUMN_CREATED_TIME, betting.getDateCreated());
        long id = db.insert(LOTTO_TABLE, null, values);
        betting.setId(id);

        return  betting;
    }

    //update
    public void updateDB (Betting betting){

        ContentValues values = new ContentValues();
        values.put(COLUMN_BETTING_NUMBER, betting.getBettingNum());
        values.put(COLUMN_CREATED_TIME, betting.getDateCreated());

        String where = COLUMN_ID + "=" + betting.getId();
        db.update(LOTTO_TABLE, values, where, null);
       // return db.update(LOTTO_TABLE, values, where, null) > 0;
    }

    //delete
    public boolean deletedDB (long id){

        String where = COLUMN_ID + "=" + id;
        return db.delete(LOTTO_TABLE, where, null) > 0;
    }

    //getALL
    public List<Betting> getAll(){
        List<Betting> result = new ArrayList<>();
        String sorting = COLUMN_ID + " DESC";
        Cursor cursor = db.query(LOTTO_TABLE,null,null,null,null,null,sorting,null);

        while (cursor.moveToNext()){
            result.add(getRecord(cursor));
        }
        cursor.close();
        return result;
    }

    //取得指定編號物件
    public Betting get(long id){
        Betting betting = null;
        String where = COLUMN_ID + "=" + id;

        Cursor result = db.query(LOTTO_TABLE,null,where,null,null,null,null,null);
        //如果有查詢結果
        if (result.moveToFirst()){
            betting = getRecord(result);
        }
        result.close();
        return betting;
    }

    //把Cursor 目前的資料包裝成物件
    public Betting getRecord(Cursor cursor) {
        Betting result = new Betting();
        result.setId(cursor.getLong(0));
        result.setBettingNum(cursor.getString(1));
        result.setDateCreated(cursor.getLong(2));

        return result;
    }

    //取得數量
    public  int getCount(){
        int result = 0;

        Cursor cursor = db.rawQuery("Select Count(*) From " + LOTTO_TABLE, null);
        if (cursor.moveToNext()){
            result = cursor.getInt(0);
        }

        return result;
    }

    public void sample(){
        Betting betting = new Betting(0,"12 20 30 40 45 49",new Date().getTime());

        insertDB(betting);
    }

}
