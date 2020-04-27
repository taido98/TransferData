package com.example.transferdata.socket;

import android.app.Activity;
import android.content.Intent;
import android.net.wifi.p2p.WifiP2pManager.ActionListener;
import android.os.AsyncTask;
import android.os.Environment;

import com.example.transferdata.connect.ConnectActivity;
import com.example.transferdata.security.RSA;
import com.example.transferdata.tranferdata.ReceiverActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Objects;

public class serverSocket extends AsyncTask<String, String, String> {
    public static int SIZE_ALL_FILE = 0;
    public static boolean[] listItem;
    public static int size = 0;
    com.example.transferdata.security.AES AES;
    private Socket client;
    private Activity context;
    private ServerSocket serverSocket;

    public serverSocket(Activity context2) {
        this.context = context2;
    }

    /* access modifiers changed from: protected */
    public String doInBackground(String... params) {
        ObjectOutputStream outputStream;
        try {
            this.serverSocket = new ServerSocket(8888);
            System.out.println("+++++++++++++++++++ wait client");
            this.client = this.serverSocket.accept();
            ObjectOutputStream outputStream2 = new ObjectOutputStream(this.client.getOutputStream());
            com.example.transferdata.security.RSA encryptrsa = new RSA();
            encryptrsa.createKey();
            outputStream2.writeObject(com.example.transferdata.security.RSA.publicKey);
            outputStream2.flush();
            PrintStream printStream = System.out;
            String sb = "++++++++++ public key " +
                    com.example.transferdata.security.RSA.publicKey;
            printStream.println(sb);
            SIZE_ALL_FILE = 0;
            size = 0;
            BufferedInputStream bis = new BufferedInputStream(this.client.getInputStream());
            DataInputStream dis = new DataInputStream(bis);
            InputStream inputStream = this.client.getInputStream();
            ObjectInputStream objectInput = new ObjectInputStream(inputStream);
            byte[] keyEncode = (byte[]) objectInput.readObject();
            com.example.transferdata.security.RSA encryptrsa2 = new RSA();
            com.example.transferdata.security.AES.secretKey = encryptrsa2.decode(keyEncode, com.example.transferdata.security.RSA.privateKey);
            listItem = (boolean[]) objectInput.readObject();
            int filesCount = dis.readInt();
            SIZE_ALL_FILE = dis.readInt();
            Intent intent = new Intent(this.context, ReceiverActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            this.context.startActivity(intent);
            this.context.finish();
            int i = 0;
            while (i < filesCount) {
                long fileLeng = dis.readLong();
                String fileName = dis.readUTF();
                PrintStream printStream2 = System.out;
                String sb2 = "++++++++ file name" +
                        fileName;
                printStream2.println(sb2);
                File f = new File(fileName);
                File dirs = new File(Objects.requireNonNull(f.getParent()));
                if (!dirs.exists()) {
                    dirs.mkdirs();
                }
                if (!dirs.exists()) {
                    StringBuilder sb3 = new StringBuilder();
                    outputStream = outputStream2;
                    sb3.append(Environment.getExternalStorageDirectory());
                    sb3.append("/Transfer Data/");
                    sb3.append(f.getName());
                    String path = sb3.toString();
                    f = new File(path);
                    File dirs2 = new File(Objects.requireNonNull(f.getParent()));
                    if (!dirs2.exists()) {
                        dirs2.mkdir();
                    }
                } else {
                    outputStream = outputStream2;
                }
                FileOutputStream fileOutput = new FileOutputStream(f);
                BufferedOutputStream bufferedOutput = new BufferedOutputStream(fileOutput);
                long j = 0;
                float sumSize = 0.0f;
                while (j < fileLeng) {
                    InputStream inputStream2 = inputStream;
                    ObjectInputStream objectInput2 = objectInput;
                    sumSize = (float) (((double) sumSize) + 1.0E-6d);
                    if (sumSize == 1.0f) {
                        size++;
                        sumSize = 0.0f;
                    }
                    bufferedOutput.write(bis.read());
                    j++;
                    inputStream = inputStream2;
                    objectInput = objectInput2;
                }
                InputStream inputStream3 = inputStream;
                ObjectInputStream objectInput3 = objectInput;
                bufferedOutput.close();
                i++;
                outputStream2 = outputStream;
                inputStream = inputStream3;
                objectInput = objectInput3;
            }
            dis.close();
            this.serverSocket.close();
            this.client.close();
            ConnectActivity.manager.removeGroup(ConnectActivity.channel, new ActionListener() {
                public void onSuccess() {
                }

                public void onFailure(int reason) {
                }
            });
            try {
                this.serverSocket.close();
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException | ClassNotFoundException e2) {
            e2.printStackTrace();
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                this.client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Throwable th) {
            try {
                this.serverSocket.close();
                this.client.close();
            } catch (IOException e4) {
                e4.printStackTrace();
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
        return null;
    }
}
