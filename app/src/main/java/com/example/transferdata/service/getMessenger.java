package com.example.transferdata.service;

import android.app.Activity;
import android.app.role.RoleManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.CancellationSignal;
import android.provider.Telephony.Sms;
import android.util.Log;
import androidx.annotation.RequiresApi;
import androidx.exifinterface.media.ExifInterface;

import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.security.AES;
import com.example.transferdata.tranferdata.ClientActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
public class getMessenger {
    private static final int CODE_RESTORE_SMS = 121;
    public static Activity context;
    private static int lengMessage;
    public static ArrayList<DataItem> listItem;


    public getMessenger(Activity contex) {
        context = contex;
        ArrayList<DataItem> arrayList = new ArrayList<>();
        listItem = arrayList;
        DataItem item = new DataItem(true, -1, "One day ago", "1", false);
        arrayList.add(item);
        ArrayList<DataItem> arrayList2 = listItem;
        DataItem item2 = new DataItem(true, -1, "One week ago", ExifInterface.GPS_MEASUREMENT_2D, false);
        arrayList2.add(item2);
        ArrayList<DataItem> arrayList3 = listItem;
        DataItem item3 = new DataItem(true, -1, "One month ago", ExifInterface.GPS_MEASUREMENT_3D, false);
        arrayList3.add(item3);
        ArrayList<DataItem> arrayList4 = listItem;
        DataItem item4 = new DataItem(true, -1, "One year ago", "4", false);
        arrayList4.add(item4);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static String backupMessages() throws ParseException {
        String str;
        String selection;
        FileNotFoundException e;
        IOException e2;
        ParserConfigurationException e3;
        TransformerConfigurationException e4;
        TransformerException e5;
        Exception e6;
        String result;
        ContentResolver contentResolver;
        String str2;
        ArrayList arrayList;
        String str3 = "messages";
        Object obj = null;
        lengMessage = 0;
        ArrayList arrayList2 = new ArrayList();
        for (DataItem it2 : listItem) {
            if (it2.isChecked()) {
                arrayList2.add(it2.getInfo());
            }
        }
        String address = "";
        int i = 1;
        if (arrayList2.size() <= 0 || arrayList2.size() == 4) {
            str = str3;
            selection = null;
        } else {
            Calendar calendar = Calendar.getInstance();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentDate = dateFormat.parse(dateFormat.format(new Date()));
            long maxDate = Objects.requireNonNull(currentDate).getTime();
            long minDate = 0;
            for (Object o : arrayList2) {
                int integer = Integer.parseInt((String) o);
                String str4 = " 00:00:00";
                String str5 = StringUtils.SPACE;
                Object obj2 = obj;
                String selection2 = "Do:";
                if (integer == i) {
                    calendar.add(Calendar.HOUR_OF_DAY, -24);
                    String dateType = dateFormat.format(calendar.getTime());
                    StringBuilder sb = new StringBuilder();
                    Date date = currentDate;
                    str2 = str3;
                    sb.append(dateType.substring(0, dateType.indexOf(str5)));
                    sb.append(str4);
                    currentDate = dateFormat.parse(sb.toString());
                    String sb2 = currentDate +
                            address;
                    Log.d(selection2, sb2);
                    minDate = Objects.requireNonNull(currentDate).getTime();
                } else {
                    str2 = str3;
                }
                if (integer == 2) {
                    calendar.add(Calendar.DAY_OF_WEEK, -7);
                    String dateType2 = dateFormat.format(calendar.getTime());
                    String sb3 = dateType2.substring(0, dateType2.indexOf(str5)) +
                            str4;
                    currentDate = dateFormat.parse(sb3);
                    String sb4 = currentDate +
                            address;
                    Log.d(selection2, sb4);
                    minDate = Objects.requireNonNull(currentDate).getTime();
                }
                if (integer == 3) {
                    calendar.add(Calendar.DAY_OF_MONTH, -31);
                    int month = calendar.get(Calendar.MONTH) + 1;
                    int year = calendar.get(Calendar.YEAR);
                    String sb5 = year +
                            "-" +
                            month +
                            "-01 00:00:00";
                    currentDate = dateFormat.parse(sb5);
                    String sb6 = currentDate +
                            address;
                    Log.d(selection2, sb6);
                    minDate = Objects.requireNonNull(currentDate).getTime();
                }
                if (integer == 4) {
                    calendar.add(Calendar.DAY_OF_YEAR, -365);
                    int year2 = calendar.get(Calendar.YEAR);
                    String sb7 = year2 +
                            "-01-01 00:00:00";
                    Date currentDate2 = dateFormat.parse(sb7);
                    minDate = Objects.requireNonNull(currentDate2).getTime();
                    currentDate = currentDate2;
                }
                obj = obj2;
//                arrayList2 = arrayList;
                str3 = str2;
                i = 1;
            }
            str = str3;
            selection = "date >= " +
                    minDate +
                    " AND " +
                    "date" +
                    " <= " +
                    maxDate;
        }
        ContentResolver contentResolver2 = context.getContentResolver();
        Cursor cursor = contentResolver2.query(Sms.CONTENT_URI, new String[]{"address", "body", "read", "type", "date", "date_sent"}, selection, null, null);
        if (Objects.requireNonNull(cursor).getCount() > 0) {
            String vfile = "messages.xml";
            try {
                File file = new File(context.getExternalFilesDir(null), str);
                if (!file.exists()) {
                    try {
                        file.mkdir();
                    } catch (Exception e12) {
                        e6 = e12;
                        e6.printStackTrace();
                        return "0 KB";
                    }
                }
                File fileMessages = new File(file, vfile);
                if (!fileMessages.exists()) {
                    fileMessages.createNewFile();
                }
                if (fileMessages.exists()) {
                    Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                    Element rootEle = dom.createElement(str);
                    while (cursor.moveToNext()) {
                        try {
                            Element messEle = dom.createElement("message");
                            rootEle.appendChild(messEle);
                            int i2 = 0;
                            while (i2 < cursor.getColumnCount()) {
                                if (cursor.getString(i2) != null) {
                                    contentResolver = contentResolver2;
                                    try {
                                        if (cursor.getColumnName(i2).equals("address") && !cursor.getString(i2).equals(address)) {
                                            lengMessage++;
                                            address = cursor.getString(i2);
                                        }
                                        Element e13 = dom.createElement(cursor.getColumnName(i2));
                                        e13.appendChild(dom.createTextNode(cursor.getString(i2)));
                                        messEle.appendChild(e13);
                                    } catch (Exception e19) {
                                        e6 = e19;
                                        e6.printStackTrace();
                                        return "0 KB";
                                    }
                                } else {
                                    contentResolver = contentResolver2;
                                }
                                i2++;
                                contentResolver2 = contentResolver;
                            }
                        } catch (Exception e25) {
                            e6 = e25;
                            e6.printStackTrace();
                            return "0 KB";
                        }
                    }
                    try {
                        dom.appendChild(rootEle);
                        Transformer tr = TransformerFactory.newInstance().newTransformer();
                        tr.setOutputProperty("indent", "yes");
                        tr.setOutputProperty("method", "xml");
                        tr.setOutputProperty("encoding", "UTF-8");
                        tr.setOutputProperty("doctype-system", "messages.dtd");
                        tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                        tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fileMessages)));
                        DataItem dataItem = new DataItem();
                        ClientActivity.SIZE_ALL_ITEM[2] = fileMessages.length();
                        result = dataItem.sizeToString(fileMessages.length());
                        AES encryptaes = new AES();
                        encryptaes.encrypt(fileMessages);
                        return result;
                    } catch (FileNotFoundException e32) {
                        e = e32;
                        e.printStackTrace();
                        return "0 KB";
                    } catch (TransformerConfigurationException e35) {
                        e4 = e35;
                        e4.printStackTrace();
                        return "0 KB";
                    } catch (TransformerException e36) {
                        e5 = e36;
                        e5.printStackTrace();
                        return "0 KB";
                    } catch (Exception e37) {
                        e6 = e37;
                        e6.printStackTrace();
                        return "0 KB";
                    }
                }
            } catch (FileNotFoundException e38) {
                e = e38;
                e.printStackTrace();
                return "0 KB";
            } catch (IOException e39) {
                e2 = e39;
                e2.printStackTrace();
                return "0 KB";
            } catch (ParserConfigurationException e40) {
                e3 = e40;
                e3.printStackTrace();
                return "0 KB";
            } catch (Exception e43) {
                e6 = e43;
                e6.printStackTrace();
                return "0 KB";
            }
        }
        return "0 KB";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void restoreMessages() {
        if (context.getPackageName().equals(Sms.getDefaultSmsPackage(context))) {
            executeRestore();
        } else if (VERSION.SDK_INT >= 29) {
            RoleManager roleManager = (RoleManager) context.getSystemService(RoleManager.class);
            String str = "android.app.role.SMS";
            if (Objects.requireNonNull(roleManager).isRoleAvailable(str) && !roleManager.isRoleHeld(str)) {
                context.startActivityForResult(roleManager.createRequestRoleIntent(str), 121);
            }
        } else {
            Intent intent = new Intent("android.provider.Telephony.ACTION_CHANGE_DEFAULT");
            intent.putExtra("package", context.getPackageName());
            context.startActivityForResult(intent, 121);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public static void executeRestore() {
        String path = context.getExternalFilesDir(null) +
                "/messages/messages.xml";
        AES encryptaes = new AES();
        File file = encryptaes.decrypt(new File(path));
        String str = "messenger";
        String sb2 = "executeRestore: " +
                file;
        Log.d(str, sb2);
        if (file.exists()) {
            Log.d(str, "restoreMessages: ");
            try {
                NodeList nl = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("message");
                for (int i = 0; i < nl.getLength(); i++) {
                    ContentValues contentValues = new ContentValues();
                    NodeList childNode = nl.item(i).getChildNodes();
                    for (int j = 0; j < childNode.getLength(); j++) {
                        Node node = childNode.item(j);
                        if (node.getNodeType() == 1) {
                            contentValues.put(node.getNodeName(), node.getTextContent());
                            String sb3 = node.getNodeName() +
                                    ": " +
                                    node.getTextContent();
                            Log.d("test:", sb3);
                        }
                    }
                    context.getContentResolver().insert(Sms.CONTENT_URI, contentValues);
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        } else {
            Log.d(str, "restoreMessages: not");
        }
        file.delete();
    }
}
