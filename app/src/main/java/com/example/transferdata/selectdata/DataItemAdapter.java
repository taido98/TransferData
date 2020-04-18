package com.example.transferdata.selectdata;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transferdata.R;

import java.util.ArrayList;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {
    private ArrayList<Data> listData;
    private ClickItemDataListener mClickItemDataListener;
    private ClickCheckBoxListener mClickCheckBoxListener;
    private Context mContext;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private ImageView imageType;
        private TextView dataType;
        private TextView dataSize;
        private ConstraintLayout dataItem;
        private CheckBox checkBoxDataItem;
        private CheckBox checkBoxAll;

        public ViewHolder(View itemView) {
            super(itemView);
            imageType = (ImageView) itemView.findViewById(R.id.image_type);
            dataType = (TextView) itemView.findViewById(R.id.data_type);
            dataSize = (TextView) itemView.findViewById(R.id.data_size);
            dataItem = (ConstraintLayout) itemView.findViewById(R.id.data_item);
            checkBoxDataItem = (CheckBox) itemView.findViewById(R.id.select_all);
        }
    }

    public DataItemAdapter(Context context, ArrayList<Data> listData) {
        this.mContext = context;
        this.listData = listData;
    }

    public void notifyAdapter(ArrayList<Data> listData) {
        this.listData = listData;
        this.notifyDataSetChanged();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public DataItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View view = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.data_item, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final Data data = (Data) listData.get(position);

        holder.dataSize.setText(data.sizeToString(data.getSize()));
        holder.dataType.setText(data.getDataType(mContext));
        holder.imageType.setImageResource(data.getImageType());
        holder.checkBoxDataItem.setChecked(data.getChecker());

        holder.dataItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickItemDataListener != null) {
                    mClickItemDataListener.onItemData(position);
                }
            }
        });

        holder.checkBoxDataItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //is chkIos checked?
                data.setChecker(((CheckBox) v).isChecked());
                if (mClickCheckBoxListener != null) {
                    mClickCheckBoxListener.onItem(position);
                }
            }
        });

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listData.size();
    }

    public void setClickItemData(ClickItemDataListener clickItemDataListener) {
        this.mClickItemDataListener = clickItemDataListener;
    }

    public void setClickCheckBox(ClickCheckBoxListener clickCheckBoxListener) {
        this.mClickCheckBoxListener = clickCheckBoxListener;
    }
}
