package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Created by huangjiechun on 2018/1/2.
 */


/*

WAREHOUSEID
WAREHOUSENAME
CHECKMONTH
STATE
STATENAME
STARTDATE
ENDDATE
 */
public class UploadStockCheckData implements Serializable {
    private String WAREHOUSEID;
    private String WAREHOUSENAME;
    private String CHECKMONTH;
    private String STATE;
    private String STATENAME;
    private String STARTDATE;
    private String ENDDATE;

    public String getWAREHOUSEID() {
        return WAREHOUSEID;
    }

    public void setWAREHOUSEID(String WAREHOUSEID) {
        this.WAREHOUSEID = WAREHOUSEID;
    }

    public String getWAREHOUSENAME() {
        return WAREHOUSENAME;
    }

    public void setWAREHOUSENAME(String WAREHOUSENAME) {
        this.WAREHOUSENAME = WAREHOUSENAME;
    }

    public String getCHECKMONTH() {
        return CHECKMONTH;
    }

    public void setCHECKMONTH(String CHECKMONTH) {
        this.CHECKMONTH = CHECKMONTH;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getSTATENAME() {
        return STATENAME;
    }

    public void setSTATENAME(String STATENAME) {
        this.STATENAME = STATENAME;
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
}
