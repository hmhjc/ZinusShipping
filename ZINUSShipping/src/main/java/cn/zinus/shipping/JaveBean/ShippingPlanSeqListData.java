package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Created by huangjiechun on 2018/1/28.
 */

public class ShippingPlanSeqListData implements Serializable {

    private String CONTAINERSEQ;
    private String SHIPPINGPLANSEQ;
    private String POID;
    private String PRODUCTDEFID;
    private String PRODUCTDEFVERSION;
    private String PRODUCTDEFNAME;
    private String QTY;
    private String CONTAINERNO;
    private String SEALNO;
    private String ORDERNO;
    private String ORDERTYPE;
    private String LINENO;
    private String SHIPPINGEDQTY;
    private String NOSHIPPINGEDQTY;

    public String getCONTAINERSEQ() {
        return CONTAINERSEQ;
    }

    public void setCONTAINERSEQ(String CONTAINERSEQ) {
        this.CONTAINERSEQ = CONTAINERSEQ;
    }

    public String getSHIPPINGEDQTY() {
        return SHIPPINGEDQTY;
    }

    public void setSHIPPINGEDQTY(String SHIPPINGEDQTY) {
        this.SHIPPINGEDQTY = SHIPPINGEDQTY;
    }

    public String getNOSHIPPINGEDQTY() {
        return NOSHIPPINGEDQTY;
    }

    public void setNOSHIPPINGEDQTY(String NOSHIPPINGEDQTY) {
        this.NOSHIPPINGEDQTY = NOSHIPPINGEDQTY;
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

    public String getSHIPPINGPLANSEQ() {
        return SHIPPINGPLANSEQ;
    }

    public void setSHIPPINGPLANSEQ(String SHIPPINGPLANSEQ) {
        this.SHIPPINGPLANSEQ = SHIPPINGPLANSEQ;
    }

    public String getPOID() {
        return POID;
    }

    public void setPOID(String POID) {
        this.POID = POID;
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

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
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
}
