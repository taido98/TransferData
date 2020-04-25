package com.example.transferdata.media;

import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterVideo;
import com.example.transferdata.adapter.itemVideo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetailListVideo extends AppCompatActivity {
    public adapterVideo adapterVideo;
    GridView gridView;
    GridView gridViewFolder;
    List<itemVideo> list;
    List<itemVideo> mListAudio,mListVideo;

    TextView txt_type;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        setContentView(R.layout.image);
        TextView textView = findViewById(R.id.type);
        this.txt_type = textView;
        textView.setText("Videos");
        adapterVideo = new adapterVideo(this, R.layout.grid_item_image, mListVideo, Boolean.TRUE);
        gridView = findViewById(R.id.grid_image);
        gridView.setAdapter(this.adapterVideo);
        this.list = new ArrayList();
        if (!mListVideo.isEmpty()) {
            this.list.add(mListVideo.get(0));
        }
        this.adapterVideo = new adapterVideo(this, R.layout.grid_item_image, list, Boolean.FALSE);
        gridViewFolder = findViewById(R.id.grid_image_folder);
        gridViewFolder.setAdapter(adapterVideo);
        clickItemGridView(gridView, adapterVideo);
        saveChooseVideo();
    }

    private void getData() {
        if (getIntent() != null) {
            mListVideo = new ArrayList<>();
            mListAudio = new ArrayList<>();
            getVideo.listVideo = getIntent().getParcelableArrayListExtra("listVideo");
            getAudio.listAudio = getIntent().getParcelableArrayListExtra("listAudio");
            mListVideo = getVideo.listVideo;
            mListAudio = getAudio.listAudio;
        }
    }

    /* access modifiers changed from: 0000 */
    public void clickItemGridView(GridView gridView2, final adapterVideo adapter2) {
        gridView2.setOnItemClickListener((adapterView, view, position, id) -> {
            DetailListVideo.this.list.clear();
            DetailListVideo.this.list.add(mListVideo.get(position));
            adapter2.notifyDataSetChanged();
        });
    }

    /* access modifiers changed from: 0000 */
    public void saveChooseVideo() {
        findViewById(R.id.save_choose_image).setOnClickListener(v -> {
            DetailListVideo.this.setResult(-1);
            DetailListVideo.this.finish();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
