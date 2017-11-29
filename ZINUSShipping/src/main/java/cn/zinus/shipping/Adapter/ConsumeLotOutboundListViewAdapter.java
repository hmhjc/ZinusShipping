package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.ConsumeLotOutboundData;
import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :2017/9/20 13:36
 * Main Change: ConsumeOutboundData ListView Adapter
 */

public class ConsumeLotOutboundListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ConsumeLotOutboundData> listMap;

    public final class ListItemView {
        public TextView tvconsuabledefnamme;
        public TextView tvconsuablelotid;
        public TextView tvunit;
        public TextView tvOutqty;
        public TextView tvRequestqty;

    }

    public ConsumeLotOutboundListViewAdapter(Context context, List<ConsumeLotOutboundData> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_consumelotoutbound, null);
            listItemView.tvconsuabledefnamme = (TextView) convertView.findViewById(R.id.tv_consuabledefnamme);
            listItemView.tvconsuablelotid = (TextView) convertView.findViewById(R.id.tv_consuablelotid);
            listItemView.tvunit = (TextView) convertView.findViewById(R.id.tv_unit);
            listItemView.tvOutqty = (TextView) convertView.findViewById(R.id.tv_outqty);
            listItemView.tvRequestqty = (TextView) convertView.findViewById(R.id.tv_requestqty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ConsumeLotOutboundData data = listMap.get(position);
        listItemView.tvconsuabledefnamme.setText(data.getCONSUMABLEDEFNAME());
        listItemView.tvconsuablelotid.setText(data.getCONSUMABLELOTID());
        listItemView.tvunit.setText(data.getUNIT());
        listItemView.tvOutqty.setText(data.getOUTQTY());
        listItemView.tvOutqty.setTextColor(ContextCompat.getColor(mContext, data.getBackgroundColor()));
        listItemView.tvRequestqty.setText(data.getREQUESTQTY());
        return convertView;
    }

}