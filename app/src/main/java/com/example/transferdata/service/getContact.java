//package com.example.transferdata.service;
//
//import android.accounts.Account;
//import android.accounts.AccountManager;
//import android.app.Activity;
//import android.content.ContentProviderOperation;
//import android.content.ContentResolver;
//import android.content.Context;
//import android.content.OperationApplicationException;
//import android.database.Cursor;
//import android.net.Uri;
//import android.os.RemoteException;
//import android.provider.ContactsContract.CommonDataKinds.Email;
//import android.provider.ContactsContract.CommonDataKinds.Phone;
//import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
//import android.provider.ContactsContract.Contacts;
//import android.provider.ContactsContract.Data;
//import android.provider.ContactsContract.RawContacts;
//
//import com.example.transferdata.R;
//import com.example.transferdata.adapter.DataItem;
//import com.example.transferdata.security.AES;
//import com.example.transferdata.tranferdata.ClientActivity;
//import com.opencsv.CSVReader;
//import com.opencsv.CSVWriter;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.FileWriter;
//import java.io.IOException;
//import java.util.ArrayList;
//
//import org.apache.commons.lang3.StringUtils;
//
//public class getContact {
//    public static Activity context;
//    private static int countContact = 0;
//    public static ArrayList<DataItem> listItem;
//    private Account[] liAccounts;
//
//    public getContact(Activity contex) {
//        context = contex;
//        getAccount();
//        listItem = new ArrayList<>();
//        for (Account account : this.liAccounts) {
//            if (account.type.equalsIgnoreCase("com.google")) {
//                ArrayList<DataItem> arrayList = listItem;
//                DataItem item = new DataItem(true, R.drawable.ic_google, account.name, account.name, false);
//                arrayList.add(item);
//            }
//        }
//        ArrayList<DataItem> arrayList2 = listItem;
//        DataItem item2 = new DataItem(true, R.drawable.ic_sim_card, "Sim card", "sim", false);
//        arrayList2.add(item2);
//        ArrayList<DataItem> arrayList3 = listItem;
//        DataItem item3 = new DataItem(true, R.drawable.ic_phone_android, "Device", "device", false);
//        arrayList3.add(item3);
//    }
//
//    private void getAccount() {
//        this.liAccounts = ((AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE)).getAccounts();
//    }
//
//    public static String backupContacts() {
//        String result;
//        String str = "account_type";
//        countContact = 0;
//        ContentResolver contentResolver = context.getContentResolver();
//        ArrayList arrayList = new ArrayList();
//        for (DataItem it2 : listItem) {
//            if (it2.isChecked()) {
//                arrayList.add(it2.getInfo());
//            }
//        }
//        Cursor cursor = contentResolver.query(Contacts.CONTENT_URI, null, null, null, null);
//        if (cursor != null && cursor.getCount() > 0) {
//            String vfile = "contacts.csv";
//            try {
//                File file = new File(context.getExternalFilesDir(null), "contacts");
//                if (!file.exists()) {
//                    file.mkdir();
//                }
//                File fileContacts = new File(file, vfile);
//                if (!fileContacts.exists()) {
//                    fileContacts.createNewFile();
//                }
//                if (fileContacts.exists()) {
//                    while (cursor.moveToNext()) {
//                        if (arrayList.size() <= 0) {
//                            getInfoContact(cursor, contentResolver, new CSVWriter(new FileWriter(fileContacts), ',', (char) 0, '\"', "\n"));
//                        } else if (cursor.getColumnIndex(str) != -1) {
//                            String typeContact = cursor.getString(cursor.getColumnIndex(str));
//                            if (arrayList.contains("device") && typeContact == null) {
//                                getInfoContact(cursor, contentResolver, new CSVWriter(new FileWriter(fileContacts), ',', (char) 0, '\"', "\n"));
//                            }
//                            if (typeContact != null) {
//                                for (Object o : arrayList) {
//                                    String s = (String) o;
//                                    if (s.contains("@")) {
//                                        String accountName = cursor.getString(cursor.getColumnIndex("account_name"));
//                                        if (typeContact.contains("google") && accountName.equals(s)) {
//                                            getInfoContact(cursor, contentResolver, new CSVWriter(new FileWriter(fileContacts), ',', (char) 0, '\"', "\n"));
//                                        }
//                                    } else if (typeContact.contains(s)) {
//                                        getInfoContact(cursor, contentResolver, new CSVWriter(new FileWriter(fileContacts), ',', (char) 0, '\"', "\n"));
//                                    }
//                                }
//                            }
//                        }
//                    }
//                    new CSVWriter(new FileWriter(fileContacts), ',', (char) 0, '\"', "\n").close();
//                }
//                DataItem dataItem = new DataItem();
//                ClientActivity.SIZE_ALL_ITEM[0] = fileContacts.length();
//                result = dataItem.sizeToString(fileContacts.length());
//                AES encryptaes = new AES();
//                encryptaes.encrypt(fileContacts);
//                return result;
//            } catch (IOException e) {
//                e.printStackTrace();
//            } catch (Exception e2) {
//                e2.printStackTrace();
//            }
//        }
//        if (cursor != null) {
//            cursor.close();
//        }
//        return "Selected : 0 item - 0 MB";
//    }
//
//    private static void getInfoContact(Cursor cursor, ContentResolver contentResolver, CSVWriter csvWriter) {
//        String typeRelation;
//        String str;
//        String where;
//        String str2;
//        String str3;
//        countContact++;
//        String[] data = new String[25];
//        for (int i = 0; i < 25; i++) {
//            data[i] = "";
//        }
//        data[0] = cursor.getString(cursor.getColumnIndex("_id"));
//        String id = data[0];
//        int countPhone = cursor.getInt(cursor.getColumnIndex("has_phone_number"));
//        String str4 = "data2";
//        String str5 = "data1";
//        String str6 = ";";
//        if (countPhone > 0) {
//            Cursor csPhone = contentResolver.query(Phone.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
//            while (csPhone.moveToNext()) {
//                String p = csPhone.getString(csPhone.getColumnIndex(str5));
//                String t = csPhone.getString(csPhone.getColumnIndex(str4));
//                if (p != null) {
//                    String sb = data[2] +
//                            p +
//                            str6;
//                    data[2] = sb;
//                }
//                if (t != null) {
//                    String sb2 = data[3] +
//                            t +
//                            str6;
//                    data[3] = sb2;
//                }
//            }
//            csPhone.close();
//        }
//        Cursor csEmail = contentResolver.query(Email.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
//        while (csEmail.moveToNext()) {
//            String e = csEmail.getString(csEmail.getColumnIndex(str5));
//            String t2 = csEmail.getString(csEmail.getColumnIndex(str4));
//            if (e != null) {
//                String sb3 = data[4] +
//                        e +
//                        str6;
//                data[4] = sb3;
//            }
//            if (t2 != null) {
//                String sb4 = data[5] +
//                        t2 +
//                        str6;
//                data[5] = sb4;
//            }
//        }
//        csEmail.close();
//        Uri uri = Data.CONTENT_URI;
//        String str7 = "mimetype";
//        String str8 = "contact_id";
//        String str9 = "%s = ? AND %s = ?";
//        String[] whereParams = {"vnd.android.cursor.item/group_membership", id};
//        Cursor csGroup = contentResolver.query(uri, null, String.format(str9, str7, str8), whereParams, null);
//        if (csGroup.moveToFirst()) {
//            data[6] = csGroup.getString(csGroup.getColumnIndex(str5));
//        }
//        csGroup.close();
//        String where2 = String.format(str9, str7, str8);
//        String[] whereParams2 = {"vnd.android.cursor.item/organization", id};
//        Cursor csGroup2 = csGroup;
//        Cursor csCompany = contentResolver.query(uri, null, where2, whereParams2, null);
//        String str11 = "data5";
//        String str12 = "data4";
//        if (csCompany.moveToFirst()) {
//            data[7] = csCompany.getString(csCompany.getColumnIndex(str5));
//            data[8] = csCompany.getString(csCompany.getColumnIndex(str11));
//            data[9] = csCompany.getString(csCompany.getColumnIndex(str12));
//        }
//        csCompany.close();
//        Cursor csAddress = contentResolver.query(StructuredPostal.CONTENT_URI, null, "contact_id = ?", new String[]{id}, null);
//        while (csAddress.moveToNext()) {
//            String _street = csAddress.getString(csAddress.getColumnIndex(str12));
//            Cursor csEmail2 = csEmail;
//            String _city = csAddress.getString(csAddress.getColumnIndex("data7"));
//            Cursor csGroup3 = csGroup2;
//            String _country = csAddress.getString(csAddress.getColumnIndex("data10"));
//            Cursor csCompany2 = csCompany;
//            String _typeAddress = csAddress.getString(csAddress.getColumnIndex(str4));
//            String str20 = str5;
//            String _region = csAddress.getString(csAddress.getColumnIndex("data8"));
//            String str21 = str12;
//            String _postzip = csAddress.getString(csAddress.getColumnIndex("data9"));
//            if (_street != null) {
//                str3 = str11;
//                StringBuilder sb5 = new StringBuilder();
//                str2 = str4;
//                sb5.append(data[10]);
//                sb5.append(_street);
//                sb5.append(str6);
//                data[10] = sb5.toString();
//            } else {
//                str2 = str4;
//                str3 = str11;
//            }
//            if (_city != null) {
//                String sb6 = data[11] +
//                        _city +
//                        str6;
//                data[11] = sb6;
//            }
//            if (_country != null) {
//                String sb7 = data[12] +
//                        _country +
//                        str6;
//                data[12] = sb7;
//            }
//            if (_typeAddress != null) {
//                String sb8 = data[13] +
//                        _typeAddress +
//                        str6;
//                data[13] = sb8;
//            }
//            if (_region != null) {
//                String sb9 = data[14] +
//                        _region +
//                        str6;
//                data[14] = sb9;
//            }
//            if (_postzip != null) {
//                String sb10 = data[15] +
//                        _postzip +
//                        str6;
//                data[15] = sb10;
//            }
//            csCompany = csCompany2;
//            csEmail = csEmail2;
//            csGroup2 = csGroup3;
//            str5 = str20;
//            str12 = str21;
//            str11 = str3;
//            str4 = str2;
//        }
//        String prefix = str12;
//        String str23 = str4;
//        String relation = str5;
//        String middleName = str11;
//        Cursor csCompany3 = csCompany;
//        csAddress.close();
//        String givenName = "";
//        String middleName2 = "";
//        String _prefix = "";
//        String[] whereParams3 = {"vnd.android.cursor.item/name", id};
//        Cursor csCompany4 = csCompany3;
//        Cursor csNickname = contentResolver.query(uri, null, String.format(str9, str7, str8), whereParams3, null);
//        while (csNickname.moveToNext()) {
//            String _familyName = csNickname.getString(csNickname.getColumnIndex("data3"));
//            Cursor csCompany5 = csCompany4;
//            String str26 = str23;
//            Cursor csAddress2 = csAddress;
//            String _givenName = csNickname.getString(csNickname.getColumnIndex(str26));
//            String givenName2 = givenName;
//            String givenName3 = middleName;
//            String middleName3 = middleName2;
//            String _middleName = csNickname.getString(csNickname.getColumnIndex(givenName3));
//            String str28 = prefix;
//            String prefix2 = _prefix;
//            String _prefix2 = csNickname.getString(csNickname.getColumnIndex(str28));
//            String _suffix = csNickname.getString(csNickname.getColumnIndex("data6"));
//            if (_familyName != null) {
//                data[16] = _familyName;
//            }
//            if (_givenName != null) {
//                data[17] = _givenName;
//            }
//            if (_middleName != null) {
//                data[18] = _middleName;
//            }
//            if (_prefix2 != null) {
//                data[19] = _prefix2;
//            }
//            if (_suffix != null) {
//                data[20] = _suffix;
//            }
//            _prefix = prefix2;
//            middleName2 = middleName3;
//            csAddress = csAddress2;
//            givenName = givenName2;
//            str23 = str26;
//            middleName = givenName3;
//            prefix = str28;
//            csCompany4 = csCompany5;
//        }
//        String str33 = str23;
//        csNickname.close();
//        String relation2 = "";
//        String typeRelation2 = "";
//        String where3 = String.format(str9, str7, str8);
//        String str34 = where3;
//        Cursor csRelation = contentResolver.query(uri, null, str34, new String[]{"vnd.android.cursor.item/relation", id}, null);
//        while (csRelation.moveToNext()) {
//            String str35 = relation;
//            String _relation = csRelation.getString(csRelation.getColumnIndex(str35));
//            String relation3 = relation2;
//            String _type = csRelation.getString(csRelation.getColumnIndex(str33));
//            if (_relation != null) {
//                str = str33;
//                StringBuilder sb11 = new StringBuilder();
//                typeRelation = typeRelation2;
//                sb11.append(data[21]);
//                sb11.append(_relation);
//                sb11.append(str6);
//                data[21] = sb11.toString();
//            } else {
//                str = str33;
//                typeRelation = typeRelation2;
//            }
//            if (_type != null) {
//                StringBuilder sb12 = new StringBuilder();
//                where = where3;
//                sb12.append(data[22]);
//                sb12.append(_type);
//                sb12.append(str6);
//                data[22] = sb12.toString();
//            } else {
//                where = where3;
//            }
//            str33 = str;
//            where3 = where;
//            relation2 = relation3;
//            typeRelation2 = typeRelation;
//            relation = str35;
//        }
//        String str37 = relation;
//        csRelation.close();
//        String note = "";
//        Cursor csNote = contentResolver.query(uri, null, String.format(str9, str7, str8), new String[]{"vnd.android.cursor.item/note", id}, null);
//        if (csNote.moveToFirst()) {
//            String _note = csNote.getString(csNote.getColumnIndex(str37));
//            if (_note != null) {
//                data[23] = _note;
//            }
//        }
//        csNote.close();
//        Cursor csWeb = contentResolver.query(uri, null, String.format(str9, str7, str8), new String[]{"vnd.android.cursor.item/website", id}, null);
//        while (csWeb.moveToNext()) {
//            String _website = csWeb.getString(csWeb.getColumnIndex(str37));
//            if (_website != null) {
//                String sb13 = data[24] +
//                        _website +
//                        str6;
//                data[24] = sb13;
//            }
//        }
//        csWeb.close();
//        csvWriter.writeNext(data);
//    }
//
//    public void restoreContacts() {
//        String displayName;
//        String str = StringUtils.SPACE;
//        String str2 = "";
//        String str3 = ";";
//        Object obj = null;
//        String path = context.getExternalFilesDir(null) +
//                "/contacts/contacts.csv";
//        AES encryptaes = new AES();
//        File file = encryptaes.decrypt(new File(path));
//        if (file.exists()) {
//            try {
//                FileReader fileReader = new FileReader(file.getPath());
//                CSVReader reader = new CSVReader(fileReader);
//                int id = 0;
//                while (true) {
//                    String[] readNext = reader.readNext();
//                    if (readNext == null) {
//                        break;
//                    }
//                    ArrayList arrayList = new ArrayList();
//                    arrayList.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI).withValue("account_type", obj).withValue("account_name", obj).build());
//                    String displayName2 = readNext[17] +
//                            str +
//                            readNext[18] +
//                            str +
//                            readNext[16];
//                    if (!readNext[20].equals(str2)) {
//                        displayName2 = displayName2 +
//                                ", " +
//                                readNext[20];
//                    }
//                    if (!readNext[19].equals(str2)) {
//                        displayName = readNext[19] +
//                                str +
//                                displayName2;
//                    } else {
//                        displayName = displayName2;
//                    }
//                    String str4 = readNext[16];
//                    String str5 = readNext[17];
//                    String str6 = displayName;
//                    String str7 = str;
//                    String str8 = readNext[18];
//                    int id2 = id;
//                    FileReader fileReader2 = fileReader;
//                    arrayList.add(setNicknameContact(id, str6, str4, str5, str8, readNext[19], readNext[20]));
//                    String[] sEmail = readNext[4].split(str3);
//                    String[] tEmail = readNext[5].split(str3);
//                    if (!readNext[5].equals(str2)) {
//                        for (int i = 0; i < sEmail.length; i++) {
//                            arrayList.add(setEmailContact(id2, new String[]{tEmail[i], sEmail[i]}));
//                        }
//                    }
//                    String[] sStreet = readNext[10].split(str3);
//                    String[] sCity = readNext[11].split(str3);
//                    String[] sCountry = readNext[12].split(str3);
//                    String[] tAddress = readNext[13].split(str3);
//                    String[] sRegion = readNext[14].split(str3);
//                    String[] sPostzip = readNext[15].split(str3);
//                    if (!readNext[13].equals(str2)) {
//                        int i2 = 0;
//                        while (i2 < tAddress.length) {
//                            String[] tEmail2 = tEmail;
//                            String[] value = new String[6];
//                            value[0] = tAddress[i2];
//                            if (sStreet.length > 0) {
//                                value[1] = sStreet[i2];
//                            }
//                            if (sCountry.length > 0) {
//                                value[2] = sCountry[i2];
//                            }
//                            if (sCountry.length > 0) {
//                                value[3] = sCountry[i2];
//                            }
//                            if (sRegion.length > 0) {
//                                value[4] = sRegion[i2];
//                            }
//                            if (sPostzip.length > 0) {
//                                value[5] = sPostzip[i2];
//                            }
//                            arrayList.add(setAddressContact(id2, value));
//                            i2++;
//                            tEmail = tEmail2;
//                        }
//                    }
//                    if (!readNext[6].equals(str2)) {
//                        arrayList.add(setGroupContact(id2, readNext[6]));
//                    }
//                    String[] sPhone = readNext[2].split(str3);
//                    String[] tPhone = readNext[3].split(str3);
//                    if (!readNext[3].equals(str2)) {
//                        int i3 = 0;
//                        while (true) {
//                            String[] sStreet2 = sStreet;
//                            if (i3 >= sPhone.length) {
//                                break;
//                            }
//                            String[] sCountry2 = sCountry;
//                            arrayList.add(setPhoneContact(id2, new String[]{tPhone[i3], sPhone[i3]}));
//                            i3++;
//                            sStreet = sStreet2;
//                            sCountry = sCountry2;
//                        }
//                    }
//                    if (!readNext[7].equals(str2) || !readNext[8].equals(str2) || !readNext[9].equals(str2)) {
//                        arrayList.add(setCompanyContact(id2, readNext[7], readNext[8], readNext[9]));
//                    }
//                    String[] sRelation = readNext[21].split(str3);
//                    String[] tRelation = readNext[22].split(str3);
//                    if (!readNext[22].equals(str2)) {
//                        int i4 = 0;
//                        while (true) {
//                            String[] sPhone2 = sPhone;
//                            if (i4 >= tRelation.length) {
//                                break;
//                            }
//                            String[] tPhone2 = tPhone;
//                            arrayList.add(setRelationContact(id2, new String[]{tRelation[i4], sRelation[i4]}));
//                            i4++;
//                            sPhone = sPhone2;
//                            tPhone = tPhone2;
//                        }
//                    }
//                    if (!readNext[23].equals(str2)) {
//                        setNoteContact(id2, readNext[23]);
//                    }
//                    String[] sWebsite = readNext[24].split(str3);
//                    if (!readNext[24].equals(str2)) {
//                        for (String websiteContact : sWebsite) {
//                            arrayList.add(setWebsiteContact(id2, websiteContact));
//                        }
//                    }
//                    context.getContentResolver().applyBatch("com.android.contacts", arrayList);
//                    id = id2;
//                    fileReader = fileReader2;
//                    str = str7;
//                    obj = null;
//                }
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e2) {
//                e2.printStackTrace();
//            } catch (RemoteException e3) {
//                e3.printStackTrace();
//            } catch (OperationApplicationException e4) {
//                e4.printStackTrace();
//            }
//        }
//        file.delete();
//    }
//
//    private ContentProviderOperation setPhoneContact(int id, String[] value) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/phone_v2").withValue("data1", value[1]).withValue("data2", value[0]).build();
//    }
//
//    private ContentProviderOperation setGroupContact(int id, String value) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/group_membership").withValue("data1", value).build();
//    }
//
//    private ContentProviderOperation setAddressContact(int id, String[] value) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/postal-address_v2").withValue("data2", value[0]).withValue("data4", value[1]).withValue("data7", value[2]).withValue("data10", value[3]).withValue("data8", value[4]).withValue("data9", value[5]).build();
//    }
//
//    private ContentProviderOperation setEmailContact(int id, String[] value) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/email_v2").withValue("data2", value[0]).withValue("data1", value[1]).build();
//    }
//
//    private void setNoteContact(int id, String value) {
//        ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/note").withValue("data1", value).build();
//    }
//
//    private ContentProviderOperation setWebsiteContact(int id, String value) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/website").withValue("data1", value).build();
//    }
//
//    private ContentProviderOperation setRelationContact(int id, String[] value) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/relation").withValue("data1", value[1]).withValue("data2", value[0]).build();
//    }
//
//    private ContentProviderOperation setNicknameContact(int id, String displayName, String familyName, String givenName, String middleName, String prefix, String suffix) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/name").withValue("data1", displayName).withValue("data3", familyName).withValue("data2", givenName).withValue("data5", middleName).withValue("data4", prefix).withValue("data6", suffix).build();
//    }
//
//    private ContentProviderOperation setCompanyContact(int id, String company, String department, String title) {
//        return ContentProviderOperation.newInsert(Data.CONTENT_URI).withValueBackReference("raw_contact_id", id).withValue("mimetype", "vnd.android.cursor.item/organization").withValue("data1", company).withValue("data5", department).withValue("data4", title).build();
//    }
//}
