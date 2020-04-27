package com.example.transferdata.media;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.transferdata.Interface.ClickItemListener;
import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterDetail;
import com.jaeger.library.StatusBarUtil;

public class DetailListFile extends AppCompatActivity {
//    TextView btn_cancel;
    TextView btn_save;
    public CheckBox checkAll;
    ClickItemListener detail;
    ListView listViewDetail;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        getData();
        this.checkAll = findViewById(R.id.detail_checl_all);
        ((TextView) findViewById(R.id.type)).setText("Choose file");
        this.detail = check -> DetailListFile.this.checkAll.setChecked(check);
        adapterDetail adapterDetail = new adapterDetail(this, R.layout.list_item_detail, getFile.listFile, this.detail);
        ListView listView = findViewById(R.id.list_detail);
        this.listViewDetail = listView;
        listView.setAdapter(adapterDetail);
        this.checkAll.setChecked(setCheckAll());
        checkAll(this.checkAll, adapterDetail);
        clickButton();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    private void getData() {
        if(getIntent()!=null){
            getFile.listFile = getIntent().getParcelableArrayListExtra("listFile");
        }
    }

    /* access modifiers changed from: 0000 */
    public Boolean setCheckAll() {
        for (int i = 0; i < getFile.listFile.size(); i++) {
            if (!getFile.listFile.get(i).isChecked()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    /* access modifiers changed from: 0000 */
    public void checkAll(final CheckBox checkBox, final adapterDetail adapter) {
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                for (int i = 0; i < getFile.listFile.size(); i++) {
                    getFile.listFile.get(i).setChecked(true);
                }
            } else {
                for (int i2 = 0; i2 < getFile.listFile.size(); i2++) {
                    getFile.listFile.get(i2).setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    /* access modifiers changed from: 0000 */
    public void clickButton() {
//        this.btn_cancel = findViewById(R.id.detail_cancel);
        this.btn_save = findViewById(R.id.choose_done);
//        this.btn_cancel.setOnClickListener(v -> DetailListFile.this.onBackPressed());
        this.btn_save.setOnClickListener(v -> {
            DetailListFile.this.setResult(-1);
            DetailListFile.this.finish();
        });
    }

    public void onBackPressed() {
        super.onBackPressed();
    }
}
