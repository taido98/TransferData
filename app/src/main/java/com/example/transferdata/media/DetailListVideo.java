package com.example.transferdata.media;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterVideo;
import com.example.transferdata.adapter.itemVideo;
import com.jaeger.library.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class DetailListVideo extends AppCompatActivity {
    public com.example.transferdata.adapter.adapterVideo adapter;
    public adapterVideo adapterVideo;
    GridView gridView;
    GridView gridViewFolder;
    List<itemVideo> list;
    TextView txt_type;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        TextView textView = findViewById(R.id.type);
        this.txt_type = textView;
        textView.setText("Videos");
        this.adapter = new adapterVideo(this, R.layout.grid_item_image, getVideo.listVideo, Boolean.TRUE);
        GridView gridView2 = findViewById(R.id.grid_image);
        this.gridView = gridView2;
        gridView2.setAdapter(this.adapter);
        this.list = new ArrayList();
        if (!getVideo.listVideo.isEmpty()) {
            this.list.add(getVideo.listVideo.get(0));
        }
        this.adapterVideo = new adapterVideo(this, R.layout.grid_item_image, this.list, Boolean.FALSE);
        GridView gridView3 = findViewById(R.id.grid_image_folder);
        this.gridViewFolder = gridView3;
        gridView3.setAdapter(this.adapterVideo);
        clickItemGridView(this.gridView, this.adapterVideo);
        saveChooseVideo();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    private void getData() {
        if(getIntent()!=null){
            getVideo.listVideo = getIntent().getParcelableArrayListExtra("listVideo");
        }
    }

    /* access modifiers changed from: 0000 */
    public void clickItemGridView(GridView gridView2, final adapterVideo adapter2) {
        gridView2.setOnItemClickListener((adapterView, view, position, id) -> {
            DetailListVideo.this.list.clear();
            DetailListVideo.this.list.add(getVideo.listVideo.get(position));
            adapter2.notifyDataSetChanged();
        });
    }

    /* access modifiers changed from: 0000 */
    public void saveChooseVideo() {
        findViewById(R.id.btn_done).setOnClickListener(v -> {
            DetailListVideo.this.setResult(-1);
            DetailListVideo.this.finish();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
        getData();
    }
}
