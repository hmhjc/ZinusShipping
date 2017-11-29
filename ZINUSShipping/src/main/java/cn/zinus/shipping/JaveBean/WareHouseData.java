package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/10/9 09:20
 * Main Change:
 */

public class WareHouseData implements Serializable {
    private String WAREHOUSEID;
    private String WAREHOUSENAME;
    private String WAREHOUSETYPE;
    private String DESCRIPTION;
    private String DICTIONARYID;
    private String CREATOR;
    private String CREATEDTIME;
    private String MODIFIER;
    private String MODIFIEDTIME;
    private String VALIDSTATE;

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

    public String getWAREHOUSETYPE() {
        return WAREHOUSETYPE;
    }

    public void setWAREHOUSETYPE(String WAREHOUSETYPE) {
        this.WAREHOUSETYPE = WAREHOUSETYPE;
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
}
