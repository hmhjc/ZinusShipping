package cn.zinus.warehouse.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.warehouse.JaveBean.ConsumeOutboundData;
import cn.zinus.warehouse.R;

/**
 * Developer:Spring
 * DataTime :2017/9/20 13:36
 * Main Change: ConsumeOutboundData ListView Adapter
 */

public class ConsumeOutboundListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ConsumeOutboundData> listMap;

    public final class ListItemView {

        public TextView tvconsuabledefnamme;
        public TextView tvunit;
        public TextView tvoutqty;
        public TextView tvrequestqty;
    }

    public ConsumeOutboundListViewAdapter(Context context, List<ConsumeOutboundData> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_consumeoutbound, null);
            listItemView.tvconsuabledefnamme = (TextView) convertView.findViewById(R.id.tv_consuabledefnamme);
            listItemView.tvunit = (TextView) convertView.findViewById(R.id.tv_unit);
            listItemView.tvoutqty = (TextView) convertView.findViewById(R.id.tv_outqty);
            listItemView.tvrequestqty = (TextView) convertView.findViewById(R.id.tv_requestqty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ConsumeOutboundData data = listMap.get(position);
        listItemView.tvconsuabledefnamme.setText(data.getCONSUMABLEDEFNAME());
        listItemView.tvunit.setText(data.getUNIT());
        listItemView.tvoutqty.setText(data.getOUTQTY());
        listItemView.tvoutqty.setTextColor(ContextCompat.getColor(mContext, data.getBackgroundColor()));
        listItemView.tvrequestqty.setText(data.getREQUESTQTY());
        return convertView;
    }

}