package com.example.transferdata.media;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.example.transferdata.Interface.ClickItemListener;
import com.example.transferdata.R;
import com.example.transferdata.adapter.adapterDetail;
import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.service.getMessenger;
import com.jaeger.library.StatusBarUtil;

import java.text.ParseException;
import java.util.List;


public class DetailMessage extends Activity implements OnClickListener, ClickItemListener {
    public static com.example.transferdata.adapter.adapterDetail adapterDetail;
    public static List<DataItem> itemMain;
    CheckBox checkAll;
    ClickItemListener detail;
    private ListView lvListItem;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        ((TextView) findViewById(R.id.type)).setText("Choose messenger");
        mapping();
        addItem();
        StatusBarUtil.setTransparent(this);
        StatusBarUtil.setLightMode(this);
    }

    public void mapping() {
        this.lvListItem = findViewById(R.id.list_detail);
        TextView btnSave = findViewById(R.id.btn_done);
//        TextView btnCancel = findViewById(R.id.detail_cancel);
        this.checkAll = findViewById(R.id.detail_checl_all);
        btnSave.setOnClickListener(this);
//        btnCancel.setOnClickListener(this);
    }

    /* access modifiers changed from: 0000 */
    public Boolean setCheckAll() {
        for (int i = 0; i < getMessenger.listItem.size(); i++) {
            if (!getMessenger.listItem.get(i).isChecked()) {
                return Boolean.FALSE;
            }
        }
        return Boolean.TRUE;
    }

    public void addItem() {
        this.checkAll.setChecked(setCheckAll());
        this.detail = check -> DetailMessage.this.checkAll.setChecked(check);
        adapterDetail adapterdetail = new adapterDetail(this, R.layout.list_item_detail, getMessenger.listItem, this.detail);
        adapterDetail = adapterdetail;
        this.lvListItem.setAdapter(adapterdetail);
        checkAll(this.checkAll, adapterDetail);
    }

    /* access modifiers changed from: 0000 */
    public boolean isEmtyCheck() {
        for (DataItem item : getMessenger.listItem) {
            if (item.isChecked()) {
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClick(View v) {
        int id = v.getId();
//        if (id != R.id.detail_cancel) {
            if (id == R.id.btn_done) {
                try {
                    if (isEmtyCheck()) {
                        getMessenger.backupMessages();
                        setResult(-1);
                        finish();
                        return;
                    }
                    Toast.makeText(this, "You must select at least 1 item", Toast.LENGTH_LONG).show();
                    return;
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            } else {
                return;
            }
        }
//        onBackPressed();
//    }

    public void onBackPressed() {
        getMessenger.listItem = getIntent().getParcelableArrayListExtra("listMessenger");
        super.onBackPressed();
    }

    /* access modifiers changed from: 0000 */
    public void checkAll(final CheckBox checkBox, final adapterDetail adapter) {
        checkBox.setOnClickListener(v -> {
            if (checkBox.isChecked()) {
                for (int i = 0; i < getMessenger.listItem.size(); i++) {
                    getMessenger.listItem.get(i).setChecked(true);
                }
            } else {
                for (int i2 = 0; i2 < getMessenger.listItem.size(); i2++) {
                    getMessenger.listItem.get(i2).setChecked(false);
                }
            }
            adapter.notifyDataSetChanged();
        });
    }

    public void statusCheck(boolean check) {
    }
}
