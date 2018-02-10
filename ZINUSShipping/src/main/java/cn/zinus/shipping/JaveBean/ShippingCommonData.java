package cn.zinus.shipping.JaveBean;

import java.io.Serializable;

/**
 * Created by huangjiechun on 2018/2/6.
 */

public class ShippingCommonData implements Serializable {
    private String BOOKINGNO;
    private String ShippingEndPlanDate;
    private String ContainerNo;
    private String SEALNO;

    public String getBOOKINGNO() {
        return BOOKINGNO;
    }

    public void setBOOKINGNO(String BOOKINGNO) {
        this.BOOKINGNO = BOOKINGNO;
    }

    public String getShippingEndPlanDate() {
        return ShippingEndPlanDate;
    }

    public void setShippingEndPlanDate(String shippingEndPlanDate) {
        ShippingEndPlanDate = shippingEndPlanDate;
    }

    public String getContainerNo() {
        return ContainerNo;
    }

    public void setContainerNo(String containerNo) {
        ContainerNo = containerNo;
    }

    public String getSEALNO() {
        return SEALNO;
    }

    public void setSEALNO(String SEALNO) {
        this.SEALNO = SEALNO;
    }
}
