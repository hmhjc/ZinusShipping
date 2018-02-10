package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.ShippingPlanSeqListData;
import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :2017/12/30 10:03
 * Main Change:PO ListView Adapter
 */

public class PlanSeqListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ShippingPlanSeqListData> shippingPlanSeqListData;

    public final class ListItemView {
        public TextView tvPlanSeq;
        public TextView tvPOID;
        public TextView tvPOQTY;
        public TextView tvSKU;
        public TextView tvSHIPPINGEDQTY;
        public TextView tvNOSHIPPINGEDQTY;
    }

    public PlanSeqListViewAdapter(Context context, List<ShippingPlanSeqListData> shippingPlanSeqListData) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.shippingPlanSeqListData = shippingPlanSeqListData;
    }

    @Override
    public int getCount() {
        return shippingPlanSeqListData.size();
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
            convertView = mInflater.inflate(R.layout.lv_item_planseq, null);
            listItemView.tvPlanSeq= (TextView) convertView.findViewById(R.id.tv_PlanSeq);
            listItemView.tvPOID= (TextView) convertView.findViewById(R.id.tv_POID);
            listItemView.tvSKU= (TextView) convertView.findViewById(R.id.tv_SKU);
            listItemView.tvPOQTY= (TextView) convertView.findViewById(R.id.tv_poqty);
            listItemView.tvSHIPPINGEDQTY= (TextView) convertView.findViewById(R.id.tv_SHIPPINGEDQTY);
            listItemView.tvNOSHIPPINGEDQTY= (TextView) convertView.findViewById(R.id.tv_NOSHIPPINGEDQTY);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ShippingPlanSeqListData data = shippingPlanSeqListData.get(position);
        listItemView.tvPlanSeq.setText(data.getSHIPPINGPLANSEQ());
        listItemView.tvPOID.setText(data.getPOID());
        listItemView.tvSKU.setText(data.getPRODUCTDEFNAME());
        listItemView.tvPOQTY.setText(data.getQTY());
        listItemView.tvSHIPPINGEDQTY.setText(data.getSHIPPINGEDQTY());
        listItemView.tvNOSHIPPINGEDQTY.setText(data.getNOSHIPPINGEDQTY());
        return convertView;
    }
}