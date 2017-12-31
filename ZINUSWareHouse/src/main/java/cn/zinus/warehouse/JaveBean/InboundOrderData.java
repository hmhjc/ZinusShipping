package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/12 14:47
 * Main Change:
 */

public class InboundOrderData implements Serializable{
    private String INBOUNDNO;
    private String WAREHOUSEID;
    private String INBOUNDDATE;
    private String INBOUNDSTATE;
    private String INSPECTIONRESULT;
    private String VALIDSTATE;
    private String URGENCYTYPE;
    private String SCHEDULEDATE;
    private String TEMPINBOUNDDATE;
    private String WAREHOUSENAME;
    private String STATENAME;
    private String CONSUMABLECOUNT;

    @Override
    public String toString() {
        return "InboundOrderData{" +
                "INBOUNDNO='" + INBOUNDNO + '\'' +
                ", WAREHOUSEID='" + WAREHOUSEID + '\'' +
                ", INBOUNDDATE='" + INBOUNDDATE + '\'' +
                ", INBOUNDSTATE='" + INBOUNDSTATE + '\'' +
                ", INSPECTIONRESULT='" + INSPECTIONRESULT + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                ", URGENCYTYPE='" + URGENCYTYPE + '\'' +
                ", SCHEDULEDATE='" + SCHEDULEDATE + '\'' +
                ", TEMPINBOUNDDATE='" + TEMPINBOUNDDATE + '\'' +
                ", WAREHOUSENAME='" + WAREHOUSENAME + '\'' +
                ", STATENAME='" + STATENAME + '\'' +
                ", CONSUMABLECOUNT='" + CONSUMABLECOUNT + '\'' +
                '}';
    }

    public String getWAREHOUSENAME() {
        return WAREHOUSENAME;
    }

    public void setWAREHOUSENAME(String WAREHOUSENAME) {
        this.WAREHOUSENAME = WAREHOUSENAME;
    }

    public String getSTATENAME() {
        return STATENAME;
    }

    public void setSTATENAME(String STATENAME) {
        this.STATENAME = STATENAME;
    }

    public String getCONSUMABLECOUNT() {
        return CONSUMABLECOUNT;
    }

    public void setCONSUMABLECOUNT(String CONSUMABLECOUNT) {
        this.CONSUMABLECOUNT = CONSUMABLECOUNT;
    }

    public InboundOrderData() {
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

    public String getINBOUNDDATE() {
        return INBOUNDDATE;
    }

    public void setINBOUNDDATE(String INBOUNDDATE) {
        this.INBOUNDDATE = INBOUNDDATE;
    }

    public String getINBOUNDSTATE() {
        return INBOUNDSTATE;
    }

    public void setINBOUNDSTATE(String INBOUNDSTATE) {
        this.INBOUNDSTATE = INBOUNDSTATE;
    }

    public String getINSPECTIONRESULT() {
        return INSPECTIONRESULT;
    }

    public void setINSPECTIONRESULT(String INSPECTIONRESULT) {
        this.INSPECTIONRESULT = INSPECTIONRESULT;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getURGENCYTYPE() {
        return URGENCYTYPE;
    }

    public void setURGENCYTYPE(String URGENCYTYPE) {
        this.URGENCYTYPE = URGENCYTYPE;
    }

    public String getSCHEDULEDATE() {
        return SCHEDULEDATE;
    }

    public void setSCHEDULEDATE(String SCHEDULEDATE) {
        this.SCHEDULEDATE = SCHEDULEDATE;
    }

    public String getTEMPINBOUNDDATE() {
        return TEMPINBOUNDDATE;
    }

    public void setTEMPINBOUNDDATE(String TEMPINBOUNDDATE) {
        this.TEMPINBOUNDDATE = TEMPINBOUNDDATE;
    }


}
