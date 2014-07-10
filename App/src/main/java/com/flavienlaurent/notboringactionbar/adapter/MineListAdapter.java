package com.flavienlaurent.notboringactionbar.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.flavienlaurent.notboringactionbar.R;
import java.util.List;

/**
 * @author rayboot
 * @from 14-7-10 16:36
 * @TODO
 */
public class MineListAdapter<T> extends BaseListAdapter<T>
{

    public MineListAdapter(Context context, List<T> listDatas)
    {
        super(context, listDatas);
    }

    @Override public View getView(int position, View convertView,
            ViewGroup parent)
    {
        ViewHolder holder;
        if (convertView != null)
        {
            holder = (ViewHolder) convertView.getTag();
        }
        else
        {
            convertView =
                    mLayoutInflater.inflate(R.layout.item_mine_content, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        String data = (String) getItem(position);
        holder.mTvName.setText(data);
        return convertView;
    }

    static class ViewHolder
    {
        @InjectView(R.id.tvName) TextView mTvName;

        public ViewHolder(View view)
        {
            ButterKnife.inject(this, view);
        }
    }
}
