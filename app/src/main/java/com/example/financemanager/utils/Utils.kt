package com.example.financemanager

import java.text.DecimalFormat
import kotlin.math.absoluteValue

fun Double.toAmountFormat(): String {
    return DecimalFormat(if (this >= 0) "###,###.##" else "–###,###.##").format(this.absoluteValue)
}