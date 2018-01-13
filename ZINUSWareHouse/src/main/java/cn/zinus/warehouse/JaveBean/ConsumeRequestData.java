package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/19 20:28
 * Main Change:
 */

public class ConsumeRequestData implements Serializable {
    private String AREAID;
    private String OUTBOUNDSTATE;
    private String VALIDSTATE;
    private String CONSUMEREQNO;
    private String CONSUMABLECOUNT;
    private String USERID;
    private String USERNAME;
    private String LOCATIONID;
    private String LOCATIONNAME;
    private String REQUESTUSERID;
    private String REQUESTUSERNAME;
    private String FROMWAREHOUSEID;
    private String FROMWAREHOUSENAME;
    private String WAREHOUSEID;
    private String TOWAREHOUSENAME;
    private String REQUESTDATE;
    private String FINISHPLANDATE;

    public String getCONSUMABLECOUNT() {
        return CONSUMABLECOUNT;
    }

    public void setCONSUMABLECOUNT(String CONSUMABLECOUNT) {
        this.CONSUMABLECOUNT = CONSUMABLECOUNT;
    }

    public String getUSERNAME() {
        return USERNAME;
    }

    public void setUSERNAME(String USERNAME) {
        this.USERNAME = USERNAME;
    }

    public String getLOCATIONID() {
        return LOCATIONID;
    }

    public void setLOCATIONID(String LOCATIONID) {
        this.LOCATIONID = LOCATIONID;
    }

    public String getLOCATIONNAME() {
        return LOCATIONNAME;
    }

    public void setLOCATIONNAME(String LOCATIONNAME) {
        this.LOCATIONNAME = LOCATIONNAME;
    }

    public String getREQUESTUSERID() {
        return REQUESTUSERID;
    }

    public void setREQUESTUSERID(String REQUESTUSERID) {
        this.REQUESTUSERID = REQUESTUSERID;
    }

    public String getREQUESTUSERNAME() {
        return REQUESTUSERNAME;
    }

    public void setREQUESTUSERNAME(String REQUESTUSERNAME) {
        this.REQUESTUSERNAME = REQUESTUSERNAME;
    }

    public String getFROMWAREHOUSEID() {
        return FROMWAREHOUSEID;
    }

    public void setFROMWAREHOUSEID(String FROMWAREHOUSEID) {
        this.FROMWAREHOUSEID = FROMWAREHOUSEID;
    }

    public String getFROMWAREHOUSENAME() {
        return FROMWAREHOUSENAME;
    }

    public void setFROMWAREHOUSENAME(String FROMWAREHOUSENAME) {
        this.FROMWAREHOUSENAME = FROMWAREHOUSENAME;
    }

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
    }

    public String getTOWAREHOUSENAME() {
        return TOWAREHOUSENAME;
    }

    public void setTOWAREHOUSENAME(String TOWAREHOUSENAME) {
        this.TOWAREHOUSENAME = TOWAREHOUSENAME;
    }

    public String getFINISHPLANDATE() {
        return FINISHPLANDATE;
    }

    public void setFINISHPLANDATE(String FINISHPLANDATE) {
        this.FINISHPLANDATE = FINISHPLANDATE;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getCONSUMEREQNO() {
        return CONSUMEREQNO;
    }

    public void setCONSUMEREQNO(String CONSUMEREQNO) {
        this.CONSUMEREQNO = CONSUMEREQNO;
    }

    public String getAREAID() {
        return AREAID;
    }

    public void setAREAID(String AREAID) {
        this.AREAID = AREAID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getREQUESTDATE() {
        return REQUESTDATE;
    }

    public void setREQUESTDATE(String REQUESTDATE) {
        this.REQUESTDATE = REQUESTDATE;
    }

    public String getOUTBOUNDSTATE() {
        return OUTBOUNDSTATE;
    }

    public void setOUTBOUNDSTATE(String OUTBOUNDSTATE) {
        this.OUTBOUNDSTATE = OUTBOUNDSTATE;
    }
}
