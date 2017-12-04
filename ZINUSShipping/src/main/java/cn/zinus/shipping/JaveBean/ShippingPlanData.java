package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 13:45
 * Main Change:
 */

public class ShippingPlanData implements Serializable {

    private String SHIPPINGPLANNO;
    private String PRODUCTIONORDERNAME;
    private String PLANTID;
    private String CUSTOMERID;
    private String PRODUCTDEFID;
    private String PRODUCTDEFVERSION;
    private String PLANSTARTTIME;
    private String PLANENDTIME;
    private String PLANQTY;
    private String CONTAINERSPEC;
    private String WORKINGSHIFT;
    private String AREAID;
    private String STATE;
    private String ISOISAVE;
    private String VALIDSTATE;

    @Override
    public String toString() {
        return "ShippingPlanData{" +
                "SHIPPINGPLANNO='" + SHIPPINGPLANNO + '\'' +
                ", PRODUCTIONORDERNAME='" + PRODUCTIONORDERNAME + '\'' +
                ", PLANTID='" + PLANTID + '\'' +
                ", CUSTOMERID='" + CUSTOMERID + '\'' +
                ", PRODUCTDEFID='" + PRODUCTDEFID + '\'' +
                ", PRODUCTDEFVERSION='" + PRODUCTDEFVERSION + '\'' +
                ", PLANSTARTTIME='" + PLANSTARTTIME + '\'' +
                ", PLANENDTIME='" + PLANENDTIME + '\'' +
                ", PLANQTY='" + PLANQTY + '\'' +
                ", CONTAINERSPEC='" + CONTAINERSPEC + '\'' +
                ", WORKINGSHIFT='" + WORKINGSHIFT + '\'' +
                ", AREAID='" + AREAID + '\'' +
                ", STATE='" + STATE + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                '}';
    }

    public String getSHIPPINGPLANNO() {
        return SHIPPINGPLANNO;
    }

    public void setSHIPPINGPLANNO(String SHIPPINGPLANNO) {
        this.SHIPPINGPLANNO = SHIPPINGPLANNO;
    }

    public String getPRODUCTIONORDERNAME() {
        return PRODUCTIONORDERNAME;
    }

    public void setPRODUCTIONORDERNAME(String PRODUCTIONORDERNAME) {
        this.PRODUCTIONORDERNAME = PRODUCTIONORDERNAME;
    }

    public String getPLANTID() {
        return PLANTID;
    }

    public void setPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
    }

    public String getCUSTOMERID() {
        return CUSTOMERID;
    }

    public void setCUSTOMERID(String CUSTOMERID) {
        this.CUSTOMERID = CUSTOMERID;
    }

    public String getPRODUCTDEFID() {
        return PRODUCTDEFID;
    }

    public void setPRODUCTDEFID(String PRODUCTDEFID) {
        this.PRODUCTDEFID = PRODUCTDEFID;
    }

    public String getPRODUCTDEFVERSION() {
        return PRODUCTDEFVERSION;
    }

    public void setPRODUCTDEFVERSION(String PRODUCTDEFVERSION) {
        this.PRODUCTDEFVERSION = PRODUCTDEFVERSION;
    }

    public String getPLANSTARTTIME() {
        return PLANSTARTTIME;
    }

    public void setPLANSTARTTIME(String PLANSTARTTIME) {
        this.PLANSTARTTIME = PLANSTARTTIME;
    }

    public String getPLANENDTIME() {
        return PLANENDTIME;
    }

    public void setPLANENDTIME(String PLANENDTIME) {
        this.PLANENDTIME = PLANENDTIME;
    }

    public String getPLANQTY() {
        return PLANQTY;
    }

    public void setPLANQTY(String PLANQTY) {
        this.PLANQTY = PLANQTY;
    }

    public String getCONTAINERSPEC() {
        return CONTAINERSPEC;
    }

    public void setCONTAINERSPEC(String CONTAINERSPEC) {
        this.CONTAINERSPEC = CONTAINERSPEC;
    }

    public String getWORKINGSHIFT() {
        return WORKINGSHIFT;
    }

    public void setWORKINGSHIFT(String WORKINGSHIFT) {
        this.WORKINGSHIFT = WORKINGSHIFT;
    }

    public String getAREAID() {
        return AREAID;
    }

    public void setAREAID(String AREAID) {
        this.AREAID = AREAID;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getISOISAVE() {
        return ISOISAVE;
    }

    public void setISOISAVE(String ISOISAVE) {
        this.ISOISAVE = ISOISAVE;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
