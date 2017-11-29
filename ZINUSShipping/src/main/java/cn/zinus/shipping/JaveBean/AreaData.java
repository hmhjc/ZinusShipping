package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/29 09:30
 * Main Change:
 */

public class AreaData implements Serializable {
    private String AREAID;
    private String LOCATIONID;
    private String PLANTID;
    private String AREANAME;
    private String AREATYPE;
    private String PARENTAREAID;
    private String DESCRIPTION;
    private String DICTIONARYID;
    private String CREATOR;
    private String CREATEDTIME;
    private String MODIFIER;
    private String MODIFIEDTIME;
    private String VALIDSTATE;
    private String PROCESSSEGMENTID;
    private String PROCESSSEGMENTTVERSION;

    public AreaData() {
    }

    public AreaData(String AREAID, String AREANAME) {
        this.AREAID = AREAID;
        this.AREANAME = AREANAME;
    }

    @Override
    public String toString() {
        return  AREANAME ;
    }

    public String getAREAID() {
        return AREAID;
    }

    public void setAREAID(String AREAID) {
        this.AREAID = AREAID;
    }

    public String getLOCATIONID() {
        return LOCATIONID;
    }

    public void setLOCATIONID(String LOCATIONID) {
        this.LOCATIONID = LOCATIONID;
    }

    public String getPLANTID() {
        return PLANTID;
    }

    public void setPLANTID(String PLANTID) {
        this.PLANTID = PLANTID;
    }

    public String getAREANAME() {
        return AREANAME;
    }

    public void setAREANAME(String AREANAME) {
        this.AREANAME = AREANAME;
    }

    public String getAREATYPE() {
        return AREATYPE;
    }

    public void setAREATYPE(String AREATYPE) {
        this.AREATYPE = AREATYPE;
    }

    public String getPARENTAREAID() {
        return PARENTAREAID;
    }

    public void setPARENTAREAID(String PARENTAREAID) {
        this.PARENTAREAID = PARENTAREAID;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getDICTIONARYID() {
        return DICTIONARYID;
    }

    public void setDICTIONARYID(String DICTIONARYID) {
        this.DICTIONARYID = DICTIONARYID;
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

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getPROCESSSEGMENTID() {
        return PROCESSSEGMENTID;
    }

    public void setPROCESSSEGMENTID(String PROCESSSEGMENTID) {
        this.PROCESSSEGMENTID = PROCESSSEGMENTID;
    }

    public String getPROCESSSEGMENTTVERSION() {
        return PROCESSSEGMENTTVERSION;
    }

    public void setPROCESSSEGMENTTVERSION(String PROCESSSEGMENTTVERSION) {
        this.PROCESSSEGMENTTVERSION = PROCESSSEGMENTTVERSION;
    }
}
