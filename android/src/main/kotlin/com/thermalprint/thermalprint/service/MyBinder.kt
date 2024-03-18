package com.thermalprint.thermalprint.service

import com.thermalprint.thermalprint.posprinterface.IMyBinder
import com.thermalprint.thermalprint.posprinterface.TaskCallback
import android.content.Context
import com.google.gson.Gson
import com.thermalprint.thermalprint.CustomPrinter
import com.thermalprint.thermalprint.Tag
import net.posprinter.*

import io.flutter.Log

class MyBinder : IMyBinder {

    companion object {
        const val TAG: String = "myBinder"
    }

    private var connect: IDeviceConnection? = null

    override fun GetConnect(): IDeviceConnection? {
        return connect;
    }

    override fun Connect(customPrinter: CustomPrinter, taskCallback: TaskCallback?) {

        if(connect != null ){
            return Disconnect(
                object : TaskCallback {
                    override fun OnSucceed() {
                        Connect(customPrinter,taskCallback);
                    }

                    override fun OnFailed() {
                        taskCallback?.OnFailed()
                    }
                }
            )
        }

        try {
            if (customPrinter.printer_type == 0) {
                val usbAddress: String = customPrinter.printer_config.address;
                android.util.Log.d(TAG, "usbAddress:$usbAddress")
                ConnectUsbPort(usbAddress, taskCallback);
            } else {
                val ip: String = customPrinter.printer_config.ip;
                val port: Int = customPrinter.printer_config.port;
                android.util.Log.d(TAG, "ip:$ip,port:$port")
                ConnectNetPort(ip, port, taskCallback);
            }
        } catch (err: Error) {
            taskCallback?.OnFailed()
        }
    }

    override fun Connect(printMapString: String, taskCallback: TaskCallback?) {
        try {
            val customPrinter: CustomPrinter =
                Gson().fromJson(printMapString, CustomPrinter::class.java);

            Connect(customPrinter, taskCallback);
        } catch (err: Error) {
            taskCallback?.OnFailed()
        }
    }


    override fun init(context: Context) {
        /// 初始化打印机
        POSConnect.init(context)
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
            connect!!.connectSync(
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
    override fun Disconnect(
        taskCallback: TaskCallback?
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

    override fun ClearBuffer() {
        TODO("Not yet implemented")
    }

    override fun CheckLinkedState(var1: TaskCallback?) {
        TODO("Not yet implemented")
    }

    override fun DisconnetNetPort(var1: TaskCallback?) {
        TODO("Not yet implemented")
    }

}