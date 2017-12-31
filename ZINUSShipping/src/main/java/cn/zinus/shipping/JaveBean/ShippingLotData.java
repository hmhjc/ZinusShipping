package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * 上车的lot
 * Created by huangjiechun on 2017/12/15.
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
 LOTID，
 QTY，
 SHIPPINGDATE，
 */

public class ShippingLotData implements Serializable {
    private String SHIPPINGPLANNO ;
    private String SHIPPINGPLANSEQ ;
    private String ORDERTYPE ;
    private String ORDERNO ;
    private String LINENO ;
    private String PRODUCTDEFID ;
    private String PRODUCTDEFVERSION ;
    private String CONTAINERSEQ ;
    private String POID ;
    private String LOTID ;
    private String QTY ;
    private String SHIPPINGDATE ;

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

    public String getLOTID() {
        return LOTID;
    }

    public void setLOTID(String LOTID) {
        this.LOTID = LOTID;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getSHIPPINGDATE() {
        return SHIPPINGDATE;
    }

    public void setSHIPPINGDATE(String SHIPPINGDATE) {
        this.SHIPPINGDATE = SHIPPINGDATE;
    }
}
