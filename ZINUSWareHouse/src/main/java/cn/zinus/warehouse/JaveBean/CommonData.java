package cn.zinus.warehouse.JaveBean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * Developer:Spring
 * DataTime :2017/8/22 09:54
 * Main Change:
 */

public class CommonData implements Serializable {
    private String DESCRIPTION;
    private String CREATEDTIME;
    private String CREATOR;
    private String MODIFIEDTIME;
    private String MODIFIER;
    private String VALIDSTATE;
    private String _COMMAND;
    private String _LANGUAGE;


    public String getCommomData() {
        return "CommonData{" +
                "DESCRIPTION='" + DESCRIPTION + '\'' +
                ", CREATEDTIME='" + CREATEDTIME + '\'' +
                ", CREATOR='" + CREATOR + '\'' +
                ", MODIFIEDTIME='" + MODIFIEDTIME + '\'' +
                ", MODIFIER='" + MODIFIER + '\'' +
                ", VALIDSTATE='" + VALIDSTATE + '\'' +
                _LANGUAGE+
                '}';
    }

    public void setTransactionDataWithJsonObject(JSONObject jsonObject) {
        try {
            this.DESCRIPTION = jsonObject.getString("DESCRIPTION");
            this.CREATEDTIME = jsonObject.getString("CREATEDTIME");
            this.CREATOR = jsonObject.getString("CREATOR");
            this.MODIFIEDTIME = jsonObject.getString("MODIFIEDTIME");
            this.MODIFIER = jsonObject.getString("MODIFIER");
            this.VALIDSTATE = jsonObject.getString("VALIDSTATE");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String get_COMMAND() {
        return _COMMAND;
    }

    public void set_COMMAND(String _COMMAND) {
        this._COMMAND = _COMMAND;
    }

    public String get_LANGUAGE() {
        return _LANGUAGE;
    }

    public void set_LANGUAGE(String _LANGUAGE) {
        this._LANGUAGE = _LANGUAGE;
    }

    public String getDESCRIPTION() {
        return DESCRIPTION;
    }

    public void setDESCRIPTION(String DESCRIPTION) {
        this.DESCRIPTION = DESCRIPTION;
    }

    public String getCREATEDTIME() {
        return CREATEDTIME;
    }

    public void setCREATEDTIME(String CREATEDTIME) {
        this.CREATEDTIME = CREATEDTIME;
    }

    public String getCREATOR() {
        return CREATOR;
    }

    public void setCREATOR(String CREATOR) {
        this.CREATOR = CREATOR;
    }

    public String getMODIFIEDTIME() {
        return MODIFIEDTIME;
    }

    public void setMODIFIEDTIME(String MODIFIEDTIME) {
        this.MODIFIEDTIME = MODIFIEDTIME;
    }

    public String getMODIFIER() {
        return MODIFIER;
    }

    public void setMODIFIER(String MODIFIER) {
        this.MODIFIER = MODIFIER;
    }

    public String getVALIDSTATE() {
        return VALIDSTATE;
    }

    public void setVALIDSTATE(String VALIDSTATE) {
        this.VALIDSTATE = VALIDSTATE;
    }

}
