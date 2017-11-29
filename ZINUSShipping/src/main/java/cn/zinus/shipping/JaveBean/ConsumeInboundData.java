package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/15 14:00
 * Main Change:
 */

public class ConsumeInboundData implements Serializable{
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFVERSION;
    private String WAREHOUSEID;
    private String INQTY;
    private String INBOUNDNO;
    private String INBOUNDSTATE;
    private String DESCRIPTION;
    private String CREATOR;
    private String CREATEDTIME;
    private String MODIFIER;
    private String MODIFIEDTIME;
    private String VALIDSTATE;
    private String UNIT;
    private String INBOUNDDATE;
    private String PLANQTY;
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
                ", DESCRIPTION='" + DESCRIPTION + '\'' +
                ", CREATOR='" + CREATOR + '\'' +
                ", CREATEDTIME='" + CREATEDTIME + '\'' +
                ", MODIFIER='" + MODIFIER + '\'' +
                ", MODIFIEDTIME='" + MODIFIEDTIME + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                ", UNIT='" + UNIT + '\'' +
                ", INBOUNDDATE='" + INBOUNDDATE + '\'' +
                ", PLANQTY='" + PLANQTY + '\'' +
                '}';
    }
}
