package cn.zinus.warehouse.JaveBean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Spring on 2017/3/6.
 */

public class TagInfoData implements Serializable{
    String ItemID;
    String ItemName;
    String Qty;
    String TagID;
    String Unit;
    boolean enableFlag;
    boolean clearFlag;

    public boolean isClearFlag() {
        return clearFlag;
    }

    public void setClearFlag(boolean clearFlag) {
        this.clearFlag = clearFlag;
    }

    public boolean isEnableFlag() {
        return enableFlag;
    }

    public void setEnableFlag(boolean enableFlag) {
        this.enableFlag = enableFlag;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public static List<TagInfoData> TagDatas = new ArrayList<TagInfoData>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TagInfoData that = (TagInfoData) o;

        return TagID.equals(that.TagID);

    }

    @Override
    public int hashCode() {
        return TagID.hashCode();
    }

    public TagInfoData() {
    }

    public TagInfoData(String itemID, String itemName, String qty, String tagID) {
        ItemID = itemID;
        ItemName = itemName;
        Qty = qty;
        TagID = tagID;
    }

    public String getItemID() {
        return ItemID;
    }

    public void setItemID(String itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getQty() {
        return Qty;
    }

    public void setQty(String qty) {
        Qty = qty;
    }

    public String getTagID() {
        return TagID;
    }

    public void setTagID(String tagID) {
        TagID = tagID;
    }
}
