package com.thermalprint.thermalprint

import net.posprinter.IDeviceConnection

data class CustomPrinterConfig(
    val device_name: String, val ip: String, val port: Int,
)

data class CustomPrinter(
    /// 打印机名
    val printer_name: String,
    /// 打印机类型
    val printer_type: Int,
    /// 打印机连接配置
    val printer_config: CustomPrinterConfig,
    /// 打印机种类
    val printer_category: Int,
    val width: Int,
)

class MyBindPrinters(
    /// 打印机字段
    val key: String,
    /// 是否启用表示是否已经连接
    var isEnabled: Boolean,
    /// 打印机类型
    val printer_type: Int,
    /// 连接打印机
    val connection: IDeviceConnection,
)