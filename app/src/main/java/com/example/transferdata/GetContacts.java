package com.example.transferdata;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class GetContacts extends AppCompatActivity {

    private Button mBtnGetContact;
    private TextView mTxtSize;
    private TextView mTxtPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initView();
        initAction();
    }

    private void initView() {
        mBtnGetContact = (Button) findViewById(R.id.get_contacts);
        mTxtSize = (TextView) findViewById(R.id.size_vcf);
        mTxtPath = (TextView) findViewById(R.id.path_vcf);
    }

    private void initAction() {
        mBtnGetContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String path = getVCF(GetContacts.this);
                File vcf = new File(path);
                double size = vcf.length();
                mTxtPath.setText(path);
                mTxtSize.setText(size + "");
            }
        });
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    ///Return path of vcf file
    public static String getVCF(Activity activity) {
        String results = "";
        Context mContext = activity.getApplicationContext();
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (!hasPermissions(mContext, PERMISSIONS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 100);
        } else {
            final String vfile = "Contacts.vcf";
            String path = Environment.getExternalStorageDirectory().toString() + File.separator + vfile;
            results = path;
            File fdelete = new File(path);
            if (fdelete.exists()) {
                if (fdelete.delete()) {
                    System.out.println("file Deleted :" + path);
                } else {
                    System.out.println("file not Deleted :" + path);
                }
            }
            Cursor phones = mContext.getContentResolver().query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                    null, null, null);
            phones.moveToFirst();
            for (int i = 0; i < phones.getCount(); i++) {
                String lookupKey = phones.getString(phones
                        .getColumnIndex(ContactsContract.Contacts.LOOKUP_KEY));
                Uri uri = Uri.withAppendedPath(
                        ContactsContract.Contacts.CONTENT_VCARD_URI,
                        lookupKey);
                AssetFileDescriptor fd;
                try {
                    fd = mContext.getContentResolver().openAssetFileDescriptor(uri, "r");
                    FileInputStream fis = fd.createInputStream();
                    byte[] buf = new byte[(int) fd.getDeclaredLength()];
                    fis.read(buf);
                    String VCard = new String(buf);
                    FileOutputStream mFileOutputStream = new FileOutputStream(path, true);
                    mFileOutputStream.write(VCard.toString().getBytes());
                    phones.moveToNext();
                    Log.d("Contact", VCard);
                } catch (Exception e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        }
        return results;
    }

    public static void restoreVCF(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "text/x-vcard"); //storage path is path of your vcf file and vFile is name of that file.
    }
}
