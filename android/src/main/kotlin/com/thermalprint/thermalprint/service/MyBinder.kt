package com.thermalprint.thermalprint.service

import com.thermalprint.thermalprint.posprinterface.IMyBinder
import com.thermalprint.thermalprint.posprinterface.TaskCallback
import android.content.Context
import android.graphics.BitmapFactory
import com.google.gson.Gson
import com.thermalprint.thermalprint.CustomPrinter
import com.thermalprint.thermalprint.MyBindPrinters
import com.thermalprint.thermalprint.Tag
import net.posprinter.*

import io.flutter.Log

class MyBinder : IMyBinder {

    companion object {
        const val TAG: String = "myBinder"
    }

    private val printers: ArrayList<MyBindPrinters> = ArrayList();

    private val deviceTypes = arrayOf(
        /// USB打印机
        POSConnect.DEVICE_TYPE_USB,
        /// 网络打印机
        POSConnect.DEVICE_TYPE_ETHERNET,
        /// 蓝牙打印机
        POSConnect.DEVICE_TYPE_BLUETOOTH,
        /// 串口打印机
        POSConnect.DEVICE_TYPE_SERIAL
    )

    private var connect: IDeviceConnection? = null

    override fun GetConnect(): IDeviceConnection? {
        return connect;
    }

    /// 初始化
    override fun init(context: Context) {
        /// 初始化打印机
        POSConnect.init(context)
    }

    override fun getCustomPrinter(printMapString: String): CustomPrinter {
        return Gson().fromJson(printMapString, CustomPrinter::class.java);
    }

    override fun findPrinterKey(customPrinter: CustomPrinter): String {
        val key: String = when (customPrinter.printer_type) {
            0 -> customPrinter.printer_config.device_name
            1 -> customPrinter.printer_config.ip
            else -> ""
        }
        return key;
    }

    override fun findPrinterIndex(customPrinter: CustomPrinter): Int {
        val key = findPrinterKey(customPrinter)
        var index = -1 // 初始化为-1表示未找到
        // 查找目标元素在数组中的位置
        for ((i, printer) in printers.withIndex()) {
            if (printer.key == key && printer.printer_type == customPrinter.printer_type) {
                index = i
                break
            }
        }
        return index
    }

    override fun findPrinter(customPrinter: CustomPrinter): MyBindPrinters? {
        var key = findPrinterKey(customPrinter)
        var printer: MyBindPrinters? = printers.find<MyBindPrinters> {
            it.key == key
                    && it.printer_type == customPrinter.printer_type
        }
        return printer;
    }

    override fun findOrCreatePrinter(customPrinter: CustomPrinter): MyBindPrinters {
        val key = findPrinterKey(customPrinter)
        var printer = findPrinter(customPrinter)

        if (printer == null) {
            val connection = deviceTypes[customPrinter.printer_type]
            printer = MyBindPrinters(
                key = key,
                printer_type = customPrinter.printer_type,
                isEnabled = false,
                connection = POSConnect.createDevice(connection)
            );
            printers.add(
                printer
            )
        }

        return printer;
    }

    override fun Connect(customPrinter: CustomPrinter, taskCallback: TaskCallback?) {
        try {
//            android.util.Log.e("Connect", "Connect----2");
            if (findPrinterKey(customPrinter) == "") {
//                android.util.Log.e("Connect", "Connect----2.find-1");
                taskCallback?.OnFailed()
                return;
            }
//            android.util.Log.e("Connect", "Connect----2.1");
            val printer = findOrCreatePrinter(customPrinter)

//            android.util.Log.e("Connect", "Connect----3");
            if (!printer.isEnabled) {
//                android.util.Log.e("Connect", "Connect----3.2");
                /// 连接
                printer.connection.connect(
                    printer.key,
                ) { code, connectInfo, msg ->
//                    android.util.Log.i(TAG, "code:=$code")
//                    android.util.Log.e("Connect", "Connect----4");
                    if (code == POSConnect.CONNECT_SUCCESS) {
                        /// 修改状态
                        printer.isEnabled = true;
                        taskCallback?.OnSucceed()
                    } else {
                        taskCallback?.OnFailed()
                    }
                }
            } else {
                taskCallback?.OnSucceed()
            }


        } catch (err: Error) {
//            android.util.Log.i(TAG, "open-error")
            taskCallback?.OnFailed()
        }


//        if (connect != null) {
//            return Disconnect(
//                object : TaskCallback {
//                    override fun OnSucceed() {
//                        Connect(customPrinter, taskCallback);
//                    }
//
//                    override fun OnFailed() {
//                        taskCallback?.OnFailed()
//                    }
//                }
//            )
//        }
//        try {
//            if (customPrinter.printer_type == 0) {
//                val usbAddress: String = customPrinter.printer_config.address;
//                android.util.Log.d(TAG, "usbAddress:$usbAddress")
//                ConnectUsbPort(usbAddress, taskCallback);
//            } else {
//                val ip: String = customPrinter.printer_config.ip;
//                val port: Int = customPrinter.printer_config.port;
//                android.util.Log.d(TAG, "ip:$ip,port:$port")
//                ConnectNetPort(ip, port, taskCallback);
//            }
//        } catch (err: Error) {
//            taskCallback?.OnFailed()
//        }
    }

    override fun Connect(printMapString: String, taskCallback: TaskCallback?) {
        try {
            android.util.Log.e("Connect", "Connect----1");
            Connect(getCustomPrinter(printMapString), taskCallback);
        } catch (err: Error) {

            taskCallback?.OnFailed()
        }
    }


    override fun ConnectNetPort(ip: String, port: Int, taskCallback: TaskCallback?) {

        android.util.Log.i(TAG, "ConnectNetPort$ip")
        //创建网络打印机
        connect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_ETHERNET)

        try {
            connect!!.connect(
                ip,
            ) { code, connectInfo, msg ->
                android.util.Log.i(TAG, "code:=$code")
                if (code == POSConnect.CONNECT_SUCCESS) {
                    taskCallback?.OnSucceed()
                } else {
                    taskCallback?.OnFailed()
                    connect = null
                }
            }
        } catch (e: Error) {
            android.util.Log.e(TAG, "address is '${e.message}'")
            taskCallback?.OnFailed()
        }
    }

    override fun ConnectUsbPort(address: String?, taskCallback: TaskCallback?) {

        /// 连接打印机为空
        if (address == "") {
            Log.e(TAG, "address is ''")
            taskCallback?.OnFailed()
            return
        }

        //创建usb打印机
        connect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_USB)

        try {
            connect!!.connect(
                address,
            ) { code, connectInfo, msg ->
                Log.i(TAG, "connectInfo:$connectInfo")
                Log.i(TAG, ("msg:$msg"))
                if (code == POSConnect.CONNECT_SUCCESS) {
                    taskCallback?.OnSucceed()
                } else {
                    taskCallback?.OnFailed()
                    connect = null
                }
            }
        } catch (e: Error) {
            Log.e(TAG, "address is '${e.message}'")
            taskCallback?.OnFailed()
        }
    }

    /// 断开连接
    override fun Disconnect(customPrinter: CustomPrinter, taskCallback: TaskCallback?) {
        if (findPrinterKey(customPrinter) != "") {
            taskCallback?.OnFailed()
            return;
        }

        val index = findPrinterIndex(customPrinter)

        if (index == -1) {
            taskCallback?.OnSucceed();
        } else {
            val printer = printers.get(index)
            /// 断开连接
            printer.connection.close();
            printer.isEnabled = false;
            printers.removeAt(index)
            taskCallback?.OnSucceed();
        }
    }

    override fun Disconnect(printMapString: String, taskCallback: TaskCallback?) {
        try {
            Disconnect(getCustomPrinter(printMapString), taskCallback);
        } catch (err: Error) {
            taskCallback?.OnFailed()
        }
    }


    override fun Disconnect(
        taskCallback: TaskCallback?,
    ) {
        try {
            connect?.closeSync()
            taskCallback?.OnSucceed()
            connect = null
        } catch (err: Error) {
            taskCallback?.OnFailed()
            connect = null
        }
    }

    override fun writeImage(
        customPrinter: CustomPrinter,
        bitMap: ByteArray,
        taskCallback: TaskCallback?,
    ) {
        val printer = findPrinter(customPrinter)


        if (printer == null) {
            /// 尝试打印
            Connect(
                customPrinter,
                object : TaskCallback {
                    override fun OnSucceed() {
                        writeImage(customPrinter, bitMap, taskCallback)
                    }

                    override fun OnFailed() {
                        taskCallback?.OnFailed()
                    }
                }
            )
        } else {
            /// 使用 384 / 58  作为换算比例
            val width: Int = ((customPrinter.width - 8) * 7.68).toInt();
            val connection = printer.connection;
            Log.e(Tag, "${connect != null}");

//            val img = call.argument<ByteArray>("bitmap")!!;

//            android.util.Log.e("writeImage", "writeImage-2");

            val bmp = BitmapFactory.decodeByteArray(bitMap, 0, bitMap.size)
            val posPrinter = POSPrinter(connection);
            posPrinter.selectBitmapModel(
                POSConst.DOUBLE_DENSITY_24, width, bmp,
            )
                .feedLine(
                    0,
                )

            taskCallback?.OnSucceed();
        }
    }

    override fun writeImage(
        printMapString: String,
        bitMap: ByteArray,
        taskCallback: TaskCallback?,
    ) {
        try {
            writeImage(getCustomPrinter(printMapString), bitMap, taskCallback);
        } catch (err: Error) {
            taskCallback?.OnFailed()
        }
    }

    override fun ClearBuffer() {
        TODO("Not yet implemented")
    }

    override fun CheckLinkedState(var1: TaskCallback?) {
        TODO("Not yet implemented")
    }

    override fun DisconnetNetPort(var1: TaskCallback?) {
        TODO("Not yet implemented")
    }

    override fun destruction() {
        /// 关闭所有连接
        printers.forEach {
            it.connection.close()
        }
        /// 清空栈
        printers.clear()
    }

}