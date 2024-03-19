package com.thermalprint.thermalprint.model;


import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Iterator;

public class PTable {
    private Integer[] numberOfSingleBytes;
    private ArrayList<String[]> rows;

    public PTable(String[] titles, Integer[] numberOfSingleBytesPerCol) {
        ArrayList var3 = new ArrayList();
        ArrayList var10002 = new ArrayList();
        this.rows = var10002;
        if (titles.length == numberOfSingleBytesPerCol.length) {
            this.numberOfSingleBytes = numberOfSingleBytesPerCol;
            var3.add(titles);
        } else {
            throw new IllegalArgumentException("titles.length != numberOfSingleBytesPerCol.length");
        }
    }

    @SuppressLint("NewApi")
    private String printRow(String[] row) {
        int var2;
        String[] var3 = new String[var2 = row.length];
        StringBuilder var4;
        var4 = new StringBuilder();

        for (int var5 = 0; var5 < row.length; ++var5) {
            String var6;
            if ((var6 = row[var5]) == null) {
                var6 = "";
            } else {
                var6 = var6.trim();
            }

            Pair var10002;
            int var7;
            if ((var7 = var6.indexOf("\n")) != -1) {
                String var8;
                if (this.getStringCharacterLength(var8 = var6.substring(0, var7)) >= this.numberOfSingleBytes[var5]) {
                    var10002 = this.getSubString(var6, this.numberOfSingleBytes[var5]);
                    var6 = (String) var10002.first;
                    var3[var5] = (String) var10002.second;
                } else {
                    var3[var5] = var6.substring(var7 + 1);
                    var6 = var8;
                }
            } else if (this.getStringCharacterLength(var6) >= this.numberOfSingleBytes[var5]) {
                var10002 = this.getSubString(var6, this.numberOfSingleBytes[var5]);
                var6 = (String) var10002.first;
                var3[var5] = (String) var10002.second;
            } else {
                var3[var5] = "";
            }

            var4.append(var6);

            for (var7 = 0; var7 < this.numberOfSingleBytes[var5] - this.getStringCharacterLength(var6); ++var7) {
                var4.append(" ");
            }
        }

        var4.append("\n");

        for (int row1 = 0; row1 < var2; ++row1) {
            if (!TextUtils.isEmpty(var3[row1])) {
                var4.append(this.printRow(var3));
                break;
            }
        }

        return var4.toString();
    }

    @SuppressLint("NewApi")
    private Pair<String, String> getSubString(String str, int width) {
        int var5 = 0;
        int var3 = 0;

        for (int var4 = 0; var4 < str.length(); ++var4) {
            if (str.charAt(var4) > 256) {
                var5 += 2;
            } else {
                ++var5;
            }

            if (var5 >= width) {
                String var6 = str.substring(0, var3);
                return new Pair(var6, str.substring(var3));
            }
            ++var3;
        }

        return new Pair(str, "");
    }

    private int getStringCharacterLength(String str) {
        int var3 = 0;

        for (int var2 = 0; var2 < str.length(); ++var2) {
            if (str.charAt(var2) > 256) {
                var3 += 2;
            } else {
                ++var3;
            }
        }

        return var3;
    }

    public PTable addRow(String... row) {
        if (row.length == this.numberOfSingleBytes.length) {
            this.rows.add(row);
            return this;
        } else {
            throw new IllegalArgumentException("row.length != numberOfSingleBytesPerCol.length");
        }
    }

    public String getTableText() {
        StringBuilder var1 = new StringBuilder();
        Iterator var2 = this.rows.iterator();
        while (var2.hasNext()) {
            var1.append(this.printRow((String[]) var2.next()));
        }
        return var1.toString();
    }
}
