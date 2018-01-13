package cn.zinus.warehouse.SocketConnect;

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

import cn.zinus.warehouse.Fragment.Event;
import cn.zinus.warehouse.JaveBean.UploadConsumeInbound;
import cn.zinus.warehouse.JaveBean.UploadConsumeLotInbound;
import cn.zinus.warehouse.JaveBean.UploadInboundOrder;
import cn.zinus.warehouse.JaveBean.UploadStockCheckData;
import cn.zinus.warehouse.JaveBean.UploadStockCheckDetail;
import cn.zinus.warehouse.JaveBean.UploadStockLotCheckDetail;
import cn.zinus.warehouse.R;
import cn.zinus.warehouse.util.Constant;
import cn.zinus.warehouse.util.DBManger;
import cn.zinus.warehouse.util.MyDateBaseHelper;

import static cn.zinus.warehouse.util.DBManger.getCursorData;

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
    //上传字符串
    String inboundOrderData;
    String consumeInboundData;
    String consumeLotInboundData;
    String stockcheckData;
    String stockcheckDetailData;
    String stocklotcheckDetailData;

    public SyncPC(Socket client, ArrayList<?> list, Context mContext) {
        this.client = client;
        this.list = list;
        this.mContext = mContext;
        mUpdateSqlite = new UpdateSqlite(mContext);
        this.mHelper = DBManger.getIntance(mContext);
        this.db = mHelper.getWritableDatabase();
    }

    @Override
    public void run() {

        try {
            //与pc端通信
            String currCMD = "";
            //输入输出流
            Log.e("warehouse", "开始监听11111");
            out = new BufferedOutputStream(client.getOutputStream());
            in = new BufferedInputStream(client.getInputStream());
            //开启一个循环
            while (true) {
                try {
                    if (!client.isConnected()) {
                        //连接不成功的时候跳出循环
                        Log.e("warehouse", "连接不成功的时候跳出循环");
                        break;
                    }
                    //连接成功

                    Log.e("warehouse", "一次连接监听");
                    currCMD = readCMDFromSocket(in);
                    String flag = currCMD.substring(0, Constant.SOCKETLENGTH);
                    String resultStr = currCMD.substring(Constant.SOCKETLENGTH);
                    Log.e("warehouse只有标志位+长度", flag + ":" + resultStr);
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

                            //region 入库相关

                            //region UpdateStockin
                            case Constant.UPDATESTOCKINSTART:
                                if (checkStockInboundAllUpload()) {
                                    Log.e("不能更新", "不能更新");
                                    out.write((Constant.UPDATEEXIT + Constant.PDANOTALUPLOAD).getBytes());
                                    out.flush();
                                } else {
                                    Log.e("可以更新", "可以更新");
                                    out.write((Constant.SYNCSF_INBOUNDORDER + Constant.PDAALUPLOAD).getBytes());
                                    out.flush();
                                }
                                break;
                            case Constant.SYNCSF_INBOUNDORDER:
                                /**
                                 * 1:给pc发更新表的消息
                                 * 2:pc传标志位加长度
                                 * 3:给pc发消息(做好准备更新)
                                 * 4:pc传更新字符串
                                 * 5:更新
                                 * 6:给pc发更新下一个表的消息...
                                 */
                                int lengthSF_INBOUNDORDER = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_INBOUNDORDER.getBytes());
                                out.flush();
                                Log.e("更新SF_INBOUNDORDER", resultStr);
                                String strSF_INBOUNDORDER = receiveFileFromSocket(in, out, lengthSF_INBOUNDORDER);
                                Log.e("更新SF_INBOUNDORDER", strSF_INBOUNDORDER.length() + ":" + strSF_INBOUNDORDER);
                                mUpdateSqlite.updateInboundOrder(strSF_INBOUNDORDER);
                                out.write(Constant.SYNCSF_CONSUMEINBOUND.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_CONSUMEINBOUND:
                                int lengthSF_CONSUMEINBOUND = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_CONSUMEINBOUND.getBytes());
                                out.flush();
                                Log.e("更新SF_CONSUMEINBOUND", resultStr);
                                String strSF_CONSUMEINBOUND = receiveFileFromSocket(in, out, lengthSF_CONSUMEINBOUND);
                                Log.e("更新SF_CONSUMEINBOUND", strSF_CONSUMEINBOUND.length() + ":" + strSF_CONSUMEINBOUND);
                                mUpdateSqlite.updateConsumeInbound(strSF_CONSUMEINBOUND);
                                out.write(Constant.SYNCSF_CONSUMELOTINBOUND.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_CONSUMELOTINBOUND:
                                int lengthSF_CONSUMELOTINBOUND = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_CONSUMELOTINBOUND.getBytes());
                                out.flush();
                                Log.e("更新SF_CONSUMELOTINBOUND", resultStr);
                                String strSF_CONSUMELOTINBOUND = receiveFileFromSocket(in, out, lengthSF_CONSUMELOTINBOUND);
                                Log.e("更新SF_CONSUMELOTINBOUND", strSF_CONSUMELOTINBOUND.length() + ":" + strSF_CONSUMELOTINBOUND);
                                mUpdateSqlite.updateConsumeLotInbound(strSF_CONSUMELOTINBOUND);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            //endregion

                            //region UploadStockin
                            case Constant.UPLOADSTOCKINSTART:
                                inboundOrderData = getInboundOrderData();
                                String inboundOrderData1 = Constant.UPLOADINBOUNDORDERINFO + inboundOrderData.getBytes("UTF8").length;
                                Log.e("准备上传inboundorder", inboundOrderData1);
                                out.write(inboundOrderData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADINBOUNDORDER:
                                Log.e("上传inboundorder", inboundOrderData);
                                out.write(inboundOrderData.getBytes("UTF8"));
                                out.flush();
                                break;
                            case Constant.UPLOADCONSUMEINBOUNDINFO:
                                consumeInboundData = getConsumeInboundData();
                                String consumeInboundData1 = Constant.UPLOADCONSUMEINBOUNDINFO + consumeInboundData.getBytes("UTF8").length;
                                Log.e("准备上传consumeInbound", consumeInboundData1);
                                out.write(consumeInboundData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADCONSUMEINBOUND:
                                Log.e("上传consumeInbound", consumeInboundData);
                                out.write(consumeInboundData.getBytes("UTF8"));
                                out.flush();
                                break;
                            case Constant.UPLOADCONSUMELOTINBOUNDINFO:
                                consumeLotInboundData = getConsumeLotInboundData();
                                String consumeLotInboundData1 = Constant.UPLOADCONSUMELOTINBOUNDINFO + consumeLotInboundData.getBytes("UTF8").length;
                                Log.e("准备上传consumeLotInbound", consumeLotInboundData1);
                                out.write(consumeLotInboundData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADCONSUMELOTINBOUND:
                                Log.e("上传consumeLotInbound", consumeLotInboundData);
                                out.write(consumeLotInboundData.getBytes("UTF8"));
                                out.flush();
                                break;
                            //endregion

                            //region 从pda上删除保存好的入库数据
                            case Constant.RETURNSTOCKINSAVESTART:
                                Log.e("删除入库保存好的数据", resultStr);
                                out.write(Constant.SYNCPDASTOCKINSAVING.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCPDASTOCKINSAVING:
                                int lengthSTOCKINSAVE = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCPDASTOCKINSAVING.getBytes());
                                out.flush();
                                Log.e("更新STOCKINSAVE", resultStr);
                                String STOCKETINSAVE = receiveFileFromSocket(in, out, lengthSTOCKINSAVE);
                                Log.e("更新STOCKINSAVE", STOCKETINSAVE.length() + ":" + STOCKETINSAVE);
                                mUpdateSqlite.updateSTOCKINSAVE(STOCKETINSAVE);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            //endregion

                            //endregion

                            //region UpdateStockout
                            case Constant.UPDATESTOCKOUTSTART:
                                /**
                                 * 收到开始的标志以后,按顺序更新数据库表:
                                 * SF_CONSUMEREQUEST-->SF_CONSUMEOUTBOUND-->SF_CONSUMELOTOUTBOUND
                                 * 更完最后一个表传更新结束的标志
                                 */
                                out.write(Constant.SYNCSF_CONSUMEREQUEST.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_CONSUMEREQUEST:
                                int lengthSF_CONSUMEREQUEST = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_CONSUMEREQUEST.getBytes());
                                out.flush();
                                Log.e("更新SF_CONSUMEREQUEST", resultStr);
                                String strSF_CONSUMEREQUEST = receiveFileFromSocket(in, out, lengthSF_CONSUMEREQUEST);
                                Log.e("更新SF_CONSUMEREQUEST", strSF_CONSUMEREQUEST.length() + ":" + strSF_CONSUMEREQUEST);
                                mUpdateSqlite.updateConsumeRequest(strSF_CONSUMEREQUEST);
                                out.write(Constant.SYNCSF_CONSUMEOUTBOUND.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_CONSUMEOUTBOUND:
                                int lengthSF_CONSUMEOUTBOUND = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_CONSUMEOUTBOUND.getBytes());
                                out.flush();
                                Log.e("更新SF_CONSUMEOUTBOUND", resultStr);
                                String strSF_CONSUMEOUTBOUND = receiveFileFromSocket(in, out, lengthSF_CONSUMEOUTBOUND);
                                Log.e("更新SF_CONSUMEOUTBOUND", strSF_CONSUMEOUTBOUND.length() + ":" + strSF_CONSUMEOUTBOUND);
                                mUpdateSqlite.updateConsumeOutbound(strSF_CONSUMEOUTBOUND);
                                out.write(Constant.SYNCSF_CONSUMELOTOUTBOUND.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_CONSUMELOTOUTBOUND:
                                int lengthSF_CONSUMELOTOUTBOUND = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_CONSUMELOTOUTBOUND.getBytes());
                                out.flush();
                                Log.e("更新SF_CONSUMELOTOUTBOUND", resultStr);
                                String strSF_CONSUMELOTOUTBOUND = receiveFileFromSocket(in, out, lengthSF_CONSUMELOTOUTBOUND);
                                Log.e("更新SF_CONSUMELOTOUTBOUND", strSF_CONSUMELOTOUTBOUND.length() + ":" + strSF_CONSUMELOTOUTBOUND);
                                mUpdateSqlite.updateConsumeLotOutbound(strSF_CONSUMELOTOUTBOUND);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            //endregion

                            //region 盘点相关

                            //region UpdateStockcheck
                            case Constant.UPDATESTOCKCHECKSTART:
                                /**
                                 * 收到开始的标志以后,按顺序更新数据库表:
                                 * SYNCSF_STOCKCHECK-->SYNCSF_STOCKCHECKDETAIL-->SYNCSF_STOCKLOTCHECKDETAIL
                                 * 更完最后一个表传更新结束的标志
                                 */
                                if (checkStockCheckIsAllUpload()) {
                                    Log.e("不能更新", "不能更新");
                                    out.write((Constant.UPDATEEXIT + Constant.PDANOTALUPLOAD).getBytes());
                                    out.flush();
                                } else {
                                    Log.e("可以更新", "可以更新");
                                    out.write((Constant.SYNCSF_STOCKCHECK + Constant.PDAALUPLOAD).getBytes());
                                    out.flush();
                                }
                                break;
                            case Constant.SYNCSF_STOCKCHECK:
                                int lengthSF_STOCKCHECK = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_STOCKCHECK.getBytes());
                                out.flush();
                                Log.e("更新SF_STOCKCHECK", resultStr);
                                String strSF_STOCKCHECK = receiveFileFromSocket(in, out, lengthSF_STOCKCHECK);
                                Log.e("更新SF_STOCKCHECK", strSF_STOCKCHECK.length() + ":" + strSF_STOCKCHECK);
                                mUpdateSqlite.updateStockCheck(strSF_STOCKCHECK);
                                out.write(Constant.SYNCSF_STOCKCHECKDETAIL.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_STOCKCHECKDETAIL:
                                int lengthSF_STOCKCHECKDETAIL = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_STOCKCHECKDETAIL.getBytes());
                                out.flush();
                                Log.e("更新SF_STOCKCHECKDETAIL", resultStr);
                                String strSF_STOCKCHECKDETAIL = receiveFileFromSocket(in, out, lengthSF_STOCKCHECKDETAIL);
                                Log.e("更新SF_STOCKCHECKDETAIL", strSF_STOCKCHECKDETAIL.length() + ":" + strSF_STOCKCHECKDETAIL);
                                mUpdateSqlite.updateStockCheckDetail(strSF_STOCKCHECKDETAIL);
                                out.write(Constant.SYNCSF_STOCKLOTCHECKDETAIL.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_STOCKLOTCHECKDETAIL:
                                int lengthSF_STOCKLOTCHECKDETAIL = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_STOCKLOTCHECKDETAIL.getBytes());
                                out.flush();
                                Log.e("更新STOCKLOTCHECKDETAIL", resultStr);
                                String strSF_STOCKLOTCHECKDETAIL = receiveFileFromSocket(in, out, lengthSF_STOCKLOTCHECKDETAIL);
                                Log.e("更新STOCKLOTCHECKDETAIL", strSF_STOCKLOTCHECKDETAIL.length() + ":" + strSF_STOCKLOTCHECKDETAIL);
                                mUpdateSqlite.updateStockLotCheckDetail(strSF_STOCKLOTCHECKDETAIL);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            //endregion

                            //region UploadStockCheck
                            case Constant.UPLOADSTOCKCHECKSTART:
                                stockcheckData = getstockcheckData();
                                String StockCheckData1 = Constant.UPLOADSTOCKCHECKINFO + stockcheckData.getBytes("UTF8").length;
                                Log.e("准备上传StockCheck", StockCheckData1);
                                out.write(StockCheckData1.getBytes());
                                out.flush();
                                break;

                            case Constant.UPLOADSTOCKCHECK:
                                Log.e("上传StockCheck", stockcheckData);
                                out.write(stockcheckData.getBytes("UTF8"));
                                out.flush();
                                break;
                            case Constant.UPLOADSTOCKCHECKDETAILINFO:
                                stockcheckDetailData = getStockcheckDetailData();
                                String stockcheckDetailData1 = Constant.UPLOADSTOCKCHECKDETAILINFO + stockcheckDetailData.getBytes("UTF8").length;
                                Log.e("准备上传stockcheckdetail", stockcheckDetailData1);
                                out.write(stockcheckDetailData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADSTOCKCHECKDETAIL:
                                Log.e("上传stockcheckdetail", stockcheckDetailData);
                                out.write(stockcheckDetailData.getBytes("UTF8"));
                                out.flush();
                                break;
                            case Constant.UPLOADSTOCKLOTCHECKDETAILINFO:
                                stocklotcheckDetailData = getStocklotcheckDetailData();
                                String stocklotcheckDetailData1 = Constant.UPLOADSTOCKLOTCHECKDETAILINFO + stocklotcheckDetailData.getBytes("UTF8").length;
                                Log.e("准备上传stocklotcheckdetail", stocklotcheckDetailData1);
                                out.write(stocklotcheckDetailData1.getBytes());
                                out.flush();
                                break;
                            case Constant.UPLOADSTOCKLOTCHECKDETAIL:
                                Log.e("上传stocklotcheckdetail", stocklotcheckDetailData);
                                out.write(stocklotcheckDetailData.getBytes("UTF8"));
                                out.flush();
                                break;
                            //endregion

                            //region 从PDA上删除保存好的盘点数据
                            case Constant.RETURNSTOCKCHECKSAVESTART:
                                Log.e("更新STOCKETCHECKSAVEkais", resultStr);
                                out.write(Constant.SYNCPDASTOCKCHECKSAVING.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCPDASTOCKCHECKSAVING:
                                int lengthSHIPPINGSAVE = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCPDASTOCKCHECKSAVING.getBytes());
                                out.flush();
                                Log.e("更新STOCKETCHECKSAVE", resultStr);
                                String STOCKETCHECKSAVE = receiveFileFromSocket(in, out, lengthSHIPPINGSAVE);
                                Log.e("更新STOCKETCHECKSAVE", STOCKETCHECKSAVE.length() + ":" + STOCKETCHECKSAVE);
                                mUpdateSqlite.updateSTOCKCHECKSAVE(STOCKETCHECKSAVE);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            //endregion

                            //endregion

                            //region UpdateCommon
                            /**
                             * 收到开始的标志以后,按顺序更新数据库表:
                             * SF_CONSUMABLEDEFINITION-->SF_AREA-->SF_WAREHOUSE-->SF_CODE
                             * 更完最后一个表传更新结束的标志
                             */
                            case Constant.UPDATECOMMONSTART:
                                out.write(Constant.SYNCSF_CONSUMABLEDEFINITION.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_CODE:
                                //Code表作为最后一个表格更新完所有sqlite的内容之后,告诉pc可以断开连接了
                                int lengthSF_CODE = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_CODE.getBytes());
                                out.flush();
                                Log.e("更新SF_CODE", resultStr);
                                String strSF_CODE = receiveFileFromSocket(in, out, lengthSF_CODE);
                                Log.e("更新SF_CODE", strSF_CODE.length() + ":" + strSF_CODE);
                                mUpdateSqlite.updateCode(strSF_CODE);
                                out.write(Constant.UPDATEEXIT.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_CONSUMABLEDEFINITION:
                                int lengthSF_CONSUMABLEDEFINITION = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_CONSUMABLEDEFINITION.getBytes());
                                out.flush();
                                Log.e("更新CONSUMABLEDEFINITION", resultStr);
                                String strSF_CONSUMABLEDEFINITION = receiveFileFromSocket(in, out, lengthSF_CONSUMABLEDEFINITION);
                                Log.e("更新CONSUMABLEDEFINITION", strSF_CONSUMABLEDEFINITION.length() + ":" + strSF_CONSUMABLEDEFINITION);
                                mUpdateSqlite.updateConsumabledefinition(strSF_CONSUMABLEDEFINITION);
                                out.write(Constant.SYNCSF_AREA.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_AREA:
                                int lengthSF_AREA = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_AREA.getBytes());
                                out.flush();
                                Log.e("更新SF_AREA", resultStr);
                                String strSF_AREA = receiveFileFromSocket(in, out, lengthSF_AREA);
                                Log.e("更新SF_AREA", strSF_AREA.length() + ":" + strSF_AREA);
                                mUpdateSqlite.updateArea(strSF_AREA);
                                out.write(Constant.SYNCSF_WAREHOUSE.getBytes());
                                out.flush();
                                break;
                            case Constant.SYNCSF_WAREHOUSE:
                                int lengthSF_WAREHOUSE = Integer.parseInt(resultStr);
                                out.write(Constant.IYNCSF_WAREHOUSE.getBytes());
                                out.flush();
                                Log.e("更新SF_WAREHOUSE", resultStr);
                                String strSF_WAREHOUSE = receiveFileFromSocket(in, out, lengthSF_WAREHOUSE);
                                Log.e("更新SF_WAREHOUSE", strSF_WAREHOUSE.length() + ":" + strSF_WAREHOUSE);
                                mUpdateSqlite.updateWareHouse(strSF_WAREHOUSE);
                                out.write(Constant.SYNCSF_CODE.getBytes());
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
                    Log.e("warehouse", "关闭");
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
//            String strtmp = "read file length ok:" + filelen;
//            out.write(strtmp.getBytes("utf-8"));
//            out.flush();

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
//            out.write("read file ok".getBytes("utf-8"));
//            out.flush();
        } catch (Exception e) {
            Log.e("1111111", Thread.currentThread().getName()
                    + "---->" + "receiveFileFromSocket error");
            e.printStackTrace();
        }
        return returnstr;

    }

    private String getInboundOrderData() {
        String a = "";
        ArrayList<UploadInboundOrder> inboundOrderlist = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadInboundOrderQuery));
        Log.e("GetUploadInbORQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                UploadInboundOrder uploadInboundOrder = new UploadInboundOrder();
                uploadInboundOrder.setINBOUNDNO(getCursorData(cursorDatalist, Constant.INBOUNDNO).trim());
                uploadInboundOrder.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                uploadInboundOrder.setWAREHOUSENAME(getCursorData(cursorDatalist, Constant.WAREHOUSENAME).trim());
                uploadInboundOrder.setSCHEDULEDATE(getCursorData(cursorDatalist, Constant.SCHEDULEDATE).trim());
                uploadInboundOrder.setINBOUNDDATE(getCursorData(cursorDatalist, Constant.INBOUNDDATE).trim());
                uploadInboundOrder.setTEMPINBOUNDDATE(getCursorData(cursorDatalist, Constant.TEMPINBOUNDDATE).trim());
                uploadInboundOrder.setINBOUNDTYPE(getCursorData(cursorDatalist, Constant.INBOUNDTYPE).trim());
                uploadInboundOrder.setINBOUNDSTATE(getCursorData(cursorDatalist, Constant.INBOUNDSTATE).trim());
                uploadInboundOrder.setINBOUNDSTATENAME(getCursorData(cursorDatalist, Constant.STATENAME).trim());
                uploadInboundOrder.setCONSUMABLECOUNT(getCursorData(cursorDatalist, Constant.CONSUMABLECOUNT).trim());
                inboundOrderlist.add(uploadInboundOrder);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(inboundOrderlist);
        return a;
    }

    private String getConsumeInboundData() {
        String a = "";
        ArrayList<UploadConsumeInbound> uploadConsumeInboundslist = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadConsumeInboundQuery));
        Log.e("GetUploadconsimQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                UploadConsumeInbound uploadConsumeInbound = new UploadConsumeInbound();
                uploadConsumeInbound.setINBOUNDNO(getCursorData(cursorDatalist, Constant.INBOUNDNO).trim());
                uploadConsumeInbound.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                uploadConsumeInbound.setCONSUMABLEDEFVERSION(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFVERSION).trim());
                uploadConsumeInbound.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                uploadConsumeInbound.setORDERNO(getCursorData(cursorDatalist, Constant.ORDERNO).trim());
                uploadConsumeInbound.setORDERTYPE(getCursorData(cursorDatalist, Constant.ORDERTYPE).trim());
                uploadConsumeInbound.setLINENO(getCursorData(cursorDatalist, Constant.LINENO).trim());
                uploadConsumeInbound.setINQTY(getCursorData(cursorDatalist, Constant.INQTY).trim());
                uploadConsumeInbound.setUNIT(getCursorData(cursorDatalist, Constant.UNIT).trim());
                uploadConsumeInbound.setPLANQTY(getCursorData(cursorDatalist, Constant.PLANQTY).trim());
                uploadConsumeInbound.setORDERCOMPANY(getCursorData(cursorDatalist, Constant.ORDERCOMPANY).trim());
                uploadConsumeInbound.setDIVERSIONUNIT(getCursorData(cursorDatalist, Constant.DIVERSIONUNIT).trim());
                uploadConsumeInbound.setDIVERSIONQTY(getCursorData(cursorDatalist, Constant.DIVERSIONQTY).trim());
                uploadConsumeInboundslist.add(uploadConsumeInbound);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(uploadConsumeInboundslist);
        return a;
    }

    private String getConsumeLotInboundData() {
        String a = "";
        ArrayList<UploadConsumeLotInbound> uploadConsumeLotInboundslist = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadConsumeLotInboundQuery));
        Log.e("GetUploadconlotsimQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                UploadConsumeLotInbound uploadConsumeLotInbound = new UploadConsumeLotInbound();
                uploadConsumeLotInbound.setINBOUNDNO(getCursorData(cursorDatalist, Constant.INBOUNDNO).trim());
                uploadConsumeLotInbound.setCONSUMABLEDEFID(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFID).trim());
                uploadConsumeLotInbound.setCONSUMABLEDEFVERSION(getCursorData(cursorDatalist, Constant.CONSUMABLEDEFVERSION).trim());
                uploadConsumeLotInbound.setWAREHOUSEID(getCursorData(cursorDatalist, Constant.WAREHOUSEID).trim());
                uploadConsumeLotInbound.setORDERNO(getCursorData(cursorDatalist, Constant.ORDERNO).trim());
                uploadConsumeLotInbound.setORDERTYPE(getCursorData(cursorDatalist, Constant.ORDERTYPE).trim());
                uploadConsumeLotInbound.setLINENO(getCursorData(cursorDatalist, Constant.LINENO).trim());
                uploadConsumeLotInbound.setINQTY(getCursorData(cursorDatalist, Constant.INQTY).trim());
                uploadConsumeLotInbound.setUNIT(getCursorData(cursorDatalist, Constant.UNIT).trim());
                uploadConsumeLotInbound.setPLANQTY(getCursorData(cursorDatalist, Constant.PLANQTY).trim());
                uploadConsumeLotInbound.setCONSUMABLELOTID(getCursorData(cursorDatalist, Constant.CONSUMABLELOTID).trim());
                uploadConsumeLotInbound.setORDERCOMPANY(getCursorData(cursorDatalist, Constant.ORDERCOMPANY).trim());
                uploadConsumeLotInbound.setDIVERSIONUNIT(getCursorData(cursorDatalist, Constant.DIVERSIONUNIT).trim());
                uploadConsumeLotInbound.setDIVERSIONQTY(getCursorData(cursorDatalist, Constant.DIVERSIONQTY).trim());
                uploadConsumeLotInboundslist.add(uploadConsumeLotInbound);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(uploadConsumeLotInboundslist);
        return a;
    }

    private String getstockcheckData() {
        String a = "";
        ArrayList<UploadStockCheckData> uploadStockCheckDatas = new ArrayList<>();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadStockCheckQuery));
        Log.e("GetStockCheckQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                UploadStockCheckData uploadStockCheckData = new UploadStockCheckData();
                uploadStockCheckData.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                uploadStockCheckData.setWAREHOUSENAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSENAME)));
                uploadStockCheckData.setCHECKMONTH(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKMONTH)));
                uploadStockCheckData.setSTATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STATE)));
                uploadStockCheckData.setSTATENAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STATENAME)));
                uploadStockCheckData.setSTARTDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.STARTDATE)));
                uploadStockCheckData.setENDDATE(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.ENDDATE)));
                uploadStockCheckDatas.add(uploadStockCheckData);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(uploadStockCheckDatas);
        return a;
    }

    private String getStockcheckDetailData() {
        String a = "";
        ArrayList<UploadStockCheckDetail> stockCheckDetails = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadStockCheckDetailQuery));
        Log.e("GetUploadInbORQuery", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                UploadStockCheckDetail uploadStockCheckDetail = new UploadStockCheckDetail();
                uploadStockCheckDetail.setCONSUMABLEDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFID)));
                uploadStockCheckDetail.setCONSUMABLEDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFNAME)));
                uploadStockCheckDetail.setCONSUMABLEDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFVERSION)));
                uploadStockCheckDetail.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                uploadStockCheckDetail.setQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                uploadStockCheckDetail.setCHECKQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)));
                uploadStockCheckDetail.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                uploadStockCheckDetail.setCHECKMONTH(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKMONTH)));
                uploadStockCheckDetail.setUSERID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.USERID)));
                uploadStockCheckDetail.setUSERNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.USERNAME)));
                if (uploadStockCheckDetail.getUSERNAME() == null)
                    uploadStockCheckDetail.setUSERNAME("");
                stockCheckDetails.add(uploadStockCheckDetail);
            }
        }
        Gson StockCheckDetailjson = new Gson();
        a = StockCheckDetailjson.toJson(stockCheckDetails);
        return a;
    }

    private String getStocklotcheckDetailData() {
        String a = "";
        ArrayList<UploadStockLotCheckDetail> uploadStockLotCheckDetailArrayList = new ArrayList<>();
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.GetUploadStockLotCheckDetailQuery));
        Log.e("GetStockLotCheckDetail", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist.getCount() != 0) {
            Log.e("查到了", "111111111");
            while (cursorDatalist.moveToNext()) {
                UploadStockLotCheckDetail stockLotCheckDetail = new UploadStockLotCheckDetail();
                stockLotCheckDetail.setCONSUMABLELOTID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLELOTID)));
                stockLotCheckDetail.setCONSUMABLEDEFID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFID)));
                stockLotCheckDetail.setCONSUMABLEDEFNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFNAME)));
                stockLotCheckDetail.setCONSUMABLEDEFVERSION(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CONSUMABLEDEFVERSION)));
                stockLotCheckDetail.setUNIT(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.UNIT)));
                stockLotCheckDetail.setQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.QTY)));
                stockLotCheckDetail.setCHECKQTY(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKQTY)));
                stockLotCheckDetail.setWAREHOUSEID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.WAREHOUSEID)));
                stockLotCheckDetail.setCHECKMONTH(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.CHECKMONTH)));
                stockLotCheckDetail.setUSERID(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.USERID)));
                stockLotCheckDetail.setUSERNAME(cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.USERNAME)));
                if (stockLotCheckDetail.getUSERNAME() == null)
                    stockLotCheckDetail.setUSERNAME("");
                uploadStockLotCheckDetailArrayList.add(stockLotCheckDetail);
            }
        }
        Gson shipplanjson = new Gson();
        a = shipplanjson.toJson(uploadStockLotCheckDetailArrayList);
        return a;
    }

    private boolean checkStockCheckIsAllUpload() {
        boolean returnflag = false;
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.checkStockCheckIsAllUpload));
        Log.e("checkIsAllUpload", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist != null) {
            if (cursorDatalist.getCount() != 0) {
                Log.e("查到了", "455");
                while (cursorDatalist.moveToNext()) {
                    if (!cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.COUNT)).equals("0"))
                        returnflag = true;
                }
            } else {
                Log.e("没有符合条件的", "111111111");
            }
        } else {
            returnflag = false;
        }
        return returnflag;
    }

    private boolean checkStockInboundAllUpload() {
        boolean returnflag = false;
        db = mHelper.getWritableDatabase();
        String selectDataListsql = String.format(mContext.getString(R.string.checkInboundIsAllUpload));
        Log.e("checkInboundIsAllUpload", selectDataListsql);
        Cursor cursorDatalist = DBManger.selectDatBySql(db, selectDataListsql, null);
        if (cursorDatalist != null) {
            if (cursorDatalist.getCount() != 0) {
                Log.e("查到了", "455");
                while (cursorDatalist.moveToNext()) {
                    if (!cursorDatalist.getString(cursorDatalist.getColumnIndex(Constant.COUNT)).equals("0"))
                        returnflag = true;
                }
            } else {
                Log.e("没有符合条件的", "111111111");
            }
        } else {
            returnflag = false;
        }
        return returnflag;
    }
}
