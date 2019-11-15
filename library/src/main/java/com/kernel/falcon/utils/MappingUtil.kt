package com.kernel.falcon.utils

import org.jetbrains.annotations.NotNull
import org.jetbrains.annotations.Nullable

object MappingUtil {

    @NotNull
    fun encode(value: Any): String {
        return Base64Util.encode(ByteUtil.encode(value))
    }

    @Nullable
    fun decode(value: String): Any? {
        return ByteUtil.decode(Base64Util.decode(value))
    }

}