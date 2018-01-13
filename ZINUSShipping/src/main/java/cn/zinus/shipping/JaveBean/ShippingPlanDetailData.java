package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Created by huangjiechun on 2017/12/29.
 *
 SHIPPINGPLANNO，
 SHIPPINGPLANSEQ，
 ORDERTYPE，
 ORDERNO，
 LINENO，
 PRODUCTDEFID，
 PRODUCTDEFVERSION，
 CONTAINERSEQ，
 POID，
 CONTAINERSPEC，
 CONTAINERNO，
 SEALNO，
 COMPLETETIME，
 PLANQTY，
 STATE，
 WORKINGSHIFT，
 AREAID，
 */

public class ShippingPlanDetailData implements Serializable {
    private String SHIPPINGPLANNO ;
    private String SHIPPINGPLANSEQ ;
    private String ORDERTYPE ;
    private String ORDERNO ;
    private String LINENO ;
    private String PRODUCTDEFID ;
    private String PRODUCTDEFVERSION ;
    private String PRODUCTDEFNAME ;
    private String CONTAINERSEQ ;
    private String POID ;
    private String CONTAINERSPEC ;
    private String CONTAINERNO ;
    private String SEALNO ;
    private String COMPLETETIME ;
    private String PLANQTY ;
    private String STATE ;
    private String WORKINGSHIFT ;
    private String AREAID ;
    private String LOADEDQTY ;

    @Override
    public String toString() {
        return "ShippingPlanDetailData{" +
                "SHIPPINGPLANNO='" + SHIPPINGPLANNO + '\'' +
                ", SHIPPINGPLANSEQ='" + SHIPPINGPLANSEQ + '\'' +
                ", ORDERTYPE='" + ORDERTYPE + '\'' +
                ", ORDERNO='" + ORDERNO + '\'' +
                ", LINENO='" + LINENO + '\'' +
                ", PRODUCTDEFID='" + PRODUCTDEFID + '\'' +
                ", PRODUCTDEFVERSION='" + PRODUCTDEFVERSION + '\'' +
                ", PRODUCTDEFNAME='" + PRODUCTDEFNAME + '\'' +
                ", CONTAINERSEQ='" + CONTAINERSEQ + '\'' +
                ", POID='" + POID + '\'' +
                ", CONTAINERSPEC='" + CONTAINERSPEC + '\'' +
                ", CONTAINERNO='" + CONTAINERNO + '\'' +
                ", SEALNO='" + SEALNO + '\'' +
                ", COMPLETETIME='" + COMPLETETIME + '\'' +
                ", PLANQTY='" + PLANQTY + '\'' +
                ", STATE='" + STATE + '\'' +
                ", WORKINGSHIFT='" + WORKINGSHIFT + '\'' +
                ", AREAID='" + AREAID + '\'' +
                ", LOADEDQTY='" + LOADEDQTY + '\'' +
                '}';
    }

    public String getSHIPPINGPLANNO() {
        return SHIPPINGPLANNO;
    }

    public void setSHIPPINGPLANNO(String SHIPPINGPLANNO) {
        this.SHIPPINGPLANNO = SHIPPINGPLANNO;
    }

    public String getSHIPPINGPLANSEQ() {
        return SHIPPINGPLANSEQ;
    }

    public void setSHIPPINGPLANSEQ(String SHIPPINGPLANSEQ) {
        this.SHIPPINGPLANSEQ = SHIPPINGPLANSEQ;
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

    public String getLINENO() {
        return LINENO;
    }

    public void setLINENO(String LINENO) {
        this.LINENO = LINENO;
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

    public String getPRODUCTDEFNAME() {
        return PRODUCTDEFNAME;
    }

    public void setPRODUCTDEFNAME(String PRODUCTDEFNAME) {
        this.PRODUCTDEFNAME = PRODUCTDEFNAME;
    }

    public String getCONTAINERSEQ() {
        return CONTAINERSEQ;
    }

    public void setCONTAINERSEQ(String CONTAINERSEQ) {
        this.CONTAINERSEQ = CONTAINERSEQ;
    }

    public String getPOID() {
        return POID;
    }

    public void setPOID(String POID) {
        this.POID = POID;
    }

    public String getCONTAINERSPEC() {
        return CONTAINERSPEC;
    }

    public void setCONTAINERSPEC(String CONTAINERSPEC) {
        this.CONTAINERSPEC = CONTAINERSPEC;
    }

    public String getCONTAINERNO() {
        return CONTAINERNO;
    }

    public void setCONTAINERNO(String CONTAINERNO) {
        this.CONTAINERNO = CONTAINERNO;
    }

    public String getSEALNO() {
        return SEALNO;
    }

    public void setSEALNO(String SEALNO) {
        this.SEALNO = SEALNO;
    }

    public String getCOMPLETETIME() {
        return COMPLETETIME;
    }

    public void setCOMPLETETIME(String COMPLETETIME) {
        this.COMPLETETIME = COMPLETETIME;
    }

    public String getPLANQTY() {
        return PLANQTY;
    }

    public void setPLANQTY(String PLANQTY) {
        this.PLANQTY = PLANQTY;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
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

    public String getLOADEDQTY() {
        return LOADEDQTY;
    }

    public void setLOADEDQTY(String LOADEDQTY) {
        this.LOADEDQTY = LOADEDQTY;
    }
}
