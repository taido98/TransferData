package com.example.transferdata.video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.transferdata.R;

import java.util.ArrayList;

public class VideoItemAdapter extends RecyclerView.Adapter<VideoItemAdapter.ViewHolder> {
    private ArrayList listPath;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.video_path);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public VideoItemAdapter(ArrayList listPath) {
        this.listPath = listPath;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public VideoItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = (View) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final String path = (String) listPath.get(position);

        holder.textView.setText(path);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return listPath.size();
    }
}
