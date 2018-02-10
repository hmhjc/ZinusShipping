package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/22 16:57
 * Main Change:
 */

public class StockCheckDeatilData implements Serializable {
    private String WAREHOUSEID;
    private String CHECKMONTH;
    private String CONSUMEABLDEFID;
    private String CONSUMEABLDEFVERSION;
    private String UNIT;
    private String QTY;
    private String USERID;
    private String CHECKUNIT;
    private String CHECKQTY;
    private String VALIDSTATE;
    private String CONSUMEABLDEFNAME;
    private String SPEC_DESC;
    private String DESCRIPTION;
    private int backgroundColor;

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getSPEC_DESC() {
        return SPEC_DESC;
    }

    public void setSPEC_DESC(String SPEC_DESC) {
        this.SPEC_DESC = SPEC_DESC;
    }

    public int getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getCONSUMEABLDEFNAME() {
        return CONSUMEABLDEFNAME;
    }

    public void setCONSUMEABLDEFNAME(String CONSUMEABLDEFNAME) {
        this.CONSUMEABLDEFNAME = CONSUMEABLDEFNAME;
    }

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
    }

    public String getCHECKMONTH() {
        return CHECKMONTH;
    }

    public void setCHECKMONTH(String CHECKMONTH) {
        this.CHECKMONTH = CHECKMONTH;
    }

    public String getCONSUMEABLDEFID() {
        return CONSUMEABLDEFID;
    }

    public void setCONSUMEABLDEFID(String CONSUMEABLDEFID) {
        this.CONSUMEABLDEFID = CONSUMEABLDEFID;
    }

    public String getCONSUMEABLDEFVERSION() {
        return CONSUMEABLDEFVERSION;
    }

    public void setCONSUMEABLDEFVERSION(String CONSUMEABLDEFVERSION) {
        this.CONSUMEABLDEFVERSION = CONSUMEABLDEFVERSION;
    }

    public String getUNIT() {
        return UNIT;
    }

    public void setUNIT(String UNIT) {
        this.UNIT = UNIT;
    }

    public String getQTY() {
        return QTY;
    }

    public void setQTY(String QTY) {
        this.QTY = QTY;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getCHECKUNIT() {
        return CHECKUNIT;
    }

    public void setCHECKUNIT(String CHECKUNIT) {
        this.CHECKUNIT = CHECKUNIT;
    }

    public String getCHECKQTY() {
        return CHECKQTY;
    }

    public void setCHECKQTY(String CHECKQTY) {
        this.CHECKQTY = CHECKQTY;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
