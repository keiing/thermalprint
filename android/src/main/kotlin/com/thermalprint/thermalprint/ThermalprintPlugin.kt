package com.thermalprint.thermalprint


import android.annotation.SuppressLint
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.IBinder
import android.util.Log
import com.google.gson.Gson
import com.thermalprint.thermalprint.data.CustomPrinter
import com.thermalprint.thermalprint.model.PTable
import com.thermalprint.thermalprint.posprinterface.PrintCallback
import com.thermalprint.thermalprint.utils.PosPrinter
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
import java.util.*


const val Tag: String = "com.thermalprint.printPlugin"

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
        @SuppressLint("LongLogTag")
        override fun onServiceConnected(name: ComponentName, service: IBinder) {
            Log.e(Tag, "myBinder")
            myBinder = service as IMyBinder
        }

        @SuppressLint("LongLogTag")
        override fun onServiceDisconnected(name: ComponentName) {
            Log.e(Tag, "onServiceDisconnected 服务关闭")
        }
    }

    var ISCONNECT = false
    var ISCREATE = false
    private fun onCreate() {
        if (ISCREATE) {
            return
        }
        ISCREATE = true
        //bind service，get imyBinder
        val intent: Intent = Intent(context, PosprinterService::class.java)
        context!!.bindService(intent, mSerconnection, Context.BIND_AUTO_CREATE)
    }

    /**
     * 连接usb
     */
    @SuppressLint("LongLogTag")
    private fun connectUSB(usbAddress: String, taskCallback: TaskCallback) {
        if (usbAddress == "") {
            taskCallback.OnFailed()
        } else {
            if (myBinder == null) {
                Log.e(Tag, "myBinder is null")
                taskCallback.OnFailed()
            } else {
                myBinder!!.ConnectUsbPort(context!!, usbAddress, taskCallback)
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
            taskCallback.OnFailed()
        } else {
            if (myBinder == null) {
                taskCallback.OnFailed()
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
                @SuppressLint("LongLogTag")
                override fun OnSucceed() {
                    ISCONNECT = false
                    Log.e(Tag, "disConnect success")
                }

                @SuppressLint("LongLogTag")
                override fun OnFailed() {
                    ISCONNECT = true
                    Log.e(Tag, "disConnect fail")
                }
            })
        }
    }

    private fun print(result: Result, callback: PrintCallback, isDisConnect: Boolean = true) {
        myBinder!!.ClearBuffer()
        myBinder!!.writeDataByUSB(object : TaskCallback {
            override fun OnSucceed() {
                callback.success()
                /// 打印结束
                result.success(1)
                if (isDisConnect) {
                    disConnect()
                }
            }

            override fun OnFailed() {
                callback.failed()
                result.success(-1)
                if (isDisConnect) {
                    disConnect()
                }
            }
        }, object : ProcessData {
            override fun processDataBeforeSend(): MutableList<ByteArray> {
                val list: MutableList<ByteArray> = ArrayList()
                callback.printList(list)
//                Log.e(Tag, list.toString())
                return list
            }
        })
    }

    /// 获取 CustomPrinter 对象
    private fun getCustomPrinter(
        printer_map: String?,
    ): CustomPrinter {
        // 获取 Flutter 传递的 Map
        return Gson().fromJson(
            printer_map ?: "", CustomPrinter::class.java,
        )
    }

    /// 开启连接
    private fun connect(customPrinter: CustomPrinter, taskCallback: TaskCallback?) {
        try {
            if (customPrinter.printer_type == 0) {
                val usbAddress: String = customPrinter.printer_config.address
                connectUSB(
                    usbAddress,
                    object : TaskCallback {
                        override fun OnSucceed() {
                            ISCONNECT = true
                            taskCallback?.OnSucceed()
                        }

                        override fun OnFailed() {
                            ISCONNECT = false
                            taskCallback?.OnFailed()
                        }
                    },
                )
            } else if (customPrinter.printer_type == 1) {
                val ip: String = customPrinter.printer_config.ip
                val port: Int = customPrinter.printer_config.port

                connectIp(
                    ip, port,
                    object : TaskCallback {
                        override fun OnSucceed() {
                            ISCONNECT = true
                            taskCallback?.OnSucceed()
                        }

                        override fun OnFailed() {
                            ISCONNECT = false
                            taskCallback?.OnFailed()
                        }
                    },
                )
            } else {
                taskCallback?.OnFailed()
            }
        } catch (err: Error) {
            taskCallback?.OnFailed()
        }
    }

    @SuppressLint("LongLogTag")
    override fun onMethodCall(call: MethodCall, result: Result) {
        Log.e(Tag, call.method)
        /// 获取版本号
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        }
        /// 打开连接
        else if (call.method == "open") {
            connect(getCustomPrinter(
                call.argument<String?>("printer"),
            ), object : TaskCallback {
                override fun OnSucceed() {
                    ISCONNECT = true
                    result.success(1)
                }

                override fun OnFailed() {
                    ISCONNECT = false
                    result.success(-1)
                }
            })
        }

        /// 打印测试
        else if (call.method == "printTest") {
//            val width: Int = call.argument<Int?>("width") ?: 58

            print(
                result,
                object : PrintCallback {
                    override fun printList(list: MutableList<ByteArray>) {
                        super.printList(list)
//                        if (width == 58) {
                            list.add(DataForSendToPrinterPos58.initializePrinter())
                            list.add(StringUtils.strTobytes("打印测试"))
                            list.add(DataForSendToPrinterPos58.printAndFeedLine())
//                        } else {
//                            /// 80
//                            list.add(DataForSendToPrinterPos80.initializePrinter())
//                            list.add(StringUtils.strTobytes("打印测试"))
//                            list.add(DataForSendToPrinterPos80.printAndFeedLine())
//                        }

                        PosPrinter
                            /// 打印
                            .printText(
                                "打印测试\n",
                                /// 设置内容
                                1,
                                /// 设置粗细
                                8,
                                /// 设置宽高
                                55,
                            ).forEach {
                                list.add(
                                    it,
                                )
                            }

                        /// 进行切纸
                        list.add(
                            PosPrinter.cutHalfAndFeed(1)
                        )
                    }
                },
            )
        } else if (call.method == "printList")
        /// 打印列表测试
        {
            print(result, object : PrintCallback {
                override fun printList(list: MutableList<ByteArray>) {
                    super.printList(list)

                    list.add(
                        PosPrinter.initializePrinter()
                    )

                    PosPrinter
                        /// 打印
                        .printText(
                            "printText Demo\nprintText DemoprintText DemoprintText DemoprintText DemoprintText Demo\n",
                            /// 设置内容
                            1,
                            /// 设置粗细
                            0,
                            /// 设置宽高
                            20
                        ).forEach {
                            list.add(
                                it,
                            )
                        }
//                        list.add(
//                            PosPrinter.printString("123234")
//                        )

                    list.add(
                        PosPrinter.feedLine(),
                    )

                    /// 打印表单
                    val w = (58 / 1.5).toInt()

                    /// 创建表单
                    list.add(
                        PosPrinter.printTable(
                            PTable(
                                arrayOf("品名/单价品名", "数量", "小计"),
                                arrayOf((w / 2).toInt(), (w / 4).toInt(), (w / 4).toInt())
                            )
                                .addRow("品名/单价", "数量", "小计")
                                .addRow(
                                    "品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价",
                                    "1",
                                    "￥24"
                                )
                                .addRow("239232848242477", "1", "￥24"),
                        )
                    )

                }
            })
        }
        /// 获取usb列表
        else if (call.method == "getList") {
            //  获取USB列表
            val getUsbPathNames = PosPrinterDev.GetUsbPathNames(context)
            // List<String>
            Log.e(Tag, getUsbPathNames.toString())
            result.success(getUsbPathNames)
        } else if (call.method == "write") {
            if (ISCONNECT) {
//                myBinder!!.ClearBuffer()
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
//                result.success(1)

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
                })
                return
            } else {
                result.success(-2)
            }
        } else if (call.method == "close") {
            disConnect()
            result.success(0)
        } else {
            result.notImplemented()
        }
    }

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext()
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "thermalprint")
        channel.setMethodCallHandler(this)
        onCreate()
        Log.e("print", "onAttachedToEngine")
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        context = null
        channel.setMethodCallHandler(null)
    }
}

