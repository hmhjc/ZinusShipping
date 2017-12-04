package cn.zinus.warehouse.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**1.提供了onCreate() onUpgrade()等创建数据库更新的方法
 * Created by Spring on 2017/4/17.
 */

public class MyDateBaseHelper extends SQLiteOpenHelper {


    /**
     *
     * @param context 上下文对象
     * @param name    表示创建数据库的名称
     * @param factory 游标工厂
     * @param version 表示创建数据库的版本(>=1)
     */
    public MyDateBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public MyDateBaseHelper(Context context){
        super(context, Constant.DATEBASE_NAME, null, Constant.DATEBASE_VERSION);
    }

    /**
     * 第一次创建数据库的时候回调的方法
     * getWritableDatabase()或者getReadableDatabase()调用的时候才会进行这个回调
     * 如果数据库不存在,就会调用,存在就是打开数据库
     * @param db 数据库对象
     */
    @Override
    public void onCreate(SQLiteDatabase db) {

    }



    /**
     * 当数据库版本跟新时回调的函数
     * @param db    数据库对象
     * @param oldVersion 数据库旧版本
     * @param newVersion 数据库新版本
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.e("tag","-------OnUpdate--------");
//        String sqldatalist = "CREATE TABLE "+ "sf_test"+"\n" +
//                "(\n" +
//                Constant.CODEID+" nvarchar(40),\n" +
//                Constant.CODECLASSID+" nvarchar(40),\n" +
//                Constant.DICTIONARYNAME+" nvarchar(40),\n" +
//                Constant.LANGUAGETYPE+" nvarchar(40),\n" +
//                "primary key("+Constant.CODEID+","+Constant.LANGUAGETYPE+")"+
//                ")"
//                ;
//        Log.e("create语句",sqldatalist);
//        db.execSQL(sqldatalist);
    }

    private void createTableSF_CODE(SQLiteDatabase db) {
        String sqldatalist = "CREATE TABLE "+ Constant.SF_CODE+"\n" +
                "(\n" +
                Constant.CODEID+" nvarchar(40),\n" +
                Constant.CODECLASSID+" nvarchar(40),\n" +
                Constant.DICTIONARYNAME+" nvarchar(40),\n" +
                Constant.LANGUAGETYPE+" nvarchar(40),\n" +
                "primary key("+Constant.CODEID+","+Constant.LANGUAGETYPE+")"+
                ")"
                ;
        Log.e("create语句",sqldatalist);
        db.execSQL(sqldatalist);
    }

    private void creaateTableSF_INBOUNDORDER(SQLiteDatabase db) {
        String sqldatalist = "CREATE TABLE "+ Constant.SF_INBOUNDORDER+"\n" +
                "(\n" +
                Constant.INBOUNDNO+" nvarchar(40),\n" +
                Constant.WAREHOUSEID+" nvarchar(40),\n" +
                Constant.INBOUNDDATE+" nvarchar(40),\n" +
                Constant.INBOUNDSTATE+" nvarchar(40),\n" +
                Constant.INSPECTIONRESULT+" nvarchar(40),\n" +
                Constant.VALIDSTATE+" nvarchar(40),\n" +
                Constant.URGENCYTYPE+" nvarchar(40),\n" +
                Constant.SCHEDULEDATE+" nvarchar(40),\n" +
                Constant.TEMPINBOUNDDATE+" nvarchar(40),\n" +
                "primary key("+Constant.INBOUNDNO+")"+
                ")"
                ;
        Log.e("create语句",sqldatalist);
        db.execSQL(sqldatalist);
    }
}
