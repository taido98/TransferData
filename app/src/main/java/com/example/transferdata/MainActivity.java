package com.example.transferdata;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {
    private ImageView mImgSendData, mImgReceiveData;
    protected OnBackPressedListener onBackPressedListener;
    private SharedPreferences sharedPreferences;
    private Button mBtnChooseDirectory;
    private String chosenDir = "";
    private boolean newFolderEnabled = true;
    private SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initAction();
    }

    private void initView() {
        mImgSendData = findViewById(R.id.img_send_data);
        mImgReceiveData = findViewById(R.id.img_receive_data);
        mBtnChooseDirectory = findViewById(R.id.btn_choose_directory);
    }

    private void initAction() {
        mImgSendData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ClientActivity.class);
                startActivity(intent);
            }
        });
        mImgReceiveData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, HostActivity.class);
                startActivity(intent);
            }
        });
        mBtnChooseDirectory.setOnClickListener(l -> {

//            // Create DirectoryChooserDialog and register a callback
//            DirectoryChooserDialog directoryChooserDialog =
//                    new DirectoryChooserDialog(MainActivity.this, chosenDir -> {
//                        this.chosenDir = chosenDir;
//                        Toast.makeText(MainActivity.this, "Chosen directory: " + chosenDir, Toast.LENGTH_LONG).show();
//
//                        sharedPreferencesEditor = sharedPreferences.edit();
//                        sharedPreferencesEditor.putString(Variables.APP_TYPE, chosenDir);
//                        sharedPreferencesEditor.commit();
//                    });
//            // Toggle new folder button enabling
//            directoryChooserDialog.setNewFolderEnabled(this.newFolderEnabled);
//            // Load directory chooser dialog for initial 'm_chosenDir' directory.
//            // The registered callback will be called upon final directory selection.
//            directoryChooserDialog.chooseDirectory(this.chosenDir);
//            this.newFolderEnabled = !this.newFolderEnabled;

            getVCF(this);

        });
    }

    @Override
    public void onStart() {
        super.onStart();
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        this.onBackPressedListener = (() -> {
            Toast.makeText(MainActivity.this, "Please press again to exit", Toast.LENGTH_SHORT).show();
            MainActivity.this.onBackPressedListener = null;
        });
    }

    @Override
    public void onBackPressed() {
        if (onBackPressedListener != null)
            onBackPressedListener.doBack();
        else
            super.onBackPressed();
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

    public static void getVCF(Activity activity) {
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

    }

    public static void restoreVCF(String filePath) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(filePath)), "text/x-vcard"); //storage path is path of your vcf file and vFile is name of that file.
    }
}
