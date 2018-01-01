package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 13:45
 * Main Change:
 *
 SHIPPINGPLANNO
 PLANTID
 CUSTOMERID
 BOOKINGNO
 PLANDATE
 SHIPPINGPLANDATE
 SHIPPINGENDPLANDATE
 SHIPPINGENDDATE
 STATE
 ISPDASHIPPING
 ISOISAVE
 */

public class ShippingPlanData implements Serializable {
    private String  SHIPPINGPLANNO ;
    private String  PLANTID ;
    private String  CUSTOMERID ;
    private String  BOOKINGNO ;
    private String  PLANDATE ;
    private String  SHIPPINGPLANDATE ;
    private String  SHIPPINGENDPLANDATE ;
    private String  SHIPPINGENDDATE ;
    private String  STATE ;
    private String  ISPDASHIPPING ;
    private String  ISOISAVE ;

    @Override
    public String toString() {
        return "ShippingPlanData{" +
                "SHIPPINGPLANNO='" + SHIPPINGPLANNO + '\'' +
                ", PLANTID='" + PLANTID + '\'' +
                ", CUSTOMERID='" + CUSTOMERID + '\'' +
                ", BOOKINGNO='" + BOOKINGNO + '\'' +
                ", PLANDATE='" + PLANDATE + '\'' +
                ", SHIPPINGPLANDATE='" + SHIPPINGPLANDATE + '\'' +
                ", SHIPPINGENDPLANDATE='" + SHIPPINGENDPLANDATE + '\'' +
                ", SHIPPINGENDDATE='" + SHIPPINGENDDATE + '\'' +
                ", STATE='" + STATE + '\'' +
                ", ISPDASHIPPING='" + ISPDASHIPPING + '\'' +
                ", ISOISAVE='" + ISOISAVE + '\'' +
                '}';
    }

    public String getSHIPPINGPLANNO() {
        return SHIPPINGPLANNO;
    }

    public void setSHIPPINGPLANNO(String SHIPPINGPLANNO) {
        this.SHIPPINGPLANNO = SHIPPINGPLANNO;
    }

    public String getPLANTID() {
        return PLANTID;
    }

    public void setPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
    }

    public String getCUSTOMERID() {
        return CUSTOMERID;
    }

    public void setCUSTOMERID(String CUSTOMERID) {
        this.CUSTOMERID = CUSTOMERID;
    }

    public String getBOOKINGNO() {
        return BOOKINGNO;
    }

    public void setBOOKINGNO(String BOOKINGNO) {
        this.BOOKINGNO = BOOKINGNO;
    }

    public String getPLANDATE() {
        return PLANDATE;
    }

    public void setPLANDATE(String PLANDATE) {
        this.PLANDATE = PLANDATE;
    }

    public String getSHIPPINGPLANDATE() {
        return SHIPPINGPLANDATE;
    }

    public void setSHIPPINGPLANDATE(String SHIPPINGPLANDATE) {
        this.SHIPPINGPLANDATE = SHIPPINGPLANDATE;
    }

    public String getSHIPPINGENDPLANDATE() {
        return SHIPPINGENDPLANDATE;
    }

    public void setSHIPPINGENDPLANDATE(String SHIPPINGENDPLANDATE) {
        this.SHIPPINGENDPLANDATE = SHIPPINGENDPLANDATE;
    }

    public String getSHIPPINGENDDATE() {
        return SHIPPINGENDDATE;
    }

    public void setSHIPPINGENDDATE(String SHIPPINGENDDATE) {
        this.SHIPPINGENDDATE = SHIPPINGENDDATE;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getISPDASHIPPING() {
        return ISPDASHIPPING;
    }

    public void setISPDASHIPPING(String ISPDASHIPPING) {
        this.ISPDASHIPPING = ISPDASHIPPING;
    }

    public String getISOISAVE() {
        return ISOISAVE;
    }

    public void setISOISAVE(String ISOISAVE) {
        this.ISOISAVE = ISOISAVE;
    }
}
