package cn.zinus.warehouse.Fragment;

import cn.zinus.warehouse.JaveBean.ConsumeInboundData;
import cn.zinus.warehouse.JaveBean.ConsumeLotInboundData;
import cn.zinus.warehouse.JaveBean.ConsumeOutboundData;
import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.JaveBean.StockCheckDeatilData;

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
    public static class ConsumeInboundByOrderEvent {
        private String mInboundOrderNo;

        public ConsumeInboundByOrderEvent(String mInboundOrderNo) {
            this.mInboundOrderNo = mInboundOrderNo;
        }

        public String getInboundOrderNo() {
            return mInboundOrderNo;
        }
    }

    //单击ConsumeInbound跳转到lot
    public static class ConsumeLotInboundByConsumeDefIDEvent {
        private ConsumeInboundData mConsumeInboundData;

        public ConsumeLotInboundByConsumeDefIDEvent(ConsumeInboundData mConsumeInboundData) {
            this.mConsumeInboundData = mConsumeInboundData;
        }

        public ConsumeInboundData getConsumeInboundData() {
            return mConsumeInboundData;
        }
    }

    //ConsumeLot数据变化之后，修改Consume的数据
    public static class ConsumeInboundbyLotCheckEvent {
        private ConsumeLotInboundData mConsumeLotInboundData;
        private String Sumqty;

        public ConsumeInboundbyLotCheckEvent(ConsumeLotInboundData mConsumeLotInboundData, String Sumqty) {
            this.mConsumeLotInboundData = mConsumeLotInboundData;
            this.Sumqty = Sumqty;
        }

        public ConsumeLotInboundData getConsumeLotInboundData() {
            return mConsumeLotInboundData;
        }

        public String getSumqty() {
            return Sumqty;
        }
    }

    //单击consumeRequest跳转到Consumeoutbound
    public static class ConsumeOutboundByConsumeRequestEvent {
        private String mConsumeRequestNo;

        public ConsumeOutboundByConsumeRequestEvent(String mConsumeRequestNo) {
            this.mConsumeRequestNo = mConsumeRequestNo;
        }

        public String getConsumeRequestNo() {
            return mConsumeRequestNo;
        }
    }

    //单击ConsumeOutbound跳转到lot
    public static class ConsumeLotOutboundByConsumeDefIDEvent {
        private ConsumeOutboundData mConsumeOutboundData;

        public ConsumeLotOutboundByConsumeDefIDEvent(ConsumeOutboundData mConsumeOutboundData) {
            this.mConsumeOutboundData = mConsumeOutboundData;
        }

        public ConsumeOutboundData getConsumeOutboundData() {
            return mConsumeOutboundData;
        }
    }

    //单击stockcheck跳转到stockcheckdetail
    public static class StockCheckDetailbyCheckMonthEvent {
        private StockCheckData mStockCheck;

        public StockCheckDetailbyCheckMonthEvent(StockCheckData mStockCheck) {
            this.mStockCheck = mStockCheck;
        }

        public StockCheckData getStockCheck() {
            return mStockCheck;
        }
    }

    //单击stockcheckDetail跳转到stockcheckLotdetail
    public static class StockLotCheckDetailbyConsumedefidEvent {
        private StockCheckDeatilData mStockCheckDeatilData;

        public StockLotCheckDetailbyConsumedefidEvent(StockCheckDeatilData mStockCheckDeatilData) {
            this.mStockCheckDeatilData = mStockCheckDeatilData;
        }

        public StockCheckDeatilData getStockCheckDeatilData() {
            return mStockCheckDeatilData;
        }
    }

    //stockcheckLotdetail数据变化之后，修改stockcheckdetail的数据
    public static class StockCheckDetailbyLotCheckEvent {
        private StockCheckDeatilData mStockLotCheckDeatilData;

        public StockCheckDetailbyLotCheckEvent(StockCheckDeatilData mStockLotCheckDeatilData) {
            this.mStockLotCheckDeatilData = mStockLotCheckDeatilData;
        }

        public StockCheckDeatilData getStockLotCheckDeatilData() {
            return mStockLotCheckDeatilData;
        }
    }

    //刷新画面
    public static class RefreshActivityEvent {
        public RefreshActivityEvent() {

        }
    }


}