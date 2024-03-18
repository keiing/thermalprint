package com.thermalprint.thermalprint.posprinterface;


import android.content.Context;

import com.thermalprint.thermalprint.CustomPrinter;

import net.posprinter.IDeviceConnection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface IMyBinder {


    /// 初始化
    void init(Context context);

    /// 获取connect
    @Nullable
    IDeviceConnection GetConnect();


    /// 连接 connect
    void Connect(@NotNull String printMapString, TaskCallback taskCallback);
    void Connect(@NotNull CustomPrinter printMapString, TaskCallback taskCallback);


    /// 连接网络打印机
    void ConnectNetPort(@NotNull String var1,@NotNull int var2, TaskCallback var3);

    /// 连接usb打印机
    void ConnectUsbPort(String var2, TaskCallback var3);

    /// 断开连接
    void Disconnect(@Nullable TaskCallback var1);

    /// 清空栈
    void ClearBuffer();

    /// 获取状态
    void CheckLinkedState(TaskCallback var1);

    /// 断开网络连接
    void DisconnetNetPort(TaskCallback var1);
}
