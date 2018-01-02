package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/21 13:29
 * Main Change:
 */

public class StockCheckData implements Serializable {
    private String WAREHOUSEID;
    private String WAREHOUSENAME;
    private String CHECKMONTH;
    private String STARTDATE;
    private String ENDDATE;
    private String STATE;
    private String STATENAME;
    private String VALIDSTATE;

    public String getWAREHOUSENAME() {
        return WAREHOUSENAME;
    }

    public void setWAREHOUSENAME(String WAREHOUSENAME) {
        this.WAREHOUSENAME = WAREHOUSENAME;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
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

    public String getSTARTDATE() {
        return STARTDATE;
    }

    public void setSTARTDATE(String STARTDATE) {
        this.STARTDATE = STARTDATE;
    }

    public String getENDDATE() {
        return ENDDATE;
    }

    public void setENDDATE(String ENDDATE) {
        this.ENDDATE = ENDDATE;
    }

    public String getSTATENAME() {
        return STATENAME;
    }

    public void setSTATENAME(String STATENAME) {
        this.STATENAME = STATENAME;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
