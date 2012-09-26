package com.station.activity;

public class RC4 {

    private byte state[] = new byte[256];
    private int x;
    private int y;

    public RC4(byte[] key) {
        // change to avoid ascii
        for (int i = 0; i < 128; i++) {
            state[i] = (byte) (i + 128);
            state[i + 128] = (byte) (i + 128);
        }

        x = 0;
        y = 0;

        int index1 = 0;
        int index2 = 0;
        byte tmp;

        for (int i = 0; i < 256; i++) {
            index2 = ((key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;

            tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;

            index1 = (index1 + 1) % key.length;
        }
    }

    public void rc4(byte[] buf, int len) {
        int xorIndex;
        byte tmp;

        for (int i = 0; i < len; i++) {
            x = (x + 1) & 0xff;
            y = ((state[x] & 0xff) + y) & 0xff;

            tmp = state[x];
            state[x] = state[y];
            state[y] = tmp;

            xorIndex = ((state[x] & 0xff) + (state[y] & 0xff)) & 0xff;
            buf[i] ^= state[xorIndex];
        }
    }
}
