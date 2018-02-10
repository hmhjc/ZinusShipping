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

        //规格信息
        public TextView tvspec_desc;
        //资材名
        public TextView tvconsuabledefnamme;
        //单位
        public TextView tvunit;
        //出库数量
        public TextView tvoutqty;
        //领料数量
        public TextView tvrequestqty;
        //发料仓库存
        public TextView tvfromqty;
        //领料仓库存
        public TextView tvtoqty;
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
            listItemView.tvspec_desc = (TextView) convertView.findViewById(R.id.tv_spec_desc);
            listItemView.tvconsuabledefnamme = (TextView) convertView.findViewById(R.id.tv_consuabledefnamme);
            listItemView.tvunit = (TextView) convertView.findViewById(R.id.tv_unit);
            listItemView.tvoutqty = (TextView) convertView.findViewById(R.id.tv_outqty);
            listItemView.tvrequestqty = (TextView) convertView.findViewById(R.id.tv_requestqty);
            listItemView.tvfromqty = (TextView) convertView.findViewById(R.id.tv_fromqty);
            listItemView.tvtoqty = (TextView) convertView.findViewById(R.id.tv_toqty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ConsumeOutboundData data = listMap.get(position);
        listItemView.tvspec_desc.setText(data.getSPEC_DESC());
        listItemView.tvconsuabledefnamme.setText(data.getCONSUMABLEDEFNAME());
        listItemView.tvunit.setText(data.getUNIT());
        listItemView.tvoutqty.setText(data.getOUTQTY());
        listItemView.tvoutqty.setTextColor(ContextCompat.getColor(mContext, data.getBackgroundColor()));
        listItemView.tvrequestqty.setText(data.getREQUESTQTY());
        listItemView.tvfromqty.setText(data.getFROMQTY());
        listItemView.tvtoqty.setText(data.getTOQTY());
        return convertView;
    }

}