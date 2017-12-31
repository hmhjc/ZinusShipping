package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Created by huangjiechun on 2017/12/28.
 */

public class UploadInboundOrder implements Serializable{
    private String INBOUNDNO;
    private String WAREHOUSEID;
    private String WAREHOUSENAME;
    private String SCHEDULEDATE;
    private String INBOUNDDATE;
    private String TEMPINBOUNDDATE;
    private String INBOUNDSTATE;
    private String INBOUNDSTATENAME;
    private String CONSUMABLECOUNT;

    public String getINBOUNDSTATENAME() {
        return INBOUNDSTATENAME;
    }

    public void setINBOUNDSTATENAME(String INBOUNDSTATENAME) {
        this.INBOUNDSTATENAME = INBOUNDSTATENAME;
    }

    public String getINBOUNDNO() {
        return INBOUNDNO;
    }

    public void setINBOUNDNO(String INBOUNDNO) {
        this.INBOUNDNO = INBOUNDNO;
    }

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

    public String getSCHEDULEDATE() {
        return SCHEDULEDATE;
    }

    public void setSCHEDULEDATE(String SCHEDULEDATE) {
        this.SCHEDULEDATE = SCHEDULEDATE;
    }

    public String getINBOUNDDATE() {
        return INBOUNDDATE;
    }

    public void setINBOUNDDATE(String INBOUNDDATE) {
        this.INBOUNDDATE = INBOUNDDATE;
    }

    public String getTEMPINBOUNDDATE() {
        return TEMPINBOUNDDATE;
    }

    public void setTEMPINBOUNDDATE(String TEMPINBOUNDDATE) {
        this.TEMPINBOUNDDATE = TEMPINBOUNDDATE;
    }

    public String getINBOUNDSTATE() {
        return INBOUNDSTATE;
    }

    public void setINBOUNDSTATE(String INBOUNDSTATE) {
        this.INBOUNDSTATE = INBOUNDSTATE;
    }

    public String getCONSUMABLECOUNT() {
        return CONSUMABLECOUNT;
    }

    public void setCONSUMABLECOUNT(String CONSUMABLECOUNT) {
        this.CONSUMABLECOUNT = CONSUMABLECOUNT;
    }
}
