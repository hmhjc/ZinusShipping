package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.ShippingPlanData;
import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :2017/11/6 10:03
 * Main Change: ShippingPlan ListView Adapter
 */

public class ShippingPlanListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ShippingPlanData> listMap;

    public final class ListItemView {
        public TextView tvShipplanNo;
        public TextView tvPoNo;
        public TextView tvShippingplanSeq;
        public TextView tvContainerSeq;
        public TextView tvCustomer;
        public TextView tvProduct;
        public TextView tvPlanQty;
        public TextView tvContainerSpec;
        public TextView tvPlanStartTime;
        public TextView tvPlanEndTime;
        public TextView tvState;
    }

    public ShippingPlanListViewAdapter(Context context, List<ShippingPlanData> listMap) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.listMap = listMap;
    }

    @Override
    public int getCount() {
        return listMap.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListItemView listItemView = null;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = mInflater.inflate(R.layout.lv_item_shippingplan, null);
            listItemView.tvShipplanNo= (TextView) convertView.findViewById(R.id.tv_ShipplanNo);
            listItemView.tvShippingplanSeq= (TextView) convertView.findViewById(R.id.tv_ShippingPlanSeq);
            listItemView.tvPoNo= (TextView) convertView.findViewById(R.id.tv_PoNo);
            listItemView.tvContainerSeq= (TextView) convertView.findViewById(R.id.tv_ContainerSeq);
            listItemView.tvCustomer= (TextView) convertView.findViewById(R.id.tv_Customer);
            listItemView.tvProduct= (TextView) convertView.findViewById(R.id.tv_Product);
            listItemView.tvPlanQty= (TextView) convertView.findViewById(R.id.tv_PlanQty);
            listItemView.tvContainerSpec= (TextView) convertView.findViewById(R.id.tv_ContainerSpec);
            listItemView.tvPlanStartTime= (TextView) convertView.findViewById(R.id.tv_PlanStartTime);
            listItemView.tvPlanEndTime= (TextView) convertView.findViewById(R.id.tv_PlanEndTime);
            listItemView.tvState= (TextView) convertView.findViewById(R.id.tv_ShippingState);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        ShippingPlanData data = listMap.get(position);
        listItemView.tvShipplanNo.setText(data.getSHIPPINGPLANNO());
        listItemView.tvPoNo.setText(data.getPOID());
        listItemView.tvShippingplanSeq.setText(data.getSHIPPINGPLANSEQ());
        listItemView.tvContainerSeq.setText(data.getCONTAINERSEQ());
        listItemView.tvCustomer.setText(data.getCUSTOMERID());
        listItemView.tvProduct.setText(data.getPRODUCTDEFNAME());
        listItemView.tvPlanQty.setText(data.getPLANQTY());
        listItemView.tvContainerSpec.setText(data.getCONTAINERSPEC());
        listItemView.tvPlanStartTime.setText(data.getPLANSTARTTIME());
        listItemView.tvPlanEndTime.setText(data.getPLANENDTIME());
        listItemView.tvState.setText(data.getSTATE());
        return convertView;
    }
}

