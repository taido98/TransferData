package com.example.transferdata.media;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterDetail;
import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.Interface.ClickItemListener;
import com.example.transferdata.service.getApplication;
import com.example.transferdata.tranferdata.ClientActivity;

import java.util.ArrayList;

public class DetailListApp extends Activity implements OnClickListener, ClickItemListener {
    public static adapterDetail adapterdetail;
    CheckBox checkAll;
    ClickItemListener detail;
    private ListView lvListItem;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        getData();
        ((TextView) findViewById(R.id.detail_title)).setText("Choose app");
        mapping();
        addItem();
    }

    private void getData() {
       if(getIntent()!=null){
           ArrayList<DataItem> list = getIntent().getParcelableArrayListExtra("listApp");
           for (int i = 0; i < list.size(); i++) {
               getApplication.listItem.get(i).setChecked(list.get(i).isChecked());
           }
       }
    }

    public void mapping() {
        this.lvListItem = findViewById(R.id.list_detail);
        TextView btnSave = findViewById(R.id.detail_save);
        TextView btnCancel = findViewById(R.id.detail_cancel);
        this.checkAll = findViewById(R.id.detail_checl_all);
        btnSave.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
    }

    /* access modifiers changed from: 0000 */
    public Boolean setCheckAll() {
        for (int i = 0; i < getApplication.listItem.size(); i++) {
            if (!getApplication.listItem.get(i).isChecked()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void addItem() {
        this.checkAll.setChecked(setCheckAll());
        this.detail = check -> DetailListApp.this.checkAll.setChecked(check);
        adapterDetail adapterdetail2 = new adapterDetail(this, R.layout.list_item_detail, getApplication.listItem, this.detail);
        adapterdetail = adapterdetail2;
        this.lvListItem.setAdapter(adapterdetail2);
        checkAll(this.checkAll, adapterdetail);
    }

    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.detail_cancel) {
            onBackPressed();
        } else if (id == R.id.detail_save) {
            setResult(-1);
            finish();
        }
    }

    public void onBackPressed() {
        super.onBackPressed();
    }

    public static String getInfo() {
        DataItem dataItem = new DataItem();
        int dem = 0;
        long size = 0;
        for (DataItem i : getApplication.listItem) {
            if (i.isChecked()) {
                dem++;
                size += i.getSize();
            }
        }

        ClientActivity.SIZE_ALL_ITEM[5] =size;
        return dataItem.sizeToString(size);
    }

    public void statusCheck(boolean check) {
    }

    public void checkAll(final CheckBox checkBox, final adapterDetail adapter) {
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                for (int i = 0; i < getApplication.listItem.size(); i++) {
                    getApplication.listItem.get(i).setChecked(true);
                }
            } else {
                for (int i2 = 0; i2 < getApplication.listItem.size(); i2++) {
                    getApplication.listItem.get(i2).setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }
}
