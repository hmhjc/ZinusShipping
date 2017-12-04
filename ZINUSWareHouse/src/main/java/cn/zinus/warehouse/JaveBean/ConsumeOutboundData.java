package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/19 21:08
 * Main Change:
 */

public class ConsumeOutboundData implements Serializable {
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFVERSION;
    private String CONSUMABLEDEFNAME;
    private String WAREHOUSEID;
    private String CONSUMEREQNO;
    private String UNIT;
    private String OUTBOUNDSTATE;
    private String USERID;
    private String FROMWAREHOUSEID;
    private String VALIDSTATE;
    private String REQUESTQTY;
    private String OUTQTY;
    private int backgroundColor;

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getFROMWAREHOUSEID() {
        return FROMWAREHOUSEID;
    }

    public void setFROMWAREHOUSEID(String FROMWAREHOUSEID) {
        this.FROMWAREHOUSEID = FROMWAREHOUSEID;
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

    public String getCONSUMEREQNO() {
        return CONSUMEREQNO;
    }

    public void setCONSUMEREQNO(String CONSUMEREQNO) {
        this.CONSUMEREQNO = CONSUMEREQNO;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
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

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getREQUESTQTY() {
        return REQUESTQTY;
    }

    public void setREQUESTQTY(String REQUESTQTY) {
        this.REQUESTQTY = REQUESTQTY;
    }

    public String getOUTQTY() {
        return OUTQTY;
    }

    public void setOUTQTY(String OUTQTY) {
        this.OUTQTY = OUTQTY;
    }
}
