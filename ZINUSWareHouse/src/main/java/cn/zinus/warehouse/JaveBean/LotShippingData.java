package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/26 13:48
 * Main Change:
 */

public class LotShippingData implements Serializable {
private String LOTID;
private String SHIPPINGPLANNO;
private String CONTAINERNO;
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

    public String getLOTID() {
        return LOTID;
    }

    public void setLOTID(String LOTID) {
        this.LOTID = LOTID;
    }

    public String getSHIPPINGPLANNO() {
        return SHIPPINGPLANNO;
    }

    public void setSHIPPINGPLANNO(String SHIPPINGPLANNO) {
        this.SHIPPINGPLANNO = SHIPPINGPLANNO;
    }

    public String getCONTAINERNO() {
        return CONTAINERNO;
    }

    public void setCONTAINERNO(String CONTAINERNO) {
        this.CONTAINERNO = CONTAINERNO;
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
