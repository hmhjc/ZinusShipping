package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/11/8 11:52
 * Main Change:
 */

public class LotShippingListData implements Serializable {
    private String LOTID;
    private String PRODUCTDEFID;
    private String PRODUCTDEFNAME;
    private String INQTY;
    private String TRACKOUTTIME;
    private String TAGID;
    private String VALIDSTATE;

    @Override
    public String toString() {
        return "LotShippingListData{" +
                "LOTID='" + LOTID + '\'' +
                ", PRODUCTDEFID='" + PRODUCTDEFID + '\'' +
                ", PRODUCTDEFNAME='" + PRODUCTDEFNAME + '\'' +
                ", INQTY='" + INQTY + '\'' +
                ", TRACKOUTTIME='" + TRACKOUTTIME + '\'' +
                ", TAGID='" + TAGID + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                '}';
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LotShippingListData)) return false;

        LotShippingListData that = (LotShippingListData) o;

        return LOTID != null ? LOTID.equals(that.LOTID) : that.LOTID == null;

    }

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

    public String getTRACKOUTTIME() {
        return TRACKOUTTIME;
    }

    public void setTRACKOUTTIME(String TRACKOUTTIME) {
        this.TRACKOUTTIME = TRACKOUTTIME;
    }

    @Override
    public int hashCode() {
        return LOTID != null ? LOTID.hashCode() : 0;
    }

    public String getLOTID() {
        return LOTID;
    }

    public void setLOTID(String LOTID) {
        this.LOTID = LOTID;
    }


    public String getINQTY() {
        return INQTY;
    }

    public void setINQTY(String INQTY) {
        this.INQTY = INQTY;
    }


    public String getTAGID() {
        return TAGID;
    }

    public void setTAGID(String TAGID) {
        this.TAGID = TAGID;
    }
}
