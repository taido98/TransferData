package com.example.transferdata.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.example.transferdata.R;
import com.example.transferdata.Interface.ClickCheckBoxListener;
import com.example.transferdata.Interface.ClickItemDataListener;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {
    private List<DataItem> mListItem;
    private ClickItemDataListener mClickItemDataListener;
    private ClickCheckBoxListener mClickCheckBoxListener;
    private Context mContext;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTxtItem;
        private ImageView mImgItem;
        private CheckBox mCbItem;
        private GifImageView mGifWait;
        private ImageView mImgDetailItem;
        private TextView mTxtTotalItemSelected;

        public ViewHolder(View itemView) {
            super(itemView);
            mTxtItem = itemView.findViewById(R.id.txt_item);
            mImgItem = itemView.findViewById(R.id.img_item);
            mCbItem = itemView.findViewById(R.id.cb_item);
            mGifWait = itemView.findViewById(R.id.iconWaitGetData);
            mImgDetailItem = itemView.findViewById(R.id.img_show_more);
            mTxtTotalItemSelected = itemView.findViewById(R.id.total_item_select);
        }
    }

    public DataItemAdapter(Context context,List<DataItem> list) {
        this.mContext = context;
        this.mListItem = list;
    }

    public void notifyAdapter(List<DataItem> list) {
        this.mListItem = list;
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final DataItem item = mListItem.get(position);

        holder.mTxtItem.setText(mListItem.get(position).name);
        holder.mImgItem.setImageResource((mListItem.get(position)).img_resource);
        holder.mCbItem.setChecked((mListItem.get(position)).checked);
        holder.mTxtTotalItemSelected.setText(item.sizeToString((ClientActivity.SIZE_ALL_ITEM[position])));
        if ((mListItem.get(position)).statusLoad) {
            holder.mGifWait.setVisibility(View.VISIBLE);
        } else {
            holder.mGifWait.setVisibility(View.GONE);
        }
        holder.mImgDetailItem.setOnClickListener(v -> {
            if (mClickItemDataListener != null) {
                mClickItemDataListener.onItemData(position);
            }
        });

        holder.mCbItem.setOnClickListener(v -> {
            //is chkIos checked?
            item.setChecked(((CheckBox) v).isChecked());
            if (mClickCheckBoxListener != null) {
                mClickCheckBoxListener.onItem(position);
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if (!mListItem.isEmpty()) {
            return mListItem.size();
        }
        return 0;
    }

    public void setClickItemData(ClickItemDataListener clickItemDataListener) {
        this.mClickItemDataListener = clickItemDataListener;
    }

    public void setClickCheckBox(ClickCheckBoxListener clickCheckBoxListener) {
        this.mClickCheckBoxListener = clickCheckBoxListener;
    }
}
