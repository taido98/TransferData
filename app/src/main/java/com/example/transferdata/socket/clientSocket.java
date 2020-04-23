package com.example.transferdata.socket;

import com.example.transferdata.security.AES;
import com.example.transferdata.security.RSA;
import com.example.transferdata.tranferdata.ClientActivity;
import com.example.transferdata.tranferdata.TransferActivity;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.PublicKey;
import java.util.Iterator;
import java.util.List;



public class clientSocket extends Thread {
    public static int SIZE_ALL_FILE = 0;
    public static int size = 0;
    private TransferActivity context;
    private List<String> data;
    private String host;
    private Socket socket = new Socket();

    public clientSocket(String hostAddress, TransferActivity context2, List<String> data2) {
        this.host = hostAddress;
        this.context = context2;
        this.data = data2;
    }

    public void run() {
        ObjectOutputStream objectOutput;
        int theByte;
        File[] file;
        super.run();
        try {
            this.socket.connect(new InetSocketAddress(this.host, 8888), 500);
//            System.out.println("Star client .................");
            BufferedOutputStream bos = new BufferedOutputStream(this.socket.getOutputStream());
            DataOutputStream dos = new DataOutputStream(bos);
            OutputStream outputStream = this.socket.getOutputStream();
            ObjectOutputStream objectOutput2 = new ObjectOutputStream(outputStream);
            File[] file2 = new File[this.data.size()];
            ObjectInputStream inputStream = new ObjectInputStream(this.socket.getInputStream());
            com.example.transferdata.security.RSA.publicKey = (PublicKey) inputStream.readObject();
            PrintStream printStream = System.out;
            String sb = "+++++++++++ public key " +
                    RSA.publicKey;
            printStream.println(sb);
            com.example.transferdata.security.RSA encryptrsa = new RSA();
            objectOutput2.writeObject(encryptrsa.encode(AES.secretKey, com.example.transferdata.security.RSA.publicKey));
            int index = 0;
            SIZE_ALL_FILE = 0;
            size = 0;
            Iterator it = this.data.iterator();
            while (it.hasNext()) {
                file2[index] = new File((String) it.next());
                Iterator it2 = it;
                SIZE_ALL_FILE = (int) (((double) SIZE_ALL_FILE) + (((double) file2[index].length()) * 1.0E-6d));
                index++;
                it = it2;
            }
            boolean[] s = new boolean[ClientActivity.listItem.size()];
            for (int i = 0; i < ClientActivity.listItem.size(); i++) {
                s[i] = ( ClientActivity.listItem.get(i)).isChecked();
            }
            objectOutput2.writeObject(s);
            objectOutput2.flush();
            dos.writeInt(this.data.size());
            dos.writeInt(SIZE_ALL_FILE);
            dos.flush();
            int index2 = 0;
            int length = file2.length;
            int i2 = 0;
            while (i2 < length) {
                File f = file2[i2];
                long leng = f.length();
                dos.writeLong(leng);
                String name = f.getName();
                dos.writeUTF(data.get(index2));
                index2++;
                dos.flush();
                OutputStream outputStream2 = outputStream;
                BufferedInputStream bufferedInput = new BufferedInputStream(new FileInputStream(f));
                while (true) {
                    objectOutput = objectOutput2;
                    int read = bufferedInput.read();
                    theByte = read;
                    file = file2;
                    if (read == -1) {
                        break;
                    }
                    int theByte2 = theByte;
                    bos.write(theByte2);
                    file2 = file;
                    objectOutput2 = objectOutput;
                }
                bufferedInput.close();
                ObjectInputStream inputStream2 = inputStream;
                size = (int) (((double) size) + (((double) leng) * 1.0E-6d));
                TransferActivity.speedTranfer();
                i2++;
                outputStream = outputStream2;
                file2 = file;
                objectOutput2 = objectOutput;
                inputStream = inputStream2;
            }
            dos.close();
            Socket socket2 = this.socket;
            if (socket2 != null && socket2.isConnected()) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e2) {
            e2.printStackTrace();
            Socket socket3 = this.socket;
            if (socket3 != null && socket3.isConnected()) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e3) {
            e3.printStackTrace();
            Socket socket4 = this.socket;
            if (socket4 != null && socket4.isConnected()) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (Throwable th) {
            Socket socket5 = this.socket;
            if (socket5 != null && socket5.isConnected()) {
                try {
                    this.socket.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            try {
                throw th;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }
}

