package com.example.transferdata.media;

import android.os.Bundle;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterImage;
import com.example.transferdata.adapter.itemImage;

import java.util.ArrayList;
import java.util.List;
public class DetailListImage extends AppCompatActivity {
    public com.example.transferdata.adapter.adapterImage adapter;
    public adapterImage adapterImage;
    GridView gridView;
    GridView gridViewFolder;
    List<itemImage> list;
    public List<itemImage> listImage;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image);
        getData();
        this.listImage = new ArrayList();
        new getDataImage();
        this.listImage = getDataImage.listImage;
        this.adapter = new adapterImage(this, R.layout.grid_item_image, this.listImage, Boolean.TRUE);
        GridView gridView2 = findViewById(R.id.grid_image);
        this.gridView = gridView2;
        gridView2.setAdapter(this.adapter);
        this.list = new ArrayList();
        if (!this.listImage.isEmpty()) {
            this.list.add(this.listImage.get(0));
        }
        this.adapterImage = new adapterImage(this, R.layout.grid_item_image, this.list, Boolean.FALSE);
        GridView gridView3 = findViewById(R.id.grid_image_folder);
        this.gridViewFolder = gridView3;
        gridView3.setAdapter(this.adapterImage);
        clickItemGridView(this.gridView, this.adapterImage);
        saveChooseImage();
    }

    private void getData() {
        new getDataImage();
        getDataImage.listImage = getIntent().getParcelableArrayListExtra("listImage");
    }

    /* access modifiers changed from: 0000 */
    public void clickItemGridView(GridView gridView2, final adapterImage adapter2) {
        gridView2.setOnItemClickListener((adapterView, view, position, id) -> {
            DetailListImage.this.list.clear();
            DetailListImage.this.list.add(DetailListImage.this.listImage.get(position));
            adapter2.notifyDataSetChanged();
        });
    }

    /* access modifiers changed from: 0000 */
    public void saveChooseImage() {
        findViewById(R.id.save_choose_image).setOnClickListener(v -> {
            DetailListImage.this.setResult(-1);
            DetailListImage.this.finish();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
