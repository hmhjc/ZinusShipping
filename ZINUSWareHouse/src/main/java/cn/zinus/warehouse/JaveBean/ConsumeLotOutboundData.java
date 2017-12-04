package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/20 11:07
 * Main Change:
 */

public class ConsumeLotOutboundData implements Serializable {
    private String CONSUMEREQNO;
    private String CONSUMABLELOTID;
    private String UNIT;
    private String OUTQTY;
    private String OUTBOUNDSTATE;
    private String USERID;
    private String OUTBOUNDDATE;
    private String DESCRIPTION;
    private String VALIDSTATE;
    private String WAREHOUSEID;
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFVERSION;
    private String CONSUMABLEDEFNAME;
    private int backgroundColor;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getCONSUMABLEDEFNAME() {
        return CONSUMABLEDEFNAME;
    }

    public void setCONSUMABLEDEFNAME(String CONSUMABLEDEFNAME) {
        this.CONSUMABLEDEFNAME = CONSUMABLEDEFNAME;
    }

    public String getCONSUMEREQNO() {
        return CONSUMEREQNO;
    }

    public void setCONSUMEREQNO(String CONSUMEREQNO) {
        this.CONSUMEREQNO = CONSUMEREQNO;
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

    public String getOUTQTY() {
        return OUTQTY;
    }

    public void setOUTQTY(String OUTQTY) {
        this.OUTQTY = OUTQTY;
    }

    public String getOUTBOUNDSTATE() {
        return OUTBOUNDSTATE;
    }

    public void setOUTBOUNDSTATE(String OUTBOUNDSTATE) {
        this.OUTBOUNDSTATE = OUTBOUNDSTATE;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getOUTBOUNDDATE() {
        return OUTBOUNDDATE;
    }

    public void setOUTBOUNDDATE(String OUTBOUNDDATE) {
        this.OUTBOUNDDATE = OUTBOUNDDATE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
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

    public String getREQUESTQTY() {
        return REQUESTQTY;
    }

    public void setREQUESTQTY(String REQUESTQTY) {
        this.REQUESTQTY = REQUESTQTY;
    }

    private String REQUESTQTY;
}
