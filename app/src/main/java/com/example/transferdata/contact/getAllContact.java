package com.example.transferdata.contact;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.security.AES;
import com.example.transferdata.tranferdata.ClientActivity;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import ezvcard.VCard;
import ezvcard.io.text.VCardReader;

public class getAllContact {
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


        String path = activity.getExternalFilesDir(null).toString() + "/Contacts.vcf";
        Log.d("Get contact>>>", path);
        File fileVcf = new File(path);
        if (fileVcf.exists()) {
            if (fileVcf.delete()) {
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
        DataItem dataItem = new DataItem();
        ClientActivity.SIZE_ALL_ITEM[0] = fileVcf.length();
        results = dataItem.sizeToString(fileVcf.length());
        AES encryptaes = new AES();
        encryptaes.encrypt(fileVcf);


        return results;
    }

    public static void restoreVCF(Activity activity) {
        String filePath = activity.getExternalFilesDir(null).toString() + "/Contacts.vcf";
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
