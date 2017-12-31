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
import cn.zinus.warehouse.JaveBean.ConsumeInboundData;
import cn.zinus.warehouse.JaveBean.ConsumeLotInboundData;
import cn.zinus.warehouse.JaveBean.ConsumeLotOutboundData;
import cn.zinus.warehouse.JaveBean.ConsumeOutboundData;
import cn.zinus.warehouse.JaveBean.ConsumeRequestData;
import cn.zinus.warehouse.JaveBean.ConsumeStockData;
import cn.zinus.warehouse.JaveBean.InboundOrderData;
import cn.zinus.warehouse.JaveBean.LotShippingData;
import cn.zinus.warehouse.JaveBean.ShippingPlanData;
import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.JaveBean.StockCheckDeatilData;
import cn.zinus.warehouse.JaveBean.StockLotCheckDeatilData;
import cn.zinus.warehouse.JaveBean.WareHouseData;
import cn.zinus.warehouse.JaveBean.lotData;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.Constant.AREAID;
import static cn.zinus.warehouse.util.Constant.CONSUMABLECOUNT;
import static cn.zinus.warehouse.util.Constant.CONSUMABLEDEFID;
import static cn.zinus.warehouse.util.Constant.CONSUMABLEDEFNAME;
import static cn.zinus.warehouse.util.Constant.CONSUMABLEDEFVERSION;
import static cn.zinus.warehouse.util.Constant.CONSUMABLELOTID;
import static cn.zinus.warehouse.util.Constant.CONTAINERSPEC;
import static cn.zinus.warehouse.util.Constant.CUSTOMERID;
import static cn.zinus.warehouse.util.Constant.INBOUNDDATE;
import static cn.zinus.warehouse.util.Constant.INBOUNDNO;
import static cn.zinus.warehouse.util.Constant.INBOUNDSTATE;
import static cn.zinus.warehouse.util.Constant.INBOUNDSTATENAME;
import static cn.zinus.warehouse.util.Constant.INQTY;
import static cn.zinus.warehouse.util.Constant.LINENO;
import static cn.zinus.warehouse.util.Constant.ORDERCOMPANY;
import static cn.zinus.warehouse.util.Constant.ORDERNO;
import static cn.zinus.warehouse.util.Constant.ORDERTYPE;
import static cn.zinus.warehouse.util.Constant.PLANENDTIME;
import static cn.zinus.warehouse.util.Constant.PLANQTY;
import static cn.zinus.warehouse.util.Constant.PLANSTARTTIME;
import static cn.zinus.warehouse.util.Constant.PLANTID;
import static cn.zinus.warehouse.util.Constant.PRODUCTDEFID;
import static cn.zinus.warehouse.util.Constant.PRODUCTDEFVERSION;
import static cn.zinus.warehouse.util.Constant.PRODUCTIONORDERNAME;
import static cn.zinus.warehouse.util.Constant.SCHEDULEDATE;
import static cn.zinus.warehouse.util.Constant.SHIPPINGPLANNO;
import static cn.zinus.warehouse.util.Constant.STATE;
import static cn.zinus.warehouse.util.Constant.STATENAME;
import static cn.zinus.warehouse.util.Constant.TAGID;
import static cn.zinus.warehouse.util.Constant.TAGQTY;
import static cn.zinus.warehouse.util.Constant.TEMPINBOUNDDATE;
import static cn.zinus.warehouse.util.Constant.UNIT;
import static cn.zinus.warehouse.util.Constant.VALIDSTATE;
import static cn.zinus.warehouse.util.Constant.WAREHOUSEID;
import static cn.zinus.warehouse.util.Constant.WAREHOUSENAME;
import static cn.zinus.warehouse.util.Constant.WORKINGSHIFT;

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

    //region Table_InboundOrder

    public void updateInboundOrder(String resultStr) {
        Log.e("更新本地数据库InboundOrder", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer INBOUNDORDER_insert = new StringBuffer();
            INBOUNDORDER_insert.append("INSERT OR REPLACE INTO " + Constant.SF_INBOUNDORDER + "("
                    + INBOUNDNO + ","             //计划号
                    + WAREHOUSEID + ","           //仓库id
                    + WAREHOUSENAME + ","         //仓库名
                    + SCHEDULEDATE + ","          //计划入库时间
                    + INBOUNDDATE + ","           //入库时间
                    + TEMPINBOUNDDATE + ","       //暂入库时间
                    + INBOUNDSTATE + ","          //入库状态
                    + STATENAME + ","             //入库状态名
                    + CONSUMABLECOUNT + ")");     //品目数
            INBOUNDORDER_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            Log.e("更新语句", new String(INBOUNDORDER_insert));
            for (int i = 0; i < array.length(); i++) {
                InboundOrderData inboundOrderTableData = new InboundOrderData();
                JSONObject jsonObject = array.getJSONObject(i);
                inboundOrderTableData.setINBOUNDNO(jsonObject.getString(INBOUNDNO));
                inboundOrderTableData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                inboundOrderTableData.setWAREHOUSENAME(jsonObject.getString(WAREHOUSENAME));
                inboundOrderTableData.setSCHEDULEDATE(jsonObject.getString(SCHEDULEDATE));
                inboundOrderTableData.setINBOUNDDATE(jsonObject.getString(INBOUNDDATE));
                inboundOrderTableData.setTEMPINBOUNDDATE(jsonObject.getString(TEMPINBOUNDDATE));
                inboundOrderTableData.setINBOUNDSTATE(jsonObject.getString(INBOUNDSTATE));
                inboundOrderTableData.setSTATENAME(jsonObject.getString(INBOUNDSTATENAME));
                inboundOrderTableData.setCONSUMABLECOUNT(jsonObject.getString(CONSUMABLECOUNT));
                updateTableSF_INBOUNDORDER(inboundOrderTableData, INBOUNDORDER_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("INBOUNDORDER更新出错", e.getMessage().toString());
        }

    }

    private void updateTableSF_INBOUNDORDER(InboundOrderData inboundOrderData, String INBOUNDORDER_insert) {
        SQLiteStatement statement = db.compileStatement(INBOUNDORDER_insert);
        statement.bindString(1, inboundOrderData.getINBOUNDNO());
        statement.bindString(2, inboundOrderData.getWAREHOUSEID());
        statement.bindString(3, inboundOrderData.getWAREHOUSENAME());
        statement.bindString(4, inboundOrderData.getSCHEDULEDATE());
        statement.bindString(5, inboundOrderData.getINBOUNDDATE());
        statement.bindString(6, inboundOrderData.getTEMPINBOUNDDATE());
        statement.bindString(7, inboundOrderData.getINBOUNDSTATE());
        statement.bindString(8, inboundOrderData.getSTATENAME());
        statement.bindString(9, inboundOrderData.getCONSUMABLECOUNT());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("INBOUNDORDER更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region TableSF_CONSUMEINBOUND

    public void updateConsumeInbound(String resultStr) {
        Log.e("更新本地数据库ConsumeInbound", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMEINBOUND_insert = new StringBuffer();
            CONSUMEINBOUND_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMEINBOUND + "("
                    + INBOUNDNO + ","
                    + CONSUMABLEDEFID + ","
                    + CONSUMABLEDEFVERSION + ","
                    + WAREHOUSEID + ","
                    + ORDERNO + ","
                    + ORDERTYPE + ","
                    + LINENO + ","
                    + INQTY + ","
                    + UNIT + ","
                    + PLANQTY + ","
                    + ORDERCOMPANY + ")");
            CONSUMEINBOUND_insert.append(" VALUES( ?, ?, ?, ?, ? ,?, ?, ?, ?, ?,?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeInboundData consumeInboundData = new ConsumeInboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeInboundData.setINBOUNDNO(jsonObject.getString(INBOUNDNO));
                consumeInboundData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumeInboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumeInboundData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                consumeInboundData.setORDERNO(jsonObject.getString(ORDERNO));
                consumeInboundData.setORDERTYPE(jsonObject.getString(ORDERTYPE));
                consumeInboundData.setLINENO(jsonObject.getString(LINENO));
                consumeInboundData.setINQTY(jsonObject.getString(INQTY));
                consumeInboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeInboundData.setPLANQTY(jsonObject.getString(PLANQTY));
                consumeInboundData.setORDERCOMPANY(jsonObject.getString(ORDERCOMPANY));
                updateTableSF_CONSUMEINBOUND(consumeInboundData, CONSUMEINBOUND_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMEINBOUND更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMEINBOUND(ConsumeInboundData consumeInboundData, String CONSUMEINBOUND_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMEINBOUND_insert);
        statement.bindString(1, consumeInboundData.getINBOUNDNO());
        statement.bindString(2, consumeInboundData.getCONSUMABLEDEFID());
        statement.bindString(3, consumeInboundData.getCONSUMABLEDEFVERSION());
        statement.bindString(4, consumeInboundData.getWAREHOUSEID());
        statement.bindString(5, consumeInboundData.getORDERNO());
        statement.bindString(6, consumeInboundData.getORDERTYPE());
        statement.bindString(7, consumeInboundData.getLINENO());
        statement.bindString(8, consumeInboundData.getINQTY());
        statement.bindString(9, consumeInboundData.getUNIT());
        statement.bindString(10, consumeInboundData.getPLANQTY());
        statement.bindString(11, consumeInboundData.getORDERCOMPANY());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMEINBOUND更新出错", e.getMessage().toString());
        }
    }

    //endregion

    //region Table_SF_CONSUMELOTINBOUND
    public void updateConsumeLotInbound(String resultStr) {
        Log.e("更新ConsumeLotInbound", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMELOTINBOUND_insert = new StringBuffer();
            CONSUMELOTINBOUND_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMELOTINBOUND + "("
                    + INBOUNDNO + ","
                    + CONSUMABLEDEFID + ","
                    + CONSUMABLEDEFVERSION + ","
                    + WAREHOUSEID + ","
                    + ORDERNO + ","
                    + ORDERTYPE + ","
                    + LINENO + ","
                    + INQTY + ","
                    + UNIT + ","
                    + PLANQTY + ","
                    + CONSUMABLELOTID + ","
                    + TAGID + ","
                    + TAGQTY + ","
                    + ORDERCOMPANY+ ")");
            CONSUMELOTINBOUND_insert.append(" VALUES( ?, ?, ?, ?, ? ,?, ?, ?, ?, ?,?,?,?,?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeLotInboundData consumeLotInboundData = new ConsumeLotInboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeLotInboundData. setINBOUNDNO(jsonObject.getString(INBOUNDNO));
                consumeLotInboundData. setCONSUMABLELOTID(jsonObject.getString(CONSUMABLELOTID));
                consumeLotInboundData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumeLotInboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumeLotInboundData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                consumeLotInboundData.setORDERNO(jsonObject.getString(ORDERNO));
                consumeLotInboundData.setORDERTYPE(jsonObject.getString(ORDERTYPE));
                consumeLotInboundData.setLINENO(jsonObject.getString(LINENO));
                consumeLotInboundData.setINQTY(jsonObject.getString(INQTY));
                consumeLotInboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeLotInboundData.setPLANQTY(jsonObject.getString(PLANQTY));
                consumeLotInboundData.setTAGID(jsonObject.getString(TAGID));
                consumeLotInboundData.setTAGQTY(jsonObject.getString(TAGQTY));
                consumeLotInboundData.setORDERCOMPANY(jsonObject.getString(ORDERCOMPANY));
                updateTableSF_CONSUMELOTINBOUND(consumeLotInboundData, CONSUMELOTINBOUND_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMELOTINBOUND更新出错JS", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMELOTINBOUND(ConsumeLotInboundData consumeLotInboundData, String CONSUMELOTINBOUND_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMELOTINBOUND_insert);
        statement.bindString(1, consumeLotInboundData.getINBOUNDNO());
        statement.bindString(2, consumeLotInboundData.getCONSUMABLEDEFID());
        statement.bindString(3, consumeLotInboundData.getCONSUMABLEDEFVERSION());
        statement.bindString(4, consumeLotInboundData.getWAREHOUSEID());
        statement.bindString(5, consumeLotInboundData.getORDERNO());
        statement.bindString(6, consumeLotInboundData.getORDERTYPE());
        statement.bindString(7, consumeLotInboundData.getLINENO());
        statement.bindString(8, consumeLotInboundData.getINQTY());
        statement.bindString(9, consumeLotInboundData.getUNIT());
        statement.bindString(10,consumeLotInboundData.getPLANQTY());
        statement.bindString(11,consumeLotInboundData.getCONSUMABLELOTID());
        statement.bindString(12,consumeLotInboundData.getTAGID());
        statement.bindString(13,consumeLotInboundData.getTAGQTY());
        statement.bindString(14,consumeLotInboundData.getORDERCOMPANY());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMELOTINBOUND更新出错", e.getMessage().toString());
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

    //region Table_SF_CONSUMEREQUEST
    public void updateConsumeRequest(String resultStr) {
        Log.e("更新ConsumeRequest", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMEREQUEST_insert = new StringBuffer();
            CONSUMEREQUEST_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMEREQUEST + "("
                    + Constant.CONSUMEREQNO + ","
                    + Constant.AREAID + ","
                    //   + Constant.USERID + ","
                    + Constant.REQUESTDATE + ","
                    + Constant.OUTBOUNDSTATE + ","
                    + Constant.VALIDSTATE + ")");
            CONSUMEREQUEST_insert.append(" VALUES( ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeRequestData consumeRequestData = new ConsumeRequestData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeRequestData.setCONSUMEREQNO(jsonObject.getString(Constant.CONSUMEREQNO));
                consumeRequestData.setAREAID(jsonObject.getString(Constant.AREAID));
                //  consumeRequestData.setUSERID(jsonObject.getString(Constant.USERID));
                consumeRequestData.setREQUESTDATE(jsonObject.getString(Constant.REQUESTDATE));
                consumeRequestData.setOUTBOUNDSTATE(jsonObject.getString(Constant.OUTBOUNDSTATE));
                consumeRequestData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
                updateTableSF_CONSUMEREQUEST(consumeRequestData, CONSUMEREQUEST_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMEREQUEST更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMEREQUEST(ConsumeRequestData consumeRequestData, String CONSUMEREQUEST_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMEREQUEST_insert);
        statement.bindString(1, consumeRequestData.getCONSUMEREQNO());
        statement.bindString(2, consumeRequestData.getAREAID());
        //  statement.bindString(3, consumeRequestData.getUSERID());
        statement.bindString(3, consumeRequestData.getREQUESTDATE());
        statement.bindString(4, consumeRequestData.getOUTBOUNDSTATE());
        statement.bindString(5, consumeRequestData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMEREQUEST更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_CONSUMEOUTBOUND
    public void updateConsumeOutbound(String resultStr) {
        Log.e("更新ConsumeOutBound", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMEOUTBOUND_insert = new StringBuffer();
            CONSUMEOUTBOUND_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMEOUTBOUND + "("
                    + CONSUMABLEDEFID + ","
                    + CONSUMABLEDEFVERSION + ","
                    + WAREHOUSEID + ","
                    + Constant.CONSUMEREQNO + ","
                    + Constant.UNIT + ","
                    + Constant.OUTBOUNDSTATE + ","
                    + Constant.FROMWAREHOUSEID + ","
                    + VALIDSTATE + ","
                    + Constant.REQUESTQTY + ","
                    + Constant.OUTQTY + ","
                    + VALIDSTATE + ")");
            CONSUMEOUTBOUND_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeOutboundData consumeOutboundData = new ConsumeOutboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeOutboundData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumeOutboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumeOutboundData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                consumeOutboundData.setCONSUMEREQNO(jsonObject.getString(Constant.CONSUMEREQNO));
                consumeOutboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeOutboundData.setOUTBOUNDSTATE(jsonObject.getString(Constant.OUTBOUNDSTATE));
                consumeOutboundData.setFROMWAREHOUSEID(jsonObject.getString(Constant.FROMWAREHOUSEID));
                consumeOutboundData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                consumeOutboundData.setREQUESTQTY(jsonObject.getString(Constant.REQUESTQTY));
                consumeOutboundData.setOUTQTY(jsonObject.getString(Constant.OUTQTY));
                consumeOutboundData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_CONSUMEOUTBOUND(consumeOutboundData, CONSUMEOUTBOUND_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMEOUTBOUND更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMEOUTBOUND(ConsumeOutboundData consumeOutboundData, String CONSUMEOUTBOUND_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMEOUTBOUND_insert);
        statement.bindString(1, consumeOutboundData.getCONSUMABLEDEFID());
        statement.bindString(2, consumeOutboundData.getCONSUMABLEDEFVERSION());
        statement.bindString(3, consumeOutboundData.getWAREHOUSEID());
        statement.bindString(4, consumeOutboundData.getCONSUMEREQNO());
        statement.bindString(5, consumeOutboundData.getUNIT());
        statement.bindString(6, consumeOutboundData.getOUTBOUNDSTATE());
        statement.bindString(7, consumeOutboundData.getFROMWAREHOUSEID());
        statement.bindString(8, consumeOutboundData.getVALIDSTATE());
        statement.bindString(9, consumeOutboundData.getREQUESTQTY());
        statement.bindString(10, consumeOutboundData.getOUTQTY());
        statement.bindString(11, consumeOutboundData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMEOUTBOUND更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_CONSUMELOTOUTBOUND
    public void updateConsumeLotOutbound(String resultStr) {
        Log.e("更新ConsumeLotOutbound", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer CONSUMELOTOUTBOUND_insert = new StringBuffer();
            CONSUMELOTOUTBOUND_insert.append("INSERT OR REPLACE INTO " + Constant.SF_CONSUMELOTOUTBOUND + "("
                    + CONSUMABLELOTID + ","
                    + CONSUMABLEDEFID + ","
                    + CONSUMABLEDEFVERSION + ","
                    + WAREHOUSEID + ","
                    + Constant.CONSUMEREQNO + ","
                    + Constant.UNIT + ","
                    + Constant.OUTBOUNDSTATE + ","
                    + VALIDSTATE + ","
                    + Constant.OUTQTY + ","
                    + VALIDSTATE + ")");
            CONSUMELOTOUTBOUND_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeLotOutboundData consumeLotOutboundData = new ConsumeLotOutboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeLotOutboundData.setCONSUMABLELOTID(jsonObject.getString(CONSUMABLELOTID));
                consumeLotOutboundData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumeLotOutboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumeLotOutboundData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                consumeLotOutboundData.setCONSUMEREQNO(jsonObject.getString(Constant.CONSUMEREQNO));
                consumeLotOutboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeLotOutboundData.setOUTBOUNDSTATE(jsonObject.getString(Constant.OUTBOUNDSTATE));
                consumeLotOutboundData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                consumeLotOutboundData.setOUTQTY(jsonObject.getString(Constant.OUTQTY));
                consumeLotOutboundData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_CONSUMELOTOUTBOUND(consumeLotOutboundData, CONSUMELOTOUTBOUND_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMELOTOUTBOUND更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMELOTOUTBOUND(ConsumeLotOutboundData consumeLotOutboundData, String CONSUMELOTOUTBOUND_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMELOTOUTBOUND_insert);
        statement.bindString(1, consumeLotOutboundData.getCONSUMABLELOTID());
        statement.bindString(2, consumeLotOutboundData.getCONSUMABLEDEFID());
        statement.bindString(3, consumeLotOutboundData.getCONSUMABLEDEFVERSION());
        statement.bindString(4, consumeLotOutboundData.getWAREHOUSEID());
        statement.bindString(5, consumeLotOutboundData.getCONSUMEREQNO());
        statement.bindString(6, consumeLotOutboundData.getUNIT());
        statement.bindString(7, consumeLotOutboundData.getOUTBOUNDSTATE());
        statement.bindString(8, consumeLotOutboundData.getVALIDSTATE());
        statement.bindString(9, consumeLotOutboundData.getOUTQTY());
        statement.bindString(10, consumeLotOutboundData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("CONSUMELOTOUTBOUND更新出错", e.getMessage().toString());
        }
    }


    //endregion

    //region Table_SF_STOCKCHECK
    public void updateStockCheck(String resultStr) {
        Log.e("更新StockCheck", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer STOCKCHECK_insert = new StringBuffer();
            STOCKCHECK_insert.append("INSERT OR REPLACE INTO " + Constant.SF_STOCKCHECK + "("
                    + WAREHOUSEID + ","
                    + Constant.CHECKMONTH + ","
                    + Constant.STARTDATE + ","
                    + Constant.ENDDATE + ","
                    + STATENAME +")");
            STOCKCHECK_insert.append(" VALUES( ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                StockCheckData stockCheckData = new StockCheckData();
                JSONObject jsonObject = array.getJSONObject(i);
                stockCheckData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                stockCheckData.setCHECKMONTH(jsonObject.getString(Constant.CHECKMONTH));
                stockCheckData.setSTARTDATE(jsonObject.getString(Constant.STARTDATE));
                stockCheckData.setENDDATE(jsonObject.getString(Constant.ENDDATE));
                stockCheckData.setSTATENAME(jsonObject.getString(STATENAME));

                updateTableSF_STOCKCHECK(stockCheckData, STOCKCHECK_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("STOCKCHECK更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_STOCKCHECK(StockCheckData stockCheckData, String STOCKCHECK_insert) {
        SQLiteStatement statement = db.compileStatement(STOCKCHECK_insert);
        statement.bindString(1, stockCheckData.getWAREHOUSEID());
        statement.bindString(2, stockCheckData.getCHECKMONTH());
        statement.bindString(3, stockCheckData.getSTARTDATE());
        statement.bindString(4, stockCheckData.getENDDATE());
        statement.bindString(5, stockCheckData.getSTATENAME());

        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("STOCKCHECK更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_STOCKCHECKDETAIL
    public void updateStockCheckDetail(String resultStr) {
        Log.e("更新StockCheckDetail", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer STOCKCHECKDETAIL_insert = new StringBuffer();
            STOCKCHECKDETAIL_insert.append("INSERT OR REPLACE INTO " + Constant.SF_STOCKCHECKDETAIL + "("
                    + WAREHOUSEID + ","
                    + Constant.CHECKMONTH + ","
                    + CONSUMABLEDEFNAME + ","
                    + Constant.UNIT + ","
                    + Constant.QTY + ","
                    + Constant.USERID + ","
                  //  + Constant.CHECKUNIT + ","
                    + Constant.CHECKQTY + ")");
            STOCKCHECKDETAIL_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                StockCheckDeatilData stockCheckDetailData = new StockCheckDeatilData();
                JSONObject jsonObject = array.getJSONObject(i);
                stockCheckDetailData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                stockCheckDetailData.setCHECKMONTH(jsonObject.getString(Constant.CHECKMONTH));
                stockCheckDetailData.setCONSUMEABLDEFNAME(jsonObject.getString(CONSUMABLEDEFNAME));
                stockCheckDetailData.setUNIT(jsonObject.getString(Constant.UNIT));
                stockCheckDetailData.setQTY(jsonObject.getString(Constant.QTY));
                stockCheckDetailData.setUSERID(jsonObject.getString(Constant.USERID));
               // stockCheckDetailData.setCHECKUNIT(jsonObject.getString(Constant.CHECKUNIT));
                stockCheckDetailData.setCHECKQTY(jsonObject.getString(Constant.CHECKQTY));
                updateTableSF_STOCKCHECKDETAIL(stockCheckDetailData, STOCKCHECKDETAIL_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("STOCKCHECKDETAIL更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_STOCKCHECKDETAIL(StockCheckDeatilData stockCheckDetailData, String STOCKCHECKDETAIL_insert) {
        SQLiteStatement statement = db.compileStatement(STOCKCHECKDETAIL_insert);
        statement.bindString(1, stockCheckDetailData.getWAREHOUSEID());
        statement.bindString(2, stockCheckDetailData.getCHECKMONTH());
        statement.bindString(3, stockCheckDetailData.getCONSUMEABLDEFNAME());
      //  statement.bindString(4, stockCheckDetailData.getCONSUMEABLDEFVERSION());
        statement.bindString(4, stockCheckDetailData.getUNIT());
        statement.bindString(5, stockCheckDetailData.getQTY());
        statement.bindString(6, stockCheckDetailData.getUSERID());
       // statement.bindString(8, stockCheckDetailData.getCHECKUNIT());
        statement.bindString(7, stockCheckDetailData.getCHECKQTY());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("STOCKCHECKDETAIL更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_STOCKLOTCHECKDETAIL
    public void updateStockLotCheckDetail(String resultStr) {
        Log.e("更新StockLotCheckDetail", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer STOCKLOTCHECKDETAIL_insert = new StringBuffer();
            STOCKLOTCHECKDETAIL_insert.append("INSERT OR REPLACE INTO " + Constant.SF_STOCKLOTCHECKDETAIL + "("
                    + WAREHOUSEID + ","
                    + Constant.CHECKMONTH + ","
                    + CONSUMABLEDEFNAME + ","
                    + CONSUMABLELOTID + ","
                    + Constant.UNIT + ","
                    + Constant.QTY + ","
                    + Constant.USERID + ","
                  //  + Constant.CHECKUNIT + ","
                    + Constant.CHECKQTY + ")");
            STOCKLOTCHECKDETAIL_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                StockLotCheckDeatilData stockLotCheckDetailData = new StockLotCheckDeatilData();
                JSONObject jsonObject = array.getJSONObject(i);
                stockLotCheckDetailData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                stockLotCheckDetailData.setCHECKMONTH(jsonObject.getString(Constant.CHECKMONTH));
                stockLotCheckDetailData.setCONSUMEABLDEFNAME(jsonObject.getString(CONSUMABLEDEFNAME));
                stockLotCheckDetailData.setCONSUMABLELOTID(jsonObject.getString(CONSUMABLELOTID));
                stockLotCheckDetailData.setUNIT(jsonObject.getString(Constant.UNIT));
                stockLotCheckDetailData.setQTY(jsonObject.getString(Constant.QTY));
                stockLotCheckDetailData.setUSERID(jsonObject.getString(Constant.USERID));
            //    stockLotCheckDetailData.setCHECKUNIT(jsonObject.getString(Constant.CHECKUNIT));
                stockLotCheckDetailData.setCHECKQTY(jsonObject.getString(Constant.CHECKQTY));
                updateTableSF_STOCKLOTCHECKDETAIL(stockLotCheckDetailData, STOCKLOTCHECKDETAIL_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("STOCKLOTCHECKDETAIL更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_STOCKLOTCHECKDETAIL(StockLotCheckDeatilData stockLotCheckDetailData, String STOCKLOTCHECKDETAIL_insert) {
        SQLiteStatement statement = db.compileStatement(STOCKLOTCHECKDETAIL_insert);
        statement.bindString(1, stockLotCheckDetailData.getWAREHOUSEID());
        statement.bindString(2, stockLotCheckDetailData.getCHECKMONTH());
        statement.bindString(3, stockLotCheckDetailData.getCONSUMEABLDEFNAME());
       // statement.bindString(4, stockLotCheckDetailData.getCONSUMEABLDEFVERSION());
        statement.bindString(4, stockLotCheckDetailData.getCONSUMABLELOTID());
        statement.bindString(5, stockLotCheckDetailData.getUNIT());
        statement.bindString(6, stockLotCheckDetailData.getQTY());
        statement.bindString(7, stockLotCheckDetailData.getUSERID());
     //   statement.bindString(9, stockLotCheckDetailData.getCHECKUNIT());
        statement.bindString(8, stockLotCheckDetailData.getCHECKQTY());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("STOCKLOTCHECKDETAIL更新出错", e.getMessage().toString());
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
                    + Constant.QTY + ","
                    + VALIDSTATE + ")");
            CONSUMESTOCK_insert.append(" VALUES( ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeStockData consumeStockData = new ConsumeStockData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeStockData.setCONSUMABLEDEFID(jsonObject.getString(CONSUMABLEDEFID));
                consumeStockData.setCONSUMABLEDEFVERSION(jsonObject.getString(CONSUMABLEDEFVERSION));
                consumeStockData.setWAREHOUSEID(jsonObject.getString(WAREHOUSEID));
                consumeStockData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeStockData.setQTY(jsonObject.getString(Constant.QTY));
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
                    + Constant.QTY + ","
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
                consumableLotData.setQTY(jsonObject.getString(Constant.QTY));
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

    //region Table_SF_SHIPPINGPLAN
    public void updateShippingPlan(String resultStr) {
        Log.e("更新SF_SHIPPINGPLAN", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer SHIPPINGPLAN_insert = new StringBuffer();
            SHIPPINGPLAN_insert.append("INSERT OR REPLACE INTO " + Constant.SF_SHIPPINGPLAN + "("
                    + SHIPPINGPLANNO + ","
                    + PRODUCTIONORDERNAME + ","
                    + PLANTID + ","
                    + CUSTOMERID + ","
                    + PRODUCTDEFID + ","
                    + PRODUCTDEFVERSION + ","
                    + PLANSTARTTIME + ","
                    + PLANENDTIME + ","
                    + PLANQTY + ","
                    + CONTAINERSPEC + ","
                    + WORKINGSHIFT + ","
                    + AREAID + ","
                    + STATE + ","
                    + VALIDSTATE + ")");
            SHIPPINGPLAN_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ShippingPlanData shippingPlanData = new ShippingPlanData();
                JSONObject jsonObject = array.getJSONObject(i);
                shippingPlanData.setSHIPPINGPLANNO(jsonObject.getString(SHIPPINGPLANNO));
                shippingPlanData.setPRODUCTIONORDERNAME(jsonObject.getString(PRODUCTIONORDERNAME));
                shippingPlanData.setPLANTID(jsonObject.getString(PLANTID));
                shippingPlanData.setCUSTOMERID(jsonObject.getString(CUSTOMERID));
                shippingPlanData.setPRODUCTDEFID(jsonObject.getString(PRODUCTDEFID));
                shippingPlanData.setPRODUCTDEFVERSION(jsonObject.getString(PRODUCTDEFVERSION));
                shippingPlanData.setPLANSTARTTIME(jsonObject.getString(PLANSTARTTIME));
                shippingPlanData.setPLANENDTIME(jsonObject.getString(PLANENDTIME));
                shippingPlanData.setPLANQTY(jsonObject.getString(PLANQTY));
                shippingPlanData.setCONTAINERSPEC(jsonObject.getString(CONTAINERSPEC));
                shippingPlanData.setWORKINGSHIFT(jsonObject.getString(WORKINGSHIFT));
                shippingPlanData.setAREAID(jsonObject.getString(AREAID));
                shippingPlanData.setSTATE(jsonObject.getString(STATE));
                shippingPlanData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_SHIPPINGPLAN(shippingPlanData, SHIPPINGPLAN_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("SHIPPINGPLAN更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_SHIPPINGPLAN(ShippingPlanData shippingPlanData, String SHIPPINGPLAN_insert) {
        SQLiteStatement statement = db.compileStatement(SHIPPINGPLAN_insert);
        statement.bindString(1, shippingPlanData.getSHIPPINGPLANNO());
        statement.bindString(2, shippingPlanData.getPRODUCTIONORDERNAME());
        statement.bindString(3, shippingPlanData.getPLANTID());
        statement.bindString(4, shippingPlanData.getCUSTOMERID());
        statement.bindString(5, shippingPlanData.getPRODUCTDEFID());
        statement.bindString(6, shippingPlanData.getPRODUCTDEFVERSION());
        statement.bindString(7, shippingPlanData.getPLANSTARTTIME());
        statement.bindString(8, shippingPlanData.getPLANENDTIME());
        statement.bindString(9, shippingPlanData.getPLANQTY());
        statement.bindString(10, shippingPlanData.getCONTAINERSPEC());
        statement.bindString(11, shippingPlanData.getWORKINGSHIFT());
        statement.bindString(12, shippingPlanData.getAREAID());
        statement.bindString(13, shippingPlanData.getSTATE());
        statement.bindString(14, shippingPlanData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("SHIPPINGPLAN更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_LOTSHIPPING
    public void updateLotShipping(String resultStr) {
        Log.e("更新SF_LOTSHIPPING", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer LOTSHIPPING_insert = new StringBuffer();
            LOTSHIPPING_insert.append("INSERT OR REPLACE INTO " + Constant.SF_LOTSHIPPING + "("
                    + Constant.LOTID + ","
                    + SHIPPINGPLANNO + ","
                    + Constant.CONTAINERNO + ","
                    + VALIDSTATE + ")");
            LOTSHIPPING_insert.append(" VALUES( ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                LotShippingData lotShippingData = new LotShippingData();
                JSONObject jsonObject = array.getJSONObject(i);
                lotShippingData.setLOTID(jsonObject.getString(Constant.LOTID));
                lotShippingData.setSHIPPINGPLANNO(jsonObject.getString(SHIPPINGPLANNO));
                lotShippingData.setCONTAINERNO(jsonObject.getString(Constant.CONTAINERNO));
                lotShippingData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_LOTSHIPPING(lotShippingData, LOTSHIPPING_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("LOTSHIPPING更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_LOTSHIPPING(LotShippingData lotShippingData, String LOTSHIPPING_insert) {
        SQLiteStatement statement = db.compileStatement(LOTSHIPPING_insert);
        statement.bindString(1, lotShippingData.getLOTID());
        statement.bindString(2, lotShippingData.getSHIPPINGPLANNO());
        statement.bindString(3, lotShippingData.getCONTAINERNO());
        statement.bindString(4, lotShippingData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("LOTSHIPPING更新出错", e.getMessage().toString());
        }
    }
    //endregion

    //region Table_SF_LOT
    public void updateLot(String resultStr) {
        Log.e("更新SF_LOT", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            StringBuffer LOT_insert = new StringBuffer();
            LOT_insert.append("INSERT OR REPLACE INTO " + Constant.SF_LOT + "("
                    + Constant.LOTID + ","
                    + Constant.SALESORDERID + ","
                    + Constant.PROCESSSEGMENTID + ","
                    + Constant.LOTSTATE + ","
                    + Constant.RFID + ","
                    + Constant.QTY + ","
                    + VALIDSTATE + ")");
            LOT_insert.append(" VALUES( ?, ?, ?, ? , ? , ? , ? )");
            for (int i = 0; i < array.length(); i++) {
                lotData lotData = new lotData();
                JSONObject jsonObject = array.getJSONObject(i);
                lotData.setLOTID(jsonObject.getString(Constant.LOTID));
                lotData.setSALESORDERID(jsonObject.getString(Constant.SALESORDERID));
                lotData.setPROCESSSEGMENTID(jsonObject.getString(Constant.PROCESSSEGMENTID));
                lotData.setLOTSTATE(jsonObject.getString(Constant.LOTSTATE));
                lotData.setRFID(jsonObject.getString(Constant.RFID));
                lotData.setQTY(jsonObject.getString(Constant.QTY));
                lotData.setVALIDSTATE(jsonObject.getString(VALIDSTATE));
                updateTableSF_LOT(lotData, LOT_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("LOT更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_LOT(lotData lotData, String LOTSHIPPING_insert) {
        SQLiteStatement statement = db.compileStatement(LOTSHIPPING_insert);
        statement.bindString(1, lotData.getLOTID());
        statement.bindString(2, lotData.getSALESORDERID());
        statement.bindString(3, lotData.getPROCESSSEGMENTID());
        statement.bindString(4, lotData.getLOTSTATE());
        statement.bindString(5, lotData.getRFID());
        statement.bindString(6, lotData.getQTY());
        statement.bindString(7, lotData.getVALIDSTATE());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("LOT更新出错", e.getMessage().toString());
        }
    }
    //endregion

}