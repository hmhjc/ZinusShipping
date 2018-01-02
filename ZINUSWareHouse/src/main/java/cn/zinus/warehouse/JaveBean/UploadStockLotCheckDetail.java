package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Created by huangjiechun on 2018/1/2.
 * CONSUMABLELOTID
 * CONSUMABLEDEFID
 * CONSUMABLEDEFNAME
 * CONSUMABLEDEFVERSION
 * UNIT
 * QTY
 * CHECKQTY
 * WAREHOUSEID
 * CHECKMONTH
 * USERID
 * USERNAME
 */

public class UploadStockLotCheckDetail implements Serializable {
    private String CONSUMABLELOTID;
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFNAME;
    private String CONSUMABLEDEFVERSION;
    private String UNIT;
    private String QTY;
    private String CHECKQTY;
    private String WAREHOUSEID;
    private String CHECKMONTH;
    private String USERID;
    private String USERNAME;

    public String getCONSUMABLELOTID() {
        return CONSUMABLELOTID;
    }

    public void setCONSUMABLELOTID(String CONSUMABLELOTID) {
        this.CONSUMABLELOTID = CONSUMABLELOTID;
    }

    public String getCONSUMABLEDEFID() {
        return CONSUMABLEDEFID;
    }

    public void setCONSUMABLEDEFID(String CONSUMABLEDEFID) {
        this.CONSUMABLEDEFID = CONSUMABLEDEFID;
    }

    public String getCONSUMABLEDEFNAME() {
        return CONSUMABLEDEFNAME;
    }

    public void setCONSUMABLEDEFNAME(String CONSUMABLEDEFNAME) {
        this.CONSUMABLEDEFNAME = CONSUMABLEDEFNAME;
    }

    public String getCONSUMABLEDEFVERSION() {
        return CONSUMABLEDEFVERSION;
    }

    public void setCONSUMABLEDEFVERSION(String CONSUMABLEDEFVERSION) {
        this.CONSUMABLEDEFVERSION = CONSUMABLEDEFVERSION;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getCHECKQTY() {
        return CHECKQTY;
    }

    public void setCHECKQTY(String CHECKQTY) {
        this.CHECKQTY = CHECKQTY;
    }

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
    }

    public String getCHECKMONTH() {
        return CHECKMONTH;
    }

    public void setCHECKMONTH(String CHECKMONTH) {
        this.CHECKMONTH = CHECKMONTH;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }
}