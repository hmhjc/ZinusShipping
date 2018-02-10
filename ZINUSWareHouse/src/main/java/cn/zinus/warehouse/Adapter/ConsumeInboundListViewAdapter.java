package cn.zinus.warehouse.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.warehouse.JaveBean.ConsumeInboundData;
import cn.zinus.warehouse.R;

/**
 * Developer:Spring
 * DataTime :2017/9/7 13:36
 * Main Change: ConsumeInboundData ListView Adapter
 */

public class ConsumeInboundListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ConsumeInboundData> listMap;

    public final class ListItemView {
        public TextView tvspec_desc;
        public TextView tvconsuabledefnamme;
        public TextView tvunit;
        public TextView tvinqty;
        public TextView tvplantqty;
        public TextView tvORDERNO;
        public TextView tvORDERTYPE;
        public TextView tvLINENO;
        public TextView tvDiversionUnit;
        public TextView tvDiversionQTY;
    }

    public ConsumeInboundListViewAdapter(Context context, List<ConsumeInboundData> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_consumeinbound, null);
            listItemView.tvspec_desc = (TextView) convertView.findViewById(R.id.tv_spec_desc);
            listItemView.tvconsuabledefnamme = (TextView) convertView.findViewById(R.id.tv_consuabledefnamme);
            listItemView.tvunit = (TextView) convertView.findViewById(R.id.tv_unit);
            listItemView.tvinqty = (TextView) convertView.findViewById(R.id.tv_inqty);
            listItemView.tvplantqty = (TextView) convertView.findViewById(R.id.tv_plantqty);
            listItemView.tvORDERNO = (TextView) convertView.findViewById(R.id.tv_ORDERNO);
            listItemView.tvORDERTYPE = (TextView) convertView.findViewById(R.id.tv_ORDERTYPE);
            listItemView.tvLINENO = (TextView) convertView.findViewById(R.id.tv_LINENO);
            listItemView.tvDiversionUnit = (TextView) convertView.findViewById(R.id.tv_DiversionUnit);
            listItemView.tvDiversionQTY = (TextView) convertView.findViewById(R.id.tv_DiversionQTY);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ConsumeInboundData data = listMap.get(position);
        listItemView.tvspec_desc.setText(data.getSPEC_DESC());
        listItemView.tvconsuabledefnamme.setText(data.getCONSUMABLEDEFNAME());
        listItemView.tvunit.setText(data.getUNIT());
        listItemView.tvinqty.setText(data.getINQTY());
        listItemView.tvinqty.setTextColor(ContextCompat.getColor(mContext, data.getBackgroundColor()));
        listItemView.tvplantqty.setText(data.getPLANQTY());
        listItemView.tvORDERNO .setText(data.getORDERNO());
        listItemView.tvORDERTYPE.setText(data.getORDERTYPE());
        listItemView.tvLINENO .setText(data.getLINENO());
        listItemView.tvDiversionUnit .setText(data.getDIVERSIONUNIT());
        listItemView.tvDiversionQTY .setText(data.getDIVERSIONQTY());
        return convertView;
    }
}