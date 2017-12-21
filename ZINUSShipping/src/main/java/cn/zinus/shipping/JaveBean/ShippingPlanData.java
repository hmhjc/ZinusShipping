package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 13:45
 * Main Change:
 */

public class ShippingPlanData implements Serializable {

    private String SHIPPINGPLANNO ;
    private String POID ;
    private String LINENO ;
    private String SHIPPINGPLANSEQ ;
    private String CONTAINERSEQ ;
    private String CONTAINERSPEC ;
    private String PRODUCTDEFNAME ;
    private String PLANQTY ;
    private String LOADEDQTY ;
    private String PLANSTARTTIME ;
    private String PLANENDTIME ;
    private String CUSTOMERID ;
    private String WORKINGSHIFT ;
    private String AREAID ;
    private String STATE ;
    private String VALIDSTATE;
    private String ORDERTYPE;
    private String ORDERNO;
    private String PRODUCTDEFID;
    private String PRODUCTDEFVERSION;

    @Override
    public String toString() {
        return "ShippingPlanData{" +
                "SHIPPINGPLANNO='" + SHIPPINGPLANNO + '\'' +
                ", POID='" + POID + '\'' +
                ", LINENO='" + LINENO + '\'' +
                ", SHIPPINGPLANSEQ='" + SHIPPINGPLANSEQ + '\'' +
                ", CONTAINERSEQ='" + CONTAINERSEQ + '\'' +
                ", CONTAINERSPEC='" + CONTAINERSPEC + '\'' +
                ", PRODUCTDEFNAME='" + PRODUCTDEFNAME + '\'' +
                ", PLANQTY='" + PLANQTY + '\'' +
                ", LOADEDQTY='" + LOADEDQTY + '\'' +
                ", PLANSTARTTIME='" + PLANSTARTTIME + '\'' +
                ", PLANENDTIME='" + PLANENDTIME + '\'' +
                ", CUSTOMERID='" + CUSTOMERID + '\'' +
                ", WORKINGSHIFT='" + WORKINGSHIFT + '\'' +
                ", AREAID='" + AREAID + '\'' +
                ", STATE='" + STATE + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                ", ORDERTYPE='" + ORDERTYPE + '\'' +
                ", ORDERNO='" + ORDERNO + '\'' +
                ", PRODUCTDEFID='" + PRODUCTDEFID + '\'' +
                ", PRODUCTDEFVERSION='" + PRODUCTDEFVERSION + '\'' +
                '}';
    }

    public String getORDERTYPE() {
        return ORDERTYPE;
    }

    public void setORDERTYPE(String ORDERTYPE) {
        this.ORDERTYPE = ORDERTYPE;
    }

    public String getORDERNO() {
        return ORDERNO;
    }

    public void setORDERNO(String ORDERNO) {
        this.ORDERNO = ORDERNO;
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

    public String getLINENO() {
        return LINENO;
    }

    public void setLINENO(String LINENO) {
        this.LINENO = LINENO;
    }

    public String getSHIPPINGPLANNO() {
        return SHIPPINGPLANNO;
    }

    public void setSHIPPINGPLANNO(String SHIPPINGPLANNO) {
        this.SHIPPINGPLANNO = SHIPPINGPLANNO;
    }

    public String getPOID() {
        return POID;
    }

    public void setPOID(String POID) {
        this.POID = POID;
    }

    public String getSHIPPINGPLANSEQ() {
        return SHIPPINGPLANSEQ;
    }

    public void setSHIPPINGPLANSEQ(String SHIPPINGPLANSEQ) {
        this.SHIPPINGPLANSEQ = SHIPPINGPLANSEQ;
    }

    public String getCONTAINERSEQ() {
        return CONTAINERSEQ;
    }

    public void setCONTAINERSEQ(String CONTAINERSEQ) {
        this.CONTAINERSEQ = CONTAINERSEQ;
    }

    public String getCONTAINERSPEC() {
        return CONTAINERSPEC;
    }

    public void setCONTAINERSPEC(String CONTAINERSPEC) {
        this.CONTAINERSPEC = CONTAINERSPEC;
    }

    public String getPRODUCTDEFNAME() {
        return PRODUCTDEFNAME;
    }

    public void setPRODUCTDEFNAME(String PRODUCTDEFNAME) {
        this.PRODUCTDEFNAME = PRODUCTDEFNAME;
    }

    public String getPLANQTY() {
        return PLANQTY;
    }

    public void setPLANQTY(String PLANQTY) {
        this.PLANQTY = PLANQTY;
    }

    public String getLOADEDQTY() {
        return LOADEDQTY;
    }

    public void setLOADEDQTY(String LOADEDQTY) {
        this.LOADEDQTY = LOADEDQTY;
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

    public String getCUSTOMERID() {
        return CUSTOMERID;
    }

    public void setCUSTOMERID(String CUSTOMERID) {
        this.CUSTOMERID = CUSTOMERID;
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

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
