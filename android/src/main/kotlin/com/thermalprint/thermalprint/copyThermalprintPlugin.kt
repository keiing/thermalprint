//package com.thermalprint.thermalprint
//
//
//import android.content.ComponentName
//import android.content.Context
//import android.content.Intent
//import android.content.ServiceConnection
//import android.os.Handler
//import android.os.IBinder
//import android.os.Looper
//import android.util.Log
//import com.google.gson.Gson
//import io.flutter.embedding.engine.plugins.FlutterPlugin
//import io.flutter.plugin.common.MethodCall
//import io.flutter.plugin.common.MethodChannel
//import io.flutter.plugin.common.MethodChannel.MethodCallHandler
//import io.flutter.plugin.common.MethodChannel.Result
//
////import net.posprinter.*
////import net.posprinter.model.PTable
//import net.posprinter.com.thermalprint.thermalprint.posprinterface.IMyBinder
//import net.posprinter.com.thermalprint.thermalprint.posprinterface.ProcessData
//import net.posprinter.com.thermalprint.thermalprint.posprinterface.TaskCallback
//import net.posprinter.service.PosprinterService
//import net.posprinter.utils.DataForSendToPrinterPos58
//import net.posprinter.utils.DataForSendToPrinterPos80
//import net.posprinter.utils.StringUtils
//import java.nio.file.attribute.PosixFileAttributeView
//
//import java.util.*
//
//
//interface PrintCallback {
//    /// 快速向数组添加数据
//    fun printList(list: MutableList<ByteArray>) {
//
//    }
//
//    /// 打印成功
//    fun success() {}
//
//    /// 打印失败
//    fun failed() {}
//}
//
//const val Tag: String = "com.thermalprint.printPlugin"
//
///** ThermalprintPlugin */
//class ThermalprintPlugin : FlutterPlugin, MethodCallHandler {
//
//    companion object {
//        const val CHANNEL: String = Tag
//
//    }
//
//    private lateinit var channel: MethodChannel
//
//    /// 自定义属性
//    private var context: Context? = null
//
//
//    var myBinder: IMyBinder? = null
//
//    var mSerconnection: ServiceConnection = object : ServiceConnection {
//        override fun onServiceConnected(name: ComponentName, service: IBinder) {
//            Log.e(Tag, "myBinder");
//            myBinder = service as IMyBinder
//        }
//
//        override fun onServiceDisconnected(name: ComponentName) {
//            Log.e(Tag, "onServiceDisconnected 服务关闭");
//        }
//    }
//
//    var ISCONNECT = false
//    var ISCREATE = false;
//    private fun onCreate() {
//        if (ISCREATE) {
//            return;
//        }
//        ISCREATE = true;
//
//
//        /// 初始化打印机
////        POSConnect.init(context)
////        bind service，get imyBinder
//        val intent: Intent = Intent(context, PosprinterService::class.java);
//        context!!.bindService(intent, mSerconnection, Context.BIND_AUTO_CREATE);
//
//    }
//
//
//    /**
//     * 连接usb
//     */
//    private fun connectUSB(usbAddress: String, taskCallback: TaskCallback) {
//        if (usbAddress == "") {
//            taskCallback.OnFailed();
//        } else {
//            if (myBinder == null) {
//                Log.e(Tag, "myBinder is null")
//                taskCallback.OnFailed();
//            } else {
//                myBinder!!.ConnectUsbPort(context!!, usbAddress, taskCallback);
//            }
//        }
//    }
//
//    /**
//     * 连接Ip
//     * @param address ip地址
//     * @param port 端口 默认 9100
//     */
//    private fun connectIp(address: String, port: Int, taskCallback: TaskCallback) {
//        if (address == "") {
//            taskCallback.OnFailed();
//        } else {
//            if (myBinder == null) {
//                taskCallback.OnFailed();
//            } else {
//                myBinder!!.ConnectNetPort(address, port, taskCallback)
//            }
//        }
//    }
//
//    /**
//     * 断开连接
//     */
//    private fun disConnect() {
//        if (ISCONNECT) {
//            myBinder!!.DisconnectCurrentPort(object : TaskCallback {
//                override fun OnSucceed() {
//                    ISCONNECT = false
//                    Log.e(Tag, "disConnect success");
//                }
//
//                override fun OnFailed() {
//                    ISCONNECT = true
//                    Log.e(Tag, "disConnect fail");
//                }
//            })
//        }
//    }
//
//    private fun print(result: Result, callback: PrintCallback, isDisConnect: Boolean = true) {
//        myBinder!!.ClearBuffer();
//        myBinder!!.writeDataByUSB(object : TaskCallback {
//            override fun OnSucceed() {
//                callback.success()
//                result.success(1);
//                if (isDisConnect) {
//                    disConnect();
//                }
//            }
//
//            override fun OnFailed() {
//                callback.failed()
//                result.success(-1);
//                if (isDisConnect) {
//                    disConnect();
//                }
//            }
//        }, object : ProcessData {
//            override fun processDataBeforeSend(): MutableList<ByteArray> {
//                val list: MutableList<ByteArray> = ArrayList()
//                callback.printList(list);
//                return list
//            }
//        });
//    }
//
//    override fun onMethodCall(call: MethodCall, result: Result) {
//        Log.e(Tag, call.method);
//        /// 获取版本号
//        if (call.method == "getPlatformVersion") {
//            result.success("Android ${android.os.Build.VERSION.RELEASE}")
//        }
//
//        /// 获取usb列表
//        else if (call.method == "getList") {
////            //  获取USB列表
////            val getUsbPathNames = PosPrinterDev.GetUsbPathNames(context);
////            // List<String>
////            Log.e(Tag, getUsbPathNames.toString())
////            result.success(getUsbPathNames)
//            result.success("1")
//        }
//        /// 打开连接
//        else if (call.method == "open") {
//
//            // 获取 Flutter 传递的 Map
//            val printer_map: String = call.argument<String?>("printer") ?: "";
//
//
//            val customPrinter: CustomPrinter = Gson().fromJson(printer_map, CustomPrinter::class.java)
//
//            Log.d(Tag, "customPrinter:" + customPrinter.toString())
//            /// 默认为usb
////            val printer_type: String = call.argument<String?>("printer_type") ?: "0";
////
////
//            if (customPrinter.printer_type == 0) {
//                val usbAddress: String = customPrinter.printer_config.address;
//
//                Log.d(Tag, "usbAddress:" + usbAddress.toString())
//
//                connectUSB(usbAddress, object : TaskCallback {
//                    override fun OnSucceed() {
//                        ISCONNECT = true
//                        result.success(1);
//                    }
//
//                    override fun OnFailed() {
//                        ISCONNECT = false
//                        result.success(-1);
//                    }
//                });
//            } else if (customPrinter.printer_type == 1) {
//                val ip: String = customPrinter.printer_config.ip;
//                val port: Int = customPrinter.printer_config.port;
//
//                connectIp(
//                    ip, port,
//                    object : TaskCallback {
//                        override fun OnSucceed() {
//                            ISCONNECT = true
//                            result.success(1);
//                        }
//
//                        override fun OnFailed() {
//                            ISCONNECT = false
//                            result.success(-1);
//                        }
//                    },
//                );
//            } else {
//                result.success(-1);
//            }
//        }
//
//        /// 打印测试
//        else if (call.method == "printTest") {
//
//            val printer_map: String = call.argument<String?>("printer") ?: "";
//            val customPrinter: CustomPrinter = Gson().fromJson(printer_map, CustomPrinter::class.java)
////            val width: Int = call.argument<Int?>("width") ?: 58;
//
//            val width:Int = customPrinter.width;
//
//            print(
//                result,
//                object : PrintCallback {
//                    override fun printList(list: MutableList<ByteArray>) {
//                        super.printList(list);
//                        if (width == 58) {
//                            list.add(DataForSendToPrinterPos58.initializePrinter())
//                            list.add(StringUtils.strTobytes("打印测试"))
//                            list.add(DataForSendToPrinterPos58.printAndFeedLine())
//                        } else {
//                            /// 80
//                            list.add(DataForSendToPrinterPos80.initializePrinter())
//                            list.add(StringUtils.strTobytes("打印测试"))
//                            list.add(DataForSendToPrinterPos80.printAndFeedLine())
//                        }
//                    }
//                },
//            );
//        }
//        /// 打印列表
//        else if(call.method == "printerList") {
//            val customPrinter: CustomPrinter = Gson().fromJson(call.argument<String?>("printer") ?: "", CustomPrinter::class.java)
//            Log.d(Tag, "customPrinter:" + customPrinter.toString())
//
//
//            val width:Int = customPrinter.width;
//
//
//            print(
//                result,
//                object : PrintCallback {
//                    override fun printList(list: MutableList<ByteArray>) {
//                        super.printList(list);
//                        if (width == 58) {
//
//                            list.add(DataForSendToPrinterPos58.initializePrinter())
//                            list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"))
//                            list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                            /// 打印表单
//                            list.add(DataForSendToPrinterPos58.initializePrinter())
//                            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
//                            list.add(StringUtils.strTobytes("黄焖鸡234567890qwertyuiopakjbdscm nkjdv mcdskjb12323"))
//                            list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
//                            list.add(StringUtils.strTobytes("5元"))
//                            list.add(DataForSendToPrinterPos58.printAndFeedLine())
//                        } else {
//                            /// 80
//                            list.add(DataForSendToPrinterPos80.initializePrinter())
//                            list.add(StringUtils.strTobytes("打印测试"))
//                            list.add(DataForSendToPrinterPos80.printAndFeedLine())
//                        }
//                    }
//                },
//            );
//
//        }
//        else if (call.method == "write") {
//            if (ISCONNECT) {
////                myBinder!!.ClearBuffer();
////                val list: MutableList<ByteArray> = ArrayList()
////                list.add(DataForSendToPrinterPos58.initializePrinter())
////                list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"))
////                list.add(DataForSendToPrinterPos58.printAndFeedLine())
////                myBinder!!.Write(DataForSendToPrinterPos58.initializePrinter(),object : TaskCallback {
////                    override fun OnSucceed() {
////                    }
////
////                    override fun OnFailed() {
////                    }
////                })
////                myBinder!!.Write(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"),object : TaskCallback {
////                    override fun OnSucceed() {
////                    }
////
////                    override fun OnFailed() {
////                    }
////                })
////                myBinder!!.Write(DataForSendToPrinterPos58.printAndFeedLine(),object : TaskCallback {
////                    override fun OnSucceed() {
////                    }
////
////                    override fun OnFailed() {
////                    }
////                })
////                result.success(1);
//
//                print(result, object : PrintCallback {
//                    override fun printList(list: MutableList<ByteArray>) {
//                        super.printList(list)
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(StringUtils.strTobytes("1234567890qwertyuiopakjbdscm nkjdv mcdskjb"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
//                        list.add(StringUtils.strTobytes("黄焖鸡234567890qwertyuiopakjbdscm nkjdv mcdskjb12323"))
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
//                        list.add(StringUtils.strTobytes("5元"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
//                        list.add(StringUtils.strTobytes("黄焖鸡呀"))
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
//                        list.add(StringUtils.strTobytes("6元"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
//                        list.add(StringUtils.strTobytes("黄焖鸡"))
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
//                        list.add(StringUtils.strTobytes("7元"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
//                        list.add(StringUtils.strTobytes("黄焖鸡"))
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
//                        list.add(StringUtils.strTobytes("8元"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
//                        list.add(StringUtils.strTobytes("黄焖鸡"))
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
//                        list.add(StringUtils.strTobytes("9元"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//
//                        list.add(DataForSendToPrinterPos58.initializePrinter())
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(30, 0))
//                        list.add(StringUtils.strTobytes("黄焖鸡"))
//                        list.add(DataForSendToPrinterPos58.setAbsolutePrintPosition(220, 0))
//                        list.add(StringUtils.strTobytes("10元"))
//                        list.add(DataForSendToPrinterPos58.printAndFeedLine())
//                    }
//                });
//                return;
//            } else {
//                result.success(-2);
//            }
//        } else if (call.method == "close") {
//            disConnect();
//            result.success(0);
//        } else {
//            result.notImplemented()
//        }
//    }
//
////    override fun onMethodCall(call: MethodCall, result: Result) {
////        Log.e(Tag, call.method);
////
////        /// 创建usb打印机
////        val connect = POSConnect.createDevice(POSConnect.DEVICE_TYPE_USB)
////
////        val printer_map: String = call.argument<String?>("printer") ?: "";
////
////
////        val customPrinter: CustomPrinter = Gson().fromJson(printer_map, CustomPrinter::class.java)
////
////        Log.d(Tag, "customPrinter:" + customPrinter.toString())
////        /// 默认为usb
////        val usbAddress: String = customPrinter.printer_config.address;
////
////        Log.d(Tag, "usbAddress:" + usbAddress.toString())
////
////
////        connect.connectSync(usbAddress, object : IConnectListener {
////            override fun onStatus(code: Int, connectInfo: String?, msg: String?) {
//////                val printer = POSPrinter(connect);
//////                printer.printString("1234567890qwertyuiopakjbdscm");
////
////
//////                val handler = Handler(Looper.getMainLooper())
//////
//////                handler.postDelayed({
//////                    // 在这里定义延迟任务执行的操作
//////                    connect.close();
//////
//////                }, 1000L)
////            }
////        });
////        val printer = POSPrinter(connect);
////        printer.initializePrinter();
////        printer.feedLine()
////        printer.setCharSet("gbk")
////
////
////        printer.setPrintArea(384,100)
////
////        printer.setAlignment(POSConst.ALIGNMENT_CENTER)
////
////        /// 打印标题
////        printer.printText(
////            "结账单\n",POSConst.ALIGNMENT_CENTER, POSConst.FNT_BOLD,POSConst.TXT_2WIDTH or POSConst.TXT_2HEIGHT,
////        );
////
//////        printer.feedLine()
////
////        /// 打印表单
//////        val w = (58 / 1.5).toInt();
//////        /// 创建表单
//////        printer.printTable(
//////            PTable(
//////                arrayOf("", "", ""),
//////                arrayOf((w / 2).toInt(), (w / 4).toInt(), (w / 4).toInt())
//////            )
//////                .addRow("品名/单价", "数量", "小计")
//////                .addRow(
//////                    "品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价",
//////                    "1",
//////                    "￥24"
//////                )
//////                .addRow("239232848242477", "1", "￥24"),
//////        );
////        printer.feedLine(1)
////
////        printer.feedLine()
////        printer.printBarCode(
////            "1234245",
////            POSConst.BCS_Code128
////        )
////        printer.printTextAlignment(
////            "-----------------------------------------------------------", POSConst.ALIGNMENT_CENTER,
////        );
////
////        /// 切纸
////        printer.cutPaper(POSConst.CUT_ALL)
////
////        result.success("1")
////
////        connect.close();
////
////    }
//
//    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
//        context = flutterPluginBinding.getApplicationContext();
//        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "thermalprint")
//
//        channel.setMethodCallHandler(this)
//        onCreate();
//        Log.e("print", "onAttachedToEngine");
//    }
//
//    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
//        context = null;
//        channel.setMethodCallHandler(null)
//    }
//}
//
