package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.JaveBean.ConsumeRequestData;
import cn.zinus.shipping.R;

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
        public TextView tvConsumeRequestNo;
        public TextView tvAreaID;
        public TextView tvUserID;
        public TextView tvRequestDate;
        public TextView tvOutBoundState;
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
            listItemView.tvConsumeRequestNo = (TextView) convertView.findViewById(R.id.tv_ConsumeRequestNo);
            listItemView.tvAreaID = (TextView) convertView.findViewById(R.id.tv_AreaID);
            listItemView.tvUserID = (TextView) convertView.findViewById(R.id.tv_UserID);
            listItemView.tvRequestDate = (TextView) convertView.findViewById(R.id.tv_RequestDate);
            listItemView.tvOutBoundState = (TextView) convertView.findViewById(R.id.tv_OutBoundState);
            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        ConsumeRequestData data = listMap.get(position);
        listItemView.tvConsumeRequestNo.setText(data.getCONSUMEREQNO());
        listItemView.tvAreaID.setText(data.getAREAID());
        listItemView.tvUserID.setText(data.getUSERID());
        listItemView.tvRequestDate.setText(data.getREQUESTDATE());
        listItemView.tvOutBoundState.setText(data.getOUTBOUNDSTATE());

        return convertView;
    }
}
