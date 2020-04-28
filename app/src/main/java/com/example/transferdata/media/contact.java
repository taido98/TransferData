//package com.example.transferdata.media;
//
//import android.accounts.Account;
//import android.app.Activity;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.CheckBox;
//import android.widget.ListView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.example.transferdata.R;
//import com.example.transferdata.adapter.DataItem;
//import com.example.transferdata.adapter.adapterDetail;
//import com.example.transferdata.Interface.ClickItemListener;
//import com.example.transferdata.service.getContact;
//
//
//public class contact extends Activity implements OnClickListener, ClickItemListener {
//    public static com.example.transferdata.adapter.adapterDetail adapterDetail;
//    CheckBox checkAll;
//    ClickItemListener detail;
//    private Account[] liAccounts;
//    private ListView lvListItem;
//
//    /* access modifiers changed from: protected */
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.detail);
//        ((TextView) findViewById(R.id.type)).setText("Choose contact");
//        mapping();
//        addItem();
//    }
//
//    public void mapping() {
//        this.lvListItem = findViewById(R.id.list_detail);
//        TextView btnSave = findViewById(R.id.btn_disconnect);
////        TextView btnCancel = findViewById(R.id.detail_cancel);
//        this.checkAll = findViewById(R.id.detail_checl_all);
//        btnSave.setOnClickListener(this);
////        btnCancel.setOnClickListener(this);
//    }
//
//    /* access modifiers changed from: 0000 */
//    public Boolean setCheckAll() {
//        for (int i = 0; i < getContact.listItem.size(); i++) {
//            if (!getContact.listItem.get(i).isChecked()) {
//                return Boolean.FALSE;
//            }
//        }
//        return Boolean.TRUE;
//    }
//
//    public void addItem() {
//        this.checkAll.setChecked(setCheckAll());
//        this.detail = check -> contact.this.checkAll.setChecked(check);
//        adapterDetail adapterdetail = new adapterDetail(this, R.layout.list_item_detail, getContact.listItem, this.detail);
//        adapterDetail = adapterdetail;
//        this.lvListItem.setAdapter(adapterdetail);
//        checkAll(this.checkAll, adapterDetail);
//    }
//
//    /* access modifiers changed from: 0000 */
//    public boolean isEmtyCheck() {
//        for (DataItem item : getContact.listItem) {
//            if (item.isChecked()) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public void onClick(View v) {
//        int id = v.getId();
////        if (id == R.id.detail_cancel) {
////            onBackPressed();
////        } else
//        if (id == R.id.btn_disconnect) {
//            if (isEmtyCheck()) {
//                setResult(-1);
//                finish();
//                return;
//            }
//            Toast.makeText(this, "You must select at least 1 item", Toast.LENGTH_LONG).show();
//        }
//    }
//
//    public void onBackPressed() {
//        getContact.listItem = getIntent().getParcelableArrayListExtra("listContact");
//        super.onBackPressed();
//    }
//
//    public void statusCheck(boolean check) {
//    }
//
//    /* access modifiers changed from: 0000 */
//    public void checkAll(final CheckBox checkBox, final adapterDetail adapter) {
//        checkBox.setOnClickListener(v -> {
//            if (checkBox.isChecked()) {
//                for (int i = 0; i < getContact.listItem.size(); i++) {
//                    getContact.listItem.get(i).setChecked(true);
//                }
//            } else {
//                for (int i2 = 0; i2 < getContact.listItem.size(); i2++) {
//                    getContact.listItem.get(i2).setChecked(false);
//                }
//            }
//            adapter.notifyDataSetChanged();
//        });
//    }
//}
