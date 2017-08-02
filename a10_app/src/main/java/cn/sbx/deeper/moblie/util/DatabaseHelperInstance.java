package cn.sbx.deeper.moblie.util;

import android.content.Context;

import net.sqlcipher.database.SQLiteDatabase;

import cn.sbx.deeper.moblie.db.DatabaseHelper;

/**
 * Created by caomang on 2017/2/14.
 */

public class DatabaseHelperInstance {
    public   DatabaseHelper   helper =null;
    private   SQLiteDatabase instance =null;


    public  SQLiteDatabase getInstance(Context context) {
            helper = new DatabaseHelper(context);
            instance = helper.getWritableDatabase();

        return instance;
    }




}
