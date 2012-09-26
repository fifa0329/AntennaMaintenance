package com.station.activity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Log;

public class Security {
    private static final boolean M2M_SECURITY_ENABLE = false;
    
    private static String calculateMD5(byte[] body, int len) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(body, 0, len);
            byte b[] = md.digest();

            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }

            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] decrypt(byte[] body, int len, byte[] key) {
        if (!M2M_SECURITY_ENABLE) {
            byte[] text = new byte[len];
            System.arraycopy(body, 0, text, 0, len);
            return text;
        } else {
            if (len < 32) {
                return null;
            }

            new RC4(key).rc4(body, len);

            String md5 = calculateMD5(body, len - 32);

            if (!md5.equals(new String(body, len - 32, 32))) {
                Log.e("TowerManager", "Data package MD5 is invalid");
                return null;
            }

            byte[] text = new byte[len - 32];
            System.arraycopy(body, 0, text, 0, text.length);
            return text;
        }
    }

    public static byte[] encrypt(byte[] body, int len, byte[] key) {
        if (!M2M_SECURITY_ENABLE) {
            byte[] text = new byte[len];
            System.arraycopy(body, 0, text, 0, len);
            return text;
        } else {
            String md5 = calculateMD5(body, len);
            byte[] text = new byte[len + 32];
            System.arraycopy(body, 0, text, 0, len);
            System.arraycopy(md5.getBytes(), 0, text, len, 32);

            new RC4(key).rc4(text, text.length);
            return text;
        }
    }
}
