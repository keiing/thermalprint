package com.thermalprint.thermalprint_example

import android.app.Activity
import android.util.Log
import android.view.Window
import android.view.WindowManager
import io.flutter.embedding.android.FlutterActivity
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.FlutterPlugin.FlutterPluginBinding
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding


const val Tag = "com.demo.printPlugin"

class MainActivity: FlutterActivity() {
    override fun getWindow(): Window {
        return super.getWindow()
    }
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        Log.e(Tag, "onWindowFocusChanged");
            Log.e(Tag, "hasFocus");
            Log.e(Tag, "imm");
            Log.e(Tag, "imm222");
//        val flags = WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
//        window.addFlags(flags)
        window.setSoftInputMode(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
    }

    override fun onResume() {
        super.onResume()
        Log.e(Tag, "onResume");
        this.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Log.e(Tag, "onResume2");
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Log.e(Tag, "onResume2");

        val flags = WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM

        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)

        var flutterEngine = FlutterEngineCache.getInstance()

    }
}


class YourPlugin1 : FlutterPlugin, ActivityAware {
    private var activity: Activity? = null
    override fun onAttachedToEngine(flutterPluginBinding: FlutterPluginBinding) {
        // 注册插件到其他组件...
    }

    override fun onDetachedFromEngine(binding: FlutterPluginBinding) {
        TODO("Not yet implemented")
    }

    override fun onAttachedToActivity(activityPluginBinding: ActivityPluginBinding) {
        activity = activityPluginBinding.activity
        // 现在你可以使用 activity 变量访问当前界面的 FlutterActivity
    }

    override fun onDetachedFromActivityForConfigChanges() {
        // 处理配置变化时的逻辑...
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        // 重新附加到 Activity 后的逻辑...
    }

    override fun onDetachedFromActivity() {
        activity = null
        // 处理 Activity 分离的逻辑...
    }
}
