package cn.zinus.warehouse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.warehouse.JaveBean.StockCheckData;
import cn.zinus.warehouse.R;

/**
 * Developer:Spring
 * DataTime :2017/09/25 11:13
 * Main Change: StockCheck ListView Adapter
 */

public class StockCheckListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<StockCheckData> listMap;

    public final class ListItemView {
        public TextView tvCheckMonth;
        public TextView tvWarehouseID;
        public TextView tvStartDate;
        public TextView tvEndDate;
        public TextView tvCheckState;
    }

    public StockCheckListViewAdapter(Context context, List<StockCheckData> listMap) {
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
        ListItemView listItemView;
        if (convertView == null) {
            listItemView = new ListItemView();
            convertView = mInflater.inflate(R.layout.lv_item_stockcheck, null);
            listItemView.tvCheckMonth = (TextView) convertView.findViewById(R.id.tv_CheckMonth);
            listItemView.tvWarehouseID = (TextView) convertView.findViewById(R.id.tv_WarehouseID);
            listItemView.tvStartDate = (TextView) convertView.findViewById(R.id.tv_StartDate);
            listItemView.tvEndDate = (TextView) convertView.findViewById(R.id.tv_EndDate);
            listItemView.tvCheckState = (TextView) convertView.findViewById(R.id.tv_CheckState);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        StockCheckData data = listMap.get(position);
        listItemView.tvCheckMonth.setText(data.getCHECKMONTH());
        listItemView.tvWarehouseID.setText(data.getWAREHOUSEID());
        listItemView.tvStartDate.setText(data.getSTARTDATE());
        listItemView.tvEndDate.setText(data.getENDDATE());
        listItemView.tvCheckState.setText(data.getSTATENAME());

        return convertView;
    }
}
