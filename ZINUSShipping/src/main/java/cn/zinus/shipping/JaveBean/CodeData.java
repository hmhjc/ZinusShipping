package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/9/12 10:50
 * Main Change:
 */

public class CodeData implements Serializable {
    private String CODEID;
    private String CODECLASSID;
    private String DICTIONARYNAME;
    private String LANGUAGETYPE;
    private String VALIDSTATE;


    public CodeData() {
    }

    public CodeData(String CODEID, String DICTIONARYNAME) {
        this.CODEID = CODEID;
        this.DICTIONARYNAME = DICTIONARYNAME;
    }

    @Override
    public String toString() {
        return  DICTIONARYNAME;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

    public String getCODEID() {
        return CODEID;
    }

    public void setCODEID(String CODEID) {
        this.CODEID = CODEID;
    }

    public String getCODECLASSID() {
        return CODECLASSID;
    }

    public void setCODECLASSID(String CODECLASSID) {
        this.CODECLASSID = CODECLASSID;
    }

    public String getDICTIONARYNAME() {
        return DICTIONARYNAME;
    }

    public void setDICTIONARYNAME(String DICTIONARYNAME) {
        this.DICTIONARYNAME = DICTIONARYNAME;
    }

    public String getLANGUAGETYPE() {
        return LANGUAGETYPE;
    }

    public void setLANGUAGETYPE(String LANGUAGETYPE) {
        this.LANGUAGETYPE = LANGUAGETYPE;
    }

}
