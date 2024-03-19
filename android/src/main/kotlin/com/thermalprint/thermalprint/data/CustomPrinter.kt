package com.thermalprint.thermalprint.data

data class CustomPrinter(
    val printer_name: String,
    val printer_type: Int,
    val printer_config: CustomPrinterConfig,
    val printer_category: Int,
    val width:Int,
)