package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/11/9 18:35
 * Main Change:
 */

public class LotData implements Serializable {
    private String LOTID;
    private String PURCHASEORDERID;
    private String PROCESSSEGMENTID;
    private String LOTSTATE;
    private String VALIDSTATE;
    private String QTY;
    private String RFID;
    private String ISPDASHIPPING;
    private String TRACKOUTTIME;
    private String PRODUCTDEFID;
    private String PRODUCTDEFNAME;

    public String getPRODUCTDEFID() {
        return PRODUCTDEFID;
    }

    public void setPRODUCTDEFID(String PRODUCTDEFID) {
        this.PRODUCTDEFID = PRODUCTDEFID;
    }

    public String getPRODUCTDEFNAME() {
        return PRODUCTDEFNAME;
    }

    public void setPRODUCTDEFNAME(String PRODUCTDEFNAME) {
        this.PRODUCTDEFNAME = PRODUCTDEFNAME;
    }

    @Override
    public String toString() {
        return "LotData{" +
                "LOTID='" + LOTID + '\'' +
                ", PURCHASEORDERID='" + PURCHASEORDERID + '\'' +
                ", PROCESSSEGMENTID='" + PROCESSSEGMENTID + '\'' +
                ", LOTSTATE='" + LOTSTATE + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                ", QTY='" + QTY + '\'' +
                ", RFID='" + RFID + '\'' +
                ", ISPDASHIPPING='" + ISPDASHIPPING + '\'' +
                ", TRACKOUTTIME='" + TRACKOUTTIME + '\'' +
                ", PRODUCTDEFID='" + PRODUCTDEFID + '\'' +
                ", PRODUCTDEFNAME='" + PRODUCTDEFNAME + '\'' +
                '}';
    }

    public String getTRACKOUTTIME() {
        return TRACKOUTTIME;
    }

    public void setTRACKOUTTIME(String TRACKOUTTIME) {
        this.TRACKOUTTIME = TRACKOUTTIME;
    }

    public String getISPDASHIPPING() {
        return ISPDASHIPPING;
    }

    public void setISPDASHIPPING(String ISPDASHIPPING) {
        this.ISPDASHIPPING = ISPDASHIPPING;
    }

    public String getPURCHASEORDERID() {
        return PURCHASEORDERID;
    }

    public void setPURCHASEORDERID(String PURCHASEORDERID) {
        this.PURCHASEORDERID = PURCHASEORDERID;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
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

    public String getPROCESSSEGMENTID() {
        return PROCESSSEGMENTID;
    }

    public void setPROCESSSEGMENTID(String PROCESSSEGMENTID) {
        this.PROCESSSEGMENTID = PROCESSSEGMENTID;
    }

    public String getLOTSTATE() {
        return LOTSTATE;
    }

    public void setLOTSTATE(String LOTSTATE) {
        this.LOTSTATE = LOTSTATE;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
