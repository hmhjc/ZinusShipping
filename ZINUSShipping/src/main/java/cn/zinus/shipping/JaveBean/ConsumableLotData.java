package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 18:50
 * Main Change:
 */

public class ConsumableLotData implements Serializable {
    private String CONSUMABLESTATE;
    private String CREATEDQTY;
    private String CONSUMABLELOTID;
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFVERSION;
    private String WAREHOUSEID;
    private String QTY;
    private String DESCRIPTION;
    private String CREATOR;
    private String CREATEDTIME;
    private String MODIFIER;
    private String MODIFIEDTIME;
    private String LASTTXNID;
    private String LASTTXNUSER;
    private String LASTTXNTIME;
    private String LASTTXNCOMMENT;
    private String LASTTXNHISTKEY;
    private String LASTGROUPHISTKEY;
    private String VALIDSTATE;

    public String getCONSUMABLESTATE() {
        return CONSUMABLESTATE;
    }

    public void setCONSUMABLESTATE(String CONSUMABLESTATE) {
        this.CONSUMABLESTATE = CONSUMABLESTATE;
    }

    public String getCREATEDQTY() {
        return CREATEDQTY;
    }

    public void setCREATEDQTY(String CREATEDQTY) {
        this.CREATEDQTY = CREATEDQTY;
    }

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

    public String getCONSUMABLEDEFVERSION() {
        return CONSUMABLEDEFVERSION;
    }

    public void setCONSUMABLEDEFVERSION(String CONSUMABLEDEFVERSION) {
        this.CONSUMABLEDEFVERSION = CONSUMABLEDEFVERSION;
    }

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String CREATOR) {
        this.CREATOR = CREATOR;
    }

    public String getCREATEDTIME() {
        return CREATEDTIME;
    }

    public void setCREATEDTIME(String CREATEDTIME) {
        this.CREATEDTIME = CREATEDTIME;
    }

    public String getMODIFIER() {
        return MODIFIER;
    }

    public void setMODIFIER(String MODIFIER) {
        this.MODIFIER = MODIFIER;
    }

    public String getMODIFIEDTIME() {
        return MODIFIEDTIME;
    }

    public void setMODIFIEDTIME(String MODIFIEDTIME) {
        this.MODIFIEDTIME = MODIFIEDTIME;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

}
