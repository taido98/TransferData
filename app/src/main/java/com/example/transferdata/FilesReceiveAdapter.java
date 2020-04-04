package com.example.transferdata;

import android.content.Context;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

class FilesReceiveAdapter extends RecyclerView.Adapter<FilesReceiveViewHolder> {

    public File[] receivedFiles;
    public ArrayList<FilesReceiveViewHolder> filesReceiveViewHolders = new ArrayList<>();
    private Context context;

    public FilesReceiveAdapter(Context context) {
        this.receivedFiles = getFilesFromStorage(context);
        this.context = context;
    }

    @Override
    public FilesReceiveViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_list_item, parent, false);
        return new FilesReceiveViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(final FilesReceiveViewHolder holder, int position) {
        filesReceiveViewHolders.add(holder);

        holder.bind(new FileModel(receivedFiles[position]));
    }

    @Override
    public int getItemCount() {
        return receivedFiles.length;
    }

    public void notifyAdapter() {
        this.receivedFiles = this.getFilesFromStorage(this.context);
        this.notifyDataSetChanged();
    }

    private File[] getFilesFromStorage(Context context) {
        File dir = new File(PreferenceManager.getDefaultSharedPreferences(context)
                .getString(Variables.APP_TYPE, Environment.getExternalStorageDirectory() + "/"
                        + context.getApplicationContext().getPackageName()));

        File[] receivedFiles = dir.listFiles();

        if (receivedFiles == null) {
            return new File[]{};
        }
        return receivedFiles;
    }

}