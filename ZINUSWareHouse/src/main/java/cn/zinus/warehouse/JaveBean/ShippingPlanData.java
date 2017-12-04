package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 13:45
 * Main Change:
 */

public class ShippingPlanData implements Serializable {

    private String SHIPPINGPLANNO;
    private String PRODUCTIONORDERNAME;
    private String PLANTID;
    private String CUSTOMERID;
    private String PRODUCTDEFID;
    private String PRODUCTDEFVERSION;
    private String PLANSTARTTIME;
    private String PLANENDTIME;
    private String PLANQTY;
    private String CONTAINERSPEC;
    private String WORKINGSHIFT;
    private String AREAID;
    private String STATE;
    private String DESCRIPTION;
    private String CREATOR;
    private String CREATEDTIME;
    private String MODIFIER;
    private String MODIFIEDTIME;
    private String LASTTXNID;
    private String LASTTXNUSER;
    private String LASTTXNTIME;
    private String LASTTXNCOMMENT;
    private String LASTTXNHISTKEY;
    private String LASTGROUPHISTKEY;
    private String VALIDSTATE;

    @Override
    public String toString() {
        return "ShippingPlanData{" +
                "SHIPPINGPLANNO='" + SHIPPINGPLANNO + '\'' +
                ", PRODUCTIONORDERNAME='" + PRODUCTIONORDERNAME + '\'' +
                ", PLANTID='" + PLANTID + '\'' +
                ", CUSTOMERID='" + CUSTOMERID + '\'' +
                ", PRODUCTDEFID='" + PRODUCTDEFID + '\'' +
                ", PRODUCTDEFVERSION='" + PRODUCTDEFVERSION + '\'' +
                ", PLANSTARTTIME='" + PLANSTARTTIME + '\'' +
                ", PLANENDTIME='" + PLANENDTIME + '\'' +
                ", PLANQTY='" + PLANQTY + '\'' +
                ", CONTAINERSPEC='" + CONTAINERSPEC + '\'' +
                ", WORKINGSHIFT='" + WORKINGSHIFT + '\'' +
                ", AREAID='" + AREAID + '\'' +
                ", STATE='" + STATE + '\'' +
                ", DESCRIPTION='" + DESCRIPTION + '\'' +
                ", CREATOR='" + CREATOR + '\'' +
                ", CREATEDTIME='" + CREATEDTIME + '\'' +
                ", MODIFIER='" + MODIFIER + '\'' +
                ", MODIFIEDTIME='" + MODIFIEDTIME + '\'' +
                ", LASTTXNID='" + LASTTXNID + '\'' +
                ", LASTTXNUSER='" + LASTTXNUSER + '\'' +
                ", LASTTXNTIME='" + LASTTXNTIME + '\'' +
                ", LASTTXNCOMMENT='" + LASTTXNCOMMENT + '\'' +
                ", LASTTXNHISTKEY='" + LASTTXNHISTKEY + '\'' +
                ", LASTGROUPHISTKEY='" + LASTGROUPHISTKEY + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                '}';
    }

    public String getSHIPPINGPLANNO() {
        return SHIPPINGPLANNO;
    }

    public void setSHIPPINGPLANNO(String SHIPPINGPLANNO) {
        this.SHIPPINGPLANNO = SHIPPINGPLANNO;
    }

    public String getPRODUCTIONORDERNAME() {
        return PRODUCTIONORDERNAME;
    }

    public void setPRODUCTIONORDERNAME(String PRODUCTIONORDERNAME) {
        this.PRODUCTIONORDERNAME = PRODUCTIONORDERNAME;
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

    public String getPRODUCTDEFID() {
        return PRODUCTDEFID;
    }

    public void setPRODUCTDEFID(String PRODUCTDEFID) {
        this.PRODUCTDEFID = PRODUCTDEFID;
    }

    public String getPRODUCTDEFVERSION() {
        return PRODUCTDEFVERSION;
    }

    public void setPRODUCTDEFVERSION(String PRODUCTDEFVERSION) {
        this.PRODUCTDEFVERSION = PRODUCTDEFVERSION;
    }

    public String getPLANSTARTTIME() {
        return PLANSTARTTIME;
    }

    public void setPLANSTARTTIME(String PLANSTARTTIME) {
        this.PLANSTARTTIME = PLANSTARTTIME;
    }

    public String getPLANENDTIME() {
        return PLANENDTIME;
    }

    public void setPLANENDTIME(String PLANENDTIME) {
        this.PLANENDTIME = PLANENDTIME;
    }

    public String getPLANQTY() {
        return PLANQTY;
    }

    public void setPLANQTY(String PLANQTY) {
        this.PLANQTY = PLANQTY;
    }

    public String getCONTAINERSPEC() {
        return CONTAINERSPEC;
    }

    public void setCONTAINERSPEC(String CONTAINERSPEC) {
        this.CONTAINERSPEC = CONTAINERSPEC;
    }

    public String getWORKINGSHIFT() {
        return WORKINGSHIFT;
    }

    public void setWORKINGSHIFT(String WORKINGSHIFT) {
        this.WORKINGSHIFT = WORKINGSHIFT;
    }

    public String getAREAID() {
        return AREAID;
    }

    public void setAREAID(String AREAID) {
        this.AREAID = AREAID;
    }

    public String getSTATE() {
        return STATE;
    }

    public void setSTATE(String STATE) {
        this.STATE = STATE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String CREATOR) {
        this.CREATOR = CREATOR;
    }

    public String getCREATEDTIME() {
        return CREATEDTIME;
    }

    public void setCREATEDTIME(String CREATEDTIME) {
        this.CREATEDTIME = CREATEDTIME;
    }

    public String getMODIFIER() {
        return MODIFIER;
    }

    public void setMODIFIER(String MODIFIER) {
        this.MODIFIER = MODIFIER;
    }

    public String getMODIFIEDTIME() {
        return MODIFIEDTIME;
    }

    public void setMODIFIEDTIME(String MODIFIEDTIME) {
        this.MODIFIEDTIME = MODIFIEDTIME;
    }

    public String getLASTTXNID() {
        return LASTTXNID;
    }

    public void setLASTTXNID(String LASTTXNID) {
        this.LASTTXNID = LASTTXNID;
    }

    public String getLASTTXNUSER() {
        return LASTTXNUSER;
    }

    public void setLASTTXNUSER(String LASTTXNUSER) {
        this.LASTTXNUSER = LASTTXNUSER;
    }

    public String getLASTTXNTIME() {
        return LASTTXNTIME;
    }

    public void setLASTTXNTIME(String LASTTXNTIME) {
        this.LASTTXNTIME = LASTTXNTIME;
    }

    public String getLASTTXNCOMMENT() {
        return LASTTXNCOMMENT;
    }

    public void setLASTTXNCOMMENT(String LASTTXNCOMMENT) {
        this.LASTTXNCOMMENT = LASTTXNCOMMENT;
    }

    public String getLASTTXNHISTKEY() {
        return LASTTXNHISTKEY;
    }

    public void setLASTTXNHISTKEY(String LASTTXNHISTKEY) {
        this.LASTTXNHISTKEY = LASTTXNHISTKEY;
    }

    public String getLASTGROUPHISTKEY() {
        return LASTGROUPHISTKEY;
    }

    public void setLASTGROUPHISTKEY(String LASTGROUPHISTKEY) {
        this.LASTGROUPHISTKEY = LASTGROUPHISTKEY;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }
}
