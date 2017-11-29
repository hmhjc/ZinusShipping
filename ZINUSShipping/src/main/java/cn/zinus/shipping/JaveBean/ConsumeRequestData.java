package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/19 20:28
 * Main Change:
 */

public class ConsumeRequestData implements Serializable {
    private String CONSUMEREQNO;
    private String AREAID;
    private String USERID;
    private String REQUESTDATE;
    private String OUTBOUNDSTATE;
    private String VALIDSTATE;

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getCONSUMEREQNO() {
        return CONSUMEREQNO;
    }

    public void setCONSUMEREQNO(String CONSUMEREQNO) {
        this.CONSUMEREQNO = CONSUMEREQNO;
    }

    public String getAREAID() {
        return AREAID;
    }

    public void setAREAID(String AREAID) {
        this.AREAID = AREAID;
    }

    public String getUSERID() {
        return USERID;
    }

    public void setUSERID(String USERID) {
        this.USERID = USERID;
    }

    public String getREQUESTDATE() {
        return REQUESTDATE;
    }

    public void setREQUESTDATE(String REQUESTDATE) {
        this.REQUESTDATE = REQUESTDATE;
    }

    public String getOUTBOUNDSTATE() {
        return OUTBOUNDSTATE;
    }

    public void setOUTBOUNDSTATE(String OUTBOUNDSTATE) {
        this.OUTBOUNDSTATE = OUTBOUNDSTATE;
    }
}
