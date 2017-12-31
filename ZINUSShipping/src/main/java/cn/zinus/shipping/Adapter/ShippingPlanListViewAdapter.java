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
        public TextView tvBookingNo;
        public TextView tvCustomer;
        public TextView tvPlanDate;
        public TextView tvShippingPlanDate;
        public TextView tvShippingEndPlanDate;
        public TextView tvShippingEndDate;
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
            listItemView.tvBookingNo= (TextView) convertView.findViewById(R.id.tv_BookingNo);
            listItemView.tvCustomer= (TextView) convertView.findViewById(R.id.tv_Customer);
            listItemView.tvPlanDate= (TextView) convertView.findViewById(R.id.tv_PlanDate);
            listItemView.tvShippingPlanDate= (TextView) convertView.findViewById(R.id.tv_ShippingPlanDate);
            listItemView.tvShippingEndPlanDate= (TextView) convertView.findViewById(R.id.tv_ShippingEndPlanDate);
            listItemView.tvShippingEndDate= (TextView) convertView.findViewById(R.id.tv_ShippingEndDate);
            listItemView.tvState= (TextView) convertView.findViewById(R.id.tv_ShippingState);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        ShippingPlanData data = listMap.get(position);
        listItemView.tvShipplanNo.setText(data.getSHIPPINGPLANNO());
        listItemView.tvBookingNo.setText(data.getBOOKINGNO());
        listItemView.tvCustomer.setText(data.getCUSTOMERID());
        listItemView.tvPlanDate.setText(data.getPLANDATE());
        listItemView.tvShippingPlanDate.setText(data.getSHIPPINGPLANDATE());
        listItemView.tvShippingEndPlanDate.setText(data.getSHIPPINGENDPLANDATE());
        listItemView.tvShippingEndDate.setText(data.getSHIPPINGENDDATE());
        listItemView.tvState.setText(data.getSTATE());
        return convertView;
    }
}

