package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/11/8 11:52
 * Main Change:
 */

public class LotShippingListData implements Serializable {
    private String LOTID;
    private String CONTAINER;
    private String INQTY;
    private String SEALNO;
    private String TAGID;
    private String VALIDSTATE;

    @Override
    public String toString() {
        return "LotShippingListData{" +
                "LOTID='" + LOTID + '\'' +
                ", CONTAINER='" + CONTAINER + '\'' +
                ", INQTY='" + INQTY + '\'' +
                ", SEALNO='" + SEALNO + '\'' +
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

    public String getCONTAINER() {
        return CONTAINER;
    }

    public void setCONTAINER(String CONTAINER) {
        this.CONTAINER = CONTAINER;
    }

    public String getINQTY() {
        return INQTY;
    }

    public void setINQTY(String INQTY) {
        this.INQTY = INQTY;
    }

    public String getSEALNO() {
        return SEALNO;
    }

    public void setSEALNO(String SEALNO) {
        this.SEALNO = SEALNO;
    }

    public String getTAGID() {
        return TAGID;
    }

    public void setTAGID(String TAGID) {
        this.TAGID = TAGID;
    }
}
