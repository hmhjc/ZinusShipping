package cn.zinus.warehouse.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**主要是对内置数据库(SQLite)操作的工具类
 * Created by Spring on 2017/4/17.
 */

public class DBManger {
    private static MyDateBaseHelper helper;
    public static MyDateBaseHelper getIntance(Context context){
        if (helper==null){
            helper = new MyDateBaseHelper(context);
        }
        return helper;
    }

    /**
     *根据sql语句在数据库中执行语句
     * @param db  数据库对象
     * @param sql sql语句
     */
 public static void execSQL(SQLiteDatabase db,String sql){
    if (db!=null){
        if (sql!=null&&!"".equals(sql)){
            db.execSQL(sql);
        }
    }
 }

    /**
     * 根据sql语句查询获得Cursor对象
     * @param db    数据库对象
     * @param sql   查询sql语句
     * @param selectionArgs 查询条件的占位符
     * @return 查询结果
     */
 public static Cursor selectDatBySql(SQLiteDatabase db, String sql,String[] selectionArgs){
     Cursor cursor = null;
     if (db!=null){
             cursor = db.rawQuery(sql,selectionArgs);
        }
        return cursor;
    }
    public static  String getCursorData(Cursor cursorDatalist, String ColumnIndex) {
        String returnstr = "";
        String a = cursorDatalist.getString(cursorDatalist.getColumnIndex(ColumnIndex));
        if (a != null) {
            returnstr = a;
        }
        return returnstr;
    }

}