package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/22 17:39
 * Main Change:
 */

public class StockLotCheckDeatilData implements Serializable {
    private String WAREHOUSEID;
    private String CHECKMONTH;
    private String CONSUMABLEDEFNAME;
    private String CONSUMABLEDEFID;
    private String CONSUMABLELOTID;
    private String UNIT;
    private String QTY;
    private String USERID;
    private String USERNAME;
    private String CHECKUNIT;
    private String CHECKQTY;
    private String VALIDSTATE;
    private String CONSUMABLEDEFVERSION;
    private int backgroundColor;
    private String TAGID;
    private String TAGQTY;

    @Override
    public String toString() {
        return "StockLotCheckDeatilData{" +
                "WAREHOUSEID='" + WAREHOUSEID + '\'' +
                ", CHECKMONTH='" + CHECKMONTH + '\'' +
                ", CONSUMABLEDEFNAME='" + CONSUMABLEDEFNAME + '\'' +
                ", CONSUMABLEDEFID='" + CONSUMABLEDEFID + '\'' +
                ", CONSUMABLELOTID='" + CONSUMABLELOTID + '\'' +
                ", UNIT='" + UNIT + '\'' +
                ", QTY='" + QTY + '\'' +
                ", USERID='" + USERID + '\'' +
                ", USERNAME='" + USERNAME + '\'' +
                ", CHECKUNIT='" + CHECKUNIT + '\'' +
                ", CHECKQTY='" + CHECKQTY + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                ", CONSUMABLEDEFVERSION='" + CONSUMABLEDEFVERSION + '\'' +
                ", backgroundColor=" + backgroundColor +
                ", TAGID='" + TAGID + '\'' +
                ", TAGQTY='" + TAGQTY + '\'' +
                '}';
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
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

    public String getCONSUMABLEDEFNAME() {
        return CONSUMABLEDEFNAME;
    }

    public void setCONSUMABLEDEFNAME(String CONSUMABLEDEFNAME) {
        this.CONSUMABLEDEFNAME = CONSUMABLEDEFNAME;
    }

    public String getCONSUMABLEDEFID() {
        return CONSUMABLEDEFID;
    }

    public void setCONSUMABLEDEFID(String CONSUMABLEDEFID) {
        this.CONSUMABLEDEFID = CONSUMABLEDEFID;
    }

    public String getCONSUMABLELOTID() {
        return CONSUMABLELOTID;
    }

    public void setCONSUMABLELOTID(String CONSUMABLELOTID) {
        this.CONSUMABLELOTID = CONSUMABLELOTID;
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

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getCHECKUNIT() {
        return CHECKUNIT;
    }

    public void setCHECKUNIT(String CHECKUNIT) {
        this.CHECKUNIT = CHECKUNIT;
    }

    public String getCHECKQTY() {
        return CHECKQTY;
    }

    public void setCHECKQTY(String CHECKQTY) {
        this.CHECKQTY = CHECKQTY;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getCONSUMABLEDEFVERSION() {
        return CONSUMABLEDEFVERSION;
    }

    public void setCONSUMABLEDEFVERSION(String CONSUMABLEDEFVERSION) {
        this.CONSUMABLEDEFVERSION = CONSUMABLEDEFVERSION;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTAGID() {
        return TAGID;
    }

    public void setTAGID(String TAGID) {
        this.TAGID = TAGID;
    }

    public String getTAGQTY() {
        return TAGQTY;
    }

    public void setTAGQTY(String TAGQTY) {
        this.TAGQTY = TAGQTY;
    }
}
