package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.StockCheckDeatilData;
import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :2017/9/25 14:06
 * Main Change: StockCheckDetail ListView Adapter
 */

public class StockCheckDetailListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<StockCheckDeatilData> listMap;

    public final class ListItemView {
        public TextView tvconsuabledefnamme;
        public TextView tvWarehouseID;
        public TextView tvunit;
        public TextView tvqty;
        public TextView etcheckqty;
    }

    public StockCheckDetailListViewAdapter(Context context, List<StockCheckDeatilData> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_stockcheckdetail, null);
            listItemView.tvconsuabledefnamme = (TextView) convertView.findViewById(R.id.tv_consuabledefnamme);
            listItemView.tvWarehouseID = (TextView) convertView.findViewById(R.id.tv_WarehouseID);
            listItemView.tvunit = (TextView) convertView.findViewById(R.id.tv_unit);
            listItemView.tvqty = (TextView) convertView.findViewById(R.id.tv_qty);
            listItemView.etcheckqty = (TextView) convertView.findViewById(R.id.et_checkqty);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        StockCheckDeatilData data = listMap.get(position);
        listItemView.tvconsuabledefnamme.setText(data.getCONSUMEABLDEFNAME());
        listItemView.tvWarehouseID.setText(data.getWAREHOUSEID());
        listItemView.tvunit.setText(data.getUNIT());
        listItemView.etcheckqty.setText(data.getCHECKQTY());
        listItemView.etcheckqty.setBackgroundColor(data.getBackgroundColor());
        listItemView.tvqty.setText(data.getQTY());
        return convertView;
    }

//    class EtTextChanged implements TextWatcher {
//        private int position;
//
//        public EtTextChanged(int position) {
//            this.position = position;
//        }
//
//        public void setPosition(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void beforeTextChanged(CharSequence s, int start, int count,
//                                      int after) {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void onTextChanged(CharSequence s, int start, int before,
//                                  int count) {
//            // TODO Auto-generated method stub
//        }
//
//        @Override
//        public void afterTextChanged(Editable s) {
//            listMap.get(position).setCHECKQTY(s.toString());
//            Log.e("StockCheckxiugai", position + ":" + s.toString());
//            int checkQTY;
//            if (listMap.get(position).getCHECKQTY().equals("")) {
//                checkQTY = 0;
//            } else {
//                checkQTY = Integer.parseInt(listMap.get(position).getCHECKQTY());
//            }
//            int Qty = Integer.parseInt(listMap.get(position).getQTY());
//            int priColor = listMap.get(position).getBackgroundColor();
//            if (checkQTY < Qty) {
//                listMap.get(position).setBackgroundColor(Color.RED);
//            } else {
//                listMap.get(position).setBackgroundColor(Color.WHITE);
//            }
//            if (priColor != listMap.get(position).getBackgroundColor()) {
//                //        EventBus.getDefault().post(new Event.stockCheckDetailRefresh());
//            }
//        }
//    }
}