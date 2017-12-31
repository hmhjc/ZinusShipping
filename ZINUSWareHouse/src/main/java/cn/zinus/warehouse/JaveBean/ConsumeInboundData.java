package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;


/**
 * Developer:Spring
 * DataTime :2017/9/15 14:00
 * Main Change:
 */

public class ConsumeInboundData implements Serializable {
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFVERSION;
    private String CONSUMABLEDEFNAME;
    private String WAREHOUSEID;
    private String INQTY;
    private String INBOUNDNO;
    private String INBOUNDSTATE;
    private String VALIDSTATE;
    private String UNIT;
    private String INBOUNDDATE;
    private String PLANQTY;
    private String ORDERNO;
    private String ORDERTYPE;
    private String LINENO;
    private String ORDERCOMPANY;
    private int backgroundColor;

    public String getORDERCOMPANY() {
        return ORDERCOMPANY;
    }

    public void setORDERCOMPANY(String ORDERCOMPANY) {
        this.ORDERCOMPANY = ORDERCOMPANY;
    }

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
    }

    public String getORDERTYPE() {
        return ORDERTYPE;
    }

    public void setORDERTYPE(String ORDERTYPE) {
        this.ORDERTYPE = ORDERTYPE;
    }

    public String getLINENO() {
        return LINENO;
    }

    public void setLINENO(String LINENO) {
        this.LINENO = LINENO;
    }

    public String getCONSUMABLEDEFNAME() {
        return CONSUMABLEDEFNAME;
    }

    public void setCONSUMABLEDEFNAME(String CONSUMABLEDEFNAME) {
        this.CONSUMABLEDEFNAME = CONSUMABLEDEFNAME;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public ConsumeInboundData() {
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

    public String getINQTY() {
        return INQTY;
    }

    public void setINQTY(String INQTY) {
        this.INQTY = INQTY;
    }

    public String getINBOUNDNO() {
        return INBOUNDNO;
    }

    public void setINBOUNDNO(String INBOUNDNO) {
        this.INBOUNDNO = INBOUNDNO;
    }

    public String getINBOUNDSTATE() {
        return INBOUNDSTATE;
    }

    public void setINBOUNDSTATE(String INBOUNDSTATE) {
        this.INBOUNDSTATE = INBOUNDSTATE;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getINBOUNDDATE() {
        return INBOUNDDATE;
    }

    public void setINBOUNDDATE(String INBOUNDDATE) {
        this.INBOUNDDATE = INBOUNDDATE;
    }

    public String getPLANQTY() {
        return PLANQTY;
    }

    public void setPLANQTY(String PLANQTY) {
        this.PLANQTY = PLANQTY;
    }

    @Override
    public String toString() {
        return "ConsumeInboundData{" +
                "CONSUMABLEDEFID='" + CONSUMABLEDEFID + '\'' +
                ", CONSUMABLEDEFVERSION='" + CONSUMABLEDEFVERSION + '\'' +
                ", WAREHOUSEID='" + WAREHOUSEID + '\'' +
                ", INQTY='" + INQTY + '\'' +
                ", INBOUNDNO='" + INBOUNDNO + '\'' +
                ", INBOUNDSTATE='" + INBOUNDSTATE + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                ", UNIT='" + UNIT + '\'' +
                ", INBOUNDDATE='" + INBOUNDDATE + '\'' +
                ", PLANQTY='" + PLANQTY + '\'' +
                '}';
    }
}
