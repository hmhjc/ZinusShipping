package cn.zinus.warehouse.SocketConnect;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.zinus.warehouse.JaveBean.AreaData;
import cn.zinus.warehouse.JaveBean.CodeData;
import cn.zinus.warehouse.JaveBean.ConsumableLotData;
import cn.zinus.warehouse.JaveBean.ConsumabledefinitionData;
import cn.zinus.warehouse.JaveBean.ConsumeStockData;
import cn.zinus.warehouse.JaveBean.WareHouseData;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.Constant.AREAID;
import static cn.zinus.warehouse.util.Constant.CONSUMABLEDEFID;
import static cn.zinus.warehouse.util.Constant.CONSUMABLEDEFVERSION;
import static cn.zinus.warehouse.util.Constant.CONSUMABLELOTID;
import static cn.zinus.warehouse.util.Constant.PLANTID;
import static cn.zinus.warehouse.util.Constant.QTY;
import static cn.zinus.warehouse.util.Constant.VALIDSTATE;
import static cn.zinus.warehouse.util.Constant.WAREHOUSEID;

/**
 * Developer:Spring
 * DataTime :2017/9/15 09:20
 * Main Change:
 */

public class UpdateSqlite {
    private MyDateBaseHelper mHelper;
    private SQLiteDatabase db;

    public UpdateSqlite(Context mContext) {
        mHelper = DBManger.getIntance(mContext);
        db = mHelper.getWritableDatabase();
    }

    //region 入库表更新
    //region Table_InboundOrder

    public void updateInboundOrder(String resultStr) {
        Log.e("更新本地数据库InboundOrder", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertSQL = String.format(Constant.InsertIntoINBOUNDORDER
                        , jsonObject.getString(Constant.INBOUNDNO)
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.WAREHOUSENAME)
                        , jsonObject.getString(Constant.SCHEDULEDATE)
                        , jsonObject.getString(Constant.INBOUNDDATE)
                        , jsonObject.getString(Constant.TEMPINBOUNDDATE)
                        , jsonObject.getString(Constant.INBOUNDSTATE)
                        , jsonObject.getString(Constant.INBOUNDSTATENAME)
                        , jsonObject.getString(Constant.INBOUNDTYPE)
                        , jsonObject.getString(Constant.CONSUMABLECOUNT));
                db.execSQL(insertSQL);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("INBOUNDORDER更新出错", e.getMessage().toString());
        }

    }

    //endregion

    //region TableSF_CONSUMEINBOUND

    public void updateConsumeInbound(String resultStr) {
        Log.e("更新本地数据库ConsumeInbound", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertSQL = String.format(Constant.InsertIntoCONSUMEINBOUND
                        , jsonObject.getString(Constant.INBOUNDNO)
                        , jsonObject.getString(Constant.CONSUMABLEDEFID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFNAME)
                        , jsonObject.getString(Constant.CONSUMABLEDEFVERSION)
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.ORDERNO)
                        , jsonObject.getString(Constant.ORDERTYPE)
                        , jsonObject.getString(Constant.LINENO)
                        , jsonObject.getString(Constant.ORDERCOMPANY)
                        , jsonObject.getString(Constant.UNIT)
                        , jsonObject.getString(Constant.DIVERSIONUNIT)
                        , jsonObject.getString(Constant.PLANQTY)
                        , jsonObject.getString(Constant.INQTY)
                        , jsonObject.getString(Constant.DIVERSIONQTY)
                        , jsonObject.getString(Constant.RATE));
                db.execSQL(insertSQL);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("CONSUMEINBOUND更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region Table_SF_CONSUMELOTINBOUND
    public void updateConsumeLotInbound(String resultStr) {
        Log.e("更新ConsumeLotInbound", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertSQL = String.format(Constant.InsertIntoCONSUMELOTINBOUND
                        , jsonObject.getString(Constant.INBOUNDNO)
                        , jsonObject.getString(Constant.CONSUMABLELOTID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFNAME)
                        , jsonObject.getString(Constant.CONSUMABLEDEFVERSION)
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.ORDERNO)
                        , jsonObject.getString(Constant.ORDERTYPE)
                        , jsonObject.getString(Constant.LINENO)
                        , jsonObject.getString(Constant.ORDERCOMPANY)
                        , jsonObject.getString(Constant.UNIT)
                        , jsonObject.getString(Constant.DIVERSIONUNIT)
                        , jsonObject.getString(Constant.PLANQTY)
                        , jsonObject.getString(Constant.INQTY)
                        , jsonObject.getString(Constant.DIVERSIONQTY)
                        , jsonObject.getString(Constant.RATE)
                        , jsonObject.getString(Constant.TAGID)
                        , jsonObject.getString(Constant.TAGQTY));
                db.execSQL(insertSQL);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("CONSUMELOTINBOUND更新出错", e.getMessage().toString());
        }
    }

    //endregion
    //endregion

    //region 出库表更新
    //region Table_SF_CONSUMEREQUEST
    public void updateConsumeRequest(String resultStr) {
        Log.e("更新ConsumeRequest", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertSQL = String.format(Constant.InsertIntoCONSUMEREQUEST
                        , jsonObject.getString(Constant.CONSUMEREQNO)
                        , jsonObject.getString(Constant.CONSUMABLECOUNT)
                        , jsonObject.getString(Constant.USERID)
                        , jsonObject.getString(Constant.USERNAME)
                        , jsonObject.getString(Constant.LOCATIONID)
                        , jsonObject.getString(Constant.LOCATIONNAME)
                        , jsonObject.getString(Constant.REQUESTUSERID)
                        , jsonObject.getString(Constant.REQUESTUSERNAME)
                        , jsonObject.getString(Constant.FROMWAREHOUSEID)
                        , jsonObject.getString(Constant.FROMWAREHOUSENAME)
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.TOWAREHOUSENAME)
                        , jsonObject.getString(Constant.REQUESTDATE)
                        , jsonObject.getString(Constant.FINISHPLANDATE));
                db.execSQL(insertSQL);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("CONSUMEREQUEST更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region Table_SF_CONSUMEOUTBOUND
    public void updateConsumeOutbound(String resultStr) {
        Log.e("更新ConsumeOutBound", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertShippingPlan = String.format(Constant.InsertIntoCONSUMEOUTBOUND
                        , jsonObject.getString(Constant.CONSUMABLEDEFID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFVERSION)
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.CONSUMEREQNO)
                        , jsonObject.getString(Constant.UNIT)
                        , jsonObject.getString(Constant.OUTBOUNDSTATE)
                        , jsonObject.getString(Constant.FROMWAREHOUSEID)
                        , jsonObject.getString(Constant.REQUESTQTY)
                        , jsonObject.getString(Constant.OUTQTY));
                db.execSQL(insertShippingPlan);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("CONSUMEOUTBOUND更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region Table_SF_CONSUMELOTOUTBOUND
    public void updateConsumeLotOutbound(String resultStr) {
        Log.e("更新ConsumeLotOutbound", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertShippingPlan = String.format(Constant.InsertIntoCONSUMELOTOUTBOUND
                        , jsonObject.getString(Constant.CONSUMABLELOTID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFVERSION)
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.CONSUMEREQNO)
                        , jsonObject.getString(Constant.UNIT)
                        , jsonObject.getString(Constant.OUTBOUNDSTATE)
                        , jsonObject.getString(Constant.OUTQTY)
                        , jsonObject.getString(Constant.TAGID)
                        , jsonObject.getString(Constant.TAGQTY));
                db.execSQL(insertShippingPlan);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("CONSUMELOTOUTBOUND更新出错", e.getMessage().toString());
        }
    }

    //endregion
    //endregion

    //region 盘点表更新
    //region Table_SF_STOCKCHECK
    public void updateStockCheck(String resultStr) {
        Log.e("更新StockCheck", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertShippingPlan = String.format(Constant.InsertIntoSTOCKCHECK
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.CHECKMONTH)
                        , jsonObject.getString(Constant.STARTDATE)
                        , jsonObject.getString(Constant.ENDDATE)
                        , jsonObject.getString(Constant.WAREHOUSENAME)
                        , jsonObject.getString(Constant.STATE)
                        , jsonObject.getString(Constant.STATENAME));
                db.execSQL(insertShippingPlan);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("STOCKCHECK更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region Table_SF_STOCKCHECKDETAIL
    public void updateStockCheckDetail(String resultStr) {
        Log.e("更新StockCheckDetail", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                String insertShippingPlan = String.format(Constant.InsertIntoSTOCKCHECKDETAIL
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.CHECKMONTH)
                        , jsonObject.getString(Constant.CONSUMABLEDEFNAME)
                        , jsonObject.getString(Constant.CONSUMABLEDEFID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFVERSION)
                        , jsonObject.getString(Constant.UNIT)
                        , jsonObject.getString(Constant.QTY)
                        , jsonObject.getString(Constant.USERID)
                        , jsonObject.getString(Constant.CHECKUNIT)
                        , jsonObject.getString(Constant.CHECKQTY));
                db.execSQL(insertShippingPlan);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("STOCKCHECKDETAIL更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region Table_SF_STOCKLOTCHECKDETAIL
    public void updateStockLotCheckDetail(String resultStr) {
        Log.e("更新StockLotCheckDetail", resultStr);
        try {
            db.beginTransaction();
            JSONArray array = new JSONArray(resultStr);
            String tagqty = "";
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                tagqty = jsonObject.getString(Constant.TAGQTY);
                if (tagqty == null || tagqty.equals("")) {
                    tagqty = "0";
                }
                String insertShippingPlan = String.format(Constant.InsertIntoSTOCKLOTCHECKDETAIL
                        , jsonObject.getString(Constant.WAREHOUSEID)
                        , jsonObject.getString(Constant.CHECKMONTH)
                        , jsonObject.getString(Constant.CONSUMABLEDEFNAME)
                        , jsonObject.getString(Constant.CONSUMABLEDEFID)
                        , jsonObject.getString(Constant.CONSUMABLEDEFVERSION)
                        , jsonObject.getString(Constant.UNIT)
                        , jsonObject.getString(Constant.QTY)
                        , jsonObject.getString(Constant.USERID)
                        , jsonObject.getString(Constant.CHECKUNIT)
                        , jsonObject.getString(Constant.CHECKQTY)
                        , jsonObject.getString(Constant.CONSUMABLELOTID)
                        , jsonObject.getString(Constant.TAGID)
                        , tagqty);
                db.execSQL(insertShippingPlan);
            }
            db.setTransactionSuccessful();
            db.endTransaction();
        } catch (JSONException e) {
            Log.e("STOCKLOTCHECKDETAIL更新出错", e.getMessage().toString());
        }
    }
    //endregion
    //endregion

    //region 其他

    //region Table_Code

    public void updateCode(String resultStr) {

        try {
            Log.e("更新本地数据库code", resultStr);
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CODE_insert = new StringBuffer();
            CODE_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CODE + "("
                    + Constant.CODEID + ","
                    + Constant.CODECLASSID + ","
                    + Constant.DICTIONARYNAME + ","
                    + Constant.LANGUAGETYPE + ","
                    + VALIDSTATE + ")");
            CODE_insert.append(" VALUES( ?, ?, ?, ?,?)");
            for (int i = 0; i < array.length(); i++) {
                CodeData codeDataData = new CodeData();
                JSONObject jsonObject = array.getJSONObject(i);
                codeDataData.setCODEID(jsonObject.getString(Constant.CODEID));
                codeDataData.setCODECLASSID(jsonObject.getString(Constant.CODECLASSID));
                codeDataData.setDICTIONARYNAME(jsonObject.getString(Constant.DICTIONARYNAME));
                codeDataData.setLANGUAGETYPE(jsonObject.getString(Constant.LANGUAGETYPE));
                codeDataData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_CODE(codeDataData, CODE_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CODE更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CODE(CodeData codeData, String CODE_insert) {
        Log.e("codeData.toString()", codeData.toString());
        SQLiteStatement statement = db.compileStatement(CODE_insert);
        statement.bindString(1, codeData.getCODEID());
        statement.bindString(2, codeData.getCODECLASSID());
        statement.bindString(3, codeData.getDICTIONARYNAME());
        statement.bindString(4, codeData.getLANGUAGETYPE());
        statement.bindString(5, codeData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CODE更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region Table_SF_CONSUMABLEDEFINITION
    public void updateConsumabledefinition(String resultStr) {
        Log.e("更新ConsumableDefinition", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMABLEDEFINITION_insert = new StringBuffer();
            CONSUMABLEDEFINITION_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMABLEDEFINITION + "("
                    + CONSUMABLEDEFID + ","
                    + CONSUMABLEDEFVERSION + ","
                    + Constant.CONSUMABLEDEFNAME + ","
                    + VALIDSTATE + ")");
            CONSUMABLEDEFINITION_insert.append(" VALUES( ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumabledefinitionData consumabledefinitionData = new ConsumabledefinitionData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumabledefinitionData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumabledefinitionData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumabledefinitionData.setCONSUMABLEDEFNAME(jsonObject.getString(Constant.CONSUMABLEDEFNAME));
                consumabledefinitionData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_CONSUMABLEDEFINITION(consumabledefinitionData, CONSUMABLEDEFINITION_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMABLEDEF更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMABLEDEFINITION(ConsumabledefinitionData consumabledefinitionData, String CONSUMABLEDEFINITION_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMABLEDEFINITION_insert);
        statement.bindString(1, consumabledefinitionData.getCONSUMABLEDEFID());
        statement.bindString(2, consumabledefinitionData.getCONSUMABLEDEFVERSION());
        statement.bindString(3, consumabledefinitionData.getCONSUMABLEDEFNAME());
        statement.bindString(4, consumabledefinitionData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMABLEDEF更新出错", e.getMessage().toString());
        }
    }


    //endregion

    //region Table_SF_CONSUMESTOCK
    public void updateConsumeStock(String resultStr) {
        Log.e("更新ConsumeStock", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMESTOCK_insert = new StringBuffer();
            CONSUMESTOCK_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMESTOCK + "("
                    + CONSUMABLEDEFID + ","
                    + CONSUMABLEDEFVERSION + ","
                    + WAREHOUSEID + ","
                    + Constant.UNIT + ","
                    + QTY + ","
                    + VALIDSTATE + ")");
            CONSUMESTOCK_insert.append(" VALUES( ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeStockData consumeStockData = new ConsumeStockData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeStockData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumeStockData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumeStockData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                consumeStockData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeStockData.setQTY(jsonObject.getString(QTY));
                consumeStockData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_CONSUMESTOCK(consumeStockData, CONSUMESTOCK_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMESTOCK更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMESTOCK(ConsumeStockData consumeStockData, String CONSUMESTOCK_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMESTOCK_insert);
        statement.bindString(1, consumeStockData.getCONSUMABLEDEFID());
        statement.bindString(2, consumeStockData.getCONSUMABLEDEFVERSION());
        statement.bindString(3, consumeStockData.getWAREHOUSEID());
        statement.bindString(4, consumeStockData.getUNIT());
        statement.bindString(5, consumeStockData.getQTY());
        statement.bindString(6, consumeStockData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMESTOCK更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_CONSUMABLELOT
    public void updateConsumableLot(String resultStr) {
        Log.e("更新CONSUMABLELOT", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMABLELOT_insert = new StringBuffer();
            CONSUMABLELOT_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMABLELOT + "("
                    + CONSUMABLEDEFID + ","
                    + CONSUMABLEDEFVERSION + ","
                    + WAREHOUSEID + ","
                    + QTY + ","
                    + Constant.CONSUMABLESTATE + ","
                    + Constant.CREATEDQTY + ","
                    + CONSUMABLELOTID + ")");
            CONSUMABLELOT_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumableLotData consumableLotData = new ConsumableLotData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumableLotData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumableLotData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumableLotData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                consumableLotData.setQTY(jsonObject.getString(QTY));
                consumableLotData.setCONSUMABLESTATE(jsonObject.getString(Constant.CONSUMABLESTATE));
                consumableLotData.setCREATEDQTY(jsonObject.getString(Constant.CREATEDQTY));
                consumableLotData.setCONSUMABLELOTID(jsonObject.getString(CONSUMABLELOTID));
                updateTableSF_CONSUMABLELOT(consumableLotData, CONSUMABLELOT_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMABLELOT出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMABLELOT(ConsumableLotData consumableLotData, String CONSUMABLELOT_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMABLELOT_insert);
        statement.bindString(1, consumableLotData.getCONSUMABLEDEFID());
        statement.bindString(2, consumableLotData.getCONSUMABLEDEFVERSION());
        statement.bindString(3, consumableLotData.getWAREHOUSEID());
        statement.bindString(4, consumableLotData.getQTY());
        statement.bindString(5, consumableLotData.getCONSUMABLESTATE());
        statement.bindString(6, consumableLotData.getCREATEDQTY());
        statement.bindString(7, consumableLotData.getCONSUMABLELOTID());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMABLELOT更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_AREA
    public void updateArea(String resultStr) {
        Log.e("更新AREA", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer AREA_insert = new StringBuffer();
            AREA_insert.append("INSERT OR REPLACE INTO " + Constant.SF_AREA + "("
                    + AREAID + ","
                    + Constant.AREANAME + ","
                    + Constant.AREATYPE + ","
                    + Constant.LOCATIONID + ","
                    + PLANTID + ","
                    + Constant.PARENTAREAID + ","
                    + VALIDSTATE + ")");
            AREA_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                AreaData areaData = new AreaData();
                JSONObject jsonObject = array.getJSONObject(i);
                areaData.setAREAID(jsonObject.getString(AREAID));
                areaData.setAREANAME(jsonObject.getString(Constant.AREANAME));
                areaData.setAREATYPE(jsonObject.getString(Constant.AREATYPE));
                areaData.setLOCATIONID(jsonObject.getString(Constant.LOCATIONID));
                areaData.setPLANTID(jsonObject.getString(PLANTID));
                areaData.setPARENTAREAID(jsonObject.getString(Constant.PARENTAREAID));
                areaData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_AREA(areaData, AREA_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("AREA更新出错JSON", e.getMessage().toString());
        }
    }

    private void updateTableSF_AREA(AreaData areaData, String area_insert) {
        SQLiteStatement statement = db.compileStatement(area_insert);
        statement.bindString(1, areaData.getAREAID());
        statement.bindString(2, areaData.getAREANAME());
        statement.bindString(3, areaData.getAREATYPE());
        statement.bindString(4, areaData.getLOCATIONID());
        statement.bindString(5, areaData.getPLANTID());
        statement.bindString(6, areaData.getPARENTAREAID());
        statement.bindString(7, areaData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("AREA更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_WAREHOUSE
    public void updateWareHouse(String resultStr) {
        Log.e("更新WAREHOUSE", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer WAREHOUSE_insert = new StringBuffer();
            WAREHOUSE_insert.append("INSERT OR REPLACE INTO " + Constant.SF_WAREHOUSE + "("
                    + WAREHOUSEID + ","
                    + Constant.WAREHOUSENAME + ","
                    + Constant.WAREHOUSETYPE + ","
                    + VALIDSTATE + ")");
            WAREHOUSE_insert.append(" VALUES( ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                WareHouseData wareHouseData = new WareHouseData();
                JSONObject jsonObject = array.getJSONObject(i);
                wareHouseData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                wareHouseData.setWAREHOUSENAME(jsonObject.getString(Constant.WAREHOUSENAME));
                wareHouseData.setWAREHOUSETYPE(jsonObject.getString(Constant.WAREHOUSETYPE));
                wareHouseData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_WAREHOUSE(wareHouseData, WAREHOUSE_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("warehouse更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_WAREHOUSE(WareHouseData wareHouseData, String wareHouse_insert) {
        SQLiteStatement statement = db.compileStatement(wareHouse_insert);
        statement.bindString(1, wareHouseData.getWAREHOUSEID());
        statement.bindString(2, wareHouseData.getWAREHOUSENAME());
        statement.bindString(3, wareHouseData.getWAREHOUSETYPE());
        statement.bindString(4, wareHouseData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("warehouse更新出错", e.getMessage().toString());
        }
    }
    //endregion
    //endregion

    //region 删除保存好的数据
    public void updateSTOCKCHECKSAVE(String stockcheckresult) {
        Log.e("保存的计划是", stockcheckresult);
        String[] aa = stockcheckresult.split(";");
        String WAREHOUSEID = aa[0];
        String CHECKMONTH = aa[1];
        try {
            db.delete(Constant.SF_STOCKCHECK, "WAREHOUSEID = ? AND CHECKMONTH = ?", new String[]{WAREHOUSEID, CHECKMONTH});
            db.delete(Constant.SF_STOCKCHECKDETAIL, "WAREHOUSEID = ? AND CHECKMONTH = ?", new String[]{WAREHOUSEID, CHECKMONTH});
            db.delete(Constant.SF_STOCKLOTCHECKDETAIL, "WAREHOUSEID = ? AND CHECKMONTH = ?", new String[]{WAREHOUSEID, CHECKMONTH});
        } catch (Exception e) {
            Log.e("STOCKCHECKsave更新出错", e.getMessage().toString());
        }
    }

    public void updateSTOCKINSAVE(String stockinresult) {
        Log.e("删除的计划是", stockinresult);
        try {
            db.delete(Constant.SF_INBOUNDORDER, "INBOUNDNO = ?", new String[]{stockinresult});
            db.delete(Constant.SF_CONSUMEINBOUND, "INBOUNDNO = ?", new String[]{stockinresult});
            db.delete(Constant.SF_CONSUMELOTINBOUND, "INBOUNDNO = ?", new String[]{stockinresult});
        } catch (Exception e) {
            Log.e("STOCKinsave更新出错", e.getMessage().toString());
        }
    }
    //endregion
}