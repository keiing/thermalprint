package com.thermalprint.thermalprint.utils;

import android.graphics.Bitmap;

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import net.posprinter.utils.StringUtils;


public class c {

    public static byte[] string2Bytes(String str, String charSet) {
        try {
            return str.getBytes(charSet);
        } catch (UnsupportedEncodingException var2) {
            var2.printStackTrace();
            return new byte[0];
        }
    }

    public static byte[] b() {
        return new byte[]{12};
    }

    public static byte[] o(int var0) {
        return new byte[]{16, 4, (byte) var0};
    }

    public static byte[] r(int var0) {
        return new byte[]{27, 32, (byte) var0};
    }

    public static byte[] n(int var0) {
        return new byte[]{27, 33, (byte) var0};
    }

    public static byte[] d(int var0, int var1) {
        byte[] var2;
        byte[] var10000 = var2 = new byte[4];
        var2[0] = 27;
        var2[1] = 36;
        var2[2] = (byte) var0;
        var10000[3] = (byte) var1;
        return var10000;
    }

    public static byte[] a(int var0, int var1, int var2, byte[] var3) {
        byte[] var4;
        byte[] var10000 = var4 = new byte[5];
        var4[0] = 27;
        var4[1] = 42;
        var4[2] = (byte) var0;
        var4[3] = (byte) var1;
        var4[4] = (byte) var2;
        return a(var10000, var3);
    }

    public static byte[] l(int var0) {
        return new byte[]{27, 45, (byte) var0};
    }

    public static byte[] e() {
        return new byte[]{27, 50};
    }

    public static byte[] s(int var0) {
        return new byte[]{27, 51, (byte) var0};
    }

    public static byte[] a() {
        return new byte[]{27, 64};
    }

    public static byte[] i(int var0) {
        return new byte[]{27, 69, (byte) var0};
    }

    public static byte[] b(int var0) {
        return new byte[]{27, 74, (byte) var0};
    }

    public static byte[] c() {
        return new byte[]{27, 76};
    }

    public static byte[] g(int var0) {
        return new byte[]{27, 77, (byte) var0};
    }

    public static byte[] d() {
        return new byte[]{27, 83};
    }

    public static byte[] m(int var0) {
        return new byte[]{27, 84, (byte) var0};
    }

    public static byte[] a(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
        byte[] var8;
        byte[] var10000 = var8 = new byte[10];
        var8[0] = 27;
        var8[1] = 87;
        var8[2] = (byte) var0;
        var8[3] = (byte) var1;
        var8[4] = (byte) var2;
        var8[5] = (byte) var3;
        var8[6] = (byte) var4;
        var8[7] = (byte) var5;
        var8[8] = (byte) var6;
        var10000[9] = (byte) var7;
        return var10000;
    }

    public static byte[] e(int var0, int var1) {
        byte[] var2;
        byte[] var10000 = var2 = new byte[4];
        var2[0] = 27;
        var2[1] = 92;
        var2[2] = (byte) var0;
        var10000[3] = (byte) var1;
        return var10000;
    }

    public static byte[] c(int var0) {
        return new byte[]{27, 97, (byte) var0};
    }

    public static byte[] a(int var0, int var1, int var2) {
        byte[] var3;
        byte[] var10000 = var3 = new byte[5];
        var3[0] = 27;
        var3[1] = 112;
        var3[2] = (byte) var0;
        var3[3] = (byte) var1;
        var10000[4] = (byte) var2;
        return var10000;
    }

    public static byte[] d(int var0) {
        return new byte[]{27, 116, (byte) var0};
    }

    public static byte[] j(int var0) {
        return new byte[]{27, 123, (byte) var0};
    }

    public static byte[] a(int var0, int var1) {
        byte[] var2;
        byte[] var10000 = var2 = new byte[4];
        var2[0] = 28;
        var2[1] = 112;
        var2[2] = (byte) var0;
        var10000[3] = (byte) var1;
        return var10000;
    }

    public static byte[] e(int var0) {
        return new byte[]{29, 33, (byte) var0};
    }

    public static byte[] c(int var0, int var1) {
        byte[] var2;
        byte[] var10000 = var2 = new byte[4];
        var2[0] = 29;
        var2[1] = 36;
        var2[2] = (byte) var0;
        var10000[3] = (byte) var1;
        return var10000;
    }

    public static byte[] k(int var0) {
        return new byte[]{29, 66, (byte) var0};
    }

    public static byte[] h(int var0) {
        return new byte[]{29, 72, (byte) var0};
    }

    public static byte[] f(int var0) {
        return new byte[]{29, 86, (byte) var0};
    }

    public static byte[] b(int var0, int var1) {
        if (var0 != 66) {
            return new byte[0];
        } else {
            byte[] var2;
            byte[] var10000 = var2 = new byte[4];
            var2[0] = 29;
            var2[1] = 86;
            var2[2] = (byte) var0;
            var10000[3] = (byte) var1;
            return var10000;
        }
    }

    public static byte[] f(int var0, int var1) {
        byte[] var2;
        byte[] var10000 = var2 = new byte[4];
        var2[0] = 29;
        var2[1] = 92;
        var2[2] = (byte) var0;
        var10000[3] = (byte) var1;
        return var10000;
    }

    public static byte[] a(int var0) {
        return new byte[]{29, 97, (byte) var0};
    }

    public static byte[] p(int var0) {
        return new byte[]{29, 104, (byte) var0};
    }

    public static byte[] a(int var0, int var1, String var2, String var3) {
        byte[] var4;
        byte[] var10000 = var4 = new byte[4];
        var4[0] = 29;
        var4[1] = 107;
        var4[2] = (byte) var0;
        var4[3] = (byte) var1;
        return a(var10000, string2Bytes(var2, var3));
    }

    public static byte[] q(int var0) {
        return new byte[]{29, 119, (byte) var0};
    }

    public static byte[] b(int var0, int var1, String var2, String var3) {
        int var4;
        byte[] var6;
        int var7;
        if ((var7 = (var6 = string2Bytes(var2, var3)).length) <= 255) {
            var4 = 0;
        } else {
            var4 = var7 / 256;
            var7 %= 256;
        }

        byte[] var5;
        byte[] var10000 = var5 = new byte[19];
        var5[0] = 29;
        var5[1] = 40;
        var5[2] = 107;
        var5[3] = 48;
        var5[4] = 103;
        var5[5] = (byte) var0;
        var5[6] = 29;
        var5[7] = 40;
        var5[8] = 107;
        var5[9] = 48;
        var5[10] = 105;
        var5[11] = (byte) var1;
        var5[12] = 29;
        var5[13] = 40;
        var5[14] = 107;
        var5[15] = 48;
        var5[16] = -128;
        var5[17] = (byte) var7;
        var5[18] = (byte) var4;
        return a(a(var10000, var6), new byte[]{29, 40, 107, 48, -127});
    }

    private static byte[] a(byte[] var0, byte[] var1) {
        byte[] var2;
        byte[] var10000 = var2 = new byte[var0.length + var1.length];
        byte[] var10001 = var1;
        byte[] var10003 = var0;
        byte[] var10004 = var0;
        int var3 = var0.length;
        System.arraycopy(var10004, 0, var2, 0, var3);
        var3 = var10003.length;
        int var4 = var1.length;
        System.arraycopy(var10001, 0, var2, var3, var4);
        return var10000;
    }
}
