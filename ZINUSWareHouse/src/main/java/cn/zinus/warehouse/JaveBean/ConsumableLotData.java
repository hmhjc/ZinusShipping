package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 18:50
 * Main Change:
 */

public class ConsumableLotData implements Serializable {
    private String CONSUMABLELOTID;
    private String WAREHOUSEID;
    private String CONSUMABLEDEFID;
    private String CONSUMABLEDEFVERSION;
    private String CREATEDQTY;
    private String QTY;
    private String RFID;

    public String getCONSUMABLELOTID() {
        return CONSUMABLELOTID;
    }

    public void setCONSUMABLELOTID(String CONSUMABLELOTID) {
        this.CONSUMABLELOTID = CONSUMABLELOTID;
    }

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
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

    public String getCREATEDQTY() {
        return CREATEDQTY;
    }

    public void setCREATEDQTY(String CREATEDQTY) {
        this.CREATEDQTY = CREATEDQTY;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getRFID() {
        return RFID;
    }

    public void setRFID(String RFID) {
        this.RFID = RFID;
    }
}
