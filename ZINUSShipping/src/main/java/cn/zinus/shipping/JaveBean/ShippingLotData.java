package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * 上车的lot
 * Created by huangjiechun on 2017/12/15.
 */

public class ShippingLotData implements Serializable {
    private String LOTID;
    private String RFID;
    private String CONTAINERNO;
    private String SHIPPINGPLANSEQ;
    private String CONTAINERDEQ;
    private String SEALNO;
    private String QTY;
    private String VALIDSTATE;

    public String getLOTID() {
        return LOTID;
    }

    public void setLOTID(String LOTID) {
        this.LOTID = LOTID;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }

    public String getCONTAINERNO() {
        return CONTAINERNO;
    }

    public void setCONTAINERNO(String CONTAINERNO) {
        this.CONTAINERNO = CONTAINERNO;
    }

    public String getSHIPPINGPLANSEQ() {
        return SHIPPINGPLANSEQ;
    }

    public void setSHIPPINGPLANSEQ(String SHIPPINGPLANSEQ) {
        this.SHIPPINGPLANSEQ = SHIPPINGPLANSEQ;
    }

    public String getCONTAINERDEQ() {
        return CONTAINERDEQ;
    }

    public void setCONTAINERDEQ(String CONTAINERDEQ) {
        this.CONTAINERDEQ = CONTAINERDEQ;
    }

    public String getSEALNO() {
        return SEALNO;
    }

    public void setSEALNO(String SEALNO) {
        this.SEALNO = SEALNO;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
