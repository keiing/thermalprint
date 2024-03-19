package com.thermalprint.thermalprint.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;

import com.thermalprint.thermalprint.model.PTable;

import net.posprinter.utils.StringUtils;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


class a {
    protected static String b = "GBK";

    public static byte[] a(String str) {
        return c.string2Bytes(str, b);
    }

    public static Bitmap a(Bitmap var0, int var1) {
        int var2;
        int var10000 = var2 = var0.getWidth();
        int var3 = var0.getHeight();
        if (var10000 <= var1) {
            return var0;
        } else {
            Bitmap var7 = var0;
            int var10001 = var3 * var1 / var2;
            float var5 = (float) var1 / (float) var2;
            float var6 = (float) var10001 / (float) var3;
            Matrix var4;
            Matrix var8 = var4 = new Matrix();
            var8.postScale(var5, var6);
            return Bitmap.createBitmap(var7, 0, 0, var2, var3, var4, true);
        }
    }
//
//    public static List<Bitmap> a(int var0, Bitmap var1) {
//        int var2 = var1.getWidth();
//        int var3;
//        int var4;
//        boolean var5;
//        if ((var4 = (var3 = var1.getHeight()) % var0) == 0) {
//            var5 = true;
//        } else {
//            var5 = false;
//        }
//
//        if (var4 == 0) {
//            var4 = var3 / var0;
//        } else {
//            var4 = var3 / var0 + 1;
//        }
//
//        ArrayList var6;
//        var6 = new ArrayList.<init>();
//
//        for (int var7 = 0; var7 < var4; ++var7) {
//            Bitmap var8;
//            if (!var5 && var7 == var4 - 1) {
//                int var10;
//                int var9 = var3 - (var10 = var7 * var0);
//                var8 = Bitmap.createBitmap(var1, 0, var10, var2, var9);
//            } else {
//                var8 = Bitmap.createBitmap(var1, 0, var7 * var0, var2, var0);
//            }
//
//            var6.add(var8);
//        }
//
//        return var6;
//    }
}


public class PosPrinter extends a {
    /// 换1行
    static public byte[] feedLine() {
        return feedLine(1);
    }

    /// 换n行
    static public byte[] feedLine(int lineCount) {
        byte[] var2;
        Arrays.fill(var2 = new byte[lineCount], (byte) 10);
        return var2;
    }


    /// 打印文本内容
    static public ArrayList<byte[]> printText(String data, int alignment, int attribute, int textSize) {
        ArrayList<byte[]> var5 = new ArrayList<byte[]>();
        var5.add(c.c(alignment));
        if ((attribute & 1) > 0) {
            var5.add(c.n(1));
        }

        if ((attribute & 8) > 0) {
            var5.add(c.i(1));
        }

        if ((attribute & 16) > 0) {
            var5.add(c.k(1));
        }

        if ((attribute & 256) > 0) {
            var5.add(c.l(2));
        } else if ((attribute & 128) > 0) {
            var5.add(c.l(1));
        }

        var5.add(c.e(textSize));
        var5.add(a.a(data));
        var5.add(c.c(0));
        var5.add(c.n(0));
        var5.add(c.i(0));
        var5.add(c.k(0));
        var5.add(c.l(0));
        var5.add(c.e(0));

        return var5;
    }

    /// 设置剧中方向
    static public ArrayList<byte[]> printTextAlignment(String data, int alignment) {
        return printText(data, alignment, 0, 0);
    }

    /// 返回切纸数据
    static public byte[] cutHalfAndFeed(int distance) {
        return c.b(
                66,
                distance
        );
    }

    /// 初始化
    @NotNull
    public static byte[] initializePrinter() {
        return c.a();
    }

    /// 打印一行内容
    @NotNull
    public static byte[] printString(@NotNull String str) {
        return a.a(str);
    }


    /// 打印表单
    public static byte[] printTable(PTable table) {
        return printString(table.getTableText());
    }
}
