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
                                String ShippingPlanData1 = Constant.UPLOADSHIPPINGPLANINFO + ShippingPlanData.getBytes("UTF8").length;
                                Log.e("准备上传ShippingPlan", ShippingPlanData1);
                                out.write(ShippingPlanData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADSHIPPINGPLAN:
                                Log.e("上传ShippingPlan", ShippingPlanData);
                                out.write((ShippingPlanData).getBytes("UTF8"));
                                out.flush();
                                break;
                            case Constant.UPLOADSHIPPINGPLANDETAILINFO:
                                ShippingPlanDetailData = getShippingPlanDetailData();
                                String ShippingPlanDetailData1 = Constant.UPLOADSHIPPINGPLANDETAILINFO + ShippingPlanDetailData.getBytes("UTF8").length;
                                Log.e("准备上传ShippingPlanDetail", ShippingPlanDetailData1);
                                out.write(ShippingPlanDetailData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADSHIPPINGPLANDETAIL:
                                Log.e("上传ShippingPlanDetail", ShippingPlanDetailData);
                                out.write((ShippingPlanDetailData).getBytes("UTF8"));
                                out.flush();
                                break;
                            case Constant.UPLOADLOTSHIPPINGINFO:
                                LotShippingdata = getLotShippingData();
                                String LotShippingData1 = Constant.UPLOADLOTSHIPPINGINFO + LotShippingdata.getBytes("UTF8").length;
                                Log.e("准备上传LotShipping", LotShippingData1);
                                out.write(LotShippingData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADLOTSHIPPING:
                                Log.e("上传LotShipping", LotShippingdata);
                                out.write((LotShippingdata).getBytes("UTF8"));
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
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.checkShippingIsAllUpload));
        Log.e("checkIsAllUpload", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist != null) {
            if (cursorDatalist.getCount() != 0) {
                Log.e("查到了", "111111111");
                while (cursorDatalist.moveToNext()) {
                    if (!cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.COUNT)).equals("0"))
                        returnflag = true;
                }
            } else {
                Log.e("没有符合条件的", "111111111");
            }
        }else {
            returnflag = false;
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
                shippingPlanData.setSHIPPINGPLANNO(getcursorData(cursorDatalist, Constant.SHIPPINGPLANNO));
                shippingPlanData.setCUSTOMERID(getcursorData(cursorDatalist, Constant.CUSTOMERID));
                shippingPlanData.setBOOKINGNO(getcursorData(cursorDatalist, Constant.BOOKINGNO));
                shippingPlanData.setPLANDATE(getcursorData(cursorDatalist, Constant.PLANDATE));
                shippingPlanData.setSHIPPINGPLANDATE(getcursorData(cursorDatalist, Constant.SHIPPINGPLANDATE));
                shippingPlanData.setSHIPPINGENDPLANDATE(getcursorData(cursorDatalist, Constant.SHIPPINGENDPLANDATE));
                shippingPlanData.setSHIPPINGENDDATE(getcursorData(cursorDatalist, Constant.SHIPPINGENDDATE));
                shippingPlanData.setSTATE(getcursorData(cursorDatalist, Constant.STATE));
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

                uploadshipplandetail.setSHIPPINGPLANNO(getcursorData(cursorDatalist, Constant.SHIPPINGPLANNO));
                uploadshipplandetail.setSHIPPINGPLANSEQ(getcursorData(cursorDatalist, Constant.SHIPPINGPLANSEQ));
                uploadshipplandetail.setORDERTYPE(getcursorData(cursorDatalist, Constant.ORDERTYPE));
                uploadshipplandetail.setORDERNO(getcursorData(cursorDatalist, Constant.ORDERNO));
                uploadshipplandetail.setLINENO(getcursorData(cursorDatalist, Constant.LINENO));
                uploadshipplandetail.setPRODUCTDEFID(getcursorData(cursorDatalist, Constant.PRODUCTDEFID));
                uploadshipplandetail.setPRODUCTDEFVERSION(getcursorData(cursorDatalist, Constant.PRODUCTDEFVERSION));
                uploadshipplandetail.setPRODUCTDEFNAME(getcursorData(cursorDatalist, Constant.PRODUCTDEFNAME));
                uploadshipplandetail.setCONTAINERSEQ(getcursorData(cursorDatalist, Constant.CONTAINERSEQ));
                uploadshipplandetail.setPOID(getcursorData(cursorDatalist, Constant.POID));
                uploadshipplandetail.setCONTAINERSPEC(getcursorData(cursorDatalist, Constant.CONTAINERSPEC));
                uploadshipplandetail.setCONTAINERNO(getcursorData(cursorDatalist, Constant.CONTAINERNO));
                uploadshipplandetail.setSEALNO(getcursorData(cursorDatalist, Constant.SEALNO));
                uploadshipplandetail.setCOMPLETETIME(getcursorData(cursorDatalist, Constant.COMPLETETIME));
                uploadshipplandetail.setPLANQTY(getcursorData(cursorDatalist, Constant.PLANQTY));
                uploadshipplandetail.setLOADEDQTY(getcursorData(cursorDatalist, Constant.LOADEDQTY));
                shipplanlist.add(uploadshipplandetail);
                Log.e("查到的detail信息", uploadshipplandetail.toString());
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
                data.setLOTID(getcursorData(cursorDatalist, Constant.LOTID));
                data.setSHIPPINGPLANNO(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANNO)));
                data.setSHIPPINGPLANSEQ(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGPLANSEQ)));
                data.setCONTAINERSEQ(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONTAINERSEQ)));
                data.setSHIPPINGDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.SHIPPINGDATE)));
                data.setPOID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.POID)));
                data.setVALIDSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.VALIDSTATE)));
                data.setQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                data.setORDERTYPE(getcursorData(cursorDatalist, Constant.ORDERTYPE));
                data.setORDERNO(getcursorData(cursorDatalist, Constant.ORDERNO));
                data.setLINENO(getcursorData(cursorDatalist, Constant.LINENO));
                data.setPRODUCTDEFID(getcursorData(cursorDatalist, Constant.PRODUCTDEFID));
                data.setPRODUCTDEFVERSION(getcursorData(cursorDatalist, Constant.PRODUCTDEFVERSION));
                data.setPRODUCTDEFNAME(getcursorData(cursorDatalist, Constant.PRODUCTDEFNAME));
                data.setTRACKOUTTIME(getcursorData(cursorDatalist, Constant.TRACKOUTTIME));
                lotshiplist.add(data);
                Log.e("上传的shippinglot数据", data.toString());
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(lotshiplist);
        return a;
    }

    private String getcursorData(Cursor cursorDatalist, String ColumnIndex) {
        String returnstr = "";
        String a = cursorDatalist.getString(cursorDatalist.getColumnIndex(ColumnIndex));
        if (a != null) {
            returnstr = a;
        }
        return returnstr;
    }

}