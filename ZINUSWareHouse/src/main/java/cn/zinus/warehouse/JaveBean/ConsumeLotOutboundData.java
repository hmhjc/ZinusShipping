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
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFNAME;
    private String CONSUMABLEDEFVERSION;
    private String SPEC_DESC;
    private String DEFAULTUNIT;
    private String OUTQTY;
    private String OUTBOUNDSTATE;
    private String WAREHOUSEID;
    private String FROMWAREHOUSEID;
    private String TAGID;
    private String TAGQTY;
    private int backgroundColor;

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

    public String getSPEC_DESC() {
        return SPEC_DESC;
    }

    public void setSPEC_DESC(String SPEC_DESC) {
        this.SPEC_DESC = SPEC_DESC;
    }

    public String getDEFAULTUNIT() {
        return DEFAULTUNIT;
    }

    public void setDEFAULTUNIT(String DEFAULTUNIT) {
        this.DEFAULTUNIT = DEFAULTUNIT;
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

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
    }

    public String getFROMWAREHOUSEID() {
        return FROMWAREHOUSEID;
    }

    public void setFROMWAREHOUSEID(String FROMWAREHOUSEID) {
        this.FROMWAREHOUSEID = FROMWAREHOUSEID;
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

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
