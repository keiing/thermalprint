package com.thermalprint.thermalprint


import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import com.google.gson.Gson
import com.thermalprint.thermalprint.posprinterface.IMyBinder
import com.thermalprint.thermalprint.posprinterface.TaskCallback
import com.thermalprint.thermalprint.service.MyBinder
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import net.posprinter.*
import net.posprinter.model.PTable
import java.util.*


interface PrintCallback {
    /// 快速向数组添加数据
    fun printList(list: MutableList<ByteArray>) {

    }

    /// 打印成功
    fun success() {}

    /// 打印失败
    fun failed() {}
}

const val Tag: String = "com.thermalprint.printPlugin"

/** ThermalprintPlugin */
@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class ThermalprintPlugin : FlutterPlugin, MethodCallHandler,
    Application.ActivityLifecycleCallbacks {

    companion object {
        const val CHANNEL: String = Tag

    }

    private lateinit var channel: MethodChannel

    /// 自定义属性
    private var context: Context? = null

    var myBinder: IMyBinder? = null

    var ISCONNECT = false
    var ISCREATE = false;
    private fun onCreate() {
        if (ISCREATE) {
            return;
        }
        ISCREATE = true;

        /// 初始化
        myBinder = MyBinder()
        myBinder!!.init(context!!);
    }


    @SuppressLint("LongLogTag")
    override fun onMethodCall(call: MethodCall, result: Result) {
        Log.e(Tag, call.method);
        /// 获取版本号
        if (call.method == "getPlatformVersion") {
            result.success("Android ${android.os.Build.VERSION.RELEASE}")
        }

        /// 获取usb列表
        else if (call.method == "getList") {
            result.success("1")
        }
        /// 打开连接
        else if (call.method == "open") {
            // 获取 Flutter 传递的 Map
            myBinder!!.Connect(
                call.argument<String?>("printer") ?: "",
                object : TaskCallback {
                    override fun OnSucceed() {
                        result.success(1);
                    }

                    override fun OnFailed() {
                        result.success(-1);
                    }
                },
            );
        }

        /// 打印测试
        else if (call.method == "printTest") {


            val printer = myBinder!!.findPrinter(
                myBinder!!.getCustomPrinter(
                    call.argument<String?>("printer") ?: ""
                )
            )

            if(printer == null){
                result.success(-1);
                return;
            }

            val posPrinter = POSPrinter(printer.connection);

            posPrinter.initializePrinter()
                .printText(
                    "打印测试\n",
                    /// 设置内容
                    POSConst.ALIGNMENT_CENTER,
                    /// 设置粗细
                    POSConst.FNT_BOLD or POSConst.FNT_UNDERLINE,
                    /// 设置宽高
                    POSConst.TXT_1WIDTH or POSConst.TXT_2HEIGHT
                )
                .cutHalfAndFeed(
                    POSConst.CUT_HALF,
                )
            result.success(1);
//            val printer_map: String = call.argument<String?>("printer") ?: "";
//            val customPrinter: CustomPrinter =
//                Gson().fromJson(printer_map, CustomPrinter::class.java)
////            val width: Int = call.argument<Int?>("width") ?: 58;
//
////            val width: Int = customPrinter.width;
//
//            val connect = myBinder!!.GetConnect();
//
//            Log.e(Tag, "${connect != null}");
//
//
//            if (connect == null) {
//                result.success(-1);
//                return
//            }
//
//            connect.setSendCallback {
//                Log.e(Tag, "connect.connectInfo:$it")
//            }
//
//            val connectInfo = connect.connectInfo.toString()
//            Log.e(Tag, "connect.connectInfo:$connectInfo")
//            /// 判断是否为当前连接打印机
//            /// 001/004 | 192.168.0.110
////            connect.setSendCallback()
//            val printer = POSPrinter(connect);
//
//            printer.initializePrinter()
//                .printText(
//                    "打印测试\n",
//                    /// 设置内容
//                    POSConst.ALIGNMENT_CENTER,
//                    /// 设置粗细
//                    POSConst.FNT_BOLD or POSConst.FNT_UNDERLINE,
//                    /// 设置宽高
//                    POSConst.TXT_1WIDTH or POSConst.TXT_2HEIGHT
//                )
//                .cutHalfAndFeed(
//                    POSConst.CUT_HALF,
//                )
//
//            connect.setSendCallback {
//                /// 断开连接
////                myBinder!!.Disconnect(
////                    null,
////                );
//                /// 发送成功
//                result.success(1);
//
//                connect.setSendCallback(
//                    null,
//                )
//            }

        }
        /// 打印列表
        else if (call.method == "printerList") {
            val customPrinter: CustomPrinter =
                Gson().fromJson(call.argument<String?>("printer") ?: "", CustomPrinter::class.java)

            val connect = myBinder!!.GetConnect();

            Log.e(Tag, "${connect != null}");

            if (connect == null) {
                result.success(-1);
                return
            }

            val printer = POSPrinter(connect);

            printer.initializePrinter();
            printer.feedLine()

            printer.getSerialNumber { }
            printer.setCharSet("gbk")


            printer.setPrintArea(384, 100)

            printer.setAlignment(POSConst.ALIGNMENT_CENTER)

            /// 打印标题
            printer.printText(
                "结账单\n",
                POSConst.ALIGNMENT_CENTER,
                POSConst.FNT_BOLD,
                POSConst.TXT_2WIDTH or POSConst.TXT_2HEIGHT,
            );

            /// 打印表单
            val w = (58 / 1.5).toInt();
            /// 创建表单
            printer.printTable(
                PTable(
                    arrayOf("", "", ""),
                    arrayOf((w / 2).toInt(), (w / 4).toInt(), (w / 4).toInt())
                )
                    .addRow("品名/单价", "数量", "小计")
                    .addRow(
                        "品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价品名/单价",
                        "1",
                        "￥24"
                    )
                    .addRow("239232848242477", "1", "￥24"),
            );
            printer.feedLine(1)

            printer.feedLine()
            printer.printBarCode(
                "1234245",
                POSConst.BCS_Code128
            )
            printer.printTextAlignment(
                "-----------------------------------------------------------",
                POSConst.ALIGNMENT_CENTER,
            );

            /// 切纸
            printer.cutPaper(POSConst.CUT_ALL)

            connect.setSendCallback {
                /// 断开连接
//                myBinder!!.Disconnect(
//                    null,
//                );
                /// 发送成功
                result.success(1);

                connect.setSendCallback(
                    null,
                )
            }


        } else if (call.method == "writeImage") {

            try {
                val bitmap = call.argument<ByteArray?>("bitmap");

                if (bitmap == null) {
                    result.success(-1);
                    return;
                }

                myBinder!!.writeImage(
                    call.argument<String?>("printer") ?: "",
                    bitmap,
                    object : TaskCallback {
                        override fun OnSucceed() {
                            result.success(1);
                        }

                        override fun OnFailed() {
                            result.success(-1);
                        }
                    },
                );
            } catch (err: Error) {
                result.success(-1);
            }

//            val width: Int = call.argument<Int?>("width") ?: 384;
//
////            val width: Int = customPrinter.width;
//
//            val connect = myBinder!!.GetConnect();
//
//            Log.e(Tag, "${connect != null}");
//
//
//            if (connect == null) {
//                result.success(-1);
//                return
//            }
//
//            val img = call.argument<ByteArray>("bitmap")!!;
//            val bmp = BitmapFactory.decodeByteArray(img, 0, img.size)
//            val printer = POSPrinter(connect);
//
//            printer.selectBitmapModel(
//                POSConst.DOUBLE_DENSITY_24, width, bmp,
//            )
//                .feedLine(0)
//
//            result.success(1);
        } else if (call.method == "close") {
            /// 断开连接
            myBinder!!.Disconnect(
                call.argument<String?>("printer") ?: "",
                object : TaskCallback {
                    override fun OnSucceed() {
                        result.success(1)
                    }

                    override fun OnFailed() {
                        result.success(0)
                    }
                }

            );
        } else {
            result.notImplemented()
        }
    }


    /// 开始绑定flutter生命周期
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        context = flutterPluginBinding.getApplicationContext();
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "thermalprint")


        channel.setMethodCallHandler(this)
        onCreate();
        Log.e("print", "onAttachedToEngine");


        (context as Application).registerActivityLifecycleCallbacks(
            this,
        )
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        Log.i(TAG, "----onActivityCreated-----")
    }

    override fun onActivityStarted(activity: Activity) {
        Log.i(TAG, "----onActivityStarted-----")
    }

    override fun onActivityResumed(activity: Activity) {
        Log.i(TAG, "----onActivityResumed-----")
    }

    override fun onActivityPaused(activity: Activity) {
        Log.i(TAG, "----onActivityPaused-----")
    }

    override fun onActivityStopped(activity: Activity) {
        Log.i(TAG, "----onActivityStopped-----")
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Log.i(TAG, "----onActivitySaveInstanceState-----")
    }

    /// 销毁 Application 生命周期
    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    override fun onActivityDestroyed(activity: Activity) {
        Log.i(TAG, "----onActivityDestroyed-----")
        (context as Application).unregisterActivityLifecycleCallbacks(
            this
        )
    }


    /// 最终销毁 flutter生命周期
    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        Log.e("print", "onDetachedFromEngine");
        myBinder!!.destruction();
        context = null;

        channel.setMethodCallHandler(null)
    }
}

