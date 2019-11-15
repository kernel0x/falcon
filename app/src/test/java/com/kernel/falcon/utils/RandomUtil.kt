package com.kernel.falcon.utils

import java.util.Date
import java.util.Random

object RandomUtil {

    private val random = Random(Date().time)

    fun generateString(): String {
        val values = charArrayOf(
            'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
            'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '0', '1', '2', '3',
            '4', '5', '6', '7', '8', '9'
        )

        var out = ""

        for (i in 0..99) {
            val idx = random.nextInt(values.size)
            out += values[idx]
        }
        return out
    }

} 