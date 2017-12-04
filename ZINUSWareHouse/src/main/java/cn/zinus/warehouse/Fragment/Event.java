package cn.zinus.warehouse.Fragment;

import cn.zinus.warehouse.JaveBean.ShippingPlanData;
import cn.zinus.warehouse.JaveBean.StockCheckData;

/**
 * EventBus
 * Created by Spring on 2017/3/7.
 */

public class Event {
    //跳转到菜单页面的事件
    public static class ToMainMenuEvent {
        public ToMainMenuEvent() {
        }
    }

    //跳转到页面的事件
    public static class ToFragmentEvent {
        private int fragmentID;
        private String fragmentName;

        public ToFragmentEvent(int fragmentID, String fragmentName) {
            this.fragmentID = fragmentID;
            this.fragmentName = fragmentName;
        }

        public int getfragmentID() {
            return fragmentID;
        }

        public String getfragmentName() {
            return fragmentName;
        }
    }

    //Inbound清除OrderItem中的项
    public static class InboundClearOrderItemEvent {
        public InboundClearOrderItemEvent() {
        }
    }

    //Outbound清除OrderItem中的项
    public static class OutboundClearOrderItemEvent {
        public OutboundClearOrderItemEvent() {
        }
    }

    //StockCheck清除OrderItem中的项
    public static class StockCheckClearOrderItemEvent {
        public StockCheckClearOrderItemEvent() {
        }
    }

    //Shipping清除OrderItem中的项
    public static class ShippingClearLotShippingItemEvent {
        public ShippingClearLotShippingItemEvent() {
        }
    }


    //LocationSearch单击ListView的Item之后更新TagListTab中的内容
    public static class SearchErrorMessageEvent {
        private String messageID;
        private int fragmentID;

        public SearchErrorMessageEvent(String messageID, int fragmentID) {
            this.messageID = messageID;
            this.fragmentID = fragmentID;
        }

        public String GetMessageID() {
            return messageID;
        }

        public int getfragmentID() {
            return fragmentID;
        }
    }

    //多国语切换
    public static class ChangeLanguageEvent {
        public ChangeLanguageEvent() {
        }
    }

    //单击InboundOrder跳转到Consumeinbound
    public static class ConsumeInboundByOrderEvent{
        private String mInboundOrderNo;

        public ConsumeInboundByOrderEvent(String mInboundOrderNo) {
            this.mInboundOrderNo = mInboundOrderNo;
        }
        public String getInboundOrderNo() {
            return mInboundOrderNo;
        }
    }

    //单击consumeRequest跳转到Consumeoutbound
    public static class ConsumeOutboundByConsumeRequestEvent{
        private String mConsumeRequestNo;

        public ConsumeOutboundByConsumeRequestEvent(String mConsumeRequestNo) {
            this.mConsumeRequestNo = mConsumeRequestNo;
        }
        public String getConsumeRequestNo() {
            return mConsumeRequestNo;
        }
    }

    //单击stockcheck跳转到stockcheckdetail
    public static class StockCheckDetailbyCheckMonthEvent{
        private StockCheckData mStockCheck;

        public StockCheckDetailbyCheckMonthEvent(StockCheckData mStockCheck) {
            this.mStockCheck = mStockCheck;
        }
        public StockCheckData getStockCheck() {
            return mStockCheck;
        }
    }

    //单击ShippingPlan跳转到LotShipping
    public static class LotShippingByShippingPlanEvent{
        private ShippingPlanData mShippingPlanData;
        public LotShippingByShippingPlanEvent(ShippingPlanData mShippingPlanData){
            this.mShippingPlanData = mShippingPlanData;
        }
        public ShippingPlanData getShippingPlanData(){
            return mShippingPlanData;
        }
    }

    //刷新画面
    public static class  RefreshActivityEvent{
        public RefreshActivityEvent() {
        }
    }


}