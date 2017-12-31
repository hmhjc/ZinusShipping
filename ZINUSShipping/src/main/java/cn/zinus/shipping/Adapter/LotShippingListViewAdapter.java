package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.LotShippingListData;
import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :2017/11/7 13:36
 * Main Change: LotShipping ListView Adapter
 */

public class LotShippingListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<LotShippingListData> listMap;

    public final class ListItemView {
        public TextView tvLotID;
        public TextView tvProductID;
        public TextView tvProductName;
        public TextView tvTrackOutTime;
        public TextView tvinqty;
    }

    public LotShippingListViewAdapter(Context context, List<LotShippingListData> listMap) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ListItemView listItemView;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = mInflater.inflate(R.layout.lv_item_lotshipping, null);
            listItemView.tvLotID = (TextView) convertView.findViewById(R.id.tv_LotID);
            listItemView.tvProductID = (TextView) convertView.findViewById(R.id.tv_ProductID);
            listItemView.tvProductName = (TextView) convertView.findViewById(R.id.tv_ProductName);
            listItemView.tvTrackOutTime = (TextView) convertView.findViewById(R.id.tv_TrackOutTime);
            listItemView.tvinqty = (TextView) convertView.findViewById(R.id.tv_inqty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        LotShippingListData data = listMap.get(position);
        listItemView.tvLotID.setText(data.getLOTID());
        listItemView.tvProductID.setText(data.getPRODUCTDEFID());
        listItemView.tvProductName.setText(data.getPRODUCTDEFNAME());
        listItemView.tvTrackOutTime.setText(data.getTRACKOUTTIME());
        listItemView.tvinqty.setText(data.getINQTY());
        return convertView;
    }
}