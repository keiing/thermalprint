package com.thermalprint.thermalprint.posprinterface;


import android.content.Context;

import com.thermalprint.thermalprint.CustomPrinter;
import com.thermalprint.thermalprint.MyBindPrinters;

import net.posprinter.IDeviceConnection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMyBinder {


    /// 初始化
    void init(Context context);

    /// 获取connect
    @Nullable
    IDeviceConnection GetConnect();


    @NotNull
    CustomPrinter getCustomPrinter(@NotNull String printMapString);

    @NotNull
    String findPrinterKey(@NotNull CustomPrinter customPrinter);

    /// 查找 MyBindPrinters
    @NotNull
    int findPrinterIndex(@NotNull CustomPrinter customPrinter);

    @Nullable
    MyBindPrinters findPrinter(@NotNull CustomPrinter customPrinter);

    @NotNull
    MyBindPrinters findOrCreatePrinter(@NotNull CustomPrinter customPrinter);


    /// 连接 connect
    void Connect(@NotNull String printMapString, TaskCallback taskCallback);

    void Connect(@NotNull CustomPrinter customPrinter, TaskCallback taskCallback);


    /// 连接网络打印机
    void ConnectNetPort(@NotNull String var1, @NotNull int var2, TaskCallback var3);

    /// 连接usb打印机
    void ConnectUsbPort(String var2, TaskCallback var3);

    /// 断开连接
    void Disconnect(@NotNull String printMapString, @Nullable TaskCallback taskCallback);

    void Disconnect(@NotNull CustomPrinter customPrinter, @Nullable TaskCallback taskCallback);

    void Disconnect(@Nullable TaskCallback var1);

    void writeImage(@NotNull String printMapString, @NotNull byte[] bitMap, @Nullable TaskCallback taskCallback);

    void writeImage(@NotNull CustomPrinter customPrinter, @NotNull byte[] bitMap, @Nullable TaskCallback taskCallback);

    /// 清空栈
    void ClearBuffer();

    /// 获取状态
    void CheckLinkedState(TaskCallback var1);

    /// 断开网络连接
    void DisconnetNetPort(TaskCallback var1);

    /// 销毁
    void destruction();
}
