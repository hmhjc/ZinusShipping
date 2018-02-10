package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/19 21:08
 * Main Change:
 */

public class ConsumeOutboundData implements Serializable {

    private String CONSUMEREQNO;
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFNAME;
    private String CONSUMABLEDEFVERSION;
    private String SPEC_DESC;
    private String WAREHOUSEID;
    private String FROMWAREHOUSEID;
    private String TOQTY;
    private String UNIT;
    private String FROMQTY;
    private String REQUESTQTY;
    private String OUTQTY;
    private String CONSUMABLETYPE;
    private String WAREHOUSEOWNERSHIPTYPE;
    private int backgroundColor;

    public String getCONSUMEREQNO() {
        return CONSUMEREQNO;
    }

    public void setCONSUMEREQNO(String CONSUMEREQNO) {
        this.CONSUMEREQNO = CONSUMEREQNO;
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

    public String getSPEC_DESC() {
        return SPEC_DESC;
    }

    public void setSPEC_DESC(String SPEC_DESC) {
        this.SPEC_DESC = SPEC_DESC;
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

    public String getTOQTY() {
        return TOQTY;
    }

    public void setTOQTY(String TOQTY) {
        this.TOQTY = TOQTY;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getFROMQTY() {
        return FROMQTY;
    }

    public void setFROMQTY(String FROMQTY) {
        this.FROMQTY = FROMQTY;
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

    public String getCONSUMABLETYPE() {
        return CONSUMABLETYPE;
    }

    public void setCONSUMABLETYPE(String CONSUMABLETYPE) {
        this.CONSUMABLETYPE = CONSUMABLETYPE;
    }

    public String getWAREHOUSEOWNERSHIPTYPE() {
        return WAREHOUSEOWNERSHIPTYPE;
    }

    public void setWAREHOUSEOWNERSHIPTYPE(String WAREHOUSEOWNERSHIPTYPE) {
        this.WAREHOUSEOWNERSHIPTYPE = WAREHOUSEOWNERSHIPTYPE;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }
}
