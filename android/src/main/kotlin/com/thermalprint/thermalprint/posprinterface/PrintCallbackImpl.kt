package com.thermalprint.thermalprint.posprinterface

interface PrintCallback {
    /// 快速向数组添加数据
    fun printList(list: MutableList<ByteArray>) {

    }

    /// 打印成功
    fun success() {}

    /// 打印失败
    fun failed() {}
}