package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Created by Spring on 2017/7/5.
 */

public class SpinnerCodeData implements Serializable{
    private String TEXT;
    private String VALUE;

    public SpinnerCodeData() {
    }

    public String getTEXT() {
        return TEXT;
    }

    public void setTEXT(String TEXT) {
        this.TEXT = TEXT;
    }

    public String getVALUE() {
        return VALUE;
    }

    public void setVALUE(String VALUE) {
        this.VALUE = VALUE;
    }
}
