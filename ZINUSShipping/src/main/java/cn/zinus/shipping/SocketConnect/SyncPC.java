package cn.zinus.shipping.SocketConnect;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import cn.zinus.shipping.Fragment.Event;
import cn.zinus.shipping.JaveBean.LotShippingData;
import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.JaveBean.ShippingPlanDetailData;
import cn.zinus.shipping.R;
import cn.zinus.shipping.util.Constant;
import cn.zinus.shipping.util.DBManger;
import cn.zinus.shipping.util.MyDateBaseHelper;

/**
 * Developer:Spring
 * DataTime :2017/9/13 11:58
 * Main Change:
 */

public class SyncPC implements Runnable {

    private Socket client;
    private ArrayList<?> list;
    private UpdateSqlite mUpdateSqlite;
    BufferedOutputStream out;
    BufferedInputStream in;
    MyDateBaseHelper mHelper;
    private SQLiteDatabase db;
    Context mContext;

    //上传的字符串
    String ShippingPlanData;
    String LotShippingdata;
    String ShippingPlanDetailData;

    public SyncPC(Socket client, ArrayList<?> list, Context mContext) {
        this.client = client;
        this.list = list;
        this.mContext = mContext;
        this.mUpdateSqlite = new UpdateSqlite(mContext);
        this.mHelper = DBManger.getIntance(mContext);
        this.db = mHelper.getWritableDatabase();
    }

    private String getJsonStr() {
        Log.e("msgmsg", "一次连接监听");
        try {
            in = new BufferedInputStream(client.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        String currCMD = readCMDFromSocket(in);
        String flag = currCMD.substring(0, Constant.SOCKETLENGTH);
        String resultStr = currCMD.substring(Constant.SOCKETLENGTH);
        Log.e("msgmsg", flag + ":" + resultStr);
        return resultStr;
    }

    @Override
    public void run() {

        try {
            //与pc端通信
            String currCMD = "";
            //输入输出流
            Log.e("shipping", "开始监听11111");
            out = new BufferedOutputStream(client.getOutputStream());
            in = new BufferedInputStream(client.getInputStream());

            //开启一个循环
            while (true) {
                try {
                    if (!client.isConnected()) {
                        //连接不成功的时候跳出循环
                        Log.e("shipping", "连接不成功的时候跳出循环");
                        break;
                    }
                    //连接成功
                    Log.e("shipping", "一次连接监听");
                    currCMD = readCMDFromSocket(in);
                    String flag = currCMD.substring(0, Constant.SOCKETLENGTH);
                    String resultStr = currCMD.substring(Constant.SOCKETLENGTH);
                    Log.e("shipping只有标志位+长度", flag + ":" + resultStr);
                    //currCMD是pc传过来的数据
                    if (flag.equals(Constant.UPDATEEXIT)) {
                        EventBus.getDefault().post(new Event.RefreshActivityEvent());
                        break;
                    } else if (flag.equals(Constant.UPLOADEXIT)) {
                        out.write(Constant.UPLOADEXIT.getBytes());
                        out.flush();
                        Log.e("结束上传", "结束");
                        break;
                    } else {
                        switch (flag) {

                            //region updateShipping
                            case Constant.UPDATESHIPPINGSTART:
                                //判断是否有未上传的内容
                                if (checkShippingIsAllUpload()) {
                                    out.write((Constant.UPDATEEXIT + Constant.PDANOTALUPLOAD).getBytes());
                                    out.flush();
                                } else {
                                    out.write((Constant.SYNCSF_SHIPPINGPLAN + Constant.PDAALUPLOAD).getBytes());
                                    out.flush();
                                }
                                break;
                            case Constant.SYNCSF_SHIPPINGPLAN:
                                int lengthSF_SHIPPINGPLAN = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_SHIPPINGPLAN.getBytes());
                                out.flush();
                                Log.e("更新SF_SHIPPINGPLAN", resultStr);
                                String strSF_SHIPPINGPLAN = receiveFileFromSocket(in, out, lengthSF_SHIPPINGPLAN);
                                Log.e("更新SF_SHIPPINGPLAN", strSF_SHIPPINGPLAN.length() + ":" + strSF_SHIPPINGPLAN);
                                mUpdateSqlite.updateShippingPlan(strSF_SHIPPINGPLAN);
                                out.write(Constant.SYNCSF_SHIPPINGPLANDETAIL.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_SHIPPINGPLANDETAIL:
                                int lengthSF_SHIPPINGPLANdetail = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_SHIPPINGPLANDETAIL.getBytes());
                                out.flush();
                                Log.e("更新SF_SHIPPINGPLANdetail", resultStr);
                                String strSF_SHIPPINGPLANdetail = receiveFileFromSocket(in, out, lengthSF_SHIPPINGPLANdetail);
                                Log.e("更新SF_SHIPPINGPLANdetail", strSF_SHIPPINGPLANdetail.length() + ":" + strSF_SHIPPINGPLANdetail);
                                mUpdateSqlite.updateShippingPlanDetail(strSF_SHIPPINGPLANdetail);
                                out.write(Constant.SYNCSF_LOTSHIPPING.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_LOTSHIPPING:
                                int lengthSF_LOTSHIPPING = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_LOTSHIPPING.getBytes());
                                out.flush();
                                Log.e("更新SF_SHIPPINGLOT", resultStr);
                                String strSF_LOTSHIPPING = receiveFileFromSocket(in, out, lengthSF_LOTSHIPPING);
                                Log.e("更新SF_SHIPPINGLOT", strSF_LOTSHIPPING.length() + ":" + strSF_LOTSHIPPING);
                                mUpdateSqlite.updateShippingLot(strSF_LOTSHIPPING);
                                out.write(Constant.SYNCSF_LOT.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_LOT:
                                int lengthSF_LOT = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_LOT.getBytes());
                                out.flush();
                                Log.e("更新SF_LOT", resultStr);
                                String strSF_LOT = receiveFileFromSocket(in, out, lengthSF_LOT);
                                Log.e("更新SF_LOT", strSF_LOT.length() + ":" + strSF_LOT);
                                mUpdateSqlite.updateLot(strSF_LOT);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            //endregion

                            //region uploadShipping
                            case Constant.UPLOADSHIPPINGSTART:
                                ShippingPlanData = getShippingPlanData();
                                String ShippingPlanData1 = Constant.UPLOADSHIPPINGPLANINFO + ShippingPlanData.getBytes().length;
                                Log.e("准备上传ShippingPlan", ShippingPlanData1);
                                out.write(ShippingPlanData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADSHIPPINGPLAN:
                                Log.e("上传ShippingPlan", ShippingPlanData);
                                out.write((Constant.UPLOADSHIPPINGPLAN + ShippingPlanData).getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADSHIPPINGPLANDETAILINFO:
                                ShippingPlanDetailData = getShippingPlanDetailData();
                                String ShippingPlanDetailData1 = Constant.UPLOADSHIPPINGPLANDETAILINFO + ShippingPlanDetailData.getBytes().length;
                                Log.e("准备上传ShippingPlan", ShippingPlanDetailData1);
                                out.write(ShippingPlanDetailData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADSHIPPINGPLANDETAIL:
                                Log.e("上传ShippingPlan", ShippingPlanDetailData);
                                out.write((Constant.UPLOADSHIPPINGPLANDETAIL + ShippingPlanDetailData).getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADLOTSHIPPINGINFO:
                                LotShippingdata = getLotShippingData();
                                String LotShippingData1 = Constant.UPLOADLOTSHIPPINGINFO + LotShippingdata.getBytes().length;
                                Log.e("准备上传LotShipping", LotShippingData1);
                                out.write(LotShippingData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADLOTSHIPPING:
                                Log.e("上传LotShipping", LotShippingdata);
                                out.write((Constant.UPLOADLOTSHIPPING + LotShippingdata).getBytes());
                                out.flush();
                                break;
                            //endregion

                            //region 返回出货保存的plan号,修改状态
                            case Constant.RETURNSHIPPINGSAVESTART:
                                out.write(Constant.SYNCPDASAVING.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCPDASAVING:
                                int lengthSHIPPINGSAVE = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCPDASAVING.getBytes());
                                out.flush();
                                Log.e("更新SHIPPINGSAVE", resultStr);
                                String strSHIPPINGSAVE = receiveFileFromSocket(in, out, lengthSHIPPINGSAVE);
                                Log.e("更新SHIPPINGSAVE", strSHIPPINGSAVE.length() + ":" + strSHIPPINGSAVE);
                                mUpdateSqlite.updateSHIPPINGSAVE(strSHIPPINGSAVE);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            //endregion
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    break;
                }
            }
            out.close();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (client != null) {
                    client.close();
                    Log.e("shipping", "结束关闭client");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /* 读取命令 */
    public String readCMDFromSocket(InputStream in) {
        int MAX_BUFFER_BYTES = 1024 * 1024;
        String msg = "";
        byte[] tempbuffer = new byte[MAX_BUFFER_BYTES];
        try {
            int numReadedBytes = in.read(tempbuffer, 0, tempbuffer.length);
            msg = new String(tempbuffer, 0, numReadedBytes, "utf-8");
            tempbuffer = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return msg;
    }

    public static String receiveFileFromSocket(InputStream in, OutputStream out, int length) {
        String returnstr = "";
        byte[] filebytes = null;// 文件数据
        try {
            int filelen = length;// 文件长度
            filebytes = new byte[filelen];
            int pos = 0;
            int rcvLen = 0;
            while ((rcvLen = in.read(filebytes, pos, filelen - pos)) > 0) {
                pos += rcvLen;
            }
            Log.e("1111111", Thread.currentThread().getName()
                    + "---->" + "read file OK:file size=" + filebytes.length);
            returnstr = new String(filebytes, "UTF8");
            Log.e("read file长度", returnstr.length() + "");
        } catch (Exception e) {
            Log.e("1111111", Thread.currentThread().getName()
                    + "---->" + "receiveFileFromSocket error");
            e.printStackTrace();
        }
        return returnstr;

    }

    private boolean checkShippingIsAllUpload() {
        boolean returnflag = false;
        ArrayList<ShippingPlanData> shipplanlist = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.checkShippingIsAllUpload));
        Log.e("checkIsAllUpload", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                if (!cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.COUNT)).equals("0"))
                    returnflag = true;
            }
        }
        return returnflag;
    }

    private String getShippingPlanData() {
        String a = "";
        ArrayList<ShippingPlanData> shipplanlist = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadShipplanQuery));
        Log.e("GetUploadShipplanQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                ShippingPlanData shippingPlanData = new ShippingPlanData();
                shippingPlanData.setSHIPPINGPLANNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANNO)));
                shippingPlanData.setCUSTOMERID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CUSTOMERID)));
                shippingPlanData.setBOOKINGNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.BOOKINGNO)));
                shippingPlanData.setPLANDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PLANDATE)));
                shippingPlanData.setSHIPPINGPLANDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANDATE)));
                shippingPlanData.setSHIPPINGENDPLANDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGENDPLANDATE)));
                shippingPlanData.setSHIPPINGENDDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGENDDATE)));
                shippingPlanData.setSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STATE)));
                //SHIPPINGPLANNO ,PLANTID,CUSTOMERID,BOOKINGNO,PLANDATE,SHIPPINGPLANDATE,SHIPPINGENDPLANDATE,SHIPPINGENDDATE,STATE
                Log.e("查到了shippingPlanData", shippingPlanData.toString());
                shipplanlist.add(shippingPlanData);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(shipplanlist);
        return a;
    }

    private String getShippingPlanDetailData() {
        String a = "";
        ArrayList<ShippingPlanDetailData> shipplanlist = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadShipplanDetailQuery));
        Log.e("UpShipplanDetailQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                ShippingPlanDetailData uploadshipplandetail = new ShippingPlanDetailData();
                uploadshipplandetail.setSHIPPINGPLANNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANNO)));
                uploadshipplandetail.setSHIPPINGPLANSEQ(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANSEQ)));
                uploadshipplandetail.setORDERTYPE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERTYPE)));
                uploadshipplandetail.setORDERNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERNO)));
                uploadshipplandetail.setLINENO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LINENO)));
                uploadshipplandetail.setPRODUCTDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFID)));
                uploadshipplandetail.setPRODUCTDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFVERSION)));
                uploadshipplandetail.setPRODUCTDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFNAME)));
                uploadshipplandetail.setCONTAINERSEQ(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERSEQ)));
                uploadshipplandetail.setPOID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.POID)));
                uploadshipplandetail.setCONTAINERSPEC(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERSPEC)));
                uploadshipplandetail.setCONTAINERNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERNO)));
                uploadshipplandetail.setSEALNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SEALNO)));
                uploadshipplandetail.setCOMPLETETIME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.COMPLETETIME)));
                uploadshipplandetail.setPLANQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PLANQTY)));
                uploadshipplandetail.setLOADEDQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LOADEDQTY)));
//                SHIPPINGPLANNO,SHIPPINGPLANSEQ,CONTAINERSEQ     \n
//                        ,POID,CONTAINERSPEC,CONTAINERNO,SEALNO     \n
//                        ,COMPLETETIME,PLANQTY,     \n
//                ORDERTYPE,ORDERNO,LINENO     \n
                shipplanlist.add(uploadshipplandetail);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(shipplanlist);
        return a;
    }

    private String getLotShippingData() {
        String a = "";
        ArrayList<LotShippingData> lotshiplist = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadlOTShipQuery));
        Log.e("GetUploadLOTShipQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                LotShippingData data = new LotShippingData();
                data.setLOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LOTID)));
                data.setSHIPPINGPLANNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANNO)));
                data.setSHIPPINGPLANSEQ(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANSEQ)));
                data.setCONTAINERSEQ(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERSEQ)));
                data.setSHIPPINGDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGDATE)));
                data.setPOID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.POID)));
                data.setVALIDSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.VALIDSTATE)));
                data.setQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                data.setLINENO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.LINENO)));
                data.setPRODUCTDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFID)));
                data.setPRODUCTDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFNAME)));
                data.setPRODUCTDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.PRODUCTDEFVERSION)));
                data.setORDERNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERNO)));
                data.setORDERTYPE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ORDERTYPE)));
                data.setTRACKOUTTIME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.TRACKOUTTIME)));
                lotshiplist.add(data);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(lotshiplist);
        return a;
    }

}















/*


    string SHIPPINGPLANNO;
    string SHIPPINGPLANSEQ;
    string CONTAINERSEQ;
    string POID;
    string LOTID;
    string QTY;
    string SHIPPINGDATE;
    string PLANQTY;
    string ORDERTYPE;
    string ORDERNO;
    string LINENO;
    string PRODUCTDEFID;
    string PRODUCTDEFNAME;
    string PRODUCTDEFVERSION;



 tblDatas.Columns.Add("SHIPPINGPLANNO", Type.GetType("System.String"));
 tblDatas.Columns.Add("SHIPPINGPLANSEQ", Type.GetType("System.String"));
 tblDatas.Columns.Add("CONTAINERSEQ", Type.GetType("System.String"));
 tblDatas.Columns.Add("POID", Type.GetType("System.String"));
 tblDatas.Columns.Add("LOTID", Type.GetType("System.String"));
 tblDatas.Columns.Add("QTY", Type.GetType("System.String"));
 tblDatas.Columns.Add("SHIPPINGDATE", Type.GetType("System.String"));
 tblDatas.Columns.Add("PLANQTY", Type.GetType("System.String"));
 tblDatas.Columns.Add("ORDERTYPE", Type.GetType("System.String"));
 tblDatas.Columns.Add("ORDERNO", Type.GetType("System.String"));
 tblDatas.Columns.Add("LINENO", Type.GetType("System.String"));
 tblDatas.Columns.Add("PRODUCTDEFID", Type.GetType("System.String"));
 tblDatas.Columns.Add("PRODUCTDEFNAME", Type.GetType("System.String"));
 tblDatas.Columns.Add("PRODUCTDEFVERSION", Type.GetType("System.String"));


SHIPPINGPLANNO = ((JObject)item)["SHIPPINGPLANNO"].ToString();
SHIPPINGPLANSEQ = ((JObject)item)["SHIPPINGPLANSEQ"].ToString();
CONTAINERSEQ = ((JObject)item)["CONTAINERSEQ"].ToString();
POID = ((JObject)item)["POID"].ToString();
LOTID = ((JObject)item)["LOTID"].ToString();
QTY = ((JObject)item)["QTY"].ToString();
SHIPPINGDATE = ((JObject)item)["SHIPPINGDATE"].ToString();
PLANQTY = ((JObject)item)["PLANQTY"].ToString();
ORDERTYPE = ((JObject)item)["ORDERTYPE"].ToString();
ORDERNO = ((JObject)item)["ORDERNO"].ToString();
LINENO = ((JObject)item)["LINENO"].ToString();
PRODUCTDEFID = ((JObject)item)["PRODUCTDEFID"].ToString();
PRODUCTDEFNAME = ((JObject)item)["PRODUCTDEFNAME"].ToString();
PRODUCTDEFVERSION = ((JObject)item)["PRODUCTDEFVERSION"].ToString();






*/

















