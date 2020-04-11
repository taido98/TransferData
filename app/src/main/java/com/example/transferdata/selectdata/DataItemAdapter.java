package com.example.transferdata.selectdata;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.transferdata.R;

import java.util.ArrayList;

public class DataItemAdapter extends RecyclerView.Adapter<DataItemAdapter.ViewHolder> {
    private ArrayList<Data> listData;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public ImageView imageType;
        public TextView dataType;
        public TextView dataSize;
        public ConstraintLayout dataItem;

        public ViewHolder(View itemView) {
            super(itemView);
            imageType = (ImageView) itemView.findViewById(R.id.image_type);
            dataType = (TextView) itemView.findViewById(R.id.data_type);
            dataSize = (TextView) itemView.findViewById(R.id.data_size);
            dataItem = (ConstraintLayout) itemView.findViewById(R.id.data_item);
        }
    }

    public DataItemAdapter(ArrayList<Data> listData) {
        this.listData = listData;
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
        holder.dataType.setText(data.getTypeData());
        holder.imageType.setImageResource(data.getImageType());

        holder.dataItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Data position: " + listData.indexOf(data));
            }
        });
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listData.size();
    }
}
