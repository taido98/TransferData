package com.example.transferdata.media;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterAudio;
import com.example.transferdata.adapter.itemAudio;

import java.util.ArrayList;
import java.util.List;

public class DetailListAudio extends AppCompatActivity {
    public adapterAudio adapterAudio, adapterFolder;
    GridView gridView;
    GridView gridViewFolder;
    List<itemAudio> list;
    List<itemAudio> mListAudio;

    TextView txt_type;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        new getAudio();
        getData();
        TextView textView = findViewById(R.id.type);
        this.txt_type = textView;
        textView.setText("Audios");
        adapterFolder = new adapterAudio(this, R.layout.grid_item_image, mListAudio, Boolean.TRUE);
        gridView = findViewById(R.id.grid_image);
        gridView.setAdapter(this.adapterFolder);
        list = new ArrayList();
        if (!mListAudio.isEmpty()) {
            this.list.add(mListAudio.get(0));
        }
        this.adapterAudio = new adapterAudio(this, R.layout.grid_item_image, list, Boolean.FALSE);
        gridViewFolder = findViewById(R.id.grid_image_folder);
        gridViewFolder.setAdapter(adapterAudio);
        clickItemGridView(gridView, adapterAudio);
        saveChooseVideo();
    }

    private void getData() {
        if (getIntent() != null) {
            mListAudio = new ArrayList<>();
            getAudio.listAudio = getIntent().getParcelableArrayListExtra("listAudio");
            mListAudio = getAudio.listAudio;
        }
    }

    public void clickItemGridView(GridView gridView2, final adapterAudio adapter2) {
        gridView2.setOnItemClickListener((adapterView, view, position, id) -> {
            DetailListAudio.this.list.clear();
            DetailListAudio.this.list.add(mListAudio.get(position));
            adapter2.notifyDataSetChanged();
        });
    }

    public void saveChooseVideo() {
        findViewById(R.id.save_choose_image).setOnClickListener(v -> {
            DetailListAudio.this.setResult(-1);
            DetailListAudio.this.finish();
        });
    }

}
