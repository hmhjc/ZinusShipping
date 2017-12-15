package cn.zinus.shipping.SocketConnect;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.zinus.shipping.JaveBean.AreaData;
import cn.zinus.shipping.JaveBean.CodeData;
import cn.zinus.shipping.JaveBean.ConsumableLotData;
import cn.zinus.shipping.JaveBean.ConsumabledefinitionData;
import cn.zinus.shipping.JaveBean.ConsumeInboundData;
import cn.zinus.shipping.JaveBean.ConsumeLotInboundData;
import cn.zinus.shipping.JaveBean.ConsumeLotOutboundData;
import cn.zinus.shipping.JaveBean.ConsumeOutboundData;
import cn.zinus.shipping.JaveBean.ConsumeRequestData;
import cn.zinus.shipping.JaveBean.ConsumeStockData;
import cn.zinus.shipping.JaveBean.InboundOrderData;
import cn.zinus.shipping.JaveBean.LotShippingData;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.JaveBean.StockCheckData;
import cn.zinus.shipping.JaveBean.StockCheckDeatilData;
import cn.zinus.shipping.JaveBean.StockLotCheckDeatilData;
import cn.zinus.shipping.JaveBean.WareHouseData;
import cn.zinus.shipping.JaveBean.LotData;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

import static cn.zinus.shipping.util.Constant.ISOISAVE;

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
                    + Constant.VALIDSTATE + ")");
            CODE_insert.append(" VALUES( ?, ?, ?, ?,?)");
            for (int i = 0; i < array.length(); i++) {
                CodeData codeDataData = new CodeData();
                JSONObject jsonObject = array.getJSONObject(i);
                codeDataData.setCODEID(jsonObject.getString(Constant.CODEID));
                codeDataData.setCODECLASSID(jsonObject.getString(Constant.CODECLASSID));
                codeDataData.setDICTIONARYNAME(jsonObject.getString(Constant.DICTIONARYNAME));
                codeDataData.setLANGUAGETYPE(jsonObject.getString(Constant.LANGUAGETYPE));
                codeDataData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
                    + Constant.INBOUNDNO + ","
                    + Constant.WAREHOUSEID + ","
                    + Constant.INBOUNDDATE + ","
                    + Constant.INBOUNDSTATE + ","
                    + Constant.INSPECTIONRESULT + ","
                    + Constant.URGENCYTYPE + ","
                    + Constant.SCHEDULEDATE + ","
                    + Constant.TEMPINBOUNDDATE + ","
                    + Constant.VALIDSTATE + ")");
            INBOUNDORDER_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                InboundOrderData inboundOrderTableData = new InboundOrderData();
                JSONObject jsonObject = array.getJSONObject(i);
                inboundOrderTableData.setINBOUNDNO(jsonObject.getString(Constant.INBOUNDNO));
                inboundOrderTableData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                inboundOrderTableData.setINBOUNDDATE(jsonObject.getString(Constant.INBOUNDDATE));
                inboundOrderTableData.setINBOUNDSTATE(jsonObject.getString(Constant.INBOUNDSTATE));
                inboundOrderTableData.setINSPECTIONRESULT(jsonObject.getString(Constant.INSPECTIONRESULT));
                inboundOrderTableData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
                inboundOrderTableData.setURGENCYTYPE(jsonObject.getString(Constant.URGENCYTYPE));
                inboundOrderTableData.setSCHEDULEDATE(jsonObject.getString(Constant.SCHEDULEDATE));
                inboundOrderTableData.setTEMPINBOUNDDATE(jsonObject.getString(Constant.TEMPINBOUNDDATE));

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
        statement.bindString(3, inboundOrderData.getINBOUNDDATE());
        statement.bindString(4, inboundOrderData.getINBOUNDSTATE());
        statement.bindString(5, inboundOrderData.getINSPECTIONRESULT());
        statement.bindString(6, inboundOrderData.getURGENCYTYPE());
        statement.bindString(7, inboundOrderData.getSCHEDULEDATE());
        statement.bindString(8, inboundOrderData.getTEMPINBOUNDDATE());
        statement.bindString(9, inboundOrderData.getVALIDSTATE());
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
                    + Constant.INBOUNDNO + ","
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.WAREHOUSEID + ","
                    + Constant.INQTY + ","
                    + Constant.INBOUNDSTATE + ","
                    + Constant.UNIT + ","
                    + Constant.INBOUNDDATE + ","
                    + Constant.PLANQTY + ","
                    + Constant.VALIDSTATE + ")");
            CONSUMEINBOUND_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeInboundData consumeInboundData = new ConsumeInboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeInboundData.setINBOUNDNO(jsonObject.getString(Constant.INBOUNDNO));
                consumeInboundData.setCONSUMABLEDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                consumeInboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                consumeInboundData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                consumeInboundData.setINQTY(jsonObject.getString(Constant.INQTY));
                consumeInboundData.setINBOUNDSTATE(jsonObject.getString(Constant.INBOUNDSTATE));
                consumeInboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeInboundData.setINBOUNDDATE(jsonObject.getString(Constant.INBOUNDDATE));
                consumeInboundData.setPLANQTY(jsonObject.getString(Constant.PLANQTY));
                consumeInboundData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
        statement.bindString(5, consumeInboundData.getINQTY());
        statement.bindString(6, consumeInboundData.getINBOUNDSTATE());
        statement.bindString(7, consumeInboundData.getUNIT());
        statement.bindString(8, consumeInboundData.getINBOUNDDATE());
        statement.bindString(9, consumeInboundData.getPLANQTY());
        statement.bindString(10, consumeInboundData.getVALIDSTATE());
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
                    + Constant.INBOUNDNO + ","
                    + Constant.CONSUMABLELOTID + ","
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.WAREHOUSEID + ","
                    + Constant.INQTY + ","
                    + Constant.INBOUNDSTATE + ","
                    + Constant.UNIT + ","
                    + Constant.PLANQTY + ","
                    + Constant.VALIDSTATE + ")");
            CONSUMELOTINBOUND_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeLotInboundData consumeLotInboundData = new ConsumeLotInboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeLotInboundData.setINBOUNDNO(jsonObject.getString(Constant.INBOUNDNO));
                consumeLotInboundData.setCONSUMABLELOTID(jsonObject.getString(Constant.CONSUMABLELOTID));
                consumeLotInboundData.setCONSUMABLEDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                consumeLotInboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                consumeLotInboundData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                consumeLotInboundData.setINQTY(jsonObject.getString(Constant.INQTY));
                consumeLotInboundData.setINBOUNDSTATE(jsonObject.getString(Constant.INBOUNDSTATE));
                consumeLotInboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeLotInboundData.setPLANQTY(jsonObject.getString(Constant.PLANQTY));
                consumeLotInboundData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
                updateTableSF_CONSUMELOTINBOUND(consumeLotInboundData, CONSUMELOTINBOUND_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("CONSUMELOTINBOUND更新出错JS", e.getMessage().toString());
        }
    }

    private void updateTableSF_CONSUMELOTINBOUND(ConsumeLotInboundData consumeLotInboundData, String CONSUMELOTINBOUND_insert) {
        SQLiteStatement statement = db.compileStatement(CONSUMELOTINBOUND_insert);
        statement.bindString(1, consumeLotInboundData.getINBOUNDNO());
        statement.bindString(2, consumeLotInboundData.getCONSUMABLELOTID());
        statement.bindString(3, consumeLotInboundData.getCONSUMABLEDEFID());
        statement.bindString(4, consumeLotInboundData.getCONSUMABLEDEFVERSION());
        statement.bindString(5, consumeLotInboundData.getWAREHOUSEID());
        statement.bindString(6, consumeLotInboundData.getINQTY());
        statement.bindString(7, consumeLotInboundData.getINBOUNDSTATE());
        statement.bindString(8, consumeLotInboundData.getUNIT());
        statement.bindString(9, consumeLotInboundData.getPLANQTY());
        statement.bindString(10, consumeLotInboundData.getVALIDSTATE());
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
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.CONSUMABLEDEFNAME + ","
                    + Constant.VALIDSTATE + ")");
            CONSUMABLEDEFINITION_insert.append(" VALUES( ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumabledefinitionData consumabledefinitionData = new ConsumabledefinitionData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumabledefinitionData.setCONSUMABLEDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                consumabledefinitionData.setCONSUMABLEDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                consumabledefinitionData.setCONSUMABLEDEFNAME(jsonObject.getString(Constant.CONSUMABLEDEFNAME));
                consumabledefinitionData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.WAREHOUSEID + ","
                    + Constant.CONSUMEREQNO + ","
                    + Constant.UNIT + ","
                    + Constant.OUTBOUNDSTATE + ","
                    + Constant.USERID + ","
                    + Constant.FROMWAREHOUSEID + ","
                    + Constant.VALIDSTATE + ","
                    + Constant.REQUESTQTY + ","
                    + Constant.OUTQTY + ","
                    + Constant.VALIDSTATE + ")");
            CONSUMEOUTBOUND_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeOutboundData consumeOutboundData = new ConsumeOutboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeOutboundData.setCONSUMABLEDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                consumeOutboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                consumeOutboundData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                consumeOutboundData.setCONSUMEREQNO(jsonObject.getString(Constant.CONSUMEREQNO));
                consumeOutboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeOutboundData.setOUTBOUNDSTATE(jsonObject.getString(Constant.OUTBOUNDSTATE));
                consumeOutboundData.setUSERID(jsonObject.getString(Constant.USERID));
                consumeOutboundData.setFROMWAREHOUSEID(jsonObject.getString(Constant.FROMWAREHOUSEID));
                consumeOutboundData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
                consumeOutboundData.setREQUESTQTY(jsonObject.getString(Constant.REQUESTQTY));
                consumeOutboundData.setOUTQTY(jsonObject.getString(Constant.OUTQTY));
                consumeOutboundData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
        statement.bindString(7, consumeOutboundData.getUSERID());
        statement.bindString(8, consumeOutboundData.getFROMWAREHOUSEID());
        statement.bindString(9, consumeOutboundData.getVALIDSTATE());
        statement.bindString(10, consumeOutboundData.getREQUESTQTY());
        statement.bindString(11, consumeOutboundData.getOUTQTY());
        statement.bindString(12, consumeOutboundData.getVALIDSTATE());
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
                    + Constant.CONSUMABLELOTID + ","
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.WAREHOUSEID + ","
                    + Constant.CONSUMEREQNO + ","
                    + Constant.UNIT + ","
                    + Constant.OUTBOUNDSTATE + ","
                    + Constant.USERID + ","
                    + Constant.VALIDSTATE + ","
                    + Constant.REQUESTQTY + ","
                    + Constant.OUTQTY + ","
                    + Constant.VALIDSTATE + ")");
            CONSUMELOTOUTBOUND_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeLotOutboundData consumeLotOutboundData = new ConsumeLotOutboundData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeLotOutboundData.setCONSUMABLELOTID(jsonObject.getString(Constant.CONSUMABLELOTID));
                consumeLotOutboundData.setCONSUMABLEDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                consumeLotOutboundData.setCONSUMABLEDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                consumeLotOutboundData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                consumeLotOutboundData.setCONSUMEREQNO(jsonObject.getString(Constant.CONSUMEREQNO));
                consumeLotOutboundData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeLotOutboundData.setOUTBOUNDSTATE(jsonObject.getString(Constant.OUTBOUNDSTATE));
                consumeLotOutboundData.setUSERID(jsonObject.getString(Constant.USERID));
                consumeLotOutboundData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
                consumeLotOutboundData.setREQUESTQTY(jsonObject.getString(Constant.REQUESTQTY));
                consumeLotOutboundData.setOUTQTY(jsonObject.getString(Constant.OUTQTY));
                consumeLotOutboundData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
        statement.bindString(8, consumeLotOutboundData.getUSERID());
        statement.bindString(9, consumeLotOutboundData.getVALIDSTATE());
        statement.bindString(10, consumeLotOutboundData.getREQUESTQTY());
        statement.bindString(11, consumeLotOutboundData.getOUTQTY());
        statement.bindString(12, consumeLotOutboundData.getVALIDSTATE());
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
                    + Constant.WAREHOUSEID + ","
                    + Constant.CHECKMONTH + ","
                    + Constant.STARTDATE + ","
                    + Constant.ENDDATE + ","
                    + Constant.STATE + ","
                    + Constant.VALIDSTATE + ")");
            STOCKCHECK_insert.append(" VALUES( ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                StockCheckData stockCheckData = new StockCheckData();
                JSONObject jsonObject = array.getJSONObject(i);
                stockCheckData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                stockCheckData.setCHECKMONTH(jsonObject.getString(Constant.CHECKMONTH));
                stockCheckData.setSTARTDATE(jsonObject.getString(Constant.STARTDATE));
                stockCheckData.setENDDATE(jsonObject.getString(Constant.ENDDATE));
                stockCheckData.setSTATE(jsonObject.getString(Constant.STATE));
                stockCheckData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
        statement.bindString(5, stockCheckData.getSTATE());
        statement.bindString(6, stockCheckData.getVALIDSTATE());
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
                    + Constant.WAREHOUSEID + ","
                    + Constant.CHECKMONTH + ","
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.UNIT + ","
                    + Constant.QTY + ","
                    + Constant.USERID + ","
                    + Constant.CHECKUNIT + ","
                    + Constant.CHECKQTY + ","
                    + Constant.VALIDSTATE + ")");
            STOCKCHECKDETAIL_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                StockCheckDeatilData stockCheckDetailData = new StockCheckDeatilData();
                JSONObject jsonObject = array.getJSONObject(i);
                stockCheckDetailData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                stockCheckDetailData.setCHECKMONTH(jsonObject.getString(Constant.CHECKMONTH));
                stockCheckDetailData.setCONSUMEABLDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                stockCheckDetailData.setCONSUMEABLDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                stockCheckDetailData.setUNIT(jsonObject.getString(Constant.UNIT));
                stockCheckDetailData.setQTY(jsonObject.getString(Constant.QTY));
                stockCheckDetailData.setUSERID(jsonObject.getString(Constant.USERID));
                stockCheckDetailData.setCHECKUNIT(jsonObject.getString(Constant.CHECKUNIT));
                stockCheckDetailData.setCHECKQTY(jsonObject.getString(Constant.CHECKQTY));
                stockCheckDetailData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
        statement.bindString(3, stockCheckDetailData.getCONSUMEABLDEFID());
        statement.bindString(4, stockCheckDetailData.getCONSUMEABLDEFVERSION());
        statement.bindString(5, stockCheckDetailData.getUNIT());
        statement.bindString(6, stockCheckDetailData.getQTY());
        statement.bindString(7, stockCheckDetailData.getUSERID());
        statement.bindString(8, stockCheckDetailData.getCHECKUNIT());
        statement.bindString(9, stockCheckDetailData.getCHECKQTY());
        statement.bindString(10, stockCheckDetailData.getVALIDSTATE());
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
                    + Constant.WAREHOUSEID + ","
                    + Constant.CHECKMONTH + ","
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.CONSUMABLELOTID + ","
                    + Constant.UNIT + ","
                    + Constant.QTY + ","
                    + Constant.USERID + ","
                    + Constant.CHECKUNIT + ","
                    + Constant.CHECKQTY + ","
                    + Constant.VALIDSTATE + ")");
            STOCKLOTCHECKDETAIL_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                StockLotCheckDeatilData stockLotCheckDetailData = new StockLotCheckDeatilData();
                JSONObject jsonObject = array.getJSONObject(i);
                stockLotCheckDetailData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                stockLotCheckDetailData.setCHECKMONTH(jsonObject.getString(Constant.CHECKMONTH));
                stockLotCheckDetailData.setCONSUMEABLDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                stockLotCheckDetailData.setCONSUMEABLDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                stockLotCheckDetailData.setCONSUMABLELOTID(jsonObject.getString(Constant.CONSUMABLELOTID));
                stockLotCheckDetailData.setUNIT(jsonObject.getString(Constant.UNIT));
                stockLotCheckDetailData.setQTY(jsonObject.getString(Constant.QTY));
                stockLotCheckDetailData.setUSERID(jsonObject.getString(Constant.USERID));
                stockLotCheckDetailData.setCHECKUNIT(jsonObject.getString(Constant.CHECKUNIT));
                stockLotCheckDetailData.setCHECKQTY(jsonObject.getString(Constant.CHECKQTY));
                stockLotCheckDetailData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
        statement.bindString(3, stockLotCheckDetailData.getCONSUMEABLDEFID());
        statement.bindString(4, stockLotCheckDetailData.getCONSUMEABLDEFVERSION());
        statement.bindString(5, stockLotCheckDetailData.getCONSUMABLELOTID());
        statement.bindString(6, stockLotCheckDetailData.getUNIT());
        statement.bindString(7, stockLotCheckDetailData.getQTY());
        statement.bindString(8, stockLotCheckDetailData.getUSERID());
        statement.bindString(9, stockLotCheckDetailData.getCHECKUNIT());
        statement.bindString(10, stockLotCheckDetailData.getCHECKQTY());
        statement.bindString(11, stockLotCheckDetailData.getVALIDSTATE());
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
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.WAREHOUSEID + ","
                    + Constant.UNIT + ","
                    + Constant.QTY + ","
                    + Constant.VALIDSTATE + ")");
            CONSUMESTOCK_insert.append(" VALUES( ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumeStockData consumeStockData = new ConsumeStockData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumeStockData.setCONSUMABLEDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                consumeStockData.setCONSUMABLEDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                consumeStockData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                consumeStockData.setUNIT(jsonObject.getString(Constant.UNIT));
                consumeStockData.setQTY(jsonObject.getString(Constant.QTY));
                consumeStockData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
                    + Constant.CONSUMABLEDEFID + ","
                    + Constant.CONSUMABLEDEFVERSION + ","
                    + Constant.WAREHOUSEID + ","
                    + Constant.QTY + ","
                    + Constant.CONSUMABLESTATE + ","
                    + Constant.CREATEDQTY + ","
                    + Constant.CONSUMABLELOTID + ")");
            CONSUMABLELOT_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                ConsumableLotData consumableLotData = new ConsumableLotData();
                JSONObject jsonObject = array.getJSONObject(i);
                consumableLotData.setCONSUMABLEDEFID(jsonObject.getString(Constant.CONSUMABLEDEFID));
                consumableLotData.setCONSUMABLEDEFVERSION(jsonObject.getString(Constant.CONSUMABLEDEFVERSION));
                consumableLotData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                consumableLotData.setQTY(jsonObject.getString(Constant.QTY));
                consumableLotData.setCONSUMABLESTATE(jsonObject.getString(Constant.CONSUMABLESTATE));
                consumableLotData.setCREATEDQTY(jsonObject.getString(Constant.CREATEDQTY));
                consumableLotData.setCONSUMABLELOTID(jsonObject.getString(Constant.CONSUMABLELOTID));
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
                    + Constant.AREAID + ","
                    + Constant.AREANAME + ","
                    + Constant.AREATYPE + ","
                    + Constant.LOCATIONID + ","
                    + Constant.PLANTID + ","
                    + Constant.PARENTAREAID + ","
                    + Constant.PROCESSSEGMENTID + ","
                    + Constant.PROCESSSEGMENTTVERSION + ","
                    + Constant.VALIDSTATE + ")");
            AREA_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                AreaData areaData = new AreaData();
                JSONObject jsonObject = array.getJSONObject(i);
                areaData.setAREAID(jsonObject.getString(Constant.AREAID));
                areaData.setAREANAME(jsonObject.getString(Constant.AREANAME));
                areaData.setAREATYPE(jsonObject.getString(Constant.AREATYPE));
                areaData.setLOCATIONID(jsonObject.getString(Constant.LOCATIONID));
                areaData.setPLANTID(jsonObject.getString(Constant.PLANTID));
                areaData.setPARENTAREAID(jsonObject.getString(Constant.PARENTAREAID));
                areaData.setPROCESSSEGMENTID(jsonObject.getString(Constant.PROCESSSEGMENTID));
                areaData.setPROCESSSEGMENTTVERSION(jsonObject.getString(Constant.PROCESSSEGMENTTVERSION));
                areaData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
        statement.bindString(7, areaData.getPROCESSSEGMENTID());
        statement.bindString(8, areaData.getPROCESSSEGMENTTVERSION());
        statement.bindString(9, areaData.getVALIDSTATE());
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
                    + Constant.WAREHOUSEID + ","
                    + Constant.WAREHOUSENAME + ","
                    + Constant.WAREHOUSETYPE + ","
                    + Constant.VALIDSTATE + ")");
            WAREHOUSE_insert.append(" VALUES( ?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                WareHouseData wareHouseData = new WareHouseData();
                JSONObject jsonObject = array.getJSONObject(i);
                wareHouseData.setWAREHOUSEID(jsonObject.getString(Constant.WAREHOUSEID));
                wareHouseData.setWAREHOUSENAME(jsonObject.getString(Constant.WAREHOUSENAME));
                wareHouseData.setWAREHOUSETYPE(jsonObject.getString(Constant.WAREHOUSETYPE));
                wareHouseData.setVALIDSTATE(jsonObject.getString(Constant.VALIDSTATE));
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
                    + Constant.SHIPPINGPLANNO + ","
                    + Constant.POID + ","
                    + Constant.SHIPPINGPLANSEQ + ","
                    + Constant.CONTAINERSEQ + ","
                    + Constant.CONTAINERSPEC + ","
                    + Constant.PRODUCTDEFNAME + ","
                    + Constant.PLANQTY + ","
                    + Constant.LOADEDQTY + ","
                    + Constant.PLANSTARTTIME + ","
                    + Constant.PLANENDTIME + ","
                    + Constant.CUSTOMERID + ","
                    + Constant.WORKINGSHIFT + ","
                    + Constant.AREAID + ","
                    + Constant.STATE + ","
                    + Constant.ISOISAVE + ")");
            SHIPPINGPLAN_insert.append(" VALUES( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )");
            for (int i = 0; i < array.length(); i++) {
                ShippingPlanData shippingPlanData = new ShippingPlanData();
                JSONObject jsonObject = array.getJSONObject(i);
                shippingPlanData.setSHIPPINGPLANNO(jsonObject.getString(Constant.SHIPPINGPLANNO));
                shippingPlanData.setPOID(jsonObject.getString(Constant.POID));
                shippingPlanData.setSHIPPINGPLANSEQ(jsonObject.getString(Constant.SHIPPINGPLANSEQ));
                shippingPlanData.setCONTAINERSEQ(jsonObject.getString(Constant.CONTAINERSEQ));
                shippingPlanData.setCONTAINERSPEC(jsonObject.getString(Constant.CONTAINERSPEC));
                shippingPlanData.setPRODUCTDEFNAME(jsonObject.getString(Constant.PRODUCTDEFNAME));
                shippingPlanData.setPLANQTY(jsonObject.getString(Constant.PLANQTY));
                shippingPlanData.setLOADEDQTY(jsonObject.getString(Constant.LOADEDQTY));
                shippingPlanData.setPLANSTARTTIME(jsonObject.getString(Constant.PLANSTARTTIME));
                shippingPlanData.setPLANENDTIME(jsonObject.getString(Constant.PLANENDTIME));
                shippingPlanData.setCUSTOMERID(jsonObject.getString(Constant.CUSTOMERID));
                shippingPlanData.setWORKINGSHIFT(jsonObject.getString(Constant.WORKINGSHIFT));
                shippingPlanData.setAREAID(jsonObject.getString(Constant.AREAID));
                shippingPlanData.setSTATE(jsonObject.getString(Constant.STATE));
                updateTableSF_SHIPPINGPLAN(shippingPlanData, SHIPPINGPLAN_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("SHIPPINGPLAN更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_SHIPPINGPLAN(ShippingPlanData shippingPlanData, String SHIPPINGPLAN_insert) {
        SQLiteStatement statement = db.compileStatement(SHIPPINGPLAN_insert);
        statement.bindString(1, shippingPlanData.getSHIPPINGPLANNO());
        statement.bindString(2, shippingPlanData.getPOID());
        statement.bindString(3, shippingPlanData.getSHIPPINGPLANSEQ());
        statement.bindString(4, shippingPlanData.getCONTAINERSEQ());
        statement.bindString(5, shippingPlanData.getCONTAINERSPEC());
        statement.bindString(6, shippingPlanData.getPRODUCTDEFNAME());
        statement.bindString(7, shippingPlanData.getPLANQTY());
        statement.bindString(8, shippingPlanData.getLOADEDQTY());
        statement.bindString(9, shippingPlanData.getPLANSTARTTIME());
        statement.bindString(10, shippingPlanData.getPLANENDTIME());
        statement.bindString(11, shippingPlanData.getCUSTOMERID());
        statement.bindString(12, shippingPlanData.getWORKINGSHIFT());
        statement.bindString(13, shippingPlanData.getAREAID());
        statement.bindString(14, shippingPlanData.getSTATE());
        statement.bindString(15, "N");
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("SHIPPINGPLAN更新出错update", e.getMessage().toString());
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
                    + Constant.SHIPPINGPLANNO + ","
                    + Constant.SHIPPINGPLANSEQ + ","
                    + Constant.CONTAINERSEQ + ","
                    + Constant.QTY + ","
                    + Constant.SHIPPINGDATE + ","
                    + Constant.CONTAINERNO + ","
                    + Constant.SEALNO + ")");
            LOTSHIPPING_insert.append(" VALUES( ?, ?, ?, ? ,?, ?, ?, ?)");
            for (int i = 0; i < array.length(); i++) {
                LotShippingData lotShippingData = new LotShippingData();
                JSONObject jsonObject = array.getJSONObject(i);
                lotShippingData.setLOTID(jsonObject.getString(Constant.LOTID));
                lotShippingData.setSHIPPINGPLANNO(jsonObject.getString(Constant.SHIPPINGPLANNO));
                lotShippingData.setSHIPPINGPLANSEQ(jsonObject.getString(Constant.SHIPPINGPLANSEQ));
                lotShippingData.setCONTAINERSEQ(jsonObject.getString(Constant.CONTAINERSEQ));
                lotShippingData.setQTY(jsonObject.getString(Constant.QTY));
                lotShippingData.setCONTAINERNO(jsonObject.getString(Constant.CONTAINERNO));
                lotShippingData.setSEALNO(jsonObject.getString(Constant.SEALNO));
                lotShippingData.setSHIPPINGDATE(jsonObject.getString(Constant.SHIPPINGDATE));
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
        statement.bindString(3, lotShippingData.getSHIPPINGPLANSEQ());
        statement.bindString(4, lotShippingData.getCONTAINERSEQ());
        statement.bindString(5, lotShippingData.getQTY());
        statement.bindString(6, lotShippingData.getSHIPPINGDATE());
        statement.bindString(7, lotShippingData.getCONTAINERNO());
        statement.bindString(8, lotShippingData.getSEALNO());
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
                    + Constant.PURCHASEORDERID + ","
                    + Constant.LOTSTATE + ","
                    + Constant.RFID + ","
                    + Constant.QTY + ")");
            LOT_insert.append(" VALUES( ?, ?, ?, ?,?)");
            for (int i = 0; i < array.length(); i++) {
                LotData lotdata = new LotData();
                JSONObject jsonObject = array.getJSONObject(i);
                lotdata.setLOTID(jsonObject.getString(Constant.LOTID));
                lotdata.setPURCHASEORDERID(jsonObject.getString(Constant.PURCHASEORDERID));
                lotdata.setLOTSTATE(jsonObject.getString(Constant.LOTSTATE));
                lotdata.setRFID(jsonObject.getString(Constant.RFID));
                lotdata.setQTY(jsonObject.getString(Constant.QTY));
                updateTableSF_LOT(lotdata, LOT_insert.toString());
            }
        } catch (JSONException e) {
            Log.e("LOT更新出错", e.getMessage().toString());
        }
    }

    private void updateTableSF_LOT(LotData lotData, String LOTSHIPPING_insert) {
        SQLiteStatement statement = db.compileStatement(LOTSHIPPING_insert);
        statement.bindString(1, lotData.getLOTID());
        statement.bindString(2, lotData.getPURCHASEORDERID());
        statement.bindString(3, lotData.getLOTSTATE());
        statement.bindString(4, lotData.getRFID());
        statement.bindString(5, lotData.getQTY());
        try {
            statement.executeInsert();
        } catch (Exception e) {
            Log.e("LOT更新出错", e.getMessage().toString());
        }
    }

    //endregion

    public void updateSHIPPINGSAVE(String resultStr) {
        Log.e("更新SHIPPINGSAVE", resultStr);
        try {
            JSONArray array = new JSONArray(resultStr);
            for (int i = 0; i < array.length(); i++) {
                JSONObject jsonObject = array.getJSONObject(i);
                ContentValues values = new ContentValues();
                values.put(ISOISAVE, "Y");
                db.update(Constant.SF_SHIPPINGPLAN, values, "SHIPPINGPLANNO = ?", new String[]{jsonObject.getString(Constant.SHIPPINGPLANNO)});
            }
        } catch (JSONException e) {
            Log.e("SHIPPINGSAVE更新出错", e.getMessage().toString());
        }
    }
}