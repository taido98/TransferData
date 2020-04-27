package com.example.transferdata.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class AES {
    public static SecretKey secretKey = null;

    private static void encode(String file_loc, String file_des, SecretKey secretKey2) throws Exception {
        byte[] data = new byte[2048];
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(1, secretKey2);
        FileInputStream fileIn = new FileInputStream(file_loc);
        CipherOutputStream cipherOut = new CipherOutputStream(new FileOutputStream(file_des), cipher);
        while (true) {
            int read = fileIn.read(data);
            if (read != -1) {
                cipherOut.write(data, 0, read);
            } else {
                cipherOut.close();
                fileIn.close();
                return;
            }
        }
    }

    private static void decode(String file_loc, String file_des, SecretKey secretKey2) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
        cipher.init(2, secretKey2);
        FileInputStream fileIn = new FileInputStream(file_loc);
        CipherInputStream cipherIn = new CipherInputStream(fileIn, cipher);
        FileOutputStream fileOut = new FileOutputStream(file_des);
        while (true) {
            int read = cipherIn.read();
            if (read != -1) {
                fileOut.write(read);
            } else {
                fileIn.close();
                cipherIn.close();
                fileOut.close();
                return;
            }
        }
    }

    public void createKey() {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(256);
            secretKey = keyGen.generateKey();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public void encrypt(File file) {
        try {
            String sb = file.getParent() +
                    "/temp";
            File fileTemp = new File(sb);
            encode(file.getPath(), fileTemp.getPath(), secretKey);
            file.delete();
            fileTemp.renameTo(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public File decrypt(File file) {
        String sb = file.getParent() +
                "/temp";
        File fileTemp = new File(sb);
        try {
            decode(file.getPath(), fileTemp.getPath(), secretKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileTemp;
    }
}
