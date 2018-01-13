package cn.zinus.warehouse.Adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.warehouse.JaveBean.StockLotCheckDeatilData;
import cn.zinus.warehouse.R;

/**
 * Developer:Spring
 * DataTime :2017/9/25 15:42
 * Main Change: StockLotCheckDetail ListView Adapter
 */

public class StockLotCheckDetailListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<StockLotCheckDeatilData> listMap;

    public final class ListItemView {
        public TextView tvconsumablelotid;
        public TextView tvunit;
        public TextView tvqty;
        public TextView tvcheckusername;
        public TextView tvcheckqty;
    }

    public StockLotCheckDetailListViewAdapter(Context context, List<StockLotCheckDeatilData> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_stocklotcheckdetail, null);
            listItemView.tvconsumablelotid = (TextView) convertView.findViewById(R.id.tv_consuablelotid);
            listItemView.tvunit = (TextView) convertView.findViewById(R.id.tv_unit);
            listItemView.tvqty = (TextView) convertView.findViewById(R.id.tv_qty);
            listItemView.tvcheckqty = (TextView) convertView.findViewById(R.id.et_checkqty);
            listItemView.tvcheckusername = (TextView) convertView.findViewById(R.id.tv_CheckUsername);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        StockLotCheckDeatilData data = listMap.get(position);
        listItemView.tvconsumablelotid.setText(data.getCONSUMABLELOTID());
        listItemView.tvunit.setText(data.getUNIT());
        listItemView.tvcheckqty.setText(data.getCHECKQTY());
        listItemView.tvqty.setText(data.getQTY());
        listItemView.tvcheckusername.setText(data.getUSERNAME());
        listItemView.tvcheckqty.setTextColor(ContextCompat.getColor(mContext, data.getBackgroundColor()));
        return convertView;
    }

}