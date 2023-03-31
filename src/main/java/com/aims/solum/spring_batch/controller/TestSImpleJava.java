package com.aims.solum.spring_batch.controller;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class TestSImpleJava {

    public static void main(String[] args) {
        int width = 100;
        byte[] data = new byte[2];
        data[0] = (byte) ((width >> 8) & 0xff);
        data[1] = (byte) (width & 0xff);

        final byte[] bytes = intToBytes(width);
        System.out.println(Arrays.toString(data) + "\n" + Arrays.toString(bytes));
    }

    private static byte[] intToBytes(final int i) {
        ByteBuffer bb = ByteBuffer.allocate(4);
        bb.putInt(i);
        return bb.array();
    }
}