package org.saudigitus.entry_points.utils

import com.journeyapps.barcodescanner.ScanOptions

object Utils {
    fun scanOptions(): ScanOptions {
        val options = ScanOptions()
        options.setDesiredBarcodeFormats(ScanOptions.ALL_CODE_TYPES)
        options.setBeepEnabled(true)

        return options
    }
}