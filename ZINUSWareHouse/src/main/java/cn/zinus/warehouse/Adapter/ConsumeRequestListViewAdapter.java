package cn.zinus.warehouse.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.warehouse.JaveBean.ConsumeRequestData;
import cn.zinus.warehouse.R;

/**
 * Developer:Spring
 * DataTime :2017/09/20 13:36
 * Main Change: ConsumeRequest ListView Adapter
 */

public class ConsumeRequestListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<ConsumeRequestData> listMap;

    public final class ListItemView {
        public TextView tvCONSUMEREQNO;
        public TextView tvCONSUMABLECOUNT;
        public TextView tvUSERNAME;
        public TextView tvLOCATIONNAME;
        public TextView tvREQUESTUSERNAME;
        public TextView tvFROMWAREHOUSENAME;
        public TextView tvTOWAREHOUSENAME;
        public TextView tvREQUESTDATE;
        public TextView tvFINISHPLANDATE;

//         public TextView tvCONSUMEREQNO
//         public TextView tvCONSUMABLECOUNT
//         public TextView tvUSERNAME
//         public TextView tvLOCATIONNAME
//         public TextView tvREQUESTUSERNAME
//         public TextView tvFROMWAREHOUSENAME
//         public TextView tvTOWAREHOUSENAME
//         public TextView tvREQUESTDATE
//         public TextView tvFINISHPLANDATE

    }

    public ConsumeRequestListViewAdapter(Context context, List<ConsumeRequestData> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_consumerequest, null);
            listItemView.tvCONSUMEREQNO = (TextView) convertView.findViewById(R.id.tv_CONSUMEREQNO);
            listItemView.tvCONSUMABLECOUNT = (TextView) convertView.findViewById(R.id.tv_CONSUMABLECOUNT);
            listItemView.tvUSERNAME = (TextView) convertView.findViewById(R.id.tv_USERNAME);
            listItemView.tvLOCATIONNAME = (TextView) convertView.findViewById(R.id.tv_LOCATIONNAME);
            listItemView.tvREQUESTUSERNAME = (TextView) convertView.findViewById(R.id.tv_REQUESTUSERNAME);
            listItemView.tvFROMWAREHOUSENAME = (TextView) convertView.findViewById(R.id.tv_FROMWAREHOUSENAME);
            listItemView.tvTOWAREHOUSENAME = (TextView) convertView.findViewById(R.id.tv_TOWAREHOUSENAME);
            listItemView.tvREQUESTDATE = (TextView) convertView.findViewById(R.id.tv_REQUESTDATE);
            listItemView.tvFINISHPLANDATE = (TextView) convertView.findViewById(R.id.tv_FINISHPLANDATE);

            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ConsumeRequestData data = listMap.get(position);
        listItemView.tvCONSUMEREQNO.setText(data.getCONSUMEREQNO());
        listItemView.tvCONSUMABLECOUNT.setText(data.getCONSUMABLECOUNT());
        listItemView.tvUSERNAME.setText(data.getUSERNAME());
        listItemView.tvLOCATIONNAME.setText(data.getLOCATIONNAME());
        listItemView.tvREQUESTUSERNAME.setText(data.getREQUESTUSERNAME());
        listItemView.tvFROMWAREHOUSENAME.setText(data.getFROMWAREHOUSENAME());
        listItemView.tvTOWAREHOUSENAME.setText(data.getTOWAREHOUSENAME());
        listItemView.tvREQUESTDATE.setText(data.getREQUESTDATE());
        listItemView.tvFINISHPLANDATE.setText(data.getFINISHPLANDATE());
        return convertView;
    }
}
