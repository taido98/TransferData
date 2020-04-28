package com.example.transferdata.service;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.provider.CallLog.Calls;
import android.util.Log;
import androidx.core.app.ActivityCompat;

import com.example.transferdata.adapter.DataItem;
import com.example.transferdata.security.AES;
import com.example.transferdata.tranferdata.ClientActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class getCallLog {
    Activity context;
    DataItem dataItem = new DataItem();

    public getCallLog(Activity context2) {
        this.context = context2;
    }

    public String backupCallogs() {
        FileNotFoundException e;
        IOException e2;
        ParserConfigurationException e3;
        TransformerConfigurationException e4;
        TransformerException e5;
        Exception e6;
        String result;
        String str = "callogs";
        String str2 = "Selected : 0 item - 0 MB";
        if (ActivityCompat.checkSelfPermission(this.context, "android.permission.READ_CALL_LOG") != 0) {
            return str2;
        }
        ContentResolver contentResolver = this.context.getContentResolver();
        Cursor cursor = contentResolver.query(Calls.CONTENT_URI, new String[]{"duration", "date", "type", "number", "features", "geocoded_location", "is_read"}, null, null, null);
        if (cursor.getCount() > 0) {
            String vfile = "callogs.xml";
            try {
                File file = new File(this.context.getExternalFilesDir(null), str);
                if (!file.exists()) {
                    try {
                        file.mkdir();
                    } catch (Exception e12) {
                        e6 = e12;
                        e6.printStackTrace();
                        return str2;
                    }
                }
                File fileCallLog = new File(file, vfile);
                if (!fileCallLog.exists()) {
                    fileCallLog.createNewFile();
                }
                if (fileCallLog.exists()) {
                    Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
                    Element rootEle = dom.createElement(str);
                    while (cursor.moveToNext()) {
                        Element callEle = dom.createElement("callog");
                        rootEle.appendChild(callEle);
                        String[] smsHeader = new String[cursor.getColumnCount()];
                        for (int i = 0; i < cursor.getColumnCount(); i++) {
                            if (cursor.getString(i) != null) {
                                Element e13 = dom.createElement(cursor.getColumnName(i));
                                e13.appendChild(dom.createTextNode(cursor.getString(i)));
                                callEle.appendChild(e13);
                            }
                        }
                    }
                    dom.appendChild(rootEle);
                    Transformer tr = TransformerFactory.newInstance().newTransformer();
                    tr.setOutputProperty("indent", "yes");
                    tr.setOutputProperty("method", "xml");
                    tr.setOutputProperty("encoding", "UTF-8");
                    tr.setOutputProperty("doctype-system", "messages.dtd");
                    tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
                    tr.transform(new DOMSource(dom), new StreamResult(new FileOutputStream(fileCallLog)));
//                    float sizekb = ((float) fileCallLog.length()) / 1024.0f;
//                    float sizeMb = (float) (((double) sizekb) * 0.001d);
                    try {
                        ClientActivity.SIZE_ALL_ITEM[1] =fileCallLog.length();
//                        String str3 = "%.02f";
//                        String str4 = " item  - ";
//                        String vfile2 = "Selected : ";
                        result = dataItem.sizeToString(fileCallLog.length());
                        AES encryptaes = new AES();
                        encryptaes.encrypt(fileCallLog);
                        return result;
                    } catch (Exception e19) {
                        e6 = e19;
                        e6.printStackTrace();
                        return str2;
                    }
                }
            } catch (FileNotFoundException e20) {
                e = e20;
                e.printStackTrace();
                return str2;
            } catch (IOException e21) {
                e2 = e21;
                e2.printStackTrace();
                return str2;
            } catch (ParserConfigurationException e22) {
                e3 = e22;
                e3.printStackTrace();
                return str2;
            } catch (TransformerConfigurationException e23) {
                e4 = e23;
                e4.printStackTrace();
                return str2;
            } catch (TransformerException e24) {
                e5 = e24;
                e5.printStackTrace();
                return str2;
            } catch (Exception e25) {
                e6 = e25;
                e6.printStackTrace();
                return str2;
            }
        }
        return str2;
    }

    public void restoreCallogs() {
        String path = this.context.getExternalFilesDir(null) +
                "/callogs/callogs.xml";
        AES encryptaes = new AES();
        File file = encryptaes.decrypt(new File(path));
        if (file.exists()) {
            try {
                NodeList nl = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file).getElementsByTagName("callog");
                int i = 0;
                while (i < nl.getLength()) {
                    ContentValues contentValues = new ContentValues();
                    NodeList childNode = nl.item(i).getChildNodes();
                    for (int j = 0; j < childNode.getLength(); j++) {
                        Node node = childNode.item(j);
                        if (node.getNodeType() == 1 && node.getTextContent() != null) {
                            contentValues.put(node.getNodeName(), node.getTextContent());
                            String sb2 = node.getNodeName() +
                                    ": " +
                                    node.getTextContent();
                            Log.d("test:", sb2);
                        }
                    }
                    if (ActivityCompat.checkSelfPermission(this.context, "android.permission.WRITE_CALL_LOG") == 0) {
                        this.context.getContentResolver().insert(Calls.CONTENT_URI, contentValues);
                        i++;
                    } else {
                        return;
                    }
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e2) {
                e2.printStackTrace();
            } catch (IOException e3) {
                e3.printStackTrace();
            }
        }
        file.delete();
    }
}
