package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.InboundOrderData;
import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :2017/9/7 13:36
 * Main Change: Inboundorder ListView Adapter
 */

public class InboundorderListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<InboundOrderData> listMap;

    public final class ListItemView {
        public TextView tvInboundno;
        public TextView tvWareHouse;
        public TextView tvScheduleDate;
        public TextView tvTempInboundDate;
        public TextView tvInboundDate;
        public TextView tvInboundState;
        public TextView tvInspectionresult;
        public TextView tvUrgencytype;
    }

    public InboundorderListViewAdapter(Context context, List<InboundOrderData> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_inboundorder, null);
            listItemView.tvInboundno = (TextView) convertView.findViewById(R.id.tv_Inboundno);
            listItemView.tvWareHouse = (TextView) convertView.findViewById(R.id.tv_WareHouse);
            listItemView.tvScheduleDate = (TextView) convertView.findViewById(R.id.tv_ScheduleDate);
            listItemView.tvTempInboundDate = (TextView) convertView.findViewById(R.id.tv_TempInboundDate);
            listItemView.tvInboundDate = (TextView) convertView.findViewById(R.id.tv_InboundDate);
            listItemView.tvInboundState = (TextView) convertView.findViewById(R.id.tv_InboundState);
            listItemView.tvInspectionresult = (TextView) convertView.findViewById(R.id.tv_Inspectionresult);
            listItemView.tvUrgencytype = (TextView) convertView.findViewById(R.id.tv_Urgencytype);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }

        InboundOrderData data = listMap.get(position);
        listItemView.tvInboundno.setText(data.getINBOUNDNO());
        listItemView.tvWareHouse.setText(data.getWAREHOUSEID());
        listItemView.tvScheduleDate.setText(data.getSCHEDULEDATE());
        listItemView.tvTempInboundDate.setText(data.getTEMPINBOUNDDATE());
        listItemView.tvInboundDate.setText(data.getINBOUNDDATE());
        listItemView.tvInboundState.setText(data.getINBOUNDSTATE());
        listItemView.tvInspectionresult.setText(data.getINSPECTIONRESULT());
        listItemView.tvUrgencytype.setText(data.getURGENCYTYPE());

        return convertView;
    }
}
