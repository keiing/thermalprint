package com.thermalprint.thermalprint


import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.graphics.BitmapFactory
import android.os.IBinder
import android.util.Log
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import net.posprinter.posprinterface.IMyBinder
import net.posprinter.posprinterface.ProcessData
import net.posprinter.posprinterface.TaskCallback
import net.posprinter.service.PosprinterService
import net.posprinter.utils.*
import net.posprinter.utils.PosPrinterDev.PortInfo
import java.util.*


const val Tag = "com.thermalprint.printPlugin"


interface PrintCallback {
    /// 快速向数组添加数据
    fun printList(list: MutableList<ByteArray>) {

    }

    /// 打印成功
    fun success() {}

    /// 打印失败
    fun failed() {}
}

/** ThermalprintPlugin */
class ThermalprintPlugin : FlutterPlugin, MethodCallHandler {

    companion object {
        const val CHANNEL: String = Tag
    }

    private lateinit var channel: MethodChannel

    /// 自定义属性
    private var context: Context? = null


    var myBinder: IMyBinder? = null

    var mSerconnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e(Tag, "myBinder");
            myBinder = service as IMyBinder
        }

        override fun onServiceDisconnected(name: ComponentName) {
            Log.e(Tag, "onServiceDisconnected 服务关闭");
        }
    }

    var ISCONNECT = false
    var ISCREATE = false;
    private fun onCreate() {
        if (ISCREATE) {
            return;
        }
        ISCREATE = true;
        //bind service，get imyBinder
        val intent: Intent = Intent(context, PosprinterService::class.java);
        context!!.bindService(intent, mSerconnection, Context.BIND_AUTO_CREATE);
    }


    /**
     * 连接usb
     */
    private fun connectUSB(usbAddress: String, taskCallback: TaskCallback) {
        if (usbAddress == "") {
            taskCallback.OnFailed();
        } else {
            if (myBinder == null) {
                Log.e(Tag, "myBinder is null")
                taskCallback.OnFailed();
            } else {
                myBinder!!.ConnectUsbPort(context!!, usbAddress, taskCallback);
            }
        }
    }

    /**
     * 连接Ip
     * @param address ip地址
     * @param port 端口 默认 9100
     */
    private fun connectIp(address: String, port: Int, taskCallback: TaskCallback) {
        if (address == "") {
            taskCallback.OnFailed();
        } else {
            if (myBinder == null) {
                taskCallback.OnFailed();
            } else {
                myBinder!!.ConnectNetPort(address, port, taskCallback)
            }
        }
    }

    /**
     * 断开连接
     */
    private fun disConnect() {
        if (ISCONNECT) {
            myBinder!!.DisconnectCurrentPort(object : TaskCallback {
                override fun OnSucceed() {
                    ISCONNECT = false
                    Log.e(Tag, "disConnect success");
                }

                override fun OnFailed() {
                    ISCONNECT = true
                    Log.e(Tag, "disConnect fail");
                }
            })
        }
    }

    private fun print(result: Result, callback: PrintCallback, isDisConnect: Boolean = true) {
        myBinder!!.ClearBuffer();
        myBinder!!.writeDataByUSB(object : TaskCallback {
            override fun OnSucceed() {
                callback.success()
                result.success(1);
                if (isDisConnect) {
                    disConnect();
                }
            }

            override fun OnFailed() {
                callback.failed()
                result.success(-1);
                if (isDisConnect) {
                    disConnect();
                }
            }
        }, object : ProcessData {
            override fun processDataBeforeSend(): MutableList<ByteArray> {
                val list: MutableList<ByteArray> = ArrayList()
                callback.printList(list);
                return list
            }
        });
    }

    override fun onMethodCall(call: MethodCall, result: Result) {
        Log.e(Tag, call.method);
        /// 获取版本号
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        }

        /// 打印测试
        else if (call.method == "printTest") {
            val width: Int = call.argument<Int?>("width") ?: 58;

            print(
                result,
                object : PrintCallback {
                    override fun printList(list: MutableList<ByteArray>) {
                        super.printList(list);
                        if(width == 58){
                            list.add(DataForSendToPrinterPos58.initializePrinter())
                            list.add(StringUtils.strTobytes("打印测试"))
                            list.add(DataForSendToPrinterPos58.printAndFeedLine())
                        } else {
                            /// 80
                            list.add(DataForSendToPrinterPos80.initializePrinter())
                            list.add(StringUtils.strTobytes("打印测试"))
                            list.add(DataForSendToPrinterPos80.printAndFeedLine())
                        }
                    }
                },
            );
        }
        /// 获取usb列表
        else if (call.method == "getList") {
            //  获取USB列表
            val getUsbPathNames = PosPrinterDev.GetUsbPathNames(context);
            // List<String>
            Log.e(Tag, getUsbPathNames.toString())
            result.success(getUsbPathNames)
        }
        /// 打开连接
        else if (call.method == "open") {

//            var usbAddress: String = "/dev/bus/usb/002/007"

            // 连接中
//            if (ISCONNECT) {
//                result.success(1);
//                return
//            }

            /// 默认为usb
            val printer_type: String = call.argument<String?>("printer_type") ?: "0";


            if (printer_type == "0") {
                val usbAddress: String = call.argument<String>("address")!!;

                Log.e(Tag, usbAddress);

                connectUSB(usbAddress, object : TaskCallback {
                    override fun OnSucceed() {
                        ISCONNECT = true
                        result.success(1);
                    }

                    override fun OnFailed() {
                        ISCONNECT = false
                        result.success(-1);
                    }
                });
            } else if (printer_type == "1") {
                val ip: String = call.argument<String>("ip")!!;
                val port: Int = call.argument<Int>("port")!!;
                Log.e(Tag, ip);

                connectIp(ip, port, object : TaskCallback {
                    override fun OnSucceed() {
                        ISCONNECT = true
                        result.success(1);
                    }

                    override fun OnFailed() {
                        ISCONNECT = false
                        result.success(-1);
                    }
                });
            } else {
                result.success(-1);
            }
        } else if (call.method == "write") {
            if (ISCONNECT) {
//                myBinder!!.ClearBuffer();
//                val list: MutableList<ByteArray> = ArrayList()
//                list.add(DataForSendToPrinterPos58.initializePrinter())
//                list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"))
//                list.add(DataForSendToPrinterPos58.printAndFeedLine())
//                myBinder!!.Write(DataForSendToPrinterPos58.initializePrinter(),object : TaskCallback {
//                    override fun OnSucceed() {
//                    }
//
//                    override fun OnFailed() {
//                    }
//                })
//                myBinder!!.Write(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"),object : TaskCallback {
//                    override fun OnSucceed() {
//                    }
//
//                    override fun OnFailed() {
//                    }
//                })
//                myBinder!!.Write(DataForSendToPrinterPos58.printAndFeedLine(),object : TaskCallback {
//                    override fun OnSucceed() {
//                    }
//
//                    override fun OnFailed() {
//                    }
//                })
//                result.success(1);

                print(result, object : PrintCallback {
                    override fun printList(list: MutableList<ByteArray>) {
                        super.printList(list)
                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())

                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())

                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
                        list.add(StringUtils.strTobytes("黄焖鸡"))
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
                        list.add(StringUtils.strTobytes("5元"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())

                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
                        list.add(StringUtils.strTobytes("黄焖鸡呀"))
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
                        list.add(StringUtils.strTobytes("6元"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())

                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
                        list.add(StringUtils.strTobytes("黄焖鸡"))
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
                        list.add(StringUtils.strTobytes("7元"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())

                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
                        list.add(StringUtils.strTobytes("黄焖鸡"))
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
                        list.add(StringUtils.strTobytes("8元"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())

                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
                        list.add(StringUtils.strTobytes("黄焖鸡"))
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
                        list.add(StringUtils.strTobytes("9元"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())

                        list.add(DataForSendToPrinterPos58.initializePrinter())
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
                        list.add(StringUtils.strTobytes("黄焖鸡"))
                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
                        list.add(StringUtils.strTobytes("10元"))
                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
                    }
                });
                return;
            } else {
                result.success(-2);
            }
        } else if (call.method == "close") {
            disConnect();
            result.success(0);
        } else {
            result.notImplemented()
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "thermalprint")
        channel.setMethodCallHandler(this)
        onCreate();
        Log.e("print", "onAttachedToEngine");
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        context = null;
        channel.setMethodCallHandler(null)
    }
}
