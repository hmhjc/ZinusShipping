package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 13:48
 * Main Change:
 */

public class LotShippingData implements Serializable {
    private String LOTID;
    private String SHIPPINGPLANNO;
    private String CONTAINERNO;
    private String SEALNO;
    private String VALIDSTATE;
    private String QTY;
    private String SHIPPINGPLANSEQ;
    private String CONTAINERSEQ;
    private String SHIPPINGDATE;

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

    public String getSHIPPINGDATE() {
        return SHIPPINGDATE;
    }

    public void setSHIPPINGDATE(String SHIPPINGDATE) {
        this.SHIPPINGDATE = SHIPPINGDATE;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getLOTID() {
        return LOTID;
    }

    public void setLOTID(String LOTID) {
        this.LOTID = LOTID;
    }

    public String getSHIPPINGPLANNO() {
        return SHIPPINGPLANNO;
    }

    public void setSHIPPINGPLANNO(String SHIPPINGPLANNO) {
        this.SHIPPINGPLANNO = SHIPPINGPLANNO;
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

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
