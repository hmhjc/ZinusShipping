package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.PODate;
import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :2017/12/30 10:03
 * Main Change:PO ListView Adapter
 */

public class POListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<PODate> listMap;

    public final class ListItemView {
        public TextView tvPOID;
        public TextView tvPOQTY;
        public TextView tvContainerNo;
        public TextView tvSealNo;
    }

    public POListViewAdapter(Context context, List<PODate> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_po, null);
            listItemView.tvPOID= (TextView) convertView.findViewById(R.id.tv_POID);
            listItemView.tvPOQTY= (TextView) convertView.findViewById(R.id.tv_poqty);
            listItemView.tvContainerNo= (TextView) convertView.findViewById(R.id.tv_ContainerNo);
            listItemView.tvSealNo= (TextView) convertView.findViewById(R.id.tv_SealNo);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        PODate data = listMap.get(position);
        listItemView.tvPOID.setText(data.getPOID());
        listItemView.tvPOQTY.setText(data.getPOQTY());
        listItemView.tvContainerNo.setText(data.getCONTAINERNO());
        listItemView.tvSealNo.setText(data.getSEALNO());
        return convertView;
    }
}