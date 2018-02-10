package cn.zinus.shipping.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import cn.zinus.shipping.R;

/**
 * Developer:Spring
 * DataTime :
 * Main Change:
 */

public class TestListViewAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private List<String> listMap;

    public final class ListItemView {
        public TextView tvtag;
    }

    public TestListViewAdapter(Context context, List<String> listMap) {
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
            convertView = mInflater.inflate(R.layout.lv_item_test, null);
            listItemView.tvtag= (TextView) convertView.findViewById(R.id.tv_tag);

            convertView.setTag(listItemView);
        } else {
            listItemView = (ListItemView) convertView.getTag();
        }
        listItemView.tvtag.setText(listMap.get(position));
        return convertView;
    }
}

