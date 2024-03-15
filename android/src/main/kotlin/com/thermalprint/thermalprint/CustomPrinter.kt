package com.thermalprint.thermalprint

data class CustomPrinterConfig(
    val address: String, val ip: String, val port: Int,
)
data class CustomPrinter(
    val printer_name: String,
    val printer_type: Int,
    val printer_config: CustomPrinterConfig,
    val printer_category: Int,
)