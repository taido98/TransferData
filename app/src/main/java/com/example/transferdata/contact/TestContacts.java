package com.example.transferdata.contact;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
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

import com.example.transferdata.R;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ezvcard.VCard;
import ezvcard.io.text.VCardReader;

public class  TestContacts extends AppCompatActivity {

    private Button mBtnGetContact;
    private Button mBtnRestoreContact;
    private TextView mTxtSize;
    private TextView mTxtPath;
    private String path = "";
    private static final String TAG = TestContacts.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contacts);
        initView();
        initAction();
    }

    private void initView() {
        mBtnGetContact = (Button) findViewById(R.id.get_vcf);
        mBtnRestoreContact = (Button) findViewById(R.id.restore_vcf);
        mTxtSize = (TextView) findViewById(R.id.size_vcf);
        mTxtPath = (TextView) findViewById(R.id.path_vcf);
    }

    private void initAction() {
        mBtnGetContact.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                path = getVCF(TestContacts.this);
                File vcf = new File(path);
                double size = vcf.length();
                mTxtPath.setText(path);
                mTxtSize.setText(size + "");
            }
        });
        mBtnRestoreContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restoreVCF(path, TestContacts.this);
            }
        });
    }

//    public static boolean hasPermissions(Context context, String... permissions) {
//        if (context != null && permissions != null) {
//            for (String permission : permissions) {
//                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
//                    return false;
//                }
//            }
//        }
//        return true;
//    }

    ///Return path of vcf file
    public static String getVCF(Activity activity) {
        String results = "";
        Context mContext = activity.getApplicationContext();
        String[] PERMISSIONS = {
                android.Manifest.permission.READ_CONTACTS,
                android.Manifest.permission.WRITE_CONTACTS,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.GET_ACCOUNTS,
        };

        if (true) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS, 100);
        } else {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/Contacts.vcf";
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

    public static void restoreVCF(String filePath, Activity activity) {
        Log.d("PATH", filePath);
        if (filePath.equals("")) {
            Log.e("Restore Error", "vCard path is empty");
        } else {
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                Log.e("Restore Error", "No external storage mounted.");
            }

            File vcardFile;
            vcardFile = new File(filePath);
            if (!vcardFile.exists()) {
                Log.e("Restore Error", "vCard file does not exist: " + filePath);
            } else {
                VCardReader reader = null;
                try {
                    reader = new VCardReader(vcardFile);
                    reader.registerScribe(new AndroidCustomFieldScribe());

                    ContactOperations operations = new ContactOperations(activity.getApplicationContext(), "Phone", "com.motorola.android.buacontactadapter");
                    VCard vcard = null;
                    while ((vcard = reader.readNext()) != null) {
                        operations.insertContact(vcard);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    closeQuietly(reader);
                }
            }
        }

    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable == null) {
            return;
        }

        try {
            closeable.close();
        } catch (IOException e) {
            //ignore
        }
    }
}
