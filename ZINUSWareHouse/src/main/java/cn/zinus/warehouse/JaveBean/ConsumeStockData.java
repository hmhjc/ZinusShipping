package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 18:46
 * Main Change:
 */

public class ConsumeStockData implements Serializable {
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFVERSION;
    private String WAREHOUSEID;
    private String UNIT;
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

    public String getLASTTXNID() {
        return LASTTXNID;
    }

    public void setLASTTXNID(String LASTTXNID) {
        this.LASTTXNID = LASTTXNID;
    }

    public String getLASTTXNUSER() {
        return LASTTXNUSER;
    }

    public void setLASTTXNUSER(String LASTTXNUSER) {
        this.LASTTXNUSER = LASTTXNUSER;
    }

    public String getLASTTXNTIME() {
        return LASTTXNTIME;
    }

    public void setLASTTXNTIME(String LASTTXNTIME) {
        this.LASTTXNTIME = LASTTXNTIME;
    }

    public String getLASTTXNCOMMENT() {
        return LASTTXNCOMMENT;
    }

    public void setLASTTXNCOMMENT(String LASTTXNCOMMENT) {
        this.LASTTXNCOMMENT = LASTTXNCOMMENT;
    }

    public String getLASTTXNHISTKEY() {
        return LASTTXNHISTKEY;
    }

    public void setLASTTXNHISTKEY(String LASTTXNHISTKEY) {
        this.LASTTXNHISTKEY = LASTTXNHISTKEY;
    }

    public String getLASTGROUPHISTKEY() {
        return LASTGROUPHISTKEY;
    }

    public void setLASTGROUPHISTKEY(String LASTGROUPHISTKEY) {
        this.LASTGROUPHISTKEY = LASTGROUPHISTKEY;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

}
