package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/11/9 18:35
 * Main Change:
 */

public class lotData implements Serializable {
    private String LOTID;
    private String SALESORDERID;
    private String PROCESSSEGMENTID;
    private String LOTSTATE;
    private String VALIDSTATE;
    private String QTY;
    private String RFID;

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

    public String getSALESORDERID() {
        return SALESORDERID;
    }

    public void setSALESORDERID(String SALESORDERID) {
        this.SALESORDERID = SALESORDERID;
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
